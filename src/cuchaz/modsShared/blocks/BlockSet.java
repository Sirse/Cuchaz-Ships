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

import java.util.Collection;
import java.util.HashSet;

public class BlockSet extends HashSet<Coords> {
	
	private static final long serialVersionUID = -1018340715197554750L;
	
	public BlockSet() {
		super();
	}
	
	public BlockSet(BlockSet other) {
		super(other);
	}
	
	public BlockSet(Collection<Coords> blocks) {
		super();
		addAll(blocks);
	}
	
	public BlockSet(int numLayers, char blockChar, String... lines) {
		this(numLayers, blockChar, 0, 0, 0, lines);
	}
	
	public BlockSet(int numLayers, int ox, int oy, int oz, String... lines) {
		this(numLayers, 'x', ox, oy, oz, lines);
	}
	
	public BlockSet(int layerSize, char blockChar, int ox, int oy, int oz, String... lines) {
		int y = 0;
		int z = 0;
		for (String line : lines) {
			for (int x = 0; x < line.length(); x++) {
				char c = line.charAt(x);
				if (c == blockChar) {
					// add the block
					add(new Coords(ox + x, oy + y, oz + z));
				}
			}
			
			// advance the pointer
			z++;
			if (z % layerSize == 0) {
				z = 0;
				y++;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		BoundingBoxInt box = getBoundingBox();
		Coords coords = new Coords();
		for (int y = box.minY; y <= box.maxY; y++) {
			for (int z = box.minZ; z <= box.maxZ; z++) {
				for (int x = box.minX; x <= box.maxX; x++) {
					coords.set(x, y, z);
					if (contains(coords)) {
						buf.append('x');
					} else {
						buf.append('.');
					}
				}
				buf.append('\n');
			}
			buf.append('\n');
		}
		return buf.toString();
	}
	
	public BoundingBoxInt getBoundingBox() {
		return new BoundingBoxInt(this);
	}
}
