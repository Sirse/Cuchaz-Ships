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
package cuchaz.modsShared.math;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public enum BoxCorner {
	BottomNorthEast {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.maxX;
			out.yCoord = box.minY;
			out.zCoord = box.maxZ;
		}
	},
	BottomNorthWest {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.maxX;
			out.yCoord = box.minY;
			out.zCoord = box.minZ;
		}
	},
	BottomSouthWest {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.minX;
			out.yCoord = box.minY;
			out.zCoord = box.minZ;
		}
	},
	BottomSouthEast {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.minX;
			out.yCoord = box.minY;
			out.zCoord = box.maxZ;
		}
	},
	TopNorthEast {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.maxX;
			out.yCoord = box.maxY;
			out.zCoord = box.maxZ;
		}
	},
	TopNorthWest {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.maxX;
			out.yCoord = box.maxY;
			out.zCoord = box.minZ;
		}
	},
	TopSouthWest {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.minX;
			out.yCoord = box.maxY;
			out.zCoord = box.minZ;
		}
	},
	TopSouthEast {
		
		@Override
		public void getPoint(Vec3 out, AxisAlignedBB box) {
			out.xCoord = box.minX;
			out.yCoord = box.maxY;
			out.zCoord = box.maxZ;
		}
	};
	
	public abstract void getPoint(Vec3 out, AxisAlignedBB box);
}
