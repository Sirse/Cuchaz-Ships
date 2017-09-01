/*******************************************************************************
 * Copyright (c) 2014 jeff.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     jeff - initial API and implementation
 ******************************************************************************/
package cuchaz.modsShared.blocks;

import java.util.HashMap;

public class BlockMap<T> extends HashMap<Coords,T> {
	
	private static final long serialVersionUID = 283133229158446376L;
	
	private BlockSetProxy m_proxy;
	
	public BlockMap() {
		m_proxy = new BlockSetProxy(keySet());
	}
	
	public BlockSet blockSet() {
		return m_proxy;
	}
}
