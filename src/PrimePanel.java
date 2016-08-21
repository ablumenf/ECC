package src;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PrimePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private long a = 7;
	private long b = 12;
	private long p = 103; /* initial curve: E(F_103) : y^2 = x^3 + 7x + 12 */
	private long Gx = 102; /* initial generator: G = (102, 2) */
	private long Gy = 2;
	private long Px = 9; /* initially P = (9, 17) */
	private long Py = 17;
	private long Qx = 19; /* initially Q = (19, 0) */
	private long Qy = 0;
	private long k = 33; /* initially k = 33 */
	private int n = 100; /* initially n = 100 random curves */

	public PrimePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel outputPan = new JPanel();
		outputPan.setLayout(new FlowLayout());
		
		JTextArea output = new JTextArea ("", 12, 42);
		output.setLineWrap(true);
		output.setWrapStyleWord(false);
		JScrollPane primeScroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		outputPan.add(primeScroll);
		
		final JTextField _a = new JTextField("" + a + "        ");
		_a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					a = Math.floorMod(Long.parseLong(_a.getText().trim()), p);
					_a.setText("" + a);
					output.setText(output.getText() + new EllipticCurve(a, b, p) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_a.setText("" + a);
				}
			}
		});
		
		final JTextField _b = new JTextField("" + b + "        ");
		_b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					b = Math.floorMod(Long.parseLong(_b.getText().trim()), p);
					_b.setText("" + b);
					output.setText(output.getText() + new EllipticCurve(a, b, p) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_b.setText("" + b);
				}
			}
		});
		
		final JTextField _p = new JTextField("" + p + "        ");
		_p.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					long input = Long.parseLong(_p.getText().trim());
					if(!ECMath.isPrime(input)) {
						_p.setText("" + p);
						output.setText(output.getText() + input + " is not prime. Please use a prime number under 10000 as your modulus.\n");
					} else if(input < 5) {
						_p.setText("" + p);
						output.setText(output.getText() + input + " is too small. Please use a prime number over 3 as your modulus.\n");
					} else if(input >= 10000) {
						_p.setText("" + p);
						output.setText(output.getText() + input + " is too large. Please use a prime number under 10000 as your modulus.\n");
					}
					else {
						p = input;
						output.setText(output.getText() + new EllipticCurve(a, b, p) + "\n");
					}
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_p.setText("" + p);
				}
			}
		});
		
		final JTextField _Gx = new JTextField("" + Gx + "        ");
		_Gx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Gx = Math.floorMod(Long.parseLong(_Gx.getText().trim()), p);
					_Gx.setText("" + Gx);
					output.setText(output.getText() + "G = " + new Point(Gx, Gy, 1) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_Gx.setText("" + Gx);
				}
			}
		});
		
		final JTextField _Gy = new JTextField("" + Gy + "        ");
		_Gy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Gy = Math.floorMod(Long.parseLong(_Gy.getText().trim()), p);
					_Gy.setText("" + Gy);
					output.setText(output.getText() + "G = " + new Point(Gx, Gy, 1) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_Gx.setText("" + Gy);
				}
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
				try {
					Px = Math.floorMod(Long.parseLong(_Px.getText().trim()), p);
					_Px.setText("" + Px);
					output.setText(output.getText() + "P = " + new Point(Px, Py, 1) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_Px.setText("" + Px);
				}
			}
		});
		
		final JTextField _Py = new JTextField("" + Py + "        ");
		_Py.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Py = Math.floorMod(Long.parseLong(_Py.getText().trim()), p);
					_Py.setText("" + Py);
					output.setText(output.getText() + "P = " + new Point(Px, Py, 1) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_Py.setText("" + Py);
				}
			}
		});
		
		final JTextField _Qx = new JTextField("" + Qx + "        ");
		_Qx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Qx = Math.floorMod(Long.parseLong(_Qx.getText().trim()), p);
					_Qx.setText("" + Qx);
					output.setText(output.getText() + "Q = " + new Point(Qx, Qy, 1) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_Qx.setText("" + Qx);
				}
			}
		});
		
		final JTextField _Qy = new JTextField("" + Qy + "        ");
		_Qy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Qy = Math.floorMod(Long.parseLong(_Qy.getText().trim()), p);
					_Qy.setText("" + Qy);
					output.setText(output.getText() + "Q = " + new Point(Qx, Qy, 1) + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_Qy.setText("" + Qy);
				}
			}
		});
		
		final JTextField _k = new JTextField("" + k + "        ");
		_k.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					k = Long.parseLong(_k.getText().trim());
					output.setText(output.getText() + "k = " + k + "\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_k.setText("" + k);
				}
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
				output.setText(output.getText() + P + " + " + Q + " = " + result + "\n");
			}
		});
		
		JButton multiplication = new JButton("kP");
		multiplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point P = new Point(Px, Py, 1);
				Point result = P.mult(k, a, b, p);
				output.setText(output.getText() + k + "" + P + " = " + result + "\n");
			}
		});
		
		JButton log = new JButton("log_G(P)");
		log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point P = new Point(Px, Py, 1);
				Point G = new Point(Gx, Gy, 1);
				output.setText(output.getText() + "log_G" + P + " = " + new EllipticCurve(a, b, p).log(P, G) + "\n");
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
				output.setText(output.getText() + "|E| = " + new EllipticCurve(a, b, p).order() + "\n");
			}
		});
		
		JButton pointOrder = new JButton("Order(G)");
		pointOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point G = new Point(Gx, Gy, 1);
				output.setText(output.getText() + "|G| = " + new EllipticCurve(a, b, p).pointOrder(G) + "\n");
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
					output.setText(output.getText() + "Please use a prime under 300 or generate a smaller number of random curves mod p.\n");
				}
				else {
					output.setText(output.getText() + "List of elliptic curves (mod " + p + "):\n" + EllipticCurve.listECs(p) + "\n");
				}
			}
		});
		
		JButton listPrimeCurves = new JButton("List ECs of prime order");
		listPrimeCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(p >= 300) {
					output.setText(output.getText() + "Please use a prime under 300 or generate a smaller number of random curves mod p.\n");
				}
				else {
					output.setText(output.getText() + "List of elliptic curves (mod " + p + ") of prime order:\n" + EllipticCurve.listPrimeECs(p) + "\n");
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
				output.setText(output.getText() + "List of " + n + " random elliptic curves (mod " + p + "):\n" + EllipticCurve.listRandomECs(n, p) + "\n");
			}
		});

		JButton randListPrimeCurves = new JButton("Random ECs of prime order");
		randListPrimeCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + "List of " + n + " random elliptic curves (mod " + p + ") of prime order:\n" + EllipticCurve.listRandomPrimeECs(n, p) + "\n");
			}
		});
		
		randListPan.add(new JLabel("Generate "));
		final JTextField _n = new JTextField("" + n + "        ");
		_n.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					n = Integer.parseInt(_n.getText().trim());
					output.setText(output.getText() + "Random curve selection will now generate " + n + " curves.\n");
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_n.setText("" + n);
				}
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
				output.setText(output.getText() + "List of points on E(F_" + p + "):\n" + new EllipticCurve(a, b, p).listPoints() + "\n");
			}
		});
		
		JButton listGMults = new JButton("List multiples of G");
		listGMults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + "List of multiples of G on E(F_" + p + "):\n" + new EllipticCurve(a, b, p).listGmults(new Point(Gx, Gy, 1)) + "\n");
			}
		});		
		
		pointsPan.add(listPoints);
		pointsPan.add(listGMults);
		
		JPanel clearPan = new JPanel();
		clearPan.setLayout(new FlowLayout());
		JButton clear = new JButton("Clear Output");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText("");
			}
		});	
		
		clearPan.add(clear);
		
		add(ecInfoPan);
		add(ptsInfoPan);
		add(arithmeticPan);
		add(orderPan);
		add(listPan);
		add(randListPan);
		add(pointsPan);
		add(outputPan);
		add(clearPan);
	}
}