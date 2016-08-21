package src;

import static org.junit.Assert.*;
import org.junit.Test;

public class PrimeTest {

	@Test
	public void Prime() {
		boolean rval = ECMath.isPrime(118428421);
		assertEquals(true, rval);
	}
	
	@Test
	public void Composite() {
		boolean rval = ECMath.isPrime(1197397); // = 997 * 1201
		assertEquals(false, rval);
	}
	
	@Test
	public void randomPrime() {
		long rval = ECMath.randomPrime(4);
		assertTrue(rval > 999 && rval < 10000 && ECMath.isPrime(rval));
	}
}