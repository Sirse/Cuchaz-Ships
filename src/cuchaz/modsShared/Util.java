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
package cuchaz.modsShared;

import java.io.Closeable;
import java.io.IOException;

public class Util {
	
	public static final int TicksPerSecond = 20; // constant set in Minecraft.java, inaccessible by methods
	
	public static double secondsToTicks(double x) {
		return x * TicksPerSecond;
	}
	
	public static double perTickToPerSecond(double x) {
		return x * TicksPerSecond;
	}
	
	public static double perTick2ToPerSecond2(double x) {
		return x * TicksPerSecond * TicksPerSecond;
	}
	
	public static double ticksToSeconds(double x) {
		return x / TicksPerSecond;
	}
	
	public static double perSecond2ToPerTick2(double x) {
		return x / TicksPerSecond / TicksPerSecond;
	}
	
	public static double perSecondToPerTick(double x) {
		return x / TicksPerSecond;
	}
	
	public static int realModulus(int a, int b) {
		// NOTE: Java's % operator is not a true modulus
		// it's a remainder operator
		return (a % b + b) % b;
	}
	
	public static void closeSilently(Closeable c) {
		if (c == null) {
			return;
		}
		
		try {
			c.close();
		} catch (IOException ex) {
			// ignore
		}
	}
}
