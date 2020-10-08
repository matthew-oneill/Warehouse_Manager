package classes;

public class Part implements Data {
	// private instance variables used to avoid issues where user may make direct
	// alterations to variables, which could lead to unintended consequences
	private String iD;
	private String name;
	private int stockLevel;
	private double price;

	public Part(String pID, String pName, int pStockLvl, double pPrice) {
		iD = pID;
		name = pName;
		stockLevel = pStockLvl;
		price = pPrice;
	}

	public String getID() {
		// get method allowing user to retrieve ID without making changes instance
		// variable
		return iD;
	}

	public String getName() {
		// get method allowing user to retrieve name without making changes to instance
		// variable
		return name;
	}

	public int getStockLevel() {
		// get method allowing user to retrieve stockLevel without making changes to
		// instance variable
		return stockLevel;
	}

	public double getPrice() {
		// get method allowing user to retrieve price without making changes to instance
		// variable
		return price;
	}

	public void replenish(int qty) {
		// allows user to add to stockLevel indirectly, within the constraints of this
		// method
		stockLevel += qty;
	}

	public double supply(int qty) throws PartShortException {
		// checks if qty is available, if so removes qty and returns cost
		// because the user cannot directly access the stockLevel this method enforces
		// that stock must not go below zero
		if (stockLevel < qty)
			throw new PartShortException("Not enough parts available", stockLevel);
		else {
			stockLevel -= qty;
			return price * qty;
		}
	}
	
	public String convertToCommaSeparatedString() {
		// Creates a comma separated string for use in saving and restoring data
		return iD + "," + name + "," + stockLevel + "," + price;
	}
	
	public String convertToFormattedString() {
		// Creates user friendly string for reading data
		return iD + "\t" + name + "\tStock: " + stockLevel + "\tPrice: $" + price;
	}
}
