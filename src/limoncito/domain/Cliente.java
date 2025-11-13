package limoncito.domain;

public record Cliente(String nombre, String telefono) {
    public Cliente {
        if (nombre == null || nombre.trim().isEmpty() || !nombre.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("El nombre no puede ser nulo, vacío o contener caracteres especiales.");
        }
        if (telefono == null || telefono.trim().isEmpty() || !telefono.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("El teléfono no puede ser nulo, vacío o contener caracteres no numéricos.");
        }
    }

    @Override
    public String toString() {
        return "Cliente: " + nombre + ", Teléfono: " + telefono;
    }
}
