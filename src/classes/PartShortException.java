package classes;

public class PartShortException extends Exception {
	private String message;
	private int stockLevel;

	public PartShortException(String message, int stockLevel) {
		this.message = message;
		this.stockLevel = stockLevel;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getStockLevel() {
		return stockLevel;
	}

}
