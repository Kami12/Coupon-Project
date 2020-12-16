package main.exceptions;

public class CouponExpired extends Exception {
	public CouponExpired() {
		super("Oops.. Seems like this coupon has expired!");
	}
}
