package fr.slypy.aotuhc.titan;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class TitanDataChanger {
	
	public static void setPlayerSize(Player p, float size) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "size " + (size / 1.8) + " " + p.getUniqueId());
		
		BoundingBox box = new BoundingBox(p.getLocation().getX() - (((size / 1.8) * 0.6) / 2), p.getLocation().getY(), p.getLocation().getZ() - (((size / 1.8) * 0.6) / 2), p.getLocation().getX() + (((size / 1.8) * 0.6) / 2), p.getLocation().getY() + size, p.getLocation().getZ() + (((size / 1.8) * 0.6) / 2));
		
		Location pos1 = new Location(p.getWorld(), Math.floor(box.getMinX()), Math.floor(box.getMinY()), Math.floor(box.getMinZ()));
		
		Location pos2 = new Location(p.getWorld(), Math.floor(box.getMaxX()), Math.floor(box.getMaxY()), Math.floor(box.getMaxZ()));

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill " + Math.round(pos1.getX()) + " " + Math.round(pos1.getY()) + " " + Math.round(pos1.getZ()) + " " + Math.round(pos2.getX()) + " " + Math.round(pos2.getY()) + " " + Math.round(pos2.getZ()) + " air");
		
	}
	
	public static void setPlayerReach(Player p, float reach) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reach " + reach + " " + p.getUniqueId());
		
	}
	
	public static void resetPlayerSize(Player p) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "size 1 " + p.getUniqueId());
		
		BoundingBox box = new BoundingBox(p.getLocation().getX() - 0.3, p.getLocation().getY(), p.getLocation().getZ() - 0.3, p.getLocation().getX() + 0.3, p.getLocation().getY() + 1.8, p.getLocation().getZ() + 0.3);
		
		Location pos1 = new Location(p.getWorld(), Math.floor(box.getMinX()), Math.floor(box.getMinY()), Math.floor(box.getMinZ()));
		
		Location pos2 = new Location(p.getWorld(), Math.floor(box.getMaxX()), Math.floor(box.getMaxY()), Math.floor(box.getMaxZ()));

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill " + Math.round(pos1.getX()) + " " + Math.round(pos1.getY()) + " " + Math.round(pos1.getZ()) + " " + Math.round(pos2.getX()) + " " + Math.round(pos2.getY()) + " " + Math.round(pos2.getZ()) + " air");
		
	}
	
	public static void resetPlayerReach(Player p) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reach 4.5 " + p.getUniqueId());
		
	}
	
	public static void setPlayerHealth(Player p, double health) {
		
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		
	}
	
	public static void resetPlayerHealth(Player p) {
		
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		
	}
	
}