package src;

/**
 * @author Aaron Blumenfeld
 * This class implements various static methods for math
 * operations used throughout the ECC Toolkit. This
 * includes modular exponentiation, inverses, square roots,
 * Jacobi (Legendre) symbols, primality testing, and factoring.
 * 
 * The factoring method just uses brute force. I considered
 * using something like Pollard's Rho algorithm for finding
 * prime factors, but that would require creating a list of
 * all factors from a list of prime factors. This could be done,
 * but it's overkill for dealing with relatively small groups.
 * (It's also possible to get stuck in a cycle that doesn't lead
 * to a nontrivial factor when using Pollard's Rho algorithm.)
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ECMath {
	
	public static long modExp(long a, long b, long p) {
		long rval = 1;
		while(b > 0) {
			if((b & 1) == 1) /* if b is odd */
				rval = (rval * a) % p;
			b >>= 1;
			a = (a * a) % p;
		}
		return rval;
	}
	
	/* lazy way to compute a^(-1) (mod p) */
	public static long inverse(long a, long p) { /* efficient enough since the numbers are pretty small */
		if(a == 0) {
			return -1;
		}
		return modExp(a, p-2, p);
	}
	
	public static long sqrt(long a, long p) { /* Shanks' algorithm for sqrt(a) (mod p) */
		if((a % p) == 0) /* sqrt(0) = 0 */
			return 0;
		if((p % 4) == 3) /* easy for p = 3 (mod 4) */
			return modExp(a, (p+1)/4, p);
		if((p % 8) == 5 && modExp(a, (p-1)/4, p) == 1) /* sometimes easy for p = 5 (mod 8) */
			return modExp(a, (p+3)/8, p);
		long e = 0; long q = p-1;
		while(q % 2 == 0 && q > 0) {
			e++;
			q >>= 1;
		}
		long n = 2;
		while(jacobi(n, p) != -1) {
			n = (long)(Math.random() * p);
		}
		long z = modExp(n, q, p);
		long y, r, x, b, t;
		y = z;
		r = e;
		x = modExp(a, (q-1)/2, p);
		b = (a * x * x) % p;
		x = (a * x) % p;
		while(b % p != 1) {
			long m = 0;
			while(modExp(b, 1<<m, p) != 1) {
				m++;
			}
			t = modExp(y, 1<<(r-m-1), p);
			y = (t * t) % p;
			r = m;
			x = (x * t) % p;
			b = (b * y) % p;
		}
		return x;
	}
	
	public static long jacobi(long a, long p) {
		if((a % p) == 0)
			return 0;
		long rval = 1;
		long mod8;
		long temp;

		a = (a % p);
		while(a != 0) {
			while(a % 2 == 0) { /* pull out factors of 2 and compute (2/n) */
				a >>= 1;
				mod8 = (p % 8);
				if(mod8 == 3 || mod8 == 5) {
					if(rval == 1) {
						rval = -1;
					}
					else {
						rval = 1;
					}
				}
			}
			temp = a;
			a = p; /* swap a and p */
			p = temp;

			if((a % 4) == 3 && (p % 4) == 3) { /* apply quadratic reciprocity */
				if(rval == 1) {
					rval = -1;
				}
				else {
					rval = 1;
				}
			}
			a = a % p;
		}
		return rval;
	}
	
	public static boolean isPrime(long N) {
		if(N == 0 || N == 1) {
			return false;
		}
		if(N == 2) {
			return true;
		}
		if(N % 2 == 0) {
			return false;
		}
		long s = N-1;
		while(s % 2 == 0) { /* we can write N-1 = 2^k*s */
			s /= 2;
		}
		Random rand = new Random();
		for(int i = 0; i < 50; i++) { // 50 iterations has failure rate of <= 1/2^100
			long r = Math.floorMod(rand.nextLong(), N);
			long a = r % (N-1) + 1;
			long exp = s;
			long mod = modExp(a, exp, N);
			while(exp != N-1 && mod != 1 && mod != N-1) {
				mod = Math.floorMod(mod * mod, N);
				exp *= 2;
			}
			if(mod != N-1 && exp % 2 == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static long randomPrime(int n) {
		Random rand = new Random();
		long rval = 0;
		while(!isPrime(rval)) {
			rval = Math.floorMod(rand.nextLong(), (int)Math.pow(10, n));
			if(rval <= 3) { // want p >= 5
				rval = 0;
			}
		}
		return rval;
	}
	
	public static List<Long> allFactors(long N) { /* this is not optimally efficient, but this program is supposed to be
	for instructive purposes only, and only uses fields with under 10000 elements */
		List<Long> factors = new ArrayList<Long>();
		for(long i = 1; i <= N; i++) {
			if(N % i == 0) {
				factors.add(i);
			}
		}
		return factors;
	}
}