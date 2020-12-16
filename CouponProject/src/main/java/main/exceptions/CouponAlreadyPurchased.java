package main.exceptions;

public class CouponAlreadyPurchased extends Exception {
	public CouponAlreadyPurchased() {
		super("Oops.. You Already Own That Coupon!");
	}
}
