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
 * A discrete logarithm of -1 indicates an error. This means that there is
 * no solution. For example, if you choose a non-cyclic elliptic curve group
 * and want to solve kG = P, where G generates a proper subgroup of E and P
 * lies in a different coset, there will be no solution.
 * 
 * I did not implement error detection for trying to do point arithmetic when
 * the point is not on the curve. Perhaps it can be instructive to see what
 * happens with the calculations in this situation.
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ECC extends JFrame {

	private static final long serialVersionUID = 1L;

	private Dimension _size = new Dimension(400, 600);
	private long a = 7;
	private long b = 12;
	private long p = 103; /* initial curve: E(F_1093) : y^2 = x^3 + x + 1 */
	private long Gx = 102; /* initial generator: G = (0, 1) */
	private long Gy = 2;
	private long Px = 9; /* initially P = (413, 959) */
	private long Py = 17;
	private long Qx = 19; /* initially Q = (36, 437) */
	private long Qy = 0;
	private long k = 33; /* initially k = 33 */
	private int n = 100; /* initially n = 100 random curves */

	public ECC() { // constructor for the ECC Toolkit
		JFrame frame = new JFrame("ECC Toolkit");
		frame.setSize(_size);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel outputPan = new JPanel();
		outputPan.setLayout(new FlowLayout());
		
		JTextArea _output = new JTextArea ("", 12, 42);
		_output.setLineWrap(true);
		_output.setWrapStyleWord(false);
		JScrollPane scroll = new JScrollPane (_output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		outputPan.add(scroll);

		final JTextField _a = new JTextField("" + a + "        ");
		_a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a = Long.parseLong(_a.getText().trim());
				_output.setText(_output.getText() + new EllipticCurve(a, b, p) + "\n");
			}
		});
		
		final JTextField _b = new JTextField("" + b + "        ");
		_b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				b = Long.parseLong(_b.getText().trim());
				_output.setText(_output.getText() + new EllipticCurve(a, b, p) + "\n");
			}
		});
		
		final JTextField _p = new JTextField("" + p + "        ");
		_p.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long input = Long.parseLong(_p.getText().trim());
				if(!ECMath.isPrime(input)) {
					_output.setText(_output.getText() + input + " is not prime. Please use a prime number under 10000 as your modulus.\n");
				} else if(input >= 10000) {
					_output.setText(_output.getText() + input + " is too large. Please use a prime number under 10000 as your modulus.\n");
				}
				else {
					p = input;
					_output.setText(_output.getText() + new EllipticCurve(a, b, p) + "\n");
				}
			}
		});
		
		final JTextField _Gx = new JTextField("" + Gx + "        ");
		_Gx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gx = Long.parseLong(_Gx.getText().trim());
				_output.setText(_output.getText() + "G = " + new Point(Gx, Gy, 1) + "\n");
			}
		});
		
		final JTextField _Gy = new JTextField("" + Gy + "        ");
		_Gy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gy = Long.parseLong(_Gy.getText().trim());
				_output.setText(_output.getText() + "G = " + new Point(Gx, Gy, 1) + "\n");
			}
		});

		JPanel ecInfoPan = new JPanel();
		ecInfoPan.setLayout(new FlowLayout());
		ecInfoPan.add(new JLabel("E : y^2 = x^3 + "));
		ecInfoPan.add(_a);
		ecInfoPan.add(new JLabel("x + "));
		ecInfoPan.add(_b);
		ecInfoPan.add(new JLabel("(mod "));
		ecInfoPan.add(_p);
		ecInfoPan.add(new JLabel("), Generator G = ("));
		ecInfoPan.add(_Gx);
		ecInfoPan.add(new JLabel(", "));
		ecInfoPan.add(_Gy);
		ecInfoPan.add(new JLabel(")"));

		final JTextField _Px = new JTextField("" + Px + "        ");
		_Px.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Px = Long.parseLong(_Px.getText().trim());
				_output.setText(_output.getText() + "P = " + new Point(Px, Py, 1) + "\n");
			}
		});
		
		final JTextField _Py = new JTextField("" + Py + "        ");
		_Py.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Py = Long.parseLong(_Py.getText().trim());
				_output.setText(_output.getText() + "P = " + new Point(Px, Py, 1) + "\n");
			}
		});
		
		final JTextField _Qx = new JTextField("" + Qx + "        ");
		_Qx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Qx = Long.parseLong(_Qx.getText().trim());
				_output.setText(_output.getText() + "Q = " + new Point(Qx, Qy, 1) + "\n");
			}
		});
		
		final JTextField _Qy = new JTextField("" + Qy + "        ");
		_Qy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Qy = Long.parseLong(_Qy.getText().trim());
				_output.setText(_output.getText() + "Q = " + new Point(Qx, Qy, 1) + "\n");
			}
		});
		
		final JTextField _k = new JTextField("" + k + "        ");
		_k.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				k = Long.parseLong(_k.getText().trim());
				_output.setText(_output.getText() + "k = " + k + "\n");
			}
		});

		JPanel ptsInfoPan = new JPanel();
		ptsInfoPan.setLayout(new FlowLayout());
		ptsInfoPan.add(new JLabel("P = ("));
		ptsInfoPan.add(_Px);
		ptsInfoPan.add(new JLabel(", "));
		ptsInfoPan.add(_Py);
		ptsInfoPan.add(new JLabel("), Q = ("));
		ptsInfoPan.add(_Qx);
		ptsInfoPan.add(new JLabel(", "));
		ptsInfoPan.add(_Qy);
		ptsInfoPan.add(new JLabel("), k = "));
		ptsInfoPan.add(_k);
		
		JPanel arithmeticPan = new JPanel();
		arithmeticPan.setLayout(new FlowLayout());
		JButton addition = new JButton("P + Q");
		addition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point P = new Point(Px, Py, 1);
				Point Q = new Point (Qx, Qy, 1);
				Point result = P.add(Q, a, b, p);
				_output.setText(_output.getText() + P + " + " + Q + " = " + result + "\n");
			}
		});
		
		JButton multiplication = new JButton("kP");
		multiplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point P = new Point(Px, Py, 1);
				Point result = P.mult(k, a, b, p);
				_output.setText(_output.getText() + k + "" + P + " = " + result + "\n");
			}
		});
		
		JButton log = new JButton("log_G(P)");
		log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point P = new Point(Px, Py, 1);
				Point G = new Point(Gx, Gy, 1);
				_output.setText(_output.getText() + "log_G" + P + " = " + new EllipticCurve(a, b, p).log(P, G) + "\n");
			}
		});
		
		arithmeticPan.add(addition);
		arithmeticPan.add(multiplication);
		arithmeticPan.add(log);
		
		JPanel orderPan = new JPanel();
		orderPan.setLayout(new FlowLayout());
		JButton curveOrder = new JButton("Order(E)");
		curveOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_output.setText(_output.getText() + "|E| = " + new EllipticCurve(a, b, p).order() + "\n");
			}
		});
		
		JButton pointOrder = new JButton("Order(G)");
		pointOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point G = new Point(Gx, Gy, 1);
				_output.setText(_output.getText() + "|G| = " + new EllipticCurve(a, b, p).pointOrder(G) + "\n");
			}
		});
		
		orderPan.add(curveOrder);
		orderPan.add(pointOrder);
		
		JPanel listPan = new JPanel();
		listPan.setLayout(new FlowLayout());
		JButton listCurves = new JButton("List ECs");
		listCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(p >= 300) {
					_output.setText(_output.getText() + "Please use a prime under 300 or generate a smaller number of random curves mod p.\n");
				}
				else {
					_output.setText(_output.getText() + "List of elliptic curves (mod " + p + "):\n" + EllipticCurve.listECs(p) + "\n");
				}
			}
		});
		
		JButton listPrimeCurves = new JButton("List ECs of prime order");
		listPrimeCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(p >= 300) {
					_output.setText(_output.getText() + "Please use a prime under 300 or generate a smaller number of random curves mod p.\n");
				}
				else {
					_output.setText(_output.getText() + "List of elliptic curves (mod " + p + ") of prime order:\n" + EllipticCurve.listPrimeECs(p) + "\n");
				}
			}
		});
		
		listPan.add(listCurves);
		listPan.add(listPrimeCurves);
		
		JPanel randListPan = new JPanel();
		randListPan.setLayout(new FlowLayout());

		JButton randListCurves = new JButton("Random ECs");
		randListCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_output.setText(_output.getText() + "List of " + n + " random elliptic curves (mod " + p + "):\n" + EllipticCurve.listRandomECs(n, p) + "\n");
			}
		});

		JButton randListPrimeCurves = new JButton("Random ECs of prime order");
		randListPrimeCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_output.setText(_output.getText() + "List of " + n + " random elliptic curves (mod " + p + ") of prime order:\n" + EllipticCurve.listRandomPrimeECs(n, p) + "\n");
			}
		});
		
		randListPan.add(new JLabel("Generate "));
		final JTextField _n = new JTextField("" + n + "        ");
		_n.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				n = Integer.parseInt(_n.getText().trim());
				_output.setText(_output.getText() + "Random curve selection will now generate " + n + " curves.\n");
			}
		});

		randListPan.add(_n);
		randListPan.add(randListCurves);
		randListPan.add(randListPrimeCurves);
		
		JPanel pointsPan = new JPanel();
		pointsPan.setLayout(new FlowLayout());
		
		JButton listPoints = new JButton("List points on E");
		listPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_output.setText(_output.getText() + "List of points on E(F_" + p + "):\n" + new EllipticCurve(a, b, p).listECpts() + "\n");
			}
		});
		
		JButton listGMults = new JButton("List multiples of G");
		listGMults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_output.setText(_output.getText() + "List of multiples of G on E(F_" + p + "):\n" + new EllipticCurve(a, b, p).listGmults(new Point(Gx, Gy, 1)) + "\n");
			}
		});		
		
		pointsPan.add(listPoints);
		pointsPan.add(listGMults);
		
		JPanel clearPan = new JPanel();
		clearPan.setLayout(new FlowLayout());
		JButton clear = new JButton("Clear Output");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_output.setText("");
			}
		});	
		
		clearPan.add(clear);
		
		// add JPanels to JFrame
		frame.add(ecInfoPan);
		frame.add(ptsInfoPan);
		frame.add(arithmeticPan);
		frame.add(orderPan);
		frame.add(listPan);
		frame.add(randListPan);
		frame.add(pointsPan);
		frame.add(outputPan);
		frame.add(clearPan);

		frame.setDefaultCloseOperation(EXIT_ON_CLOSE); // closes the window
		frame.pack();
	}

	public static void main(String[] args) { /* driver method */
		ECC ecc = new ECC();
	}
}