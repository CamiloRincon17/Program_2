package limoncito.domain;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    public enum EstadoPedido {
        EN_PROCESO,
        CONFIRMADO,
        CANCELADO
    }

    public record ResumenPedido(
            Cliente cliente,
            List<ItemPedido> items,
            boolean servicioExpres,
            double totalBruto,
            double recargoExpres,
            double descuentoPorVolumen,
            double totalFinal
    ) {
        public String generarTextoResumen() {
            StringBuilder resumen = new StringBuilder();
            resumen.append("--- Resumen de Orden ---\n");
            resumen.append(cliente).append("\n");
            resumen.append("------------------------\n");
            resumen.append("Items:\n");
            items.forEach(item -> resumen.append("- ").append(item).append("\n"));
            resumen.append("------------------------\n");
            resumen.append(String.format("Total Bruto: $%.2f\n", totalBruto));
            if (servicioExpres) {
                resumen.append(String.format("Recargo Exprés (10%%): $%.2f\n", recargoExpres));
            }
            if (descuentoPorVolumen > 0) {
                resumen.append(String.format("Descuento por Volumen (5%%): -$%.2f\n", descuentoPorVolumen));
            }
            resumen.append("------------------------\n");
            resumen.append(String.format("Total Final: $%.2f\n", totalFinal));
            resumen.append("\n--- Gracias por su preferencia ---");

            return resumen.toString();
        }
    }

    private Cliente cliente;
    private List<ItemPedido> items = new ArrayList<>();
    private boolean servicioExpres;
    private EstadoPedido estado = EstadoPedido.EN_PROCESO;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    }

    public void agregarItem(ItemPedido item) {
        if (this.estado != EstadoPedido.EN_PROCESO) {
            throw new IllegalStateException("No se puede modificar un pedido confirmado.");
        }
        items.add(item);
    }

    public void setServicioExpres(boolean servicioExpres) {
        if (estado == EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("No se puede modificar un pedido que no está en proceso.");
        }
        this.servicioExpres = servicioExpres;
    }

    public ResumenPedido confirmarPedido() {
        if (this.estado != EstadoPedido.EN_PROCESO) {
            throw new IllegalStateException("El pedido ya ha sido confirmado.");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un pedido vacío.");
        }
        
        ResumenPedido resumen = calcularTotales();
        this.estado = EstadoPedido.CONFIRMADO;
        return resumen;
    }

    private ResumenPedido calcularTotales() {
        double totalBruto = items.stream().mapToDouble(ItemPedido::calcularSubtotal).sum();
        double descuento = 0;
        double recargo = 0;

        // El descuento se aplica sobre el total bruto de los productos.
        if (totalBruto > 60000) {
            descuento = totalBruto * 0.05;
        }

        double subtotalConDescuento = totalBruto - descuento;

        // El recargo exprés se calcula sobre el total bruto.
        if (servicioExpres) {
            recargo = totalBruto * 0.10;
        }

        double totalFinal = subtotalConDescuento + recargo;

        return new ResumenPedido(cliente, List.copyOf(items), servicioExpres, totalBruto, recargo, descuento, totalFinal);
    }

    public boolean isConfirmado() {
        return estado == EstadoPedido.CONFIRMADO;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
