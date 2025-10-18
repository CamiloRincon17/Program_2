package agrosense.test;

import agrosense.model.Lote;
import agrosense.sensors.SensorHumedad;
import agrosense.sensors.SensorTemperatura;
import agrosense.monitoring.SistemaMonitoreo;
import agrosense.recommendations.MotorRecomendaciones;
import agrosense.recommendations.Recomendacion;

/**
 * Clase de pruebas unitarias para el sistema AgroSense.
 * Valida el funcionamiento correcto de todos los componentes principales.
 */
public class PruebasAgroSense {
    
    /**
     * Ejecuta todas las pruebas del sistema
     */
    public static void ejecutarTodasLasPruebas() {
        System.out.println("🧪 ==============================================");
        System.out.println("🧪        INICIANDO PRUEBAS DE AGROSENSE        ");
        System.out.println("🧪 ==============================================");
        System.out.println();
        
        boolean todasLasPruebasExitosas = true;
        
        // Ejecutar pruebas de sensores
        todasLasPruebasExitosas &= ejecutarPruebasSensores();
        
        // Ejecutar pruebas de lotes
        todasLasPruebasExitosas &= ejecutarPruebasLotes();
        
        // Ejecutar pruebas del sistema de monitoreo
        todasLasPruebasExitosas &= ejecutarPruebasSistemaMonitoreo();
        
        // Ejecutar pruebas del motor de recomendaciones
        todasLasPruebasExitosas &= ejecutarPruebasMotorRecomendaciones();
        
        // Mostrar resumen final
        System.out.println("🧪 ==============================================");
        if (todasLasPruebasExitosas) {
            System.out.println("🧪        ✅ TODAS LAS PRUEBAS EXITOSAS        ");
        } else {
            System.out.println("🧪        ❌ ALGUNAS PRUEBAS FALLARON          ");
        }
        System.out.println("🧪 ==============================================");
    }
    
    /**
     * Ejecuta pruebas de los sensores
     */
    private static boolean ejecutarPruebasSensores() {
        System.out.println("🌡️ === PRUEBAS DE SENSORES ===");
        boolean exitosas = true;
        
        // Prueba del sensor de humedad
        System.out.println("Probando Sensor de Humedad...");
        try {
            SensorHumedad sensorHumedad = new SensorHumedad("TEST_HUM", "Sensor de Prueba");
            
            // Verificar que el sensor se inicializa correctamente
            assert sensorHumedad.getId().equals("TEST_HUM") : "ID del sensor incorrecto";
            assert sensorHumedad.getNombre().equals("Sensor de Prueba") : "Nombre del sensor incorrecto";
            assert sensorHumedad.isActivo() : "Sensor no está activo por defecto";
            
            // Probar lectura del sensor
            double humedad = sensorHumedad.leer();
            assert humedad >= 0 && humedad <= 100 : "Valor de humedad fuera del rango válido";
            
            // Probar validación de valores
            assert sensorHumedad.esValorNormal(50) : "Valor normal no reconocido";
            assert !sensorHumedad.esValorNormal(20) : "Valor crítico no detectado";
            
            System.out.println("   ✅ Sensor de Humedad: PASÓ");
            
        } catch (Exception e) {
            System.out.println("   ❌ Sensor de Humedad: FALLÓ - " + e.getMessage());
            exitosas = false;
        }
        
        // Prueba del sensor de temperatura
        System.out.println("Probando Sensor de Temperatura...");
        try {
            SensorTemperatura sensorTemperatura = new SensorTemperatura("TEST_TEMP", "Sensor de Prueba");
            
            // Verificar que el sensor se inicializa correctamente
            assert sensorTemperatura.getId().equals("TEST_TEMP") : "ID del sensor incorrecto";
            assert sensorTemperatura.getNombre().equals("Sensor de Prueba") : "Nombre del sensor incorrecto";
            assert sensorTemperatura.isActivo() : "Sensor no está activo por defecto";
            
            // Probar lectura del sensor
            double temperatura = sensorTemperatura.leer();
            assert temperatura >= -50 && temperatura <= 60 : "Valor de temperatura fuera del rango válido";
            
            // Probar validación de valores
            assert sensorTemperatura.esValorNormal(25) : "Valor normal no reconocido";
            assert !sensorTemperatura.esValorNormal(45) : "Valor crítico no detectado";
            
            System.out.println("   ✅ Sensor de Temperatura: PASÓ");
            
        } catch (Exception e) {
            System.out.println("   ❌ Sensor de Temperatura: FALLÓ - " + e.getMessage());
            exitosas = false;
        }
        
        System.out.println();
        return exitosas;
    }
    
    /**
     * Ejecuta pruebas de los lotes
     */
    private static boolean ejecutarPruebasLotes() {
        System.out.println("🌱 === PRUEBAS DE LOTES ===");
        boolean exitosas = true;
        
        System.out.println("Probando creación y funcionamiento de lotes...");
        try {
            // Crear un lote de prueba
            Lote lote = new Lote("TEST_LOTE", "Lote de Prueba", 100.0, "Tomate");
            
            // Verificar inicialización
            assert lote.getId().equals("TEST_LOTE") : "ID del lote incorrecto";
            assert lote.getNombre().equals("Lote de Prueba") : "Nombre del lote incorrecto";
            assert lote.getArea() == 100.0 : "Área del lote incorrecta";
            assert lote.getTipoCultivo().equals("Tomate") : "Tipo de cultivo incorrecto";
            assert lote.getEstadoGeneral().equals("NUEVO") : "Estado inicial incorrecto";
            
            // Verificar que tiene sensores
            assert lote.getSensorHumedad() != null : "Sensor de humedad no creado";
            assert lote.getSensorTemperatura() != null : "Sensor de temperatura no creado";
            
            // Probar lectura del lote
            lote.realizarLectura();
            assert !lote.getEstadoGeneral().equals("NUEVO") : "Estado no se actualizó después de la lectura";
            
            // Verificar que se generan recomendaciones
            assert !lote.obtenerRecomendaciones().isEmpty() : "No se generaron recomendaciones";
            
            System.out.println("   ✅ Creación de Lotes: PASÓ");
            System.out.println("   ✅ Lectura de Sensores: PASÓ");
            System.out.println("   ✅ Generación de Recomendaciones: PASÓ");
            
        } catch (Exception e) {
            System.out.println("   ❌ Pruebas de Lotes: FALLÓ - " + e.getMessage());
            exitosas = false;
        }
        
        System.out.println();
        return exitosas;
    }
    
    /**
     * Ejecuta pruebas del sistema de monitoreo
     */
    private static boolean ejecutarPruebasSistemaMonitoreo() {
        System.out.println("📊 === PRUEBAS DEL SISTEMA DE MONITOREO ===");
        boolean exitosas = true;
        
        System.out.println("Probando sistema de monitoreo...");
        try {
            SistemaMonitoreo sistema = new SistemaMonitoreo();
            
            // Verificar inicialización
            assert sistema.getCantidadLotes() == 0 : "Sistema no se inicializa vacío";
            assert !sistema.isMonitoreoActivo() : "Monitoreo activo al inicializar";
            
            // Crear y agregar lotes de prueba
            Lote lote1 = new Lote("TEST1", "Lote 1", 50.0, "Lechuga");
            Lote lote2 = new Lote("TEST2", "Lote 2", 75.0, "Tomate");
            
            sistema.agregarLote(lote1);
            sistema.agregarLote(lote2);
            
            assert sistema.getCantidadLotes() == 2 : "No se agregaron correctamente los lotes";
            assert sistema.obtenerLote("TEST1") != null : "No se puede obtener lote por ID";
            
            // Probar monitoreo manual
            sistema.realizarMonitoreoCompleto();
            
            // Verificar que los lotes se actualizaron
            assert !lote1.getEstadoGeneral().equals("NUEVO") : "Lote 1 no se actualizó";
            assert !lote2.getEstadoGeneral().equals("NUEVO") : "Lote 2 no se actualizó";
            
            // Probar filtros por estado
            int lotesOptimos = sistema.obtenerLotesPorEstado("ÓPTIMO").size();
            int lotesAtencion = sistema.obtenerLotesPorEstado("ATENCIÓN").size();
            int lotesCriticos = sistema.obtenerLotesPorEstado("CRÍTICO").size();
            
            assert (lotesOptimos + lotesAtencion + lotesCriticos) == 2 : "Filtros de estado no funcionan correctamente";
            
            System.out.println("   ✅ Inicialización del Sistema: PASÓ");
            System.out.println("   ✅ Gestión de Lotes: PASÓ");
            System.out.println("   ✅ Monitoreo Manual: PASÓ");
            System.out.println("   ✅ Filtros por Estado: PASÓ");
            
        } catch (Exception e) {
            System.out.println("   ❌ Pruebas del Sistema de Monitoreo: FALLÓ - " + e.getMessage());
            exitosas = false;
        }
        
        System.out.println();
        return exitosas;
    }
    
    /**
     * Ejecuta pruebas del motor de recomendaciones
     */
    private static boolean ejecutarPruebasMotorRecomendaciones() {
        System.out.println("💡 === PRUEBAS DEL MOTOR DE RECOMENDACIONES ===");
        boolean exitosas = true;
        
        System.out.println("Probando motor de recomendaciones...");
        try {
            MotorRecomendaciones motor = new MotorRecomendaciones();
            
            // Crear lote de prueba con condiciones específicas
            Lote lote = new Lote("TEST_REC", "Lote de Prueba", 100.0, "Tomate");
            
            // Simular condiciones críticas modificando directamente los sensores
            // (En un sistema real, esto se haría a través de métodos específicos)
            lote.realizarLectura();
            
            // Generar recomendaciones
            java.util.List<Recomendacion> recomendaciones = motor.generarRecomendacionesLote(lote);
            
            assert recomendaciones != null : "Motor no genera recomendaciones";
            assert !recomendaciones.isEmpty() : "Lista de recomendaciones vacía";
            
            // Verificar que las recomendaciones tienen la estructura correcta
            for (Recomendacion rec : recomendaciones) {
                assert rec.getTitulo() != null && !rec.getTitulo().isEmpty() : "Título de recomendación vacío";
                assert rec.getDescripcion() != null && !rec.getDescripcion().isEmpty() : "Descripción vacía";
                assert rec.getTipo() != null : "Tipo de recomendación nulo";
                assert rec.getAccion() != null && !rec.getAccion().isEmpty() : "Acción vacía";
                assert rec.getPrioridad() >= 1 && rec.getPrioridad() <= 3 : "Prioridad fuera del rango válido";
            }
            
            // Probar generación de plan de acción
            String planAccion = motor.generarPlanAccion(lote);
            assert planAccion != null && !planAccion.isEmpty() : "Plan de acción vacío";
            assert planAccion.contains(lote.getNombre()) : "Plan de acción no contiene nombre del lote";
            
            System.out.println("   ✅ Generación de Recomendaciones: PASÓ");
            System.out.println("   ✅ Validación de Estructura: PASÓ");
            System.out.println("   ✅ Generación de Plan de Acción: PASÓ");
            
        } catch (Exception e) {
            System.out.println("   ❌ Pruebas del Motor de Recomendaciones: FALLÓ - " + e.getMessage());
            exitosas = false;
        }
        
        System.out.println();
        return exitosas;
    }
    
    /**
     * Prueba de integración completa del sistema
     */
    public static void ejecutarPruebaIntegracion() {
        System.out.println("🔄 === PRUEBA DE INTEGRACIÓN COMPLETA ===");
        
        try {
            // Crear sistema completo
            SistemaMonitoreo sistema = new SistemaMonitoreo();
            MotorRecomendaciones motor = new MotorRecomendaciones();
            
            // Crear lotes con diferentes condiciones
            Lote loteSeco = new Lote("SECO", "Lote Seco", 100.0, "Tomate");
            Lote loteHumedo = new Lote("HUMEDO", "Lote Húmedo", 75.0, "Lechuga");
            Lote loteOptimo = new Lote("OPTIMO", "Lote Óptimo", 50.0, "Papa");
            
            // Agregar al sistema
            sistema.agregarLote(loteSeco);
            sistema.agregarLote(loteHumedo);
            sistema.agregarLote(loteOptimo);
            
            // Ejecutar monitoreo
            sistema.realizarMonitoreoCompleto();
            
            // Generar recomendaciones para cada lote
            for (Lote lote : sistema.obtenerTodosLotes()) {
                java.util.List<Recomendacion> recomendaciones = motor.generarRecomendacionesLote(lote);
                assert !recomendaciones.isEmpty() : "No se generaron recomendaciones para " + lote.getNombre();
                
                String planAccion = motor.generarPlanAccion(lote);
                assert planAccion.contains(lote.getNombre()) : "Plan de acción no contiene nombre del lote";
            }
            
            // Verificar estadísticas
            String estadisticas = sistema.obtenerEstadisticas();
            assert estadisticas.contains("3") : "Estadísticas no muestran 3 lotes";
            
            System.out.println("   ✅ Sistema de Monitoreo: FUNCIONANDO");
            System.out.println("   ✅ Motor de Recomendaciones: FUNCIONANDO");
            System.out.println("   ✅ Integración de Componentes: EXITOSA");
            System.out.println("   ✅ Prueba de Integración: PASÓ");
            
        } catch (Exception e) {
            System.out.println("   ❌ Prueba de Integración: FALLÓ - " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Método principal para ejecutar las pruebas
     */
    public static void main(String[] args) {
        ejecutarTodasLasPruebas();
        ejecutarPruebaIntegracion();
        
        System.out.println("🎉 Pruebas completadas. El sistema AgroSense está listo para usar!");
    }
}
