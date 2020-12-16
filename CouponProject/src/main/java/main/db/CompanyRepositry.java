package main.db;

import org.springframework.data.jpa.repository.JpaRepository;

import main.beans.Company;

public interface CompanyRepositry extends JpaRepository<Company, Integer> {
	
	public Company findCompanyByEmailAndPassword(String email, String password);
	
	public Company findCompanyByEmail(String email);
}
