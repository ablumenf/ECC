package src;

/**
 * @author Aaron Blumenfeld
 * The following program implements point addition and multiplication
 * for elliptic curves. Projective coordinates are used, not affine. 
 * Therefore, to understand the points, (0 : 1 : 0) represents the point
 * at infinity; otherwise, the z-coordinate is 1, and (x : y : 1)
 * represents (x, y) in affine coordinates. But the toString method prints
 * either (x, y) or the string "infinity".
 */

public class Point {
    private long x;
    private long y;
    private long z;
    
    public Point() { // default constructor gives the point at infinity
    	x = 0;
    	y = 1;
    	z = 0;
    }
   
    public Point(long a, long b, long c) {
        x = a;
        y = b;
        z = c;
    }
   
    public Point(Point P) {
        x = P.getX();
        y = P.getY();
        z = P.getZ();
    }
    
    public long getX() {
    	return x;
    }
    
    public long getY() {
    	return y;
    }
    
    public long getZ() {
    	return z;
    }
    
    public void setX(long x) {
    	this.x = x;
    }
    
    public void setY(long y) {
    	this.y = y;
    }
    
    public void setZ(long z) {
    	this.z = z;
    }

    public boolean equals(Point Q) {
    	return this.x == Q.getX() && this.y == Q.getY() && this.z == Q.getZ();
    }

    public String toString() {
    	if(this.equals(new Point())) {
    		return "infinity";
    	}
    	return "(" + x + ", " + y + ")";
    }
    
    public Point addHelper(Point Q, long a, long b, long p) { /* uses projective coordinates formulas from Washington's Elliptic Curves text.
    this method separate so mult doesn't invert the z-coordinate O(logk) times */
    	long x3, y3, z3;
    	Point infinity = new Point();
    	if(!equals(Q) && !equals(new Point(Q.getX(), Math.floorMod(-Q.getY(), p), Q.getZ()))) { /* if P != +-Q */
    		long u = Math.floorMod(Q.getY()*getZ(), p) - Math.floorMod(getY()*Q.getZ(), p);
    		long v = Math.floorMod(Q.getX()*getZ(), p) - Math.floorMod(getX()*Q.getZ(), p);
    		long w = Math.floorMod(Math.floorMod(u*u, p)*Math.floorMod(getZ()*Q.getZ(), p), p) - Math.floorMod(v*v*v, p) - Math.floorMod(Math.floorMod(2*v*v, p), p)*Math.floorMod(getX()*Q.getZ(), p);
    		x3 = Math.floorMod(v*w, p);
    		y3 = Math.floorMod(u*(Math.floorMod(v*v, p)*Math.floorMod(getX()*Q.getZ(), p) - w), p) - Math.floorMod(Math.floorMod(v*v*v, p)*Math.floorMod(getY()*Q.getZ(), p), p);
    		z3 = Math.floorMod(Math.floorMod(v*v*v, p)*Math.floorMod(getZ()*Q.getZ(), p), p);
    	}
    	else if(equals(Q)) { /* if P = Q */
    		long t = Math.floorMod(a*getZ()*getZ(), p) + Math.floorMod(3*getX()*getX(), p);
    		long u = Math.floorMod(getY()*getZ(), p);
    		long v = Math.floorMod(u*getX()*getY(), p);
    		long w = Math.floorMod(t*t, p) - Math.floorMod(8*v, p);
    		x3 = Math.floorMod(2*u*w, p);
    		y3 = Math.floorMod(t*(4*v - w), p) - Math.floorMod(8*getY()*getY()*u*u, p);
    		z3 = Math.floorMod(8*u*u*u, p);
    	}
    	else { /* if P = -Q ==> P + Q = infinity */
    		x3 = 0;
    		y3 = 1;
    		z3 = 0;
    	}
    	if(equals(infinity)) { /* if P = infinity ==> P + Q = Q */
    		x3 = Q.getX();
    		y3 = Q.getY();
    		z3 = Q.getZ();
    	}
    	if(Q.equals(infinity)) { /* if Q = infinity ==> P + Q = P */
    		x3 = getX();
    		y3 = getY();
    		z3 = getZ();
    	}
    	return new Point(x3, y3, z3);
    }
    
    public Point add(Point Q, long a, long b, long p) { /* call addhelper and
	    reduce the coordinates at the end, since the result is an equivalence
	         class, so it must be scaled back down to reduced coordinates */
    	Point R = addHelper(Q, a, b, p);
    	if(Math.floorMod(R.getZ(), p) == 0) {
    		R.setY(1); /* scale infinity down to unique (0, 1, 0) */
    	}
    	else {
    		long inv = ECMath.inverse(R.getZ(), p);
    		R.setX(R.getX() * inv);
    		R.setY(R.getY() * inv); /* scale z coordinate back down to 1 */
    		R.setZ(R.getZ() * inv);
    	}
    	R.setX(Math.floorMod(R.getX(), p));
    	R.setY(Math.floorMod(R.getY(), p)); /* reduce coordinates mod p */
    	R.setZ(Math.floorMod(R.getZ(), p));
    	return R;
    }
    
    public Point mult(long k, long a, long b, long p) { /* compute kP using repeated doubling */
    	long A = k;
    	Point B = new Point(0, 1, 0);
    	Point C = new Point(this);
    	while(A > 0) {
    		if(A % 2 == 0) { /* if A is even */
    			A /= 2;
    			C = C.addHelper(C, a, b, p);
    			C.setX(Math.floorMod(C.getX(), p));
    			C.setY(Math.floorMod(C.getY(), p)); /* have to reduce mod p O(logk) times to prevent overflow */
    			C.setZ(Math.floorMod(C.getZ(), p));
    		}
    		else { /* if A is odd */
    			A -= 1;
    			B = B.addHelper(C, a, b, p);
    			B.setX(Math.floorMod(B.getX(), p));
    			B.setY(Math.floorMod(B.getY(), p)); /* have to reduce mod p O(logk) times to prevent overflow */
    			B.setZ(Math.floorMod(B.getZ(), p));
    		}
    	}
    	if(Math.floorMod(B.getZ(), p) == 0)
    		B.setY(1); /* scale infinity down to unique (0, 1, 0) */
    	else {
    		long inv = ECMath.inverse(B.getZ(), p);
    		B.setX(B.getX() * inv);
    		B.setY(B.getY() * inv); /* scale z coordinate back down to 1 */
    		B.setZ(B.getZ() * inv);
    	}
    	B.setX(Math.floorMod(B.getX(), p));
		B.setY(Math.floorMod(B.getY(), p)); /* reduce coordinates mod p */
		B.setZ(Math.floorMod(B.getZ(), p));
    	return B;
    }
}