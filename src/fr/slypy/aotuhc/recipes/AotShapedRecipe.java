package fr.slypy.aotuhc.recipes;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class AotShapedRecipe extends ShapedRecipe {

	String uuid = "";
	
	public AotShapedRecipe(NamespacedKey key, ItemStack result, String uuid) {
		
		super(key, result);

		this.uuid = uuid;
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof AotShapedRecipe) {
		
			AotShapedRecipe recipe = (AotShapedRecipe) o;
			
			if(recipe.getUUID() == this.getUUID()) {

				return true;
			
			}
		
		}
		
		return false;
		
	}

	public String getUUID() {
		
		return uuid;
		
	}

}