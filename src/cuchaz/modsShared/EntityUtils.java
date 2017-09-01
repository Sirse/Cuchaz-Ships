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
package cuchaz.modsShared;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EntityUtils {
	
	public static Vec3 getPlayerEyePos(EntityPlayer player) {
		if (player == null) {
			throw new IllegalArgumentException("player cannot be null!");
		}
		
		return Vec3.createVectorHelper(player.posX, player.posY + (Environment.isClient() ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight()), player.posZ);
	}
	
	public static Vec3 getPlayerLookDirection(EntityPlayer player) {
		if (player == null) {
			throw new IllegalArgumentException("player cannot be null!");
		}
		
		final double toRadians = Math.PI / 180.0;
		float pitch = (float) (player.rotationPitch * toRadians);
		float yaw = (float) (player.rotationYaw * toRadians);
		float cosYaw = MathHelper.cos(-yaw - (float)Math.PI);
		float sinYaw = MathHelper.sin(-yaw - (float)Math.PI);
		float cosPitch = MathHelper.cos(-pitch);
		float sinPitch = MathHelper.sin(-pitch);
		
		return Vec3.createVectorHelper(sinYaw * -cosPitch, sinPitch, cosYaw * -cosPitch);
	}
	
	public static double getPlayerReachDistance(EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			return ((EntityPlayerMP)player).theItemInWorldManager.getBlockReachDistance();
		} else if (player instanceof AbstractClientPlayer) {
			return Minecraft.getMinecraft().playerController.getBlockReachDistance();
		}
		
		Log.logger.warning("Unable to determine reach distance for player!");
		return 0;
	}
}
