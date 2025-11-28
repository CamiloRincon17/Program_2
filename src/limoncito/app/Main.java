package limoncito.app;

import limoncito.domain.Cliente;
import limoncito.domain.ItemPedido;
import limoncito.domain.Pedido;
import limoncito.domain.Producto;
import limoncito.service.LimoncitoService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenido a la Lavandería 'El Limoncito'");

        LimoncitoService service = new LimoncitoService();

        try {
            // RF1: Registrar cliente
            Cliente cliente = new Cliente("Juan Perez", "3001234567");
            System.out.println("\nCliente registrado: " + cliente.nombre());

            // Crear Pedido
            Pedido pedido = service.crearPedido(cliente);
            System.out.println("Pedido creado para: " + pedido.getCliente().nombre());

            // RF2: Mostrar catálogo y agregar items
            System.out.println("\n--- Agregando productos al pedido ---");
            pedido.agregarItem(new ItemPedido(Producto.CAMISA, 5));
            System.out.println("Añadido: 5 Camisas");

            pedido.agregarItem(new ItemPedido(Producto.PANTALON, 10));
            System.out.println("Añadido: 10 Pantalones");

            pedido.agregarItem(new ItemPedido(Producto.CHAQUETA, 1));
            System.out.println("Añadido: 1 Chaqueta");

            // Requerimiento Opcional: Servicio Exprés
            pedido.setServicioExpres(true);
            System.out.println("\nServicio exprés solicitado.");

            // RF5: Confirmar Pedido y RF6: Generar Resumen
            Pedido.ResumenPedido resumen = pedido.confirmarPedido();
            System.out.println("\n>> Pedido confirmado. Calculando total...");
            System.out.println("\n" + resumen.generarTextoResumen());

            // --- Demostración de la regla de negocio: no se puede modificar un pedido confirmado ---
            System.out.println("\n--- Intentando modificar pedido confirmado (debería fallar) ---");
            try {
                pedido.agregarItem(new ItemPedido(Producto.CAMISA, 1));
            } catch (IllegalStateException e) {
                System.out.println("Correcto: " + e.getMessage());
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("\nERROR EN LA OPERACIÓN: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nHA OCURRIDO UN ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}