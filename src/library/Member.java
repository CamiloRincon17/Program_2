package library;

import java.util.ArrayList;
import java.util.List;

public class Member {
    public int ID;
    public String Name;
    public List<Book> borrowedBooks;
    public Member(int ID, String Name){
        this.ID = ID;
        this.Name = Name;
        this.borrowedBooks = new ArrayList<>();
    }
    void borrow(Book book){
        if(book.borrowBook()){
            borrowedBooks.add(book);

        }
    }
    public void returnBook(Book book){
        this.borrowedBooks.remove(book);

    }
}
