package fr.slypy.aotuhc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.slypy.aotuhc.recipes.AotCampLockedShapedRecipe;
import fr.slypy.aotuhc.recipes.AotRoleLockedShapedRecipe;
import fr.slypy.aotuhc.recipes.AotShapedRecipe;
import fr.slypy.aotuhc.recipes.RecipeUtils;
import fr.slypy.aotuhc.roles.PureTitanRole;
import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.RolesName;
import fr.slypy.aotuhc.roles.TitanRole;
import net.minecraft.server.v1_16_R3.EntityPlayer;

public class AotListener implements Listener {

	@EventHandler
	public void onProjectileThrown(ProjectileLaunchEvent event) {
		
		if(event.getEntityType() == EntityType.TRIDENT && event.getEntity().getShooter() instanceof Player) {
			
			if(GameStorage.pvp > 0 && !((Player) event.getEntity().getShooter()).isOp()) {
				
				event.setCancelled(true);
				((Player) event.getEntity().getShooter()).sendMessage(AotUhc.prefix + "§cVous ne pouvez pas lancer de lance foudroyante tant que le pvp est désactivé !");
				
			}
			
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
		
		if(event.getCause() == DamageCause.BLOCK_EXPLOSION && event.getEntity() instanceof Player) {

			event.setDamage(event.getDamage() / 2);
			
		}
		
	}
	
	@EventHandler
	public void playerCraft(CraftItemEvent event) {

		boolean notOp = !event.getWhoClicked().isOp();
		
		if(event.getRecipe() != null && event.getRecipe() instanceof CraftShapedRecipe && notOp) {
		
			CraftShapedRecipe cRecipe = (CraftShapedRecipe) event.getRecipe();
			
			if(RecipeUtils.containsRecipe(cRecipe)) {
				
				if(event.getWhoClicked() instanceof Player) {
					
					Player p = (Player) event.getWhoClicked();
					
					if(!GameStorage.gameStarted || !GameStorage.roles.containsKey(p.getUniqueId())) {
						
						event.setCancelled(true);
						
					} else {
					
						Role r = GameStorage.roles.get(p.getUniqueId());
						
						AotShapedRecipe recipe = RecipeUtils.getRecipe(cRecipe);
						
						if(recipe instanceof AotRoleLockedShapedRecipe) {
							
							AotRoleLockedShapedRecipe roleLockedRecipe = (AotRoleLockedShapedRecipe) recipe;
							
							if(!roleLockedRecipe.canCraft(r)) {
								
								event.setCancelled(true);
								
							}
							
						} else if(recipe instanceof AotCampLockedShapedRecipe) {
							
							AotCampLockedShapedRecipe campLockedRecipe = (AotCampLockedShapedRecipe) recipe;
							
							if(!campLockedRecipe.canCraft(r)) {
								
								event.setCancelled(true);
								
							}
							
						}
					
					}
					
				}
				
			}
		
		}
		
	}
	
	@EventHandler
	public void playerCraftFireCharge(CraftItemEvent event) {
		
		if(event.getRecipe() != null && event.getRecipe() instanceof CraftShapelessRecipe) {
		
			CraftShapelessRecipe cRecipe = (CraftShapelessRecipe) event.getRecipe();
			
			if(cRecipe.getResult().getType() == Material.FIRE_CHARGE) {
				
				event.setCancelled(true);
				
			}
		
		}
		
	}
	
	@EventHandler
	public void playerBite(PlayerInteractEvent event) {
				
		if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getPlayer().isSneaking() && CraftItemStack.asNMSCopy(event.getItem()).getOrCreateTag().hasKey("aot_titan") && GameStorage.gameStarted && GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && (GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole || GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof PureTitanRole)) {
						
			if(GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole) {
			
				TitanRole role = (TitanRole) GameStorage.roles.get(event.getPlayer().getUniqueId());
				
				if(!role.isTransformed()) {
					
					role.transform();
					
				}
			
			} else {
				
				PureTitanRole role = (PureTitanRole) GameStorage.roles.get(event.getPlayer().getUniqueId());
				
				if(!role.isTransformed()) {
					
					role.transform();
				
				}
				
			}
			
		}
		
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
		
		if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().isSneaking() && CraftItemStack.asNMSCopy(event.getItem()).getOrCreateTag().hasKey("aot_titan") && GameStorage.gameStarted && GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole && event.getPlayer().getLocation().getPitch() >= 75) {
						
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
		
		if(GameStorage.gameStarted) {
			
			event.getPlayer().kickPlayer("Game already started !");
			
			return;
			
		}
		
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
			
			RolesName name = GameStorage.roles.get(event.getPlayer().getUniqueId()).getName();
			
			if(GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof TitanRole) {
				
				((TitanRole) GameStorage.roles.get(event.getPlayer().getUniqueId())).destroy();
				
			}
			
			if(GameStorage.roles.get(event.getPlayer().getUniqueId()) instanceof PureTitanRole) {
				
				((PureTitanRole) GameStorage.roles.get(event.getPlayer().getUniqueId())).destroy();
				
			}
			
			GameStorage.roles.remove(event.getPlayer().getUniqueId());
			
			Location deathLoc = event.getPlayer().getLocation();
			
			Inventory playerInv = event.getPlayer().getInventory();
			
			event.getPlayer().getWorld().strikeLightningEffect(deathLoc);
			
			for(ItemStack stack : playerInv.getContents()) {
				
				if(stack != null) {
				
					event.getPlayer().getWorld().dropItemNaturally(deathLoc, stack);
				
				}
				
			}
			
			for(Role r : GameStorage.roles.values()) {
				
				r.getPlayer().sendMessage(AotUhc.prefix + "§a" + name + " §6(§c" + event.getPlayer().getName() + "§6) disconnected !");
				
			}
			
			event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			
			AotUhc.checkForGameEnd();
			
		}
		
	}
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent event) {

		if(GameStorage.gameStarted && GameStorage.roles.containsKey(event.getEntity().getUniqueId())) {

			Player p = event.getEntity();
			Role r = GameStorage.roles.get(p.getUniqueId());
			
			Skin.resetSkin(p);
			
			if(r instanceof TitanRole) {
				
				((TitanRole) r).destroy();
				
			}
			
			if(r instanceof PureTitanRole) {
				
				((PureTitanRole) r).destroy();
				
			}
			
			GameStorage.roles.remove(p.getUniqueId());
			
			p.getWorld().strikeLightningEffect(p.getLocation());
			
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			
			AotUhc.checkForGameEnd();
			
			if(p.getKiller() != null && p.getKiller() instanceof Player && GameStorage.rolesBackup.containsKey(((Player) p.getKiller()).getUniqueId())) {
				
				Player k = p.getKiller();
				
				GameStorage.kills.put(k.getUniqueId(), GameStorage.kills.get(k.getUniqueId()) + 1);
				
				Role kr = GameStorage.rolesBackup.get(k.getUniqueId());
				
				Bukkit.broadcastMessage(AotUhc.prefix + "§a" + r.getName() + " §6(§c" + p.getName() + "§6) a été tué par §a" + kr.getName() + (kr.isReveal() ? " (§c" + k.getName() + "§6) " : " §6") + "!");
				
			} else {
				
				Bukkit.broadcastMessage(AotUhc.prefix + "§a" + r.getName() + " §6(§c" + p.getName() + "§6) est mort !");
				
			}
			
		}
		
	}
	
	@EventHandler
	public void entityHit(EntityDamageByEntityEvent event) {
		
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			if(GameStorage.pvp > 0) {
				
				event.setCancelled(true);
				
			}
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void fallDamage(EntityDamageEvent event) {
		
		try {
		
			if(event.getEntity() instanceof Player && event.getCause().equals(DamageCause.FALL)) {
				
				int gravity = ((Player) event.getEntity()).getScoreboard().getObjective("gravity").getScore((Player) event.getEntity()).getScore();
				
				System.out.println(gravity);
				
				if(event.getDamage() <= gravity) {
					
					event.setCancelled(true);
					
				} else {
					
					event.setDamage(event.getDamage() - gravity);
					
				}
				
			}
		
		} catch (NullPointerException e) {
			
			
			
		}
		
	}
	
	@EventHandler
	public void changeMainHandItem(PlayerItemHeldEvent event) {
		
		if(event.getPlayer().getInventory().getItem(event.getNewSlot()) != null && (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().equals("§8§lUltra Hard Steel Sword") || event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().equals("§7§lHard Steel Sword"))) {
			
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 2));
			
		} else if(event.getPlayer().getInventory().getItem(event.getPreviousSlot()) != null && (event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getItemMeta().getDisplayName().equals("§8§lUltra Hard Steel Sword") || event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getItemMeta().getDisplayName().equals("§7§lHard Steel Sword"))) {
			
			event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
			
		}
		
	}
	
}