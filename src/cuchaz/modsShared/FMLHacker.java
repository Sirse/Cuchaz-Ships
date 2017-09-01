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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import cpw.mods.fml.common.InjectedModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class FMLHacker {
	
	public static void unwrapModContainer(ModContainer target) {
		try {
			// get the mod controller
			Field modControllerField = Loader.class.getDeclaredField("modController");
			modControllerField.setAccessible(true);
			LoadController modController = (LoadController)modControllerField.get(Loader.instance());
			
			// get the active mods list
			Field activeModListField = LoadController.class.getDeclaredField("activeModList");
			activeModListField.setAccessible(true);
			@SuppressWarnings("unchecked")
			List<ModContainer> activeModList = (List<ModContainer>)activeModListField.get(modController);
			
			// find our mod container
			InjectedModContainer wrapperContainer = null;
			for (ModContainer modContainer : activeModList) {
				if (modContainer instanceof InjectedModContainer) {
					InjectedModContainer container = (InjectedModContainer)modContainer;
					if (container.wrappedContainer == target) {
						wrapperContainer = container;
					}
				}
			}
			if (wrapperContainer == null) {
				throw new IllegalArgumentException("Unable to find target mod container!");
			}
			
			// unwrap our mod container in-place
			for (int i = 0; i < activeModList.size(); i++) {
				if (activeModList.get(i) == wrapperContainer) {
					activeModList.set(i, target);
				}
			}
			
			// rebuild the mod object list if needed
			Field modObjectListField = LoadController.class.getDeclaredField("modObjectList");
			modObjectListField.setAccessible(true);
			if (modObjectListField.get(modController) != null) {
				modObjectListField.set(modController, modController.buildModObjectList());
			}
			
			// look through namedMods too: Loader.private Map<String, ModContainer> namedMods;
			Field namedModsField = Loader.class.getDeclaredField("namedMods");
			namedModsField.setAccessible(true);
			wrapperContainer = null;
			@SuppressWarnings("unchecked")
			Map<String,ModContainer> namedMods = new HashMap<String,ModContainer>((Map<String,ModContainer>)namedModsField.get(Loader.instance()));
			for (Map.Entry<String,ModContainer> entry : namedMods.entrySet()) {
				ModContainer modContainer = entry.getValue();
				if (modContainer instanceof InjectedModContainer) {
					InjectedModContainer container = (InjectedModContainer)modContainer;
					if (container.wrappedContainer == target) {
						wrapperContainer = container;
						entry.setValue(container.wrappedContainer);
					}
				}
			}
			if (wrapperContainer == null) {
				throw new IllegalArgumentException("Unable to find target mod container!");
			} else {
				// replace the old namedMods since it's immutable
				namedModsField.set(Loader.instance(), namedMods);
			}
		} catch (Exception ex) {
			Log.logger.log(Level.WARNING, "Unable to unwrap mod container!", ex);
		}
	}
	
	public static File getModSource(Class<? extends ModContainer> c) {
		// determine the mod source
		URL url = c.getProtectionDomain().getCodeSource().getLocation();
		try {
			// NOTE: urls look like this:
			// jar:file:/C:/proj/parser/jar/parser.jar!/test.xml
			// file:/C:/proj/parser/jar/parser.jar
			if (url.getProtocol().equalsIgnoreCase("jar")) {
				JarURLConnection connection = (JarURLConnection)url.openConnection();
				return new File(connection.getJarFileURL().toURI());
			} else if (url.getProtocol().equalsIgnoreCase("file")) {
				// get the file of the mod container
				File file = new File(url.toURI());
				
				// the file will look something like /path/to/folder/bin/package/package/Mod.class
				// for resource loading, we want just the /path/to/folder/bin part
				int numPackageComponents = c.getPackage().getName().split("\\.").length;
				for (int i = 0; i <= numPackageComponents; i++) {
					file = file.getParentFile();
				}
				return file;
			} else {
				throw new Error("Unable to determine mod source: " + url.toString());
			}
		} catch (IOException ex) {
			throw new Error("Unable to determine mod source: " + url.toString(), ex);
		} catch (URISyntaxException ex) {
			throw new Error("Unable to determine mod source: " + url.toString(), ex);
		}
	}
}
