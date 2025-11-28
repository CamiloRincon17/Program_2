package domain;

public record Cliente(String nombre, String telefono) {
    public Cliente {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo, vacío o contener caracteres especiales.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede ser nulo, vacío o contener caracteres no numéricos.");
        }
    }
}
