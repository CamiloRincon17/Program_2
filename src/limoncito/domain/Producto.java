package limoncito.domain;

public enum Producto {
    CAMISA("Camisa", 4000, 3500, 5),
    PANTALON("Pantalón", 6000, 5000, 5),
    CHAQUETA("Chaqueta", 9000, 7500, 5);

    private final String nombre;
    private final double precio;
    private final double precioPorVolumen;
    private final int umbralVolumen;

    Producto(String nombre, double precio, double precioPorVolumen, int umbralVolumen) {
        this.nombre = nombre;
        this.precio = precio;
        this.precioPorVolumen = precioPorVolumen;
        this.umbralVolumen = umbralVolumen;
    }

    public double getPrecio(int cantidad) {
        if (cantidad >= umbralVolumen) {
            return precioPorVolumen;
        }
        return precio;
    }
    public String getNombre() {
        return nombre;
    }
}
