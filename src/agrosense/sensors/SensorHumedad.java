package agrosense.sensors;

import java.util.Random;

/**
 * Sensor de humedad del suelo para el sistema AgroSense.
 * Simula la medición de humedad en porcentaje (0-100%).
 */
public class SensorHumedad extends Sensor {
    private static final double HUMEDAD_MINIMA = 30.0;  // % - Umbral crítico inferior
    private static final double HUMEDAD_MAXIMA = 80.0;  // % - Umbral crítico superior
    private static final double HUMEDAD_OPTIMA_MIN = 45.0; // % - Rango óptimo
    private static final double HUMEDAD_OPTIMA_MAX = 65.0; // % - Rango óptimo
    
    private Random random;

    /**
     * Constructor del sensor de humedad
     * @param id Identificador único del sensor
     * @param nombre Nombre descriptivo del sensor
     */
    public SensorHumedad(String id, String nombre) {
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

        // Simulación realista: la humedad tiende a estar en rangos específicos
        double humedad;
        int tipoCondicion = random.nextInt(10); // 0-9
        
        switch (tipoCondicion) {
            case 0: // 10% - Condición crítica (muy seca)
                humedad = random.nextDouble() * 25 + 5; // 5-30%
                break;
            case 1: // 10% - Condición crítica (muy húmeda)
                humedad = random.nextDouble() * 20 + 80; // 80-100%
                break;
            case 2:
            case 3: // 20% - Condición subóptima (seca)
                humedad = random.nextDouble() * 15 + 30; // 30-45%
                break;
            case 4:
            case 5: // 20% - Condición subóptima (húmeda)
                humedad = random.nextDouble() * 15 + 65; // 65-80%
                break;
            default: // 40% - Condición óptima
                humedad = random.nextDouble() * 20 + 45; // 45-65%
                break;
        }

        actualizarLectura(humedad);
        return humedad;
    }

    /**
     * Verifica si el valor de humedad está dentro del rango normal
     * @param valor Valor de humedad a verificar
     * @return true si está en rango óptimo, false si está en rango crítico o subóptimo
     */
    @Override
    public boolean esValorNormal(double valor) {
        return valor >= HUMEDAD_OPTIMA_MIN && valor <= HUMEDAD_OPTIMA_MAX;
    }

    /**
     * Determina el estado de la humedad
     * @param valor Valor de humedad
     * @return Descripción del estado
     */
    public String obtenerEstadoHumedad(double valor) {
        if (valor < HUMEDAD_MINIMA) {
            return "CRÍTICO - Suelo muy seco";
        } else if (valor > HUMEDAD_MAXIMA) {
            return "CRÍTICO - Exceso de humedad";
        } else if (valor < HUMEDAD_OPTIMA_MIN) {
            return "ATENCIÓN - Humedad baja";
        } else if (valor > HUMEDAD_OPTIMA_MAX) {
            return "ATENCIÓN - Humedad alta";
        } else {
            return "ÓPTIMO - Humedad adecuada";
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
     * Obtiene la descripción del rango normal
     * @return String con el rango normal de humedad
     */
    @Override
    public String obtenerRangoNormal() {
        return String.format("Rango óptimo: %.1f%% - %.1f%%", HUMEDAD_OPTIMA_MIN, HUMEDAD_OPTIMA_MAX);
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
