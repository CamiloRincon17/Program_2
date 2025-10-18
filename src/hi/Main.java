  package hi;
  class Motor {
    private int potencia;
    private int cilindros;

    public Motor(int potencia, int cilindros) {
        this.potencia = potencia;
        this.cilindros = cilindros;
    }

    public void encender() {
        System.out.println("Motor encendido (" + potencia + " HP)");
    }

    // Getters (opcional)
    public int getPotencia() { return potencia; }
}

// Clase Conductor (usada en AGREGACIÓN y ASOCIACIÓN)
class Conductor {
    private String nombre;
    private String licencia;

    public Conductor(String nombre, String licencia) {
        this.nombre = nombre;
        this.licencia = licencia;
    }

    // Asociación: el conductor puede conducir un coche
    public void conducir(Coche coche) {
        System.out.println(nombre + " está conduciendo un " + coche.getMarca());
        coche.arrancar();
    }

    public String getNombre() {
        return nombre;
    }
}

// Clase Coche (usa COMPOSICIÓN con Motor y AGREGACIÓN con Conductor)
class Coche {
    private String marca;
    private String modelo;
    private Motor motor;           // COMPOSICIÓN: el motor pertenece al coche
    private Conductor conductor;   // AGREGACIÓN: el conductor es "prestado"

    // Constructor: crea su propio motor (composición)
    public Coche(String marca, String modelo, int potenciaMotor, int cilindros, Conductor conductor) {
        this.marca = marca;
        this.modelo = modelo;
        this.motor = new Motor(potenciaMotor, cilindros); // ← COMPOSICIÓN
        // conductor se asigna después (agregación)
    }

    // Agregación: asignar un conductor existente
    public void asignarConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public void arrancar() {
        motor.encender();
        System.out.println(marca + " " + modelo + " en marcha.");
    }

    public void detener() {
        System.out.println(marca + " detenido.");
    }

    public String getMarca() {
        return marca;
    }

    public Conductor getConductor() {
        return conductor;
    }
}

// Clase principal para probar
public class Main {
    public static void main(String[] args) {
        // Crear un conductor (existe independientemente → AGREGACIÓN)
        Conductor juan = new Conductor("Juan", "ABC123");
        Conductor maria = new Conductor("Maria", "XYZ789");
        Conductor luis = new Conductor("Luis", "LMN456");

        // Crear un coche → automáticamente crea su motor (COMPOSICIÓN)
        Coche miCoche = new Coche("Toyota", "Corolla", 140, 4,juan);

        // Asignar conductor al coche (AGREGACIÓN)
       miCoche.asignarConductor(maria);
        miCoche.asignarConductor(maria);
        miCoche.asignarConductor(luis);
        // Asociación: el conductor usa el coche
        juan.conducir(miCoche);
        maria.conducir(miCoche);
        luis.conducir(miCoche);
        System.out.println("El conductor actual es: " + miCoche.getConductor().getNombre());
        System.out.println(miCoche.getConductor().getNombre() + " ha terminado de conducir.");
    }
}