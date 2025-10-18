package agrosense.recommendations;

/**
 * Clase que representa una recomendacion generada por el motor de recomendaciones (version simplificada).
 * Cada recomendacion contiene informacion especifica sobre acciones a tomar.
 */
public class RecomendacionSimple {
    private String titulo;
    private String descripcion;
    private TipoRecomendacionSimple tipo;
    private int prioridad;
    private String accion;
    private String categoria;

    /**
     * Tipos de recomendacion disponibles
     */
    public enum TipoRecomendacionSimple {
        CRITICA("CRITICA", "Critica"),
        ATENCION("ATENCION", "Atencion"),
        INFORMATIVA("INFORMATIVA", "Informativa");

        private final String codigo;
        private final String descripcion;

        TipoRecomendacionSimple(String codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Constructor de la recomendacion
     * @param titulo Titulo de la recomendacion
     * @param descripcion Descripcion detallada
     * @param tipo Tipo de recomendacion
     * @param prioridad Prioridad (1 = maxima, 3 = minima)
     * @param accion Accion especifica a realizar
     * @param categoria Categoria de la recomendacion
     */
    public RecomendacionSimple(String titulo, String descripcion, TipoRecomendacionSimple tipo, 
                        int prioridad, String accion, String categoria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.accion = accion;
        this.categoria = categoria;
    }

    /**
     * Constructor simplificado
     * @param titulo Titulo de la recomendacion
     * @param descripcion Descripcion detallada
     * @param tipo Tipo de recomendacion
     * @param prioridad Prioridad (1 = maxima, 3 = minima)
     * @param accion Accion especifica a realizar
     */
    public RecomendacionSimple(String titulo, String descripcion, TipoRecomendacionSimple tipo, 
                        int prioridad, String accion) {
        this(titulo, descripcion, tipo, prioridad, accion, tipo.getCodigo());
    }

    /**
     * Verifica si la recomendacion es critica
     * @return true si es critica
     */
    public boolean esCritica() {
        return tipo == TipoRecomendacionSimple.CRITICA;
    }

    /**
     * Verifica si la recomendacion requiere atencion inmediata
     * @return true si requiere atencion inmediata
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
                return "ALTA - Accion inmediata requerida";
            case 2:
                return "MEDIA - Accion en las proximas horas";
            case 3:
            default:
                return "BAJA - Accion cuando sea conveniente";
        }
    }

    /**
     * Obtiene una representacion visual de la recomendacion
     * @return String formateado con la recomendacion
     */
    public String obtenerVistaCompleta() {
        StringBuilder vista = new StringBuilder();
        vista.append("+------------------------------------------+\n");
        vista.append(String.format("| %-40s |\n", titulo));
        vista.append("+------------------------------------------+\n");
        vista.append(String.format("| Tipo: %-32s |\n", tipo.getDescripcion()));
        vista.append(String.format("| Prioridad: %-27s |\n", obtenerNivelUrgencia()));
        vista.append("+------------------------------------------+\n");
        vista.append(String.format("| %-40s |\n", "Descripcion:"));
        vista.append(String.format("| %-40s |\n", descripcion));
        vista.append("+------------------------------------------+\n");
        vista.append(String.format("| %-40s |\n", "Accion a realizar:"));
        vista.append(String.format("| %-40s |\n", accion));
        vista.append("+------------------------------------------+");
        
        return vista.toString();
    }

    /**
     * Obtiene una representacion resumida de la recomendacion
     * @return String resumido
     */
    public String obtenerVistaResumida() {
        return String.format("%s %s - %s", categoria, tipo.getDescripcion(), titulo);
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoRecomendacionSimple getTipo() {
        return tipo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getAccion() {
        return accion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipo(TipoRecomendacionSimple tipo) {
        this.tipo = tipo;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return obtenerVistaResumida();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RecomendacionSimple that = (RecomendacionSimple) obj;
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
