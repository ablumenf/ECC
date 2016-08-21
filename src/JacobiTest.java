package src;

import static org.junit.Assert.*;
import org.junit.Test;

public class JacobiTest {

	@Test
	public void TestZero() {
		long rval = ECMath.jacobi(19, 19);
		assertEquals(0, rval, 0);
	}
	
	@Test
	public void TestSquare() {
		long rval = ECMath.jacobi(57, 997);
		assertEquals(1, rval, 0);
	}
	
	@Test
	public void TestNonsquare() {
		long rval = ECMath.jacobi(114, 997);
		assertEquals(-1, rval, 0);
	}
}