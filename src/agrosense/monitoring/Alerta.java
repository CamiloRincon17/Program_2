package agrosense.monitoring;

import java.util.Date;

/**
 * Clase que representa una alerta en el sistema AgroSense.
 * Las alertas se generan cuando se detectan condiciones críticas en los lotes.
 */
public class Alerta {
    private String idLote;
    private String nombreLote;
    private String mensaje;
    private TipoAlerta tipo;
    private Date fechaHora;
    private boolean leida;
    private String recomendacion;

    /**
     * Tipos de alerta disponibles
     */
    public enum TipoAlerta {
        INFORMATIVA("ℹ️", "Informativa"),
        ATENCION("⚠️", "Atención"),
        CRITICA("🚨", "Crítica");

        private final String icono;
        private final String descripcion;

        TipoAlerta(String icono, String descripcion) {
            this.icono = icono;
            this.descripcion = descripcion;
        }

        public String getIcono() {
            return icono;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Constructor de la alerta
     * @param idLote ID del lote que generó la alerta
     * @param nombreLote Nombre del lote
     * @param mensaje Mensaje descriptivo de la alerta
     * @param tipo Tipo de alerta
     * @param fechaHora Fecha y hora de la alerta
     */
    public Alerta(String idLote, String nombreLote, String mensaje, TipoAlerta tipo, Date fechaHora) {
        this.idLote = idLote;
        this.nombreLote = nombreLote;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
        this.leida = false;
        this.recomendacion = generarRecomendacionAutomatica();
    }

    /**
     * Constructor con recomendación personalizada
     * @param idLote ID del lote que generó la alerta
     * @param nombreLote Nombre del lote
     * @param mensaje Mensaje descriptivo de la alerta
     * @param tipo Tipo de alerta
     * @param fechaHora Fecha y hora de la alerta
     * @param recomendacion Recomendación específica
     */
    public Alerta(String idLote, String nombreLote, String mensaje, TipoAlerta tipo, 
                  Date fechaHora, String recomendacion) {
        this.idLote = idLote;
        this.nombreLote = nombreLote;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
        this.leida = false;
        this.recomendacion = recomendacion != null ? recomendacion : generarRecomendacionAutomatica();
    }

    /**
     * Genera una recomendación automática basada en el tipo de alerta
     * @return Recomendación generada
     */
    private String generarRecomendacionAutomatica() {
        if (mensaje.contains("Riego urgente") || mensaje.contains("muy seco")) {
            return "Regar inmediatamente con 15 L/m² y verificar sistema de riego";
        } else if (mensaje.contains("encharcamiento") || mensaje.contains("Exceso de humedad")) {
            return "Suspender riego y mejorar drenaje del lote";
        } else if (mensaje.contains("heladas") || mensaje.contains("Temperatura muy baja")) {
            return "Cubrir cultivos y aplicar riego de protección contra heladas";
        } else if (mensaje.contains("calor") || mensaje.contains("Temperatura muy alta")) {
            return "Aumentar frecuencia de riego y proporcionar sombra";
        } else {
            return "Monitorear de cerca y tomar medidas preventivas";
        }
    }

    /**
     * Marca la alerta como leída
     */
    public void marcarComoLeida() {
        this.leida = true;
    }

    /**
     * Verifica si la alerta es crítica
     * @return true si es crítica
     */
    public boolean esCritica() {
        return tipo == TipoAlerta.CRITICA;
    }

    /**
     * Obtiene el tiempo transcurrido desde que se generó la alerta
     * @return Tiempo en milisegundos
     */
    public long getTiempoTranscurrido() {
        return System.currentTimeMillis() - fechaHora.getTime();
    }

    /**
     * Obtiene el tiempo transcurrido en formato legible
     * @return String con el tiempo transcurrido
     */
    public String getTiempoTranscurridoFormateado() {
        long tiempo = getTiempoTranscurrido();
        long segundos = tiempo / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        long dias = horas / 24;

        if (dias > 0) {
            return dias + " día(s)";
        } else if (horas > 0) {
            return horas + " hora(s)";
        } else if (minutos > 0) {
            return minutos + " minuto(s)";
        } else {
            return segundos + " segundo(s)";
        }
    }

    /**
     * Obtiene la prioridad de la alerta (1 = máxima, 3 = mínima)
     * @return Prioridad numérica
     */
    public int getPrioridad() {
        switch (tipo) {
            case CRITICA:
                return 1;
            case ATENCION:
                return 2;
            case INFORMATIVA:
            default:
                return 3;
        }
    }

    /**
     * Obtiene una representación visual de la alerta
     * @return String formateado con la alerta
     */
    public String obtenerVistaCompleta() {
        StringBuilder vista = new StringBuilder();
        vista.append(tipo.getIcono()).append(" ALERTA ").append(tipo.getDescripcion().toUpperCase()).append("\n");
        vista.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        vista.append("🌱 Lote: ").append(nombreLote).append(" (").append(idLote).append(")\n");
        vista.append("📅 Fecha: ").append(fechaHora.toString()).append("\n");
        vista.append("⏰ Hace: ").append(getTiempoTranscurridoFormateado()).append("\n");
        vista.append("📝 Mensaje: ").append(mensaje).append("\n");
        vista.append("💡 Recomendación: ").append(recomendacion).append("\n");
        vista.append("📖 Estado: ").append(leida ? "✅ Leída" : "🔴 Sin leer").append("\n");
        
        return vista.toString();
    }

    /**
     * Obtiene una representación resumida de la alerta
     * @return String resumido
     */
    public String obtenerVistaResumida() {
        return String.format("%s %s - %s (%s) [%s]", 
                           tipo.getIcono(), 
                           tipo.getDescripcion(), 
                           nombreLote, 
                           getTiempoTranscurridoFormateado(),
                           leida ? "Leída" : "Sin leer");
    }

    // Getters y Setters
    public String getIdLote() {
        return idLote;
    }

    public String getNombreLote() {
        return nombreLote;
    }

    public String getMensaje() {
        return mensaje;
    }

    public TipoAlerta getTipo() {
        return tipo;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public boolean isLeida() {
        return leida;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    @Override
    public String toString() {
        return obtenerVistaResumida();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Alerta alerta = (Alerta) obj;
        return idLote.equals(alerta.idLote) && 
               mensaje.equals(alerta.mensaje) && 
               fechaHora.equals(alerta.fechaHora);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idLote, mensaje, fechaHora);
    }
}
