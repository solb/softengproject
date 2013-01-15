import java.util.Scanner;
import java.util.*;

/**
 * Utility class for creating a usable CLI.
 * Provides methods to do things like prompt the user, present an options list, etc.
 */
public class CLIUtilities {

	/**
	 * Scanner instance
	 **/
	private static final Scanner scan = new Scanner(System.in);

	/** Private constructor to disable construction */
	private CLIUtilities() {

	}


	/**
	 * Prompts the user with a given message and returns their response.
	 * The given message is printed verbatim, with no newline, and one line of input is recorded and returned.
	 *
	 * @param  message	The message to show the user, verbatim, with no newline at the end
	 * @return			The user's response to the message. One line of recorded input
	 */
	public static String prompt(String message) {
		System.out.print(message + ": ");
		return scan.nextLine();
	}

	/**
	 * Provides a simple way to get the user to pick an option.
	 * The options passed in as args are listed in numerical order.
	 * Then, the user is prompted for their selection (starting at 1, not 0).
	 * The user is re-prompted for as long as the selection is invalid.
	 * The selected number is then returned, after having 1 subtracted from it.
	 * This is because the programmer should deal with the selection starting from 0, not 1.
	 *
	 * @param  options			Variable arguments list containing all options to display
	 * @return selectedOption	The selected option (numbered from 0, not 1)
	 */
	public static int option(String... options) {
		System.out.println();
		for(int i = 0; i < options.length; ++i) {
			System.out.println((i+1) + ". " + options[i]);
		}
		System.out.println();

		int selectedOption = -1;
		do {
			try {
				selectedOption = Integer.parseInt(prompt("Select An Option")) - 1;
			} catch(NumberFormatException e) {
				continue;
			}
		} while(selectedOption < 0 || selectedOption >= options.length);

		return selectedOption;
	}

	/**
	 * Provies a method for users to select from a list of objects.
	 * @param options The array list of objects the user will select from
	 * @return The index of the item (0 based) the user selected
	 **/
	public static int option(ArrayList<? extends Object> options) {
		System.out.println();
		for(int i = 0; i < options.size(); ++i) {
			System.out.println((i+1) + ". " + options.get(i));
		}
		System.out.println();

		int selectedOption = -1;
		do {
			try {
				selectedOption = Integer.parseInt(prompt("Select An Option")) - 1;
			} catch(NumberFormatException e) {
				continue;
			}
		} while(selectedOption < 0 || selectedOption >= options.size());

		return selectedOption;
	}

	/**
	 * shows a new title
	 * @param name the name of the new title
	 */
	public static void printTitle( String name ) {
		System.out.println("        ");
		System.out.println("========");
		System.out.println(name);
		System.out.println("========");
	}

	/**
	 * Provides a simple way to get the user to enter a float for cash amounts
	 * @param prompt 
	 * @return moneys the amount of money returned
	 */
	public static int moneyPrompt( String prompt ) {
		float moneys = -1;
		do {
			try {
				moneys = Float.parseFloat(prompt(prompt) );
				moneys *= 100;
				if ( Math.abs(moneys - (int)moneys) > (Math.pow(10, -23)) ) {
					moneys = -1;
					System.out.println("Please have only two places after the decimal");
				}
			} catch (NumberFormatException e) {
				moneys = -1;
				continue;
			}
		} while (moneys < 0);
		return (int)moneys;
	} 

	/**
	 * Provides a simple way to get a number of ints
	 * @param prompt the prompt to the screen
	 * @return the int
	 */
	public static int promptInt( String prompt ) {
		int theInt = -1;
		do {
			try {
				theInt = Integer.parseInt(prompt(prompt));
			} catch(NumberFormatException e) {
				theInt = -1;
				continue;
			}
		} while(theInt < 0);
		return theInt;
	}

	public static int promptIntDefault(String prompt, int defVal) {
		int theInt = -1;
		do {
			try {
				String thePrompt = prompt(prompt + " (default " + defVal + ")");
				if(thePrompt.equals("")) {
					return defVal;
				}

				theInt = Integer.parseInt(thePrompt);
			} catch(NumberFormatException e) {
				theInt = -1;
				continue;
			}
		} while(theInt < 0);
		return theInt;
	}

	/**
	 * Prompts the user (Y/N) for a message.
	 *
	 * @param  prompt	The message to display to the user before they select Y/N
	 * @return			True if the user said "y" or "Y", false if the user said "n" or "N"
	 */
	public static boolean yesOrNo(String prompt) {
		String response;
		do {
			response = CLIUtilities.prompt(prompt + " (Y/N)");
		} while(!response.toLowerCase().equals("y") && !response.toLowerCase().equals("n"));

		return response.toLowerCase().equals("y");
	}

	/** 
	 * Provides a way to print a collection
	 * @param col the collection to print
	 */
	public static void printCollection( Collection<? extends Object> col ) {
		for ( Object next : col )
			System.out.println( next.toString() );
	}

	/**
	 * Allows easily formatting quantities of money.
	 * @param amount the amount of money, in cents
	 */
	public static String formatMoney(int amount)
	{
		String amt=String.format("%03d", amount);
		int split=amt.length()-2;
		
		return "$"+amt.substring(0, split)+'.'+amt.substring(split);
	}

	/**
	 * Provides a nice way to print a layout
	 * @param rows The layout to print
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(FoodItem[][] rows, boolean hideInactive)
	{
		for (int i=0;i<rows.length;++i)
		{
			FoodItem[] column = rows[i];
			for (int j=0;j<column.length;++j)
			{
				System.out.printf("(%d, %d) ", i, j);
				FoodItem item = column[j];
				String name = "EMPTY";
				if (item != null) {
					if(item.isActive() || !hideInactive) {
						name = item.getName();
					} else {
						name = "INACTIVE";
					}
				}
				System.out.printf("%-10s ", name);
			}
			System.out.println();
			for (int j=0;j<column.length;++j)
			{
				FoodItem item = column[j];
				String price = "";
				if (item != null && item.isActive())
					price = String.format("$%-8.2f ", item.getPrice()/100.0);
				else if (item != null && !hideInactive)
					price = "(INACTIVE)";
				System.out.printf("       %10s ", price);
			}
			System.out.println();
			System.out.println();
		}
	}

	/**
	 * Provides a nice way to print a layout. Hides inactive items
	 * @param rows The layout to print
	 **/
	public static void printLayout(FoodItem[][] rows)
	{
		printLayout(rows, true);
	}

	/**
	 * Provides a nice way to print a layout
	 * Note: Not tested
	 * @param rows The layout to print
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(Row[][] rows, boolean hideInactive)
	{
		if (rows.length == 0 || rows[0].length == 0)
			return;
		FoodItem[][] temp = new FoodItem[rows.length][rows[0].length];
		for (int i=0;i<rows.length;++i)
		{
			Row[] column = rows[i];
			for (int j=0;j<column.length;++j)
			{
				Row row = column[j];
				FoodItem item = null;
				if (row != null)
					item = row.getProduct();
				temp[i][j] = item;
			}
		}
		printLayout(temp, hideInactive);
	}

	/**
	 * Provides a nice way to print a layout. Hides inactive items.
	 * @param rows The layout to print
	 **/
	public static void printLayout(Row[][] rows)
	{
		printLayout(rows, true);
	}

	/**
	 * Provides a nice way to print a layout
	 * Note: Not tested
	 * @param layout The layout to print
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(VMLayout layout, boolean hideInactive)
	{
		printLayout(layout.getRows(), hideInactive);
	}

	/**
	 * Provides a nice way to print a layout. Hides inactive items.
	 * @param hideInactive Whether to hide inactive items
	 **/
	public static void printLayout(VMLayout layout)
	{
		printLayout(layout, true);
	}
}