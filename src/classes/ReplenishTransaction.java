package classes;

public class ReplenishTransaction extends Transaction {

	public ReplenishTransaction(String transID, Part part, int qty) {
		super(transID, part, qty);
		part.replenish(qty);
	}

	public ReplenishTransaction(String transID, String date, Part part, int qty) {
		// alternative constructor for handling historic transactions
		super(transID, date, part, qty);
	}

	@Override
	public String convertToCommaSeparatedString() {
		return super.getID() + "," + super.getFormattedDate() + "," + (super.getPart()).getID() + "," + super.getQty();
	}

	@Override
	public String convertToFormattedString() {
		return super.getID() + "\t" + super.getFormattedDate() + "\tReplenished " + (super.getPart()).getName() + " (ID="
				+ (super.getPart()).getID() + ") with " + super.getQty() + " new items.";
	}
}
