package practica.paginaPagina.app;

import practica.paginaPagina.domain.Cliente;
import practica.paginaPagina.domain.Pedido;
import practica.paginaPagina.domain.Producto;

public class Main {
    public static void main(String[] args) {
        System.out.println("📚 ¡Bienvenido a la Librería Online! 📚");
        try{
        // 1. Crear un cliente.
        // Se usa un número de teléfono válido para evitar errores.
        Cliente nuevoCliente = new Cliente("Mauricio", 312222);
        System.out.println("\nCliente registrado: " + nuevoCliente.name());

        // 2. Crear un nuevo pedido para el cliente.
        Pedido nuevoPedido = new Pedido(nuevoCliente);
        System.out.println("Nuevo pedido creado.");

        // 3. Agregar productos (libros) al pedido.
        nuevoPedido.agregarItem(Producto.Novela, 2); // Agregamos 2 novelas
        nuevoPedido.agregarItem(Producto.Infantil, 1);
        nuevoPedido.agregarItem(Producto.Novela, 8);
         // Agregamos 1 libro infantil

        // 4. Confirmar el pedido. Esto calcula el total y lo "cierra".
        nuevoPedido.confirmarPedido();
        System.out.println("\n>> Pedido confirmado. Generando resumen...");

        // 5. Mostrar el resumen del pedido.
        System.out.println(nuevoPedido.generarResumen());
        }catch(Exception e){
            System.out.println("se produjo un error "+ e.getMessage());
        }

        }
    }
 
