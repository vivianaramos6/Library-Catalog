package main;

import interfaces.List;



public class User {
	private int userID;
	private String userName;
	private List<Book> userCheckedOutList;
	
	/**
	 * Creates a new User with the specified user ID, user name, and a list of checked-out books.
	 *
	 * @param id-The unique identifier for the user.
	 * @param name-The name of the user.
	 * @param checkedOutList-A list of books that the user has checked out.
	 */
	
	public User(int id, String name, List<Book>checkedOutList) {
		this.userID=id;
		this.userName=name;
		this.userCheckedOutList=checkedOutList;
	}

	
	public int getId() {
		return this.userID;
	}

	public void setId(int id) {
		this.userID=id;
	}

	public String getName() {
		return this.userName;
	}

	public void setName(String name) {
		this.userName=name;
	}

	public List<Book> getCheckedOutList() {
		return this.userCheckedOutList;
	}

	public void setCheckedOutList(List<Book> checkedOutList) {
		this.userCheckedOutList=checkedOutList;
	}
}
