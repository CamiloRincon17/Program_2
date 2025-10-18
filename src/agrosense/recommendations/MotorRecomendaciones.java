package agrosense.recommendations;

import agrosense.model.Lote;
import agrosense.sensors.SensorHumedad;
import agrosense.sensors.SensorTemperatura;
import java.util.ArrayList;
import java.util.List;

/**
 * Motor de recomendaciones inteligente para el sistema AgroSense.
 * Analiza las condiciones de los lotes y genera recomendaciones específicas.
 */
public class MotorRecomendaciones {
    
    /**
     * Genera recomendaciones personalizadas para un lote específico
     * @param lote Lote a analizar
     * @return Lista de recomendaciones
     */
    public List<Recomendacion> generarRecomendacionesLote(Lote lote) {
        List<Recomendacion> recomendaciones = new ArrayList<>();
        
        if (lote == null) {
            return recomendaciones;
        }

        // Analizar condiciones de humedad
        if (lote.getSensorHumedad() != null && lote.getSensorHumedad().isActivo()) {
            recomendaciones.addAll(analizarHumedad(lote));
        }

        // Analizar condiciones de temperatura
        if (lote.getSensorTemperatura() != null && lote.getSensorTemperatura().isActivo()) {
            recomendaciones.addAll(analizarTemperatura(lote));
        }

        // Recomendaciones generales basadas en el estado del lote
        recomendaciones.addAll(generarRecomendacionesGenerales(lote));

        // Ordenar por prioridad
        recomendaciones.sort((r1, r2) -> Integer.compare(r1.getPrioridad(), r2.getPrioridad()));

        return recomendaciones;
    }

    /**
     * Analiza las condiciones de humedad y genera recomendaciones específicas
     * @param lote Lote a analizar
     * @return Lista de recomendaciones de humedad
     */
    private List<Recomendacion> analizarHumedad(Lote lote) {
        List<Recomendacion> recomendaciones = new ArrayList<>();
        SensorHumedad sensorHumedad = lote.getSensorHumedad();
        double humedad = sensorHumedad.getUltimaLectura();

        if (sensorHumedad.requiereRiegoUrgente(humedad)) {
            double cantidadRiego = sensorHumedad.calcularRiegoRecomendado(humedad);
            recomendaciones.add(new Recomendacion(
                "RIEGO URGENTE",
                "El suelo está muy seco y requiere riego inmediato",
                TipoRecomendacion.CRITICA,
                1,
                String.format("Regar con %.1f L/m² inmediatamente. Total: %.1f litros para el lote completo.", 
                             cantidadRiego, cantidadRiego * lote.getArea()),
                "🌧️ Riego de emergencia"
            ));
        } else if (sensorHumedad.hayRiesgoEncharcamiento(humedad)) {
            recomendaciones.add(new Recomendacion(
                "EXCESO DE HUMEDAD",
                "Hay riesgo de encharcamiento en el lote",
                TipoRecomendacion.CRITICA,
                1,
                "Suspender todo riego, mejorar drenaje y considerar cubrir el lote si llueve",
                "💧 Control de humedad"
            ));
        } else if (humedad < 45) {
            recomendaciones.add(new Recomendacion(
                "RIEGO RECOMENDADO",
                "La humedad del suelo está por debajo del óptimo",
                TipoRecomendacion.ATENCION,
                2,
                String.format("Regar con 8 L/m². El lote se beneficiaría de un riego moderado."),
                "🌧️ Riego preventivo"
            ));
        } else if (humedad > 65) {
            recomendaciones.add(new Recomendacion(
                "MONITOREO DE HUMEDAD",
                "La humedad está en el límite superior",
                TipoRecomendacion.INFORMATIVA,
                3,
                "Monitorear de cerca. Evitar riego adicional hasta que la humedad baje al rango óptimo.",
                "📊 Monitoreo"
            ));
        }
        return recomendaciones;
    }

    /**
     * Analiza las condiciones de temperatura y genera recomendaciones específicas
     * @param lote Lote a analizar
     * @return Lista de recomendaciones de temperatura
     */
    private List<Recomendacion> analizarTemperatura(Lote lote) {
        List<Recomendacion> recomendaciones = new ArrayList<>();
        SensorTemperatura sensorTemperatura = lote.getSensorTemperatura();
        double temperatura = sensorTemperatura.getUltimaLectura();

        if (sensorTemperatura.hayRiesgoHeladas(temperatura)) {
            recomendaciones.add(new Recomendacion(
                "PROTECCIÓN CONTRA HELADAS",
                "Riesgo crítico de heladas",
                TipoRecomendacion.CRITICA,
                1,
                "Cubrir cultivos inmediatamente con mallas o plásticos. Aplicar riego de protección si es posible.",
                "🧊 Protección contra heladas"
            ));
        } else if (sensorTemperatura.hayRiesgoCalor(temperatura)) {
            recomendaciones.add(new Recomendacion(
                "PROTECCIÓN CONTRA CALOR",
                "Riesgo de estrés por calor",
                TipoRecomendacion.CRITICA,
                1,
                "Aumentar frecuencia de riego, proporcionar sombra y evitar labores en horas pico de calor.",
                "🌡️ Protección contra calor"
            ));
        } else if (temperatura < 18) {
            recomendaciones.add(new Recomendacion(
                "TEMPERATURA BAJA",
                "Las temperaturas están por debajo del óptimo",
                TipoRecomendacion.ATENCION,
                2,
                "Monitorear crecimiento. Considerar protección adicional si las temperaturas bajan más.",
                "🌡️ Monitoreo de temperatura"
            ));
        } else if (temperatura > 28) {
            recomendaciones.add(new Recomendacion(
                "TEMPERATURA ALTA",
                "Las temperaturas están por encima del óptimo",
                TipoRecomendacion.ATENCION,
                2,
                "Incrementar riego y evitar labores en las horas de máximo calor (11:00-15:00).",
                "🌡️ Manejo de temperatura alta"
            ));
        }

        // Recomendación basada en el índice de estrés
        double indiceEstres = sensorTemperatura.calcularIndiceEstres(temperatura);
        if (indiceEstres > 50) {
            recomendaciones.add(new Recomendacion(
                "ESTRÉS TÉRMICO",
                "Alto índice de estrés térmico detectado",
                TipoRecomendacion.ATENCION,
                2,
                String.format("Índice de estrés: %.0f%%. Ajustar manejo del cultivo según las condiciones térmicas.", indiceEstres),
                "📊 Análisis de estrés"
            ));
        }

        return recomendaciones;
    }

    /**
     * Genera recomendaciones generales basadas en el estado del lote
     * @param lote Lote a analizar
     * @return Lista de recomendaciones generales
     */
    private List<Recomendacion> generarRecomendacionesGenerales(Lote lote) {
        List<Recomendacion> recomendaciones = new ArrayList<>();

        // Recomendación basada en el estado general
        switch (lote.getEstadoGeneral()) {
            case "ÓPTIMO":
                recomendaciones.add(new Recomendacion(
                    "MANTENIMIENTO",
                    "Condiciones óptimas detectadas",
                    TipoRecomendacion.INFORMATIVA,
                    3,
                    "Continuar con el monitoreo regular. Las condiciones actuales son ideales para el crecimiento.",
                    "✅ Mantenimiento"
                ));
                break;
            case "ATENCIÓN":
                recomendaciones.add(new Recomendacion(
                    "MONITOREO INTENSIVO",
                    "Se requieren acciones preventivas",
                    TipoRecomendacion.ATENCION,
                    2,
                    "Incrementar frecuencia de monitoreo y estar preparado para intervenir si las condiciones empeoran.",
                    "👁️ Monitoreo intensivo"
                ));
                break;
            case "CRÍTICO":
                recomendaciones.add(new Recomendacion(
                    "INTERVENCIÓN INMEDIATA",
                    "Estado crítico requiere acción urgente",
                    TipoRecomendacion.CRITICA,
                    1,
                    "Tomar medidas inmediatas según las alertas específicas. El lote requiere atención urgente.",
                    "🚨 Acción inmediata"
                ));
                break;
        }

        // Recomendaciones basadas en el tipo de cultivo
        recomendaciones.addAll(generarRecomendacionesPorCultivo(lote));

        return recomendaciones;
    }

    /**
     * Genera recomendaciones específicas según el tipo de cultivo
     * @param lote Lote a analizar
     * @return Lista de recomendaciones por tipo de cultivo
     */
    private List<Recomendacion> generarRecomendacionesPorCultivo(Lote lote) {
        List<Recomendacion> recomendaciones = new ArrayList<>();
        String tipoCultivo = lote.getTipoCultivo().toLowerCase();

        switch (tipoCultivo) {
            case "tomate":
            case "tomates":
                recomendaciones.add(new Recomendacion(
                    "CUIDADOS ESPECÍFICOS - TOMATE",
                    "Recomendaciones para cultivo de tomate",
                    TipoRecomendacion.INFORMATIVA,
                    3,
                    "Los tomates son sensibles a cambios bruscos de humedad. Mantener riego consistente y evitar mojar las hojas.",
                    "🍅 Cultivo de tomate"
                ));
                break;
            case "lechuga":
            case "lechugas":
                recomendaciones.add(new Recomendacion(
                    "CUIDADOS ESPECÍFICOS - LECHUGA",
                    "Recomendaciones para cultivo de lechuga",
                    TipoRecomendacion.INFORMATIVA,
                    3,
                    "La lechuga requiere humedad constante. En clima caluroso, considerar riego por goteo y sombra parcial.",
                    "🥬 Cultivo de lechuga"
                ));
                break;
            case "papa":
            case "papas":
                recomendaciones.add(new Recomendacion(
                    "CUIDADOS ESPECÍFICOS - PAPA",
                    "Recomendaciones para cultivo de papa",
                    TipoRecomendacion.INFORMATIVA,
                    3,
                    "Las papas requieren buen drenaje. Evitar encharcamientos que pueden causar enfermedades.",
                    "🥔 Cultivo de papa"
                ));
                break;
            default:
                recomendaciones.add(new Recomendacion(
                    "CUIDADOS GENERALES",
                    "Recomendaciones generales para el cultivo",
                    TipoRecomendacion.INFORMATIVA,
                    3,
                    "Mantener monitoreo regular y ajustar las prácticas según las condiciones específicas del cultivo.",
                    "🌱 Cuidados generales"
                ));
                break;
        }

        return recomendaciones;
    }

    /**
     * Genera un plan de acción integral para un lote
     * @param lote Lote a analizar
     * @return Plan de acción formateado
     */
    public String generarPlanAccion(Lote lote) {
        List<Recomendacion> recomendaciones = generarRecomendacionesLote(lote);
        
        StringBuilder plan = new StringBuilder();
        plan.append("📋 === PLAN DE ACCIÓN - ").append(lote.getNombre()).append(" ===\n");
        plan.append("🌱 Lote: ").append(lote.getNombre()).append(" (").append(lote.getId()).append(")\n");
        plan.append("🌾 Cultivo: ").append(lote.getTipoCultivo()).append("\n");
        plan.append("📊 Estado: ").append(lote.getEstadoGeneral()).append("\n");
        plan.append("📅 Generado: ").append(new java.util.Date()).append("\n\n");

        if (recomendaciones.isEmpty()) {
            plan.append("✅ No se requieren acciones específicas en este momento.\n");
            plan.append("   Continúe con el monitoreo regular.\n");
        } else {
            plan.append("🎯 ACCIONES RECOMENDADAS:\n\n");
            
            for (int i = 0; i < recomendaciones.size(); i++) {
                Recomendacion rec = recomendaciones.get(i);
                plan.append(String.format("%d. %s %s\n", i + 1, rec.getIcono(), rec.getTitulo()));
                plan.append("   Prioridad: ").append(rec.getTipo().getDescripcion()).append("\n");
                plan.append("   Descripción: ").append(rec.getDescripcion()).append("\n");
                plan.append("   Acción: ").append(rec.getAccion()).append("\n\n");
            }
        }

        return plan.toString();
    }
}
