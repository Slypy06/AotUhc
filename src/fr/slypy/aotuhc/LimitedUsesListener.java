package fr.slypy.aotuhc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.Listener;

public class LimitedUsesListener implements Listener {

	public Map<UUID, Integer> uses = new HashMap<UUID, Integer>();
	
}
