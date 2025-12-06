package com.agrosense.model;

import java.util.ArrayList;
import java.util.List;

public class Lote {
    private String id;
    private String nombre;
    private String tipoCultivo;
    private double area; // en hectáreas o m2
    private List<Sensor> sensores;

    public Lote(String id, String nombre, String tipoCultivo, double area) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCultivo = tipoCultivo;
        this.area = area;
        this.sensores = new ArrayList<>();
    }

    public void agregarSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoCultivo() {
        return tipoCultivo;
    }

    public double getArea() {
        return area;
    }

    @Override
    public String toString() {
        return "Lote{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoCultivo='" + tipoCultivo + '\'' +
                ", area=" + area +
                ", sensores=" + sensores.size() +
                '}';
    }
}
