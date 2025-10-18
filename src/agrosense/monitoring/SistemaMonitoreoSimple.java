package agrosense.monitoring;

import agrosense.model.LoteSimple;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Sistema central de monitoreo para el sistema AgroSense (version simplificada).
 * Maneja la supervision de multiples lotes y genera alertas automaticas.
 */
public class SistemaMonitoreoSimple {
    private List<LoteSimple> lotes;
    private List<AlertaSimple> alertasGlobales;
    private boolean monitoreoActivo;
    private Timer timer;
    private static final long INTERVALO_MONITOREO = 30000; // 30 segundos

    /**
     * Constructor del sistema de monitoreo
     */
    public SistemaMonitoreoSimple() {
        this.lotes = new ArrayList<>();
        this.alertasGlobales = new ArrayList<>();
        this.monitoreoActivo = false;
    }

    /**
     * Agrega un lote al sistema de monitoreo
     * @param lote Lote a agregar
     */
    public void agregarLote(LoteSimple lote) {
        if (lote != null && !lotes.contains(lote)) {
            lotes.add(lote);
            System.out.println("Lote '" + lote.getNombre() + "' agregado al sistema de monitoreo.");
        }
    }

    /**
     * Elimina un lote del sistema de monitoreo
     * @param idLote ID del lote a eliminar
     * @return true si se elimino exitosamente
     */
    public boolean eliminarLote(String idLote) {
        return lotes.removeIf(lote -> lote.getId().equals(idLote));
    }

    /**
     * Inicia el monitoreo automatico de todos los lotes
     */
    public void iniciarMonitoreo() {
        if (!monitoreoActivo) {
            monitoreoActivo = true;
            timer = new Timer("MonitoreoAgroSense");
            
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    realizarMonitoreoCompleto();
                }
            }, 0, INTERVALO_MONITOREO);
            
            System.out.println("Sistema de monitoreo iniciado. Intervalo: " + 
                             (INTERVALO_MONITOREO / 1000) + " segundos");
        }
    }

    /**
     * Detiene el monitoreo automatico
     */
    public void detenerMonitoreo() {
        if (monitoreoActivo && timer != null) {
            timer.cancel();
            timer = null;
            monitoreoActivo = false;
            System.out.println("Sistema de monitoreo detenido.");
        }
    }

    /**
     * Realiza una lectura completa de todos los lotes
     */
    public void realizarMonitoreoCompleto() {
        if (lotes.isEmpty()) {
            return;
        }

        System.out.println("\n=== MONITOREO AUTOMATICO - " + new Date() + " ===");
        
        for (LoteSimple lote : lotes) {
            try {
                lote.realizarLectura();
                procesarAlertasLote(lote);
                mostrarEstadoLote(lote);
            } catch (Exception e) {
                System.err.println("Error al monitorear lote " + lote.getId() + ": " + e.getMessage());
            }
        }
        
        mostrarResumenGlobal();
    }

    /**
     * Procesa las alertas de un lote especifico
     * @param lote Lote a procesar
     */
    private void procesarAlertasLote(LoteSimple lote) {
        for (String alertaLote : lote.getAlertas()) {
            AlertaSimple alertaGlobal = new AlertaSimple(
                lote.getId(),
                lote.getNombre(),
                alertaLote,
                AlertaSimple.TipoAlerta.CRITICA,
                new Date()
            );
            alertasGlobales.add(alertaGlobal);
        }
    }

    /**
     * Muestra el estado de un lote
     * @param lote Lote a mostrar
     */
    private void mostrarEstadoLote(LoteSimple lote) {
        System.out.println("\n" + lote.toString());
        
        if (lote.getSensorHumedad() != null && lote.getSensorHumedad().isActivo()) {
            double humedad = lote.getSensorHumedad().getUltimaLectura();
            System.out.println("   Humedad: " + String.format("%.1f%%", humedad) + 
                             " - " + lote.getSensorHumedad().obtenerEstadoHumedad(humedad));
        }
        
        if (lote.getSensorTemperatura() != null && lote.getSensorTemperatura().isActivo()) {
            double temperatura = lote.getSensorTemperatura().getUltimaLectura();
            System.out.println("   Temperatura: " + String.format("%.1f°C", temperatura) + 
                             " - " + lote.getSensorTemperatura().obtenerEstadoTemperatura(temperatura));
        }
    }

    /**
     * Muestra un resumen global del sistema
     */
    private void mostrarResumenGlobal() {
        long lotesOptimos = lotes.stream()
            .filter(lote -> "OPTIMO".equals(lote.getEstadoGeneral()))
            .count();
        long lotesAtencion = lotes.stream()
            .filter(lote -> "ATENCION".equals(lote.getEstadoGeneral()))
            .count();
        long lotesCriticos = lotes.stream()
            .filter(lote -> "CRITICO".equals(lote.getEstadoGeneral()))
            .count();

        System.out.println("\n=== RESUMEN GLOBAL ===");
        System.out.println("Total de lotes: " + lotes.size());
        System.out.println("Optimos: " + lotesOptimos);
        System.out.println("Atencion: " + lotesAtencion);
        System.out.println("Criticos: " + lotesCriticos);
        
        if (lotesCriticos > 0) {
            System.out.println("\n¡ATENCION! Hay " + lotesCriticos + " lote(s) en estado critico que requieren intervencion inmediata.");
        }
    }

    /**
     * Obtiene un lote por su ID
     * @param id ID del lote
     * @return Lote encontrado o null si no existe
     */
    public LoteSimple obtenerLote(String id) {
        return lotes.stream()
            .filter(lote -> lote.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    /**
     * Obtiene todos los lotes
     * @return Lista de todos los lotes
     */
    public List<LoteSimple> obtenerTodosLotes() {
        return new ArrayList<>(lotes);
    }

    /**
     * Obtiene lotes por estado
     * @param estado Estado a filtrar
     * @return Lista de lotes con el estado especificado
     */
    public List<LoteSimple> obtenerLotesPorEstado(String estado) {
        return lotes.stream()
            .filter(lote -> estado.equals(lote.getEstadoGeneral()))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene todas las alertas globales
     * @return Lista de alertas
     */
    public List<AlertaSimple> obtenerAlertasGlobales() {
        return new ArrayList<>(alertasGlobales);
    }

    /**
     * Limpia las alertas globales
     */
    public void limpiarAlertas() {
        alertasGlobales.clear();
        System.out.println("Alertas globales limpiadas.");
    }

    /**
     * Obtiene estadisticas del sistema
     * @return String con estadisticas detalladas
     */
    public String obtenerEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADISTICAS DEL SISTEMA ===\n");
        stats.append("Total de lotes monitoreados: ").append(lotes.size()).append("\n");
        stats.append("Monitoreo activo: ").append(monitoreoActivo ? "SI" : "NO").append("\n");
        stats.append("Total de alertas: ").append(alertasGlobales.size()).append("\n");
        
        if (!lotes.isEmpty()) {
            long sensoresActivos = lotes.stream()
                .mapToLong(lote -> {
                    int activos = 0;
                    if (lote.getSensorHumedad() != null && lote.getSensorHumedad().isActivo()) activos++;
                    if (lote.getSensorTemperatura() != null && lote.getSensorTemperatura().isActivo()) activos++;
                    return activos;
                })
                .sum();
            
            stats.append("Sensores activos: ").append(sensoresActivos).append(" de ").append(lotes.size() * 2).append("\n");
        }
        
        return stats.toString();
    }

    // Getters
    public boolean isMonitoreoActivo() {
        return monitoreoActivo;
    }

    public int getCantidadLotes() {
        return lotes.size();
    }
}
