package practica.dulcefrio.domain;

public record Cliente(String nombre, int telefono){
    public Cliente{
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("el nombre es obligatorio");
        }
        if(telefono == 0 || nombre.isBlank()){
            throw new IllegalArgumentException("el telefono es obligatorio");
        }
    }
}
