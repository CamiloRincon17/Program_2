package practica.elGranoFino.domain;

public enum  Producto {
    Espresso(2000),
    Capuchino(3000),
    Latte(3500);

    private int precio;

    private Producto(int precio){
        this.precio =precio;
    }
    public int getPrecio(){
        return precio;
    }

}
