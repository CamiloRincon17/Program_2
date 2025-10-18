package agrosense.sensors;

/**
 * Clase abstracta base para todos los sensores del sistema AgroSense.
 * Define la interfaz común que deben implementar todos los sensores.
 */
public abstract class Sensor {
    protected String id;
    protected String nombre;
    protected boolean activo;
    protected double ultimaLectura;
    protected long ultimaActualizacion;

    /**
     * Constructor del sensor
     * @param id Identificador único del sensor
     * @param nombre Nombre descriptivo del sensor
     */
    public Sensor(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.activo = true;
        this.ultimaLectura = 0.0;
        this.ultimaActualizacion = System.currentTimeMillis();
    }

    /**
     * Método abstracto para realizar una lectura del sensor
     * @return Valor de la lectura actual
     */
    public abstract double leer();

    /**
     * Método abstracto para validar si una lectura está dentro del rango normal
     * @param valor Valor a validar
     * @return true si el valor está dentro del rango normal
     */
    public abstract boolean esValorNormal(double valor);

    /**
     * Método abstracto para obtener el rango normal del sensor
     * @return String con la descripción del rango normal
     */
    public abstract String obtenerRangoNormal();

    /**
     * Actualiza la última lectura del sensor
     * @param nuevaLectura Nuevo valor leído
     */
    protected void actualizarLectura(double nuevaLectura) {
        this.ultimaLectura = nuevaLectura;
        this.ultimaActualizacion = System.currentTimeMillis();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public double getUltimaLectura() {
        return ultimaLectura;
    }

    public long getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    /**
     * Obtiene información del estado del sensor
     * @return String con información detallada del sensor
     */
    public String obtenerEstado() {
        String estado = activo ? "Activo" : "Inactivo";
        return String.format("Sensor %s (%s): %s - Última lectura: %.2f - %s", 
                           nombre, id, estado, ultimaLectura, obtenerRangoNormal());
    }

    @Override
    public String toString() {
        return String.format("%s - %s", nombre, id);
    }
}
