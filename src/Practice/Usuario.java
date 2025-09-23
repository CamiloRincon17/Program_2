package Practice;

import java.util.List; 

public class Usuario {
    private String nombre;
    private int numSocio;
    private List<Libro> librosPrestados;

    public Usuario(String nombre, int numSocio, List<Libro> librosPrestados) {
        this.nombre = nombre;
        this.numSocio = numSocio;
        this.librosPrestados = librosPrestados;
    }

    void tomarPrestado(Libro libro) {
        librosPrestados.add(libro);
        libro.prestar();

    }

    void devolverLibro(Libro libro) {
        librosPrestados.remove(libro);
        libro.devolver();
    }
}
