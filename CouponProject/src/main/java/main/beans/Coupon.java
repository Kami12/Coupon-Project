package main.beans;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "coupons")
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int couponId;
	@ManyToOne
	private Company company;
	@Column(nullable = false)
	private int amount;
	@Column(name = "category_id" , nullable = false)
	private CategoryType category;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String description;
	@Column
	private String img;
	@Column(name = "startDate", nullable = false)
	private Date startDate;
	@Column(name = "endDate", nullable = false)
	private Date endDate;
	@Column(nullable = false)
	private double price;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Customer> customers;
	
	public Coupon() {
		//THIS IS FOR HIBERNATE!!!
	}

	
	
	public List<Customer> getCustomers() {
		return customers;
	}



	@Override
	public int hashCode() {
		return couponId;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Coupon))
				return false;
		
		return this.couponId == ((Coupon)obj ).couponId;
	}

	public Coupon(Company company, int amount, CategoryType category, String title, String description, String img,
			Date startDate, Date endDate, double price) {
		this.company = company;
		this.amount = amount;
		this.category = category;
		this.title = title;
		this.description = description;
		this.img = img;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CategoryType getCategory() {
		return category;
	}

	public void setCategory(CategoryType category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getId() {
		return couponId;
	}

	@Override
	public String toString() {
		return "Coupon [couponId=" + couponId + ", company id=" + company.getId() + ", amount=" + amount + ", category=" + category
				+ ", title=" + title + ", description=" + description + ", img=" + img + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", price=" + price + "]";
	}
}
