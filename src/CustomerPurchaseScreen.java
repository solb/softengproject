import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

/**
 * CustomerPurchaseScreen.
 * The screen that presents the purchasing options to the customer.
 * @author Kyle Savarese
 * 
 */
public class CustomerPurchaseScreen {
	
	/** The Database instance */
	private static DatabaseLayer db = DatabaseLayer.getInstance();

	/** The current Customer */
	private Customer user;

 	/** The current VendingMachine */
	private VendingMachine machine;

	/**
	 * Item that the customer purchased
	 **/
	private FoodItem purchasedItem;

	/** 
	 * The Standard CustomerPurchaseScreen constructor.
	 * @param cust the current customer
	 * @param vm the VendingMachine the user is at
	 */
	public CustomerPurchaseScreen( Customer cust, VendingMachine vm ) {
		user = cust;
		machine = vm;
		purchasedItem = null;
	}

	/** 
	 * Lists the layout of all the items still available for sale.
	 * Those items that have sold out are delivered as <tt>null</tt>s.
	 * @return the layout of items
 	 */
	public FoodItem[][] listLayout() {
		VMLayout locs = machine.getCurrentLayout();
		Row[][] rows = locs.getRows();
		FoodItem[][] items = new FoodItem[rows.length][rows[0].length];
		for ( int i = 0; i < rows.length; i++ ) {
			for ( int j = 0; j < rows[i].length; j++ ) {
				if(rows[i][j]!=null && 
					rows[i][j].getRemainingQuantity()>0)
					items[i][j] = rows[i][j].getProduct();
			}
		}
		return items;
	}

	/**
	 * Attempts to purchase the item at the specified location.
	 * @param product the location (as a Pair of Integers) of the product
	 * @return a reason why it either succeded or failed.  If success, returns "GOOD"
 	 */
	public String tryPurchase( Pair<Integer, Integer> product ) {
		VMLayout locs = machine.getCurrentLayout();
		Row[][] rows = locs.getRows();
		if ( product.first < 0 || rows.length <= product.first ||
			product.second < 0 || rows[product.first].length <= product.second )
			return "Invalid location"; //not a valid location
		if ( rows[product.first][product.second] == null)
			return "No product"; //nothing to see here
		if ( rows[product.first][product.second].getRemainingQuantity() <= 0 )
			return "Item sold out"; //check if there is some remaining
		if ( !rows[product.first][product.second].getProduct().isActive() )
			return "Item inactive"; //check if product disabled
		
		FoodItem item = rows[product.first][product.second].getProduct();
		purchasedItem = item;
		int cash = getBalance();
		int price = item.getPrice();
		if ( cash < price )
			return "Insufficient funds"; //insufficient funds
		
		try
		{
			Transaction trans = new Transaction(new GregorianCalendar(), 
				machine, user, item, product);
			db.updateOrCreateTransaction( trans );
			user.setMoney( cash - price );
			db.updateOrCreateCustomer(user);
			rows[product.first][product.second].decrementRemainingQuantity();
			db.updateOrCreateVendingMachine(machine);
		}
		catch(Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, databaseProblem);
		}
		return "Good";
	}

	/**
	 * Attempts to find and purchase the specified item
	 * @param item The item to purchase
	 * @return GOOD or a reason why this operation failed
	 **/
	public String tryPurchase(FoodItem item)
	{
		try
		{
			Row[][] rows = machine.getCurrentLayout().getRows();
			for (int i=0;i<rows.length;++i)
			{
				for (int j=0;j<rows[i].length;++j)
				{
					Row row = rows[i][j];
					if (row != null && row.getRemainingQuantity() > 0 && row.getProduct().equals(item))
						return tryPurchase(new Pair<Integer,Integer>(i,j));
				}
			}
		}
		catch (Exception databaseProblem)
		{

			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.INFO, databaseProblem);
		}
		return "Item not found";
	}

	/**
	 * Reveals the name of the logged in user.
	 * @return the name
	 */
	public String getUserName()
	{
		return user.getName();
	}

	/**
	 * This method is <i>not</i> intended for use by the view.
	 * @return the customer logged into the vending machine
	 */
	public Customer getUser()
	{
		return user;
	}

	/**
	 * Gets the current balance of the user.
	 * @return the amount of money available
	 */
	public int getBalance() {
		return user.getMoney();
	}

	/**
	 * @return The item purchased by the user or null if no item has been
	 * purchased yet.
	 **/
	public FoodItem getPurchasedItem()
	{
		return purchasedItem;
	}

	/**
	 * Checks whether the given food item is stocked in the vending machine
	 * @param item The item to check
	 * @return True iff the item is in stock in the vending machine.
	 **/
	private boolean isStocked(FoodItem item)
	{
		for (Row[] column : machine.getCurrentLayout().getRows())
		{
			for (Row row : column)
			{
				if (row != null && row.getRemainingQuantity() > 0 && row.getProduct().equals(item))
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets an ordered list of most frequently bought items by the current user.
	 * @return A list of items sorted in descending order of frequency the
	 * current user has bought the item.
	 **/
	public ArrayList<FoodItem> getFrequentlyBought()
	{
		ArrayList<Transaction> raw = null;
		try
		{
			raw = db.getTransactionsByCustomer(user);
		}
		catch (Exception databaseProblem)
		{
			ControllerExceptionHandler.registerConcern(ControllerExceptionHandler.Verbosity.ERROR, databaseProblem);
			return null;
		}
		final HashMap<FoodItem, Integer> counts = new HashMap<FoodItem, Integer>();
		for (Transaction trans : raw)
		{
			if (isStocked(trans.getProduct()))
			{
				if (!counts.containsKey(trans.getProduct()))
					counts.put(trans.getProduct(), 1);
				else
					counts.put(trans.getProduct(), counts.get(trans.getProduct()) + 1);
			}
		}
		ArrayList<FoodItem> favorites = new ArrayList<FoodItem>(counts.keySet());
		Collections.sort(favorites, new Comparator<FoodItem>()
		{
			public int compare(FoodItem item1, FoodItem item2)
			{
				return -counts.get(item1).compareTo(counts.get(item2));
			}
		});
		return favorites;
	}
}
