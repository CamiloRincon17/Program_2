package agrosense.sensors;

import java.util.Random;

/**
 * Sensor de temperatura ambiental para el sistema AgroSense.
 * Simula la medicion de temperatura en grados Celsius.
 */
public class SensorTemperaturaSimple extends Sensor {
    private static final double TEMP_MINIMA_CRITICA = 5.0;   // °C - Temperatura critica baja
    private static final double TEMP_MAXIMA_CRITICA = 40.0;  // °C - Temperatura critica alta
    private static final double TEMP_OPTIMA_MIN = 18.0;      // °C - Rango optimo minimo
    private static final double TEMP_OPTIMA_MAX = 28.0;      // °C - Rango optimo maximo
    
    private Random random;

    /**
     * Constructor del sensor de temperatura
     * @param id Identificador unico del sensor
     * @param nombre Nombre descriptivo del sensor
     */
    public SensorTemperaturaSimple(String id, String nombre) {
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

        // Simulacion realista considerando variaciones diurnas y estacionales
        double temperatura;
        int tipoCondicion = random.nextInt(12); // 0-11
        
        switch (tipoCondicion) {
            case 0: // 8.3% - Temperatura critica baja (heladas)
                temperatura = random.nextDouble() * 8 + 2; // 2-10°C
                break;
            case 1: // 8.3% - Temperatura critica alta (ola de calor)
                temperatura = random.nextDouble() * 10 + 40; // 40-50°C
                break;
            case 2:
            case 3: // 16.7% - Temperatura suboptima baja
                temperatura = random.nextDouble() * 8 + 10; // 10-18°C
                break;
            case 4:
            case 5: // 16.7% - Temperatura suboptima alta
                temperatura = random.nextDouble() * 12 + 28; // 28-40°C
                break;
            default: // 50% - Temperatura optima
                temperatura = random.nextDouble() * 10 + 18; // 18-28°C
                break;
        }

        actualizarLectura(temperatura);
        return temperatura;
    }

    /**
     * Verifica si el valor de temperatura esta dentro del rango normal
     * @param valor Valor de temperatura a verificar
     * @return true si esta en rango optimo, false si esta en rango critico o suboptimo
     */
    @Override
    public boolean esValorNormal(double valor) {
        return valor >= TEMP_OPTIMA_MIN && valor <= TEMP_OPTIMA_MAX;
    }

    /**
     * Determina el estado de la temperatura
     * @param valor Valor de temperatura
     * @return Descripcion del estado
     */
    public String obtenerEstadoTemperatura(double valor) {
        if (valor < TEMP_MINIMA_CRITICA) {
            return "CRITICO - Riesgo de heladas";
        } else if (valor > TEMP_MAXIMA_CRITICA) {
            return "CRITICO - Ola de calor";
        } else if (valor < TEMP_OPTIMA_MIN) {
            return "ATENCION - Temperatura baja";
        } else if (valor > TEMP_OPTIMA_MAX) {
            return "ATENCION - Temperatura alta";
        } else {
            return "OPTIMO - Temperatura adecuada";
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
     * Verifica si hay riesgo de estres por calor
     * @param valor Valor de temperatura actual
     * @return true si hay riesgo de estres por calor
     */
    public boolean hayRiesgoCalor(double valor) {
        return valor > TEMP_MAXIMA_CRITICA;
    }

    /**
     * Obtiene la descripcion del rango normal
     * @return String con el rango normal de temperatura
     */
    @Override
    public String obtenerRangoNormal() {
        return String.format("Rango optimo: %.1f°C - %.1f°C", TEMP_OPTIMA_MIN, TEMP_OPTIMA_MAX);
    }

    /**
     * Calcula el indice de estres termico
     * @param valor Valor de temperatura actual
     * @return Indice de estres (0-100, donde 0 es sin estres y 100 es maximo estres)
     */
    public double calcularIndiceEstres(double valor) {
        if (valor >= TEMP_OPTIMA_MIN && valor <= TEMP_OPTIMA_MAX) {
            return 0; // Sin estres
        } else if (valor < TEMP_MINIMA_CRITICA) {
            return 100; // Maximo estres por frio
        } else if (valor > TEMP_MAXIMA_CRITICA) {
            return 100; // Maximo estres por calor
        } else if (valor < TEMP_OPTIMA_MIN) {
            return ((TEMP_OPTIMA_MIN - valor) / (TEMP_OPTIMA_MIN - TEMP_MINIMA_CRITICA)) * 50; // Estres moderado por frio
        } else {
            return ((valor - TEMP_OPTIMA_MAX) / (TEMP_MAXIMA_CRITICA - TEMP_OPTIMA_MAX)) * 50; // Estres moderado por calor
        }
    }

    /**
     * Obtiene recomendaciones basadas en la temperatura
     * @param valor Valor de temperatura actual
     * @return Recomendacion especifica para la temperatura
     */
    public String obtenerRecomendacionTemperatura(double valor) {
        if (valor < TEMP_MINIMA_CRITICA) {
            return "Cubrir cultivos, aplicar riego de proteccion contra heladas";
        } else if (valor > TEMP_MAXIMA_CRITICA) {
            return "Aumentar riego, proporcionar sombra, evitar labores en horas pico";
        } else if (valor < TEMP_OPTIMA_MIN) {
            return "Considerar proteccion adicional, monitorear crecimiento";
        } else if (valor > TEMP_OPTIMA_MAX) {
            return "Incrementar riego, evitar labores en horas de maximo calor";
        } else {
            return "Condiciones ideales para el crecimiento";
        }
    }
}
