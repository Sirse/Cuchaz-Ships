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

import java.awt.Color;

public class ColorUtils {
	
	private static final int DefaultAlpha = 255;
	
	public static int getColor(Color c) {
		return getColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	
	public static int getGrey(int grey) {
		return getColor(grey, grey, grey, DefaultAlpha);
	}
	
	public static int getGrey(int grey, int a) {
		return getColor(grey, grey, grey, a);
	}
	
	public static int getColor(int r, int g, int b) {
		return getColor(r, g, b, DefaultAlpha);
	}
	
	public static int getColor(int r, int g, int b, int a) {
		return (b & 0xff) | (g & 0xff) << 8 | (r & 0xff) << 16 | (a & 0xff) << 24;
	}
	
	public static int getRed(int color) {
		return (color >> 16) & 0xff;
	}
	
	public static int getGreen(int color) {
		return (color >> 8) & 0xff;
	}
	
	public static int getBlue(int color) {
		return color & 0xff;
	}
	
	public static int getAlpha(int color) {
		return (color >> 24) & 0xff;
	}
	
	public static float getRedf(int color) {
		return (float)getRed(color) / 255.0f;
	}
	
	public static float getGreenf(int color) {
		return (float)getGreen(color) / 255.0f;
	}
	
	public static float getBluef(int color) {
		return (float)getBlue(color) / 255.0f;
	}
	
	public static float getAlphaf(int color) {
		return (float)getAlpha(color) / 255.0f;
	}
}
