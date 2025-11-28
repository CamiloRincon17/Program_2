package practica.elGranoFino.app;


import practica.elGranoFino.*;
import practica.elGranoFino.domain.Cliente;
import practica.elGranoFino.domain.Pedido;
import practica.elGranoFino.domain.Producto;

public class main {

    public static void main(String[] args) {
        try {
        Cliente nuevoCliente = new Cliente("chicho", 32100000);
        Pedido nuevoPedido = new Pedido(nuevoCliente);
        
        nuevoPedido.agregarPedidido(Producto.Capuchino,12);
        System.out.println("Pedido agregado exitosamente.");
        nuevoPedido.calcularTotal();
        // Imprimimos el estado del pedido para ver el resultado
        
        System.out.println(nuevoPedido.toString());

        } catch (Exception e) {System.out.println("EL error es "+e.getMessage());
        }
        

    }
    
}
