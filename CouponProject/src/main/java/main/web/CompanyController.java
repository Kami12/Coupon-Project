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

import main.beans.CategoryType;
import main.beans.Coupon;
import main.exceptions.CouponAlreadyExists;
import main.exceptions.CouponNotFound;
import main.facade.CompanyFacade;

@RestController
@RequestMapping("company")
public class CompanyController {

	@Autowired
	Map<String, Session> sessionsMap;
	
	public CompanyController() {
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
	@PostMapping("/{token}")
	public ResponseEntity<Object> addCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service =  (CompanyFacade) session.getService();
			try {
				service.addCoupon(coupon);
				return ResponseEntity.ok(coupon);
			} catch (CouponAlreadyExists e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@PutMapping("/{token}")
	public ResponseEntity<Object> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service =  (CompanyFacade) session.getService();
			try {
				service.updateCoupon(coupon);
				return ResponseEntity.ok(coupon);
			} catch (CouponAlreadyExists | CouponNotFound e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}

	@DeleteMapping("/{token}/{id}")
	public ResponseEntity<Object> deleteCoupon(@PathVariable String token, @RequestBody int id) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service =  (CompanyFacade) session.getService();
			try {
				service.deleteCoupon(id);
				return ResponseEntity.ok("Coupon id:" + id + " deleted successfully.");
			} catch (CouponNotFound e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@GetMapping("/{token}")
	public ResponseEntity<Object> getCompanyCoupons(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service =  (CompanyFacade) session.getService();
			return ResponseEntity.ok(service.getCompanyCoupons());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login"); 
	}
	
	@GetMapping("/{token}/{categoryType}")
	public ResponseEntity<Object> getCompanyCoupons(@PathVariable String token, @RequestBody CategoryType category) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service =  (CompanyFacade) session.getService();
			return ResponseEntity.ok(service.getCompanyCoupons(category));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login"); 
	}
	
	@GetMapping("/{token}/{maxPrice}")
	public ResponseEntity<Object> getCompanyCoupons(@PathVariable String token, @RequestBody int maxPrice) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service =  (CompanyFacade) session.getService();
			return ResponseEntity.ok(service.getCompanyCoupons(maxPrice));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login"); 
	}
	
	@GetMapping("/token}")
	public ResponseEntity<Object> getCompanyDetails(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CompanyFacade service =  (CompanyFacade) session.getService();
			return ResponseEntity.ok(service.getComapnyDetails());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login"); 
	}
	
}
