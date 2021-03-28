package fr.slypy.aotuhc;

import java.util.HashMap;
import java.util.UUID;

import fr.slypy.aotuhc.roles.Role;

public class GameStorage {

	public static HashMap<UUID, Role> roles = new HashMap<UUID, Role>();
	public static HashMap<UUID, Role> rolesBackup = new HashMap<UUID, Role>();
	public static boolean gameStarted = false;
	
}