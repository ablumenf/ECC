package src.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import src.Polynomial;

public class PolynomialTest {

	@Test
	public void AddTest() {
		Polynomial p = new Polynomial("z^4 + z^3 + z^2 + z");
		Polynomial q = new Polynomial("z^2 + z + 1");
		assertEquals(new Polynomial("z^4 + z^3 + 1"), p.add(q));
	}
	
	@Test
	public void MultTest() {
		Polynomial p = new Polynomial("z^4 + z^3 + z^2 + z");
		Polynomial q = new Polynomial("z^2 + z + 1");
		assertEquals(new Polynomial("z^6 + z^4 + z^3 + z"), p.mult(q));
	}
	
	@Test
	public void ModTest() {
		Polynomial p = new Polynomial("z^4 + z^3 + z^2 + z");
		Polynomial q = new Polynomial("z^2 + z + 1");
		assertEquals(new Polynomial("z"), p.mod(q));
	}
	
	@Test
	public void InverseTest() {
		Polynomial p = new Polynomial("z^2");
		Polynomial q = new Polynomial("z^4 + z + 1");
		assertEquals(new Polynomial("z^3 + z^2 + 1"), p.inverse(q));
	}
	
	@Test
	public void ValidTest() {
		assertTrue(Polynomial.isValid("3z"));
	}
	
	@Test
	public void PolySqrt() {
		Polynomial p = new Polynomial("z^3 + z^2");
		assertEquals(new Polynomial("z^3"), p.sqrt(new Polynomial("z^4 + z + 1")));
	}
}