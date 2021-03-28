package fr.slypy.aotuhc.titan;

import java.util.List;

import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.slypy.aotuhc.Skin;

public class TitanData {

	float size;
	float reach;
	int transformTime;
	List<PotionEffect> effects;
	Listener listener;
	Skin skin;
	int cooldown;
	ItemStack titanWeapon;
	
	public TitanData(float size, float reach, List<PotionEffect> effects, Listener listener, int transformTime, int cooldown, ItemStack titanWeapon, Skin skin) {

		this.size = size;
		this.reach = reach;
		this.effects = effects;
		this.listener = listener;
		this.transformTime = transformTime;
		this.cooldown = cooldown;
		this.titanWeapon = TitanData.addTitanTagToItem(titanWeapon);
		this.skin = skin;
		
	}
	
	public Skin getSkin() {
		
		return skin;
		
	}


	public void setSkin(Skin skin) {
		
		this.skin = skin;
		
	}


	public ItemStack getTitanWeapon() {
		
		return titanWeapon;
		
	}


	public void setTitanWeapon(ItemStack titanWeapon) {
		
		this.titanWeapon = titanWeapon;
		
	}


	public float getSize() {
		
		return size;
		
	}
	
	public void setSize(float size) {
		
		this.size = size;
		
	}

	public int getCooldown() {
		
		return cooldown;
		
	}
	
	public void setCooldown(int cooldown) {
		
		this.cooldown = cooldown;
		
	}
	
	public float getReach() {
		
		return reach;
		
	}
	
	public void setReach(float reach) {
		
		this.reach = reach;
		
	}
	
	public List<PotionEffect> getEffects() {
		
		return effects;
		
	}
	
	public void setEffects(List<PotionEffect> effects) {
		
		this.effects = effects;
		
	}
	
	public Listener getListener() {
		
		return listener;
		
	}
	
	public void setListener(Listener listener) {
		
		this.listener = listener;
		
	}
	
	public int getTransformTime() {
		
		return transformTime;
		
	}
	
	public void setTransformTime(int transformTime) {
		
		this.transformTime = transformTime;
		
	}
	
	public static ItemStack addTitanTagToItem(ItemStack stack) {
		
		net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
		nmsStack.getTag().setInt("aot_titan", 1);
		return CraftItemStack.asBukkitCopy(nmsStack);
		
	}
	
}