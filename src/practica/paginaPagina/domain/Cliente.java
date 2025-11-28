package practica.paginaPagina.domain;

public record Cliente(String name, int telefono){
    public Cliente{
        if(name== null || name.isBlank()){
            System.out.println("debe ser ingresar el nombre");
        }
        if (telefono==0 ) {
            throw new ArithmeticException("debe de ingresar el telefono");
        }

    }
}
