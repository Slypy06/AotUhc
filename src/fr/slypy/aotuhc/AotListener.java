package fr.slypy.aotuhc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftShapelessRecipe;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;

import fr.slypy.aotuhc.recipes.RecipeUtils;
import fr.slypy.aotuhc.roles.PureTitanRole;
import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.Roles;
import fr.slypy.aotuhc.roles.RolesName;
import fr.slypy.aotuhc.roles.TitanRole;

public class AotListener implements Listener {

	@EventHandler
	public void onProjectileThrown(ProjectileLaunchEvent event) {
		
		if(event.getEntityType() == EntityType.TRIDENT) {
			
			Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {
				
				Trident spear = (Trident) event.getEntity();
				
				@Override
				public void run() {
					
					if(spear.isValid() && !spear.isDead()) {
						
						spear.getWorld().createExplosion(spear.getLocation(), 3.5F, true);
						spear.remove();
						
					}
					
				}
				
			}, 60);
			
		}
		
	}
	
	@EventHandler
	public void projectileHit(ProjectileHitEvent event) {
		
		if(event.getEntityType() == EntityType.TRIDENT && event.getHitEntity() != null) {
			
			event.getEntity().remove();
			
			Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {
				
				Entity victim = (Entity) event.getHitEntity();
				
				@Override
				public void run() {
					
					if(victim.isValid() && !victim.isDead()) {
							
						victim.getWorld().createExplosion(new Location(victim.getWorld(), victim.getLocation().getX(), victim.getLocation().getY() + (victim.getBoundingBox().getHeight() / 2), victim.getLocation().getZ()), 3.5F, true);
						
					}
					
				}
				
			}, 50);
			
		}
		
	}
	
	@EventHandler
	public void entityExplode(EntityDamageEvent event) {
		
		if(event.getCause() == DamageCause.BLOCK_EXPLOSION) {
			
			System.out.println(event.getDamage());
			
			event.setDamage(event.getDamage() / 4);
			
			System.out.println(event.getDamage());
			
		}
		
	}
	
	@EventHandler
	public void playerCraft(CraftItemEvent event) {

		boolean not = false;
		
		if(event.getRecipe() != null && event.getRecipe() instanceof CraftShapedRecipe && not) { //remove "&& not" at the final version
		
			CraftShapedRecipe cRecipe = (CraftShapedRecipe) event.getRecipe();
			
			if(RecipeUtils.containsRecipe(cRecipe)) {
				
				if(event.getWhoClicked() instanceof Player) {
					
					Player p = (Player) event.getWhoClicked();
					
					if(!GameStorage.gameStarted || !GameStorage.roles.containsKey(p.getUniqueId()) || (GameStorage.roles.get(p.getUniqueId()).getName() != RecipeUtils.getRole(cRecipe) && RecipeUtils.getRole(cRecipe) != null)) {
						
						event.setCancelled(true);
						
					}
					
				}
				
			}
		
		}
		
	}
	
	@EventHandler
	public void playerCraftFireCharge(CraftItemEvent event) {
		
		if(event.getRecipe() != null && event.getRecipe() instanceof CraftShapelessRecipe) { //remove "&& not" at the final version
		
			CraftShapelessRecipe cRecipe = (CraftShapelessRecipe) event.getRecipe();
			
			if(cRecipe.getResult().getType() == Material.FIRE_CHARGE) {
				
				event.setCancelled(true);
				
			}
		
		}
		
	}
	
	@EventHandler
	public void playerBite(PlayerInteractEvent event) {
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getPlayer().isSneaking() && CraftItemStack.asNMSCopy(event.getItem()).getOrCreateTag().hasKey("aot_titan")) {
		
			event.getPlayer().setInvulnerable(true);
			
			for(int i = 0; i < 20; i++) {
				
				event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
	
			}
			
			event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), 15);
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "size set " + (Roles.eren.getData().getSize() / 1.8) + " " + event.getPlayer().getUniqueId());
		
			Skin.assailant.applySkin(event.getPlayer());
			
			event.getPlayer().setInvulnerable(false);
			
			for(PotionEffect effect : Roles.eren.getData().getEffects()) {
				
				event.getPlayer().addPotionEffect(new PotionEffect(effect.getType(), 60 * 20, effect.getAmplifier()));
				
			}
			
		}
		
		/*if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getPlayer().isSneaking() && CraftItemStack.asNMSCopy(event.getItem()).getOrCreateTag().hasKey("aot_titan") && GameStorage.gameStarted && GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && (GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole || GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof PureTitanRole)) {
						
			if(GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole) {
			
				TitanRole role = (TitanRole) GameStorage.roles.get(event.getPlayer().getUniqueId());
				
				if(!role.isTransformed()) {
					
					for(int i = 0; i < 20; i++) {
					
						event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
		
					}
					
					event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), role.getData().getSize());
					
					role.transform();
				
				}
			
			} else {
				
				PureTitanRole role = (PureTitanRole) GameStorage.roles.get(event.getPlayer().getUniqueId());
				
				if(!role.isTransformed()) {
					
					for(int i = 0; i < 20; i++) {
					
						event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
		
					}
					
					role.transform();
				
				}
				
			}
			
		}*/
		
	}
	
	@EventHandler
	public void playerSwapItem(PlayerSwapHandItemsEvent event) {
		
		if(CraftItemStack.asNMSCopy(event.getOffHandItem()).getOrCreateTag().hasKey("aot_titan") && GameStorage.gameStarted && GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && (GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole || GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof PureTitanRole)) {
			
			event.setCancelled(true);
			
		}
		
	}
	
	@EventHandler
	public void playerClickInInventory(InventoryClickEvent event) {
		
		if(event.getWhoClicked() instanceof Player) {
			
			Player p = (Player) event.getWhoClicked();
			
			if(CraftItemStack.asNMSCopy(event.getCurrentItem()).getOrCreateTag().hasKey("aot_titan") && GameStorage.gameStarted && GameStorage.roles.containsKey(p.getUniqueId()) && (GameStorage.roles.get(p.getUniqueId()) instanceof TitanRole || GameStorage.roles.get(p.getUniqueId()) instanceof PureTitanRole)) {
				
				event.setCancelled(true);
				
			}
			
		}
		
	}
	
	@EventHandler
	public void playerDropEvent(PlayerDropItemEvent event) {
		
		if(CraftItemStack.asNMSCopy(event.getItemDrop().getItemStack()).getOrCreateTag().hasKey("aot_titan") && event.getPlayer().getInventory().getHeldItemSlot() == 8 && GameStorage.gameStarted && GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && (GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole || GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof PureTitanRole)) {
			
			event.setCancelled(true);
			
		}
		
	}
	
	@EventHandler
	public void playerUntransform(PlayerInteractEvent event) {
		
		if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) && event.getPlayer().isSneaking() && CraftItemStack.asNMSCopy(event.getItem()).getOrCreateTag().hasKey("aot_titan") && GameStorage.gameStarted && GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole && event.getPlayer().getLocation().getPitch() >= 75) {
						
			TitanRole role = (TitanRole) GameStorage.roles.get(event.getPlayer().getUniqueId());
			
			if(role.isTransformed()) {
				
				event.getPlayer().getWorld().spawnParticle(Particle.END_ROD, event.getPlayer().getEyeLocation(), 100);
				
				role.untransform();
				
			}
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void projectilePickup(PlayerPickupArrowEvent event) {

		if(event.getArrow().getType().equals(EntityType.TRIDENT)) {
			
            Trident trident = (Trident) event.getArrow();
                
            if(trident.getShooter() instanceof Player) {
            	
            	event.setCancelled(true);
                
            }
            
        }
		
	}
	
	@EventHandler
	public void projectileShoot(ProjectileLaunchEvent event) {
		
		if(event.getEntityType() == EntityType.ARROW) {
			
			Arrow arrow = (Arrow) event.getEntity();
			
			if(arrow.getShooter() instanceof Player) {
				
				Player player = (Player) arrow.getShooter();
				
				if(GameStorage.gameStarted && GameStorage.roles.containsKey(player.getUniqueId()) && GameStorage.roles.get(player.getUniqueId()).getName() == RolesName.ZEKE) {
					
					TitanRole role = (TitanRole) GameStorage.roles.get(player.getUniqueId());
					
					if(role.isTransformed()) {
						
						arrow.setDamage(15);
						arrow.setVelocity(arrow.getVelocity().multiply(5));
						
					}
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		
		Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {

			@Override
			public void run() {
				
				event.getPlayer().setResourcePack("http://enzo.m.7.free.fr/aotuhc/data/resourcepacks/snk.zip");
				Skin.resetSkin(event.getPlayer());
				
			}
			
		}, 60);
		
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent event) {
		
		if(GameStorage.gameStarted && GameStorage.roles.containsKey(event.getPlayer().getUniqueId())) {
			
			GameStorage.roles.remove(event.getPlayer().getUniqueId());
			
		}
		
	}
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent event) {
		
		if(GameStorage.gameStarted && GameStorage.roles.containsKey(event.getEntity().getUniqueId())) {
			
			Player p = event.getEntity();
			Role r = GameStorage.roles.get(p.getUniqueId());
			
			GameStorage.roles.remove(p.getUniqueId());
			
			p.getWorld().strikeLightningEffect(p.getLocation());
			
			AotUhc.checkForGameEnd();
			
			if(p.getKiller() != null && p.getKiller() instanceof Player && GameStorage.rolesBackup.containsKey(((Player) p.getKiller()).getUniqueId())) {
				
				Player k = p.getKiller();
				
				Role kr = GameStorage.rolesBackup.get(k.getUniqueId());
				
				Bukkit.broadcastMessage(AotUhc.prefix + "§a" + r.getName() + " §6(§c" + p.getName() + "§6) a été tué par §a" + kr.getName() + (kr.isReveal() ? " (§c" + k.getName() + "§6) " : " §6") + "!");
				
			}	
			
		}
		
	}
	
}