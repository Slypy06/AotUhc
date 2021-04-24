package fr.slypy.aotuhc.recipes;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import fr.slypy.aotuhc.roles.Camps;
import fr.slypy.aotuhc.roles.Role;

public class AotCampLockedShapedRecipe extends AotShapedRecipe {

	Camps lock;
	
	public AotCampLockedShapedRecipe(NamespacedKey key, ItemStack result, String uuid, Camps lock) {
		
		super(key, result, uuid);
		
		this.lock = lock;
		
	}
	
	public boolean canCraft(Role r) {
		
		return r.getName().getCamp().equals(lock);
		
	}

}
