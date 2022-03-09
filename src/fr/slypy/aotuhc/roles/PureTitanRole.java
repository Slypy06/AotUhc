package fr.slypy.aotuhc.roles;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.GameStorage;
import fr.slypy.aotuhc.Skin;
import fr.slypy.aotuhc.commands.Command;
import fr.slypy.aotuhc.titan.PureTitanData;
import fr.slypy.aotuhc.titan.TitanDataChanger;

public class PureTitanRole extends Role {

	public PureTitanData data;
	public boolean transformed;
	private RoleRunnable transformRunnable;
	private BukkitTask cooldownTask = null;
	private BukkitTask damageTask = null;
	private boolean canTransform = false;
	
	public PureTitanRole(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, PureTitanData data) {
		
		super(name, nb, effects, listener, commands, startRun, skin);
		this.data = data;
		
		transformRunnable = new RoleRunnable() {
			
			@Override
			public void run(Role role) {}
			
		};

	}
	
	private PureTitanRole(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, PureTitanData data, Player p, RoleRunnable transformRunnable) {
		
		super(name, nb, effects, listener, commands, startRun, skin);
		this.data = data;
		this.p = p;
		this.implemented = true;
		
		this.transformRunnable = transformRunnable;

	}
	
	public void transform() {
		
		if(!this.isImplemented() || transformed) {
			
			return;
			
		}
		
		if(!canTransform) {
			
			this.getPlayer().sendMessage(AotUhc.prefix + "§cTu ne peut pas te transformer en titan maintenant !");
			
			return;
			
		}
		
		p.setInvulnerable(true);
		
		for(int i = 0; i < 20; i++) {
			
			p.getWorld().strikeLightningEffect(p.getLocation());

		}
		
		p.getWorld().createExplosion(p.getLocation(), getData().getSize());
		
		this.getPlayer().getInventory().setItem(8, null);
		
		data.getSkin().applySkin(this.getPlayer());
		
		transformed = true;
		
		TitanDataChanger.setPlayerReach(this.getPlayer(), data.getReach());
		TitanDataChanger.setPlayerSize(this.getPlayer(), data.getSize());
		TitanDataChanger.setPlayerHealth(this.getPlayer(), data.getHealth());
		
		this.affectTitanEffects();
		
		if(!isReveal()) {
			
			p.setDisplayName(getName().name());
			p.setCustomName(getName().name());
			p.setPlayerListName(getName().name());
			
		}
 		
		for(Role r : GameStorage.roles.values()) {
			
			Player player = r.getPlayer();			
			
			if(r.getName() == RolesName.MAGATH) {
				
				player.sendMessage(AotUhc.prefix + "§6" + getName().name() + " viens de se transformer en titan dans le chunk X : " + getPlayer().getLocation().getChunk().getX() + " Z : " +  getPlayer().getLocation().getChunk().getZ() + " !");
				
			} else {
				
				player.sendMessage(AotUhc.prefix + "§6 Un titan viens de se transformer !");
				
			}
			
		}
		
		transformRunnable.run(this);
		
		Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {

			@Override
			public void run() {
				
				p.setInvulnerable(false);
				
			}
			
		}, 20);
		
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
	
	@Override
	public Role implementPlayer(Player p) {
		
		return new PureTitanRole(name, nb, effects, listener, commands, startRun, skin, data, p, transformRunnable);
		
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
			
		}, 300 * 20);
		
		damageTask = Bukkit.getScheduler().runTaskTimer(AotUhc.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("damage");
				
		        for(LivingEntity r : p.getWorld().getLivingEntities()) {

		            if(transformed && !r.getUniqueId().equals(p.getUniqueId())) {

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
		
		getStartRun().run(this);
		
	}
	
	public void cleanupTask() {
		
		if(cooldownTask != null) {
		
			cooldownTask.cancel();
		
		}
		
		if(damageTask != null) {
			
			cooldownTask.cancel();
		
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