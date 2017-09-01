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

public class CompareReal {
	
	/**************************
	 * Data Members
	 **************************/
	
	private static double m_epsilon = 1e-5;
	
	/**************************
	 * Accessors
	 **************************/
	
	public static double getEpsilon() {
		return m_epsilon;
	}
	
	public static void setEpsilon(double epsilon) {
		m_epsilon = epsilon;
	}
	
	/**************************
	 * Methods
	 **************************/
	
	public static boolean eq(double a, double b) {
		return eq(a, b, m_epsilon);
	}
	
	public static boolean eq(double a, double b, double epsilon) {
		return (a - b < epsilon) && (b - a < epsilon);
	}
	
	public static boolean neq(double a, double b) {
		return neq(a, b, m_epsilon);
	}
	
	public static boolean neq(double a, double b, double epsilon) {
		return (a - b >= epsilon) || (b - a >= epsilon);
	}
	
	public static boolean lte(double a, double b) {
		return lte(a, b, m_epsilon);
	}
	
	public static boolean lte(double a, double b, double epsilon) {
		return a - b <= epsilon;
	}
	
	public static boolean gte(double a, double b) {
		return gte(a, b, m_epsilon);
	}
	
	public static boolean gte(double a, double b, double epsilon) {
		return b - a <= epsilon;
	}
}
