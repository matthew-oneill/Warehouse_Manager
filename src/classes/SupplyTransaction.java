package classes;

public class SupplyTransaction extends Transaction {
	private double cost;
	private double discount;
	private Customer customer;

	public SupplyTransaction(String transID, Part part, Customer customer, int qty) throws PartShortException {
		super(transID, part, qty);
		this.customer = customer;
		cost = part.supply(qty);
		this.discount = customer.getDiscount(getQty());

	}

	public SupplyTransaction(String transID, String date, Part part, Customer customer, int qty, double cost) {
		// alternative constructor for handling historic transactions
		super(transID, date, part, qty);
		this.customer = customer;
		this.cost = cost;
		this.discount = customer.getDiscount(getQty());
	}

	public double getCost() {
		return cost;
	}

	public Customer getCustomer() {
		return customer;
	}

	public double getDiscount() {
		return discount;
	}

	@Override
	public String convertToCommaSeparatedString() {
		// Creates a comma separated string for use in saving and restoring data
		return super.getID() + "," + super.getFormattedDate() + "," + (super.getPart()).getID() + "," + customer.getID()
				+ "," + super.getQty() + "," + cost;
	}

	@Override
	public String convertToFormattedString() {
		// Creates user friendly string for reading data
		return super.getID() + "\t" + super.getFormattedDate() + "\tSupplied " + customer.getName() + " "
				+ super.getQty() + " items of" + (super.getPart()).getName() + " (ID=" + (super.getPart()).getID()
				+ ") for $" + cost + " (discount $" + discount + ").";
	}
}
