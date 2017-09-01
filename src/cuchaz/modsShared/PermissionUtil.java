package cuchaz.modsShared;

import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Method;

public class PermissionUtil {

    public static PermissionUtil instance = new PermissionUtil();
    private Class bukkit;
    private Method getPlayer;
    private Method hasPermission;

    public PermissionUtil() {
        try {
            this.bukkit = Class.forName("org.bukkit.Bukkit");
            this.getPlayer = this.bukkit.getMethod("getPlayer", new Class[]{String.class});
            this.hasPermission = Class.forName("org.bukkit.entity.Player").getMethod("hasPermission", new Class[]{String.class});
        } catch (Exception exp) {
        }
    }

    public static boolean hasPermission(EntityPlayer player, String permission) {
        return instance.bukkit != null ? instance.bukkitPermission(player.getCommandSenderName(), permission) : true;
    }

    private boolean bukkitPermission(String username, String permission) {
        try {
            Object e = this.getPlayer.invoke(null, new Object[]{username});
            return ((Boolean) this.hasPermission.invoke(e, new Object[]{permission})).booleanValue();
        } catch (Exception exp) {
        }

        return false;
    }
}