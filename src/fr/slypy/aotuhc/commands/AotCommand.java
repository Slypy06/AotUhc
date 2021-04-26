package fr.slypy.aotuhc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.GameStorage;
import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.RolesName;
import fr.slypy.aotuhc.roles.RolesRegister;

public class AotCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(args.length == 1 && args[0].equalsIgnoreCase("start")) {
			
			if(Bukkit.getOnlinePlayers().size() < 4) {
				
				sender.sendMessage(AotUhc.prefix + "§cIl n'y a pas assez de joueurs pour lancer une partie !");
				
			} else {
				
				GameStorage.gameStarted = true;
				
				int totalRoles = 0;
				
				for(Role r : RolesRegister.getRoles()) {
					
					totalRoles += r.getNb();
					
				}
				
				if(totalRoles < 6) {
					
					sender.sendMessage(AotUhc.prefix + "§cIl n'y a pas assez de roles distribuables pour lancer une partie ! Veuillez en rajouter dans la config.");
					
				}
				
				Bukkit.broadcastMessage(AotUhc.prefix + "§6Démarage de la partie");
				
				List<Player> players = new ArrayList<Player>();
				Player[] playersArray = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
				
				if(Bukkit.getOnlinePlayers().size() >= totalRoles) {
					
					for(int i = 0; i < totalRoles; i++) {
						
						players.add(playersArray[i]);
						
					}
					
				} else {
					
					players = Arrays.asList(playersArray);
					
				}
				
				players = mixList(players);
				
				List<Role> rolesMahr = new ArrayList<Role>();
				List<Role> rolesEldia = new ArrayList<Role>();
				List<Role> rolesTitans = new ArrayList<Role>();
				
				for(Role role : RolesRegister.getRoles()) {
					
					switch(role.getName().getCamp()) {
					
						case MAHR:
							
							for(int i = 0; i < role.getNb(); i++) {
								
								rolesMahr.add(role);
								
							}
							
							break;
							
						case ELDIA:
							
							for(int i = 0; i < role.getNb(); i++) {
								
								rolesEldia.add(role);
								
							}
							
							break;
							
						case TITANS:
							
							for(int i = 0; i < role.getNb(); i++) {
								
								rolesTitans.add(role);
								
							}
							
							break;
					
					}
					
				}
				
				GameStorage.roles = new HashMap<UUID, Role>();
				
				int[][] patern = new int[][] {new int[] {1, 1, 1}, new int[] {1, 1, 2}, new int[] {1, 2, 2}};
				
				int current1 = 0;
				int current2 = 0;
				int current = 0;
				
				Random rand = new Random();
				
				for(int i = 0; i < players.size(); i++) {
					
					Player player = players.get(i);
					
					player.setGameMode(GameMode.SURVIVAL);
					player.getInventory().clear();
					player.teleport(new Location(players.get(i).getWorld(), 2000 * Math.cos(Math.toRadians(i * 9)), 255.0F, 2000 * Math.sin(Math.toRadians(i * 9))));
					player.setInvulnerable(true);
					
					player.sendMessage(AotUhc.prefix + "§6You're invulnerable for 60s !");
					
					Bukkit.getScheduler().runTaskLater(AotUhc.plugin, new Runnable() {

						@Override
						public void run() {
							
							if(player.isOnline() && GameStorage.roles.containsKey(player.getUniqueId()) && GameStorage.gameStarted) {
								
								player.setInvulnerable(false);
								player.sendMessage(AotUhc.prefix + "§cVous n'êtes plus invulnérable !");
								
							}
							
						}

					}, 60 * 20);
					
					switch(current1) {
					
						case 0:
							
							int indexMahr = rand.nextInt(rolesMahr.size());
							
							player.sendTitle("§2Bonne chance !", "§aTu es §6§l" + rolesMahr.get(indexMahr).getName().toString(), 60, 5, 20);
							
							Role playerRoleMahr = rolesMahr.get(indexMahr).implementPlayer(player);
							
							GameStorage.roles.put(player.getUniqueId(), playerRoleMahr);
							
							playerRoleMahr.affectEffects();
							
							rolesMahr.remove(indexMahr);
							
							break;
						
						case 1:
							
							int indexEldia = rand.nextInt(rolesEldia.size());
							
							player.sendTitle("§2Bonne chance !", "§aTu es §6§l" + rolesEldia.get(indexEldia).getName().toString(), 60, 5, 20);
							
							Role playerRoleEldia = rolesEldia.get(indexEldia).implementPlayer(player);
							
							GameStorage.roles.put(player.getUniqueId(), playerRoleEldia);
							
							playerRoleEldia.affectEffects();
							
							rolesEldia.remove(indexEldia);
							
							break;
						
						case 2:
							
							int indexTitans = rand.nextInt(rolesTitans.size());
							
							player.sendTitle("§2Bonne chance !", "§aTu es §6§l" + rolesTitans.get(indexTitans).getName().toString(), 60, 5, 20);
							
							Role playerRoleTitans = rolesTitans.get(indexTitans).implementPlayer(player);
							
							GameStorage.roles.put(player.getUniqueId(), playerRoleTitans);
							
							playerRoleTitans.affectEffects();
							
							rolesTitans.remove(indexTitans);
							
							break;
					
					}
					
					current++;
					
					if(current >= patern[current1][current2]) {
					
						current1++;
						current = 0;
					
					}
					
					if(current1 >= 3) {
						
						current1 = 0;
						current2++;
						
					}
					
				}
				
			}
			
			GameStorage.rolesBackup = GameStorage.roles;
			
			for(Role r : GameStorage.roles.values()) {
				
				GameStorage.kills.put(r.getPlayer().getUniqueId(), 0);
				
				r.startRun();
				
                Player p = r.getPlayer();
                
                Scoreboard playerBoard = Bukkit.getScoreboardManager().getNewScoreboard();
                
                playerBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        		Objective health = playerBoard.registerNewObjective("health", "health", "§cHealth");
        		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
                
                Objective sidebarObjective;
                
                PluginDescriptionFile desc = AotUhc.plugin.getDescription();
                sidebarObjective = playerBoard.registerNewObjective("aotuhc", "dummy", AotUhc.prefix + "§7[" + desc.getVersion() + "]");
                
                sidebarObjective.getScore("§l§r§7=======================").setScore(12);
                sidebarObjective.getScore("§6§lRole : §r§a" + r.getName()).setScore(11);
                sidebarObjective.getScore("§k§r§7=======================").setScore(10);
                sidebarObjective.getScore("§l").setScore(9);
                sidebarObjective.getScore("§6§lGame Time : §r§a" + GameStorage.getTime()).setScore(8);
                sidebarObjective.getScore("§6§lPlayers : §r§a" + GameStorage.roles.size()).setScore(7);
                sidebarObjective.getScore("§r").setScore(6);
                sidebarObjective.getScore("§6§lKills : §r§a" + GameStorage.kills.get(p.getUniqueId())).setScore(5);
                sidebarObjective.getScore("§a").setScore(4);
                sidebarObjective.getScore("§6§lPvp : §r§a" + GameStorage.getPvpState()).setScore(3);
                sidebarObjective.getScore("§6§lBorder : §r§a" + GameStorage.getBorderState()).setScore(2);
                sidebarObjective.getScore("§c").setScore(1);
                sidebarObjective.getScore("§6§lBorder Size : §r§a" + p.getWorld().getWorldBorder().getSize() + " x " + p.getWorld().getWorldBorder().getSize()).setScore(0);
                
                sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
                
                p.setScoreboard(playerBoard);
                p.setHealth(p.getHealth());
				
			}
			
			AotUhc.updateTask = Bukkit.getScheduler().runTaskTimer(AotUhc.plugin, new Runnable() {
				
				@Override
				public void run() {
					
					GameStorage.time++;
					
					if(GameStorage.pvp > 0) {
						
						GameStorage.pvp--;
						
					}
					
					if(GameStorage.border > 0) {
						
						GameStorage.border--;
						
						if(GameStorage.border == 0) {
							
							Bukkit.getWorlds().forEach(w -> w.getWorldBorder().setSize(AotUhc.config.isInt("border.size.end") ? AotUhc.config.getInt("border.size.end") : 50, (AotUhc.config.isInt("border.end") ? AotUhc.config.getInt("border.end") : 900) - GameStorage.time));
							
						}
						
					}
					
					AotUhc.plugin.refreshScoreboard();
					
				}
				
			}, 0, 20);
			
			return true;
			
		} else if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
			
			if(sender instanceof Player && GameStorage.gameStarted) {
			
				Map<RolesName, Integer> roles = new HashMap<RolesName, Integer>();
				
				for(Role r : GameStorage.roles.values()) {
					
					if(!roles.containsKey(r.getName())) {
						
						roles.put(r.getName(), 1);
						
					} else {
						
						roles.put(r.getName(), roles.get(r.getName()) + 1);
						
					}
					
				}
				
				sender.sendMessage(AotUhc.prefix + "§l§6Voici les roles encore en jeu :");
				
				for(Entry<RolesName, Integer> entry : roles.entrySet()) {
					
					sender.sendMessage("     §6" + entry.getValue() + " §a" + entry.getKey());
					
				}
			
			} else {
				
				sender.sendMessage(AotUhc.prefix + "§cLa partie n'a pas commencée !");
				
			}
			
		} else if(args.length == 1 && args[0].equalsIgnoreCase("adminlist")) {
			
			if(sender instanceof Player && GameStorage.gameStarted && sender.isOp()) {
				
				Map<RolesName, List<Player>> roles = new HashMap<RolesName, List<Player>>();
				
				for(Role r : GameStorage.roles.values()) {
					
					if(!roles.containsKey(r.getName())) {
						
						roles.put(r.getName(), new ArrayList<Player>(Arrays.asList(r.getPlayer())));
						
					} else {
						
						List<Player> players = roles.get(r.getName());
						
						players.add(r.getPlayer());
						
						roles.put(r.getName(), players);
						
					}
					
				}
				
				sender.sendMessage(AotUhc.prefix + "§l§6Voici les roles encore en jeu :");
				
				for(Entry<RolesName, List<Player>> entry : roles.entrySet()) {
					
					String players = "§c";
					
					for(Player p : entry.getValue()) {
						
						players += p.getName() + "§6, §c";
						
					}
					
					if(players.length() > 6) {
						
						players.substring(0, players.length() - 5);
						
					}
					
					sender.sendMessage("     §6" + entry.getValue().size() + " §a" + entry.getKey() + " §6(" + players + "§6)");
					
				}
			
			}
			
		}
		
		if(GameStorage.gameStarted) {
		
			for(Role role : RolesRegister.getRoles()) {
				
				for(fr.slypy.aotuhc.commands.Command command : role.getCommands()) {
				
					if(args.length >= 1 && args[0].equalsIgnoreCase(command.getCommandName()))  {
						
						List<String> argsList = Arrays.asList(args);
						
						argsList.remove(0);
						
						String[] commandArgs = argsList.toArray(new String[argsList.size()]);
						
						command.getExecutor().onCommand(sender, cmd, msg, commandArgs);
						
					}
				
				}
				
			}
		
		}
		
		return false;
		
	}

	private List<Player> mixList(List<Player> players) {

		for(int i = players.size() - 1; i >= 1; i--) {
			
			int random = (int) Math.floor(Math.random() * (i + 1));
			
			Player save = players.get(i);
			players.set(i, players.get(random));
			players.set(random, save);
			
		}
		
		return players;
		
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		
		List<String> list = new ArrayList<String>();
		
		if(args.length == 1) {
			
			if("start".contains(args[0])) {
			
				list.add("start");
			
			}
			
			if(GameStorage.gameStarted) {
				
				if("list".contains(args[0])) {
					
					list.add("list");
					
				}
				
				if("adminlist".contains(args[0])) {
					
					list.add("adminlist");
					
				}
				
				for(Role role : RolesRegister.getRoles()) {
					
					for(fr.slypy.aotuhc.commands.Command command : role.getCommands()) {
						
						if(!list.contains(command.getCommandName()) && command.getCommandName().contains(args[0])) {
							
							list.add(command.getCommandName());
							
						}
					
					}
					
				}
				
			}
			
		} else if(GameStorage.gameStarted && args.length >= 2) {
			
			for(Role role : RolesRegister.getRoles()) {
				
				for(fr.slypy.aotuhc.commands.Command command : role.getCommands()) {
					
					if(command.getCommandName() == args[0]) {
						
						List<String> argsList = Arrays.asList(args);
						
						argsList.remove(0);
						
						String[] commandArgs = argsList.toArray(new String[argsList.size()]);
						
						command.getCompleter().onTabComplete(sender, cmd, msg, commandArgs).forEach(str -> list.add(str));
						
					}
				
				}
				
			}
			
		}
		
		return list;
		
	}

}