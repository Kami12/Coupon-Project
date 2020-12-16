package main.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.beans.CategoryType;
import main.exceptions.CouponAlreadyPurchased;
import main.exceptions.CouponExpired;
import main.exceptions.CouponNotFound;
import main.exceptions.CouponOutOfStock;
import main.facade.CustomerFacade;

@RestController
@RequestMapping("customer")
public class CustomerController extends LoginController{
	
	@Autowired
	Map<String, Session> sessionsMap;
	
	public CustomerController() {
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
	
	@PostMapping("/{token}/{couponId}")
	public ResponseEntity<Object> purchaseCoupon(@PathVariable String token, @RequestBody int couponID) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service =   (CustomerFacade) session.getService();
			try {
				service.purchaseCoupon(couponID);
				return ResponseEntity.ok("Coupon id: " + couponID + " purchased successfully.");
			} catch (CouponNotFound | CouponAlreadyPurchased | CouponOutOfStock | CouponExpired e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login");
	}
	
	@GetMapping("/{token}")
	public ResponseEntity<Object> getCustomerCoupons(@PathVariable String token) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service =   (CustomerFacade) session.getService();
			return ResponseEntity.ok(service.getCustomerCoupons());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login"); 
	}
	
	@GetMapping("/{token}/{categoryType}")
	public ResponseEntity<Object> getCustomerCoupons(@PathVariable String token, @RequestBody CategoryType category) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service =   (CustomerFacade) session.getService();
			return ResponseEntity.ok(service.getCustomerCoupons(category));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login"); 
	}
	
	@GetMapping("{token}/{maxPrice}")
	public ResponseEntity<Object> getCustomerCoupons(@PathVariable String token, @RequestBody int maxPrice) {
		Session session = sessionsMap.get(token);
		isTimeOut(session, token);
		if(session != null) {
			session.setLastAccessed(System.currentTimeMillis());
			CustomerFacade service =   (CustomerFacade) session.getService();
			return ResponseEntity.ok(service.getCustomerCoupons(maxPrice));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized login"); 
	}
}
