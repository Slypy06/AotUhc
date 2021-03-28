package fr.slypy.aotuhc.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.slypy.aotuhc.AotUhc;

public class Command {

	private CommandExecutor executor;
	private TabCompleter completer;
	private String commandName;
	private Map<UUID, Integer> uses = new HashMap<UUID, Integer>();
	private boolean disabled = false;
	private int maxUses;
	
	public Command(CommandExecutor executor, TabCompleter completer, String commandName) {

		this(executor, completer, commandName, Integer.MAX_VALUE);
		
	}
	
	public Command(CommandExecutor executor, TabCompleter completer, String commandName, int maxUses) {

		this.executor = executor;
		this.completer = completer;
		this.commandName = commandName;
		this.maxUses = maxUses;
		
	}
	
	public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			
			if(disabled) {
				
				sender.sendMessage(AotUhc.prefix + "§cVous ne pouvez plus uttiliser cette commande !");
				return false;
				
			}
			
			if(uses.containsKey(((Player) sender).getUniqueId())) {
				
				if(uses.get(((Player) sender).getUniqueId()) >= maxUses) {
					
					sender.sendMessage(AotUhc.prefix + "§cVous ne pouvez plus uttiliser cette commande !");
					return false;
					
				}
				
				
				
			}
			
			boolean valid = executor.onCommand(sender, cmd, msg, args);
			
			if(valid) {
			
				if(uses.containsKey(((Player) sender).getUniqueId())) {
					
					uses.put(((Player) sender).getUniqueId(), uses.get(((Player) sender).getUniqueId()) + 1);
					
				} else {
					
					uses.put(((Player) sender).getUniqueId(), 1);
					
				}
			
			}
			
			return valid;
			
		}
		
		return executor.onCommand(sender, cmd, msg, args);
		
	}

	public CommandExecutor getExecutor() {
		
		return executor;
		
	}

	public void setExecutor(CommandExecutor executor) {
		
		this.executor = executor;
		
	}
	
	public TabCompleter getCompleter() {
		
		return completer;
		
	}

	public void setCompleter(TabCompleter completer) {
		
		this.completer = completer;
		
	}

	public String getCommandName() {
		
		return commandName;
		
	}

	public void setCommandName(String commandName) {
		
		this.commandName = commandName;
		
	}

	public boolean isDisabled() {
		
		return disabled;
		
	}

	public void setDisabled(boolean disabled) {
		
		this.disabled = disabled;
		
	}

	public int getMaxUses() {
		
		return maxUses;
		
	}

	public void setMaxUses(int maxUses) {
		
		this.maxUses = maxUses;
		
	}

	public Map<UUID, Integer> getUses() {
		
		return uses;
		
	}
	
}