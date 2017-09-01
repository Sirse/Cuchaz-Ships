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
package cuchaz.modsShared.blocks;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Envelopes {
	
	private Map<BlockSide,BlockArray> m_envelopes;
	private BoundingBoxInt m_boundingBox;
	
	public Envelopes(Set<Coords> blocks) {
		m_boundingBox = new BoundingBoxInt(blocks);
		
		// init the extreme arrays
		m_envelopes = new TreeMap<BlockSide,BlockArray>();
		for (BlockSide side : BlockSide.values()) {
			BlockArray surface = new BlockArray(side.getU(m_boundingBox.minX, m_boundingBox.minY, m_boundingBox.minZ), side.getV(m_boundingBox.minX, m_boundingBox.minY, m_boundingBox.minZ), side.getWidth(m_boundingBox.getDx(), m_boundingBox.getDy(), m_boundingBox.getDz()), side.getHeight(m_boundingBox.getDx(), m_boundingBox.getDy(), m_boundingBox.getDz()));
			for (int u = surface.getUMin(); u <= surface.getUMax(); u++) {
				for (int v = surface.getVMin(); v <= surface.getVMax(); v++) {
					surface.setBlock(u, v, null);
				}
			}
			m_envelopes.put(side, surface);
		}
		
		// compute the envelopes
		for (Coords coords : blocks) {
			for (BlockSide side : BlockSide.values()) {
				BlockArray surface = m_envelopes.get(side);
				int u = side.getU(coords.x, coords.y, coords.z);
				int v = side.getV(coords.x, coords.y, coords.z);
				Coords extremalCoords = surface.getBlock(u, v);
				if (extremalCoords == null || side.isMoreExtremal(coords, extremalCoords)) {
					surface.setBlock(u, v, coords);
				}
			}
		}
	}
	
	public BoundingBoxInt getBoundingBox() {
		return m_boundingBox;
	}
	
	public BlockArray getEnvelope(BlockSide side) {
		return m_envelopes.get(side);
	}
}
