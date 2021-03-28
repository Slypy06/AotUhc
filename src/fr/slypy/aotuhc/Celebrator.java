package fr.slypy.aotuhc;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;

public class Celebrator {

	BukkitTask task;
	
	public void celebrate(Player p) {
		
		Random rand = new Random();
		int qte = rand.nextInt(10) + 10;
		
		task = Bukkit.getScheduler().runTaskTimer(AotUhc.plugin, new Runnable() {
			
			int i = qte;
			
			@Override
			public void run() {
				
				if(!p.isOnline() || p.isDead()) {
					
					task.cancel();
					
				}
				
				spawnFirework(p.getLocation());
				i--;
				
				if(i <= 0) {
				
					task.cancel();
				
				}
				
			}
			
		}, rand.nextInt(8), rand.nextInt(20));
		
	}
	
	private void spawnFirework(Location loc) {
		
		Random rand = new Random();
		
		 Location newLoc = new Location(loc.getWorld(), loc.getX() + ((rand.nextFloat() * 8) - 4), loc.getY(), loc.getZ() + ((rand.nextFloat() * 8) - 4));
		 
		 Firework fw = (Firework) newLoc.getWorld().spawnEntity(newLoc, EntityType.FIREWORK);
         FireworkMeta fwm = fw.getFireworkMeta();
        
         Random r = new Random();   

         int rt = r.nextInt(5) + 1;
         Type type = Type.BALL;       
         if (rt == 1) type = Type.BALL;
         if (rt == 2) type = Type.BALL_LARGE;
         if (rt == 3) type = Type.BURST;
         if (rt == 4) type = Type.CREEPER;
         if (rt == 5) type = Type.STAR;
        
         int r1i = r.nextInt(17) + 1;
         int r2i = r.nextInt(17) + 1;
         Color c1 = getColor(r1i);
         Color c2 = getColor(r2i);
        
         FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        
         fwm.addEffect(effect);
        
         int rp = r.nextInt(2) + 1;
         fwm.setPower(rp);
        
         fw.setFireworkMeta(fwm);           
         
	}

	private Color getColor(int i) {
		
		Color c = null;
		
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		
	}
	
}
