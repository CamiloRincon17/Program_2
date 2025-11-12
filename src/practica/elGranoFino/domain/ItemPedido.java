package practica.elGranoFino.domain;

import java.util.Objects;

public class ItemPedido {
    private Producto producto;
    private int cantidad;
    private boolean descuento;
    private double subtotal;

    public ItemPedido(Producto producto, int cantidad) {

        this.producto = Objects.requireNonNull(producto, "no se puede");
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;

        if (cantidad >= 5) {
            this.subtotal = Math.round(((producto.getPrecio() * cantidad) * 0.07) * 100.0) / 100.0;
            this.descuento=true;
        }else{
            this.subtotal =producto.getPrecio()*cantidad;
            this.descuento=false;

        }
    }

   

  

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

 

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isDescuento() {
        return descuento;
    }

    public void setDescuento(boolean descuento) {
        this.descuento = descuento;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }





    @Override
    public String toString() {
        return "ItemPedido [producto=" + producto + ", cantidad=" + cantidad + ", descuento=" + descuento
                + ", subtotal=" + subtotal + "]";
    }





   
    

}
