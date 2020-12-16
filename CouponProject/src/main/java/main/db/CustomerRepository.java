package main.db;

import org.springframework.data.jpa.repository.JpaRepository;

import main.beans.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	public Customer findCustomerByEmailAndPassword(String email, String password);
	
	public Customer findCustomerByEmail(String email);
}
