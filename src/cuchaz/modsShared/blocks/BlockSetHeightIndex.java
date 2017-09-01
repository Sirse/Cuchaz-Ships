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

import java.util.Map;
import java.util.TreeMap;

public class BlockSetHeightIndex {
	
	private Map<Integer,BlockSet> m_layers;
	
	public BlockSetHeightIndex() {
		m_layers = new TreeMap<Integer,BlockSet>();
	}
	
	public BlockSetHeightIndex(BlockSet blocks) {
		this();
		add(blocks);
	}
	
	public void add(BlockSet blocks) {
		for (Coords coords : blocks) {
			// is there a layer at this y yet?
			BlockSet layer = m_layers.get(coords.y);
			if (layer == null) {
				layer = new BlockSet();
				m_layers.put(coords.y, layer);
			}
			
			layer.add(coords);
		}
	}
	
	public BlockSet get(int y) {
		return m_layers.get(y);
	}
}
