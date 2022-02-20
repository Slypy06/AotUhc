package fr.slypy.aotuhc;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.RolesName;
import fr.slypy.aotuhc.roles.TitanRole;

public class LaraListener extends LimitedUsesListener {

	@EventHandler
	public void spikes(PlayerInteractEvent event) {
		
		System.out.println("lara : " + event.getAction());
		
		if(GameStorage.roles.containsKey(event.getPlayer().getUniqueId()) && GameStorage.roles.get((event.getPlayer().getUniqueId())).getName() == RolesName.LARA && event.getAction() == Action.LEFT_CLICK_AIR && ((TitanRole)GameStorage.roles.get((event.getPlayer().getUniqueId()))).isTransformed() &&event.getPlayer().isSneaking()) {
			
			if(uses.containsKey(event.getPlayer().getUniqueId())) {
				
				return;
				
			} else {
				
				uses.put(event.getPlayer().getUniqueId(), 0);
				
				Player l = event.getPlayer();
				
				for(Role r : GameStorage.roles.values()) {
					
					Player p = r.getPlayer();
					
					if(p.getLocation().distance(l.getLocation()) <= 30) {
						
						p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0);
						
					}
					
					if(r.getPlayer().getUniqueId() != l.getUniqueId()) {
						
						if(p.getLocation().distance(l.getLocation()) <= 30) {
							
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
	
}
