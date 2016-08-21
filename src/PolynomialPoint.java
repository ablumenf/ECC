package src;

/**
 * @author Aaron Blumenfeld
 * The following program implements point addition and multiplication
 * for elliptic curves. Lopez-Dahab (LD) projective coordinates are used, not affine. 
 * Therefore, to understand the points, (1 : 0 : 0) represents the point
 * at infinity; otherwise, the z-coordinate is 1, and (x : y : z)
 * represents (x/z, y/z^2) in affine coordinates. But the toString method prints
 * either (x, y) or the string "infinity".
 */

public class PolynomialPoint {
    private Polynomial x;
    private Polynomial y;
    private Polynomial z;
    
    public PolynomialPoint() { // default constructor gives the point at infinity
    	x = new Polynomial("1");
    	y = new Polynomial();
    	z = new Polynomial();
    }
   
    public PolynomialPoint(Polynomial x, Polynomial y, Polynomial z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
   
    public PolynomialPoint(PolynomialPoint P) {
        x = new Polynomial(P.getX());
        y = new Polynomial(P.getY());
        z = new Polynomial(P.getZ());
    }
    
    public Polynomial getX() {
    	return x;
    }
    
    public Polynomial getY() {
    	return y;
    }
    
    public Polynomial getZ() {
    	return z;
    }
    
    public void setX(Polynomial x) {
    	this.x = x;
    }
    
    public void setY(Polynomial y) {
    	this.y = y;
    }
    
    public void setZ(Polynomial z) {
    	this.z = z;
    }

    public boolean equals(PolynomialPoint Q) {
    	return this.x.equals(Q.getX()) && this.y.equals(Q.getY()) && this.z.equals(Q.getZ());
    }

    public String toString() {
    	if(this.equals(new PolynomialPoint())) {
    		return "infinity";
    	}
    	return "(" + x + ", " + y + ")";
    }
    
    /* uses LD projective coordinates formulas from Guide to Elliptic Curve Cryptography.
    this method separate so add method isn't as messy */
    public PolynomialPoint addHelper(PolynomialPoint Q, Polynomial a, Polynomial b, Polynomial modulus) {
    	PolynomialPoint infinity = new PolynomialPoint();
    	if(Q.equals(infinity)) { /* if Q = infinity ==> P + Q = P */
    		return this;
    	}
    	if(this.equals(infinity)) { /* if P = infinity ==> P + Q = Q */
    		return Q;
    	}
    	// point doubling
    	if(new PolynomialPoint(getX().mod(modulus), getY().mod(modulus), getZ().mod(modulus)).equals(Q)) {
    		Polynomial z3 = getX().mult(getX());
    		Polynomial temp = getZ().mult(getZ());
    		z3 = z3.mult(temp);
    		Polynomial x3 = getX().mult(getX());
    		x3 = x3.mult(x3);
    		temp = getZ().mult(getZ());
    		temp = temp.mult(temp);
    		temp = temp.mult(b);
    		x3 = x3.add(temp);
    		Polynomial y3 = getZ().mult(getZ());
    		y3 = y3.mult(y3);
    		y3 = b.mult(y3).mult(z3);
    		temp = a.mult(z3).add(getY().mult(getY())).add(b.mult(getZ().modExp(4, modulus)));
    		temp = x3.mult(temp);
    		y3 = y3.add(temp);
    		return new PolynomialPoint(x3, y3, z3);
    	}
    	// point addition
    	Q.setX(Q.getX().mult(Q.getZ().inverse(modulus)));
    	Polynomial temp = Q.getZ().mult(Q.getZ());
    	temp = temp.inverse(modulus);
    	Q.setY(Q.getY().mult(temp));
    	Q.setZ(new Polynomial("1"));
    	Polynomial A = Q.getY().mult(getZ().mult(getZ()));
    	A = A.add(getY());
    	Polynomial B = Q.getX().mult(getZ());
    	B = B.add(getX());
    	Polynomial C = getZ().mult(B);
    	temp = a.mult(getZ()).mult(getZ());
    	temp = C.add(temp);
    	Polynomial D = B.mult(B).mult(temp);
    	Polynomial z3 = C.mult(C);
    	Polynomial E = A.mult(C);
    	Polynomial x3 = A.mult(A);
    	x3 = x3.add(D).add(E);
    	Polynomial F = Q.getX().mult(z3);
    	F = x3.add(F);
    	Polynomial G = Q.getX().add(Q.getY());
    	G = G.mult(z3).mult(z3);
    	Polynomial y3 = E.add(z3);
    	y3 = y3.mult(F);
    	y3 = y3.add(G);
    	return new PolynomialPoint(x3, y3, z3);
    }
    
    public PolynomialPoint add(PolynomialPoint Q, Polynomial a, Polynomial b, Polynomial modulus) {
    	PolynomialPoint rval = this.addHelper(Q, a, b, modulus);
    	Polynomial temp = rval.getZ().mod(modulus);
    	if(!temp.equals(new Polynomial())) { // z != 0
    		rval.setX(rval.getX().mult(temp.inverse(modulus)));
    		rval.setX(rval.getX().mod(modulus));
    		temp = temp.mult(temp);
    		rval.setY(rval.getY().mult(temp.inverse(modulus)));
    		rval.setY(rval.getY().mod(modulus));
    		rval.setZ(new Polynomial("1"));
    	} else { // z = 0
    		rval.setX(new Polynomial("1"));
    		rval.setY(new Polynomial());
    	}
    	return rval;
    }
    
    public PolynomialPoint mult(long k, Polynomial a, Polynomial b, Polynomial modulus) { /* compute kP using repeated doubling */
    	long A = k;
    	PolynomialPoint B = new PolynomialPoint();
    	PolynomialPoint C = new PolynomialPoint(this);
    	while(A > 0) {
    		if(A % 2 == 0) { /* if A is even */
    			A /= 2;
    			C = C.add(C, a, b, modulus);
    			C.setX(C.getX().mod(modulus));
    			C.setY(C.getY().mod(modulus)); /* reduce to prevent huge-degree polynomials */
    			C.setZ(C.getZ().mod(modulus));
    		} else { /* if A is odd */
    			A -= 1;
    			B = B.add(C, a, b, modulus);
    			B.setX(B.getX().mod(modulus));
    			B.setY(B.getY().mod(modulus)); /* reduce to prevent huge-degree polynomials */
    			B.setZ(B.getZ().mod(modulus));
    		}
    	}
    	return B;
    }
}