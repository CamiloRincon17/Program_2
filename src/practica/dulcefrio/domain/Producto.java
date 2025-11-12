package practica.dulcefrio.domain;

public enum Producto {
    VAINILLA("Vainilla", 3000),
    CHOCOLATE("Chocolate", 3500),
    FRESA( "Fresa", 3200);

    private final String nombre;
    private final double precio;

    private Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }
    public double calcularPrecionUnitario(){
        return precio;
    }
   
    
}
