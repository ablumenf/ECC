package src;

import static org.junit.Assert.*;
import org.junit.Test;

public class PolynomialTest {

	@Test
	public void AddTest() {
		Polynomial p = new Polynomial("z^4 + z^3 + z^2 + z");
		Polynomial q = new Polynomial("z^2 + z + 1");
		assertTrue(new Polynomial("z^4 + z^3 + 1").equals(p.add(q)));
	}
	
	@Test
	public void MultTest() {
		Polynomial p = new Polynomial("z^4 + z^3 + z^2 + z");
		Polynomial q = new Polynomial("z^2 + z + 1");
		assertTrue(new Polynomial("z^6 + z^4 + z^3 + z").equals(p.mult(q)));
	}
	
	@Test
	public void ModTest() {
		Polynomial p = new Polynomial("z^4 + z^3 + z^2 + z");
		Polynomial q = new Polynomial("z^2 + z + 1");
		assertTrue(new Polynomial("z").equals(p.mod(q)));
	}
	
	@Test
	public void InverseTest() {
		Polynomial p = new Polynomial("z^2");
		Polynomial q = new Polynomial("z^4 + z + 1");
		assertTrue(new Polynomial("z^3 + z^2 + 1").equals(p.inverse(q)));
		System.out.println(new Polynomial("z^2").inverse(new Polynomial("z^4 + z + 1")));
	}
	
	@Test
	public void ValidTest() {
		assertEquals(true, Polynomial.isValid("3z"));
	}
	
	@Test
	public void PolySqrt() {
		Polynomial p = new Polynomial("z^3 + z^2");
		assertTrue(new Polynomial("z^3").equals(p.sqrt(new Polynomial("z^4 + z + 1"))));
	}
}