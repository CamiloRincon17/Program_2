package practica.elGranoFino.domain;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Cliente cliente;
    private List<ItemPedido> items;
    private double total;
    private boolean confirmado;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.confirmado = false;
    }

    public void agregarPedidido(Producto producto, int cantidad) {
        if (confirmado) {
            throw new IllegalStateException("El pedido ya está confirmado, no se pueden agregar más items.");
        }
        ItemPedido nuevoPedido = new ItemPedido(producto, cantidad);
        items.add(nuevoPedido);
        System.out.println("Has agregado"+nuevoPedido);

    }

    public void calcularTotal() {
        this.total = items.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
    }

    public void confirmarPedidido() {
        if (confirmado) {
            throw new IllegalStateException("El pedido ya fue confirmado previamente.");
        }
        this.confirmado = true;
    }

    @Override
    public String toString() {
        return "Pedido [cliente=" + cliente + ",\n items=" + items + ",\n total=" + total + ",\n confirmado=" + confirmado
                + "]";
    }


}
