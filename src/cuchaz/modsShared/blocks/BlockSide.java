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

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cuchaz.modsShared.math.BoxCorner;
import cuchaz.modsShared.math.CircleRange;

public enum BlockSide {
	// NOTE: order is important Bottom, Top, North, South, West, East
	// y-axis (-)
	Bottom(0, -1, 0, new BoxCorner[] { BoxCorner.BottomNorthEast, BoxCorner.BottomNorthWest, BoxCorner.BottomSouthWest, BoxCorner.BottomSouthEast }) {
		
		@Override
		public void renderSide(RenderBlocks renderBlocks, Block block, double x, double y, double z, IIcon icon) {
			renderBlocks.renderFaceYNeg(block, (double)x, (double)y, (double)z, icon);
		}
		
		@Override
		public int getWidth(int dx, int dy, int dz) {
			return dx;
		}
		
		@Override
		public int getHeight(int dx, int dy, int dz) {
			return dz;
		}
		
		@Override
		public int getU(int x, int y, int z) {
			return x;
		}
		
		@Override
		public int getV(int x, int y, int z) {
			return z;
		}
		
		@Override
		public boolean isMoreExtremal(Coords compareWith, Coords compareTo) {
			return compareWith.y < compareTo.y;
		}
		
		@Override
		public double getFractionSubmerged(int y, double waterHeight) {
			return y <= waterHeight ? 1 : 0;
		}
	},
	// y-axis (+)
	Top(0, 1, 0, new BoxCorner[] { BoxCorner.TopNorthEast, BoxCorner.TopNorthWest, BoxCorner.TopSouthWest, BoxCorner.TopSouthEast }) {
		
		@Override
		public void renderSide(RenderBlocks renderBlocks, Block block, double x, double y, double z, IIcon icon) {
			renderBlocks.renderFaceYPos(block, (double)x, (double)y, (double)z, icon);
		}
		
		@Override
		public int getWidth(int dx, int dy, int dz) {
			return dx;
		}
		
		@Override
		public int getHeight(int dx, int dy, int dz) {
			return dz;
		}
		
		@Override
		public int getU(int x, int y, int z) {
			return x;
		}
		
		@Override
		public int getV(int x, int y, int z) {
			return z;
		}
		
		@Override
		public boolean isMoreExtremal(Coords compareWith, Coords compareTo) {
			return compareWith.y > compareTo.y;
		}
		
		@Override
		public double getFractionSubmerged(int y, double waterHeight) {
			return y + 1 <= waterHeight ? 1 : 0;
		}
	},
	// z-axis (-)
	North(0, 0, -1, new BoxCorner[] { BoxCorner.TopNorthWest, BoxCorner.TopNorthEast, BoxCorner.BottomNorthEast, BoxCorner.BottomNorthWest }) {
		
		@Override
		public void renderSide(RenderBlocks renderBlocks, Block block, double x, double y, double z, IIcon icon) {
			renderBlocks.renderFaceZNeg(block, (double)x, (double)y, (double)z, icon);
		}
		
		@Override
		public int getWidth(int dx, int dy, int dz) {
			return dx;
		}
		
		@Override
		public int getHeight(int dx, int dy, int dz) {
			return dy;
		}
		
		@Override
		public int getU(int x, int y, int z) {
			return x;
		}
		
		@Override
		public int getV(int x, int y, int z) {
			return y;
		}
		
		@Override
		public boolean isMoreExtremal(Coords compareWith, Coords compareTo) {
			return compareWith.z < compareTo.z;
		}
		
		@Override
		public double getFractionSubmerged(int y, double waterHeight) {
			return getSideFractionSubmerged(y, waterHeight);
		}
	},
	// z-axis (+)
	South(0, 0, 1, new BoxCorner[] { BoxCorner.TopSouthEast, BoxCorner.TopSouthWest, BoxCorner.BottomSouthWest, BoxCorner.BottomSouthEast }) {
		
		@Override
		public void renderSide(RenderBlocks renderBlocks, Block block, double x, double y, double z, IIcon icon) {
			renderBlocks.renderFaceZPos(block, (double)x, (double)y, (double)z, icon);
		}
		
		@Override
		public int getWidth(int dx, int dy, int dz) {
			return dx;
		}
		
		@Override
		public int getHeight(int dx, int dy, int dz) {
			return dy;
		}
		
		@Override
		public int getU(int x, int y, int z) {
			return x;
		}
		
		@Override
		public int getV(int x, int y, int z) {
			return y;
		}
		
		@Override
		public boolean isMoreExtremal(Coords compareWith, Coords compareTo) {
			return compareWith.z > compareTo.z;
		}
		
		@Override
		public double getFractionSubmerged(int y, double waterHeight) {
			return getSideFractionSubmerged(y, waterHeight);
		}
	},
	// x-axis (-)
	West(-1, 0, 0, new BoxCorner[] { BoxCorner.TopSouthWest, BoxCorner.TopNorthWest, BoxCorner.BottomNorthWest, BoxCorner.BottomSouthWest }) {
		
		@Override
		public void renderSide(RenderBlocks renderBlocks, Block block, double x, double y, double z, IIcon icon) {
			renderBlocks.renderFaceXNeg(block, (double)x, (double)y, (double)z, icon);
		}
		
		@Override
		public int getWidth(int dx, int dy, int dz) {
			return dz;
		}
		
		@Override
		public int getHeight(int dx, int dy, int dz) {
			return dy;
		}
		
		@Override
		public int getU(int x, int y, int z) {
			return z;
		}
		
		@Override
		public int getV(int x, int y, int z) {
			return y;
		}
		
		@Override
		public boolean isMoreExtremal(Coords compareWith, Coords compareTo) {
			return compareWith.x < compareTo.x;
		}
		
		@Override
		public double getFractionSubmerged(int y, double waterHeight) {
			return getSideFractionSubmerged(y, waterHeight);
		}
	},
	// x-axis (+)
	East(1, 0, 0, new BoxCorner[] { BoxCorner.TopNorthEast, BoxCorner.TopSouthEast, BoxCorner.BottomSouthEast, BoxCorner.BottomNorthEast }) {
		
		@Override
		public void renderSide(RenderBlocks renderBlocks, Block block, double x, double y, double z, IIcon icon) {
			renderBlocks.renderFaceXPos(block, (double)x, (double)y, (double)z, icon);
		}
		
		@Override
		public int getWidth(int dx, int dy, int dz) {
			return dz;
		}
		
		@Override
		public int getHeight(int dx, int dy, int dz) {
			return dy;
		}
		
		@Override
		public int getU(int x, int y, int z) {
			return z;
		}
		
		@Override
		public int getV(int x, int y, int z) {
			return y;
		}
		
		@Override
		public boolean isMoreExtremal(Coords compareWith, Coords compareTo) {
			return compareWith.x > compareTo.x;
		}
		
		@Override
		public double getFractionSubmerged(int y, double waterHeight) {
			return getSideFractionSubmerged(y, waterHeight);
		}
	};
	
	private static BlockSide[] m_xzSides;
	
	private BlockSide m_oppositeSide;
	private int m_xzOffset;
	private int m_dx;
	private int m_dy;
	private int m_dz;
	private BoxCorner[] m_corners;
	
	private BlockSide(int dx, int dy, int dz, BoxCorner[] corners) {
		m_dx = dx;
		m_dy = dy;
		m_dz = dz;
		m_corners = corners;
		
		m_oppositeSide = null;
	}
	
	static {
		// set opposite sides
		Bottom.m_oppositeSide = Top;
		Top.m_oppositeSide = Bottom;
		East.m_oppositeSide = West;
		West.m_oppositeSide = East;
		North.m_oppositeSide = South;
		South.m_oppositeSide = North;
		
		// set zx side order
		m_xzSides = new BlockSide[] { North, East, South, West };
		North.m_xzOffset = 0;
		East.m_xzOffset = 1;
		South.m_xzOffset = 2;
		West.m_xzOffset = 3;
	}
	
	public int getId() {
		return ordinal();
	}
	
	public int getDx() {
		return m_dx;
	}
	
	public int getDy() {
		return m_dy;
	}
	
	public int getDz() {
		return m_dz;
	}
	
	public BoxCorner[] getCorners() {
		return m_corners;
	}
	
	public BlockSide getOppositeSide() {
		return m_oppositeSide;
	}
	
	public int getXZOffset() {
		return m_xzOffset;
	}
	
	public BlockSide rotateXZCcw(int offset) {
		return m_xzSides[ (m_xzOffset + m_xzSides.length - offset) % m_xzSides.length];
	}
	
	public BlockSide rotateXZCw(int offset) {
		return m_xzSides[ (m_xzOffset + offset) % m_xzSides.length];
	}
	
	public static BlockSide getById(int side) {
		return values()[side];
	}
	
	public static BlockSide getByYaw(float yaw) {
		// yaw values mapped to view direction and xzOffsets:
		// 0 south 2
		// 90 west 3
		// 180 north 0
		// -90 east 1
		
		int offset = MathHelper.floor_float(CircleRange.mapZeroTo360(yaw) * 4.0f / 360.0f + 0.5f) % m_xzSides.length;
		
		// assume we're looking at a block. Return the side we're looking at rather than the direction in which we're looking
		return m_xzSides[offset].getOppositeSide();
	}
	
	public static BlockSide getByXZOffset(int offset) {
		return m_xzSides[offset];
	}
	
	public abstract void renderSide(RenderBlocks renderBlocks, Block block, double x, double y, double z, IIcon icon);
	
	public abstract int getWidth(int dx, int dy, int dz);
	
	public abstract int getHeight(int dx, int dy, int dz);
	
	public abstract int getU(int x, int y, int z);
	
	public abstract int getV(int x, int y, int z);
	
	public abstract boolean isMoreExtremal(Coords compareWith, Coords compareTo);
	
	public abstract double getFractionSubmerged(int y, double waterHeight);
	
	public static BlockSide[] xzSides() {
		return m_xzSides;
	}
	
	private static double getSideFractionSubmerged(int y, double waterHeight) {
		double bottom = y;
		double top = y + 1;
		if (top <= waterHeight) {
			return 1.0;
		} else if (bottom > waterHeight) {
			return 0;
		} else {
			return waterHeight - bottom;
		}
	}
}
