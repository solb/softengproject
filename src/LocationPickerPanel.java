import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.util.Vector;
import java.util.Arrays;
import java.awt.GridLayout;

/**
 * Allows the user to select a location.
 * @author Matthew Koontz
 **/
public class LocationPickerPanel extends JPanel implements ActionListener
{
	/**
	 * The location that is being edited.
	 **/
	private Location location;

	/**
	 * The field for the zip code.
	 **/
	private NumberField zipCodeField;

	/**
	 * The field for the state.
	 **/
	private JTextField stateField;

	/**
	 * The list of nearby businesses.
	 **/
	private JList businessList;

	/**
	 * The field to change the name of a nearby business.
	 **/
	private JTextField businessField;

	/**
	 * The button that adds a new business.
	 **/
	private JButton addBusinessButton;

	/**
	 * The button that changes the name of a nearby business.
	 **/
	private ConditionButton editBusinessButton;

	/**
	 * Button that removes a nearby business. KaPow
	 **/
	private ConditionButton removeBusinessButton;

	/**
	 * The list of nearby businesses.
	 **/
	private Vector<String> businesses;

	/**
	 * Creates a new location picker to edit the given location.
	 * @param location The location to edit.
	 **/
	public LocationPickerPanel(Location location)
	{
		this.location = location;

		zipCodeField = new NumberField(NumberField.NONNEGATIVE_Z);
		stateField = new JTextField();
		businessList = new JList();
		businessField = new JTextField();
		addBusinessButton = new JButton("Add Business");
		editBusinessButton = new ConditionButton("Edit Business");
		removeBusinessButton = new ConditionButton("Remove Business");
		if (location != null)
			businesses = new Vector<String>(Arrays.asList(location.getNearbyBusinesses()));
		else
			businesses = new Vector<String>();

		addComponents();
		addLogic();
		fillData();
	}

	/**
	 * Fills to form with the initial data.
	 **/
	private void fillData()
	{
		if (location != null)
		{
			zipCodeField.setText(location.getZipCode()+"");
			stateField.setText(location.getState());
		}
		refreshData();
	}

	/**
	 * Refreshes the data in the list.
	 **/
	private void refreshData()
	{
		businessList.setListData(businesses);
		businessList.clearSelection();
		businessField.setText("");
		businessField.setEnabled(false);
	}

	/**
	 * Lays out the form.
	 **/
	private void addComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel businessPanel = new JPanel();
		businessPanel.setAlignmentY(TOP_ALIGNMENT);
		businessPanel.setLayout(new BoxLayout(businessPanel, BoxLayout.Y_AXIS));
		add(businessPanel);

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1,1,0,0));
		//businessList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		JScrollPane scrollPane = new JScrollPane(businessList);
		//scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		//scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		listPanel.add(scrollPane);
		listPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		listPanel.setAlignmentX(LEFT_ALIGNMENT);
		businessPanel.add(listPanel);

		businessPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel addBusinessPanel = new JPanel();
		addBusinessPanel.setLayout(new BoxLayout(addBusinessPanel, BoxLayout.X_AXIS));
		addBusinessPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		addBusinessPanel.setAlignmentX(LEFT_ALIGNMENT);
		addBusinessPanel.add(addBusinessButton);
		businessPanel.add(addBusinessPanel);

		businessPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel businessFieldPanel = new JPanel();
		businessFieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		businessFieldPanel.setAlignmentX(LEFT_ALIGNMENT);
		businessFieldPanel.setLayout(new BoxLayout(businessFieldPanel, BoxLayout.X_AXIS));
		businessFieldPanel.add(new JLabel("Business Name:"));
		businessFieldPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		//businessField.setColumns(10);
		businessField.setAlignmentX(LEFT_ALIGNMENT);
		//businessField.setMaximumSize(businessField.getPreferredSize());
		businessFieldPanel.add(businessField);
		businessPanel.add(businessFieldPanel);

		businessPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel businessButtonPanel = new JPanel();
		businessButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		businessButtonPanel.setAlignmentX(LEFT_ALIGNMENT);
		businessButtonPanel.setLayout(new BoxLayout(businessButtonPanel, BoxLayout.X_AXIS));
		businessPanel.add(businessButtonPanel);

		removeBusinessButton.setEnabled(false);
		//removeBusinessButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)removeBusinessButton.getHeight()));
		businessButtonPanel.add(removeBusinessButton);
		businessButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		editBusinessButton.setEnabled(false);
		//editBusinessButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)editBusinessButton.getHeight()));
		businessButtonPanel.add(editBusinessButton);

		businessPanel.setMaximumSize(businessPanel.getPreferredSize());

		add(Box.createRigidArea(new Dimension(20, 0)));

		scrollPane.setMaximumSize(new Dimension((int)businessField.getPreferredSize().getWidth(), Integer.MAX_VALUE));

		LabeledFieldPanel zipAndStatePanel = new LabeledFieldPanel();
		zipAndStatePanel.setAlignmentY(TOP_ALIGNMENT);
		add(zipAndStatePanel);

		zipCodeField.setColumns(10);
		zipCodeField.setMaximumSize(zipCodeField.getPreferredSize());
		zipAndStatePanel.addLabeledTextField("Zip Code:", zipCodeField);

		stateField.setColumns(30);
		stateField.setMaximumSize(stateField.getPreferredSize());
		zipAndStatePanel.addLabeledTextField("State:", stateField);

		add(Box.createGlue());
	}

	/**
	 * Adds the logic to the view.
	 **/
	private void addLogic()
	{
		addBusinessButton.addActionListener(this);
		removeBusinessButton.addActionListener(this);
		editBusinessButton.addActionListener(this);

		ConditionButtonCondition itemSelected = new ConditionButtonCondition()
		{
			@Override
			public boolean checkCondition()
			{
				return businessList.getSelectedValue() != null;
			}
		};

		editBusinessButton.addCondition(itemSelected);
		removeBusinessButton.addCondition(itemSelected);

		businessList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent _)
			{
				editBusinessButton.checkAndSetEnabled();
				removeBusinessButton.checkAndSetEnabled();
				businessField.setText((String)businessList.getSelectedValue());
				businessField.setEnabled(true);
			}
		});

	}

	/**
	 * @return The zip code that the user entered.
	 **/
	public int getZipCode()
	{
		return Integer.parseInt(zipCodeField.getText());
	}
	
	/**
	 * @return The state that the user entered.
	 **/
	public String getState()
	{
		return stateField.getText();
	}

	/**
	 * @return An array of the names of the nearby businesses.
	 **/
	public String[] getNearbyBusinesses()
	{
		return businesses.toArray(new String[0]);
	}

	/**
	 * Handles the buttons getting clicked.
	 * @param event Contains information regarding the event.
	 **/
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == addBusinessButton)
		{
			businesses.add("New Business");
			refreshData();
		}
		else if (source == editBusinessButton)
		{
			businesses.set(businessList.getSelectedIndex(), businessField.getText());
			refreshData();
		}
		else if (source == removeBusinessButton)
		{
			businesses.remove(businessList.getSelectedIndex());
			refreshData();
		}
	}
}