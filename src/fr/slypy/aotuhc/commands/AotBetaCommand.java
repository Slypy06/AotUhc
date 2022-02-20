package fr.slypy.aotuhc.commands;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.google.gson.Gson;

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.GameStorage;
import fr.slypy.aotuhc.api.FancyMessage;
import fr.slypy.aotuhc.json.JsonRole;
import fr.slypy.aotuhc.json.JsonStart;
import fr.slypy.aotuhc.roles.PureTitanRole;
import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.Roles;
import fr.slypy.aotuhc.roles.RolesName;
import fr.slypy.aotuhc.roles.RolesRegister;
import fr.slypy.aotuhc.roles.TitanRole;

public class AotBetaCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(args.length >= 4 && args[0].equalsIgnoreCase("report")) {

			if(args[1].equalsIgnoreCase("minor")) {
				
				String title = args[2];
				
				title = title.replaceAll("%20", " ");
				
				String message = "";
				
				for(int i = 3; i < args.length; i++) {
					
					message += args[i] + " ";
					
				}
				
				message = message.substring(0, message.length() - 1);
				
				File reportFolder = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "minor");
				
				if(!reportFolder.exists()) {
					
					reportFolder.mkdirs();
					
				}
				
				File reportPage = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "minor" + File.separator + title + " report-1.html");

				int i = 1;
				
				while(reportPage.exists()) {
					
					i++;
					reportPage = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "minor" + File.separator + title + " report-" + i + ".html");

				}
				
				title = StringEscapeUtils.escapeHtml(title);
				message = StringEscapeUtils.escapeHtml(message);
				
				message = message.replaceAll(" ", "\n");

				message = message.replaceAll("%20", " ");
				
				System.out.println(reportPage.getAbsolutePath());
				
				try {
				
					reportPage.createNewFile();
					
					PrintWriter writer = new PrintWriter(reportPage.getPath(), "UTF-8");
					String page = "<!DOCTYPE html>\n<html>\n	<head>\n		<meta charset=\"utf-8\">\n		<link rel=\"icon\" type=\"image/png\" href=\"attention.png\"/>\n		<title>Minor report #" + i + " : " + title + "</title>\n	</head>\n	<body>\n		<h1 style=\"text-align: center\"><u><strong>Minor Bug Report #" + i + "</strong></u></h1>\n		<p style=\"text-align: right\">Reported by <i>" + sender.getName() + "</i></p>\n		<h2 style=\"margin-left: 25px; margin-top: 50px\"><u><strong>Description : </strong></u></h2>\n		<p>" + message + "</p>\n		<h3 style=\"margin-left: 50px; margin-top: 75px\">Fixed : x</h3>\n	</body>\n</html>";
					writer.println(page);
					writer.close();
				
				} catch(Exception e) {
					
					e.printStackTrace();
					
				}
				
				new FancyMessage("[").color(ChatColor.DARK_GREEN).then("AOT").color(ChatColor.GOLD).then("UHC").color(ChatColor.RED).then("] ").color(ChatColor.DARK_GREEN).then(sender.getName()).style(ChatColor.ITALIC).color(ChatColor.GREEN).then(" a ").color(ChatColor.GREEN).then("report").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).suggest("/report").then(" un ").color(ChatColor.GREEN).then("bug").link("http://88.168.83.3:18080/minor/" + title.replaceAll(" ", "%20") + "%20report-" + i + ".html").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).then(" ").color(ChatColor.GREEN).then("MINEUR").color(ChatColor.DARK_GREEN).then(" !").color(ChatColor.GREEN).send(Bukkit.getOnlinePlayers());
				new FancyMessage("[").color(ChatColor.DARK_GREEN).then("AOT").color(ChatColor.GOLD).then("UHC").color(ChatColor.RED).then("] ").color(ChatColor.DARK_GREEN).then(sender.getName()).style(ChatColor.ITALIC).color(ChatColor.GREEN).then(" a ").color(ChatColor.GREEN).then("report").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).suggest("/report").then(" un ").color(ChatColor.GREEN).then("bug").link("http://88.168.83.3:18080/minor/" + title.replaceAll(" ", "%20") + "%20report-" + i + ".html").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).then(" ").color(ChatColor.GREEN).then("MINEUR").color(ChatColor.DARK_GREEN).then(" !").color(ChatColor.GREEN).send(Bukkit.getConsoleSender());
				
			} else if(args[1].equalsIgnoreCase("major")) {
				
				String title = args[2];
				title = title.replaceAll("%20", " ");
				
				String message = "";
				
				for(int i = 3; i < args.length; i++) {
					
					message += args[i] + " ";
					
				}
				
				message = message.substring(0, message.length() - 1);
				
				File reportFolder = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "major");
				
				if(!reportFolder.exists()) {
					
					reportFolder.mkdirs();
					
				}
				
				File reportPage = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "major" + File.separator + title + " report-1.html");

				int i = 1;
				
				while(reportPage.exists()) {
					
					i++;
					reportPage = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "major" + File.separator + title + " report-" + i + ".html");

				}
				
				title = StringEscapeUtils.escapeHtml(title);
				message = StringEscapeUtils.escapeHtml(message);
				
				message = message.replaceAll(" ", "\n");
				
				message = message.replaceAll("%20", " ");
				
				System.out.println(reportPage.getAbsolutePath());
				
				try {
				
					reportPage.createNewFile();
					
					PrintWriter writer = new PrintWriter(reportPage.getPath(), "UTF-8");
					String page = "<!DOCTYPE html>\n<html>\n	<head>\n		<meta charset=\"utf-8\">\n		<link rel=\"icon\" type=\"image/png\" href=\"attention.png\"/>\n		<title>Major report #" + i + " : " + title + "</title>\n	</head>\n	<body>\n		<h1 style=\"text-align: center\"><u><strong>Major Bug Report #" + i + "</strong></u></h1>\n		<p style=\"text-align: right\">Reported by <i>" + sender.getName() + "</i></p>\n		<h2 style=\"margin-left: 25px; margin-top: 50px\"><u><strong>Description : </strong></u></h2>\n		<p>" + message + "</p>\n		<h3 style=\"margin-left: 50px; margin-top: 75px\">Fixed : x</h3>\n	</body>\n</html>";
					writer.println(page);
					writer.close();
				
				} catch(Exception e) {
					
					e.printStackTrace();
					
				}
				
				new FancyMessage("[").color(ChatColor.DARK_GREEN).then("AOT").color(ChatColor.GOLD).then("UHC").color(ChatColor.RED).then("] ").color(ChatColor.DARK_GREEN)
				.then(sender.getName()).style(ChatColor.ITALIC).color(ChatColor.GREEN)
				.then(" a ").color(ChatColor.GREEN)
				.then("report").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).suggest("/report")
				.then(" un ").color(ChatColor.GREEN)
				.then("bug").link("http://88.168.83.3:18080/major/" + title.replaceAll(" ", "%20") + "%20report-" + i + ".html").color(ChatColor.GREEN).style(ChatColor.UNDERLINE)
				.then(" ").color(ChatColor.GREEN)
				.then("MAJEUR").color(ChatColor.GOLD)
				.then(" !").color(ChatColor.GREEN).send(Bukkit.getOnlinePlayers());
				
				new FancyMessage("[").color(ChatColor.DARK_GREEN).then("AOT").color(ChatColor.GOLD).then("UHC").color(ChatColor.RED).then("] ").color(ChatColor.DARK_GREEN)
				.then(sender.getName()).style(ChatColor.ITALIC).color(ChatColor.GREEN)
				.then(" a ").color(ChatColor.GREEN)
				.then("report").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).suggest("/report")
				.then(" un ").color(ChatColor.GREEN)
				.then("bug").link("http://88.168.83.3:18080/major/" + title.replaceAll(" ", "%20") + "%20report-" + i + ".html").color(ChatColor.GREEN).style(ChatColor.UNDERLINE)
				.then(" ").color(ChatColor.GREEN)
				.then("MAJEUR").color(ChatColor.GOLD)
				.then(" !").color(ChatColor.GREEN).send(Bukkit.getConsoleSender());
				
			} else if(args[1].equalsIgnoreCase("critical")) {
				
				String title = args[2];
				
				title = title.replaceAll("%20", " ");
				
				String message = "";
				
				for(int i = 3; i < args.length; i++) {
					
					message += args[i] + " ";
					
				}
				
				message = message.substring(0, message.length() - 1);
				
				File reportFolder = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "critical");
				
				if(!reportFolder.exists()) {
					
					reportFolder.mkdirs();
					
				}
				
				File reportPage = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "critical" + File.separator + title + " report-1.html");

				int i = 1;
				
				while(reportPage.exists()) {
					
					i++;
					reportPage = new File(Bukkit.getWorldContainer().getPath() + File.separator + "web" + File.separator + "reports" + File.separator + "critical" + File.separator + title + " report-" + i + ".html");

				}
				
				title = StringEscapeUtils.escapeHtml(title);
				message = StringEscapeUtils.escapeHtml(message);
				
				message = message.replaceAll(" ", "\n");
				message = message.replaceAll("%20", " ");
				
				System.out.println(reportPage.getAbsolutePath());
				
				try {
				
					reportPage.createNewFile();
					
					PrintWriter writer = new PrintWriter(reportPage.getPath(), "UTF-8");
					String page = "<!DOCTYPE html>\n<html>\n	<head>\n		<meta charset=\"utf-8\">\n		<link rel=\"icon\" type=\"image/png\" href=\"attention.png\"/>\n		<title>Critical report #" + i + " : " + title + "</title>\n	</head>\n	<body>\n		<h1 style=\"text-align: center\"><u><strong>Critical Bug Report #" + i + "</strong></u></h1>\n		<p style=\"text-align: right\">Reported by <i>" + sender.getName() + "</i></p>\n		<h2 style=\"margin-left: 25px; margin-top: 50px\"><u><strong>Description : </strong></u></h2>\n		<p>" + message + "</p>\n		<h3 style=\"margin-left: 50px; margin-top: 75px\">Fixed : x</h3>\n	</body>\n</html>";
					writer.println(page);
					writer.close();
				
				} catch(Exception e) {
					
					e.printStackTrace();
					
				}
				
				new FancyMessage("[").color(ChatColor.DARK_GREEN).then("AOT").color(ChatColor.GOLD).then("UHC").color(ChatColor.RED).then("] ").color(ChatColor.DARK_GREEN).then(sender.getName()).style(ChatColor.ITALIC).color(ChatColor.GREEN).then(" a ").color(ChatColor.GREEN).then("report").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).suggest("/report").then(" un ").color(ChatColor.GREEN).then("bug").link("http://88.168.83.3:18080/critical/" + title.replaceAll(" ", "%20") + "%20report-" + i + ".html").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).then(" ").color(ChatColor.GREEN).then("CRITIQUE").color(ChatColor.DARK_RED).then(" !").color(ChatColor.GREEN).send(Bukkit.getOnlinePlayers());
				new FancyMessage("[").color(ChatColor.DARK_GREEN).then("AOT").color(ChatColor.GOLD).then("UHC").color(ChatColor.RED).then("] ").color(ChatColor.DARK_GREEN).then(sender.getName()).style(ChatColor.ITALIC).color(ChatColor.GREEN).then(" a ").color(ChatColor.GREEN).then("report").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).suggest("/report").then(" un ").color(ChatColor.GREEN).then("bug").link("http://88.168.83.3:18080/critical/" + title.replaceAll(" ", "%20") + "%20report-" + i + ".html").color(ChatColor.GREEN).style(ChatColor.UNDERLINE).then(" ").color(ChatColor.GREEN).then("CRITIQUE").color(ChatColor.DARK_RED).then(" !").color(ChatColor.GREEN).send(Bukkit.getConsoleSender());
				
			}
			
		} else if(args.length == 2 && args[0].equalsIgnoreCase("customstart") && sender.isOp()) {
			
			String rolesJson = args[1];
			Gson g = new Gson();
			JsonStart start = g.fromJson(rolesJson, JsonStart.class);
			
			//VERIFICATION DES ARGS
			
			List<String> connectedPlayers = new ArrayList<String>();
			List<Player> players = new ArrayList<Player>();
			Bukkit.getOnlinePlayers().forEach(p -> connectedPlayers.add(p.getName()));
			
			boolean valid = connectedPlayers.size() > 0;
			
			for(JsonRole r : start.roles) {
				
				if(RolesName.valueOf(r.role) == null) {
					
					System.out.println("No role named " + r.role);
					valid = false;
					
				}
				
				for(String p : r.players) {
					
					if(connectedPlayers.contains(p)) {
						
						connectedPlayers.remove(p);
						players.add(Bukkit.getPlayer(p));
						
					} else {
						
						System.out.println("No player named " + p);
						valid = false;
						
					}
					
				}
				
			}
			
			if(!valid) {
				
				sender.sendMessage(AotUhc.prefix + "§cL'argument n'est pas valide");
				return true;
			}
			
			//DEMARRAGE
			
			GameStorage.gameStarted = true;
			
			int totalRoles = 0;
			
			for(Role r : RolesRegister.getRoles()) {
				
				totalRoles += r.getNb();
				
			}
			
			if(totalRoles < 6) {
				
				sender.sendMessage(AotUhc.prefix + "§cIl n'y a pas assez de roles distribuables pour lancer une partie ! Veuillez en rajouter dans la config.");
				
			}
			
			Bukkit.broadcastMessage(AotUhc.prefix + "§6Démarage de la partie");
			
			GameStorage.roles = new HashMap<UUID, Role>();
			
			int i = 0;
			
			double a = 360 / players.size();
			
			for(JsonRole r : start.roles) {
				
				for(String p : r.players) {

					Player player = Bukkit.getPlayer(p);
					
					player.setGameMode(GameMode.SURVIVAL);
					player.getInventory().clear();
					
					player.teleport(player.getWorld().getHighestBlockAt(2000 * (int) Math.cos(Math.toRadians((double) i * a)), 2000 * (int) Math.sin(Math.toRadians((double) i * a))).getLocation());
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
					
					player.sendTitle("§2Bonne chance !", "§aTu es §6§l" + Roles.getRole(RolesName.valueOf(r.role)).getName(), 60, 5, 20);
					
					Role playerRole = Roles.getRole(RolesName.valueOf(r.role)).implementPlayer(player);
					
					GameStorage.roles.put(player.getUniqueId(), playerRole);
					
					playerRole.affectEffects();
					
					i++;
					
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
	            sidebarObjective.getScore("§6§lBorder Size : §r§a" + (int) p.getWorld().getWorldBorder().getSize() + " x " + (int) p.getWorld().getWorldBorder().getSize()).setScore(0);
	            
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
			
		} else if(args.length == 1 && args[0].equalsIgnoreCase("cancelcooldown") && sender.isOp()) {
			
			if(GameStorage.gameStarted && sender instanceof Player && GameStorage.roles.containsKey(((Player) sender).getUniqueId())) {
				
				if(GameStorage.roles.get(((Player) sender).getUniqueId()) instanceof TitanRole) {
					
					((TitanRole) GameStorage.roles.get(((Player) sender).getUniqueId())).cancelCooldown();
					
				} else if(GameStorage.roles.get(((Player) sender).getUniqueId()) instanceof PureTitanRole) {
					
					((PureTitanRole) GameStorage.roles.get(((Player) sender).getUniqueId())).cancelCooldown();
					
				}
				
			}
			
		}
		
		return false;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		
		List<String> list = new ArrayList<String>();
		
		if(args.length == 1) {
			
			if("report".contains(args[0])) {
				
				list.add("report");
			
			}
			
			if("customstart".contains(args[0]) && sender.isOp()) {
				
				list.add("customstart");
			
			}
			
			if("cancelcooldown".contains(args[0]) && sender.isOp()) {
				
				list.add("cancelcooldown");
			
			}
			
		} else if(args.length == 2) {
			
			if("minor".contains(args[0])) {
				
				list.add("minor");
			
			}
			
			if("major".contains(args[0])) {
				
				list.add("major");
			
			}
			
			if("critical".contains(args[0])) {
				
				list.add("critical");
			
			}
			
		}
		
		return list;
		
	}

}
