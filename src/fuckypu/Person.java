package fuckypu;

public class Person {
    private int id;
    private int docNum;
    private int phone;
    private String email;
    public String fullName;
    public String nationality;
    public int age;

    public Person(int id, int docNum, int phone, String email, String fullName, String nationality, int age) {
        this.id = id;
        this.docNum = docNum;
        this.phone = phone;
        this.email = email;
        this.fullName = fullName;
        this.nationality = nationality;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocNum() {
        return docNum;
    }

    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", docNum=" + docNum + ", phone=" + phone + ", email=" + email + ", fullName="
                + fullName + ", nationality=" + nationality + ", age=" + age + "]";
    }
    
}
