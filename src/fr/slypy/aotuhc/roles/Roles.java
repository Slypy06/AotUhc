package fr.slypy.aotuhc.roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.AxePowerAlly;
import fr.slypy.aotuhc.GameStorage;
import fr.slypy.aotuhc.LimitedUsesListener;
import fr.slypy.aotuhc.Skin;
import fr.slypy.aotuhc.api.FancyMessage;
import fr.slypy.aotuhc.commands.Command;
import fr.slypy.aotuhc.titan.PureTitanData;
import fr.slypy.aotuhc.titan.TitanData;

public class Roles {
	
	public static Role erwin;
	public static TitanRole eren;
	public static Role mikasa;
	public static Role livai;
	public static Role historia;
	public static Role armin;
	public static Role hansi;
	public static Role sasha;
	public static Role conny;
	public static Role jean;
	public static Role soldier;
	
	public static TitanRole zeke;
	public static TitanRole pieck;
	public static TitanRole lara;
	public static TitanRole reiner;
	public static TitanRole annie;
	public static TitanRole bertholdt;
	public static TitanRole porco;
	public static Role falco;
	public static Role gaby;
	public static Role magath;
	
	public static PureTitanRole smiling;
	public static PureTitanRole deviant;
	public static PureTitanRole small;
	public static PureTitanRole medium;
	public static PureTitanRole great;
	
	public static void initRoles() {
		
		erwin = new Role(RolesName.ERWIN, AotUhc.config.isInt("erwin.nb") ? AotUhc.config.getInt("erwin.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(new Command(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
				
				if(sender instanceof Player) {
					
					Player p = (Player) sender;
					
					if(GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.ERWIN && args.length == 1) {
						
						Player player = Bukkit.getPlayer(args[0]);
						
						if(player != null && GameStorage.roles.containsKey(player.getUniqueId())) {
							
							Role role = GameStorage.roles.get(player.getUniqueId());
							role.reveal();
							GameStorage.roles.get(p.getUniqueId()).reveal();
							Bukkit.broadcastMessage(AotUhc.prefix + "§cErwin §6(§2" + p.getName() + "§6) a découvert que §2" + args[0] + "§6 est §c" + role.getName() + "§6 !");

							return true;
							
						}
						
					}
					
				}
				
				return false;
				
			}
			
		}, new TabCompleter() {
			
			@Override
			public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {

				return null;
				
			}
			
		}, "inspect", 2)), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.erwin);
		
		
		ItemStack emptyWeapon = new ItemStack(Material.FLINT);
		ItemMeta emptyWeaponMeta = emptyWeapon.getItemMeta();
		emptyWeaponMeta.setCustomModelData(10002);
		emptyWeapon.setItemMeta(emptyWeaponMeta);
		
		eren = new TitanRole(RolesName.EREN, AotUhc.config.isInt("eren.nb") ? AotUhc.config.getInt("eren.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new Listener() {
			
			boolean used = false;
			
			@EventHandler
			public void onPlayerDeath(PlayerDeathEvent event) {
				
				if(GameStorage.gameStarted) {
					
					Player p = event.getEntity();
					
					if(AxePowerAlly.target != null && AxePowerAlly.playersStorage != null && AxePowerAlly.players != null && p.getUniqueId() == AxePowerAlly.target.getUniqueId()) {
						
						AxePowerAlly.players = AxePowerAlly.playersStorage;
						
					}
					
				}
				
			}
			
			@EventHandler
			public void onHit(EntityDamageByEntityEvent event) {
				
				if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
					
					Player damager = (Player) event.getDamager();
					Player player = (Player) event.getEntity();
					
					if(GameStorage.gameStarted && GameStorage.roles.containsKey(player.getUniqueId()) && GameStorage.roles.containsKey(damager.getUniqueId())) {
						
						if(GameStorage.roles.get(damager.getUniqueId()).getName() == RolesName.EREN && (GameStorage.roles.get(player.getUniqueId()).getName() == RolesName.ZEKE || GameStorage.roles.get(player.getUniqueId()).getName() == RolesName.HISTORIA || GameStorage.roles.get(player.getUniqueId()).getName() == RolesName.SMILINGTITAN)) {
							
							if(used) {
								
								return;
								
							} else {
								
								used = true;
								
							}
							
							new FancyMessage(AotUhc.prefix + "§aVous avez touché quelqu'un de sang royal. Vous pouvez soit ").then("[ralier a votre cause les trois titans purs les plus proches contre une cible commune durant 5 min]").color(ChatColor.GREEN).suggest("aot ally [target]").then("[tuer les trois titans les plus proches]").color(ChatColor.RED).suggest("aot kill").send(damager);
							
							Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {

								@Override
								public void run() {
									
									RolesRegister.getRole(RolesName.EREN).getCommandByName("kill").setDisabled(true);
									RolesRegister.getRole(RolesName.EREN).getCommandByName("ally").setDisabled(true);
									
								}
								
							}, 15 * 20);
							
						}
						
						if(AxePowerAlly.target != null && AxePowerAlly.players != null && AxePowerAlly.playersStorage != null && player.getUniqueId() == AxePowerAlly.target.getUniqueId()) {
					
							for(Player p : AxePowerAlly.playersStorage) {
								
								if(p.getUniqueId() == damager.getUniqueId()) {
									
									AxePowerAlly.playersStorage.remove(p);
									
								}
								
							}
							
						}
						
					}
							
				}
				
			}
			
		}, Arrays.asList(new Command(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
				
				if(GameStorage.gameStarted && sender instanceof Player && GameStorage.roles.containsKey(((Player) sender).getUniqueId()) && GameStorage.roles.get(((Player) sender).getUniqueId()).getName() == RolesName.EREN) {
					
					Player p = (Player) sender;
						
					Map<Player, Double> players = getPlayerByDistance(p.getLocation());
					
					int i = 3;
					
					for(Player player : players.keySet()) {
						
						if(i > 0) {
							
							if(GameStorage.roles.get(player.getUniqueId()) instanceof PureTitanRole) {
								
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getUniqueId());
								i--;
								
							}
							
						}
						
					}
					
					if(i < 3) {
					
						GameStorage.roles.get(p.getUniqueId()).reveal();
						
						Bukkit.broadcastMessage(AotUhc.prefix + "§cEren (" + p.getName() + ") a uttilisé son pouvoir et a tué les " + (3 - i) + " titans purs les plus proches");
					
					} else {
						
						p.sendMessage(AotUhc.prefix + "§cIl n'y a plus de titan en vie");
						
					}
					
					RolesRegister.getRole(RolesName.EREN).getCommandByName("kill").setDisabled(true);
					RolesRegister.getRole(RolesName.EREN).getCommandByName("ally").setDisabled(true);
						
					return true;
					
				}
				
				return false;
				
			}

		}, new TabCompleter() {
			
			@Override
			public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {

				return null;
				
			}
			
		}, "kill", 1), new Command(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
				
				if(args.length == 1) {
					
					if(Bukkit.getPlayer(args[0]) == null || !GameStorage.roles.containsKey(Bukkit.getPlayer(args[0]).getUniqueId())) {
						
						sender.sendMessage(AotUhc.prefix + "§cLe joueur spécifié n'est pas valide !");
						
						return true;
						
					}
					
					Player target = Bukkit.getPlayer(args[0]);

					if(GameStorage.gameStarted && sender instanceof Player && GameStorage.roles.containsKey(((Player) sender).getUniqueId()) && GameStorage.roles.containsKey(target.getUniqueId()) && GameStorage.roles.get(((Player) sender).getUniqueId()).getName() == RolesName.EREN) {
						
						Player p = (Player) sender;

						AxePowerAlly.target = target;
						
						Map<Player, Double> players = getPlayerByDistance(p.getLocation());
						List<Player> affectedPlayers = new ArrayList<Player>();
						
						int i = 3;
						
						for(Player player : players.keySet()) {
							
							if(i > 0) {
								
								if(GameStorage.roles.get(player.getUniqueId()) instanceof PureTitanRole) {
									
									((PureTitanRole) GameStorage.roles.get(player.getUniqueId())).transform();
									
									player.sendMessage(AotUhc.prefix + "§6Eren a uttilisé son pouvoir et vous devez donc tuer " + target.getDisplayName() + " en moins de 5 min sous peine de prendre des dégats.");
									
									affectedPlayers.add(player);
									i--;
									
								}
								
							}
							
						}
						
						if(affectedPlayers.size() == 0) {
							
							sender.sendMessage(AotUhc.prefix + "§cIl n'y a plus de titans en vie !");
							
						} else {
						
								AxePowerAlly.players = affectedPlayers;
								AxePowerAlly.playersStorage = affectedPlayers;
								
								AxePowerAlly.bukkitTask = Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {
		
									@Override
									public void run() {
										
										for(Player p : AxePowerAlly.players) {
											
											if(GameStorage.roles.containsKey(p.getUniqueId())) {
												
												p.damage(15);
												
											}
											
										}
										
									}
									
								}, 5 * 60 * 20);
						
						}
								
						RolesRegister.getRole(RolesName.EREN).getCommandByName("kill").setDisabled(true);
						RolesRegister.getRole(RolesName.EREN).getCommandByName("ally").setDisabled(true);
						
						return true;
						
					}
				
				}
				
				return false;
			}

		}, new TabCompleter() {
			
			@Override
			public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {

				return null;
				
			}
			
		}, "ally", 1)), new RoleRunnable() {

			@Override
			public void run(Role role) {

				Player p = role.getPlayer();
				p.getInventory().setItem(8, AotUhc.titanFlint);
				
			}
			
		}, Skin.eren, new TitanData(15, 25, Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 3), new PotionEffect(PotionEffectType.SPEED, 0, 3), new PotionEffect(PotionEffectType.JUMP, 0, 2)), new Listener() {}, 90, 300, emptyWeapon, Skin.assailant, 40));
		 
		mikasa = new Role(RolesName.MIKASA, AotUhc.config.isInt("mikasa.nb") ? AotUhc.config.getInt("mikasa.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1), new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1)), new Listener() {
			
			@EventHandler
			public void onPlayerDeath(PlayerDeathEvent event) {
				
				if(GameStorage.gameStarted) {
					
					Player p = event.getEntity();
					
					if(GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.EREN) {
						
						for(Role r : GameStorage.roles.values()) {
							
							if(r.getName() == RolesName.MIKASA) {
								
								r.removeEffects();
								r.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}, Arrays.asList(), new RoleRunnable() {

			@Override
			public void run(Role role) {
				
				for(Role r : GameStorage.roles.values()) {
					
					if(r.getName() == RolesName.EREN) {
						
						return;
						
					}
					
				}
				
				role.removeEffects();
				role.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
				
			}
			
		}, Skin.mikasa);
		
		livai = new Role(RolesName.LIVAI, AotUhc.config.isInt("livai.nb") ? AotUhc.config.getInt("livai.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1), new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1)), new Listener() {
			
			@EventHandler
			public void onPlayerDeath(PlayerDeathEvent event) {
				
				if(GameStorage.gameStarted) {
					
					Player p = event.getEntity();
					
					if(GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.ZEKE) {
						
						if(event.getEntity().getKiller() instanceof Player) {
							
							Player killer = event.getEntity().getKiller();
							
							if(GameStorage.roles.containsKey(killer.getUniqueId()) && GameStorage.roles.get(killer.getUniqueId()).getName() == RolesName.LIVAI) {
								
								killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 1));
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}, Arrays.asList(), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.livai);
		
		historia = new Role(RolesName.LIVAI, AotUhc.config.isInt("historia.nb") ? AotUhc.config.getInt("historia.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(new Command(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
				
				if(sender instanceof Player) {
					
					Player p = (Player) sender;
					
					if(GameStorage.gameStarted && GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.HISTORIA) {
						
						if(args.length == 1) {
							
							if(Bukkit.getPlayer(args[0]) != null) {
								
								Player target = Bukkit.getPlayer(args[0]);
								
								if(GameStorage.roles.containsKey(target.getUniqueId()) && p.getLocation().distance(target.getLocation()) <= 20) {
									
									target.sendMessage(AotUhc.prefix + "§aHistoria vous a soigné !");
									target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4* 20, 2, false, false));
									target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 2, false, false));
									target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 0, false, false));
									
									return true;
									
								} else {
									
									p.sendMessage(AotUhc.prefix + "§cImpossible de soigner " + args[0] +".");
									
								}
								
							}
							
						}
						
					}
					
				}
				
				return false;
			}
			
		}, new TabCompleter() {
			
			@Override
			public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {

				return null;
				
			}
			
		}, "heal", 2)), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.historia);
		
		armin = new Role(RolesName.ARMIN, AotUhc.config.isInt("armin.nb") ? AotUhc.config.getInt("armin.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(new Command(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
				
				if(sender instanceof Player) {
					
					Player p = (Player) sender;
					
					if(GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.ARMIN && args.length == 1) {
						
						Player player = Bukkit.getPlayer(args[0]);
						
						if(player != null && GameStorage.roles.containsKey(player.getUniqueId())) {
							
							Role role = GameStorage.roles.get(player.getUniqueId());
							role.reveal();
							GameStorage.roles.get(p.getUniqueId()).reveal();
							Bukkit.broadcastMessage(AotUhc.prefix + "§cArmin §6(§2" + p.getName() + "§6) a découvert que §2" + args[0] + "§6 est §c" + role.getName() + "§6 !");

							return true;
							
						}
						
					}
					
				}
				
				return false;
				
			}
			
		}, new TabCompleter() {
			
			@Override
			public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {

				return null;
				
			}
			
		}, "inspect", 2)), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.armin);
		
		hansi = new Role(RolesName.HANSI, AotUhc.config.isInt("hansi.nb") ? AotUhc.config.getInt("hansi.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(new Command(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
				
				if(sender instanceof Player) {
					
					Player p = (Player) sender;
					
					if(GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.HANSI && args.length == 1) {
						
						Player player = Bukkit.getPlayer(args[0]);
						
						if(player != null && GameStorage.roles.containsKey(player.getUniqueId())) {
							
							Role role = GameStorage.roles.get(player.getUniqueId());
							role.reveal();
							GameStorage.roles.get(p.getUniqueId()).reveal();
							Bukkit.broadcastMessage(AotUhc.prefix + "§cHansi §6(§2" + p.getName() + "§6) a découvert que §2" + args[0] + "§6 est §c" + role.getName() + "§6 !");

							return true;
							
						}
						
					}
					
				}
				
				return false;
				
			}
			
		}, new TabCompleter() {
			
			@Override
			public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {

				return null;
				
			}
			
		}, "inspect", 1)), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.hansi);
		
		sasha = new Role(RolesName.HANSI, AotUhc.config.isInt("sasha.nb") ? AotUhc.config.getInt("sasha.nb") : 1, Arrays.asList(), new Listener() {
			
			@EventHandler
			public void onFoodLevelChange(FoodLevelChangeEvent event) {
				
				if(event.getEntity() instanceof Player) {
					
					Player p = (Player) event.getEntity();
					
					if(event.getFoodLevel() >= 18) {
						
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));

					} else {
						
						if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
						
							p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						
						}
						
						if(p.hasPotionEffect(PotionEffectType.SPEED)) {
						
							p.removePotionEffect(PotionEffectType.SPEED);
						
						}
						
					}
					
				}
				
			}
			
		}, Arrays.asList(), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.sasha);
		
		conny = new Role(RolesName.CONNY, AotUhc.config.isInt("conny.nb") ? AotUhc.config.getInt("conny.nb") : 1, Arrays.asList(), new Listener() {
			
			@EventHandler
			public void onPlayerMove(PlayerMoveEvent event) {
				
				Player p = event.getPlayer();
				
				if(GameStorage.gameStarted && GameStorage.roles.containsKey(p.getUniqueId())) {
					
					if(GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.SASHA) {
						
						Role rs = GameStorage.roles.get(p.getUniqueId());
						Player ps = rs.getPlayer();
						
						for(Role rc : GameStorage.roles.values()) {
							
							if(rc.getName() == RolesName.CONNY) {
								
								Player pc = rc.getPlayer();
								
								if(pc.getLocation().distance(ps.getLocation()) <= 20) {
									
									pc.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
									pc.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
									
								} else {
									
									if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
										
										p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									
									}
									
									if(p.hasPotionEffect(PotionEffectType.SPEED)) {
									
										p.removePotionEffect(PotionEffectType.SPEED);
									
									}
									
								}
								
							}
							
						}
						
					}
					
					if(GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.CONNY) {
						
						Role rc = GameStorage.roles.get(p.getUniqueId());
						Player pc = rc.getPlayer();

						for(Role rs : GameStorage.roles.values()) {
							
							if(rs.getName() == RolesName.SASHA) {
								
								Player ps = rs.getPlayer();
								
								if(pc.getLocation().distance(ps.getLocation()) <= 20) {
									
									pc.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
									pc.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
									
								} else {
									
									if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
										
										p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									
									}
									
									if(p.hasPotionEffect(PotionEffectType.SPEED)) {
									
										p.removePotionEffect(PotionEffectType.SPEED);
									
									}
									
								}
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}, Arrays.asList(), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.conny);
		
		jean = new Role(RolesName.JEAN, AotUhc.config.isInt("jean.nb") ? AotUhc.config.getInt("jean.nb") : 1, Arrays.asList(), new Listener() {}, Arrays.asList(new Command(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
				
				if(sender instanceof Player) {
					
					Player p = (Player) sender;
					
					if(GameStorage.gameStarted && GameStorage.roles.containsKey(p.getUniqueId()) && GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.JEAN) {
						
						if(args.length == 0) {
							
							for(Role r : GameStorage.roles.values()) {
								
								if(r.getName() == RolesName.SOLDIER) {
									
									Player soldier = r.getPlayer();
									
									if(soldier.getLocation().distance(p.getLocation()) <= 20) {
									
										Bukkit.broadcastMessage(AotUhc.prefix + "§6Jean (" + p.getName() + ") a prouvé ses talents de meneur en boostant les soldats à proximité de lui !");
										
										soldier.sendMessage(AotUhc.prefix + "§aJean (" + p.getName() + ") vous a boosté !");
										
										soldier.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 0, false, false));
										soldier.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30 * 20, 0, false, false));
									
									}
									
								}
								
							}
							
						}
						
					}
					
				}
				
				return false;
				
			}
			
		}, new TabCompleter() {
			
			@Override
			public List<String> onTabComplete(CommandSender arg0, org.bukkit.command.Command arg1, String arg2, String[] arg3) {

				return null;
				
			}
			
		}, "boost", 3)), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.jean);
		
		soldier = new Role(RolesName.SOLDIER, AotUhc.config.isInt("soldier.nb") ? AotUhc.config.getInt("soldier.nb") : 1, Arrays.asList(), new Listener() {}, Arrays.asList(), new RoleRunnable() {

			@Override
			public void run(Role role) {}
			
		}, Skin.soldier);
		
		ItemStack crossbow = new ItemStack(Material.CROSSBOW);
		crossbow.addEnchantment(Enchantment.MULTISHOT, 1);
		
		zeke = new TitanRole(RolesName.ZEKE, AotUhc.config.isInt("zeke.nb") ? AotUhc.config.getInt("zeke.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(), new RoleRunnable() {
			
			@Override
			public void run(Role role) {
				
				Player p = role.getPlayer();
				p.getInventory().setItem(8, AotUhc.titanFlint);
				
			}
			
		}, Skin.zeke, new TitanData(16, 30, Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 2), new PotionEffect(PotionEffectType.SPEED, 0, 1), new PotionEffect(PotionEffectType.JUMP, 0, 0)), new Listener() {}, 75, 390, crossbow, Skin.beast, 40));
		
		ItemStack warHammer = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta warHammerMeta = warHammer.getItemMeta();
		warHammerMeta.setCustomModelData(10002);
		warHammer.setItemMeta(warHammerMeta);
		
		lara = new TitanRole(RolesName.LARA, AotUhc.config.isInt("lara.nb") ? AotUhc.config.getInt("lara.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new LimitedUsesListener() {

			
			
		}, Arrays.asList(), new RoleRunnable() {
			
			@Override
			public void run(Role role) {
				
				Player p = role.getPlayer();
				p.getInventory().setItem(8, AotUhc.titanFlint);
				
			}
			
		}, Skin.lara, new TitanData(15, 40, Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 3), new PotionEffect(PotionEffectType.SPEED, 0, 1), new PotionEffect(PotionEffectType.JUMP, 0, 1)), new Listener() {}, 60, 480, warHammer, Skin.warhammer, 40));
		
		lara.setTransformRunnable(new RoleRunnable() {
			
			@Override
			public void run(Role role) {
				
				if(AotUhc.plugin.laraListener.uses.containsKey(role.getPlayer().getUniqueId())) {
					
					AotUhc.plugin.laraListener.uses.remove(role.getPlayer().getUniqueId());
					
				}
				
			}
			
		});
		
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowMeta = bow.getItemMeta();
		bowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
		bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		bow.setItemMeta(bowMeta);
		
        pieck = new TitanRole(RolesName.PIECK, AotUhc.config.isInt("pieck.nb") ? AotUhc.config.getInt("pieck.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(), new RoleRunnable() {
            
            @Override
            public void run(Role role) {

                Player p = role.getPlayer();
                p.getInventory().setItem(8, AotUhc.titanFlint);
                
            }
            
        }, Skin.pieck, new TitanData(4, 8, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 0), new PotionEffect(PotionEffectType.SPEED, 0, 2), new PotionEffect(PotionEffectType.JUMP, 0, 0), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 0)), new Listener() {}, 180, 300, bow, Skin.cart, 30));
		
       reiner = new TitanRole(RolesName.REINER, AotUhc.config.isInt("reiner.nb") ? AotUhc.config.getInt("reiner.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(), new RoleRunnable() {
            
            @Override
            public void run(Role role) {

                Player p = role.getPlayer();
                p.getInventory().setItem(8, AotUhc.titanFlint);
                
            }
            
        }, Skin.reiner, new TitanData(15, 23, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 3), new PotionEffect(PotionEffectType.SPEED, 0, 1), new PotionEffect(PotionEffectType.JUMP, 0, 2), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 3)), new Listener() {}, 75, 390, emptyWeapon, Skin.armorer, 40));

       annie = new TitanRole(RolesName.ANNIE, AotUhc.config.isInt("annie.nb") ? AotUhc.config.getInt("annie.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(), new RoleRunnable() {
           
           @Override
           public void run(Role role) {

               Player p = role.getPlayer();
               p.getInventory().setItem(8, AotUhc.titanFlint);
               
           }
           
       }, Skin.annie, new TitanData(15, 25, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 3), new PotionEffect(PotionEffectType.SPEED, 0, 3), new PotionEffect(PotionEffectType.JUMP, 0, 3), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1)), new Listener() {}, 75, 300, emptyWeapon, Skin.female, 40)); 
       
       bertholdt = new TitanRole(RolesName.BERTHOLDT, AotUhc.config.isInt("bertholdt.nb") ? AotUhc.config.getInt("bertholdt.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new LimitedUsesListener() {
    	   
    	   @EventHandler
    	   public void fireWave(PlayerToggleSneakEvent event) {
    		   
    		   if(GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && GameStorage.roles.get((event.getPlayer().getUniqueId())).getName() == RolesName.BERTHOLDT && ((TitanRole)GameStorage.roles.get((event.getPlayer().getUniqueId()))).isTransformed() && event.isSneaking()) {
    			   
    			   for(Entry<UUID, Integer> e : uses.entrySet()) {System.out.println(e.getKey() + " : " + e.getValue());}
    			   
    			   if(!uses.containsKey(event.getPlayer().getUniqueId())) {
    				   
    				   uses.put(event.getPlayer().getUniqueId(), 0);
    				   
    				   Player c = event.getPlayer();
    				   
    				   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "particle minecraft:campfire_cosy_smoke " + c.getLocation().getX() + " " + (c.getLocation().getY() + (c.getHeight() / 2)) + " " + c.getLocation().getZ() + " 5 12 5 0.2 10000");
    				   
    				   for(LivingEntity e : c.getWorld().getLivingEntities()) {
    					   
    					   if(!e.getUniqueId().equals(c.getPlayer().getUniqueId())) {

								if(e.getLocation().distance(c.getLocation().add(0, c.getHeight() / 2, 0)) <= 50) {
									
									Location loc = c.getLocation();
									loc.setY(loc.getY() + (c.getHeight() / 2));
									
									e.setVelocity(e.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(10));
									e.setFireTicks(200);
									
								}
    						   
    					   }
    					   
    				   }
    				   
    			   }
    			   
    		   }
    		   
    	   }
    	   
       }, Arrays.asList(), new RoleRunnable() {
           
           @Override
           public void run(Role role) {

               Player p = role.getPlayer();
               p.getInventory().setItem(8, AotUhc.titanFlint);
               
           }
           
       }, Skin.bertholdt, new TitanData(60, 40, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 5), new PotionEffect(PotionEffectType.SLOW_DIGGING, 0, 1), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 2), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 0, 0)), new Listener() {}, 60, 720, emptyWeapon, Skin.colossal, 60)); 
       
       bertholdt.setTransformRunnable(new RoleRunnable() {
			
			@Override
			public void run(Role role) {
				
				if(((LimitedUsesListener) role.getListener()).uses.containsKey(role.getPlayer().getUniqueId())) {
					
					((LimitedUsesListener) role.getListener()).uses.remove(role.getPlayer().getUniqueId());

				}
				
			}
			
		});
       
       porco = new TitanRole(RolesName.PORCO, AotUhc.config.isInt("porco.nb") ? AotUhc.config.getInt("porco.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(), new RoleRunnable() {
           
           @Override
           public void run(Role role) {

               Player p = role.getPlayer();
               p.getInventory().setItem(8, AotUhc.titanFlint);
               
           }
           
       }, Skin.porco, new TitanData(6, 10, Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 0, 3), new PotionEffect(PotionEffectType.JUMP, 0, 3), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 0), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 1)), new Listener() {}, 75, 300, emptyWeapon, Skin.jaw, 30));
       
       falco = new Role(RolesName.FALCO, AotUhc.config.isInt("falco.nb") ? AotUhc.config.getInt("falco.nb") : 1, Arrays.asList(), new Listener() {
    	   
			@EventHandler
			public void onPlayerMove(PlayerMoveEvent event) {
				
				Player p = event.getPlayer();
				
				if(GameStorage.gameStarted && GameStorage.roles.containsKey(p.getUniqueId())) {
					
					if(GameStorage.roles.get(p.getUniqueId()).getName() == RolesName.GABY) {
						
						Role rg = GameStorage.roles.get(p.getUniqueId());
						Player pg = rg.getPlayer();
						
						for(Role r : GameStorage.roles.values()) {
							
							if(r.getName().getCamp() != Camps.MAHR) {
								
								Player pe = r.getPlayer();
								
								if(pe.getLocation().distance(pg.getLocation()) <= 20) {
									
									for(Role rf : GameStorage.roles.values()) {
										
										if(rf.getName() == RolesName.FALCO) {
											
											Player f = rf.getPlayer();
											
											f.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
											f.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
											
										}
										
									}
									
									return;
									
								}
								
							}
							
						}
						
						for(Role rf : GameStorage.roles.values()) {
							
							if(rf.getName() == RolesName.FALCO) {
								
								Player f = rf.getPlayer();
								
								if(f.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
									
									f.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									
								}
								
								if(f.hasPotionEffect(PotionEffectType.SPEED)) {
									
									f.removePotionEffect(PotionEffectType.SPEED);
									
								}
								
							}
							
						}
						
						return;
						
					}
					
					if(GameStorage.roles.get(p.getUniqueId()).getName().getCamp() != Camps.MAHR) {
						
						Role re = GameStorage.roles.get(p.getUniqueId());
						Player pe = re.getPlayer();
						
						for(Role r : GameStorage.roles.values()) {
							
							if(r.getName() == RolesName.GABY) {
								
								Player pg = r.getPlayer();
								
								if(pe.getLocation().distance(pg.getLocation()) <= 20) {
									
									for(Role rf : GameStorage.roles.values()) {
										
										if(rf.getName() == RolesName.FALCO) {
											
											Player f = rf.getPlayer();
											
											f.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
											f.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
											
										}
										
									}
									
									return;
									
								}
								
							}
							
						}
						
						for(Role rf : GameStorage.roles.values()) {
							
							if(rf.getName() == RolesName.FALCO) {
								
								Player f = rf.getPlayer();
								
								if(f.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
									
									f.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
									
								}
								
								if(f.hasPotionEffect(PotionEffectType.SPEED)) {
									
									f.removePotionEffect(PotionEffectType.SPEED);
									
								}
								
							}
							
						}
						
						return;
						
					}
					
				}
				
			}
    	   
       }, Arrays.asList(), new RoleRunnable() {
		
			@Override
			public void run(Role role) {}
		
       }, Skin.falco);
       
       gaby = new Role(RolesName.GABY, AotUhc.config.isInt("gaby.nb") ? AotUhc.config.getInt("gaby.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0)), new Listener() {

    	   @EventHandler
    	   public void onHit(EntityDamageByEntityEvent event) {
    		   
    		   if(GameStorage.pvp > 0) {
    			   
    			   return;
    			   
    		   }
    		   
    		   if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
    			   
    			   Player victim = (Player) event.getEntity();
    			   Player damager = (Player) event.getDamager();
    			   
    			   if(GameStorage.roles.containsKey(victim.getUniqueId()) && GameStorage.roles.containsKey(damager.getUniqueId())) {
    				   
    				   Role vr = GameStorage.roles.get(victim.getUniqueId());
    				   Role dr = GameStorage.roles.get(damager.getUniqueId());
    				   
    				   if(vr.getName() == RolesName.SASHA && dr.getName() == RolesName.GABY) {
    					   
    					   double damage = event.getDamage();
    					   
    					   event.setCancelled(true);
    					   
    					   dr.getPlayer().damage(damage * 2);
    					   
    				   }
    				   
    			   }
    			   
    		   }
    		   
    	   }
       
       }, Arrays.asList(), new RoleRunnable() {
		
			@Override
			public void run(Role role) {}
		
	   }, Skin.gaby);
       
       magath = new Role(RolesName.MAGATH, AotUhc.config.isInt("magath.nb") ? AotUhc.config.getInt("magath.nb") : 1, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0)), new Listener() {}, Arrays.asList(), new RoleRunnable() {
		
		@Override
		public void run(Role role) {}
		
       }, Skin.magath);
       
       smiling = new PureTitanRole(RolesName.SMILINGTITAN, AotUhc.config.isInt("smiling.nb") ? AotUhc.config.getInt("smiling.nb") : 1, Arrays.asList(), new Listener() {}, Arrays.asList(), new RoleRunnable() {

    	   @Override
	       public void run(Role role) {

    		   Player p = role.getPlayer();
	           p.getInventory().setItem(8, AotUhc.titanFlint);
	               
    	   }
    	   
       }, Skin.smiling, new PureTitanData(14, 20, Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1), new PotionEffect(PotionEffectType.REGENERATION, 0, 0), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 1)), new Listener() {}, Skin.smiling, 40));
       
       deviant = new PureTitanRole(RolesName.DEVIANTTITAN, AotUhc.config.isInt("deviant.nb") ? AotUhc.config.getInt("deviant.nb") : 1, Arrays.asList(), new Listener() {}, Arrays.asList(), new RoleRunnable() {

    	   @Override
	       public void run(Role role) {

    		   Player p = role.getPlayer();
	           p.getInventory().setItem(8, AotUhc.titanFlint);
	               
    	   }
    	   
       }, Skin.deviant, new PureTitanData(14, 20, Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1), new PotionEffect(PotionEffectType.JUMP, 0, 3), new PotionEffect(PotionEffectType.SPEED, 0, 2), new PotionEffect(PotionEffectType.REGENERATION, 0, 0), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 1)), new Listener() {}, Skin.deviant, 40));
       
       small = new PureTitanRole(RolesName.SMALLTITAN, AotUhc.config.isInt("small.nb") ? AotUhc.config.getInt("small.nb") : 1, Arrays.asList(), new Listener() {}, Arrays.asList(), new RoleRunnable() {

    	   @Override
	       public void run(Role role) {

    		   Player p = role.getPlayer();
	           p.getInventory().setItem(8, AotUhc.titanFlint);
	               
    	   }
    	   
       }, Skin.small, new PureTitanData(5, 8, Arrays.asList(new PotionEffect(PotionEffectType.REGENERATION, 0, 0), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 0), new PotionEffect(PotionEffectType.SPEED, 0, 1)), new Listener() {}, Skin.small, 25));
       
       medium = new PureTitanRole(RolesName.MEDIUMTITAN, AotUhc.config.isInt("medium.nb") ? AotUhc.config.getInt("medium.nb") : 1, Arrays.asList(), new Listener() {}, Arrays.asList(), new RoleRunnable() {

    	   @Override
	       public void run(Role role) {

    		   Player p = role.getPlayer();
	           p.getInventory().setItem(8, AotUhc.titanFlint);
	               
    	   }
    	   
       }, Skin.medium, new PureTitanData(14, 20, Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 0), new PotionEffect(PotionEffectType.REGENERATION, 0, 0), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 1), new PotionEffect(PotionEffectType.SPEED, 0, 0)), new Listener() {}, Skin.medium, 35));
       
       great = new PureTitanRole(RolesName.GREATTITAN, AotUhc.config.isInt("great.nb") ? AotUhc.config.getInt("great.nb") : 1, Arrays.asList(), new Listener() {}, Arrays.asList(), new RoleRunnable() {

    	   @Override
	       public void run(Role role) {

    		   Player p = role.getPlayer();
	           p.getInventory().setItem(8, AotUhc.titanFlint);
	               
    	   }
    	   
       }, Skin.great, new PureTitanData(15, 20, Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1), new PotionEffect(PotionEffectType.REGENERATION, 0, 0), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 2)), new Listener() {}, Skin.great, 40));
       
	}
	
	public static Map<Player, Double> getPlayerByDistance(Location src) {
		
		if(!GameStorage.gameStarted) {
			
			return null;
			
		}
		
		Map<Player, Double> players = new HashMap<Player, Double>();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			if(GameStorage.roles.containsKey(p.getUniqueId())) {
				
				players.put(p, p.getLocation().distance(src));
				
			}
				
			
		}
		
		List<Map.Entry<Player, Double>> list = new LinkedList<Map.Entry<Player, Double>>(players.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<Player, Double>>() {

			@Override
			public int compare(Entry<Player, Double> o1, Entry<Player, Double> o2) {
				
				return o1.getValue().compareTo(o2.getValue());
				
			}
			
		});
		
		players = new HashMap<Player, Double>();
		
		for(Map.Entry<Player, Double> entry : list) {
			
			players.put(entry.getKey(), entry.getValue());
			
		}
		
		return players;
		
	}
	
	public static Role getRole(RolesName name) {
		
		switch(name) {
		
			case ANNIE:
				return annie;
				
			case ARMIN:
				return armin;
				
			case BERTHOLDT:
				return bertholdt;
				
			case CONNY:
				return conny;
				
			case DEVIANTTITAN:
				return deviant;
				
			case EREN:
				return eren;
				
			case ERWIN:
				return erwin;
				
			case FALCO:
				return falco;
				
			case GABY:
				return gaby;
				
			case GREATTITAN:
				return great;
				
			case HANSI:
				return hansi;
				
			case HISTORIA:
				return historia;
				
			case JEAN:
				return jean;
				
			case LARA:
				return lara;
				
			case LIVAI:
				return livai;
				
			case MAGATH:
				return magath;
				
			case MEDIUMTITAN:
				return medium;
				
			case MIKASA:
				return mikasa;
				
			case PIECK:
				return pieck;
				
			case PORCO:
				return porco;
				
			case REINER:
				return reiner;
				
			case SASHA:
				return sasha;
				
			case SMALLTITAN:
				return small;
				
			case SMILINGTITAN:
				return smiling;
				
			case SOLDIER:
				return soldier;
				
			case ZEKE:
				return zeke;
				
			default:
				return null;
		
		}
		
	}
	
}