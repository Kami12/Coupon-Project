package main.exceptions;

public class CouponAlreadyExists extends Exception {
	public CouponAlreadyExists() {
		super("Coupon Already Exists!");
	}
}
