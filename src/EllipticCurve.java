/**
 * @author Aaron Blumenfeld
 * The following program implements elliptic curves. Elliptic
 * curves are of the form y^2 = x^3 + ax + b.  They have
 * no repeated roots on the right side of the equation, which is
 * equivalent to 4a^3 + 27b^2 != 0 (mod p).  The isEC method
 * tests if a given equation is an elliptic curve or not.
 * Point and curve orders, logarithms, and lists of points are
 * implemented. There are also static methods for generating
 * lists of elliptic curves.
 * 
 * The logarithm calculator just uses brute force. I considered
 * using something like Pollard's Rho algorithm, but that would require
 * inverting numbers modulo n (or possibly modulo n/k for some k > 1 and
 * checking the k possibilities modulo n). This could be done, but
 * it's overkill for dealing with relatively small groups.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class EllipticCurve {

    private long a;
    private long b;
    private long p;
   
    public EllipticCurve(long a, long b, long p) {
        this.a = a;
        this.b = b;
        this.p = p;
    }
   
    public EllipticCurve(EllipticCurve E) {
        this.a = E.getA();
        this.b = E.getB();
        this.p = E.getP();
    }
    
    public long getA() {
    	return a;
    }
    
    public long getB() {
    	return b;
    }
    
    public long getP() {
    	return p;
    }
    
    public void setA(long a) {
    	this.a = a;
    }
    
    public void setB(long b) {
    	this.b = b;
    }
    
    public void setP(long p) {
    	this.p = p;
    }
   
	public String toString() {
		String t = "E(F_" + p + ") : "; 
		String s = "y^2 = x^3 + ";
		if(a == 0) {
		}
		else if(a == 1) {
			s += "x + ";
		}
		else {
			s += a + "x + ";
		}
		s += b;
		if(b == 0) {
			if(a == 0) {
				s = s.substring(0, s.indexOf('3') + 1);
			}
			else {
				s = s.substring(0, s.indexOf('x', 8) + 1);
			}
		}
		return t + s;
	}
	
	public boolean isEC() {
		long a = getA();
		long b = getB();
		return Math.floorMod(4*a*a*a + 27*b*b, getP()) != 0; /* make sure no multiple roots */
	}
	
	public long order() { /* O(plogp) algorithm */
		long order = getP() + 1;
		for(long x = 0; x < getP(); x++) {
			long jac = ECMath.jacobi(x*x*x + getA()*x + getB(), getP());
			order += jac;
		}
		return order;
	}
	
	public long pointOrder(Point G) {
		List<Long> factors = ECMath.allFactors(order());
		for(long l : factors) {
			if(G.mult(l, getA(), getB(), getP()).equals(new Point())) {
				System.out.println(l);
				return l;
			}
		}
		return -1; /* error */
	}
	
	public long log(Point P, Point G) { /* return k, where kG = P */
		long N = pointOrder(G);
		Point B = new Point(); // B = infinity
		for(long i = 1; i <= N; i++) {
			if((B = G.add(B, getA(), getB(), getP())).equals(P)) {
				return i;
			}
		}
		return -1; /* error */
	}
	
	public String listPoints() {
		String s = "";
		for(long x = 0; x < getP(); x++) {
			long temp = (x*x*x + getA()*x + getB()) % getP();
			if(ECMath.jacobi(temp, getP()) == 1) {
				Point P = new Point(x, ECMath.sqrt(temp, getP()), 1);
				if(P.getY() > getP() - P.getY()) {
					P.setY(getP() - P.getY());
				}
				Point Q = new Point(P.getX(), getP() - P.getY(), 1);
				s += P + ", " + Q + ", ";
			}
		}
		s += new Point() + "\n";
		return s;
	}
	
	public String listGmults(Point G) {
		String s = "";
		long N = pointOrder(G);
		Point temp = new Point(G);
		for(long i = 1; i < N; i++) {
			s += temp + ", ";
			temp = temp.add(G, getA(), getB(), getP());
		}
		s += temp + "\n";
		return s;
	}
	
	public static String listECs(long p) {
		int count = 0;
		String s = "";
		for(long a = 0; a < p; a++) {
			for(long b = 0; b < p; b++) {
				EllipticCurve E = new EllipticCurve(a, b, p);
				if(E.isEC()) {
					s += E + ", |E| = " + E.order() + "\n";
					count++;
				}
			}
		}
		s += count + " curves were generated.\n";
		return s;
	}
	
	public static String listPrimeECs(long p) {
		int count = 0;
		String s = "";
		List<EllipticCurve> curves = new ArrayList<EllipticCurve>();
		for(long a = 0; a < p; a++) {
			for(long b = 1; b < p; b++) { /* start b at 1 since y^2 = x^3 + ax has an even number of points */
				EllipticCurve E = new EllipticCurve(a, b, p);
				long order = E.order();
				if(E.isEC() && ECMath.isPrime(order)) {
					curves.add(E);
				}
			}
		}
		Collections.sort(curves, new EllipticCurveComparator());
		for(EllipticCurve E : curves) {
			s += E + ", |E| = " + E.order() + "\n";
			count++;
		}
		s += count + " curves were generated.\n";
		return s;
	}
	
	public static String listRandomECs(int n, long p) {
		int count = 0;
		String s = "";
		Random rand = new Random();
		long a, b;
		EllipticCurve E;
		while(n > 0) {
			a = Math.floorMod(rand.nextLong(), p);
			b = Math.floorMod(rand.nextLong(), p);
			E = new EllipticCurve(a, b, p);
			if(E.isEC()) {
				s += E + ", |E| = " + E.order() + "\n";
				n--;
				count++;
			}
		}
		s += count + " curves were generated.\n";
		return s;
	}
	
	public static String listRandomPrimeECs(int n, long p) {
		int count = 0;
		String s = "";
		Random rand = new Random();
		long a, b;
		EllipticCurve E;
		while(n > 0) {
			a = Math.floorMod(rand.nextLong(), p);
			b = Math.floorMod(rand.nextLong(), p);
			E = new EllipticCurve(a, b, p);
			if(E.isEC() && ECMath.isPrime(E.order())) {
				s += E + ", |E| = " + E.order() + "\n";
				n--;
				count++;
			}
		}
		s += count + " curves were generated.\n";
		return s;
	}
	
	public static void main(String[] args) { /* method for testing */
		EllipticCurve E = new EllipticCurve(2, 2, 17);
		System.out.println(E);
		System.out.println(E.isEC());
		Point G = new Point(5, 1, 1);
		Point P = new Point(0, 6, 1);
		System.out.println(E.log(P, G));
		System.out.println(E.listGmults(G));
		System.out.println(E.listPoints());
		System.out.println(listECs(17));
		System.out.println(listPrimeECs(47));
		System.out.println(listRandomPrimeECs(100, 997));
	}
}

class EllipticCurveComparator implements Comparator<EllipticCurve> {
	@Override
	public int compare(EllipticCurve E, EllipticCurve F) {
		if(E.order() > F.order()) {
			return 1;
		}
		if(E.order() < F.order()) {
			return -1;
		}
		return 0;
	}
}