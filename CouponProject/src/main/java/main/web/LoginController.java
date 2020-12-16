package main.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import main.exceptions.IncorrectLoginDetails;
import main.facade.ClientFacade;
import main.login.ClientType;
import main.login.LoginManager;

@RestController
public abstract class LoginController {
	
	private LoginManager loginManager;
	@Autowired
	private Map<String, Session> sessionsMap;
	
	@PostMapping("/login/{email}/{password}/{type}")
	public String login(@PathVariable String email, @PathVariable String password, @PathVariable ClientType type) {
		String token = UUID.randomUUID().toString();
		try {
			ClientFacade service = loginManager.login(email, password, type);
			if(service !=null) {
			Session session = new Session(service, System.currentTimeMillis());
			sessionsMap.put(token, session);

			return token;
			}
			return "Error: login failed";
		} catch (IncorrectLoginDetails e) {
			return "Error: " + e.getMessage();
		} 
	}
	
	@PostMapping("/logout/{token}")
	public void logout(@PathVariable String token) {
		sessionsMap.remove(token);
	}
}
