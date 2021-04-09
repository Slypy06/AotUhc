package fr.slypy.aotuhc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.GameStorage;
import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.RolesRegister;

public class AotCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(args.length == 1 && args[0].equalsIgnoreCase("start")) {
			
			if(Bukkit.getOnlinePlayers().size() < 10) {
				
				sender.sendMessage(AotUhc.prefix + "§cIl n'y a pas assez de joueurs pour lancer une partie !");
				
			} else {
				
				Bukkit.broadcastMessage(AotUhc.prefix + "§6Démarage de la partie");
				
				List<Player> players = new ArrayList<Player>();
				Player[] playersArray = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
				
				if(Bukkit.getOnlinePlayers().size() >= 40) {
					
					for(int i = 0; i< 40; i++) {
						
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
								player.sendMessage(AotUhc.prefix + "§cYou're no longer invulnerable !");
								
							}
							
						}

					}, 60 * 20);
					
					switch(current1) {
					
						case 0:
							
							int indexMahr = rand.nextInt(rolesMahr.size());
							
							player.sendTitle("§2Good Luck !", "§aYou're §6§l" + rolesMahr.get(indexMahr).getName().toString(), 60, 5, 20);
							
							Role playerRoleMahr = rolesMahr.get(indexMahr).implementPlayer(player);
							
							playerRoleMahr.getStartRun().run(playerRoleMahr);
							
							GameStorage.roles.put(player.getUniqueId(), playerRoleMahr);
							
							playerRoleMahr.affectEffects();
							
							rolesMahr.remove(indexMahr);
							
							break;
						
						case 1:
							
							int indexEldia = rand.nextInt(rolesEldia.size());
							
							player.sendTitle("§2Good Luck !", "§aYou're §6§l" + rolesEldia.get(indexEldia).getName().toString(), 60, 5, 20);
							
							Role playerRoleEldia = rolesEldia.get(indexEldia).implementPlayer(player);
							
							playerRoleEldia.getStartRun().run(playerRoleEldia);
							
							GameStorage.roles.put(player.getUniqueId(), playerRoleEldia);
							
							playerRoleEldia.affectEffects();
							
							rolesEldia.remove(indexEldia);
							
							break;
						
						case 2:
							
							int indexTitans = rand.nextInt(rolesTitans.size());
							
							player.sendTitle("§2Good Luck !", "§aYou're §6§l" + rolesTitans.get(indexTitans).getName().toString(), 60, 5, 20);
							
							Role playerRoleTitans = rolesTitans.get(indexTitans).implementPlayer(player);
							
							playerRoleTitans.getStartRun().run(playerRoleTitans);
							
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
			
			return true;
			
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
			
			if(!GameStorage.gameStarted) {
				
				for(Role role : RolesRegister.getRoles()) {
					
					for(fr.slypy.aotuhc.commands.Command command : role.getCommands()) {
						
						if(!list.contains(command.getCommandName()) && command.getCommandName().contains(args[0])) {
							
							list.add(command.getCommandName());
							
						}
					
					}
					
				}
				
			}
			
		} else if(!GameStorage.gameStarted && args.length >= 2) {
			
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