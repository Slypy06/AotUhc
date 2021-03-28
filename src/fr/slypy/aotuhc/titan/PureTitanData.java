package fr.slypy.aotuhc.titan;

import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import fr.slypy.aotuhc.Skin;

public class PureTitanData {

	float size;
	float reach;
	List<PotionEffect> effects;
	Listener listener;
	Skin skin;
	
	public PureTitanData(float size, float reach, List<PotionEffect> effects, Listener listener, Skin skin) {

		this.size = size;
		this.reach = reach;
		this.effects = effects;
		this.listener = listener;
		this.skin = skin;
		
	}

	public float getSize() {
		
		return size;
		
	}
	
	public void setSize(float size) {
		
		this.size = size;
		
	}
	
	public Skin getSkin() {
		
		return skin;
		
	}


	public void setSkin(Skin skin) {
		
		this.skin = skin;
		
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
	
}
