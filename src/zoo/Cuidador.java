package zoo;

import java.util.List;

public class Cuidador {
    private String nombre;
    private int id;
    private List<Habitat> habitats;
    public Cuidador(String nombre, int id, List<Habitat> habitats) {
        this.nombre = nombre;
        this.id = id;
        this.habitats = habitats;
    }
    void asignarHabitat(Habitat h ){
        System.out.println("se le asigna el habitat"+nombre);
    }
    void mostrarResponsabilidades(){
        System.out.println("a" +nombre+" id :"+id+" habitats "+habitats);
    }
}
