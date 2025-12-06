package com.agrosense.service;

import com.agrosense.model.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;

public class ToonPersistenceService {

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + "/agrosense_data.toon";

    public void exportarDatos(GestorLotes gestorLotes, AlertaService alertaService) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Export Lotes
        for (Lote lote : gestorLotes.obtenerTodos()) {
            sb.append("LOTE {\n");
            sb.append("  ID: ").append(lote.getId()).append("\n");
            sb.append("  NOMBRE: \"").append(lote.getNombre()).append("\"\n");
            sb.append("  CULTIVO: \"").append(lote.getTipoCultivo()).append("\"\n");
            sb.append("  AREA: ").append(lote.getArea()).append("\n");

            if (!lote.getSensores().isEmpty()) {
                sb.append("  SENSORES {\n");
                for (Sensor sensor : lote.getSensores()) {
                    sb.append("    SENSOR {\n");
                    sb.append("      ID: ").append(sensor.getId()).append("\n");
                    sb.append("      TIPO: ").append(sensor.getTipo()).append("\n");
                    sb.append("      UBICACION: \"").append(sensor.getUbicacion()).append("\"\n");
                    sb.append("    }\n");
                }
                sb.append("  }\n");
            }
            sb.append("}\n");
        }

        // Export Alertas
        for (Alerta alerta : alertaService.getHistorialAlertas()) {
            sb.append("ALERTA {\n");
            sb.append("  NIVEL: ").append(alerta.getNivel()).append("\n");
            sb.append("  MENSAJE: \"").append(alerta.getMensaje()).append("\"\n");
            sb.append("  FECHA: ").append(alerta.getFechaHora()).append("\n");
            sb.append("  LOTE: ").append(alerta.getLoteId()).append("\n");
            sb.append("}\n");
        }

        Files.createDirectories(Paths.get(DATA_DIR));
        Files.writeString(Paths.get(DATA_FILE), sb.toString());
    }

    public DataPersistenceService.AgroSenseData importarDatos() throws IOException {
        if (!Files.exists(Paths.get(DATA_FILE))) {
            throw new FileNotFoundException("No se encontró el archivo TOON");
        }

        String content = Files.readString(Paths.get(DATA_FILE));
        DataPersistenceService.AgroSenseData data = new DataPersistenceService.AgroSenseData();
        data.lotes = new ArrayList<>();
        data.alertas = new ArrayList<>();

        // Parse Lotes
        Pattern lotePattern = Pattern.compile("LOTE \\{(.*?)\\}", Pattern.DOTALL);
        Matcher loteMatcher = lotePattern.matcher(content);

        while (loteMatcher.find()) {
            String loteBlock = loteMatcher.group(1);
            String id = extractValue(loteBlock, "ID");
            String nombre = extractValue(loteBlock, "NOMBRE");
            String cultivo = extractValue(loteBlock, "CULTIVO");
            double area = Double.parseDouble(extractValue(loteBlock, "AREA"));

            Lote lote = new Lote(id, nombre, cultivo, area);

            // Parse Sensors within Lote
            Pattern sensorPattern = Pattern.compile("SENSOR \\{(.*?)\\}", Pattern.DOTALL);
            Matcher sensorMatcher = sensorPattern.matcher(loteBlock);

            while (sensorMatcher.find()) {
                String sensorBlock = sensorMatcher.group(1);
                String sId = extractValue(sensorBlock, "ID");
                String sTipo = extractValue(sensorBlock, "TIPO");
                String sUbicacion = extractValue(sensorBlock, "UBICACION");

                if ("HUMEDAD".equals(sTipo)) {
                    lote.agregarSensor(new SensorHumedad(sId, sUbicacion));
                } else {
                    lote.agregarSensor(new SensorTemperatura(sId, sUbicacion));
                }
            }
            data.lotes.add(lote);
        }

        // Parse Alertas
        Pattern alertaPattern = Pattern.compile("ALERTA \\{(.*?)\\}", Pattern.DOTALL);
        Matcher alertaMatcher = alertaPattern.matcher(content);

        while (alertaMatcher.find()) {
            String alertaBlock = alertaMatcher.group(1);
            String nivelStr = extractValue(alertaBlock, "NIVEL");
            String mensaje = extractValue(alertaBlock, "MENSAJE");
            String fechaStr = extractValue(alertaBlock, "FECHA");
            String loteId = extractValue(alertaBlock, "LOTE");

            Alerta.Nivel nivel = Alerta.Nivel.valueOf(nivelStr);
            LocalDateTime fecha = LocalDateTime.parse(fechaStr);

            data.alertas.add(new Alerta(mensaje, nivel, loteId, fecha));
            // Note: Alerta constructor sets date to now, we might need a setter or
            // constructor that accepts date
            // For now, we accept the new date or modify Alerta class.
            // Let's assume for this exercise we just load them as new alerts or modify
            // Alerta later if needed.
        }

        return data;
    }

    private String extractValue(String block, String key) {
        Pattern p = Pattern.compile(key + ":\\s*\"?(.*?)\"?\\s*(\\n|$|\\r)");
        Matcher m = p.matcher(block);
        if (m.find()) {
            return m.group(1).trim();
        }
        return "";
    }
}
