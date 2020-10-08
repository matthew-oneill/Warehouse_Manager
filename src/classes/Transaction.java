package classes;

import java.util.*;
import java.text.SimpleDateFormat;

public abstract class Transaction implements Data {
	private String transID;
	private Part part;
	private int qty;
	private Date date;

	public abstract String convertToCommaSeparatedString();
	public abstract String convertToFormattedString();
	
	public Transaction(String transID, Part part, int qty) {
		// constructor for brand new transaction, instancing current time as date
		this.transID = transID;
		this.part = part;
		this.qty = qty;
		date = new Date();
	}
	
	public Transaction(String transID, String formattedDate, Part part, int qty) {
		// alternate constructor for historic transactions, taking in date data
		this.transID = transID;
		this.part = part;
		this.qty = qty;
		
		// handles importing and instancing date from file
		StringTokenizer splitDate = new StringTokenizer(formattedDate, "/");
		Calendar cal = Calendar.getInstance();
		int day = Integer.parseInt(splitDate.nextToken());
		cal.set(Calendar.DAY_OF_MONTH, day);
	    //  Calendar treats January as 0, so 1 taken from month to handle for this.
		int month = Integer.parseInt(splitDate.nextToken()) - 1; 
		cal.set(Calendar.MONTH, month); 	
		int year = Integer.parseInt(splitDate.nextToken());
		cal.set(Calendar.YEAR, year);
		
		this.date = cal.getTime(); 
	}

	public String getID() {
		return transID;
	}

	public Part getPart() {
		return part;
	}

	public int getQty() {
		return qty;
	}
	
	public Date getDate() {
		return date;
	}

	public String getFormattedDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		return dateFormat.format(date);
	}
}
