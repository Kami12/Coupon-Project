package main.exceptions;

public class CouponOutOfStock extends Exception {
	public CouponOutOfStock() {
		super("Sorry! This Coupon Is Out Of Stock!");
	}
}
