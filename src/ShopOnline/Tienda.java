package ShopOnline;

import java.util.List;

public class Tienda {
    private List<Producto> productos;
    private List<Cliente> clientes;

    public Tienda(List<Producto> productos, List<Cliente> clientes) {
        this.productos = productos;
        this.clientes = clientes;
    }

    void registrarCliente(Cliente cliente) {

    }

    void agregarProducto(Producto prod) {
        System.out.println("Producto agregado: " + prod);
        productos.add(prod);

    }

    void generarPedido(Cliente cliente, Pedido pedido) {
        System.out.println("El pedido ha sido generado para el cliente: " + cliente);

    }

    public Producto buscarProductoPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public void listarProductos() {
        System.out.println("Productos en la tienda:");
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    public void listarClientes() {
        System.out.println("Clientes registrados:");
        for (Cliente c : clientes) {
            System.out.println(c.getNombre() + " - " + c.getEmail());
        }
    }
}
