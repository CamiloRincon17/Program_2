package Practice;

public class Main {
    public static void main(String[] args) {
        Libro libro1 = new Libro("El principito", "Antoine de Saint-Exupéry", 1943, true);
        Libro libro2 = new Libro("Cien años de soledad", "Gabriel García Márquez", 1967, true);

        Usuario usuario1 = new Usuario("Juan Perez", 12345, new java.util.ArrayList<>());
        Usuario usuario2 = new Usuario("Maria Gomez", 67890, new java.util.ArrayList<>());

        usuario1.tomarPrestado(libro1);
        usuario2.tomarPrestado(libro2);

        usuario1.devolverLibro(libro1);
        usuario2.devolverLibro(libro2);
    }
}
