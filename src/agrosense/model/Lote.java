package agrosense.model;

import agrosense.sensors.SensorHumedad;
import agrosense.sensors.SensorTemperatura;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un lote de cultivo en el sistema AgroSense.
 * Cada lote contiene información del cultivo y sensores asociados.
 */
public class Lote {
    private String id;
    private String nombre;
    private double area; // en metros cuadrados
    private String tipoCultivo;
    private Date fechaSiembra;
    private String estadoGeneral;
    private SensorHumedad sensorHumedad;
    private SensorTemperatura sensorTemperatura;
    private List<String> alertas;
    private List<String> recomendaciones;

    /**
     * Constructor del lote
     * @param id Identificador único del lote
     * @param nombre Nombre descriptivo del lote
     * @param area Área del lote en metros cuadrados
     * @param tipoCultivo Tipo de cultivo sembrado
     */
    public Lote(String id, String nombre, double area, String tipoCultivo) {
        this.id = id;
        this.nombre = nombre;
        this.area = area;
        this.tipoCultivo = tipoCultivo;
        this.fechaSiembra = new Date();
        this.estadoGeneral = "NUEVO";
        this.alertas = new ArrayList<>();
        this.recomendaciones = new ArrayList<>();
        
        // Crear sensores específicos para este lote
        this.sensorHumedad = new SensorHumedad("HUM_" + id, "Humedad " + nombre);
        this.sensorTemperatura = new SensorTemperatura("TEMP_" + id, "Temperatura " + nombre);
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
            estadoGeneral = "ÓPTIMO";
        } else if (sensorHumedad != null && sensorHumedad.requiereRiegoUrgente(sensorHumedad.getUltimaLectura()) ||
                   sensorTemperatura != null && sensorTemperatura.hayRiesgoHeladas(sensorTemperatura.getUltimaLectura()) ||
                   sensorTemperatura != null && sensorTemperatura.hayRiesgoCalor(sensorTemperatura.getUltimaLectura())) {
            estadoGeneral = "CRÍTICO";
        } else {
            estadoGeneral = "ATENCIÓN";
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
                alertas.add("🚨 Riego urgente requerido - Suelo muy seco (" + String.format("%.1f%%", humedad) + ")");
            } else if (sensorHumedad.hayRiesgoEncharcamiento(humedad)) {
                alertas.add("⚠️ Riesgo de encharcamiento - Exceso de humedad (" + String.format("%.1f%%", humedad) + ")");
            }
        }

        if (sensorTemperatura != null && sensorTemperatura.isActivo()) {
            double temperatura = sensorTemperatura.getUltimaLectura();
            if (sensorTemperatura.hayRiesgoHeladas(temperatura)) {
                alertas.add("🧊 Riesgo de heladas - Temperatura muy baja (" + String.format("%.1f°C", temperatura) + ")");
            } else if (sensorTemperatura.hayRiesgoCalor(temperatura)) {
                alertas.add("🌡️ Riesgo de estrés por calor - Temperatura muy alta (" + String.format("%.1f°C", temperatura) + ")");
            }
        }
    }

    /**
     * Obtiene un resumen completo del estado del lote
     * @return String con información detallada del lote
     */
    public String obtenerResumen() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== LOTE: ").append(nombre).append(" ===\n");
        resumen.append("ID: ").append(id).append("\n");
        resumen.append("Área: ").append(String.format("%.2f", area)).append(" m²\n");
        resumen.append("Cultivo: ").append(tipoCultivo).append("\n");
        resumen.append("Estado General: ").append(estadoGeneral).append("\n");
        resumen.append("Fecha de Siembra: ").append(fechaSiembra.toString()).append("\n\n");

        // Información de humedad
        if (sensorHumedad != null && sensorHumedad.isActivo()) {
            double humedad = sensorHumedad.getUltimaLectura();
            resumen.append("🌧️ HUMEDAD: ").append(String.format("%.1f%%", humedad)).append("\n");
            resumen.append("   Estado: ").append(sensorHumedad.obtenerEstadoHumedad(humedad)).append("\n");
            resumen.append("   ").append(sensorHumedad.obtenerRangoNormal()).append("\n");
            
            double riegoRecomendado = sensorHumedad.calcularRiegoRecomendado(humedad);
            if (riegoRecomendado > 0) {
                resumen.append("   💧 Riego recomendado: ").append(String.format("%.1f L/m²", riegoRecomendado)).append("\n");
            }
        }

        // Información de temperatura
        if (sensorTemperatura != null && sensorTemperatura.isActivo()) {
            double temperatura = sensorTemperatura.getUltimaLectura();
            resumen.append("\n🌡️ TEMPERATURA: ").append(String.format("%.1f°C", temperatura)).append("\n");
            resumen.append("   Estado: ").append(sensorTemperatura.obtenerEstadoTemperatura(temperatura)).append("\n");
            resumen.append("   ").append(sensorTemperatura.obtenerRangoNormal()).append("\n");
            
            double indiceEstres = sensorTemperatura.calcularIndiceEstres(temperatura);
            if (indiceEstres > 0) {
                resumen.append("   ⚠️ Índice de estrés: ").append(String.format("%.0f%%", indiceEstres)).append("\n");
            }
        }

        // Alertas
        if (!alertas.isEmpty()) {
            resumen.append("\n🚨 ALERTAS:\n");
            for (String alerta : alertas) {
                resumen.append("   ").append(alerta).append("\n");
            }
        }

        return resumen.toString();
    }

    /**
     * Obtiene las recomendaciones específicas para este lote
     * @return Lista de recomendaciones
     */
    public List<String> obtenerRecomendaciones() {
        recomendaciones.clear();

        if (sensorHumedad != null && sensorHumedad.isActivo()) {
            double humedad = sensorHumedad.getUltimaLectura();
            if (sensorHumedad.requiereRiegoUrgente(humedad)) {
                recomendaciones.add("Regar inmediatamente con " + 
                                  String.format("%.1f L/m²", sensorHumedad.calcularRiegoRecomendado(humedad)));
            } else if (sensorHumedad.hayRiesgoEncharcamiento(humedad)) {
                recomendaciones.add("Evitar riego adicional y mejorar drenaje");
            }
        }

        if (sensorTemperatura != null && sensorTemperatura.isActivo()) {
            double temperatura = sensorTemperatura.getUltimaLectura();
            recomendaciones.add(sensorTemperatura.obtenerRecomendacionTemperatura(temperatura));
        }

        if (recomendaciones.isEmpty()) {
            recomendaciones.add("Condiciones óptimas - Continuar monitoreo regular");
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

    public SensorHumedad getSensorHumedad() {
        return sensorHumedad;
    }

    public SensorTemperatura getSensorTemperatura() {
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
