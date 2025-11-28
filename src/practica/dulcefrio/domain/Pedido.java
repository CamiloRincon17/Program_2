package practica.dulcefrio.domain;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final Cliente cliente;
    private final List<ItemPedido> items;
    private boolean confirmado;
    private double total;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.confirmado = false;
        this.total = 0.0;
    }

    /**
     * Añade un nuevo item (producto y cantidad) al pedido.
     * Lanza una excepción si el pedido ya está confirmado.
     */
    public void agregarItem(Producto producto, int cantidad) {
        if (confirmado) {
            throw new IllegalStateException("No se pueden añadir items a un pedido confirmado.");
        }
        ItemPedido nuevoItem = new ItemPedido(producto, cantidad);
        items.add(nuevoItem);
    }

    /**
     * Calcula el costo total del pedido sumando los subtotales de cada item.
     */
    private void calcularTotal() {
        this.total = items.stream()
                          .mapToDouble(ItemPedido::getSubtotal)
                          .sum();
    }

    /**
     * Confirma el pedido, calcula el total final y bloquea futuras modificaciones.
     */
    public void confirmarPedido() {
        if (!confirmado) {
            calcularTotal();
            this.confirmado = true;
        }
    }

    /**
     * Genera un resumen detallado del pedido para ser impreso.
     */
    public String generarResumen() {
        // Asegurarse de que el total esté calculado si el pedido está confirmado
        if (confirmado) {
            calcularTotal();
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("--- Resumen del Pedido ---\n");
        resumen.append("Cliente: ").append(cliente.nombre()).append(" | Tel: ").append(cliente.telefono()).append("\n");
        resumen.append("--------------------------\n");
        resumen.append("Detalle de Items:\n");
        items.forEach(item -> resumen.append(item.generarDetalle()).append("\n"));
        resumen.append("--------------------------\n");
        resumen.append(String.format("TOTAL A PAGAR: $%,.2f\n", total));
        resumen.append("Estado: ").append(confirmado ? "CONFIRMADO" : "EN PROCESO").append("\n");
        resumen.append("--------------------------\n");
        return resumen.toString();
    }

    // Getters
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItems() { return new ArrayList<>(items); } // Devuelve una copia
    public boolean isConfirmado() { return confirmado; }
    public double getTotal() { return total; }
}
