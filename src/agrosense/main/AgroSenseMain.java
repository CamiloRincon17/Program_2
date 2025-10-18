package agrosense.main;

import agrosense.model.Lote;
import agrosense.monitoring.SistemaMonitoreo;
import agrosense.recommendations.MotorRecomendaciones;
import agrosense.recommendations.Recomendacion;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal del sistema AgroSense.
 * Maneja la interfaz de usuario y coordina todas las funcionalidades del sistema.
 */
public class AgroSenseMain {
    private SistemaMonitoreo sistemaMonitoreo;
    private MotorRecomendaciones motorRecomendaciones;
    private Scanner scanner;
    private boolean sistemaActivo;

    /**
     * Constructor del sistema principal
     */
    public AgroSenseMain() {
        this.sistemaMonitoreo = new SistemaMonitoreo();
        this.motorRecomendaciones = new MotorRecomendaciones();
        this.scanner = new Scanner(System.in);
        this.sistemaActivo = true;
    }

    /**
     * Método principal que inicia el sistema
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        AgroSenseMain agroSense = new AgroSenseMain();
        agroSense.mostrarBienvenida();
        agroSense.inicializarLotesEjemplo();
        agroSense.ejecutarSistema();
    }

    /**
     * Muestra el mensaje de bienvenida del sistema
     */
    private void mostrarBienvenida() {
        System.out.println("🌱 ==============================================");
        System.out.println("🌱           AGROSENSE - Sistema de              ");
        System.out.println("🌱         Monitoreo Agrícola Inteligente        ");
        System.out.println("🌱 ==============================================");
        System.out.println("🌱 Desarrollado por:                            ");
        System.out.println("🌱   • Jhoan Alexander Molina Gomez (192490)    ");
        System.out.println("🌱   • Adrian Camilo Rincon Ascanio (192531)    ");
        System.out.println("🌱   • Isaac David García Vesga (192...)        ");
        System.out.println("🌱 ==============================================");
        System.out.println();
        System.out.println("✨ ¡Bienvenido al sistema de monitoreo agrícola!");
        System.out.println("📊 Monitoreo en tiempo real de humedad y temperatura");
        System.out.println("🚨 Alertas inteligentes y recomendaciones personalizadas");
        System.out.println();
    }

    /**
     * Inicializa lotes de ejemplo para demostración
     */
    private void inicializarLotesEjemplo() {
        System.out.println("🌱 Inicializando lotes de ejemplo...");
        
        // Crear lotes de ejemplo
        Lote lote1 = new Lote("LOTE001", "Lote Norte - Tomates", 100.0, "Tomate");
        Lote lote2 = new Lote("LOTE002", "Lote Sur - Lechugas", 75.0, "Lechuga");
        Lote lote3 = new Lote("LOTE003", "Lote Este - Papas", 150.0, "Papa");
        Lote lote4 = new Lote("LOTE004", "Lote Oeste - Maíz", 200.0, "Maíz");

        // Agregar lotes al sistema
        sistemaMonitoreo.agregarLote(lote1);
        sistemaMonitoreo.agregarLote(lote2);
        sistemaMonitoreo.agregarLote(lote3);
        sistemaMonitoreo.agregarLote(lote4);

        System.out.println("✅ Se han inicializado " + sistemaMonitoreo.getCantidadLotes() + " lotes de ejemplo.");
        System.out.println();
    }

    /**
     * Ejecuta el bucle principal del sistema
     */
    private void ejecutarSistema() {
        while (sistemaActivo) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();
            procesarOpcion(opcion);
        }
        
        scanner.close();
        System.out.println("👋 ¡Gracias por usar AgroSense! Hasta luego.");
    }

    /**
     * Muestra el menú principal del sistema
     */
    private void mostrarMenuPrincipal() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│              MENÚ PRINCIPAL                 │");
        System.out.println("├─────────────────────────────────────────────┤");
        System.out.println("│ 1. 📊 Monitoreo de Lotes                   │");
        System.out.println("│ 2. 🌱 Gestionar Lotes                      │");
        System.out.println("│ 3. 🚨 Ver Alertas                          │");
        System.out.println("│ 4. 💡 Recomendaciones                      │");
        System.out.println("│ 5. 📈 Estadísticas del Sistema             │");
        System.out.println("│ 6. 🔄 Monitoreo Automático                 │");
        System.out.println("│ 7. 📋 Plan de Acción                       │");
        System.out.println("│ 8. ❓ Ayuda                                │");
        System.out.println("│ 9. 🚪 Salir                                │");
        System.out.println("└─────────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Lee la opción seleccionada por el usuario
     * @return Opción seleccionada
     */
    private int leerOpcion() {
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            return opcion;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Procesa la opción seleccionada por el usuario
     * @param opcion Opción seleccionada
     */
    private void procesarOpcion(int opcion) {
        System.out.println();
        
        switch (opcion) {
            case 1:
                mostrarMonitoreoLotes();
                break;
            case 2:
                gestionarLotes();
                break;
            case 3:
                mostrarAlertas();
                break;
            case 4:
                mostrarRecomendaciones();
                break;
            case 5:
                mostrarEstadisticas();
                break;
            case 6:
                gestionarMonitoreoAutomatico();
                break;
            case 7:
                mostrarPlanAccion();
                break;
            case 8:
                mostrarAyuda();
                break;
            case 9:
                sistemaActivo = false;
                break;
            default:
                System.out.println("❌ Opción inválida. Por favor, seleccione una opción del 1 al 9.");
        }
        
        if (opcion != 9) {
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
            System.out.println();
        }
    }

    /**
     * Muestra el monitoreo de todos los lotes
     */
    private void mostrarMonitoreoLotes() {
        System.out.println("📊 === MONITOREO DE LOTES ===");
        
        List<Lote> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("ℹ️ No hay lotes registrados en el sistema.");
            return;
        }

        for (Lote lote : lotes) {
            lote.realizarLectura();
            System.out.println(lote.obtenerResumen());
            System.out.println("─────────────────────────────────────────────");
        }
    }

    /**
     * Gestiona los lotes (agregar, eliminar, modificar)
     */
    private void gestionarLotes() {
        System.out.println("🌱 === GESTIÓN DE LOTES ===");
        System.out.println("1. Agregar nuevo lote");
        System.out.println("2. Ver lotes existentes");
        System.out.println("3. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        
        int opcion = leerOpcion();
        
        switch (opcion) {
            case 1:
                agregarNuevoLote();
                break;
            case 2:
                mostrarLotesExistentes();
                break;
            case 3:
                return;
            default:
                System.out.println("❌ Opción inválida.");
        }
    }

    /**
     * Agrega un nuevo lote al sistema
     */
    private void agregarNuevoLote() {
        System.out.println("\n🌱 === AGREGAR NUEVO LOTE ===");
        
        System.out.print("Ingrese el ID del lote: ");
        String id = scanner.nextLine().trim();
        
        System.out.print("Ingrese el nombre del lote: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Ingrese el área en m²: ");
        try {
            double area = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Ingrese el tipo de cultivo: ");
            String tipoCultivo = scanner.nextLine().trim();
            
            Lote nuevoLote = new Lote(id, nombre, area, tipoCultivo);
            sistemaMonitoreo.agregarLote(nuevoLote);
            
            System.out.println("✅ Lote agregado exitosamente!");
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El área debe ser un número válido.");
        }
    }

    /**
     * Muestra todos los lotes existentes
     */
    private void mostrarLotesExistentes() {
        System.out.println("\n🌱 === LOTES EXISTENTES ===");
        
        List<Lote> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("ℹ️ No hay lotes registrados.");
            return;
        }

        for (int i = 0; i < lotes.size(); i++) {
            Lote lote = lotes.get(i);
            System.out.println((i + 1) + ". " + lote.toString());
        }
    }

    /**
     * Muestra todas las alertas del sistema
     */
    private void mostrarAlertas() {
        System.out.println("🚨 === ALERTAS DEL SISTEMA ===");
        
        List<Lote> lotes = sistemaMonitoreo.obtenerTodosLotes();
        boolean hayAlertas = false;
        
        for (Lote lote : lotes) {
            lote.realizarLectura();
            List<String> alertasLote = lote.getAlertas();
            
            if (!alertasLote.isEmpty()) {
                hayAlertas = true;
                System.out.println("\n🌱 " + lote.getNombre() + " (" + lote.getId() + ")");
                for (String alerta : alertasLote) {
                    System.out.println("   " + alerta);
                }
            }
        }
        
        if (!hayAlertas) {
            System.out.println("✅ No hay alertas activas en el sistema.");
        }
    }

    /**
     * Muestra recomendaciones para todos los lotes
     */
    private void mostrarRecomendaciones() {
        System.out.println("💡 === RECOMENDACIONES ===");
        
        List<Lote> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("ℹ️ No hay lotes registrados.");
            return;
        }

        for (Lote lote : lotes) {
            System.out.println("\n🌱 " + lote.getNombre() + " (" + lote.getId() + ")");
            List<Recomendacion> recomendaciones = motorRecomendaciones.generarRecomendacionesLote(lote);
            
            if (recomendaciones.isEmpty()) {
                System.out.println("   ✅ No hay recomendaciones específicas en este momento.");
            } else {
                for (Recomendacion rec : recomendaciones) {
                    System.out.println("   " + rec.obtenerVistaResumida());
                    System.out.println("      " + rec.getAccion());
                }
            }
        }
    }

    /**
     * Muestra estadísticas del sistema
     */
    private void mostrarEstadisticas() {
        System.out.println("📈 === ESTADÍSTICAS DEL SISTEMA ===");
        System.out.println(sistemaMonitoreo.obtenerEstadisticas());
        
        List<Lote> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (!lotes.isEmpty()) {
            long lotesOptimos = sistemaMonitoreo.obtenerLotesPorEstado("ÓPTIMO").size();
            long lotesAtencion = sistemaMonitoreo.obtenerLotesPorEstado("ATENCIÓN").size();
            long lotesCriticos = sistemaMonitoreo.obtenerLotesPorEstado("CRÍTICO").size();
            
            System.out.println("📊 DISTRIBUCIÓN DE ESTADOS:");
            System.out.println("   ✅ Óptimos: " + lotesOptimos);
            System.out.println("   ⚠️ Atención: " + lotesAtencion);
            System.out.println("   🚨 Críticos: " + lotesCriticos);
        }
    }

    /**
     * Gestiona el monitoreo automático
     */
    private void gestionarMonitoreoAutomatico() {
        System.out.println("🔄 === MONITOREO AUTOMÁTICO ===");
        
        if (sistemaMonitoreo.isMonitoreoActivo()) {
            System.out.println("El monitoreo automático está ACTIVO.");
            System.out.println("1. Detener monitoreo automático");
            System.out.println("2. Realizar lectura manual");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            int opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    sistemaMonitoreo.detenerMonitoreo();
                    break;
                case 2:
                    sistemaMonitoreo.realizarMonitoreoCompleto();
                    break;
            }
        } else {
            System.out.println("El monitoreo automático está INACTIVO.");
            System.out.println("1. Iniciar monitoreo automático");
            System.out.println("2. Realizar lectura manual");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            int opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    sistemaMonitoreo.iniciarMonitoreo();
                    break;
                case 2:
                    sistemaMonitoreo.realizarMonitoreoCompleto();
                    break;
            }
        }
    }

    /**
     * Muestra un plan de acción para un lote específico
     */
    private void mostrarPlanAccion() {
        System.out.println("📋 === PLAN DE ACCIÓN ===");
        
        List<Lote> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("ℹ️ No hay lotes registrados.");
            return;
        }

        System.out.println("Seleccione un lote para generar su plan de acción:");
        for (int i = 0; i < lotes.size(); i++) {
            System.out.println((i + 1) + ". " + lotes.get(i).toString());
        }
        
        System.out.print("Ingrese el número del lote: ");
        int seleccion = leerOpcion();
        
        if (seleccion >= 1 && seleccion <= lotes.size()) {
            Lote loteSeleccionado = lotes.get(seleccion - 1);
            loteSeleccionado.realizarLectura();
            
            String planAccion = motorRecomendaciones.generarPlanAccion(loteSeleccionado);
            System.out.println("\n" + planAccion);
        } else {
            System.out.println("❌ Selección inválida.");
        }
    }

    /**
     * Muestra la ayuda del sistema
     */
    private void mostrarAyuda() {
        System.out.println("❓ === AYUDA DEL SISTEMA AGROSENSE ===");
        System.out.println();
        System.out.println("🌱 AGROSENSE es un sistema de monitoreo agrícola inteligente que:");
        System.out.println("   • Monitorea humedad del suelo y temperatura ambiental");
        System.out.println("   • Genera alertas automáticas cuando detecta condiciones críticas");
        System.out.println("   • Proporciona recomendaciones específicas para cada lote");
        System.out.println("   • Optimiza el uso del agua y recursos agrícolas");
        System.out.println();
        System.out.println("📊 FUNCIONALIDADES PRINCIPALES:");
        System.out.println("   1. Monitoreo de Lotes: Visualiza el estado actual de todos los lotes");
        System.out.println("   2. Gestionar Lotes: Agrega, modifica o elimina lotes");
        System.out.println("   3. Ver Alertas: Revisa alertas críticas que requieren atención");
        System.out.println("   4. Recomendaciones: Obtén consejos específicos para cada lote");
        System.out.println("   5. Estadísticas: Ve el resumen general del sistema");
        System.out.println("   6. Monitoreo Automático: Activa/desactiva monitoreo continuo");
        System.out.println("   7. Plan de Acción: Genera un plan específico para un lote");
        System.out.println();
        System.out.println("🚨 TIPOS DE ALERTAS:");
        System.out.println("   🚨 CRÍTICA: Requiere acción inmediata");
        System.out.println("   ⚠️ ATENCIÓN: Requiere monitoreo y posible acción");
        System.out.println("   ℹ️ INFORMATIVA: Información útil para el agricultor");
        System.out.println();
        System.out.println("💡 TIPOS DE RECOMENDACIONES:");
        System.out.println("   • Riego: Cantidad y frecuencia de riego recomendada");
        System.out.println("   • Temperatura: Protección contra heladas o calor excesivo");
        System.out.println("   • Cultivos: Recomendaciones específicas por tipo de cultivo");
        System.out.println("   • General: Mantenimiento y monitoreo continuo");
    }
}
