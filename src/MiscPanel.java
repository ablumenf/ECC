import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;

public class MiscPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private long a = 17;
	private long b = 45;
	private long prime = 83;
	private Polynomial p = new Polynomial("z^3 + z^2 + z + 1");
	private Polynomial q = new Polynomial("z^3 + z^2");
	private Polynomial r = new Polynomial("z^4 + z + 1");
	private long k = 23;
	private int modChanges = -1;

	public MiscPanel() throws IOException {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel outputPan = new JPanel();
		outputPan.setLayout(new FlowLayout());
		
		JTextArea output = new JTextArea ("Enter polynomials as powers of z. For example, enter z^3 + z + 1.\nThe polynomial functions work mod 2.\n\n", 12, 42);
		output.setLineWrap(true);
		output.setWrapStyleWord(false);
		JScrollPane miscScroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		outputPan.add(miscScroll);
		
		JPanel modularInputPan = new JPanel();
		modularInputPan.setLayout(new FlowLayout());
		
		final JTextField _a = new JTextField("" + a + "        ");
		_a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					a = Math.floorMod(Long.parseLong(_a.getText().trim()), prime);
					output.setText(output.getText() + "a = " + a + "\n");
					_a.setText("" + a);
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
					b = Math.floorMod(Long.parseLong(_b.getText().trim()), prime);
					output.setText(output.getText() + "b = " + b + "\n");
					_b.setText("" + b);
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_b.setText("" + b);
				}
			}
		});
		
		final JTextField _prime = new JTextField("" + prime + "        ");
		_prime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					long temp = Long.parseLong(_prime.getText().trim());
					if(ECMath.isPrime(temp)) {
						prime = temp;
						output.setText(output.getText() + "p = " + prime + "\n");
					} else {
						output.setText(output.getText() + temp + " is not prime. Please try again.\n");
					}
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_prime.setText("" + prime);
				}
			}
		});
		
		JButton randPrime = new JButton("Generate Random Prime");
		randPrime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Random rand = new Random();
				int numDigits = Math.floorMod(rand.nextInt(), 4) + 1;
				prime  = ECMath.randomPrime(numDigits); // prime with <= 4 digits
				_prime.setText("" + prime);
				output.setText(output.getText() + "p = " + prime + "\n");
			}
		});
		
		modularInputPan.add(new JLabel("a = "));
		modularInputPan.add(_a);
		modularInputPan.add(new JLabel("b = "));
		modularInputPan.add(_b);
		modularInputPan.add(new JLabel("p = "));
		modularInputPan.add(_prime);
		modularInputPan.add(randPrime);
		
		JPanel modularPan = new JPanel();
		modularPan.setLayout(new FlowLayout());
		
		JButton modExp = new JButton("a^b (mod p)");
		modExp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + a + "^" + b + " (mod " + prime + ") = " + ECMath.modExp(a, b, prime) + "\n");
			}
		});	
		
		JButton inverse = new JButton("1/a (mod p)");
		inverse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long rval = ECMath.inverse(a, prime);
				if(rval != -1) {
					output.setText(output.getText() + "1/" + a + " (mod " + prime + ") = " + rval + "\n");
				} else {
					output.setText(output.getText() + "1/" + a + " (mod " + prime + ") = undefined\n");
				}
			}
		});
		
		JButton sqrt = new JButton("sqrt(a) (mod p)");
		sqrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ECMath.jacobi(a, prime) != -1) {
					output.setText(output.getText() + "sqrt(" + a + ") (mod " + prime + ") = " + ECMath.sqrt(a,  prime) + "\n");
				} else {
					output.setText(output.getText() + a + " is not a square (mod " + prime + ")\n");
				}
			}
		});
		
		modularPan.add(modExp);
		modularPan.add(inverse);
		modularPan.add(sqrt);
		
		JPanel polyInputPan1 = new JPanel();
		polyInputPan1.setLayout(new FlowLayout());
		
		final JTextField _p = new JTextField("" + p + "        ");
		_p.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = _p.getText().trim();
				if(Polynomial.isValid(s)) {
					try {
						p = new Polynomial(s).mod(r);
						output.setText(output.getText() + "P(z) = " + p + "\n");
						_p.setText("" + p);
					} catch(NumberFormatException e1) {
						output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
						_p.setText("" + p);
					}
					
				} else {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_p.setText("" + p);
				}
			}
		});
		
		final JTextField _q = new JTextField("" + q + "        ");
		_q.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = _q.getText().trim();
				if(Polynomial.isValid(s)) {
					try {
						q = new Polynomial(s).mod(r);
						output.setText(output.getText() + "Q(z) = " + q + "\n");
						_q.setText("" + q);
					} catch(NumberFormatException e1) {
						output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
						_q.setText("" + q);
					}
				} else {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
					_q.setText("" + q);
				}
			}
		});
		
		final JTextField _k = new JTextField("" + k + "        ");
		_k.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					k = Long.parseLong(_k.getText().trim());
					output.setText(output.getText() + "k = " + k + "\n");
					_k.setText("" + k);
				} catch(NumberFormatException e1) {
					output.setText(output.getText() + "There was an error parsing your input. Please try again.\n");
				}
			}
		});
		
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
				r = (Polynomial)poly.getSelectedItem();
				if(modChanges > 0) {
					output.setText(output.getText() + "The irreducible polynomial is now set to " + r + ".\n");
				}
			}
		});
		poly.setSelectedItem(irred[5]); // default is z^4 + z + 1
		((JLabel)poly.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		polyInputPan1.add(new JLabel("P(z) = "));
		polyInputPan1.add(_p);
		polyInputPan1.add(new JLabel("Q(z) = "));
		polyInputPan1.add(_q);
		polyInputPan1.add(new JLabel("k = "));
		polyInputPan1.add(_k);
		polyInputPan1.add(poly);
		
		JPanel polyInputPan2 = new JPanel();
		polyInputPan2.setLayout(new FlowLayout());
		polyInputPan2.add(new JLabel("R(z) = "));
		polyInputPan2.add(poly);
		
		JPanel polyPan1 = new JPanel();
		JPanel polyPan2 = new JPanel();
		polyPan1.setLayout(new FlowLayout());
		polyPan2.setLayout(new FlowLayout());
		
		JButton add = new JButton("P(z) + Q(z) (mod R(z))");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + "(" + p + ") + (" + q + ") = (" + p.add(q).mod(r) + ") (mod " + r + ")\n");
			}
		});
		
		JButton mult = new JButton("P(z) * Q(z) (mod R(z))");
		mult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + "(" + p + ") * (" + q + ") = (" + p.mult(q).mod(r) + ") (mod " + r + ")\n");
			}
		});
		
		JButton polyModExp = new JButton("P(z)^k (mod R(z))");
		polyModExp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText(output.getText() + "(" + p + ")^" + k + " = (" + p.modExp(k, r) + ") (mod " + r + ")\n");
			}
		});
		
		JButton polyInverse = new JButton("1/P(z) (mod R(z))");
		polyInverse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Polynomial rval = p.inverse(r);
				if(rval != null) {
					output.setText(output.getText() + "1/(" + p + ") = (" + p.inverse(r) + ") (mod " + r + ")\n");
				} else {
					output.setText(output.getText() + "1/(" + p + ") = undefined (mod " + r + ")\n");
				}
			}
		});
		
		JButton polySqrt = new JButton("sqrt(P(z)) (mod R(z))");
		polySqrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Polynomial rval = p.sqrt(r);
				output.setText(output.getText() + "sqrt(" + p + ") = (" + rval + ") (mod " + r + ")\n");
			}
		});
		
		polyPan1.add(add);
		polyPan1.add(mult);
		polyPan2.add(polyModExp);
		polyPan2.add(polyInverse);
		polyPan2.add(polySqrt);
		
		JPanel clearPan = new JPanel();
		clearPan.setLayout(new FlowLayout());
		JButton clear = new JButton("Clear Output");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setText("Enter polynomials as powers of z. For example, enter z^3 + z + 1.\nThe polynomial functions work mod 2.\n\n");
			}
		});	
		
		clearPan.add(clear);
		
		add(modularInputPan);
		add(modularPan);
		add(polyInputPan1);
		add(polyInputPan2);
		add(polyPan1);
		add(polyPan2);
		add(outputPan);
		add(clearPan);
	}
}