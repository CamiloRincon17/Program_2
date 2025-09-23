package Practice;

public class Libro {
    private String titulo;
    private String autor;
    private int año;
    private boolean disponible;

    public Libro(String titulo, String autor, int año, boolean disponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.año = año;
        this.disponible = disponible;
    }

    void prestar() {
        System.out.println("Que libro desea llevar");
        boolean prestar = true;

        if (prestar) {
            System.out.println("El libro se presto");
            titulo = titulo;
            autor = autor;
            año = año;
            disponible = false;
        }
    }

    void devolver() {
        System.out.println("Que libro va al devolver");
        boolean devolver = true;
        if (devolver) {
            System.out.println("El libro se devolvio");
            disponible = true;
            titulo = titulo;
            autor = autor;
            año = año;
        }
    }

}
