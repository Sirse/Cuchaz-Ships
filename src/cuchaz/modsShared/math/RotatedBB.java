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

import cuchaz.modsShared.blocks.BlockSide;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotatedBB {
	
	private AxisAlignedBB m_box;
	private float m_yaw;
	private double m_centerX;
	private double m_centerZ;
	
	public RotatedBB(AxisAlignedBB box, float yaw) {
		this(box, yaw, (box.maxX + box.minX) / 2, (box.maxZ + box.minZ) / 2);
	}
	
	public RotatedBB(AxisAlignedBB box, float yaw, double centerX, double centerZ) {
		m_box = box;
		m_yaw = yaw;
		m_centerX = centerX;
		m_centerZ = centerZ;
	}
	
	public AxisAlignedBB getAABox() {
		return m_box;
	}
	
	public float getYaw() {
		return m_yaw;
	}
	
	public double getMinY() {
		return m_box.minY;
	}
	
	public double getMaxY() {
		return m_box.maxY;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("[RotatedBB]");
		buf.append(String.format(" y=[%.2f,%.2f]", m_box.minY, m_box.maxY));
		Vec3 p = Vec3.createVectorHelper(0, 0, 0);
		for (BoxCorner corner : BlockSide.Top.getCorners()) {
			getCorner(p, corner);
			buf.append(String.format(" (%.2f,%.2f)", p.xCoord, p.zCoord));
		}
		return buf.toString();
	}
	
	public void getCorner(Vec3 out, BoxCorner corner) {
		corner.getPoint(out, m_box);
		
		// translate so the box is centered at the origin
		out.xCoord -= m_centerX;
		out.zCoord -= m_centerZ;
		
		// rotate by the yaw
		float yawRad = (float)Math.toRadians(m_yaw);
		float cos = MathHelper.cos(yawRad);
		float sin = MathHelper.sin(yawRad);
		
		double x = out.xCoord * cos + out.zCoord * sin;
		double z = -out.xCoord * sin + out.zCoord * cos;
		out.xCoord = x;
		out.zCoord = z;
		
		// translate back to the world coords
		out.xCoord += m_centerX;
		out.zCoord += m_centerZ;
	}
	
	public boolean containsPoint(double x, double y, double z) {
		// y is easy
		if (y < m_box.minY || y > m_box.maxY) {
			return false;
		}
		
		// translate so the box is centered at the origin
		x -= m_centerX;
		z -= m_centerZ;
		
		// rotate the query point into box space
		float yawRad = (float)Math.toRadians(-m_yaw);
		float cos = MathHelper.cos(yawRad);
		float sin = MathHelper.sin(yawRad);
		double newx = x * cos + z * sin;
		double newz = -x * sin + z * cos;
		x = newx;
		z = newz;
		
		// translate back to the world coords
		x += m_centerX;
		z += m_centerZ;
		
		// finally, perform the check
		return x >= m_box.minX && x <= m_box.maxX && z >= m_box.minZ && z <= m_box.maxZ;
	}
}
