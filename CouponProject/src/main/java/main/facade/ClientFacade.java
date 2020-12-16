package main.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.db.CompanyRepositry;
import main.db.CouponRepository;
import main.db.CustomerRepository;



@Service
public abstract class ClientFacade {
	@Autowired
	protected CompanyRepositry comRepo;
	@Autowired
	protected CouponRepository coupRepo; 
	@Autowired
	protected CustomerRepository custRepo;
	
	public abstract boolean login(String email, String password);
	
}
