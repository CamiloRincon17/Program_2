package practica.dulcefrio.app;

import practica.dulcefrio.domain.Cliente;
import practica.dulcefrio.domain.Pedido;
import practica.dulcefrio.domain.Producto;
import practica.dulcefrio.service.Heladeria;

/**
 * Clase principal para ejecutar la aplicación de la heladería.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("🍦 ¡Bienvenido a la Heladería Dulce Frío! 🍦");

        // Se instancia el servicio de la heladería
        Heladeria heladeria = new Heladeria();

        // 1. Registrar nombre y teléfono del cliente.
        Cliente cliente = new Cliente("Ana García", 310123456);

        // Se usa el servicio para crear el pedido
        Pedido pedido = heladeria.crearPedido(cliente);
        System.out.println("\nNuevo pedido para: " + cliente.nombre());

        // 2. Añadir uno o varios sabores con sus cantidades.
        try {
            // Este item tendrá descuento
            pedido.agregarItem(Producto.CHOCOLATE, 4);
            System.out.println("Añadido: 4 bolas de Chocolate");

            // Este item no tendrá descuento
            pedido.agregarItem(Producto.VAINILLA, 1);
            System.out.println("Añadido: 1 bola de Vainilla");

            // Este item tendrá descuento
            pedido.agregarItem(Producto.FRESA, 3);
            System.out.println("Añadido: 3 bolas de Fresa");
        } catch (IllegalArgumentException e) {
            System.err.println("Error al añadir item: " + e.getMessage());
        }

        // 4. Bloquear modificaciones tras confirmar el pedido.
        pedido.confirmarPedido();
        System.out.println("\n>> Pedido confirmado. Calculando total...");

        // 5. Generar un resumen detallado del pedido.
        System.out.println("\n" + pedido.generarResumen());
    }
}
