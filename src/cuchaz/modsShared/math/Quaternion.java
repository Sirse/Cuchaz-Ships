/*******************************************************************************
 * Copyright (c) 2013 Jeff Martin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jeff Martin - initial API and implementation
 ******************************************************************************/
package cuchaz.modsShared.math;

public class Quaternion {
	/**************************
	 *   Definitions
	 **************************/
	
	public static final int Dimension = 4;
	
	
	/**************************
	 *   Fields
	 **************************/
	
	public double a; // w
	public double b; // x
	public double c; // y
	public double d; // z
	
	
	/**************************
	 *   Constructors
	 **************************/
	
	public Quaternion() {
		set(1.0, 0.0, 0.0, 0.0);
	}
	
	public Quaternion(double a, double b, double c, double d) {
		set(a, b, c, d);
	}
	
	public Quaternion(Vector3 v) {
		set(v);
	}
	
	public Quaternion(Quaternion other) {
		set(other);
	}
	
	
	/**************************
	 *   Accessors
	 **************************/
	
	public double get(int i) {
		switch (i) {
			case 0: return a;
			case 1: return b;
			case 2: return c;
			case 3: return d;
		}
		
		assert (false) : "Invalid index: " + i;
		
		// just to make the compiler happy
		return Double.NaN;
	}

	public void set(int i, double val) {
		switch (i) {
			case 0: a = val; return;
			case 1: b = val; return;
			case 2: c = val; return;
			case 3: d = val; return;
		}
		
		assert (false) : "Invalid index: " + i;
	}
	
	public void set(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public void set(Quaternion other) {
		set(other.a, other.b, other.c, other.d);
	}
	
	public void set(Vector3 v) {
		set(0.0, v.x, v.y, v.z);
	}
	 
	
	/**************************
	 *   Static Methods
	 **************************/
	
	public static void getRotation(Quaternion q, Vector3 axis, double angle) {
		double halfAngle = angle / 2.0;
		double multiplier = Math.sin( halfAngle ) / axis.getLength();
		q.set(
			Math.cos( halfAngle ),
			multiplier * axis.x,
			multiplier * axis.y,
			multiplier * axis.z
		);
		q.normalize();
	}
	
	public static void getRotationByPi(Quaternion q, Vector3 axis) {
		q.set(axis);
		q.normalize();
	}

	public static void getRotation(Quaternion q, Vector3 from, Vector3 to) {
		// calculate the rotation axis
		Vector3 fromUnit = new Vector3(from);
		fromUnit.normalize();
		Vector3 toUnit = new Vector3(to);
		toUnit.normalize();
		Vector3 axis = new Vector3();
		fromUnit.getCross(axis, toUnit);
		axis.normalize();
		
		// NOTE: this rotation is poorly-defined when from and to are colinear
		// if from and two are in the same direction, return the identity
		double dot = from.getDot(to);
		if (CompareReal.eq(dot, 1.0, 1e-10)) {
			getIdentity(q);
			return;
		}
		// if from and two are in opposite directions, pick an arbitrary, but perpendicular, rotation axis
		if (CompareReal.eq(dot, -1.0, 1e-10)) {
			from.getArbitraryOrthogonal(axis);
		}
		
		// just in case...
		assert (axis.x != Double.NaN && axis.y != Double.NaN && axis.z != Double.NaN);
		
		// calculate the rotation angle
		double angle = Math.acos(Math.min(fromUnit.getDot(toUnit), 1.0));
		
		getRotation(q, axis, angle);
	}

	public static void getRotation(Quaternion q, Vector3 axis, Vector3 from, Vector3 to) {
		// make sure the rotation is well-defined
		assert (CompareReal.eq(axis.getSquaredLength(), 1));
		assert (CompareReal.eq(axis.getDot(from), axis.getDot(to)));
		
		// build a basis that represents the coordinate system where x is from and z is the axis
		Matrix3 rot = new Matrix3();
		from = new Vector3(from);
		from.orthogonalProjection(axis);
		Matrix3.getRightBasisFromXZ(rot, from, axis);
		
		// rotate to into that coordinate system
		rot.transpose();
		to = new Vector3(to);
		rot.multiply(to);
		
		// calculate the rotation angle
		double angle = Math.atan2(to.y, to.x);
		
		getRotation(q, axis, angle);
	}

	public static void getRotation(Quaternion q, Matrix3 basis) {
		// code adapted from:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm
		double[][] m = basis.data;
		double tr = m[0][0] + m[1][1] + m[2][2];
		
		if (tr > 0) {
			double S = Math.sqrt( tr + 1.0 ) * 2;
			q.a = 0.25 * S;
			q.b = ( m[2][1] - m[1][2] ) / S;
			q.c = ( m[0][2] - m[2][0] ) / S;
			q.d = ( m[1][0] - m[0][1] ) / S;
		} else if ( (m[0][0] > m[1][1]) & (m[0][0] > m[2][2])) {
			double S = Math.sqrt( 1.0 + m[0][0] - m[1][1] - m[2][2] ) * 2;
			q.a = ( m[2][1] - m[1][2] ) / S;
			q.b = 0.25 * S;
			q.c = ( m[0][1] + m[1][0] ) / S;
			q.d = ( m[0][2] + m[2][0] ) / S;
		} else if (m[1][1] > m[2][2]) {
			double S = Math.sqrt( 1.0 + m[1][1] - m[0][0] - m[2][2] ) * 2;
			q.a = ( m[0][2] - m[2][0] ) / S;
			q.b = ( m[0][1] + m[1][0] ) / S;
			q.c = 0.25 * S;
			q.d = ( m[1][2] + m[2][1] ) / S;
		} else {
			double S = Math.sqrt( 1.0 + m[2][2] - m[0][0] - m[1][1] ) * 2;
			q.a = ( m[1][0] - m[0][1] ) / S;
			q.b = ( m[0][2] + m[2][0] ) / S;
			q.c = ( m[1][2] + m[2][1] ) / S;
			q.d = 0.25 * S;
		}
		
		// only return quaternions with positive i component so we avoid double-covering S^3
		if (q.b < 0) {
			q.negate();
		}
	}
	
	public static Quaternion getIdentity() {
		return new Quaternion(1.0, 0.0, 0.0, 0.0);
	}
	
	public static void getIdentity(Quaternion q) {
		q.set(1.0, 0.0, 0.0, 0.0);
	}
	
	/**************************
	 *   Methods
	 **************************/
	
	public double toAxisAngle(Vector3 axis) {
		double multiplier = Math.sqrt(1.0 - a * a);
		axis.set(
			b / multiplier,
			c / multiplier,
			d / multiplier
		);
		return Math.acos(a) * 2.0;
	}
	
	public void negate() {
		// this doesn't really make any sense to do in "the real world"
		// but it's useful for testing
		a = -a;
		b = -b;
		c = -c;
		d = -d;
	}
	
	public double getSquaredLength() {
		return a * a + b * b + c * c + d * d;
	}
	
	public double getLength() {
		return Math.sqrt(getSquaredLength());
	}
	
	public void normalize() {
		double length = getLength();
		a /= length;
		b /= length;
		c /= length;
		d /= length;
	}
	
	public void add(Quaternion other) {
		a += other.a;
		b += other.b;
		c += other.c;
		d += other.d;
	}
	
	public void subtract(Quaternion other) {
		a -= other.a;
		b -= other.b;
		c -= other.c;
		d -= other.d;
	}

	public void multiplyLeft(Quaternion other) {
		/*
			q1q2
			= (a1a2 − b1b2 − c1c2 − d1d2)
			+ (a1b2 + b1a2 + c1d2 − d1c2)i
			+ (a1c2 − b1d2 + c1a2 + d1b2)j
			+ (a1d2 + b1c2 − c1b2 + d1a2)k
			other = q1
			this = q2
		*/
		set(
			other.a * a - other.b * b - other.c * c - other.d * d,
			other.a * b + other.b * a + other.c * d - other.d * c,
			other.a * c - other.b * d + other.c * a + other.d * b,
			other.a * d + other.b * c - other.c * b + other.d * a
		);
	}
	
	public void conjugate() {
		b = -b;
		c = -c;
		d = -d;
	}
	
	public double getDot(Quaternion other) {
		return a * other.a + b * other.b + c * other.c + d * other.d;
	}
	
	public String toString() {
		return "( " + a + ", " + b + "i, " + c + "j, " + d + "k )";
	}

	public void rotate(Vector3 out) {
		// v' = qvq'
		
		// first, compute i = vq'
		double ia = out.x*b + out.y*c + out.z*d;
		double ib = out.x*a - out.y*d + out.z*c;
		double ic = out.x*d + out.y*a - out.z*b;
		double id = -out.x*c + out.y*b + out.z*a;
		
		// finally, compute o = qvq'
		out.set(
			a*ib + b*ia + c*id - d*ic,
			a*ic - b*id + c*ia + d*ib,
			a*id + b*ic - c*ib + d*ia
		);
	}
	
	public void rotate(Matrix3 basis) {
		for (int i = 0; i < Matrix3.Dimension; i++) {
			Vector3 axis = new Vector3();
			basis.getAxis(axis, i);
			rotate(axis);
			basis.setAxis(axis, i);
		}
	}

	public double getDistance(Quaternion other) {
		/* Jeff: 12/05/2008 - NOTE:
			The standard quaternion distance formula (length of the great arc) is
			d = ||log(p^*q)|| = acos(p.q) = angle of rotation about an axis for quaternion p^*q
			
			However, I only care about unit quaternions and this lets q and -q have a nonzero
			distance is annoying because they represent the same rotation.
			
			Therefore, I changed the distance formula to the following which fixes this problem.
			d = 2*acos(|p.q|)
		*/
		
		double aCopy = Math.abs(getDot(other));
		
		// HACKHACK: sometimes copy.a isn't strictly less than 1.0
		if (aCopy > 1.0) {
			aCopy = 1.0;
		}
		
		return Math.acos(aCopy);
	}
}
