package classes;

public abstract class Customer implements Data {
	private String iD;
	private String name;
	
	public abstract double getDiscount(double amount);
	public abstract String convertToCommaSeparatedString();
	public abstract String convertToFormattedString();

	public Customer(String iD, String name) {
		this.iD = iD;
		this.name = name;
	}
	
	public String getID() {
		return iD;
	}
	
	public String getName() {
		return name;
	}

}
