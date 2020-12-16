package main.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import main.exceptions.IncorrectLoginDetails;
import main.facade.AdminFacade;
import main.facade.ClientFacade;
import main.facade.CompanyFacade;
import main.facade.CustomerFacade;



@Component
public class LoginManager {
	
	@Autowired
	private ConfigurableApplicationContext ctx;
	
	public ClientFacade login(String email, String password, ClientType ct) throws IncorrectLoginDetails {
		
		if(ct.ordinal()==0)
		{
			AdminFacade admin = ctx.getBean(AdminFacade.class);
			if(admin.login(email, password))
			{
					return admin;
			}
			else {
				throw new IncorrectLoginDetails();
			}
		}
		else if(ct.ordinal()==1)
		{
			CompanyFacade company = ctx.getBean(CompanyFacade.class);
			if(company.login(email, password))
			{
				return company;
			}
			else {
				throw new IncorrectLoginDetails();
			}
		}
		else 
		{
			CustomerFacade customer = ctx.getBean(CustomerFacade.class);
			if(customer.login(email, password))
			{
				return customer;
			}
			else {
				throw new IncorrectLoginDetails();
			}
		}
	}
}
