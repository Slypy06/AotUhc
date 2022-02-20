package fr.slypy.aotuhc.roles;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
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
		
		transformRunnable = new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		};
		
		untransformRunnable = new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		};

	}
	
	private TitanRole(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, TitanData data, Player p, RoleRunnable transformRunnable, RoleRunnable untransformRunnable) {
		
		super(name, nb, effects, listener, commands, startRun, skin);
		this.data = data;
		this.p = p;
		this.implemented = true;
		this.transformRunnable = transformRunnable;
		this.untransformRunnable = untransformRunnable;

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
		
		for(int i = 0; i < 20; i++) {
			
			p.getWorld().strikeLightningEffect(p.getLocation());

		}
		
		p.getWorld().createExplosion(p.getLocation(), getData().getSize());
		
		data.getSkin().applySkin(p);
		
		p.getInventory().setItem(8, data.getTitanWeapon());
		
		transformed = true;
		
		TitanDataChanger.setPlayerReach(this.getPlayer(), data.getReach());
		TitanDataChanger.setPlayerSize(this.getPlayer(), data.getSize());
		TitanDataChanger.setPlayerHealth(this.getPlayer(), data.getHealth());
		
		if(!isReveal()) {
			
			p.setDisplayName(getName().toString());
			p.setCustomName(getName().toString());
			p.setPlayerListName(getName().toString());
			
		}
		
		this.affectTitanEffects();
		
		for(Role r : GameStorage.roles.values()) {
			
			Player player = r.getPlayer();			
			
			if(r.getName() == RolesName.MAGATH) {
				
				player.sendMessage(AotUhc.prefix + "§6" + getName().name() + " viens de se transformer en titan dans le chunk X : " + getPlayer().getLocation().getChunk().getX() + " Z : " +  getPlayer().getLocation().getChunk().getZ() + " !");
				
			} else {
				
				player.sendMessage(AotUhc.prefix + "§6 Un titan viens de se transformer !");
				
			}
			
		}
		
		damageTask = Bukkit.getScheduler().runTaskTimer(AotUhc.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("damage");
				
		        for(LivingEntity r : p.getWorld().getLivingEntities()) {

		            if(transformed) {

		                double size = r.getHeight();
		                double titanSize = data.getSize();

		                if(size <= titanSize / 3.0D) {

		                    Location loc = r.getLocation();
		                    Location titanLoc = p.getLocation();

		                    if(loc.getX() >= titanLoc.getX() - (((data.getSize() * 0.6D) / 2.0D) + 1) && loc.getX() <= titanLoc.getX() + (((data.getSize() * 0.6D) / 2.0D) + 1) && loc.getZ() >= titanLoc.getZ() - (((data.getSize() * 0.6D) / 2.0D) + 1) && loc.getZ() <= titanLoc.getZ() + (((data.getSize() * 0.6D) / 2.0D) + 1) && loc.getY() >= titanLoc.getY() - (r instanceof TitanRole ? ((TitanRole) r).getData().getSize() : r instanceof PureTitanRole ? ((PureTitanRole) r).getData().getSize() : 1.8D) && loc.getY() <= titanLoc.getY() + data.getSize() / 4.0D) {

		                        r.damage(((int) (titanSize / size)) / 2);

		                    }

		                }

		            }

		        }	
				
			}
			
		}, 0, 20);
		
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
		
		if(!isReveal()) {
			
			p.setDisplayName(p.getName());
			p.setCustomName(p.getName());
			p.setPlayerListName(p.getName());
			
		}
		
		damageTask.cancel();
		
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
	
	public void destroy() {
		
		if(!this.isImplemented()) {
			
			return;
			
		}
		
		if(isTransformed()) {
			
			removeTitanEffects();
			TitanDataChanger.resetPlayerHealth(p);
			TitanDataChanger.resetPlayerReach(p);
			TitanDataChanger.resetPlayerSize(p);
			Skin.resetSkin(p);
			autoUntransformTask.cancel();
			p.setDisplayName(p.getName());
			p.setCustomName(p.getName());
			p.setPlayerListName(p.getName());
			
		}
		
		if(cooldownTask != null) {
			
			cooldownTask.cancel();
		
		}
		
		if(damageTask != null) {
			
			damageTask.cancel();
			
		}
		
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
		
		return new TitanRole(name, nb, effects, listener, commands, startRun, skin, data, p, transformRunnable, untransformRunnable);
		
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

	public void cancelCooldown() {
		
		if(cooldownTask != null) {
			
			cooldownTask.cancel();
		
		}
		
		if(GameStorage.gameStarted && GameStorage.roles.containsKey(getPlayer().getUniqueId())) {
			
			getPlayer().sendMessage(AotUhc.prefix + "§6Tu peut a nouveau te transformer en titan !");
			
			canTransform = true;
		
		}
		
	}
 	
}