package main.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import main.beans.Company;
import main.beans.Coupon;
import main.beans.Customer;
import main.exceptions.CompanyAlreadyExists;
import main.exceptions.CompanyNotFound;
import main.exceptions.CustomerAlreadyExists;
import main.exceptions.CustomerNotFound;



@Service
public class AdminFacade extends ClientFacade {

	@Override
	public boolean login(String email, String password) {
		if(email.equals("admin@admin.com") && password.equals("admin"))
		{
			return true;
		}
		return false;
	}
	
	public void addCompany(Company company) throws CompanyAlreadyExists {
		String name = company.getName();
		String email = company.getEmail();
		ArrayList<Company> allCompanies = (ArrayList<Company>) comRepo.findAll();
		for (Company com : allCompanies) {
			if(name.equals(com.getName()) || email.equals(com.getEmail()))
			{
				throw new CompanyAlreadyExists();
			}
		}
		comRepo.save(company);
	}
	
	public void updateCompany(Company company) throws CompanyAlreadyExists, CompanyNotFound {
		List<Company> allCompanies = comRepo.findAll();
		boolean companyNotExist = true;
		boolean exists = false;
		//Check if same id exists
		//Check if same email is already in use
		for (Company com : allCompanies) {
			if(company.getId()==com.getId())
			{
				companyNotExist = false;
			}
			if(company.getEmail().equals(com.getEmail()) && company.getId() != com.getId())
			{
				exists = true;
				throw new CompanyAlreadyExists();
			}
		}
		if(companyNotExist == true)
		{
			throw new CompanyNotFound();
		}
		if(!exists)
		{
			comRepo.save(company);
		}
		
		
	}
	
	public void deleteCompany(int companyID) throws CompanyNotFound {
		//Validation
		if(!comRepo.existsById(companyID))
		{
			throw new CompanyNotFound();
		}
		List<Coupon> comCoupons = coupRepo.findCouponsByCompany_id(companyID);
		for (Coupon c : comCoupons) {	
			for (Customer cust : custRepo.findAll()) {
				//Do i need an if to check if the coupon is contained in the customer?
				cust.getCoupons().remove(c);
				custRepo.save(cust);
			}	
			coupRepo.delete(c);
		}
		comRepo.deleteById(companyID);
	}

	public List<Company> getAllCompanies() {
		return comRepo.findAll();
	}

	public Company getOneCompany(int companyID) throws CompanyNotFound{
		return comRepo.findById(companyID).orElseThrow(/* new CompanyNotFound() */); // ??? ///
	}

	public void addCustomer(Customer customer) throws CustomerAlreadyExists {
		List<Customer> allCustomers = custRepo.findAll();
		for (Customer cust : allCustomers) {
			if(customer.getId()==cust.getId() || customer.getEmail().equals(cust.getEmail()))
			{
				throw new CustomerAlreadyExists();
			}
		}
		custRepo.save(customer);
	}
	
	public void updateCustomer(Customer customer) throws CustomerAlreadyExists, CustomerNotFound {
		List<Customer> allCustomers = custRepo.findAll();
		boolean found = false; // Found customer match
		//Check if email already been used
		for (Customer cust : allCustomers) {
			if(customer.getId() != cust.getId() && customer.getEmail().equals(cust.getEmail()))
			{
				throw new CustomerAlreadyExists();
			}
			if(customer.getId()==cust.getId())
			{
				found = true;
			}
		}
		if(!found)
		{
			throw new CustomerNotFound();
		}
		custRepo.save(customer);
	}
	
	public void deleteCustomer(int customerID) throws CustomerNotFound {
		//Validation
		if(!custRepo.existsById(customerID))
		{
			throw new CustomerNotFound();
		}
		//Need to delete all coupons first
		Customer c = custRepo.findById(customerID).get();
		c.getCoupons().clear();
		custRepo.save(c);
		//Then we can delete the customer
		custRepo.deleteById(customerID);
	}
	
	public List<Customer> getAllCustomers() {
		return custRepo.findAll();
	}
	
	public Customer getOneCustomer(int customerID) throws CustomerNotFound{
		return custRepo.findById(customerID).orElseThrow(/* new CustomerNotFound() */);
	}

}
