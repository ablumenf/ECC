/**
 * @author Aaron Blumenfeld
 * This is a basic Java Swing application to implement the most commonly
 * used computations in elliptic curve cryptography. It is intended to be
 * used for instructive purposes (by students or educators) studying
 * cryptography. Only primes p < 10,000 are supported, and only primes 
 * p < 300 are supported for generating lists of all curves (or all curves
 * with prime order) mod p. This is because the number of curves mod p
 * grows too quickly as p grows. For larger primes, you can generate a
 * fixed number of randomly chosen curves (general curves, or curves with
 * prime order).
 * 
 * For binary elliptic curves, only fields F_m for m = 2, 4, 8, ..., 1024 
 * are supported. Again, this program is only designed for instructive
 * purposes. Only binary fields with up to 1024 elements are supported.
 * You can choose a field by selecting an irreducible polynomial from the
 * drop-down menu. Only binary fields with up to 64 elements are supported
 * for generating lists of all curves. This is because the number of curves
 * over these fields grows too quickly as the size of the field grows. For
 * larger fields, you can generate a fixed number of randomly chosen curves.
 * 
 * A discrete logarithm of -1 indicates an error. This means that there is
 * no solution. For example, if you choose a non-cyclic elliptic curve group
 * and want to solve kG = P, where G generates a proper subgroup of E and P
 * lies in a different coset, there will be no solution.
 * 
 * I did not implement error detection for trying to do point arithmetic when
 * the point is not on the curve. Perhaps it can be instructive to see what
 * happens with the calculations in this situation.
 * 
 * There is also a Misc. tab, which includes support for exponentiation, inverses,
 * and square roots (mod p), as well as addition, multiplication, inverses, and 
 * exponentiation of polynomials in finite fields.
 * 
 * When you enter a value in the GF(p) tab (other than the modulus, k, and n), it
 * automatically gets reduced mod p. Similarly, when you enter a polynomial in
 * the GF(2^r) and Misc. tabs, it automatically gets reduced mod the irreducible
 * polynomial and R(z), respectively.
 */

import java.awt.Dimension;
import javax.swing.*;
import java.io.IOException;

public class ECC extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Dimension _size = new Dimension(400, 600);

	public ECC() throws IOException {
		
		setSize(_size);
		setVisible(true);
		setResizable(false);

		setTitle("ECC Toolkit");
		JTabbedPane tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane);
		PrimePanel prime = new PrimePanel();
		BinaryPanel binary = new BinaryPanel();
		MiscPanel misc = new MiscPanel();
		
		tabbedPane.addTab("GF(p)", prime);
		tabbedPane.addTab("GF(2^r)", binary);
		tabbedPane.addTab("Misc.", misc);
	}

	public static void main(String[] args) throws IOException { /* driver method */
		ECC ecc = new ECC();
		ecc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes the window
		ecc.pack();
		ecc.setVisible(true);
	}
}