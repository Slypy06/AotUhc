package fr.slypy.aotuhc.roles;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.GameStorage;
import fr.slypy.aotuhc.Skin;
import fr.slypy.aotuhc.commands.Command;
import fr.slypy.aotuhc.titan.TitanData;
import fr.slypy.aotuhc.titan.TitanDataChanger;

public class TitanRole extends Role {

	public TitanData data;
	private BukkitTask autoUntransformTask = null;
	public boolean transformed;
	private boolean canTransform = true;
	
	public TitanRole(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, TitanData data) {
		
		super(name, nb, effects, listener, commands, startRun, skin);
		this.data = data;

	}
	
	public void transform() {
		
		if(!this.isImplemented() || transformed) {
			
			return;
			
		}
		
		if(!canTransform) {
			
			this.getPlayer().sendMessage(AotUhc.prefix + "§cTu ne peut pas te transformer en titan maintenant !");
			
			return;
			
		}
		
		canTransform = false;
		
		Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(GameStorage.gameStarted && GameStorage.roles.containsKey(getPlayer().getUniqueId())) {
				
					getPlayer().sendMessage(AotUhc.prefix + "§6Tu peut a nouveau te transformer en titan !");
					
					canTransform = true;
				
				}
				
			}
			
		}, data.getCooldown() * 20);
		
		Player p = this.getPlayer();
		
		data.getSkin().applySkin(p);
		
		p.getInventory().setItem(8, data.getTitanWeapon());
		
		transformed = true;
		
		TitanDataChanger.setPlayerReach(this.getPlayer(), data.getReach());
		TitanDataChanger.setPlayerSize(this.getPlayer(), data.getSize());
		
		this.affectTitanEffects();
		
		autoUntransformTask = Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {

			@Override
			public void run() {
				
				if(p.isOnline() && GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.gameStarted) {
				
					autoUntransform();
				
				}
				
			}
			
		}, data.getTransformTime() * 20);
		
	}
	
	private void autoUntransform() {
		
		Player p = this.getPlayer();
		
		Skin.resetSkin(p);
		
		p.getInventory().setItem(8, AotUhc.titanFlint);
		
		this.affectEffects();
		
		transformed = false;
		
		TitanDataChanger.resetPlayerReach(this.getPlayer());
		TitanDataChanger.resetPlayerSize(this.getPlayer());
		
	}
	
	public void untransform() {
		
		if(!this.isImplemented() || !transformed) {
			
			return;
			
		}
		
		autoUntransformTask.cancel();
		this.removeTitanEffects();
		this.affectEffects();
		autoUntransform();
		
	}

	public TitanData getData() {
		
		return data;
		
	}

	public void setData(TitanData data) {
		
		this.data = data;
		
	}

	public boolean isTransformed() {
		
		return transformed;
		
	}

	public void affectTitanEffects() {
		
		if(!this.isImplemented()) {
			
			return;
			
		}
		
		data.getEffects().forEach(potion -> this.getPlayer().addPotionEffect(new PotionEffect(potion.getType(), data.getTransformTime() * 20, potion.getAmplifier(), false, false)));
		
	}
	
	public void removeTitanEffects() {
		
		if(!this.isImplemented()) {
			
			return;
			
		}
		
		data.getEffects().forEach(potion -> this.getPlayer().removePotionEffect(potion.getType()));
		
	}
	
}