package agrosense.recommendations;

/**
 * Clase que representa una recomendación generada por el motor de recomendaciones.
 * Cada recomendación contiene información específica sobre acciones a tomar.
 */
public class Recomendacion {
    private String titulo;
    private String descripcion;
    private TipoRecomendacion tipo;
    private int prioridad;
    private String accion;
    private String icono;

    /**
     * Tipos de recomendación disponibles
     */
    public enum TipoRecomendacion {
        CRITICA("🚨", "Crítica"),
        ATENCION("⚠️", "Atención"),
        INFORMATIVA("ℹ️", "Informativa");

        private final String icono;
        private final String descripcion;

        TipoRecomendacion(String icono, String descripcion) {
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
     * Constructor de la recomendación
     * @param titulo Título de la recomendación
     * @param descripcion Descripción detallada
     * @param tipo Tipo de recomendación
     * @param prioridad Prioridad (1 = máxima, 3 = mínima)
     * @param accion Acción específica a realizar
     * @param icono Icono representativo
     */
    public Recomendacion(String titulo, String descripcion, TipoRecomendacion tipo, 
                        int prioridad, String accion, String icono) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.accion = accion;
        this.icono = icono;
    }

    /**
     * Constructor simplificado
     * @param titulo Título de la recomendación
     * @param descripcion Descripción detallada
     * @param tipo Tipo de recomendación
     * @param prioridad Prioridad (1 = máxima, 3 = mínima)
     * @param accion Acción específica a realizar
     */
    public Recomendacion(String titulo, String descripcion, TipoRecomendacion tipo, 
                        int prioridad, String accion) {
        this(titulo, descripcion, tipo, prioridad, accion, tipo.getIcono());
    }

    /**
     * Verifica si la recomendación es crítica
     * @return true si es crítica
     */
    public boolean esCritica() {
        return tipo == TipoRecomendacion.CRITICA;
    }

    /**
     * Verifica si la recomendación requiere atención inmediata
     * @return true si requiere atención inmediata
     */
    public boolean requiereAtencionInmediata() {
        return prioridad == 1;
    }

    /**
     * Obtiene el nivel de urgencia en texto
     * @return String con el nivel de urgencia
     */
    public String obtenerNivelUrgencia() {
        switch (prioridad) {
            case 1:
                return "ALTA - Acción inmediata requerida";
            case 2:
                return "MEDIA - Acción en las próximas horas";
            case 3:
            default:
                return "BAJA - Acción cuando sea conveniente";
        }
    }

    /**
     * Obtiene una representación visual de la recomendación
     * @return String formateado con la recomendación
     */
    public String obtenerVistaCompleta() {
        StringBuilder vista = new StringBuilder();
        vista.append("┌─────────────────────────────────────────┐\n");
        vista.append(String.format("│ %s %-35s │\n", icono, titulo));
        vista.append("├─────────────────────────────────────────┤\n");
        vista.append(String.format("│ Tipo: %-32s │\n", tipo.getDescripcion()));
        vista.append(String.format("│ Prioridad: %-27s │\n", obtenerNivelUrgencia()));
        vista.append("├─────────────────────────────────────────┤\n");
        vista.append(String.format("│ %-37s │\n", "Descripción:"));
        vista.append(String.format("│ %-37s │\n", descripcion));
        vista.append("├─────────────────────────────────────────┤\n");
        vista.append(String.format("│ %-37s │\n", "Acción a realizar:"));
        vista.append(String.format("│ %-37s │\n", accion));
        vista.append("└─────────────────────────────────────────┘");
        
        return vista.toString();
    }

    /**
     * Obtiene una representación resumida de la recomendación
     * @return String resumido
     */
    public String obtenerVistaResumida() {
        return String.format("%s %s - %s", icono, tipo.getDescripcion(), titulo);
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoRecomendacion getTipo() {
        return tipo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getAccion() {
        return accion;
    }

    public String getIcono() {
        return icono;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipo(TipoRecomendacion tipo) {
        this.tipo = tipo;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    @Override
    public String toString() {
        return obtenerVistaResumida();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recomendacion that = (Recomendacion) obj;
        return prioridad == that.prioridad &&
               titulo.equals(that.titulo) &&
               descripcion.equals(that.descripcion) &&
               tipo == that.tipo;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(titulo, descripcion, tipo, prioridad);
    }
}
