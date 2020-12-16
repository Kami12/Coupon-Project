package main.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.beans.Company;
import main.beans.Customer;
import main.exceptions.CompanyAlreadyExists;
import main.exceptions.CompanyNotFound;
import main.exceptions.CustomerAlreadyExists;
import main.exceptions.CustomerNotFound;
import main.facade.AdminFacade;

@RestController
@RequestMapping("admin")
public class AdminController {

	@Autowired
	Map<String, Session> sessionsMap;
	
	public AdminController() {
	}
	
	private void isTimeOut(Session session, String token) {
		if (session != null) {
			long timeSession = session.getLastAccessed();
			long timeNow = System.currentTimeMillis();
			if ((timeNow - timeSession) > 1000 * 60 * 30) {
				sessionsMap.remove(token);
				session = null;
			}
		}
	}
	@PostMapping("/company/{token}")
	public ResponseEntity<Object> addCompany(@PathVariable String token, @RequestBody Company company) {
		//First we check if there is a token and validate it
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			//first set lastAccessed time for now
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.addCompany(company);
				return ResponseEntity.ok(company);
			} catch (CompanyAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	@PutMapping("/company/{token}")
	public ResponseEntity<Object> updateCompany(@PathVariable String token, @RequestBody Company company) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.updateCompany(company);
				return ResponseEntity.ok(company);
			} catch (CompanyNotFound | CompanyAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@DeleteMapping("/company/{token}/{id}")
	public ResponseEntity<Object> deleteCompany(@PathVariable String token, @RequestBody int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.deleteCompany(id);
				return ResponseEntity.ok("Company with id: " + id + " deleted successfully.");
			} catch (CompanyNotFound e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@GetMapping("/company/{token}")
	public ResponseEntity<Object> getAllCompanies(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			return ResponseEntity.ok(service.getAllCompanies());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	@GetMapping("/company/{token}/{id}")
	public ResponseEntity<Object> getOneCompany(@PathVariable String token, @RequestBody int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				return ResponseEntity.ok(service.getOneCompany(id));
			} catch (CompanyNotFound e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@PostMapping("/customer/{token}")
	public ResponseEntity<Object> addCustomer(@PathVariable String token, @RequestBody Customer customer) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if( session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.addCustomer(customer);
				return ResponseEntity.ok(customer);
			} catch (CustomerAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@PutMapping("/customer/{token}")
	public ResponseEntity<Object> updateCustomer(@PathVariable String token, @RequestBody Customer customer) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if( session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.updateCustomer(customer);
				return ResponseEntity.ok(customer);
			} catch (CustomerAlreadyExists | CustomerNotFound e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@DeleteMapping("/customer/{token}/{id}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable String token, @RequestBody int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if( session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				service.deleteCustomer(id);
				return ResponseEntity.ok("Customer id: " + id + " deleted successfully.");
			} catch (CustomerNotFound e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@GetMapping("/customer/{token}")
	public ResponseEntity<Object> getAllCustomers(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			return ResponseEntity.ok(service.getAllCustomers());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@GetMapping("/customer/{token}/{id}")
	public ResponseEntity<Object> getOneCustomer(@PathVariable String token, @RequestBody int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			AdminFacade service = (AdminFacade) session.getService();
			try {
				return ResponseEntity.ok(service.getOneCustomer(id));
			} catch (CustomerNotFound e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
}
