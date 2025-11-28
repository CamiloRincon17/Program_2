package practica.dulcefrio.domain;

import java.util.Objects;

/**
 * Representa una línea de un pedido, con un producto y una cantidad.
 * Se encarga de calcular el subtotal para ese item, aplicando descuentos si es necesario.
 */
public class ItemPedido {
    private final Producto producto;
    private final int cantidad;
    private final double subtotal;
    private final boolean descuentoAplicado;

    public ItemPedido(Producto producto, int cantidad) {
        this.producto = Objects.requireNonNull(producto, "El producto no puede ser nulo");
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;

        // Lógica de descuento: 10% si la cantidad es 3 o más
        if (cantidad >= 3) {
            this.subtotal = (producto.getPrecio() * cantidad) * 0.90;
            this.descuentoAplicado = true;
        } else {
            this.subtotal = producto.getPrecio() * cantidad;
            this.descuentoAplicado = false;
        }
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }
    public boolean isDescuentoAplicado() { return descuentoAplicado; }

    public String generarDetalle() {
        String detalle = String.format("  - %-10s | %2d u. | $%7.2f | Subtotal: $%8.2f", producto.getNombre(), cantidad, producto.getPrecio(), subtotal);
        return descuentoAplicado ? detalle + " (Dto. 10% incl.)" : detalle;
    }
}