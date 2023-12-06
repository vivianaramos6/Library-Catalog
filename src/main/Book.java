package main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


/*
 * class where user's books are created
 */
public class Book {
	
	private int Id;
	private String title;
	private String author;
	private String genre;
	private LocalDate checkoutDate;
	private boolean checkedOut;
	private float fee;
	public LocalDate providedDate=LocalDate.of(2023, 9, 15);
	
	/**
	 * Creates a new Book with the specified attributes.
	 *
	 * @param id - The unique identifier for the book.
	 * @param title - The title of the book.
	 * @param author - The author of the book.
	 * @param genre - The genre of the book.
	 * @param lastCheckOut- The date when the book was last checked out.
	 * @param checkedOut - A boolean indicating whether the book is currently checked out.
	 */
	public Book(int id,String title, String author, String genre, LocalDate lastCheckOut, boolean checkedOut ) {
		this.Id=id;
		this.title=title;
		this.author=author;
		this.genre=genre;
		this.checkoutDate=lastCheckOut;
		this.checkedOut=checkedOut;
	}
	
	
	
	public int getId() {
		return this.Id;
	}
	public void setId(int id) {
		this.Id=id;
	}
	public String getTitle() {
		return this.title ;
	}
	public void setTitle(String title) {
		this.title=title;
	}
	public String getAuthor() {
		return this.author;
	}
	public void setAuthor(String author) {
		this.author=author;
	}
	public String getGenre() {
		return this.genre;
	}
	public void setGenre(String genre) {
		this.genre=genre;
	}
	public LocalDate getLastCheckOut() {
		return this.checkoutDate;
	}
	public void setLastCheckOut(LocalDate lastCheckOut) {
		this.checkoutDate=lastCheckOut;
	}
	public boolean isCheckedOut() {
		
		return checkedOut;
	}
	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut=checkedOut;
	}
	
	
	
	@Override
	public String toString() {
		/*
		 * This is supposed to follow the format
		 * 
		 * {TITLE} By {AUTHOR}
		 * 
		 * Both the title and author are in uppercase.
		 */
		return this.title.toUpperCase() + " BY " + this.author.toUpperCase();
	}
	public float calculateFees() {
		
		/**
		 * fee (if applicable) = base fee + 1.5 per additional day
		 * set providedDate to September 15, 2023
		 */
		
		
		long daysDifference=this.getLastCheckOut().until(providedDate, ChronoUnit.DAYS);
			if(daysDifference>=31) {
				fee=(float) (10+(1.5*(daysDifference-31)));
			}
			return fee;
		}
	}

