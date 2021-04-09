package fr.slypy.aotuhc.titan;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class TitanDataChanger {
	
	public static void setPlayerSize(Player p, float size) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "size " + (size / 1.8) + " " + p.getUniqueId());
		
	}
	
	public static void setPlayerReach(Player p, float reach) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reach " + reach + " " + p.getUniqueId());
		
	}
	
	public static void resetPlayerSize(Player p) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "size 1 " + p.getUniqueId());
		
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