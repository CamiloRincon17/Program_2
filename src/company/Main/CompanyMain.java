package company.Main;
import company.hr.Employee;

public class CompanyMain {
    public static void main(String[] args) {
       Employee employe1 = new Employee("adrian", 100, "boss");
        Employee employe2 = new Employee("Camilo", 140, "employee");


        employe1.showInfo();
        employe2.showInfo();

        employe1.increaseSalary(600);
        employe2.increaseSalary(-400);

        System.out.println("afther the change");
        employe1.showInfo();
        employe2.showInfo();
    }
}
