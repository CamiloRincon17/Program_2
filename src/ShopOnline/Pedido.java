package ShopOnline;

import java.time.LocalDate;
import java.util.List;

public class Pedido {
    private List<Producto> productos;
    private double total;
    private LocalDate fecha;

    public Pedido(List<Producto> productos) {
        this.productos = productos;
        this.fecha = LocalDate.now();
        this.total = calcularTotal();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        total = calcularTotal();
    }

    public void quitarProducto(Producto producto) {
        productos.remove(producto);
        total = calcularTotal();
    }

    public double calcularTotal() {
        double suma = 0;
        for (Producto p : productos) {
            suma += p.getPrecio();
        }
        return suma;
    }

    public void mostrarDetalles() {
        System.out.println("Pedido realizado el: " + fecha);
        for (Producto p : productos) {
            System.out.println(p);
        }
        System.out.println("Total: $" + calcularTotal());
    }
}
