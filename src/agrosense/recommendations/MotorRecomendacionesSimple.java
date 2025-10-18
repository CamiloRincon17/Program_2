package agrosense.recommendations;

import agrosense.model.LoteSimple;
import agrosense.sensors.SensorHumedadSimple;
import agrosense.sensors.SensorTemperaturaSimple;
import java.util.ArrayList;
import java.util.List;

/**
 * Motor de recomendaciones inteligente para el sistema AgroSense (version simplificada).
 * Analiza las condiciones de los lotes y genera recomendaciones especificas.
 */
public class MotorRecomendacionesSimple {
    
    /**
     * Genera recomendaciones personalizadas para un lote especifico
     * @param lote Lote a analizar
     * @return Lista de recomendaciones
     */
    public List<RecomendacionSimple> generarRecomendacionesLote(LoteSimple lote) {
        List<RecomendacionSimple> recomendaciones = new ArrayList<>();
        
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
     * Analiza las condiciones de humedad y genera recomendaciones especificas
     * @param lote Lote a analizar
     * @return Lista de recomendaciones de humedad
     */
    private List<RecomendacionSimple> analizarHumedad(LoteSimple lote) {
        List<RecomendacionSimple> recomendaciones = new ArrayList<>();
        SensorHumedadSimple sensorHumedad = lote.getSensorHumedad();
        double humedad = sensorHumedad.getUltimaLectura();

        if (sensorHumedad.requiereRiegoUrgente(humedad)) {
            double cantidadRiego = sensorHumedad.calcularRiegoRecomendado(humedad);
            recomendaciones.add(new RecomendacionSimple(
                "RIEGO URGENTE",
                "El suelo esta muy seco y requiere riego inmediato",
                RecomendacionSimple.TipoRecomendacionSimple.CRITICA,
                1,
                String.format("Regar con %.1f L/m2 inmediatamente. Total: %.1f litros para el lote completo.", 
                             cantidadRiego, cantidadRiego * lote.getArea()),
                "Riego de emergencia"
            ));
        } else if (sensorHumedad.hayRiesgoEncharcamiento(humedad)) {
            recomendaciones.add(new RecomendacionSimple(
                "EXCESO DE HUMEDAD",
                "Hay riesgo de encharcamiento en el lote",
                RecomendacionSimple.TipoRecomendacionSimple.CRITICA,
                1,
                "Suspender todo riego, mejorar drenaje y considerar cubrir el lote si llueve",
                "Control de humedad"
            ));
        } else if (humedad < 45) {
            recomendaciones.add(new RecomendacionSimple(
                "RIEGO RECOMENDADO",
                "La humedad del suelo esta por debajo del optimo",
                RecomendacionSimple.TipoRecomendacionSimple.ATENCION,
                2,
                String.format("Regar con 8 L/m2. El lote se beneficiaria de un riego moderado."),
                "Riego preventivo"
            ));
        } else if (humedad > 65) {
            recomendaciones.add(new RecomendacionSimple(
                "MONITOREO DE HUMEDAD",
                "La humedad esta en el limite superior",
                RecomendacionSimple.TipoRecomendacionSimple.INFORMATIVA,
                3,
                "Monitorear de cerca. Evitar riego adicional hasta que la humedad baje al rango optimo.",
                "Monitoreo"
            ));
        }
        return recomendaciones;
    }

    /**
     * Analiza las condiciones de temperatura y genera recomendaciones especificas
     * @param lote Lote a analizar
     * @return Lista de recomendaciones de temperatura
     */
    private List<RecomendacionSimple> analizarTemperatura(LoteSimple lote) {
        List<RecomendacionSimple> recomendaciones = new ArrayList<>();
        SensorTemperaturaSimple sensorTemperatura = lote.getSensorTemperatura();
        double temperatura = sensorTemperatura.getUltimaLectura();

        if (sensorTemperatura.hayRiesgoHeladas(temperatura)) {
            recomendaciones.add(new RecomendacionSimple(
                "PROTECCION CONTRA HELADAS",
                "Riesgo critico de heladas",
                RecomendacionSimple.TipoRecomendacionSimple.CRITICA,
                1,
                "Cubrir cultivos inmediatamente con mallas o plasticos. Aplicar riego de proteccion si es posible.",
                "Proteccion contra heladas"
            ));
        } else if (sensorTemperatura.hayRiesgoCalor(temperatura)) {
            recomendaciones.add(new RecomendacionSimple(
                "PROTECCION CONTRA CALOR",
                "Riesgo de estres por calor",
                RecomendacionSimple.TipoRecomendacionSimple.CRITICA,
                1,
                "Aumentar frecuencia de riego, proporcionar sombra y evitar labores en horas pico de calor.",
                "Proteccion contra calor"
            ));
        } else if (temperatura < 18) {
            recomendaciones.add(new RecomendacionSimple(
                "TEMPERATURA BAJA",
                "Las temperaturas estan por debajo del optimo",
                RecomendacionSimple.TipoRecomendacionSimple.ATENCION,
                2,
                "Monitorear crecimiento. Considerar proteccion adicional si las temperaturas bajan mas.",
                "Monitoreo de temperatura"
            ));
        } else if (temperatura > 28) {
            recomendaciones.add(new RecomendacionSimple(
                "TEMPERATURA ALTA",
                "Las temperaturas estan por encima del optimo",
                RecomendacionSimple.TipoRecomendacionSimple.ATENCION,
                2,
                "Incrementar riego y evitar labores en las horas de maximo calor (11:00-15:00).",
                "Manejo de temperatura alta"
            ));
        }

        // Recomendacion basada en el indice de estres
        double indiceEstres = sensorTemperatura.calcularIndiceEstres(temperatura);
        if (indiceEstres > 50) {
            recomendaciones.add(new RecomendacionSimple(
                "ESTRES TERMICO",
                "Alto indice de estres termico detectado",
                RecomendacionSimple.TipoRecomendacionSimple.ATENCION,
                2,
                String.format("Indice de estres: %.0f%%. Ajustar manejo del cultivo segun las condiciones termicas.", indiceEstres),
                "Analisis de estres"
            ));
        }

        return recomendaciones;
    }

    /**
     * Genera recomendaciones generales basadas en el estado del lote
     * @param lote Lote a analizar
     * @return Lista de recomendaciones generales
     */
    private List<RecomendacionSimple> generarRecomendacionesGenerales(LoteSimple lote) {
        List<RecomendacionSimple> recomendaciones = new ArrayList<>();

        // Recomendacion basada en el estado general
        switch (lote.getEstadoGeneral()) {
            case "OPTIMO":
                recomendaciones.add(new RecomendacionSimple(
                    "MANTENIMIENTO",
                    "Condiciones optimas detectadas",
                    RecomendacionSimple.TipoRecomendacionSimple.INFORMATIVA,
                    3,
                    "Continuar con el monitoreo regular. Las condiciones actuales son ideales para el crecimiento.",
                    "Mantenimiento"
                ));
                break;
            case "ATENCION":
                recomendaciones.add(new RecomendacionSimple(
                    "MONITOREO INTENSIVO",
                    "Se requieren acciones preventivas",
                    RecomendacionSimple.TipoRecomendacionSimple.ATENCION,
                    2,
                    "Incrementar frecuencia de monitoreo y estar preparado para intervenir si las condiciones empeoran.",
                    "Monitoreo intensivo"
                ));
                break;
            case "CRITICO":
                recomendaciones.add(new RecomendacionSimple(
                    "INTERVENCION INMEDIATA",
                    "Estado critico requiere accion urgente",
                    RecomendacionSimple.TipoRecomendacionSimple.CRITICA,
                    1,
                    "Tomar medidas inmediatas segun las alertas especificas. El lote requiere atencion urgente.",
                    "Accion inmediata"
                ));
                break;
        }

        // Recomendaciones basadas en el tipo de cultivo
        recomendaciones.addAll(generarRecomendacionesPorCultivo(lote));

        return recomendaciones;
    }

    /**
     * Genera recomendaciones especificas segun el tipo de cultivo
     * @param lote Lote a analizar
     * @return Lista de recomendaciones por tipo de cultivo
     */
    private List<RecomendacionSimple> generarRecomendacionesPorCultivo(LoteSimple lote) {
        List<RecomendacionSimple> recomendaciones = new ArrayList<>();
        String tipoCultivo = lote.getTipoCultivo().toLowerCase();

        switch (tipoCultivo) {
            case "tomate":
            case "tomates":
                recomendaciones.add(new RecomendacionSimple(
                    "CUIDADOS ESPECIFICOS - TOMATE",
                    "Recomendaciones para cultivo de tomate",
                    RecomendacionSimple.TipoRecomendacionSimple.INFORMATIVA,
                    3,
                    "Los tomates son sensibles a cambios bruscos de humedad. Mantener riego consistente y evitar mojar las hojas.",
                    "Cultivo de tomate"
                ));
                break;
            case "lechuga":
            case "lechugas":
                recomendaciones.add(new RecomendacionSimple(
                    "CUIDADOS ESPECIFICOS - LECHUGA",
                    "Recomendaciones para cultivo de lechuga",
                    RecomendacionSimple.TipoRecomendacionSimple.INFORMATIVA,
                    3,
                    "La lechuga requiere humedad constante. En clima caluroso, considerar riego por goteo y sombra parcial.",
                    "Cultivo de lechuga"
                ));
                break;
            case "papa":
            case "papas":
                recomendaciones.add(new RecomendacionSimple(
                    "CUIDADOS ESPECIFICOS - PAPA",
                    "Recomendaciones para cultivo de papa",
                    RecomendacionSimple.TipoRecomendacionSimple.INFORMATIVA,
                    3,
                    "Las papas requieren buen drenaje. Evitar encharcamientos que pueden causar enfermedades.",
                    "Cultivo de papa"
                ));
                break;
            default:
                recomendaciones.add(new RecomendacionSimple(
                    "CUIDADOS GENERALES",
                    "Recomendaciones generales para el cultivo",
                    RecomendacionSimple.TipoRecomendacionSimple.INFORMATIVA,
                    3,
                    "Mantener monitoreo regular y ajustar las practicas segun las condiciones especificas del cultivo.",
                    "Cuidados generales"
                ));
                break;
        }

        return recomendaciones;
    }

    /**
     * Genera un plan de accion integral para un lote
     * @param lote Lote a analizar
     * @return Plan de accion formateado
     */
    public String generarPlanAccion(LoteSimple lote) {
        List<RecomendacionSimple> recomendaciones = generarRecomendacionesLote(lote);
        
        StringBuilder plan = new StringBuilder();
        plan.append("=== PLAN DE ACCION - ").append(lote.getNombre()).append(" ===\n");
        plan.append("Lote: ").append(lote.getNombre()).append(" (").append(lote.getId()).append(")\n");
        plan.append("Cultivo: ").append(lote.getTipoCultivo()).append("\n");
        plan.append("Estado: ").append(lote.getEstadoGeneral()).append("\n");
        plan.append("Generado: ").append(new java.util.Date()).append("\n\n");

        if (recomendaciones.isEmpty()) {
            plan.append("No se requieren acciones especificas en este momento.\n");
            plan.append("Continue con el monitoreo regular.\n");
        } else {
            plan.append("ACCIONES RECOMENDADAS:\n\n");
            
            for (int i = 0; i < recomendaciones.size(); i++) {
                RecomendacionSimple rec = recomendaciones.get(i);
                plan.append(String.format("%d. %s\n", i + 1, rec.getTitulo()));
                plan.append("   Prioridad: ").append(rec.getTipo().getDescripcion()).append("\n");
                plan.append("   Descripcion: ").append(rec.getDescripcion()).append("\n");
                plan.append("   Accion: ").append(rec.getAccion()).append("\n\n");
            }
        }

        return plan.toString();
    }
}
