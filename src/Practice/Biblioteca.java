package Practice;

import java.util.List;

public class Biblioteca {
    private List<Libro> libros;
    private List<Usuario> usuarios;

    public Biblioteca(List<Libro> libros, List<Usuario> usuarios) {
        this.libros = libros;
        this.usuarios = usuarios;
    }

    void agregarLibro(Libro libro) {
        libros.add(libro);
        System.out.println("Libro agregado: " + libro);
    }

    void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        system.out.println("Usuario registrado: " + usuario);
    }

    void prestarLibro(Libro libro, Usuario usuario) {
        usuario.tomarPrestado(libro);
        System.out.println("El libro ha sido prestado al usuario: " + usuario);
    }

    void devolverLibro(Libro libro, Usuario usuario) {
        usuario.devolverLibro(libro);
        System.out.println("El libro ha sido devuelto a la biblioteca."+libro);
    }
}
