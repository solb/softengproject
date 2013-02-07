import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Dimension;

/**
 * Changes the location of a vending machine.
 * @author Matthew Koontz
 **/
public class ChangeLocationGUI extends JPanel implements ActionListener
{
	/**
	 * Saves the data.
	 **/
	private JButton confirmButton;

	/**
	 * Cancels the changes.
	 **/
	private JButton cancelButton;

	/**
	 * Controller instance.
	 **/
	private ManagerMachineManagementScreen controller;

	/**
	 * Master frame
	 **/
	private BaseGUI master;

	/**
	 * Machine that is getting its location changed.
	 **/
	private VendingMachine machine;

	/**
	 * Allows the user to select a location.
	 **/
	private LocationPickerPanel locationPicker;

	/**
	 * Parent screen.
	 **/
	private ManagerMachineManagementScreenGUI parent;

	/**
	 * Creates a new ChangeLocationGUI to change the location of the given vending machine.
	 * @param controller The controller instance for this view.
	 * @param master The master frame.
	 * @param machine The machine to change the location for.
	 * @param parent The parent screen. Used to refresh the list.
	 **/
	public ChangeLocationGUI(ManagerMachineManagementScreen controller, BaseGUI master, VendingMachine machine, ManagerMachineManagementScreenGUI parent)
	{
		this.controller=controller;
		this.master = master;
		this.machine = machine;
		this.parent = parent;

		confirmButton = new JButton("Confirm Location Change");
		cancelButton = new JButton("Cancel Location Change");

		locationPicker = new LocationPickerPanel(machine.getLocation());
		addComponents();
		addLogic();
	}

	/**
	 * Adds logic to the components.
	 **/
	private void addLogic()
	{
		cancelButton.addActionListener(this);
		confirmButton.addActionListener(this);
	}

	/**
	 * Lays out the components.
	 **/
	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(locationPicker);

		add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel bottomPanel = new JPanel();
		add(bottomPanel);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(cancelButton);
		bottomPanel.add(Box.createGlue());
		bottomPanel.add(confirmButton);
	}

	/**
	 * Handles buttons getting clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == confirmButton)
		{
			if (controller.changeMachineLocation(machine, locationPicker.getZipCode(), locationPicker.getState(), locationPicker.getNearbyBusinesses()))
				master.getStatusBar().setStatus("Location changed successfully", StatusBar.STATUS_GOOD_COLOR);
			else
				master.getStatusBar().setStatus("An error occurred while attempting to change the location", StatusBar.STATUS_BAD_COLOR);
			master.popContentPanel();
			parent.refreshList();
		}
		else if (source == cancelButton)
		{
			master.popContentPanel();
		}
	}
}