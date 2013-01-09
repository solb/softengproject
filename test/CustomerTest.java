import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.GregorianCalendar;

/**
 * Unit test suite for <tt>Customer</tt>.
 * @author Lane Lawley <lxl5734@rit.edu>
 */
@RunWith(JUnit4.class)
public class CustomerTest {
	@Test
	public void testNormalConstruction() {
		Customer krutz = new Customer("Krutz", 512);

		Assert.assertTrue(krutz.getName().equals("Krutz"));
		Assert.assertTrue(krutz.getMoney() == 512);
	}

	@Test
	public void testCopyConstruction() {
		Customer krutz = new Customer("Krutz", 512);
		Customer kCopy = new Customer(krutz);

		Assert.assertTrue(krutz.equals(kCopy));
		Assert.assertFalse(krutz == kCopy);
	}

	@Test
	public void testNullName() {
		boolean testFailed = false;

		try {
			Customer c = new Customer(null, 512);
		} catch(IllegalArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}

	@Test
	public void testNegativeMoney() {
		boolean testFailed = false;

		try {
			Customer c = new Customer("Krutz", -5);
		} catch(IllegalArgumentException e) {
			testFailed = true;
		} finally {
			Assert.assertTrue(testFailed);
		}
	}
}