
import classes.*;
import java.io.*;
import java.util.*;

public class ManageWarehouse {
	final int MAX_PARTS = 1000;
	final int MAX_CUSTOMERS = 1000;
	final int MAX_TRANSACTIONS = 10000;

	Part parts[] = new Part[MAX_PARTS];
	Customer customers[] = new Customer[MAX_CUSTOMERS];
	Transaction transactions[] = new Transaction[MAX_TRANSACTIONS];

	int partsCount = 0;
	int custsCount = 0;
	int transCount = 0;

	Scanner userInput = new Scanner(System.in);

	public void addPart() {
		boolean assembledPart = false;

		String partID = validateID(parts, partsCount);
		System.out.println("Enter part name: ");
		String partName = userInput.nextLine();
		System.out.println("Is " + partName + " an assembled part?");
		char yesCheck = (userInput.nextLine()).charAt(0);

		if (yesCheck == 'y' || yesCheck == 'Y')
			assembledPart = true;
		int partStockLvl = validateIntInput("Enter " + partName + "'s stock level: ");

		// handles for part vs assembled parts
		if (assembledPart) {
			double partPrice = validateDblInput("Enter " + partName + "'s assembly cost per unit: ");
			int p1Index = getIndex("Enter part ID for sub-part 1: ", parts, partsCount);
			int p2Index = getIndex("Enter part ID for sub-part 2: ", parts, partsCount);
			parts[partsCount] = new AssembledPart(partID, partName, partStockLvl, partPrice, parts[p1Index],
					parts[p2Index]);
		} else {
			double partPrice = validateDblInput("Enter " + partName + "'s price per unit: ");
			parts[partsCount] = new Part(partID, partName, partStockLvl, partPrice);
		}

		partsCount++;
	}

	public String validateID(Data arr[], int count) {
		// verifies ID is unique for array
		String iD;
		int index;

		do {
			index = -1;
			System.out.println("Enter ID: ");
			iD = (userInput.nextLine()).toLowerCase();
			for (int i = 0; i < count; i++) {
				if (iD.equals(arr[i].getID()))
					index = i;
			}
			if (index != -1)
				System.out.println("ERROR: " + iD + " already in system.");
		} while (index != -1);

		return iD;
	}

	public int validateIntInput(String msg) {
		boolean error;
		// value only initialised to avoid Eclipse warning
		int value = 0;

		do {
			error = false;
			try {
				System.out.print(msg);
				value = Integer.parseInt(userInput.nextLine());
			} catch (Exception e) {
				System.out.println("ERROR: input must be an integer");
				error = true;
			}
		} while (error);

		return value;
	}

	public double validateDblInput(String msg) {
		boolean error;
		double value = 0;

		do {
			error = false;
			try {
				System.out.print(msg);
				value = Double.parseDouble(userInput.nextLine());
			} catch (Exception e) {
				System.out.println("ERROR: input must be a double");
				error = true;
			}
		} while (error);

		return value;
	}

	public void addCustomer() {
		boolean wholesaleCust = false;

		String custID = validateID(customers, custsCount);
		System.out.println("Enter customer name: ");
		String custName = userInput.nextLine();
		System.out.println("Is " + custName + " a wholesale customer?");
		char yesCheck = (userInput.nextLine()).charAt(0);

		if (yesCheck == 'y' || yesCheck == 'Y')
			wholesaleCust = true;
		if (wholesaleCust) {
			double lowRate = validateDblInput("Enter " + custName + "'s discount rate for <1000 units: ");
			double highRate = validateDblInput("Enter " + custName + "'s discount rate for >1000 units: ");
			customers[custsCount] = new WholesaleCustomer(custID, custName, lowRate, highRate);
		} else {
			double discountRate = validateDblInput("Enter " + custName + "'s discount rate: ");
			customers[custsCount] = new RetailCustomer(custID, custName, discountRate);
		}

		custsCount++;
	}

	public void replenish() {
		// TODO: handle for deciding no longer wanting to replenish at id validate loop
		if (partsCount > 0) {
			System.out.println("Please enter the part number you wish to replenish: ");
			int partIndex = getIndex("Enter part ID: ", parts, partsCount);
			int qty = validateIntInput("Enter quantity to replenish: ");
			transactions[transCount] = new ReplenishTransaction("Tr" + (transCount + 1), parts[partIndex], qty);
			transCount++;
		} else
			System.out.println("No part data. Please add a part before attempting to replenish.");
	}

	public int getIndex(String msg, Data arr[], int count) {
		int index = -1;

		do {
			System.out.print(msg);
			String inputID = userInput.nextLine();
			for (int i = 0; i < count; i++) {
				if (inputID.equals(arr[i].getID())) {
					return i;
				}
			}
			if (index == -1)
				System.out.println("ERROR: " + inputID + "  not in system.");
		} while (index == -1);

		// added to avoid eclipse warning - I believe this is redundant
		return -1;
	}

	public void supply() {
		// TODO: handle for deciding no longer wanting to supply at id validate loop

		if (partsCount > 0)
			if (custsCount > 0) {
				int partIndex = getIndex("Enter part ID: ", parts, partsCount);
				int custIndex = getIndex("Enter customer ID: ", customers, custsCount);

				try {
					int qty = validateIntInput("Enter quantity to supply: ");
					transactions[transCount] = new SupplyTransaction("Tr" + (transCount + 1), parts[partIndex],
							customers[custIndex], qty);
					transCount++;
				} catch (PartShortException e) {
					System.out.printf(e.getMessage() + ". Current stock level is: " + e.getStockLevel());
				}

				// Handling for no data cases
			} else
				System.out.println("No customer data. Please add a customer before attempting to supply.");
		else
			System.out.println("No part data. Please add a part before attempting to supply.");
	}

	public String[] readFile(String fileName) throws FileNotFoundException {
		String data[];
		int lines = 0;
		Scanner inputFile = new Scanner(new File(fileName));

		// gets line count from file
		while (inputFile.hasNextLine()) {
			inputFile.nextLine();
			lines++;
		}

		// gets data from file
		inputFile = new Scanner(new File(fileName));
		data = new String[lines];
		for (int i = 0; i < lines; i++) {
			data[i] = inputFile.nextLine();
		}

		inputFile.close();
		return data;
	}

	public void writeFile(String fileName, String data[]) throws IOException {
		FileWriter outputFile = new FileWriter(fileName);
		for (int i = 0; i < data.length; i++) {
			outputFile.write(data[i] + "\n");
		}
		outputFile.close();
	}

	public String[] createCommaSeparatedArray(Data arr[], int count) {
		// converts Part array into string array ready for writing to file
		String data[] = new String[count];
		for (int i = 0; i < count; i++)
			data[i] = arr[i].convertToCommaSeparatedString();
		return data;
	}

	public String[] createReceiptsArray(Data arr[], int count) {
		// formats data from array for use in receipts
		String[] receipts = new String[count];
		for (int i = 0; i < count; i++)
			receipts[i] = arr[i].convertToFormattedString();
		return receipts;
	}

	public void readIntoPartArray(String data[]) {
		// method for reading from comma separated data array into Part array
		for (int i = 0; i < data.length; i++) {
			StringTokenizer attributes = new StringTokenizer(data[i], ",");
			int tokenCount = attributes.countTokens();
			String iD = attributes.nextToken();
			String name = attributes.nextToken();
			int stockLevel = Integer.parseInt(attributes.nextToken());
			double price = Double.parseDouble(attributes.nextToken());

			// Handles for difference between Part and AssembledPart objects
			if (tokenCount == 4)
				parts[partsCount] = new Part(iD, name, stockLevel, price);
			else if (tokenCount == 6) {
				// uses partID String from input data to find part
				Part part1 = parts[getIndex(attributes.nextToken(), parts)];
				Part part2 = parts[getIndex(attributes.nextToken(), parts)];
				parts[partsCount] = new AssembledPart(iD, name, stockLevel, price, part1, part2);
			}

			// TODO else throw exception;
			partsCount++;
		}
	}

	public void readIntoCustomerArray(String data[]) {
		// method for reading from comma separated data array into Customer array
		for (int i = 0; i < data.length; i++) {
			StringTokenizer attributes = new StringTokenizer(data[i], ",");
			int tokenCount = attributes.countTokens();
			String iD = attributes.nextToken();
			String name = attributes.nextToken();
			double discount = Double.parseDouble(attributes.nextToken());

			// Handles for difference between Retail and Wholesale customers
			if (tokenCount == 3)
				customers[custsCount] = new RetailCustomer(iD, name, discount);
			else if (tokenCount == 4) {
				double discountHigh = Double.parseDouble(attributes.nextToken());
				customers[custsCount] = new WholesaleCustomer(iD, name, discount, discountHigh);
			}

			// TODO else throw exception;
			custsCount++;
		}
	}

	public void readIntoTransactionArray(String data[]) {
		// method for reading from comma separated data array into Transaction array
		for (int i = 0; i < data.length; i++) {
			StringTokenizer attributes = new StringTokenizer(data[i], ",");
			int tokenCount = attributes.countTokens();
			String iD = attributes.nextToken();
			String date = attributes.nextToken();
			Part part = parts[getIndex(attributes.nextToken(), parts)];

			// Handles for difference between Replenish and Supply transactions
			if (tokenCount == 4) {
				int qty = Integer.parseInt(attributes.nextToken());
				transactions[transCount] = new ReplenishTransaction(iD, date, part, qty);
			} else if (tokenCount == 6) {
				Customer customer = customers[getIndex(attributes.nextToken(), customers)];
				int qty = Integer.parseInt(attributes.nextToken());
				double cost = Double.parseDouble(attributes.nextToken());
				transactions[transCount] = new SupplyTransaction(iD, date, part, customer, qty, cost);
			}

			// TODO else throw exception;
			transCount++;
		}
	}

	public int getIndex(String ID, Data arr[]) {
		for (int i = 0; i < partsCount; i++) {
			if (ID.equals(parts[i].getID())) {
				return i;
			}
		}
		return -1;
	}

	public void initialise() {
		// TODO refactor?
		String partsData[], custsData[], transData[];
		System.out.println("Initialising...");

		try {
			partsData = readFile("part_data.txt");
			readIntoPartArray(partsData);
			System.out.println("Parts loaded from file.");
		} catch (FileNotFoundException e) {
			System.out.println("No part data found.");
		}

		try {
			custsData = readFile("customer_data.txt");
			readIntoCustomerArray(custsData);
			System.out.println("Customers loaded from file.");
		} catch (FileNotFoundException e) {
			System.out.println("No customer data found.");
		}

		try {
			transData = readFile("transaction_data.txt");
			readIntoTransactionArray(transData);
			System.out.println("Transactions loaded from file.");
		} catch (FileNotFoundException e) {
			System.out.println("No transaction data found.");
		}
	}

	public void saveData() {
		System.out.println("Saving data...");
		String file_names[] = { "part_data.txt", "customer_data.txt", "transaction_data.txt", "transactions.txt" };
		String arrays[][] = { createCommaSeparatedArray(parts, partsCount),
				createCommaSeparatedArray(customers, custsCount), createCommaSeparatedArray(transactions, transCount),
				createReceiptsArray(transactions, transCount) };

		// loops through two arrays above to save correctly formatted data
		for (int i = 0; i < file_names.length; i++) {
			try {
				writeFile(file_names[i], arrays[i]);
			} catch (IOException e) {
				System.out.println("IO ERROR: failed to write part data to " + file_names[i]);
			}
		}
	}

	public void subMenu() {
		System.out.println("----------SUBMENU---------");
		System.out.println("VIEW PART DATA           1");
		System.out.println("VIEW CUSTOMER DATA       2");
		System.out.println("VIEW TRANSACTION DATA    3");
		System.out.println("SEARCH TRANSACTION DATE  4");
		System.out.println("--------------------------");
		int choice = validateIntInput("Please enter choice: ");

		if (choice == 1)
			viewData(parts, partsCount);
		else if (choice == 2)
			viewData(customers, custsCount);
		else if (choice == 3)
			viewData(transactions, transCount);
		else if (choice == 4)
			searchTransactionDate();
	}

	public void viewData(Data arr[], int count) {
		System.out.println("Enter part ID to view individual data OR enter * to view all part data");
		String inputID = userInput.nextLine();
		int foundCount = 0;

		for (int i = 0; i < count; i++) {
			if (inputID.equals("*") || inputID.equals(arr[i].getID())) {
				System.out.println(arr[i].convertToFormattedString());
				foundCount++;
			}
		}
		if (foundCount == 0)
			System.out.println("No matching items found.");
	}

	public void searchTransactionDate() {
		Date start_date = new Date(0);
		Date end_date = new Date();
		boolean loop;
		int foundCount = 0;

		// Validates user input as formatted correctly for parsing to Date
		do {
			try {
				System.out.println("Enter start date for search in dd/MM/yyyy format:");
				start_date = convertToDate(userInput.nextLine());
				System.out.println("Enter end date for search in dd/MM/yyyy format:");
				end_date = convertToDate(userInput.nextLine());
				loop = false;
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Incorrectly formatted date input.");
				loop = true;
			}
		} while (loop == true);

		// Searches through transactions to find those within date range
		for (int i = 0; i < transCount; i++) {
			if ((transactions[i].getDate()).after(start_date) && (transactions[i].getDate()).before(end_date)) {
				System.out.println(transactions[i].convertToFormattedString());
				foundCount++;
			}
		}
		if (foundCount == 0)
			System.out.println("No matching items found.");
	}

	public Date convertToDate(String formattedDate) {
		StringTokenizer splitDate = new StringTokenizer(formattedDate, "/");
		int day = Integer.parseInt(splitDate.nextToken());
		// Calendar treats January as 0, so I have taken 1 from formatted month to
		// handle for this
		int month = Integer.parseInt(splitDate.nextToken()) - 1;
		int year = Integer.parseInt(splitDate.nextToken());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);

		return cal.getTime();
	}

	public int menu() {
		System.out.println("------PARTS MENU------");
		System.out.println("ADD PART             1");
		System.out.println("ADD CUSTOMER         2");
		System.out.println("SUPPLY PART          3");
		System.out.println("REPLENISH PART       4");
		System.out.println("SUBMENU              5");
		System.out.println("EXIT                 6");
		System.out.println("----------------------");

		return validateIntInput("Please enter choice: ");
	}

	public void control() {
		int choice;
		initialise();

		do {
			choice = menu();
			if (choice > 0 && choice < 6) {
				if (choice == 1)
					addPart();
				else if (choice == 2)
					addCustomer();
				else if (choice == 3)
					supply();
				else if (choice == 4)
					replenish();
				else
					subMenu();
			} else if (choice != 6)
				System.out.println("INPUT ERROR: Please enter a menu number");
			if (choice != 6) {
				System.out.println("Press ENTER to continue...");
				userInput.nextLine();
			}
		} while (choice != 6);

		saveData();
		System.out.println("Exiting... Goodbye!");
	}

	public static void main(String args[]) {
		ManageWarehouse mw = new ManageWarehouse();
		mw.control();
	}
}
