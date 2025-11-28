package practica.paginaPagina.domain;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Cliente cliente;
    private Producto producto;
    private List<ItemPedido> items;
    boolean confirmado;
    double total;
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.confirmado = false;
        this.total = 0.0;
    }
    
    public void agregarItem(Producto producto, int cantidad){
        if(confirmado){
            throw new IllegalArgumentException("no puede agregar items a pedididos ya comfirmados");
        }
        ItemPedido nuevoItem= new ItemPedido(producto, cantidad);
        items.add(nuevoItem);
        System.out.println("has agregado "+nuevoItem);
    }
    public void calcularTotal(){
        this.total = items.stream()
                          .mapToDouble(ItemPedido :: getSubtotal)
                          .sum();
    }

    /**
     * Confirma el pedido, calcula el total final y bloquea futuras modificaciones.
     */
    public void confirmarPedido() {
        if (!confirmado) {
            calcularTotal(); // Se calcula el total justo antes de confirmar.
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
        resumen.append("Cliente: ").append(cliente.name()).append(" | Tel: ").append(cliente.telefono()).append("\n");
        resumen.append("--------------------------\n");
        resumen.append(String.format("TOTAL A PAGAR: $%,.2f\n", total));
        resumen.append("Estado: ").append(confirmado ? "CONFIRMADO" : "EN PROCESO").append("\n");
        return resumen.toString();
    }
}
