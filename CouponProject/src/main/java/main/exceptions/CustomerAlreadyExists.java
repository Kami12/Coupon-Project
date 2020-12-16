package main.exceptions;

public class CustomerAlreadyExists extends Exception {
	public CustomerAlreadyExists() {
		super("Customer Already Exists!");
	}
}
