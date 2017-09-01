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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Vector3 implements Serializable, Comparable<Vector3> {
	/**************************
	 *   Definitions
	 **************************/
	
	private static final long serialVersionUID = -7088845310201551458L;
	
	public static final int Dimension = 3;
	
	private static final Vector3 Origin = new Vector3(0.0, 0.0, 0.0);
	private static final Vector3 UnitX = new Vector3(1.0, 0.0, 0.0);
	private static final Vector3 UnitY = new Vector3(0.0, 1.0, 0.0);
	private static final Vector3 UnitZ = new Vector3(0.0, 0.0, 1.0);	

	/**************************
	 *   Fields
	 **************************/
	
	public double x;
	public double y;
	public double z;
	
	
	/**************************
	 *   Constructors
	 **************************/
	
	public Vector3() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(double[] vals) {
		set(vals);
	}
	
	public Vector3(Vector3 other) {
		set(other);
	}
	
	/**************************
	 *   Accessors
	 **************************/
	
	public double get(int i) {
		switch (i) {
			case 0: return x;
			case 1: return y;
			case 2: return z;
		}
		
		assert (false) : "Invalid index: " + i;
		
		// just to make the compiler happy
		return Double.NaN;
	}
	
	public void set(int i, double val) {
		switch (i) {
			case 0: x = val; return;
			case 1: y = val; return;
			case 2: z = val; return;
		}
		
		assert (false) : "Invalid index: " + i;
	}
	
	public void set(Vector3 other) {
		x = other.x;
		y = other.y;
		z = other.z;
	}
	
	public void set(double a, double b, double c) {
		x = a;
		y = b;
		z = c;
	}
	
	public void set(double[] vals) {
		assert (vals.length == Vector3.Dimension);
		
		x = vals[0];
		y = vals[1];
		z = vals[2];
	}

	public boolean isValid() {
		return !Double.isNaN(x) && !Double.isInfinite(x)
			&& !Double.isNaN(y) && !Double.isInfinite(y)
			&& !Double.isNaN(z) && !Double.isInfinite(z);
	}
	
	
	/**************************
	 *   Static Methods
	 **************************/
	
	public static void getUnitX(Vector3 v) {
		v.set(UnitX);
	}
	
	public static void getUnitY(Vector3 v) {
		v.set(UnitY);
	}
	
	public static void getUnitZ(Vector3 v) {
		v.set(UnitZ);
	}
	
	public static void getOrigin(Vector3 v) {
		v.set(Origin);
	}
	
	public static Vector3 getUnitX() {
		return new Vector3(UnitX);
	}
	
	public static Vector3 getUnitY() {
		return new Vector3(UnitY);
	}
	
	public static Vector3 getUnitZ() {
		return new Vector3(UnitZ);
	}
	
	public static Vector3 getOrigin() {
		return new Vector3(Origin);
	}
	
	public static void getNormal(Vector3 out, Vector3 a, Vector3 b, Vector3 c) {
		// precondition: a, b, c are not colinear
		// returns bc x ba
		
		out.set(c);
		out.subtract(b);
		out.normalize();
		
		Vector3 v = new Vector3();
		v.set(a);
		v.subtract(b);
		v.normalize();
		
		out.getCross(out, v);
		out.normalize();
	}
	
	public static List<Vector3> copyList(List<Vector3> points) {
		List<Vector3> pointsCopy = new ArrayList<Vector3>(points.size());
		for (Vector3 p : points) {
			pointsCopy.add(new Vector3(p));
		}
		return pointsCopy;
	}
	
	public static List<List<Vector3>> copyListList(List<List<Vector3>> points) {
		List<List<Vector3>> pointsCopy = new ArrayList<List<Vector3>>(points.size());
		for (List<Vector3> list : points) {
			pointsCopy.add(copyList(list));
		}
		return pointsCopy;
	}
	
	/**************************
	 *   Methods
	 **************************/
	
	public double getSquaredLength() {
		return x * x + y * y + z * z;
	}
	
	public double getLength() {
		return Math.sqrt(getSquaredLength());
	}
	
	public void normalize() {
		double length = getLength();
		x /= length;
		y /= length;
		z /= length;
	}
	
	public double getSquaredDistance(Vector3 other) {
		double dx = other.x - x;
		double dy = other.y - y;
		double dz = other.z - z;
		return dx * dx + dy * dy + dz * dz;
	}
	
	public double getDistance(Vector3 other) {
		return Math.sqrt(getSquaredDistance(other));
	}
	
	public void add(Vector3 other) {
		x += other.x;
		y += other.y;
		z += other.z;
	}
	
	public void subtract(Vector3 other) {
		x -= other.x;
		y -= other.y;
		z -= other.z;
	}
	
	public void scale(double s) {
		x *= s;
		y *= s;
		z *= s;
	}
	
	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	public double getDot(Vector3 other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public void getCross(Vector3 out, Vector3 other) {
		out.set(
			y * other.z - z * other.y,
			z * other.x - x * other.z,
			x * other.y - y * other.x
		);
	}
	
	public double getCrossMagnitude(Vector3 other) {
		double dot = getDot(other);
		return Math.sqrt(1.0 - dot * dot);
	}
	
	public void getArbitraryOrthogonal(Vector3 out) {
		// pick the coordinate axis that is already the most perpendicular to this vector
		double xdot = Math.abs(x);
		double ydot = Math.abs(y);
		double zdot = Math.abs(z);
		if (xdot <= ydot && xdot <= zdot) {
			Vector3.getUnitX(out);
		} else if (ydot <= xdot && ydot <= zdot) {
			Vector3.getUnitY(out);
		} else {
			Vector3.getUnitZ(out);
		}
		
		// project it orthogonally to finish the job
		Matrix3 projection = new Matrix3();
		Matrix3.getOrthogonalProjection(projection, this);
		projection.multiply(out);
		
		assert (CompareReal.eq(out.getDot(this), 0.0));
	}
	
	public void rotate(Quaternion q) {
		q.rotate(this);
	}
	
	public void transform(Matrix3 m) {
		m.multiply(this);
	}
	
	public String toString() {
		return "( " + x + ", " + y + ", " + z + " )";
	}
	
	public void toAngles(double[] angles) {
		// equtorial angle (x axis = 0)
		angles[0] = Math.atan2(y, x);
		// polar angle (equator = 0, poles = +- PI)
		angles[1] = Math.atan2(z, Math.sqrt(x * x + y * y));
	}
	
	public void fromAngles(double[] angles) {
		x = Math.cos(angles[0]) * Math.cos(angles[1]);
		y = Math.sin(angles[0]) * Math.cos(angles[1]);
		z = Math.sin(angles[1]);
		assert (CompareReal.eq(x * x + y * y + z * z, 1.0));
	}

	public void orthogonalProjection(Vector3 normal) {
		double dot = getDot( normal );
		set(
			x - normal.x * dot,
			y - normal.y * dot,
			z - normal.z * dot
		);
		assert (CompareReal.eq(getDot(normal), 0.0));
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector3) {
			return equals((Vector3)other);
		}
		
		return false;
	}
	
	public boolean equals(Vector3 other) {
		// NOTE: don't want fuzzy comparisons here
		return x == other.x && y == other.y && z == other.z;
	}
	
	public boolean approximatelyEquals(Vector3 other) {
		return approximatelyEquals(other, CompareReal.getEpsilon());
	}
	
	public boolean approximatelyEquals(Vector3 other, double epsilon) {
		return CompareReal.eq(getDistance(other), 0.0, epsilon);
	}

	@Override
	public int hashCode() {
		return HashCalculator.combineHashes(
			Double.valueOf( x ).hashCode(),
			Double.valueOf( y ).hashCode(),
			Double.valueOf( z ).hashCode()
		);
	}
	
	@Override
	public int compareTo(Vector3 other) {
		int result = Double.compare(x, other.x);
		if (result != 0) {
			return result;
		}
		result = Double.compare(y, other.y);
		if (result != 0) {
			return result;
		}
		return Double.compare(z, other.z);
	}
}
