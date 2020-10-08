package classes;

public class RetailCustomer extends Customer {
	private double discountRate;

	public RetailCustomer(String iD, String name, double discountRate) {
		super(iD, name);
		this.discountRate = discountRate;
	}

	@Override
	public double getDiscount(double amount) {
		return amount*discountRate;
	}
	
	public double getDiscountRate() {
		return discountRate;
	}
	
	@Override
	public String convertToCommaSeparatedString() {
		// Creates a comma separated string for use in saving and restoring data
		return super.getID() + "," + super.getName() + "," + discountRate;
	}

	@Override
	public String convertToFormattedString() {
		// Creates user friendly string for reading data
		return super.getID() + "\t" + super.getName() + "\tRetail\tDiscount: " + discountRate;
	}
}
