package zoo;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Cuidador cuidador1 = new Cuidador("adrian", 1,  null); 
                Animal animal1 = new Animal("leon", "carnivoro", 5);

                Habitat habitat1 = new Habitat("selva", animal1);
        cuidador1.asignarHabitat(habitat1);
        cuidador1.mostrarResponsabilidades();
        
        List<Habitat> habitats = List.of(habitat1);
        List<Cuidador> cuidadores = List.of(cuidador1);

        List<Animal> animales = List.of(animal1);
        Animal animal2 = new Animal("tigre", "carnivoro", 4);
    }
}
