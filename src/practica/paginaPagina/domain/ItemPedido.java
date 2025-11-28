package practica.paginaPagina.domain;

import java.util.Objects;

public class ItemPedido {
    private final Producto producto;
    private final int cantidad;
    private final boolean seAplicaDescuento;
    private final double subtotal;

    /**
     * Constructor que inicializa un item del pedido.
     * Calcula el subtotal y si se aplica un descuento automáticamente
     */
    public ItemPedido(Producto producto, int cantidad) {
        this.producto = Objects.requireNonNull(producto, "El producto no debe ser nulo");
        
        // 1. La validación debe ser para cantidades menores o iguales a cero.
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;

        // 2. La lógica de negocio (descuentos, etc.) se calcula aquí.
        // Por ejemplo, un descuento si el subtotal es mayor a 100,000.
        double subtotalTemporal = this.producto.getPrecio() * this.cantidad;
        if (subtotalTemporal > 100000) {
            this.subtotal = subtotalTemporal * 0.90; // Aplica un 10% de descuento
            this.seAplicaDescuento = true;
        } else {
            this.subtotal = subtotalTemporal;
            this.seAplicaDescuento = false;
        }
    }

    // Getters para acceder a los valores privados
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public boolean isSeAplicaDescuento() { return seAplicaDescuento; }
    public double getSubtotal() { return subtotal; }

    @Override
    public String toString() {
        return "ItemPedido [producto=" + producto + ", cantidad=" + cantidad + ", seAplicaDescuento="
                + seAplicaDescuento + ", subtotal=" + subtotal + "]";
    }
    
}
