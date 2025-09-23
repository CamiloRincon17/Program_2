package company.hr;

public class Employee {

    public String name ;
    private double salary;
    protected String role;
    public Employee(String name, double salary,String role){
        this.name=name;
        this.salary=salary;
        this.role=role;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        if(name!=null && name.isEmpty()){
            this.name=name;
        }
    }
    public  double getsalary(){
        return salary;
    }
    public  void setsalary(double salary){
        if(salary>0){
            this.salary=salary;
        }else{
            System.out.println("The salary must be over 0");
        }
    }
    public String getRole(){
        return role;
    }
    public void setRole(String role){
      
          this.role=role;  
        
    }
    public void increaseSalary(double amount){
        if(amount>0){
            this.salary+=amount;
        }else{
            System.out.println("the increase must be positive");
        }

    
    }
    public void showInfo(){
        System.out.println("Empleado:"+name+"|\n rol: "+role+"\n| Salary: "+salary);
    }
}
