package fr.slypy.aotuhc.roles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import fr.slypy.aotuhc.AotUhc;

public class RolesRegister {

	private static List<Role> roles = new ArrayList<Role>();
	
	public static void registerRole(Role role) {
		
		for(Role r : roles) {
			
			if(r.getName() == role.getName()) {
				
				return;
				
			}
			
		}
		
		roles.add(role);
		Bukkit.getPluginManager().registerEvents(role.getListener(), AotUhc.plugin);
		
		if(role instanceof TitanRole) {
			
			TitanRole titanRole = (TitanRole) role;
			
			Bukkit.getPluginManager().registerEvents(titanRole.getData().getListener(), AotUhc.plugin);
			
		} else if(role instanceof PureTitanRole) {
			
			PureTitanRole titanRole = (PureTitanRole) role;
			
			Bukkit.getPluginManager().registerEvents(titanRole.getData().getListener(), AotUhc.plugin);
			
		}
		
	}

	public static List<Role> getRoles() {
		
		return roles;
		
	}
	
	public static Role getRole(RolesName n) {
		
		for(Role r : roles) {
			
			if(r.getName() == n) {
				
				return r;
				
			}
			
		}
		
		return null;
		
	}
	
}