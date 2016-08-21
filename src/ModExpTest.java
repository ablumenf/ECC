package src;

import static org.junit.Assert.*;
import org.junit.Test;

public class ModExpTest {

	@Test
	public void testPrime() {
		long rval = ECMath.modExp(13, 5, 11);
		assertEquals(10, rval, 0);
	}
	
	@Test
	public void testComposite() {
		long rval = ECMath.modExp(20, 10, 12);
		assertEquals(4, rval, 0);
	}
}