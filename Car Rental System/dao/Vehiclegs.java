package dao;

public class Vehiclegs {
	private String customerID;
	private String registrationno;
	private String rentDate;
	private String returnDate;
	private double fees;
	public Vehiclegs() {
		super();
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getregistrationno() {
		return registrationno;
	}
	public void setregistrationno(String registrationno) {
		this.registrationno = registrationno;
	}
	public String getRentDate() {
		return rentDate;
	}
	public void setRentDate(String rentDate) {
		this.rentDate = rentDate;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public double getFees() {
		return fees;
	}
	public void setFees(double fees) {
		this.fees = fees;
	}
	
	
}
