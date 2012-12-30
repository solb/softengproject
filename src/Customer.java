/**
 * Represents a customer.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class Customer extends User
{
	/** The name by which cash customers are known. */
	public static final String CASH_NAME="Anonymous";

	/** The ID reserved for cash customers. */
	private static final int CASH_ID=Integer.MAX_VALUE;

	/** The person's account balance. */
	private int money;

	/**
	 * Default (cash customer) constructor.
	 * Creates a special <i>cash</i> customer with the ability to change its balance on the fly.
	 * The balance starts out at <tt>0</tt>, however, and remains there until funds are added.
	 */
	public Customer()
	{
		super(CASH_NAME);
		setId(CASH_ID); //we *cannot* get an IllegalStateException because we just created the parent instance
		money=0;
	}

	/**
	 * Fresh (account-backed customer) constructor.
	 * Creates an instance with the specified initial balance.
	 * @param name the <tt>Customer</tt>'s name
	 * @param money the <tt>Customer</tt>'s initial balance
	 * @throws IllegalArgumentException if the <tt>name</tt> is invalid or the <tt>money</tt> is negative
	 */
	public Customer(String name, int money) throws IllegalArgumentException
	{
		super(name);
		
		if(money<0)
			throw new IllegalArgumentException("Money must not be negative");
		
		this.money=money;
	}

	/**
	 * Copy constructor.
	 * Creates a copy of the supplied instance.
	 * @param existing the instance to clone
	 */
	public Customer(Customer existing)
	{
		super(existing);
		this.money=existing.money;
	}

	/**
	 * This method may only be used to obtain the primary key of a <i>non-cash</i> customer if such a key has actually been set.
	 * @return the primary key
	 * @throws IllegalStateException if the instance has never been assigned a primary key
	 * @throws UnsupportedOperationException if the instance is a cash customer
	 */
	@Override
	public int getId() throws IllegalStateException, UnsupportedOperationException
	{
		if(super.getId()==CASH_ID)
			throw new UnsupportedOperationException("Cash customers refuse to show their IDs");
		
		return super.getId();
	}
	
	/**
	 * Note that <tt>deductMoney(int)</tt> is more appropriate for fulfilling purchases.
	 * @param money the new balance
	 * @throws IllegalArgumentException if a negative value is supplied
	 */
	public void setMoney(int money)
	{
		if(money<0)
			throw new IllegalArgumentException("Money must not be negative");
		
		this.money=money;
	}

	/**
	 * @return the account balance
	 */
	public int getMoney()
	{
		return money;
	}

	/**
	 * Deducts the specified amount from the account.
	 * This amount may be positive, negative, or zero, but must not cause the balance to go negative.
	 * @param change the value by which to diminish the balance
	 * @return whether the operation succeeded (i.e. didn't exceed the available funds)
	 */
	public boolean deductMoney(int change)
	{
		if(change<=money) //balance wouldn't go negative
		{
			money-=change;
			
			return true;
		}
		else
			return false;
	}

	/**
	 * Indicates whether this customer is a cash customer.
	 * @return the answer to The Question
	 */
	public boolean isCashCustomer()
	{
		try
		{
			return getId()==CASH_ID; //always false if it doesn't fail
		}
		catch(IllegalStateException noneSet) //ID was unset
		{
			return false; //not a cash customer
		}
		catch(UnsupportedOperationException informationWithheld) //no reply
		{
			return true; //must be a cash customer
		}
	}
}