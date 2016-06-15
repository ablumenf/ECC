import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class BinaryPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Polynomial a = new Polynomial("z^3"); /* initially E : y^2 + xy = x^3 + (z^3)x^2 + (z^3 + 1) */
	private Polynomial b = new Polynomial("z^3 + 1");
	private long m = 16; /* field is initially GF(16) */
	private Polynomial modulus = new Polynomial("z^4 + z + 1"); /* initial irreducible polynomial */
	private Polynomial Gx = new Polynomial("z^3"); /* inititally G = (z^3, 1) */
	private Polynomial Gy = new Polynomial("1");
	private Polynomial Px = new Polynomial("z^3 + z^2"); /* initially P = (z^3 + z^2, z^3 + z^2) */
	private Polynomial Py = new Polynomial("z^3 + z^2");
	private Polynomial Qx = new Polynomial("z^3 + 1"); /* initially Q = (z^3 + 1, z^3 + z^2 + z + 1) */
	private Polynomial Qy = new Polynomial("z^3 + z^2 + z + 1");
	private long k = 6; /* initially k = 6 */
	private int n = 100; /* initially n = 100 random curves */
	private int modChanges = -1;

	public BinaryPanel() throws IOException {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel outputPan = new JPanel();
		outputPan.setLayout(new FlowLayout());
		
		JTextArea output = new JTextArea("Enter polynomials as powers of z. For example, enter z^3 + z + 1.\n\n", 12, 42);
		output.setLineWrap(true);
		output.setWrapStyleWord(false);
		JScrollPane binaryScroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		outputPan.add(binaryScroll);
		
		final JTextField _a = new JTextField("" + a + "        ");
		_a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a = new Polynomial(_a.getText().trim());
				output.setText(output.getText() + new BinaryEllipticCurve(a, b, modulus) + "\n");
			}
		});
		
		final JTextField _b = new JTextField("" + b + "        ");
		_b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				b = new Polynomial(_b.getText().trim());
				output.setText(output.getText() + new BinaryEllipticCurve(a, b, modulus) + "\n");
			}
		});

		JPanel ecInfoPan = new JPanel();
		ecInfoPan.setLayout(new FlowLayout());
		JLabel ec = new JLabel("E(F_16) : y^2 + xy = x^3 + ");
		ecInfoPan.add(ec);
		ecInfoPan.add(_a);
		ecInfoPan.add(new JLabel("x^2 + "));
		ecInfoPan.add(_b);
		
		Polynomial[] irred = new Polynomial[226];

		String filename = "polys.txt";
		BufferedReader in = new BufferedReader(new FileReader(filename));
		try { /* read file */
			int i = 0;
			while (in.ready() && i < irred.length) {
				String s = in.readLine();
				s = s.substring(s.indexOf(' '), s.length()).trim();
				String t = "";
				for (int j = 0; j < s.length(); j++) {
					if (s.charAt(j) == '1') {
						t += "z^" + (s.length() - 1 - j) + " + ";
					}
				}
				t = t.substring(0, t.length() - 2).trim();
				irred[i] = new Polynomial(t);
				i++;
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			in.close();
		} /* end read file */
		JComboBox<Polynomial> poly = new JComboBox<Polynomial>(irred);
		
		poly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modChanges++;
				modulus = (Polynomial)poly.getSelectedItem();
				m = (1<<modulus.degree());
				ec.setText("E(F_" + (1<<modulus.degree()) + "): y^2 + xy = x^3 + ");
				if(modChanges > 0) {
					output.setText(output.getText() + "The irreducible polynomial is now set to " + modulus + ".\n");
				}
			}
		});
		poly.setSelectedItem(irred[5]); // default is z^4 + z + 1
		((JLabel)poly.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel polyPan = new JPanel();
		polyPan.setLayout(new FlowLayout());
		polyPan.add(new JLabel("Irreducible Polynomial:"));
		polyPan.add(poly);
		
		Polynomial one = new Polynomial("z^0");
		
		final JTextField _Gx = new JTextField("" + Gx + "        ");
		_Gx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gx = new Polynomial(_Gx.getText().trim());
				output.setText(output.getText() + "G = " + new PolynomialPoint(Gx, Gy, one) + "\n");
			}
		});
		
		final JTextField _Gy = new JTextField("" + Gy + "        ");
		_Gy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gy = new Polynomial(_Gy.getText().trim());
				output.setText(output.getText() + "G = " + new PolynomialPoint(Gx, Gy, one) + "\n");
			}
		});
		
		final JTextField _Px = new JTextField("" + Px + "        ");
		_Px.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Px = new Polynomial(_Px.getText().trim());
				output.setText(output.getText() + "P = " + new PolynomialPoint(Px, Py, one) + "\n");
			}
		});
		
		final JTextField _Py = new JTextField("" + Py + "        ");
		_Py.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Py = new Polynomial(_Py.getText().trim());
				output.setText(output.getText() + "P = " + new PolynomialPoint(Px, Py, one) + "\n");
			}
		});
		
		final JTextField _Qx = new JTextField("" + Qx + "        ");
		_Qx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Qx = new Polynomial(_Qx.getText().trim());
				output.setText(output.getText() + "Q = " + new PolynomialPoint(Qx, Qy, one) + "\n");
			}
		});
		
		final JTextField _Qy = new JTextField("" + Qy + "        ");
		_Qy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Qy = new Polynomial(_Qy.getText().trim());
				output.setText(output.getText() + "Q = " + new PolynomialPoint(Qx, Qy, one) + "\n");
			}
		});
		
		final JTextField _k = new JTextField("" + k + "        ");
		_k.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				k = Long.parseLong(_k.getText().trim());
				output.setText(output.getText() + "k = " + k + "\n");
			}
		});
		
		JPanel ptsInfoPan1 = new JPanel();
		JPanel ptsInfoPan2 = new JPanel();
		
		ptsInfoPan1.add(new JLabel("Generator G = ("));
		ptsInfoPan1.add(_Gx);
		ptsInfoPan1.add(new JLabel(", "));
		ptsInfoPan1.add(_Gy);
		ptsInfoPan1.add(new JLabel("), k = "));
		ptsInfoPan1.add(_k);
		
		ptsInfoPan2.add(new JLabel("P = ("));
		ptsInfoPan2.add(_Px);
		ptsInfoPan2.add(new JLabel(", "));
		ptsInfoPan2.add(_Py);
		ptsInfoPan2.add(new JLabel("), Q = ("));
		ptsInfoPan2.add(_Qx);
		ptsInfoPan2.add(new JLabel(", "));
		ptsInfoPan2.add(_Qy);
		ptsInfoPan2.add(new JLabel(")"));
		
		JPanel arithmeticPan = new JPanel();
		arithmeticPan.setLayout(new FlowLayout());
		JButton addition = new JButton("P + Q");
		addition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PolynomialPoint P = new PolynomialPoint(Px, Py, one);
				PolynomialPoint Q = new PolynomialPoint (Qx, Qy, one);
				PolynomialPoint result = P.add(Q, a, b, modulus);
				output.setText(output.getText() + P + " + " + Q + " = " + result + "\n");
			}
		});
		
		JButton multiplication = new JButton("kP");
		multiplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PolynomialPoint P = new PolynomialPoint(Px, Py, one);
				PolynomialPoint result = P.mult(k, a, b, modulus);
				output.setText(output.getText() + k + "" + P + " = " + result + "\n");
			}
		});
		
		JButton log = new JButton("log_G(P)");
		log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PolynomialPoint P = new PolynomialPoint(Px, Py, one);
				PolynomialPoint G = new PolynomialPoint(Gx, Gy, one);
				output.setText(output.getText() + "log_G" + P + " = " + new BinaryEllipticCurve(a, b, modulus).log(P, G) + "\n");
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
				output.setText(output.getText() + "|E| = " + new BinaryEllipticCurve(a, b, modulus).order() + "\n");
			}
		});
		
		JButton pointOrder = new JButton("Order(G)");
		pointOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PolynomialPoint G = new PolynomialPoint(Gx, Gy, one);
				output.setText(output.getText() + "|G| = " + new BinaryEllipticCurve(a, b, modulus).pointOrder(G) + "\n");
			}
		});
		
		orderPan.add(curveOrder);
		orderPan.add(pointOrder);
		
		JPanel listPan = new JPanel();
		listPan.setLayout(new FlowLayout());
		JButton listCurves = new JButton("List ECs");
		listCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(m <= 64) {
						output.setText(output.getText() + "List of elliptic curves over F_" + m + " using the irreducible polynomial " + modulus + ":\n\n" + BinaryEllipticCurve.listECs(m, modulus) + "\n");
					} else {
						output.setText(output.getText() + BinaryEllipticCurve.listECs(m, modulus) + "\n\n");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		listPan.add(listCurves);
		
		listPan.add(new JLabel("Generate "));
		final JTextField _n = new JTextField("" + n + "        ");
		_n.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				n = Integer.parseInt(_n.getText().trim());
				output.setText(output.getText() + "Random curve selection will now generate " + n + " curves.\n");
			}
		});
		
		JButton randListCurves = new JButton("Random ECs");
		randListCurves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					output.setText(output.getText() + "List of " + n + " random elliptic curves over F_" + m + " using the irreducible polynomial " + modulus + ":\n\n" + BinaryEllipticCurve.listRandomECs(m, modulus, n) + "\n");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		listPan.add(_n);
		listPan.add(randListCurves);
		
		JPanel pointsPan = new JPanel();
		pointsPan.setLayout(new FlowLayout());
		
		JButton listPoints = new JButton("List points on E");
		listPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + "List of points on E(F_" + m + "):\n" + new BinaryEllipticCurve(a, b, modulus).listPoints() + "\n");
			}
		});
		
		JButton listGMults = new JButton("List multiples of G");
		listGMults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + "List of multiples of G on E(F_" + m + "):\n" + new BinaryEllipticCurve(a, b, modulus).listGmults(new PolynomialPoint(Gx, Gy, one)) + "\n");
			}
		});		
		
		pointsPan.add(listPoints);
		pointsPan.add(listGMults);
		
		JPanel clearPan = new JPanel();
		clearPan.setLayout(new FlowLayout());
		JButton clear = new JButton("Clear Output");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText("Enter polynomials as powers of z. For example, enter z^3 + z + 1.\n\n");
			}
		});	
		
		clearPan.add(clear);
		
		add(ecInfoPan);
		add(polyPan);
		add(ptsInfoPan1);
		add(ptsInfoPan2);
		add(arithmeticPan);
		add(orderPan);
		add(listPan);
		add(pointsPan);
		add(outputPan);
		add(clearPan);
	}
}