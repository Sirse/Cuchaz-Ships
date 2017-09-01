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

public class BlockArray {
	
	public static enum Rotation {
		None {
			
			@Override
			public BlockArray rotate(BlockArray blocks) {
				return blocks;
			}
		},
		Ccw90 {
			
			@Override
			public BlockArray rotate(BlockArray blocks) {
				BlockArray out = new BlockArray(0, 0, blocks.getHeight(), blocks.getWidth());
				for (int u = 0; u < out.getWidth(); u++) {
					for (int v = 0; v < out.getHeight(); v++) {
						out.m_blocks[v][u] = blocks.m_blocks[blocks.getHeight() - 1 - u][v];
					}
				}
				return out;
			}
		};
		
		// UNDONE: implement these if needed
		// Ccw180
		// Ccw270
		
		public abstract BlockArray rotate(BlockArray blocks);
	}
	
	private int m_uMin;
	private int m_vMin;
	private Coords[][] m_blocks;
	
	public BlockArray(int uMin, int vMin, int width, int height) {
		m_uMin = uMin;
		m_vMin = vMin;
		m_blocks = new Coords[height][width];
	}
	
	public int getWidth() {
		return m_blocks[0].length;
	}
	
	public int getHeight() {
		return m_blocks.length;
	}
	
	public int getUMin() {
		return m_uMin;
	}
	
	public int getUMax() {
		return m_uMin + getWidth() - 1;
	}
	
	public int getVMin() {
		return m_vMin;
	}
	
	public int getVMax() {
		return m_vMin + getHeight() - 1;
	}
	
	public Coords getBlock(int u, int v) {
		return m_blocks[toZeroBasedV(v)][toZeroBasedU(u)];
	}
	
	public void setBlock(int u, int v, Coords coords) {
		m_blocks[toZeroBasedV(v)][toZeroBasedU(u)] = coords;
	}
	
	public int toZeroBasedU(int u) {
		return u - m_uMin;
	}
	
	public int toZeroBasedV(int v) {
		return v - m_vMin;
	}
	
	public BlockArray newEmptyCopy() {
		return new BlockArray(m_uMin, m_vMin, getWidth(), getHeight());
	}
	
	public BlockSet toBlockSet() {
		BlockSet blocks = new BlockSet();
		for (int u = 0; u < getWidth(); u++) {
			for (int v = 0; v < getHeight(); v++) {
				if (m_blocks[v][u] != null) {
					blocks.add(m_blocks[v][u]);
				}
			}
		}
		return blocks;
	}
}
