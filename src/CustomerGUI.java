public class CustomerGUI
{
	/**
	 * Main method, displays the GUI for the vending machine specified on the
	 * command line.
	 * @param args Command line arguments:
	 * <ol>
	 * <li>ID of the vending machine</li>
	 * </ol>
	 **/
	public static void main(String[] args) throws Exception
	{
		GUIUtilities.setNativeLookAndFeel();
		BaseGUI base = new BaseGUI("Customer Login Screen");
		CustomerLoginScreen controller = CustomerLoginScreen.buildInstance(Integer.parseInt(args[0]));
		CustomerLoginScreenGUI gui = new CustomerLoginScreenGUI(controller);
		base.pushContentPanel(gui);
		base.displayGUI();
	}
}