package practica.elGranoFino.domain;

public record Cliente(String nombre, Integer telefono) {
    public Cliente {
        if(nombre==null || nombre.isBlank()){
            throw new IllegalArgumentException("debe de ingresar el nombre");
        }
        if(telefono==0 || telefono==null){
            throw new IllegalStateException("debe ingresar el telefono");
        }
    }
}
