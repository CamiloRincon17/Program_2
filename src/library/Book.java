package library;

public class Book {
    public String isbn;
    public String title;
    public String author;
    public int availableCopies;
    
    public Book(String isbn,String title,String author,int availableCopies){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.availableCopies = availableCopies;

    }
    public void borrowBook(int amount){
        this.availableCopies-= amount;
        System.out.println("BOOK :"+this.title+"\nStock"+this.availableCopies);
    }
    public void returnBook(int amount){
        this.availableCopies+= amount;
        System.out.println("BOOK :"+this.title+"\nStock"+this.availableCopies);

    }
    public void showBook(){
        System.out.println("BOOK :"+this.title+"\nStock :"+this.availableCopies+"\nAUTHOR :"+this.author);

    }

}
