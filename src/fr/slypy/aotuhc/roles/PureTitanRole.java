package fr.slypy.aotuhc.roles;

import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import fr.slypy.aotuhc.Skin;
import fr.slypy.aotuhc.commands.Command;
import fr.slypy.aotuhc.titan.PureTitanData;
import fr.slypy.aotuhc.titan.TitanDataChanger;

public class PureTitanRole extends Role {

	public PureTitanData data;
	public boolean transformed;
	private RoleRunnable transformRunnable;
	
	public PureTitanRole(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, PureTitanData data) {
		
		super(name, nb, effects, listener, commands, startRun, skin);
		this.data = data;
		
		transformRunnable = new RoleRunnable() {
			
			@Override
			public void run(Role role) {}
			
		};

	}
	
	public void transform() {
		
		if(!this.isImplemented() || transformed) {
			
			return;
			
		}
		
		this.getPlayer().getInventory().setItem(8, null);
		
		data.getSkin().applySkin(this.getPlayer());
		
		transformed = true;
		
		TitanDataChanger.setPlayerReach(this.getPlayer(), data.getReach());
		TitanDataChanger.setPlayerSize(this.getPlayer(), data.getSize());
		TitanDataChanger.setPlayerHealth(this.getPlayer(), data.getHealth());
		
		this.affectTitanEffects();
		
		transformRunnable.run(this);
		
	}

	public RoleRunnable getTransformRunnable() {
		
		return transformRunnable;
		
	}

	public void setTransformRunnable(RoleRunnable transformRunnable) {
		
		this.transformRunnable = transformRunnable;
		
	}

	public PureTitanData getData() {
		
		return data;
		
	}

	public void setData(PureTitanData data) {
		
		this.data = data;
		
	}

	public boolean isTransformed() {
		
		return transformed;
		
	}

	public void affectTitanEffects() {
		
		if(!this.isImplemented()) {
			
			return;
			
		}
		
		data.getEffects().forEach(potion -> this.getPlayer().addPotionEffect(new PotionEffect(potion.getType(), Integer.MAX_VALUE, potion.getAmplifier(), false, false)));
		
	}
	
	public void removeTitanEffects() {
		
		if(!this.isImplemented()) {
			
			return;
			
		}
		
		data.getEffects().forEach(potion -> this.getPlayer().removePotionEffect(potion.getType()));
		
	}

}
