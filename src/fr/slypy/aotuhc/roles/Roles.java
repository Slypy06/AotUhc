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
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.AxePowerAlly;
import fr.slypy.aotuhc.GameStorage;
import fr.slypy.aotuhc.Skin;
import fr.slypy.aotuhc.api.FancyMessage;
import fr.slypy.aotuhc.commands.Command;
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
		
		eren = new TitanRole(RolesName.EREN, AotUhc.config.isInt("eren.nb") ? AotUhc.config.getInt("eren.nb") : 1, Arrays.asList(), new Listener() {
			
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
					
					if(3 - 1 > 0) {
					
						GameStorage.roles.get(p.getUniqueId()).reveal();
						
						Bukkit.broadcastMessage(AotUhc.prefix + "§cEren (" + p.getName() + ") a uttilisé son pouvoir et a tué les " + (3 - i) + " titans purs les plus proches");
					
					}
						
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
							
						}
						
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
			
		}, Skin.eren, new TitanData(15, 25, Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 3), new PotionEffect(PotionEffectType.SPEED, 0, 3), new PotionEffect(PotionEffectType.JUMP, 0, 2)), new Listener() {}, 90, 300, emptyWeapon, Skin.assailant));
		 
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
			public void run(Role role) {}
			
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
								
								double x = p.getLocation().getX() - target.getLocation().getX();
								
								double y = p.getLocation().getY() - target.getLocation().getY();
								
								if(GameStorage.roles.containsKey(target.getUniqueId()) && Math.sqrt(x * x + y * y) <= 20) {
									
									target.sendMessage(AotUhc.prefix + "§aHistoria vous a soigné !");
									target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4* 20, 2, false, false));
									target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 2, false, false));
									target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 10 * 20, 0, false, false));
									
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
								
								double x = pc.getLocation().getX() - ps.getLocation().getX();
								
								double y = pc.getLocation().getY() - ps.getLocation().getY();
								
								if(Math.sqrt(x * x + y * y) <= 20) {
									
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
							
							if(rs.getName() == RolesName.CONNY) {
								
								Player ps = rs.getPlayer();
								
								double x = pc.getLocation().getX() - ps.getLocation().getX();
								
								double y = pc.getLocation().getY() - ps.getLocation().getY();
								
								if(Math.sqrt(x * x + y * y) <= 20) {
									
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
									
									double x = soldier.getLocation().getX() - p.getLocation().getX();
									
									double y = soldier.getLocation().getY() - p.getLocation().getY();
									
									if(Math.sqrt(x * x + y * y) <= 20) {
									
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
		crossbow.addEnchantment(Enchantment.PIERCING, 4);
		
		zeke = new TitanRole(RolesName.ZEKE, 1, Arrays.asList(), new Listener() {}, Arrays.asList(), new RoleRunnable() {
			
			@Override
			public void run(Role role) {
				
				Player p = role.getPlayer();
				p.getInventory().setItem(8, AotUhc.titanFlint);
				
			}
			
		}, Skin.zeke, new TitanData(16, 30, Arrays.asList(), new Listener() {}, 75, 390, crossbow, Skin.beast));
		
		ItemStack warHammer = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta warHammerMeta = warHammer.getItemMeta();
		warHammerMeta.setCustomModelData(10002);
		warHammer.setItemMeta(warHammerMeta);
		
		lara = new TitanRole(RolesName.LARA, 1, Arrays.asList(), new Listener() {
			
			List<UUID> uses = new ArrayList<UUID>();
			
			@EventHandler
			public void spikes(PlayerInteractEvent event) {
				
				if(GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && GameStorage.roles.get((event.getPlayer().getUniqueId())).getName() == RolesName.LARA) {
					
					if(uses.contains(event.getPlayer().getUniqueId())) {
						
						return;
						
					} else {
						
						uses.add(event.getPlayer().getUniqueId());
						
						Player l = event.getPlayer();
						
						for(Role r : GameStorage.roles.values()) {
							
							if(r.getPlayer().getUniqueId() != l.getUniqueId()) {
								
								Player p = r.getPlayer();
								
								double x = p.getLocation().getX() - l.getLocation().getX();
								double y = p.getLocation().getY() - l.getLocation().getY();
								
								if(Math.sqrt(x*x + y*y) <= 30) {
									
									p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 7));
									p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3 * 20, 250));
									p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0));
									p.damage(8);
									
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
			
		}, Skin.lara, new TitanData(15, 40, Arrays.asList(), new Listener() {}, 60, 480, warHammer, Skin.warhammer));
		
	}
	
	public static Map<Player, Double> getPlayerByDistance(Location src) {
		
		if(!GameStorage.gameStarted) {
			
			return null;
			
		}
		
		Map<Player, Double> players = new HashMap<Player, Double>();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			if(GameStorage.roles.containsKey(p.getUniqueId())) {
				
				double x = p.getLocation().getX() - src.getX();
				
				double y = p.getLocation().getY() - src.getY();
				
				players.put(p, new Double(Math.sqrt(x*x + y*y)));
				
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
	
}
