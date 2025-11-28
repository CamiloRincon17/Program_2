package practica.dulcefrio.service;

import practica.dulcefrio.domain.Cliente;
import practica.dulcefrio.domain.Pedido;

/**
 * Clase de servicio que maneja la lógica de negocio de la heladería.
 */
public class Heladeria {
    
    /**
     * Crea un nuevo pedido para un cliente.
     * @param cliente El cliente que realiza el pedido.
     * @return Un nuevo objeto Pedido, listo para agregarle items.
     */
    public Pedido crearPedido(Cliente cliente) {
        return new Pedido(cliente);
    }
}
