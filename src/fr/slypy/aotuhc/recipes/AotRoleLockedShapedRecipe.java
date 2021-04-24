package fr.slypy.aotuhc.recipes;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import fr.slypy.aotuhc.roles.Role;
import fr.slypy.aotuhc.roles.RolesName;

public class AotRoleLockedShapedRecipe extends AotShapedRecipe {

	RolesName lock;
	
	public AotRoleLockedShapedRecipe(NamespacedKey key, ItemStack result, String uuid, RolesName lock) {
		
		super(key, result, uuid);
		this.lock = lock;

	}

	public boolean canCraft(Role r) {
		
		return r.getName().equals(lock);
		
	}
	
}
