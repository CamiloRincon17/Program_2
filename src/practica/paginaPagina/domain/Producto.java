package practica.paginaPagina.domain;

public enum  Producto {
    Novela("novela",15000),
    Ensayo("ensayo",120000),
    Infantil("infantil",10000);

    private final String name;
    private final int precio;

    private Producto(String name, int precio){
        this.name = name;
        this.precio = precio;
    }

    public String getName() {
        return name;
    }

    public int getPrecio() {
        return precio;
    }
}
