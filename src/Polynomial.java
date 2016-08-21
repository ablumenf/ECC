package src;

/**
 * @author Aaron Blumenfeld
 * This is an implementation of a polynomial mod 2. I used hashing
 * instead of arrays for possible sparse polynomials. Though it wouldn't
 * have mattered much since the program only uses up to degree 10 polynomials.
 * Though you could reuse the code for much larger-degree polynomials. HashSets
 * are used instead of HashMaps since we're working mod 2: if a degree is in the
 * underlying data structure, the only possible value would be 1.
 * 
 * You should enter polynomials in terms of z. E.g., z^2 + z + 1. Use lower case z.
 * You can also enter coefficients in front of the z. E.g., 3z. But these coefficients
 * will get reduced mod 2.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Polynomial {
	private HashSet<Integer> poly;
	
	public Polynomial() {
		poly = new HashSet<Integer>(); // default constructor gives the 0 polynomial
	}
	
	public Polynomial(String s) {
		poly = new HashSet<Integer>();
		s = s.replaceAll(" ", ""); // remove spaces
		s = s.replaceAll("\\+", " "); // change +'s to spaces because of regex issues
		String[] split = s.split(" ");
		for(int i = 0; i < split.length; i++) { // split polynomial into monomials
			int val = parseMonomial(split[i]);
			if(val != -1) {
				if(!poly.contains(val)) {
					poly.add(val);
				} else {
					poly.remove(val);
				}
			}
		}
	}
	
	public Polynomial(Polynomial q) {
		poly = new HashSet<Integer>();
		for(int i : q.poly) {
			poly.add(i);
		}
	}
	
	public int degree() {
		int max = -1;
		for(int i : poly) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}
	
	public boolean equals(Polynomial q) {
		int pTerms = 0;
		for(int i : poly) {
			pTerms++;
		}
		int qTerms = 0;
		for(int i : q.poly) {
			qTerms++;
		}
		if(pTerms != qTerms) {
			return false;
		}
		for(int i : poly) {
			if(!q.poly.contains(i)) {
				return false;
			}
		}
		return true;
	}
	
	public Polynomial add(Polynomial q) {
		Polynomial rval = new Polynomial();
		Iterator<Integer> itr = poly.iterator();
		while(itr.hasNext()) { // loop through terms in p polynomial
			int temp = itr.next();
			if(!q.poly.contains(temp)) {
				rval.poly.add(temp);
			}
		}
		itr = q.poly.iterator();
		while(itr.hasNext()) { // loop through terms in q polynomial
			int temp = itr.next();
			if(!poly.contains(temp)) {
				rval.poly.add(temp);
			}
		}
		return rval;
	}
	
	public Polynomial multHelper(int i) { // computes p -> z^i*p
		Polynomial rval = new Polynomial();
		for(int j : poly) {
			rval.poly.add(i+j);
		}
		return rval;
	}

	public Polynomial mult(Polynomial q) {
		Polynomial rval = new Polynomial();
		for(int i : poly) { // applies distributive law
			rval = rval.add(q.multHelper(i));
		}
		return rval;
	}
	
	public Polynomial mod(Polynomial q) {
		Polynomial rval = new Polynomial(this);
		int pmax = rval.degree();
		int qmax = q.degree();
		
		while(pmax >= qmax) {
			rval = rval.add(q.multHelper(pmax-qmax));
			pmax = rval.degree();
		}
		return rval;
	}
	
	public Polynomial modExp(long k, Polynomial q) {
		Polynomial temp = new Polynomial(this);
		Polynomial rval = new Polynomial("1");
		while(k > 0) {
			if((k & 1) == 1) /* if k is odd */
				rval = rval.mult(temp).mod(q); /* rval = rval * temp (mod q) */
			k >>= 1;
			temp = temp.mult(temp).mod(q); /* temp = temp^2 (mod q) */
		}
		return rval;
	}
	
	public Polynomial inverse(Polynomial q) { /* p^(ord-1) = 1 by Lagrange's Theorem, so p^(ord-2) = p^(-1) */
		Polynomial temp = null;
		if(this.equals(new Polynomial())) {
			return temp;
		}
		return modExp((1<<q.degree()) - 2, q);
	}
	
	public Polynomial sqrt(Polynomial q) {
		int m = q.degree();
		return modExp((1<<(m-1)), q);
	}
	
	public String toString() {	
		if(poly.isEmpty()) {
			return "0";
		}
		String rval = "";
		Iterator<Integer> itr = poly.iterator();
		List<Integer> a = new ArrayList<Integer>();
		while(itr.hasNext()) {
			a.add(itr.next());
		}
		Collections.sort(a);
		Collections.reverse(a);
		
		itr = a.iterator();
		while(itr.hasNext()) {
			int i = itr.next();
			if(i > 1) {
				rval += "z^" + i + " ";
			} else if(i == 1) {
				rval += "z ";
			}
			else { // i == 0
				rval += "1 ";
			}
			if(itr.hasNext()) {
				rval += "+ ";
			}
		}
		rval = rval.trim();
		return rval;
	}
	
	public static void generatePolys(ArrayList<Polynomial> polys) {
		if(polys.isEmpty()) { // generate list (0, 1)
			polys.add(new Polynomial());
			polys.add(new Polynomial("1"));
		}
		else { // generate list of polynomials of degree <= n from list of polynomials of degree <= n-1
			ArrayList<Polynomial> l = new ArrayList<Polynomial>();
			for(int i = polys.size()/2; i < polys.size(); i++) {
				Polynomial temp = polys.get(i).multHelper(1);
				l.add(temp);
				temp = temp.add(new Polynomial("1"));
				l.add(temp);
			}
			polys.addAll(l);
		}
	}
	
	public static Polynomial random(int n) { /* returns random polynomial of max degree n */
		Polynomial p = new Polynomial();
		Random rand = new Random();
		for(int i = 0; i <= n; i++) {
			boolean b = rand.nextBoolean();
			if(b) {
				p = p.add(new Polynomial("z^" + i));
			}
		}
		return p;
	}
	
	public static boolean isValid(String s) {
		if(s.isEmpty()) {
			return true;
		}
		byte lastType = -1; // 0 corresponds to z, 1 corresponds to ^, 2 corresponds to number, 3 corresponds to +
		char c = s.charAt(0);
		if(c == 'z') {
			lastType = 0;
		} else if(c >= '0' && c <= '9') {
			lastType = 2;
		} else {
			return false;
		}
		
		for(int i = 1; i < s.length(); i++) {
			c = s.charAt(i);
			if(c != 'z' && c != '^' && c != '+' && (c < '0' || c > '9')) {
				return false;
			}
			if(c == 'z') {
				if(lastType != 2 && lastType != 3) {
					return false;
				}
				lastType = 0;
			}
			if(c == '^') {
				if(lastType != 0) {
					return false;
				}
				lastType = 1;
			}
			if(c >= '0' && c <= '9') {
				if(lastType == 0) {
					return false;
				}
				lastType = 2;
			}
			if(c == '+') {
				if(lastType != 0 && lastType != 2) {
					return false;
				}
				lastType = 3;
			}
		}
		return true;
	}
	
	public static int parseMonomial(String s) {
		if(!s.isEmpty()) {
			char c = s.charAt(0);
			if(c == 'z') {
				if(s.length() == 1) {
					return 1;
				} else {
					return Integer.parseInt(s.substring(2, s.length()));
				}
			}
			if(c >= '0' && c <= '9') {
				int zIndex = s.indexOf('z');
				if(zIndex != -1) {
					int coeff = Integer.parseInt(s.substring(0, zIndex)) % 2;
					if(s.indexOf('^') == -1) { // if we see nz
						if(coeff != 0) {
							return 1;
						}
					} else { // if we see nz^i
						if(coeff != 0) {
							return Integer.parseInt(s.substring(zIndex+2, s.length()));
						}
					}
				}
				if(zIndex == -1 && Integer.parseInt(s) % 2 == 1) {
					return 0;
				}
			}
		}
		return -1; // 0 polynomial
	}
	
	public static void main(String[] args) {
		Polynomial p = new Polynomial("z^2 + z + z + 1 + z^3 + z^2");
		System.out.println(p);
	}
}