package domain;

public enum Producto {
    CAMISA(4000, 3500),
    PANTALON(6000, 5000),
    CHAQUETA(9000, 7500);

    private final int precio;
    private final int precioPorVolumen;

    Producto(int precio, int precioPorVolumen) {
        this.precio = precio;
        this.precioPorVolumen = precioPorVolumen;
    }

    public int getPrecio() {
        return precio;
    }

    public int getPrecioPorVolumen() {
        return precioPorVolumen;
    }
}