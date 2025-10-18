package agrosense.sensors;

import java.util.Random;

/**
 * Sensor de temperatura ambiental para el sistema AgroSense.
 * Simula la medición de temperatura en grados Celsius.
 */
public class SensorTemperatura extends Sensor {
    private static final double TEMP_MINIMA_CRITICA = 5.0;   // °C - Temperatura crítica baja
    private static final double TEMP_MAXIMA_CRITICA = 40.0;  // °C - Temperatura crítica alta
    private static final double TEMP_OPTIMA_MIN = 18.0;      // °C - Rango óptimo mínimo
    private static final double TEMP_OPTIMA_MAX = 28.0;      // °C - Rango óptimo máximo
    
    private Random random;

    /**
     * Constructor del sensor de temperatura
     * @param id Identificador único del sensor
     * @param nombre Nombre descriptivo del sensor
     */
    public SensorTemperatura(String id, String nombre) {
        super(id, nombre);
        this.random = new Random();
    }

    /**
     * Simula la lectura de temperatura ambiental
     * @return Valor de temperatura en grados Celsius
     */
    @Override
    public double leer() {
        if (!activo) {
            return -999; // Sensor inactivo
        }

        // Simulación realista considerando variaciones diurnas y estacionales
        double temperatura;
        int tipoCondicion = random.nextInt(12); // 0-11
        
        switch (tipoCondicion) {
            case 0: // 8.3% - Temperatura crítica baja (heladas)
                temperatura = random.nextDouble() * 8 + 2; // 2-10°C
                break;
            case 1: // 8.3% - Temperatura crítica alta (ola de calor)
                temperatura = random.nextDouble() * 10 + 40; // 40-50°C
                break;
            case 2:
            case 3: // 16.7% - Temperatura subóptima baja
                temperatura = random.nextDouble() * 8 + 10; // 10-18°C
                break;
            case 4:
            case 5: // 16.7% - Temperatura subóptima alta
                temperatura = random.nextDouble() * 12 + 28; // 28-40°C
                break;
            default: // 50% - Temperatura óptima
                temperatura = random.nextDouble() * 10 + 18; // 18-28°C
                break;
        }

        actualizarLectura(temperatura);
        return temperatura;
    }

    /**
     * Verifica si el valor de temperatura está dentro del rango normal
     * @param valor Valor de temperatura a verificar
     * @return true si está en rango óptimo, false si está en rango crítico o subóptimo
     */
    @Override
    public boolean esValorNormal(double valor) {
        return valor >= TEMP_OPTIMA_MIN && valor <= TEMP_OPTIMA_MAX;
    }

    /**
     * Determina el estado de la temperatura
     * @param valor Valor de temperatura
     * @return Descripción del estado
     */
    public String obtenerEstadoTemperatura(double valor) {
        if (valor < TEMP_MINIMA_CRITICA) {
            return "CRÍTICO - Riesgo de heladas";
        } else if (valor > TEMP_MAXIMA_CRITICA) {
            return "CRÍTICO - Ola de calor";
        } else if (valor < TEMP_OPTIMA_MIN) {
            return "ATENCIÓN - Temperatura baja";
        } else if (valor > TEMP_OPTIMA_MAX) {
            return "ATENCIÓN - Temperatura alta";
        } else {
            return "ÓPTIMO - Temperatura adecuada";
        }
    }

    /**
     * Verifica si hay riesgo de heladas
     * @param valor Valor de temperatura actual
     * @return true si hay riesgo de heladas
     */
    public boolean hayRiesgoHeladas(double valor) {
        return valor < TEMP_MINIMA_CRITICA;
    }

    /**
     * Verifica si hay riesgo de estrés por calor
     * @param valor Valor de temperatura actual
     * @return true si hay riesgo de estrés por calor
     */
    public boolean hayRiesgoCalor(double valor) {
        return valor > TEMP_MAXIMA_CRITICA;
    }

    /**
     * Obtiene la descripción del rango normal
     * @return String con el rango normal de temperatura
     */
    @Override
    public String obtenerRangoNormal() {
        return String.format("Rango óptimo: %.1f°C - %.1f°C", TEMP_OPTIMA_MIN, TEMP_OPTIMA_MAX);
    }

    /**
     * Calcula el índice de estrés térmico
     * @param valor Valor de temperatura actual
     * @return Índice de estrés (0-100, donde 0 es sin estrés y 100 es máximo estrés)
     */
    public double calcularIndiceEstres(double valor) {
        if (valor >= TEMP_OPTIMA_MIN && valor <= TEMP_OPTIMA_MAX) {
            return 0; // Sin estrés
        } else if (valor < TEMP_MINIMA_CRITICA) {
            return 100; // Máximo estrés por frío
        } else if (valor > TEMP_MAXIMA_CRITICA) {
            return 100; // Máximo estrés por calor
        } else if (valor < TEMP_OPTIMA_MIN) {
            return ((TEMP_OPTIMA_MIN - valor) / (TEMP_OPTIMA_MIN - TEMP_MINIMA_CRITICA)) * 50; // Estrés moderado por frío
        } else {
            return ((valor - TEMP_OPTIMA_MAX) / (TEMP_MAXIMA_CRITICA - TEMP_OPTIMA_MAX)) * 50; // Estrés moderado por calor
        }
    }

    /**
     * Obtiene recomendaciones basadas en la temperatura
     * @param valor Valor de temperatura actual
     * @return Recomendación específica para la temperatura
     */
    public String obtenerRecomendacionTemperatura(double valor) {
        if (valor < TEMP_MINIMA_CRITICA) {
            return "Cubrir cultivos, aplicar riego de protección contra heladas";
        } else if (valor > TEMP_MAXIMA_CRITICA) {
            return "Aumentar riego, proporcionar sombra, evitar labores en horas pico";
        } else if (valor < TEMP_OPTIMA_MIN) {
            return "Considerar protección adicional, monitorear crecimiento";
        } else if (valor > TEMP_OPTIMA_MAX) {
            return "Incrementar riego, evitar labores en horas de máximo calor";
        } else {
            return "Condiciones ideales para el crecimiento";
        }
    }
}
