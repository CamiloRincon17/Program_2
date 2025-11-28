package limoncito.domain;

public class ItemPedido {
    private Producto producto;
    private int cantidad;

    public ItemPedido(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double calcularSubtotal() {
        return producto.getPrecio(cantidad) * cantidad;
    }

    @Override
    public String toString() {
        return String.format("%d x %s (a $%.2f c/u) - Subtotal: $%.2f",
                cantidad, producto.getNombre(), producto.getPrecio(cantidad), calcularSubtotal());
    }
}
