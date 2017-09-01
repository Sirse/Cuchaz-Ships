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

public class BlockSubset extends BlockSet {
	
	private static final long serialVersionUID = 4665021537648940014L;
	
	BlockSet m_parent;
	
	public BlockSubset(BlockSet parent) {
		m_parent = parent;
	}
	
	public BlockSet getParent() {
		return m_parent;
	}
}
