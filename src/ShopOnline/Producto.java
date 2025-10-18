package ShopOnline;

public class Producto {
    private String nombre;
    private double precio;
    private int stock;
    private int id;
    private static int contador = 1;

    public Producto(String nombre, double precio, int stock) {
        this.id = contador++;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public void reducirStock(int cantidad) {
        if (cantidad > 0 && cantidad <= stock) {
            stock -= cantidad;
            System.out.println("Se han vendido " + cantidad + " unidades de " + nombre + ". Stock restante: " + stock);
        } else {
            System.out.println("No hay suficiente stock para vender " + cantidad + " unidades de " + nombre + ".");
        }
    }

    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            stock += cantidad;
            System.out.println("Se han agregado " + cantidad + " unidades de " + nombre + ". Stock actual: " + stock);
        } else {
            System.out.println("Cantidad inválida para aumentar stock.");
        }
    }

    public boolean hayStock(int cantidad) {
        return cantidad > 0 && cantidad <= stock;
    }

    public String toString() {
        return "Producto: " + nombre + ", Precio: $" + precio + ", Stock: " + stock;
    }

    public void actualizarPrecio(double nuevoPrecio) {
        if (nuevoPrecio > 0) {
            this.precio = nuevoPrecio;
            System.out.println("El precio de " + nombre + " ha sido actualizado a $" + precio);
        }
    }

    // Getters y setters opcionales
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public int getId() { return id; }
}
