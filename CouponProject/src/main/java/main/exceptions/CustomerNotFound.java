package main.exceptions;

public class CustomerNotFound extends Exception {
	public CustomerNotFound() {
		super("Oops.. Customer Not Found!");
	}
}
