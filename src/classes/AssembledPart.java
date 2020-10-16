package classes;

public class AssembledPart extends Part {
	// instance variables for the parts being assembled
	private Part part1;
	private Part part2;

	public AssembledPart(String pID, String pName, int pStockLvl, double pPrice, Part p1, Part p2) {
		// use the super class to produce the Assembled part object
		super(pID, pName, pStockLvl, pPrice);

		// only addition to subclass is references to already existing parts
		part1 = p1;
		part2 = p2;
	}

	public Part getPart1() {
		return part1;
	}

	public Part getPart2() {
		return part2;
	}

	@Override
	public double getPrice() {
		// cost for assembled part is price of references parts, plus assembly cost
		double cost = part1.getPrice() + part2.getPrice() + super.getPrice();
		return cost;
	}

	public int getAvailForAssembly() {
		// uses the inherited method getStockLevel to get the lower stockLevel of the
		// referenced parts for use in assembly
		int available;
		if (part1.getStockLevel() > part2.getStockLevel())
			available = part2.getStockLevel();
		else
			available = part1.getStockLevel();
		return available;
	}

	@Override
	public double supply(int qty) throws PartShortException {
		// overrides the inherited supply method for the super class and is able to use
		// referenced parts supplyLevels method to supply assembled part when no
		// assembled parts on hand
		if ((getStockLevel() + getAvailForAssembly()) < qty)
			throw new PartShortException("Not enough parts available", getStockLevel() + getAvailForAssembly());
		else if (getStockLevel() < qty) {
			// making use of the superclass method to make changes to stockLevel instance
			// variables of referenced parts and assembledPart
			part1.supply(qty - getStockLevel());
			part2.supply(qty - getStockLevel());
			super.supply(getStockLevel());
		} else
			super.supply(qty);
		return getPrice() * qty;
	}

	@Override
	public String convertToCommaSeparatedString() {
		// Creates a comma separated string for use in saving and restoring data
		return super.getID() + "," + super.getName() + "," + super.getStockLevel() + "," + super.getPrice() + ","
				+ part1.getID() + "," + part2.getID();
	}

	@Override
	public String convertToFormattedString() {
		// Creates user friendly string for reading data
		return super.getID() + "\t" + super.getName() + "\tAssembled\tStock: "
				+ (super.getStockLevel() + getAvailForAssembly()) + "\tPrice: $" + getPrice() + "\tSub-parts: "
				+ part1.getID() + " " + part1.getName() + " and " + part2.getID() + " " + part2.getName();
	}

}
