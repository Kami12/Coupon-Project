package main.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import main.beans.CategoryType;
import main.beans.Coupon;
import main.beans.Customer;
import main.exceptions.CouponAlreadyPurchased;
import main.exceptions.CouponExpired;
import main.exceptions.CouponNotFound;
import main.exceptions.CouponOutOfStock;


@Service
public class CustomerFacade extends ClientFacade {

private Customer customer;
	
	@Override
	public boolean login(String email, String password) {
		if(custRepo.findCustomerByEmailAndPassword(email, password) != null)
		{
			customer = custRepo.findCustomerByEmail(email);
			return true;
		}
		return false;
	}
	
	public void purchaseCoupon(Coupon coupon) throws CouponExpired, CouponOutOfStock, CouponAlreadyPurchased, CouponNotFound {
		//Validation
		if(!coupRepo.existsById(coupon.getId())) {
			throw new CouponNotFound();
		}
		// Expiration Check
		if(coupon.getEndDate().getTime()<Calendar.getInstance().getTimeInMillis())
		{
			throw new CouponExpired();
		}
		// Stock Check
		if(coupon.getAmount() == 0)
		{
			throw new CouponOutOfStock();
		}
		// Already Purchased Check
		Set<Coupon> custCoupons = customer.getCoupons();
		boolean found = false; // track the coupon
		for (Coupon c : custCoupons) {
			if(coupon.getId() == c.getId())
			{
				found = true;
				throw new CouponAlreadyPurchased();
			}
		}
		if(!found)
		{
			customer.getCoupons().add(coupon);
			custRepo.save(customer);
			coupon.getCustomers().add(customer);
			coupon.setAmount(coupon.getAmount()-1);
			coupRepo.save(coupon);
		}
	}
	public void purchaseCoupon(int couponId) throws CouponExpired, CouponOutOfStock, CouponAlreadyPurchased, CouponNotFound {
		//Validation
			if(!coupRepo.existsById(couponId)) {
				throw new CouponNotFound();
			}
		// Expiration Check
		Coupon coupon = coupRepo.findById(couponId).get();
		if(coupon.getEndDate().getTime()<Calendar.getInstance().getTimeInMillis())
		{
			throw new CouponExpired();
		}
		// Stock Check
		if(coupon.getAmount() == 0)
		{
			throw new CouponOutOfStock();
		}
		// Already Purchased Check
		Set<Coupon> custCoupons = customer.getCoupons();
		boolean found = false; // track the coupon
		for (Coupon c : custCoupons) {
			if(coupon.getId() == c.getId())
			{
				found = true;
				throw new CouponAlreadyPurchased();
			}
		}
		if(!found)
		{
			customer.getCoupons().add(coupon);
			custRepo.save(customer);
			coupon.setAmount(coupon.getAmount()-1);
			coupon.getCustomers().add(customer);
			coupRepo.save(coupon);
		}
	}
	
	public Set<Coupon> getCustomerCoupons() {
		return customer.getCoupons();
	}
	
	public List<Coupon> getCustomerCoupons(CategoryType category) {
		Set<Coupon> temp = customer.getCoupons();
		List<Coupon> coupons = new ArrayList<Coupon>();
		for (Coupon c : temp) {
			if(c.getCategory().ordinal() == category.ordinal())
			{
				coupons.add(c);
			}
		}
		return coupons;
	}
	
	public List<Coupon> getCustomerCoupons(int maxPrice) {
		Set<Coupon> temp = customer.getCoupons();
		List<Coupon> customerCouponsByMaxPrice = new ArrayList<Coupon>();
		for (Coupon c : temp) {
			if(c.getPrice() <= maxPrice)
			{
				customerCouponsByMaxPrice.add(c);
			}
		}
		return customerCouponsByMaxPrice;
	}
	
	public Customer getCustomerDetails() {
		return customer;
	}


}
