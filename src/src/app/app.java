package app;

import domain.Cliente;
import domain.Producto;
import service.Limoncito;

public class app {
    public static void main(String[] args) {
        
        var service = new Limoncito();
        var cliente = new Cliente("Ana", "3001234567");
        var pedido = service.crearPedido(cliente);
        
        service.agregarItem(pedido, Producto.CAMISA, 0);
        service.agregarItem(pedido, Producto.PANTALON, 2);
        
        // Servicio Expres
        pedido.setServicioExpres(false);
        
        System.out.println(service.resumen(pedido));
        
        // CONFIRMAR
        service.confirmar(pedido);
        System.out.println("Pedido confirmado: " + pedido.getEstado());

    }
}