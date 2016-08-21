package src;

import static org.junit.Assert.*;
import org.junit.Test;

public class SqrtTest {

	@Test
	public void TestZero() {
		long rval = ECMath.sqrt(19, 19);
		assertEquals(0, rval, 0);
	}
	
	@Test
	public void TestSquare() {
		long rval = ECMath.sqrt(57, 997);
		assertEquals(481, rval, 0);
	}
	
	@Test
	public void TestNonsquare() {
		long rval = ECMath.jacobi(114, 997);
		assertEquals(-1, rval, 0);
	}
}