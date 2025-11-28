package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pedido {
    private final Cliente cliente;
    private final List<ItemPedido> items = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.EN_CREACION;
    private boolean servicioExpres;

    public Pedido(Cliente cliente) {
        this.cliente = Objects.requireNonNull(cliente, "cliente requerido");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public boolean isServicioExpres() {
        return servicioExpres;
    }

    public void setServicioExpres(boolean servicioExpres) {
        if (estado == EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("No se puede modificar el servicio exprés en un pedido CONFIRMADO");
        }
        this.servicioExpres = servicioExpres;
    }

    public void agregarItem(Producto p, int cant) {
        if (estado == EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("No se puede editar un pedido CONFIRMADO");
        }
        items.add(new ItemPedido(p, cant));
    }

    public void confirmar() {
        if (items.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un pedido vacio");
        }
        this.estado = EstadoPedido.CONFIRMADO;
    }

    public int totalBruto() {
        return items.stream().mapToInt(ItemPedido::subtotal).sum();
    }

    public int recargoServicioExpres() {
        return servicioExpres ? (int) Math.round(totalBruto() * 0.10) : 0;
    }

    public int descuento() {
        int totalConRecargo = totalBruto() + recargoServicioExpres();
        if (totalConRecargo > 60000) {
            return (int) Math.round(totalConRecargo * 0.05);
        }
        return 0;
    }

    public int totalFinal() {
        int total = totalBruto();
        int recargo = recargoServicioExpres();
        int descuento = descuento();
        int totalFinal = total + recargo - descuento;
        
        if (totalFinal < 0) {
            throw new IllegalStateException("Total no puede ser negativo");
        }
        return totalFinal;
    }
}