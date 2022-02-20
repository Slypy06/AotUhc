package fr.slypy.aotuhc;

import java.util.HashMap;
import java.util.UUID;

import fr.slypy.aotuhc.roles.Role;

public class GameStorage {

	public static HashMap<UUID, Role> roles = new HashMap<UUID, Role>();
	public static HashMap<UUID, Role> rolesBackup = new HashMap<UUID, Role>();
	public static boolean gameStarted = false;
    public static int time = 0;
    public static HashMap<UUID, Integer> kills = new HashMap<UUID, Integer>();    
    public static int pvp = AotUhc.config.isInt("pvp") ? AotUhc.config.getInt("pvp") : 600;
    public static int border = AotUhc.config.isInt("border.start") ? AotUhc.config.getInt("border.start") : 900;
    
    public static String getTime() {
        
        int tempTime = time;
        
        int s = (int) (tempTime % 60);
        int m = (int) (Math.floorDiv(tempTime, 60) % 60);
        int h = (int) Math.floorDiv(Math.floorDiv(tempTime, 60), 60);
        
        return String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
        
    }
    
    
    public static String getPvpState() {
        
        int tempTime = pvp;
        
        int s = (int) (tempTime % 60);
        int m = (int) (Math.floorDiv(tempTime, 60) % 60);
        int h = (int) Math.floorDiv(Math.floorDiv(tempTime, 60), 60);

        
        return pvp == 0 ? "On" : String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
        
    }
    
    public static String getBorderState() {
        
        int tempTime = border;
        
        int s = (int) (tempTime % 60);
        int m = (int) (Math.floorDiv(tempTime, 60) % 60);
        int h = (int) Math.floorDiv(Math.floorDiv(tempTime, 60), 60);

        
        return border == 0 ? "On" : String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
        
    }
	
}