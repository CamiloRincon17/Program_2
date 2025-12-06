package com.agrosense.service;

import com.agrosense.model.*;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class DataPersistenceService {

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + "/agrosense_data.json";
    private Gson gson;

    public DataPersistenceService() {
        // Custom GSON with adapters for LocalDateTime
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Sensor.class, new SensorAdapter())
                .setPrettyPrinting()
                .create();

        // Create data directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    public void exportarDatos(GestorLotes gestorLotes, AlertaService alertaService) throws IOException {
        AgroSenseData data = new AgroSenseData();
        data.lotes = gestorLotes.obtenerTodos();
        data.alertas = alertaService.getHistorialAlertas();

        String json = gson.toJson(data);

        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            writer.write(json);
        }
    }

    public AgroSenseData importarDatos() throws IOException {
        if (!Files.exists(Paths.get(DATA_FILE))) {
            throw new FileNotFoundException("No se encontró el archivo de datos");
        }

        String json = new String(Files.readAllBytes(Paths.get(DATA_FILE)));
        return gson.fromJson(json, AgroSenseData.class);
    }

    // Data container class
    public static class AgroSenseData {
        public List<Lote> lotes;
        public List<Alerta> alertas;
    }

    // Adapter for LocalDateTime
    private static class LocalDateTimeAdapter
            implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.toString());
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            return LocalDateTime.parse(json.getAsString());
        }
    }

    // Adapter for Sensor (handles polymorphism)
    private static class SensorAdapter implements JsonSerializer<Sensor>, JsonDeserializer<Sensor> {
        @Override
        public JsonElement serialize(Sensor sensor, Type type, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("tipo", sensor.getTipo());
            obj.addProperty("id", sensor.getId());
            obj.addProperty("ubicacion", sensor.getUbicacion());
            obj.addProperty("class", sensor.getClass().getSimpleName());
            return obj;
        }

        @Override
        public Sensor deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            JsonObject obj = json.getAsJsonObject();
            String id = obj.get("id").getAsString();
            String ubicacion = obj.get("ubicacion").getAsString();
            String tipo = obj.get("tipo").getAsString();

            if ("HUMEDAD".equals(tipo)) {
                return new SensorHumedad(id, ubicacion);
            } else {
                return new SensorTemperatura(id, ubicacion);
            }
        }
    }
}
