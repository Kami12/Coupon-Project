package main.db;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import main.beans.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	
	public List<Coupon> findCouponsByCompany_id(int companyID);
	
	public List<Coupon> findByEndDateBefore(Date date);
	
}
