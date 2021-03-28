package fr.slypy.aotuhc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Skin {

	public static String skinsStorageURL = "http://enzo.m.7.free.fr/aotuhc/data/skins/";
	
	public static Skin erwin = new Skin(Skin.skinsStorageURL + "eldia/erwin.png");
	public static Skin eren = new Skin(Skin.skinsStorageURL + "eldia/eren.png");
	public static Skin mikasa = new Skin(Skin.skinsStorageURL + "eldia/mikasa.png");
	public static Skin livai = new Skin(Skin.skinsStorageURL + "eldia/livai.png");
	public static Skin historia = new Skin(Skin.skinsStorageURL + "eldia/historia.png");
	public static Skin armin = new Skin(Skin.skinsStorageURL + "eldia/armin.png");
	public static Skin hansi = new Skin(Skin.skinsStorageURL + "eldia/hansi.png");
	public static Skin sasha = new Skin(Skin.skinsStorageURL + "eldia/sasha.png");
	public static Skin conny = new Skin(Skin.skinsStorageURL + "eldia/conny.png");
	public static Skin jean = new Skin(Skin.skinsStorageURL + "eldia/jean.png");
	public static Skin soldier = new Skin(Skin.skinsStorageURL + "eldia/soldier.png");
	
	public static Skin zeke = new Skin(Skin.skinsStorageURL + "eldia/zeke.png");
	public static Skin pieck = new Skin(Skin.skinsStorageURL + "eldia/pieck.png");
	public static Skin lara = new Skin(Skin.skinsStorageURL + "eldia/lara.png");
	
	public static Skin assailant = new Skin(Skin.skinsStorageURL + "assailant.png");
	public static Skin beast = new Skin(Skin.skinsStorageURL + "beast.png");
	public static Skin cart = new Skin(Skin.skinsStorageURL + "cart.png");
	public static Skin warhammer = new Skin(Skin.skinsStorageURL + "warhammer.png");
	
	String skinPath;
	
	public Skin(String skinPath) {

		this.skinPath = skinPath;
		
	}

	public String getSkinPath() {
		
		return skinPath;
		
	}

	public void setSkinPath(String skinPath) {
		
		this.skinPath = skinPath;
		
	}

	public void applySkin(Player p) {
		
		System.out.println("skin player " + p.getUniqueId() + " set " + skinPath);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin player " + p.getUniqueId() + " set " + skinPath);
		
	}
	
	public static void resetSkin(Player p) {
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin player " + p.getUniqueId() + " clear");
		
	}
	
}
