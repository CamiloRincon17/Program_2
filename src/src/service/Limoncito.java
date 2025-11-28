package service;


import domain.Cliente;
import domain.Pedido;
import domain.Producto;

public class Limoncito {
    public Pedido crearPedido(Cliente cliente) {
        return new Pedido(cliente);
    }

    public void agregarItem(Pedido p, Producto prod, int cant) {
        p.agregarItem(prod, cant);
    }

    public void confirmar(Pedido p) {
        p.confirmar();
    }

    public String resumen(Pedido p) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== RESUMEN DEL PEDIDO ===\n");
        sb.append("Cliente: ").append(p.getCliente().nombre())
           .append(" | Tel: ").append(p.getCliente().telefono())
           .append("\n");
        sb.append("Estado: ").append(p.getEstado()).append("\n");
        sb.append("---------------------------------------\n");
        
        for (var item : p.getItems()) {
            sb.append(String.format(
                "%-15s x%-3d unit=%,6d  subtotal=%,7d\n",
                item.getProducto(), 
                item.getCantidad(), 
                item.precioUnitarioAplicado(), 
                item.subtotal()
            ));
        }
        
        sb.append("---------------------------------------\n");
        int bruto = p.totalBruto();
        int recargo = p.recargoServicioExpres();
        int descuento = p.descuento();
        int total = p.totalFinal();
        
        sb.append(String.format("Total Bruto: %,d\n", bruto));
        if (recargo > 0) {
            sb.append(String.format("Recargo Servicio Expres: %,d\n", recargo));
        }
        if (descuento > 0) {
            sb.append(String.format("Descuento: %,d\n", descuento));
        }
        sb.append(String.format("Total Final: %,d\n", total));
        
        return sb.toString();
    }
}