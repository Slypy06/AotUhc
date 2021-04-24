package fr.slypy.aotuhc.roles;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import fr.slypy.aotuhc.Skin;
import fr.slypy.aotuhc.commands.Command;

public class Role {

	RolesName name;
	int nb;
	List<PotionEffect> effects;
	Listener listener;
	List<Command> commands;
	Player p;
	boolean implemented = false;
	RoleRunnable startRun;
	Skin skin;
	boolean reveal = false;
	
	public Role(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin) {

		this.name = name;
		this.nb = nb;
		this.effects = effects;
		this.listener = listener;
		this.commands = commands;
		this.startRun = startRun;
		this.skin = skin;
		
	}
	
	private Role(RolesName name, int nb, List<PotionEffect> effects, Listener listener, List<Command> commands, RoleRunnable startRun, Skin skin, Player p) {

		this.name = name;
		this.nb = nb;
		this.effects = effects;
		this.listener = listener;
		this.commands = commands;
		this.p = p;
		this.implemented = true;
		this.startRun = startRun;
		this.skin = skin;
		
	}

	public RolesName getName() {
		
		return name;
		
	}

	public void setName(RolesName name) {
		
		this.name = name;
		
	}

	public int getNb() {
		
		return nb;
		
	}

	public void setNb(int nb) {
		
		this.nb = nb;
		
	}

	public List<PotionEffect> getEffects() {
		
		return effects;
		
	}

	public void setEffects(List<PotionEffect> effects) {
		
		this.effects = effects;
		
	}

	public Listener getListener() {
		
		return listener;
		
	}

	public void setListener(Listener listener) {
		
		this.listener = listener;
		
	}
	
	public RoleRunnable getStartRun() {
		
		return startRun;
		
	}

	public void setStartRun(RoleRunnable startRun) {
		
		this.startRun = startRun;
		
	}

	public List<Command> getCommands() {
		
		return commands;
		
	}

	public void setCommands(List<Command> commands) {
		
		this.commands = commands;
		
	}
	
	public Player getPlayer() {
		
		return p;
		
	}
	
	public boolean isImplemented() {
		
		return implemented;
		
	}
	
	public Role implementPlayer(Player p) {
		
		return new Role(name, nb, effects, listener, commands, startRun, skin, p);
		
	}
	
	public void affectEffects() {
		
		if(!isImplemented()) {
			
			return;
			
		}
		
		effects.forEach(potion -> this.getPlayer().addPotionEffect(new PotionEffect(potion.getType(), Integer.MAX_VALUE, potion.getAmplifier(), false, false)));
		
	}
	
	public void removeEffects() {
		
		if(!isImplemented()) {
			
			return;
			
		}
		
		effects.forEach(potion -> this.getPlayer().removePotionEffect(potion.getType()));
		
	}
	
	public boolean isReveal() {
		
		return reveal;
		
	}
	
	public void reveal() {
		
		if(!isImplemented() || isReveal()) {
			
			return;
			
		}
		
		p.setDisplayName(p.getName() + " (" + getName() + ")");
		skin.applySkin(p);
		
	}
	
	public Command getCommandByName(String name) {
		
		for(Command c : commands) {
			
			if(c.getCommandName() == name) {
				
				return c;
				
			}
			
		}
		
		return null;
		
	}
	
	public void startRun() {
		
		startRun.run(this);
		
	}
	
}