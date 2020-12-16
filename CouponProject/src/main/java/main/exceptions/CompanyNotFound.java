package main.exceptions;

public class CompanyNotFound extends Exception {
	public CompanyNotFound() {
		super("Oops.. Company Not Found.");
	}
}
