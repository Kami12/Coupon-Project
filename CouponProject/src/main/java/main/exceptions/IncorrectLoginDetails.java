package main.exceptions;

public class IncorrectLoginDetails extends Exception {
	public IncorrectLoginDetails() {
		super("Error!\nEmail or Password is incorrect!\nTry again:");
	}
}
