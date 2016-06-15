/**
 * @author Aaron Blumenfeld
 * This is an implementation of a polynomial mod 2. I used hashing
 * instead of arrays for possible sparse polynomials. Though it wouldn't
 * have mattered much since the program only uses up to degree 10 polynomials.
 * Though you could reuse the code for much larger-degree polynomials. HashSets
 * are used instead of HashMaps since we're working mod 2: if a degree is in the
 * underlying data structure, the only possible value would be 1.
 * 
 * You can enter polynomials in terms of whatever variable you want. a^5 + 1 will work,
 * as will z^2 + z + 1. But z^5 + a^2 + x + 1 will be intepreted as z^5 + z^2 + z + 1.
 * There is no support for multivariable polynomials. Even if you enter a^5 + 1, it will
 * print (via toString()) as z^5 + 1. Probably less confusing to just input polynomials
 * using powers of z.
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
		s = s.replaceAll("\\s+", ""); // remove spaces
		if(!s.isEmpty()) {
			int i = 0;
			while(i < s.length()) {
				char current = s.charAt(i);
				if(current >= 'a' && current <= 'z') {
					if(s.length() >= i+2 && s.charAt(i+1) == '^' && s.charAt(i+2) >= '0' && s.charAt(i+2) <= '9') {
						//poly.add(Integer.parseInt("" + s.charAt(i+2))); // add z^i
						String degree;
						int idx = s.indexOf('+', i+2);
						if(idx != -1) {
							degree = s.substring(i+2, idx); // take care of degrees with >= 2 digits
						} else {
							degree = s.substring(i+2, s.length()); // for stuff like z^10 without any terms afterward
						}
						poly.add(Integer.parseInt(degree));
					}
					if(s.length() >= i+2 && s.charAt(i+1) == '+') { // if we see z + ...
						poly.add(1);
					}
					if(i == s.length()-1) { // add z = z^1
						poly.add(1);
					}
				}
				if(current == '1' && i > 0 && s.charAt(i-1) == '+') { // if we see ... + 1
					poly.add(0);
				}
				if(current == '1' && i == s.length()-1) { // if we see 1
					poly.add(0);
				}
				i++;
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
		Polynomial rval = new Polynomial("z^0");
		while(k > 0) {
			if((k & 1) == 1) /* if k is odd */
				rval = rval.mult(temp).mod(q); /* rval = rval * temp (mod q) */
			k >>= 1;
			temp = temp.mult(temp).mod(q); /* temp = temp^2 (mod q) */
		}
		return rval;
	}
	
	public Polynomial inverse(Polynomial q) { /* p^(ord-1) = 1 by Lagrange's Theorem, so p^(ord-2) = p^(-1) */
		return modExp((1<<q.degree()) - 2, q);
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
	
	public static void main(String[] args) {
		Polynomial p = new Polynomial("z^4 + z^3 + z^2 + z");
		System.out.println(p);
		Polynomial q = new Polynomial("z^2 + z + 1");
		System.out.println(q);
		System.out.println(p.add(q));
		System.out.println(q.multHelper(2));
		System.out.println(p.mult(q));
		System.out.println(p.mod(q) + "\n");
		
		System.out.println("random " + random(5));
		System.out.println(new Polynomial("z^2").inverse(new Polynomial("z^4 + z + 1")));
		
		p = new Polynomial("z^3");
		System.out.println(p);
		
		p = new Polynomial("1");
		System.out.println(p);
		
		p = new Polynomial("z^10 + a^5 + 1");
		System.out.println(p.equals(new Polynomial()));
		System.out.println(new Polynomial().equals(new Polynomial()));
	}
}