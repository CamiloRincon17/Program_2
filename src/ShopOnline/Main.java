package ShopOnline;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenido a la tienda en línea");

        // Crear productos
        Producto prod1 = new Producto("Laptop", 15000, 10);
        Producto prod2 = new Producto("Mouse", 300, 50);

        // Crear cliente
        Cliente cliente = new Cliente("Ana Torres", "ana@email.com", new java.util.ArrayList<>());

        // Crear pedido
        java.util.List<Producto> lista = new java.util.ArrayList<>();
        lista.add(prod1);
        lista.add(prod2);
        Pedido pedido = new Pedido(lista);

        // Cliente hace pedido
        cliente.hacerPedido(pedido);

        // Mostrar detalles
        pedido.mostrarDetalles();
    }
}


