package agrosense.sensors;

import java.util.Random;

/**
 * Sensor de humedad del suelo para el sistema AgroSense.
 * Simula la medicion de humedad en porcentaje (0-100%).
 */
public class SensorHumedadSimple extends Sensor {
    private static final double HUMEDAD_MINIMA = 30.0;  // % - Umbral critico inferior
    private static final double HUMEDAD_MAXIMA = 80.0;  // % - Umbral critico superior
    private static final double HUMEDAD_OPTIMA_MIN = 45.0; // % - Rango optimo
    private static final double HUMEDAD_OPTIMA_MAX = 65.0; // % - Rango optimo
    
    private Random random;

    /**
     * Constructor del sensor de humedad
     * @param id Identificador unico del sensor
     * @param nombre Nombre descriptivo del sensor
     */
    public SensorHumedadSimple(String id, String nombre) {
        super(id, nombre);
        this.random = new Random();
    }

    /**
     * Simula la lectura de humedad del suelo
     * @return Valor de humedad en porcentaje (0-100)
     */
    @Override
    public double leer() {
        if (!activo) {
            return -1; // Sensor inactivo
        }

        // Simulacion realista: la humedad tiende a estar en rangos especificos
        double humedad;
        int tipoCondicion = random.nextInt(10); // 0-9
        
        switch (tipoCondicion) {
            case 0: // 10% - Condicion critica (muy seca)
                humedad = random.nextDouble() * 25 + 5; // 5-30%
                break;
            case 1: // 10% - Condicion critica (muy humeda)
                humedad = random.nextDouble() * 20 + 80; // 80-100%
                break;
            case 2:
            case 3: // 20% - Condicion suboptima (seca)
                humedad = random.nextDouble() * 15 + 30; // 30-45%
                break;
            case 4:
            case 5: // 20% - Condicion suboptima (humeda)
                humedad = random.nextDouble() * 15 + 65; // 65-80%
                break;
            default: // 40% - Condicion optima
                humedad = random.nextDouble() * 20 + 45; // 45-65%
                break;
        }

        actualizarLectura(humedad);
        return humedad;
    }

    /**
     * Verifica si el valor de humedad esta dentro del rango normal
     * @param valor Valor de humedad a verificar
     * @return true si esta en rango optimo, false si esta en rango critico o suboptimo
     */
    @Override
    public boolean esValorNormal(double valor) {
        return valor >= HUMEDAD_OPTIMA_MIN && valor <= HUMEDAD_OPTIMA_MAX;
    }

    /**
     * Determina el estado de la humedad
     * @param valor Valor de humedad
     * @return Descripcion del estado
     */
    public String obtenerEstadoHumedad(double valor) {
        if (valor < HUMEDAD_MINIMA) {
            return "CRITICO - Suelo muy seco";
        } else if (valor > HUMEDAD_MAXIMA) {
            return "CRITICO - Exceso de humedad";
        } else if (valor < HUMEDAD_OPTIMA_MIN) {
            return "ATENCION - Humedad baja";
        } else if (valor > HUMEDAD_OPTIMA_MAX) {
            return "ATENCION - Humedad alta";
        } else {
            return "OPTIMO - Humedad adecuada";
        }
    }

    /**
     * Verifica si se requiere riego urgente
     * @param valor Valor de humedad actual
     * @return true si se requiere riego urgente
     */
    public boolean requiereRiegoUrgente(double valor) {
        return valor < HUMEDAD_MINIMA;
    }

    /**
     * Verifica si hay riesgo de encharcamiento
     * @param valor Valor de humedad actual
     * @return true si hay riesgo de encharcamiento
     */
    public boolean hayRiesgoEncharcamiento(double valor) {
        return valor > HUMEDAD_MAXIMA;
    }

    /**
     * Obtiene la descripcion del rango normal
     * @return String con el rango normal de humedad
     */
    @Override
    public String obtenerRangoNormal() {
        return String.format("Rango optimo: %.1f%% - %.1f%%", HUMEDAD_OPTIMA_MIN, HUMEDAD_OPTIMA_MAX);
    }

    /**
     * Calcula la cantidad de riego recomendada
     * @param valor Valor de humedad actual
     * @return Cantidad de agua recomendada en litros por metro cuadrado
     */
    public double calcularRiegoRecomendado(double valor) {
        if (valor >= HUMEDAD_OPTIMA_MIN) {
            return 0; // No requiere riego
        } else if (valor < HUMEDAD_MINIMA) {
            return 15; // Riego urgente - cantidad alta
        } else {
            return 8; // Riego moderado
        }
    }
}
