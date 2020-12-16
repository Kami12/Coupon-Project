package main.exceptions;

public class CompanyAlreadyExists extends Exception {
	public CompanyAlreadyExists() {
		super("Company Already Exists!");
	}
}
