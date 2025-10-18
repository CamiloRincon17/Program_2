package zoo;

import java.util.List;

public class Habitat {
    private String tipo;
    private List<Animal> animales;
    public Habitat(String tipo, List<Animal> animales) {
        this.tipo = tipo;
        this.animales = animales;
    }
   void agregarAnimal(Animal a){
    List<Animal> animales;
    animales.add(a);

   }
   void listarAnimales(){
    System.out.println("los animales que hay son : "+animales);
   }

}
