package src;

import static org.junit.Assert.*;
import org.junit.Test;

public class PolynomialPointTest {

	@Test
	public void test() {
		Polynomial x = new Polynomial("z^3");
    	Polynomial y = new Polynomial("1");
    	Polynomial z = new Polynomial("1");
    	Polynomial a = new Polynomial("z^3");
    	Polynomial b = new Polynomial("z^3 + 1");
    	Polynomial m = new Polynomial("z^4 + z + 1");
    	PolynomialPoint P = new PolynomialPoint(x, y, z);
    	PolynomialPoint result = P.mult(5, a, b, m);
    	assertEquals(new PolynomialPoint(new Polynomial("z^3 + z + 1"), new Polynomial("z"), new Polynomial("1")), result);
	}
}