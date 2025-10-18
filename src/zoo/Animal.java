package zoo;

public class Animal {
    private String nombre;
    private String especie;
    private int edad;
    public Animal(String nombre, String especie, int edad) {
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
    }
    void mostrarInfo(){
        System.out.println("Nombre del animal :"+nombre);
        System.out.println("Especie :"+especie);
        System.out.println("La edad es :"+edad);
    }
}
