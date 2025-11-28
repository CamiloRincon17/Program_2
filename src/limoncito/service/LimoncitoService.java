package limoncito.service;

import limoncito.domain.Cliente;
import limoncito.domain.Pedido;

public class LimoncitoService {
    
    public Pedido crearPedido(Cliente cliente) {
        return new Pedido(cliente);
    }
}
