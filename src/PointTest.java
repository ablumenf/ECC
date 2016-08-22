package src;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTest {

	@Test
	public void AddTest() {
		Point P = new Point(5, 1, 1);
        Point Q = new Point(6, 3, 1);
        assertEquals(new Point(10, 6, 1), Q.add(P, 2, 2, 17));
	}
	
	@Test
	public void MultTest() {
		Point P = new Point(71264, 344, 1);
		assertEquals(new Point(36493, 22255, 1), P.mult(2, 64379, 22921, 71933));
	}
}