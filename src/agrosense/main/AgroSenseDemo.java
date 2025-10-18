package agrosense.main;

import agrosense.model.LoteSimple;
import agrosense.monitoring.SistemaMonitoreoSimple;
import agrosense.recommendations.MotorRecomendacionesSimple;
import agrosense.recommendations.RecomendacionSimple;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal del sistema AgroSense (version demo simplificada).
 * Maneja la interfaz de usuario y coordina todas las funcionalidades del sistema.
 */
public class AgroSenseDemo {
    private SistemaMonitoreoSimple sistemaMonitoreo;
    private MotorRecomendacionesSimple motorRecomendaciones;
    private Scanner scanner;
    private boolean sistemaActivo;

    /**
     * Constructor del sistema principal
     */
    public AgroSenseDemo() {
        this.sistemaMonitoreo = new SistemaMonitoreoSimple();
        this.motorRecomendaciones = new MotorRecomendacionesSimple();
        this.scanner = new Scanner(System.in);
        this.sistemaActivo = true;
    }

    /**
     * Método principal que inicia el sistema
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        AgroSenseDemo agroSense = new AgroSenseDemo();
        agroSense.mostrarBienvenida();
        agroSense.inicializarLotesEjemplo();
        agroSense.ejecutarSistema();
    }

    /**
     * Muestra el mensaje de bienvenida del sistema
     */
    private void mostrarBienvenida() {
        System.out.println("==============================================");
        System.out.println("           AGROSENSE - Sistema de              ");
        System.out.println("         Monitoreo Agricola Inteligente        ");
        System.out.println("==============================================");
        System.out.println(" Desarrollado por:                            ");
        System.out.println("   • Jhoan Alexander Molina Gomez (192490)    ");
        System.out.println("   • Adrian Camilo Rincon Ascanio (192531)    ");
        System.out.println("   • Isaac David Garcia Vesga (192...)        ");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("¡Bienvenido al sistema de monitoreo agricola!");
        System.out.println("Monitoreo en tiempo real de humedad y temperatura");
        System.out.println("Alertas inteligentes y recomendaciones personalizadas");
        System.out.println();
    }

    /**
     * Inicializa lotes de ejemplo para demostración
     */
    private void inicializarLotesEjemplo() {
        System.out.println("Inicializando lotes de ejemplo...");
        
        // Crear lotes de ejemplo
        LoteSimple lote1 = new LoteSimple("LOTE001", "Lote Norte - Tomates", 100.0, "Tomate");
        LoteSimple lote2 = new LoteSimple("LOTE002", "Lote Sur - Lechugas", 75.0, "Lechuga");
        LoteSimple lote3 = new LoteSimple("LOTE003", "Lote Este - Papas", 150.0, "Papa");
        LoteSimple lote4 = new LoteSimple("LOTE004", "Lote Oeste - Maiz", 200.0, "Maiz");

        // Agregar lotes al sistema
        sistemaMonitoreo.agregarLote(lote1);
        sistemaMonitoreo.agregarLote(lote2);
        sistemaMonitoreo.agregarLote(lote3);
        sistemaMonitoreo.agregarLote(lote4);

        System.out.println("Se han inicializado " + sistemaMonitoreo.getCantidadLotes() + " lotes de ejemplo.");
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
        System.out.println("¡Gracias por usar AgroSense! Hasta luego.");
    }

    /**
     * Muestra el menú principal del sistema
     */
    private void mostrarMenuPrincipal() {
        System.out.println("+---------------------------------------------+");
        System.out.println("|              MENU PRINCIPAL                 |");
        System.out.println("+---------------------------------------------+");
        System.out.println("| 1. Monitoreo de Lotes                      |");
        System.out.println("| 2. Gestionar Lotes                         |");
        System.out.println("| 3. Ver Alertas                             |");
        System.out.println("| 4. Recomendaciones                         |");
        System.out.println("| 5. Estadisticas del Sistema                |");
        System.out.println("| 6. Monitoreo Automatico                    |");
        System.out.println("| 7. Plan de Accion                          |");
        System.out.println("| 8. Ayuda                                   |");
        System.out.println("| 9. Salir                                   |");
        System.out.println("+---------------------------------------------+");
        System.out.print("Seleccione una opcion: ");
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
                System.out.println("Opcion invalida. Por favor, seleccione una opcion del 1 al 9.");
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
        System.out.println("=== MONITOREO DE LOTES ===");
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("No hay lotes registrados en el sistema.");
            return;
        }

        for (LoteSimple lote : lotes) {
            lote.realizarLectura();
            System.out.println(lote.obtenerResumen());
            System.out.println("---------------------------------------------");
        }
    }

    /**
     * Gestiona los lotes (agregar, eliminar, modificar)
     */
    private void gestionarLotes() {
        System.out.println("=== GESTION DE LOTES ===");
        System.out.println("1. Agregar nuevo lote");
        System.out.println("2. Ver lotes existentes");
        System.out.println("3. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
        
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
                System.out.println("Opcion invalida.");
        }
    }

    /**
     * Agrega un nuevo lote al sistema
     */
    private void agregarNuevoLote() {
        System.out.println("\n=== AGREGAR NUEVO LOTE ===");
        
        System.out.print("Ingrese el ID del lote: ");
        String id = scanner.nextLine().trim();
        
        System.out.print("Ingrese el nombre del lote: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Ingrese el area en m2: ");
        try {
            double area = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Ingrese el tipo de cultivo: ");
            String tipoCultivo = scanner.nextLine().trim();
            
            LoteSimple nuevoLote = new LoteSimple(id, nombre, area, tipoCultivo);
            sistemaMonitoreo.agregarLote(nuevoLote);
            
            System.out.println("Lote agregado exitosamente!");
            
        } catch (NumberFormatException e) {
            System.out.println("Error: El area debe ser un numero valido.");
        }
    }

    /**
     * Muestra todos los lotes existentes
     */
    private void mostrarLotesExistentes() {
        System.out.println("\n=== LOTES EXISTENTES ===");
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("No hay lotes registrados.");
            return;
        }

        for (int i = 0; i < lotes.size(); i++) {
            LoteSimple lote = lotes.get(i);
            System.out.println((i + 1) + ". " + lote.toString());
        }
    }

    /**
     * Muestra todas las alertas del sistema
     */
    private void mostrarAlertas() {
        System.out.println("=== ALERTAS DEL SISTEMA ===");
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        boolean hayAlertas = false;
        
        for (LoteSimple lote : lotes) {
            lote.realizarLectura();
            List<String> alertasLote = lote.getAlertas();
            
            if (!alertasLote.isEmpty()) {
                hayAlertas = true;
                System.out.println("\n" + lote.getNombre() + " (" + lote.getId() + ")");
                for (String alerta : alertasLote) {
                    System.out.println("   " + alerta);
                }
            }
        }
        
        if (!hayAlertas) {
            System.out.println("No hay alertas activas en el sistema.");
        }
    }

    /**
     * Muestra recomendaciones para todos los lotes
     */
    private void mostrarRecomendaciones() {
        System.out.println("=== RECOMENDACIONES ===");
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("No hay lotes registrados.");
            return;
        }

        for (LoteSimple lote : lotes) {
            System.out.println("\n" + lote.getNombre() + " (" + lote.getId() + ")");
            List<RecomendacionSimple> recomendaciones = motorRecomendaciones.generarRecomendacionesLote(lote);
            
            if (recomendaciones.isEmpty()) {
                System.out.println("   No hay recomendaciones especificas en este momento.");
            } else {
                for (RecomendacionSimple rec : recomendaciones) {
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
        System.out.println("=== ESTADISTICAS DEL SISTEMA ===");
        System.out.println(sistemaMonitoreo.obtenerEstadisticas());
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (!lotes.isEmpty()) {
            long lotesOptimos = sistemaMonitoreo.obtenerLotesPorEstado("OPTIMO").size();
            long lotesAtencion = sistemaMonitoreo.obtenerLotesPorEstado("ATENCION").size();
            long lotesCriticos = sistemaMonitoreo.obtenerLotesPorEstado("CRITICO").size();
            
            System.out.println("DISTRIBUCION DE ESTADOS:");
            System.out.println("   Optimos: " + lotesOptimos);
            System.out.println("   Atencion: " + lotesAtencion);
            System.out.println("   Criticos: " + lotesCriticos);
        }
    }

    /**
     * Gestiona el monitoreo automático
     */
    private void gestionarMonitoreoAutomatico() {
        System.out.println("=== MONITOREO AUTOMATICO ===");
        
        if (sistemaMonitoreo.isMonitoreoActivo()) {
            System.out.println("El monitoreo automatico esta ACTIVO.");
            System.out.println("1. Detener monitoreo automatico");
            System.out.println("2. Realizar lectura manual");
            System.out.println("3. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
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
            System.out.println("El monitoreo automatico esta INACTIVO.");
            System.out.println("1. Iniciar monitoreo automatico");
            System.out.println("2. Realizar lectura manual");
            System.out.println("3. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            
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
        System.out.println("=== PLAN DE ACCION ===");
        
        List<LoteSimple> lotes = sistemaMonitoreo.obtenerTodosLotes();
        if (lotes.isEmpty()) {
            System.out.println("No hay lotes registrados.");
            return;
        }

        System.out.println("Seleccione un lote para generar su plan de accion:");
        for (int i = 0; i < lotes.size(); i++) {
            System.out.println((i + 1) + ". " + lotes.get(i).toString());
        }
        
        System.out.print("Ingrese el numero del lote: ");
        int seleccion = leerOpcion();
        
        if (seleccion >= 1 && seleccion <= lotes.size()) {
            LoteSimple loteSeleccionado = lotes.get(seleccion - 1);
            loteSeleccionado.realizarLectura();
            
            String planAccion = motorRecomendaciones.generarPlanAccion(loteSeleccionado);
            System.out.println("\n" + planAccion);
        } else {
            System.out.println("Seleccion invalida.");
        }
    }

    /**
     * Muestra la ayuda del sistema
     */
    private void mostrarAyuda() {
        System.out.println("=== AYUDA DEL SISTEMA AGROSENSE ===");
        System.out.println();
        System.out.println("AGROSENSE es un sistema de monitoreo agricola inteligente que:");
        System.out.println("   • Monitorea humedad del suelo y temperatura ambiental");
        System.out.println("   • Genera alertas automaticas cuando detecta condiciones criticas");
        System.out.println("   • Proporciona recomendaciones especificas para cada lote");
        System.out.println("   • Optimiza el uso del agua y recursos agricolas");
        System.out.println();
        System.out.println("FUNCIONALIDADES PRINCIPALES:");
        System.out.println("   1. Monitoreo de Lotes: Visualiza el estado actual de todos los lotes");
        System.out.println("   2. Gestionar Lotes: Agrega, modifica o elimina lotes");
        System.out.println("   3. Ver Alertas: Revisa alertas criticas que requieren atencion");
        System.out.println("   4. Recomendaciones: Obten consejos especificos para cada lote");
        System.out.println("   5. Estadisticas: Ve el resumen general del sistema");
        System.out.println("   6. Monitoreo Automatico: Activa/desactiva monitoreo continuo");
        System.out.println("   7. Plan de Accion: Genera un plan especifico para un lote");
        System.out.println();
        System.out.println("TIPOS DE ALERTAS:");
        System.out.println("   CRITICA: Requiere accion inmediata");
        System.out.println("   ATENCION: Requiere monitoreo y posible accion");
        System.out.println("   INFORMATIVA: Informacion util para el agricultor");
        System.out.println();
        System.out.println("TIPOS DE RECOMENDACIONES:");
        System.out.println("   • Riego: Cantidad y frecuencia de riego recomendada");
        System.out.println("   • Temperatura: Proteccion contra heladas o calor excesivo");
        System.out.println("   • Cultivos: Recomendaciones especificas por tipo de cultivo");
        System.out.println("   • General: Mantenimiento y monitoreo continuo");
    }
}
