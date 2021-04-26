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
	private BukkitTask cooldownTask = null;
	private BukkitTask damageTask = null;
	private RoleRunnable transformRunnable;
	private RoleRunnable untransformRunnable;
	public boolean transformed;
	private boolean canTransform = false;
	
	public TitanRole(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, TitanData data) {
		
		super(name, nb, effects, listener, commands, startRun, skin);
		this.data = data;

	}
	
	private TitanRole(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, TitanData data, Player p) {
		
		super(name, nb, effects, listener, commands, startRun, skin);
		this.data = data;
		this.p = p;
		this.implemented = true;
		
		transformRunnable = new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		};
		
		untransformRunnable = new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		};

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
		
		cooldownTask = Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(GameStorage.gameStarted && GameStorage.roles.containsKey(getPlayer().getUniqueId())) {
				
					getPlayer().sendMessage(AotUhc.prefix + "§6Tu peut a nouveau te transformer en titan !");
					
					canTransform = true;
				
				}
				
			}
			
		}, data.getCooldown() * 20);
		
		Player p = this.getPlayer();
		
		p.setInvulnerable(true);
		
		data.getSkin().applySkin(p);
		
		p.getInventory().setItem(8, data.getTitanWeapon());
		
		transformed = true;
		
		TitanDataChanger.setPlayerReach(this.getPlayer(), data.getReach());
		TitanDataChanger.setPlayerSize(this.getPlayer(), data.getSize());
		TitanDataChanger.setPlayerHealth(this.getPlayer(), data.getHealth());
		
		this.affectTitanEffects();
		
		for(Role r : GameStorage.roles.values()) {
			
			Player player = r.getPlayer();			
			
			if(r.getName() == RolesName.MAGATH) {
				
				player.sendMessage(AotUhc.prefix + "§6" + getPlayer().getDisplayName() + " viens de se transformer en titan dans le chunk X : " + getPlayer().getLocation().getChunk().getX() + " Z : " +  getPlayer().getLocation().getChunk().getZ() + " !");
				
			} else {
				
				player.sendMessage(AotUhc.prefix + "§6 Un titan viens de se transformer !");
				
			}
			
		}
		
		autoUntransformTask = Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {

			@Override
			public void run() {
				
				if(p.isOnline() && GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.gameStarted) {
				
					autoUntransform();
				
				}
				
			}
			
		}, data.getTransformTime() * 20);
		
		transformRunnable.run(this);
		
		Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {

			@Override
			public void run() {
				
				p.setInvulnerable(false);
				
			}
			
		}, 20);
		
	}
	
	private void autoUntransform() {
		
		Player p = this.getPlayer();
		
		Skin.resetSkin(p);
		
		p.getInventory().setItem(8, AotUhc.titanFlint);
		
		this.affectEffects();
		
		transformed = false;
		
		TitanDataChanger.resetPlayerReach(this.getPlayer());
		TitanDataChanger.resetPlayerSize(this.getPlayer());
		TitanDataChanger.resetPlayerHealth(this.getPlayer());
		
		untransformRunnable.run(this);
		
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

	public RoleRunnable getTransformRunnable() {
		
		return transformRunnable;
		
	}

	public void setTransformRunnable(RoleRunnable transformRunnable) {
		
		this.transformRunnable = transformRunnable;
		
	}

	public RoleRunnable getUntransformRunnable() {
		
		return untransformRunnable;
		
	}

	public void setUntransformRunnable(RoleRunnable untransformRunnable) {
		
		this.untransformRunnable = untransformRunnable;
		
	}
	
	@Override
	public Role implementPlayer(Player p) {
		
		return new TitanRole(name, nb, effects, listener, commands, startRun, skin, data, p);
		
	}
	
	@Override
	public void startRun() {
		
		cooldownTask = Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(GameStorage.gameStarted && GameStorage.roles.containsKey(getPlayer().getUniqueId())) {
				
					getPlayer().sendMessage(AotUhc.prefix + "§6Tu peut désormais te transformer en titan !");
					
					canTransform = true;
				
				}
				
			}
			
		}, data.getCooldown() * 20);
		
		getStartRun().run(this);
		
	}
	
	public void cleanupTask() {
		
		if(cooldownTask != null) {
		
			cooldownTask.cancel();
		
		}
		
		if(autoUntransformTask != null && !autoUntransformTask.isCancelled()) {
		
			autoUntransformTask.cancel();
		
		}
		
		if(damageTask != null) {
			
			damageTask.cancel();
			
		}
		
	}
 	
}