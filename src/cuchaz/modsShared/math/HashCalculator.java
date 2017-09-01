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

public class HashCalculator {
	
	/**************************
	 * Static Methods
	 **************************/
	
	public static int combineHashes(int... nums) {
		int hashCode = 1;
		for (int i : nums) {
			hashCode = hashCode * 31 + i;
		}
		return hashCode;
	}
	
	public static int combineHashesCommutative(int... nums) {
		int hashCode = 1;
		for (int i : nums) {
			hashCode += i;
		}
		return hashCode;
	}
	
	public static int hashIds(int... nums) {
		int hashCode = 1;
		for (int i : nums) {
			hashCode = hashCode * 37 ^ (i + 1);
		}
		return hashCode;
	}
}
