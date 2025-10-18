package ShopOnline;

import java.util.List;

public class Cliente {
    private String nombre;
    private String email;
    private List<Pedido> pedidos;

    public Cliente(String nombre, String email, List<Pedido> pedidos) {
        this.nombre = nombre;
        this.email = email;
        this.pedidos = pedidos;
    }

    void hacerPedido(Pedido pedido) {
        pedidos.add(pedido);
        System.out.println("Pedido realizado: " + pedido);
    }

    public void mostrarHistorialPedidos() {
        System.out.println("Historial de pedidos de " + nombre + ":");
        for (Pedido p : pedidos) {
            System.out.println(p);
        }
    }

    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
}
