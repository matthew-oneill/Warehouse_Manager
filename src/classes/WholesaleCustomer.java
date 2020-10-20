package classes;

public class WholesaleCustomer extends Customer {
	private double discountRateLow;
	private double discountRateHigh;

	public WholesaleCustomer(String iD, String name, double discountRateLow, double discountRateHigh) {
		super(iD, name);
		this.discountRateLow = discountRateLow;
		this.discountRateHigh = discountRateHigh;
	}

	@Override
	public double getDiscount(double amount) {
		if (amount > 1000)
			return amount * discountRateHigh;
		else
			return amount * discountRateLow;
	}

	public double getDiscountLow() {
		return discountRateLow;
	}

	public double getDiscountHigh() {
		return discountRateHigh;
	}

	@Override
	public String convertToCommaSeparatedString() {
		// Creates a comma separated string for use in saving and restoring data
		return super.getID() + "," + super.getName() + "," + discountRateLow + "," + discountRateHigh;
	}

	@Override
	public String convertToFormattedString() {
		// Creates user friendly string for reading data
		return super.getID() + "\t" + super.getName() + "\tWholesale\tLow Discount: " + discountRateLow
				+ " High Discount: " + discountRateHigh;
	}
}
