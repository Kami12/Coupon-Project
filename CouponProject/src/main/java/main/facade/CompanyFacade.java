package main.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import main.beans.CategoryType;
import main.beans.Company;
import main.beans.Coupon;
import main.beans.Customer;
import main.exceptions.CouponAlreadyExists;
import main.exceptions.CouponNotFound;


@Service
public class CompanyFacade extends ClientFacade {

	private Company company;
	
	
	@Override
	public boolean login(String email, String password) {
		if(comRepo.findCompanyByEmailAndPassword(email, password) != null)
		{
			//Email is unique
			company = comRepo.findCompanyByEmail(email);
			return true;
		}
		return false;
	}
	
	
	public void addCoupon(Coupon coupon) throws CouponAlreadyExists {
		List<Coupon> comCoupons = company.getCoupons();
		boolean exists = false; // For validating the coupon
		for (Coupon c : comCoupons) {
			if(coupon.getId() != c.getId() && coupon.getTitle().equals(c.getTitle()))
			{
				exists = true;
				throw new CouponAlreadyExists();
			}
		}
		if(!exists)
		{
			//Set company ID
			coupon.setCompany(company);
			
			//Add it to the database
//			coupRepo.findAll().add(coupon);
			coupRepo.save(coupon);
			
			//Add it to company coupons and save in db
			company.getCoupons().add(coupon);
			comRepo.save(company);
			
		}
	}
	
	public void updateCoupon(Coupon coupon) throws CouponNotFound, CouponAlreadyExists {
		//Validation
		if(!coupRepo.existsById(coupon.getId()) || coupon.getCompany().getId() != company.getId())
		{
			throw new CouponNotFound();
		}
		//Check if title exists already
		List<Coupon> allCoupons = coupRepo.findAll();
		for (Coupon c : allCoupons) {
			if(coupon.getId() != c.getId() && coupon.getTitle().equals(c.getTitle()))
			{
					throw new CouponAlreadyExists();
			}
		}
			coupRepo.save(coupon);
	}
	
	public void deleteCoupon(int couponID) throws CouponNotFound {
		//Validation
		if(!coupRepo.existsById(couponID) || coupRepo.getOne(couponID).getCompany().getId() != company.getId()) {
			throw new CouponNotFound();
		}
		//If found , save it and remove for each customer
		Coupon c = coupRepo.findById(couponID).get();
		for (Customer cust : custRepo.findAll()) {
			cust.getCoupons().remove(c);
			custRepo.save(cust);
		}
		//Then remove the coupon from the company
		company.getCoupons().remove(c);
		comRepo.save(company);
		//Delete the coupon
		coupRepo.deleteById(couponID);
		
	}
	
	public List<Coupon> getCompanyCoupons() {
		return company.getCoupons();
	}

	public List<Coupon> getCompanyCoupons(CategoryType category) {
		List<Coupon> temp = company.getCoupons();
		List<Coupon> comCouponsByCategory = new ArrayList<Coupon>();
		
		for (Coupon c : temp) {
			if(c.getCategory().ordinal() == category.ordinal())
			{
				comCouponsByCategory.add(c);
			}
		}
		return comCouponsByCategory;
	}
	
	public List<Coupon> getCompanyCoupons(double maxPrice) {
		List<Coupon> temp = company.getCoupons();
		List<Coupon> comCouponsByMaxPrice = new ArrayList<Coupon>();
		
		for (Coupon c : temp) {
			if(c.getPrice() <= maxPrice)
			{
				comCouponsByMaxPrice.add(c);
			}
		}
		return comCouponsByMaxPrice;
	}
	
	public Company getComapnyDetails() {
		return company;
	}
}
