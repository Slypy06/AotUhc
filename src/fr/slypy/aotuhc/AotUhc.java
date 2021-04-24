package fr.slypy.aotuhc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import fr.slypy.aotuhc.commands.AotCommand;
import fr.slypy.aotuhc.recipes.RecipeUtils;
import fr.slypy.aotuhc.roles.Camps;
import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.Roles;
import fr.slypy.aotuhc.roles.RolesName;
import fr.slypy.aotuhc.roles.RolesRegister;
//import mkremins.fanciful.FancyMessage;

public class AotUhc extends JavaPlugin {
	
//	public static void main(String[] args) {
		
	//	System.out.println(new FancyMessage("[").color(ChatColor.DARK_GREEN).then("AOT").color(ChatColor.GOLD).then("UHC").color(ChatColor.RED).then("]").color(ChatColor.DARK_GREEN).then(" ").then("Vous avez touché quelqu'un de sang royal. Vous pouvez soit ").color(ChatColor.GOLD).then("[ralier a votre cause les trois titans les plus proches contre une cible commune durant 5 min]").color(ChatColor.GREEN).suggest("/aot ally ").tooltip("/aot ally [target]").then(" ").then("[tuer les trois titans les plus proches]").color(ChatColor.RED).suggest("/aot kill").tooltip("/aot kill").toJSONString());
		
	//}
	
	public static AotUhc plugin;
	public static File configFile;
	public static FileConfiguration config;
	public static String prefix = "§l§6[§aAOT§cUHC§6]§r ";
	public static ItemStack titanFlint;
	public static Scoreboard board;
	public static BukkitTask updateTask = null;
	
	@Override	
	public void onEnable() {
		
		configFile = new File(this.getDataFolder().getPath() + "/" + "aot.yml");
		
		if(!configFile.exists()) {
			
			try {
				
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				
			} catch (IOException e) {

				e.printStackTrace();
				
			}
			
			config = YamlConfiguration.loadConfiguration(configFile);
			
			setDefaultConfig();
			
		} else {
		
			config = YamlConfiguration.loadConfiguration(configFile);
		
		}
		
		plugin = this;
		
		Bukkit.getWorlds().forEach(w -> w.getWorldBorder().setCenter(0, 0));
		Bukkit.getWorlds().forEach(w -> w.getWorldBorder().setSize(config.isInt("worldborder.size.start") ? config.getInt("worldborder.size.start") : 5000));
		
		File snkDatapack = new File(Bukkit.getWorldContainer().getPath() + "/" + "world" + "/" + "datapacks" + "/" + "snk.zip");
		
		if(!snkDatapack.exists()) {
			
			System.out.println("Le datapack SNK n'est pas présent. Téléchargement du datapack...");

			Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {

				@Override
				public void run() {
					
					try {
						
						FileUtils.copyURLToFile(new URL("http://enzo.m.7.free.fr/aotuhc/data/datapacks/snk.zip"), snkDatapack);
						System.out.println("Activation du datapack");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "datapack list");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "datapack enable " + '"' + "file/snk.zip" + '"');
						System.out.println("Datapack actif");
						
					} catch (MalformedURLException e) {

						System.out.println("Erreur lors du téléchargement du datapack");
						e.printStackTrace();
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
						
					} catch (IOException e) {

						System.out.println("Erreur lors du téléchargement du datapack");
						e.printStackTrace();
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
						
					}
					
				}
				
			});
			
			
			
		}
		
		Bukkit.getServer().getPluginManager().registerEvents(new AotListener(), this);
		
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective health = board.registerNewObjective("health", "health", "§cHealth");
		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		for(Player online : Bukkit.getOnlinePlayers()){
			
			  online.setScoreboard(board);
			  online.setHealth(online.getHealth());
			  
		}
		
		AotCommand aotCmd = new AotCommand();
		
		this.getCommand("aot").setExecutor(aotCmd);
		this.getCommand("aot").setTabCompleter(aotCmd);
	
		net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(new ItemStack(Material.FLINT));
		stack.getOrCreateTag().setInt("aot_bite", 1);
		titanFlint = CraftItemStack.asBukkitCopy(stack);
		ItemMeta meta = titanFlint.getItemMeta();
		meta.setDisplayName("§2TRANSFORM");
		meta.setLore(Arrays.asList("§r§7Shift + right click in air to transform", "yourself into a titan !"));
		titanFlint.setItemMeta(meta);
		
		ItemStack result1 = new ItemStack(Material.CARROT_ON_A_STICK);
		net.minecraft.server.v1_16_R3.ItemStack nmsStack1 = CraftItemStack.asNMSCopy(result1);
		nmsStack1.getTag().setInt("aot", 1);
		result1 = CraftItemStack.asBukkitCopy(nmsStack1);
		ItemMeta resultMeta1 = result1.getItemMeta();
		resultMeta1.setCustomModelData(10001);
		resultMeta1.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic_attack_damage", 6, Operation.ADD_NUMBER, EquipmentSlot.HAND));
		resultMeta1.setDisplayName("§7§lHard Steel Sword");
		result1.setItemMeta(resultMeta1);
		HashMap<String, Material> ingredients1 = new HashMap<String, Material>();
		ingredients1.put("I", Material.IRON_BLOCK);
		ingredients1.put("L", Material.LEVER);
		ingredients1.put("D", Material.DIAMOND);
		
		try {
			
			RecipeUtils.addRecipe(this, "aot_sword", result1, new String[] {" I ", " I ", "LDL"}, ingredients1, Camps.ELDIA);
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		ItemStack result2 = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
		net.minecraft.server.v1_16_R3.ItemStack nmsStack2 = CraftItemStack.asNMSCopy(result2);
		nmsStack2.getTag().setInt("aot_odm", 1);
		result2 = CraftItemStack.asBukkitCopy(nmsStack2);
		ItemMeta resultMeta2 = result2.getItemMeta();
		resultMeta2.setCustomModelData(10004);
		resultMeta2.setDisplayName("§8§l3D Maneuver Gear");
		result2.setItemMeta(resultMeta2);
		HashMap<String, Material> ingredients2 = new HashMap<String, Material>();
		ingredients2.put("I", Material.IRON_BLOCK);
		ingredients2.put("S", Material.STRING);
		ingredients2.put("D", Material.DIAMOND);
		
		try {
			
			RecipeUtils.addRecipe(this, "aot_odm", result2, new String[] {"ISI", "ISI", "IDI"}, ingredients2, Camps.ELDIA); //ELDIA
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		ItemStack result3 = new ItemStack(Material.TRIDENT);
		ItemMeta resultMeta3 = result3.getItemMeta();
		resultMeta3.setCustomModelData(10003);
		resultMeta3.setDisplayName("§7§lThunder Spear");
		result3.setItemMeta(resultMeta3);
		HashMap<String, Material> ingredients3 = new HashMap<String, Material>();
		ingredients3.put("F", Material.FLINT);
		ingredients3.put("I", Material.DIAMOND);
		ingredients3.put("G", Material.FIRE_CHARGE);
		
		try {
			
			RecipeUtils.addRecipe(this, "thunder_spear", result3, new String[] {"F", "G", "I"}, ingredients3, RolesName.HANSI); //HANSI
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		ItemStack result5 = new ItemStack(Material.CARROT_ON_A_STICK);
		net.minecraft.server.v1_16_R3.ItemStack nmsStack5 = CraftItemStack.asNMSCopy(result5);
		nmsStack5.getTag().setInt("aot", 1);
		result5 = CraftItemStack.asBukkitCopy(nmsStack5);
		ItemMeta resultMeta5 = result5.getItemMeta();
		resultMeta5.setCustomModelData(10001);
		resultMeta5.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic_attack_damage", 12, Operation.ADD_NUMBER, EquipmentSlot.HAND));
		resultMeta5.setDisplayName("§8§lUltra Hard Steel Sword");
		result5.setItemMeta(resultMeta5);
		HashMap<String, Material> ingredients5 = new HashMap<String, Material>();
		ingredients5.put("I", Material.DIAMOND_BLOCK);
		ingredients5.put("L", Material.GOLD_INGOT);
		ingredients5.put("D", Material.OBSIDIAN);
		
		try {
			
			RecipeUtils.addRecipe(this, "ultra_aot_sword", result5, new String[] {" I ", " I ", "LDL"}, ingredients5, RolesName.ARMIN); //ARMIN
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		ItemStack result6 = new ItemStack(Material.BLAZE_POWDER);
		HashMap<String, Material> ingredients6 = new HashMap<String, Material>();	
		ingredients6.put("R", Material.REDSTONE);
		ingredients6.put("G", Material.GOLD_NUGGET);
		ingredients6.put("L", Material.LAVA_BUCKET);
		
		try {
			
			RecipeUtils.addRecipe(this, "blaze_powder", result6, new String[] {"RRR", "RGR", "LLL"}, ingredients6);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		ItemStack result7 = new ItemStack(Material.FIRE_CHARGE);
		HashMap<String, Material> ingredients7 = new HashMap<String, Material>();	
		ingredients7.put("G", Material.GUNPOWDER);
		ingredients7.put("B", Material.BLAZE_POWDER);
		ingredients7.put("C", Material.COAL);
		
		try {
			
			RecipeUtils.addRecipe(this, "fire_charge", result7, new String[] {"GBG", "BCB", "GBG"}, ingredients7);
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		registerRoles();

	}
	
	public void refreshScoreboard() {
		
		for(Role r : GameStorage.roles.values()) {
			
			Player p = r.getPlayer();
			
			Scoreboard board = p.getScoreboard();
			
			Objective sidebarObjective = board.getObjective(DisplaySlot.SIDEBAR);
			
			if(sidebarObjective != null) {
				
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
				
			}
			
		}
		
	}
	
	public void setDefaultConfig() {
		
		config.set("pvp", 600);

		config.set("border.start", 900);
		config.set("border.end", 1500);
		config.set("border.size.start", 5000);
		config.set("border.size.end", 100);
		
		config.set("erwin.use", true);
		config.set("erwin.nb", 1);
		
		config.set("eren.use", true);
		config.set("eren.nb", 1);
		
		config.set("mikasa.use", true);
		config.set("mikasa.nb", 1);
		
		config.set("livai.use", true);
		config.set("livai.nb", 1);
		
		config.set("historia.use", true);
		config.set("historia.nb", 1);
		
		config.set("armin.use", true);
		config.set("armin.nb", 1);
		
		config.set("hansi.use", true);
		config.set("hansi.nb", 1);
		
		config.set("sasha.use", true);
		config.set("sasha.nb", 1);
		
		config.set("conny.use", true);
		config.set("conny.nb", 1);
		
		config.set("jean.use", true);
		config.set("jean.nb", 1);
		
		config.set("soldier.use", true);
		config.set("soldier.nb", 3);
		
		
		
		config.set("zeke.use", true);
		config.set("zeke.nb", 1);
		
		config.set("pieck.use", true);
		config.set("pieck.nb", 1);
		
		config.set("teyber.use", true);
		config.set("teyber.nb", 1);
		
		config.set("reiner.use", true);
		config.set("reiner.nb", 1);
		
		config.set("annie.use", true);
		config.set("annie.nb", 1);
		
		config.set("bertholdt.use", true);
		config.set("bertholdt.nb", 1);
		
		config.set("porco.use", true);
		config.set("porco.nb", 1);
		
		config.set("magath.use", true);
		config.set("magath.nb", 1);
		
		config.set("gaby.use", true);
		config.set("gaby.nb", 1);
		
		config.set("falco.use", true);
		config.set("falco.nb", 1);
		
		
		
		
		config.set("smiling.use", true);
		config.set("smiling.nb", 1);
		
		config.set("deviant.use", true);
		config.set("deviant.nb", 2);
		
		config.set("great.use", true);
		config.set("great.nb", 3);
		
		config.set("medium.use", true);
		config.set("medium.nb", 5);
		
		config.set("small.use", true);
		config.set("small.nb", 4);
		
		saveAotConfig();

	}
	
	public void registerRoles() {
		
		Roles.initRoles();
		
		if(config.isConfigurationSection("erwin") && config.isBoolean("erwin.use") && config.getBoolean("erwin.use")) {
			
			RolesRegister.registerRole(Roles.erwin);
			
		}
		
		if(config.isConfigurationSection("eren") && config.isBoolean("eren.use") && config.getBoolean("eren.use")) {
		
			RolesRegister.registerRole(Roles.eren);
			
		}
		
		if(config.isConfigurationSection("mikasa") && config.isBoolean("mikasa.use") && config.getBoolean("mikasa.use")) {
			
			RolesRegister.registerRole(Roles.mikasa);
			
		}
		
		if(config.isConfigurationSection("livai") && config.isBoolean("livai.use") && config.getBoolean("livai.use")) {
			
			RolesRegister.registerRole(Roles.livai);
			
		}
		
		if(config.isConfigurationSection("historia") && config.isBoolean("historia.use") && config.getBoolean("historia.use")) {
			
			RolesRegister.registerRole(Roles.historia);
			
		}
		
		if(config.isConfigurationSection("armin") && config.isBoolean("armin.use") && config.getBoolean("armin.use")) {
			
			RolesRegister.registerRole(Roles.armin);
			
		}
		
		if(config.isConfigurationSection("hansi") && config.isBoolean("hansi.use") && config.getBoolean("hansi.use")) {
			
			RolesRegister.registerRole(Roles.hansi);
			
		}
		
		if(config.isConfigurationSection("sasha") && config.isBoolean("sasha.use") && config.getBoolean("sasha.use")) {
			
			RolesRegister.registerRole(Roles.sasha);
			
		}
		
		if(config.isConfigurationSection("armin") && config.isBoolean("conny.use") && config.getBoolean("conny.use")) {
			
			RolesRegister.registerRole(Roles.conny);
			
		}
		
		if(config.isConfigurationSection("jean") && config.isBoolean("jean.use") && config.getBoolean("jean.use")) {
			
			RolesRegister.registerRole(Roles.jean);
			
		}
		
		if(config.isConfigurationSection("soldier") && config.isBoolean("soldier.use") && config.getBoolean("soldier.use")) {
			
			RolesRegister.registerRole(Roles.soldier);
			
		}
		
		if(config.isConfigurationSection("zeke") && config.isBoolean("zeke.use") && config.getBoolean("zeke.use")) {
			
			RolesRegister.registerRole(Roles.zeke);
			
		}
		
		if(config.isConfigurationSection("pieck") && config.isBoolean("pieck.use") && config.getBoolean("pieck.use")) {
			
			RolesRegister.registerRole(Roles.soldier);
			
		}
		
		if(config.isConfigurationSection("lara") && config.isBoolean("lara.use") && config.getBoolean("lara.use")) {
			
			RolesRegister.registerRole(Roles.lara);
			
		}
		
		if(config.isConfigurationSection("reiner") && config.isBoolean("reiner.use") && config.getBoolean("reiner.use")) {
			
			RolesRegister.registerRole(Roles.reiner);
			
		}
		
		if(config.isConfigurationSection("annie") && config.isBoolean("annie.use") && config.getBoolean("annie.use")) {
			
			RolesRegister.registerRole(Roles.annie);
			
		}
		
		if(config.isConfigurationSection("bertholdt") && config.isBoolean("bertholdt.use") && config.getBoolean("bertholdt.use")) {
			
			RolesRegister.registerRole(Roles.bertholdt);
			
		}
		
		if(config.isConfigurationSection("porco") && config.isBoolean("porco.use") && config.getBoolean("porco.use")) {
			
			RolesRegister.registerRole(Roles.porco);
			
		}
		
		if(config.isConfigurationSection("falco") && config.isBoolean("falco.use") && config.getBoolean("falco.use")) {
			
			RolesRegister.registerRole(Roles.falco);
			
		}
		
		if(config.isConfigurationSection("gaby") && config.isBoolean("gaby.use") && config.getBoolean("gaby.use")) {
			
			RolesRegister.registerRole(Roles.gaby);
			
		}
			
		if(config.isConfigurationSection("magath") && config.isBoolean("magath.use") && config.getBoolean("magath.use")) {
			
			RolesRegister.registerRole(Roles.magath);
			
		}
		
		if(config.isConfigurationSection("smiling") && config.isBoolean("smiling.use") && config.getBoolean("smiling.use")) {
			
			RolesRegister.registerRole(Roles.smiling);
			
		}
		
		if(config.isConfigurationSection("deviant") && config.isBoolean("deviant.use") && config.getBoolean("deviant.use")) {
			
			RolesRegister.registerRole(Roles.deviant);
			
		}
		
		if(config.isConfigurationSection("small") && config.isBoolean("small.use") && config.getBoolean("small.use")) {
			
			RolesRegister.registerRole(Roles.small);
			
		}
		
		if(config.isConfigurationSection("medium") && config.isBoolean("medium.use") && config.getBoolean("medium.use")) {
			
			RolesRegister.registerRole(Roles.medium);
			
		}
		
		if(config.isConfigurationSection("great") && config.isBoolean("great.use") && config.getBoolean("great.use")) {
			
			RolesRegister.registerRole(Roles.great);
			
		}
		
	}
	
	public static void saveAotConfig() {
		
		try {
			
			config.save(configFile);
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

	public static void checkForGameEnd() {
		
		if(GameStorage.gameStarted) {
			
			boolean gameEnd = true;
			Camps c = null;
			
			for(Role r : GameStorage.roles.values()) {
				
				if(c != null && c != r.getName().getCamp()) {
					
					gameEnd = false;
					break;
					
				}
				
				c = r.getName().getCamp();
				
			}
			
			if(gameEnd) {
				
				GameStorage.gameStarted = false;
				
				for(UUID uuid : GameStorage.roles.keySet()) {
					
					Player p = Bukkit.getPlayer(uuid);
					new Celebrator().celebrate(p);
					
				}
				
				GameStorage.roles = new HashMap<UUID, Role>();
				GameStorage.rolesBackup = new HashMap<UUID, Role>();
				GameStorage.time = 0;
				GameStorage.kills = new HashMap<UUID, Integer>();
				GameStorage.pvp = AotUhc.config.isInt("pvp") ? AotUhc.config.getInt("pvp") : 600;
				GameStorage.border = AotUhc.config.isInt("border.start") ? AotUhc.config.getInt("border.start") : 900;
				
			}
			
		}
		
	}

}