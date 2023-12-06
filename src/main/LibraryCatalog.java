package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;

import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import data_structures.SinglyLinkedList;
import interfaces.FilterFunction;
import interfaces.List;
import java.text.DecimalFormat;

/* 
 * Main class where methods to run the library catalog are implemented
 */
public class LibraryCatalog {
	private  BufferedReader catalogReader= new BufferedReader(new FileReader("data/catalog.csv"));
	private BufferedReader userReader= new BufferedReader(new FileReader("data/user.csv"));
	private List<Book> catalog;
	private List<User> users;
	

	
		
	public LibraryCatalog() throws IOException {
		this.catalog=getBooksFromFiles();
		this.users=getUsersFromFiles();
		
		
	}
	
	/**
	 * This method runs through the catalog.csv file, two .readLine() are used to skip the first line of the table
	 *that does not contain the information needed. Then we iterate through the lines and split them by the ","s in the line. The split's positions are:
	 *[0]=ID
	 *[1]=Title
	 *[2]=Author
	 *[3]=Genre
	 *[4]=Last checkedout Date
	 *[5]=checked out or not 
	 *then we add to the list of books a book constructed with the information obtained from the splitted list and return it after 
	 *running through entire file
	 * @return List<Book> booksInFile - all books found in the file
	 * @see Book.java
	 * @throws IOException
	 */
	private List<Book> getBooksFromFiles() throws IOException {
		List<Book>booksInFile= new DoublyLinkedList<Book>();
		String line=this.catalogReader.readLine();
		line=this.catalogReader.readLine();
		while(line!=null) {
			
			String[]line_list=line.split(",");
			LocalDate last_checkout_date=LocalDate.parse(line_list[4]);
			booksInFile.add(new Book(Integer.parseInt(line_list[0]),line_list[1],line_list[2],line_list[3],last_checkout_date,Boolean.parseBoolean(line_list[5])));
			line=this.catalogReader.readLine();
		}
		return booksInFile;
	}
	
	/**
	 * This method runs through the users.csv file, we first create a  doubly linked list for the users and then another one inside the while loop
	 * where it stores the user's checked out books. We use readLine() twice to skip the line without information. Inside the loop we split
	 * the line by commas if the list created by the split is of length 3 it means the user has books checked out. We then use substring to
	 * remove the "{}" from the checkedoutbooks array  and then split the numbers by the " " between them. We obtain the book from matching the id
	 * with the index in the catalog-1. Finally a constructor is built for the user and added to the userlist.
	 * @return List<User>usersInFile - all users found in the file
	 * @see Book.java
	 * @see User.java
	 * @throws IOException
	 */
	private List<User> getUsersFromFiles() throws IOException {
		List<User>usersInFile=new DoublyLinkedList<User>();
		
		String line=this.userReader.readLine();
		line=this.userReader.readLine();
		while(line!=null) {
			List<Book>users_books=new DoublyLinkedList<Book>();
			String[]line_list=line.split(",");
			if(line_list.length==3) {
			
			String fixed_string=line_list[2].substring(1,line_list[2].length()-1);
			String[] checked_out_books=fixed_string.split(" ");
			for(int i=0; i<checked_out_books.length; i++) {
				users_books.add(this.catalog.get(Integer.parseInt(checked_out_books[i])-1));

			}
			}
			
			 usersInFile.add(new User(Integer.parseInt(line_list[0]), line_list[1], users_books));
			 line=this.userReader.readLine();
		}
		return usersInFile;
	}
	
	
	public List<Book> getBookCatalog() {
		return catalog;
		
	}
	public List<User> getUsers() {
		return this.users;
	}
	/**
	 * adds book using constructor created in book file with given parameters
	 * @param title
	 * @param author
	 * @param genre
	 * @see book.java
	 * 
	 */
	public void addBook(String title, String author, String genre) {
		this.catalog.add(new Book(this.catalog.size()+1,title,author,genre,LocalDate.of(2023,9, 15),false));
	}
	/**
	 * 
	 * @param id
	 */
	public void removeBook(int id) {
		this.catalog.remove(id-1);
	}	
	/**
	 * setting checked out state to true and the last checkout date to September 15, 2023
	 * @param id
	 * @return boolean indicating book is checked out
	 */
	public boolean checkOutBook(int id) {
		if(this.catalog.contains(this.catalog.get(id-1)) && !this.catalog.get(id-1).isCheckedOut()) {
			this.catalog.get(id-1).setCheckedOut(true);
			this.catalog.get(id-1).setLastCheckOut(LocalDate.of(2023, 9, 15));
			return true;
		}
		return false;
	}
	/**
	 * setting checkedout to false to indicate book has been returned
	 * @param id
	 * @return boolean indicating book has been returned
	 */
	public boolean returnBook(int id) {
		if(this.catalog.contains(this.catalog.get(id-1)) && this.catalog.get(id-1).isCheckedOut()) {
			this.catalog.get(id-1).setCheckedOut(false);
			return true;
		}
		return false;
	}
	/**
	 * if the book is not checked out then the book is available and returns true, else it returns false
	 * @param id
	 * @return boolean that indicates if book is available 
	 */
	
	public boolean getBookAvailability(int id) {
		if(!this.catalog.get(id-1).isCheckedOut()){
			return true;
		}
		return false;
	}
	
	/**
	 * for each loop running through the catalog and matching the book's title to the parameter given. If it matches the 
	 * counter is increased by one
	 * @param title - title to be matched
	 * @return counter - amount of books with title
	 */
	public int bookCount(String title) {
		int counter=0;
		for(Book bookI:this.catalog) {
			if( bookI.getTitle().equals(title)) {
				counter++;
			}
			
		}
		return counter;
	}
	
	/**
	 * Searches for books in the book catalog that match the specified filtering criteria
	 * and returns an Arraylist of matching books.
	 *
	 * @param func The filtering function used to determine if a book matches the criteria.
	 * @return bookList - list of books that meet criteria 
	 */
	
	public List<Book> searchForBook(FilterFunction<Book> func) {
		List<Book> bookList=new ArrayList<>();
		for(Book book: this.getBookCatalog()) {
			if (func.filter(book)) {
				bookList.add(book);
			}
		}
		return bookList;
	}
	
	/**
	 * Searches for users in the book catalog that match the specified filtering criteria
	 * and returns a Arraylist of matching books.
	 *
	 * @param func The filtering function used to determine if a book matches the criteria.
	 * @return userList - list of users that meet criteria 
	 */
	
	
	public List<User> searchForUsers(FilterFunction<User> func) {
		List<User>userList=new ArrayList<>();
		for(User user: this.getUsers()) {
			if(func.filter(user)) {
				userList.add(user);
				
			}
			
		}
		
		return userList;
	}
	/**
	 * Counts the number of books in the book catalog that belong to a specific genre.
	 *
	 * @param genre The genre to count books for (case-insensitive).
	 * @return The count of books that match the specified genre.
	 */
	public int categoryCounter(String genre) {
		FilterFunction<Book> categoryFilter= book ->book.getGenre().equalsIgnoreCase(genre);
		return searchForBook(categoryFilter).size();
	}
	/**
	 * Counts the number of books in the book catalog that have a specific title
	 *
	 * @param title that is being matched
	 * @return The count of books that match thee title
	 */
	public int bookCounter(String title) {
		int counter=0;
		for(Book book: this.catalog) {
			if(book.getTitle().equals(title)) {
				counter++;
			}
			
		}
		return counter;
	}
	
	public void generateReport() throws IOException {
		
		String output = "\t\t\t\tREPORT\n\n";
		output += "\t\tSUMMARY OF BOOKS\n";
		output += "GENRE\t\t\t\t\t\tAMOUNT\n";
		/*
		 * In this section you will print the amount of books per category.
		 * 
		 * Place in each parenthesis the specified count. 
		 * 
		 * Note this is NOT a fixed number, you have to calculate it because depending on the 
		 * input data we use the numbers will differ.
		 * 
		 * How you do the count is up to you. You can make a method, use the searchForBooks()
		 * function or just do the count right here.
		 */
		
		 String categories[]= {"Adventure", "Fiction", "Classics", "Mystery", "Science Fiction"};
		output += "Adventure\t\t\t\t\t" +(categoryCounter(categories[0]))+ "\n";
		output += "Fiction\t\t\t\t\t\t"  +(categoryCounter(categories[1]))+  "\n";
		output += "Classics\t\t\t\t\t" + (categoryCounter(categories[2])) + "\n";
		output += "Mystery\t\t\t\t\t\t"+ (categoryCounter(categories[3]))+ "\n";
		output += "Science Fiction\t\t\t\t\t"  +(categoryCounter(categories[4]))+ "\n";
		output += "====================================================\n";
		int totalSum=categoryCounter(categories[0])+categoryCounter(categories[1])+categoryCounter(categories[2])+categoryCounter(categories[3])+categoryCounter(categories[4]);
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" +(totalSum)+"\n\n";
		
		/*
		 * This part prints the books that are currently checked out
		 */
		output += "\t\t\tBOOKS CURRENTLY CHECKED OUT\n\n";
		/*
		 * Here you will print each individual book that is checked out.
		 * 
		 * Remember that the book has a toString() method. 
		 * Notice if it was implemented correctly it should print the books in the 
		 * expected format.
		 * 
		 * PLACE CODE HERE
		 */
		
		
		
		int checkedOutCounter=0;
		for(Book book: this.catalog) {
			if(book.isCheckedOut()) {
				checkedOutCounter++;
				output+=(book.toString()+"\n");
			}
		}
		
		output += "====================================================\n";
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" +(checkedOutCounter)+"\n\n";
		
		
		/*
		 * Here we will print the users the owe money.
		 */
		output += "\n\n\t\tUSERS THAT OWE BOOK FEES\n\n";
		/*
		 * Here you will print all the users that owe money.
		 * The amount will be calculating taking into account 
		 * all the books that have late fees.
		 * 
		 * For example if user Jane Doe has 3 books and 2 of them have late fees.
		 * Say book 1 has $10 in fees and book 2 has $78 in fees.
		 * 
		 * You would print: Jane Doe\t\t\t\t\t$88.00
		 * 
		 * Notice that we place 5 tabs between the name and fee and 
		 * the fee should have 2 decimal places.
		 * 
		 * PLACE CODE HERE!
		 */
		float totalAmount=0;
		DecimalFormat formatter = new DecimalFormat("0.00"); 
		for(User user: this.users) {
			float fee=0;
			for(Book book: user.getCheckedOutList()) {
				
				fee+=book.calculateFees();
				
			}
			if(fee>0) {
				
			output+=user.getName()+"\t\t\t\t\t$"+formatter.format(fee)+"\n";
			totalAmount+=fee;
		}
		}

			
		output += "====================================================\n";
		output += "\t\t\t\tTOTAL DUE\t$"+ formatter.format(totalAmount)  + "\n\n\n";
		output += "\n\n";
		System.out.println(output);// You can use this for testing to see if the report is as expected.
		
		/*
		 * Here we will write to the file.
		 * 
		 * The variable output has all the content we need to write to the report file.
		 * 
		 * PLACE CODE HERE!!
		 */
		BufferedWriter reportFile = new BufferedWriter(new FileWriter("report/report.txt"));
		reportFile.write(output);
		reportFile.close();
		
		

		
	}
	
	/*
	 * BONUS Methods
	 * 
	 * You are not required to implement these, but they can be useful for
	 * other parts of the project.
	 */
	
	
}

