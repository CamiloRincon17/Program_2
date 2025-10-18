package agrosense.model;

import agrosense.sensors.SensorHumedadSimple;
import agrosense.sensors.SensorTemperaturaSimple;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un lote de cultivo en el sistema AgroSense (version simplificada).
 * Cada lote contiene informacion del cultivo y sensores asociados.
 */
public class LoteSimple {
    private String id;
    private String nombre;
    private double area; // en metros cuadrados
    private String tipoCultivo;
    private Date fechaSiembra;
    private String estadoGeneral;
    private SensorHumedadSimple sensorHumedad;
    private SensorTemperaturaSimple sensorTemperatura;
    private List<String> alertas;
    private List<String> recomendaciones;

    /**
     * Constructor del lote
     * @param id Identificador unico del lote
     * @param nombre Nombre descriptivo del lote
     * @param area Area del lote en metros cuadrados
     * @param tipoCultivo Tipo de cultivo sembrado
     */
    public LoteSimple(String id, String nombre, double area, String tipoCultivo) {
        this.id = id;
        this.nombre = nombre;
        this.area = area;
        this.tipoCultivo = tipoCultivo;
        this.fechaSiembra = new Date();
        this.estadoGeneral = "NUEVO";
        this.alertas = new ArrayList<>();
        this.recomendaciones = new ArrayList<>();
        
        // Crear sensores especificos para este lote
        this.sensorHumedad = new SensorHumedadSimple("HUM_" + id, "Humedad " + nombre);
        this.sensorTemperatura = new SensorTemperaturaSimple("TEMP_" + id, "Temperatura " + nombre);
    }

    /**
     * Realiza una lectura completa de todos los sensores del lote
     */
    public void realizarLectura() {
        if (sensorHumedad != null && sensorHumedad.isActivo()) {
            sensorHumedad.leer();
        }
        if (sensorTemperatura != null && sensorTemperatura.isActivo()) {
            sensorTemperatura.leer();
        }
        
        actualizarEstadoGeneral();
        generarAlertas();
    }

    /**
     * Actualiza el estado general del lote basado en las lecturas de los sensores
     */
    private void actualizarEstadoGeneral() {
        boolean humedadOptima = sensorHumedad != null && 
                               sensorHumedad.esValorNormal(sensorHumedad.getUltimaLectura());
        boolean temperaturaOptima = sensorTemperatura != null && 
                                   sensorTemperatura.esValorNormal(sensorTemperatura.getUltimaLectura());

        if (humedadOptima && temperaturaOptima) {
            estadoGeneral = "OPTIMO";
        } else if (sensorHumedad != null && sensorHumedad.requiereRiegoUrgente(sensorHumedad.getUltimaLectura()) ||
                   sensorTemperatura != null && sensorTemperatura.hayRiesgoHeladas(sensorTemperatura.getUltimaLectura()) ||
                   sensorTemperatura != null && sensorTemperatura.hayRiesgoCalor(sensorTemperatura.getUltimaLectura())) {
            estadoGeneral = "CRITICO";
        } else {
            estadoGeneral = "ATENCION";
        }
    }

    /**
     * Genera alertas basadas en las condiciones actuales del lote
     */
    private void generarAlertas() {
        alertas.clear();

        if (sensorHumedad != null && sensorHumedad.isActivo()) {
            double humedad = sensorHumedad.getUltimaLectura();
            if (sensorHumedad.requiereRiegoUrgente(humedad)) {
                alertas.add("Riego urgente requerido - Suelo muy seco (" + String.format("%.1f%%", humedad) + ")");
            } else if (sensorHumedad.hayRiesgoEncharcamiento(humedad)) {
                alertas.add("Riesgo de encharcamiento - Exceso de humedad (" + String.format("%.1f%%", humedad) + ")");
            }
        }

        if (sensorTemperatura != null && sensorTemperatura.isActivo()) {
            double temperatura = sensorTemperatura.getUltimaLectura();
            if (sensorTemperatura.hayRiesgoHeladas(temperatura)) {
                alertas.add("Riesgo de heladas - Temperatura muy baja (" + String.format("%.1f°C", temperatura) + ")");
            } else if (sensorTemperatura.hayRiesgoCalor(temperatura)) {
                alertas.add("Riesgo de estres por calor - Temperatura muy alta (" + String.format("%.1f°C", temperatura) + ")");
            }
        }
    }

    /**
     * Obtiene un resumen completo del estado del lote
     * @return String con informacion detallada del lote
     */
    public String obtenerResumen() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== LOTE: ").append(nombre).append(" ===\n");
        resumen.append("ID: ").append(id).append("\n");
        resumen.append("Area: ").append(String.format("%.2f", area)).append(" m2\n");
        resumen.append("Cultivo: ").append(tipoCultivo).append("\n");
        resumen.append("Estado General: ").append(estadoGeneral).append("\n");
        resumen.append("Fecha de Siembra: ").append(fechaSiembra.toString()).append("\n\n");

        // Informacion de humedad
        if (sensorHumedad != null && sensorHumedad.isActivo()) {
            double humedad = sensorHumedad.getUltimaLectura();
            resumen.append("HUMEDAD: ").append(String.format("%.1f%%", humedad)).append("\n");
            resumen.append("   Estado: ").append(sensorHumedad.obtenerEstadoHumedad(humedad)).append("\n");
            resumen.append("   ").append(sensorHumedad.obtenerRangoNormal()).append("\n");
            
            double riegoRecomendado = sensorHumedad.calcularRiegoRecomendado(humedad);
            if (riegoRecomendado > 0) {
                resumen.append("   Riego recomendado: ").append(String.format("%.1f L/m2", riegoRecomendado)).append("\n");
            }
        }

        // Informacion de temperatura
        if (sensorTemperatura != null && sensorTemperatura.isActivo()) {
            double temperatura = sensorTemperatura.getUltimaLectura();
            resumen.append("\nTEMPERATURA: ").append(String.format("%.1f°C", temperatura)).append("\n");
            resumen.append("   Estado: ").append(sensorTemperatura.obtenerEstadoTemperatura(temperatura)).append("\n");
            resumen.append("   ").append(sensorTemperatura.obtenerRangoNormal()).append("\n");
            
            double indiceEstres = sensorTemperatura.calcularIndiceEstres(temperatura);
            if (indiceEstres > 0) {
                resumen.append("   Indice de estres: ").append(String.format("%.0f%%", indiceEstres)).append("\n");
            }
        }

        // Alertas
        if (!alertas.isEmpty()) {
            resumen.append("\nALERTAS:\n");
            for (String alerta : alertas) {
                resumen.append("   ").append(alerta).append("\n");
            }
        }

        return resumen.toString();
    }

    /**
     * Obtiene las recomendaciones especificas para este lote
     * @return Lista de recomendaciones
     */
    public List<String> obtenerRecomendaciones() {
        recomendaciones.clear();

        if (sensorHumedad != null && sensorHumedad.isActivo()) {
            double humedad = sensorHumedad.getUltimaLectura();
            if (sensorHumedad.requiereRiegoUrgente(humedad)) {
                recomendaciones.add("Regar inmediatamente con " + 
                                  String.format("%.1f L/m2", sensorHumedad.calcularRiegoRecomendado(humedad)));
            } else if (sensorHumedad.hayRiesgoEncharcamiento(humedad)) {
                recomendaciones.add("Evitar riego adicional y mejorar drenaje");
            }
        }

        if (sensorTemperatura != null && sensorTemperatura.isActivo()) {
            double temperatura = sensorTemperatura.getUltimaLectura();
            recomendaciones.add(sensorTemperatura.obtenerRecomendacionTemperatura(temperatura));
        }

        if (recomendaciones.isEmpty()) {
            recomendaciones.add("Condiciones optimas - Continuar monitoreo regular");
        }

        return recomendaciones;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getArea() {
        return area;
    }

    public String getTipoCultivo() {
        return tipoCultivo;
    }

    public Date getFechaSiembra() {
        return fechaSiembra;
    }

    public String getEstadoGeneral() {
        return estadoGeneral;
    }

    public SensorHumedadSimple getSensorHumedad() {
        return sensorHumedad;
    }

    public SensorTemperaturaSimple getSensorTemperatura() {
        return sensorTemperatura;
    }

    public List<String> getAlertas() {
        return new ArrayList<>(alertas);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setTipoCultivo(String tipoCultivo) {
        this.tipoCultivo = tipoCultivo;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s - Estado: %s", nombre, id, tipoCultivo, estadoGeneral);
    }
}
