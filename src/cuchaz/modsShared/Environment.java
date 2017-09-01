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

import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;

public class Environment {
	
	private static Boolean m_isObfuscated;
	
	static {
		m_isObfuscated = null;
	}
	
	public static boolean isObfuscated() {
		if (m_isObfuscated == null) {
			// attempt to detect whether or not the environment is obfuscated
			try {
				// check for a well-known method name
				Entity.class.getDeclaredMethod("onUpdate");
				m_isObfuscated = false;
			} catch (NoSuchMethodException ex) {
				m_isObfuscated = true;
			} catch (SecurityException ex) {
				// this is a problem...
				throw new Error("Unable to reflect on Minecraft classes!", ex);
			}
		}
		
		return m_isObfuscated;
	}
	
	public static String getRuntimeName(String name, String id) {
		return isObfuscated() ? id : name;
	}
	
	public static Side getSide() {
		// which thread are we on?
		// looks like the best way to tell is to ask which thread we're in
		String threadName = Thread.currentThread().getName();
		if (threadName.equalsIgnoreCase("client thread")) {
			return Side.CLIENT;
		} else if (threadName.equalsIgnoreCase("server thread")) {
			return Side.SERVER;
		} else {
			throw new Error("I don't know what side we're on for thread " + threadName);
		}
	}
	
	public static boolean isClient() {
		return getSide() == Side.CLIENT;
	}
	
	public static boolean isServer() {
		return getSide() == Side.SERVER;
	}
}
