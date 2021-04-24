package fr.slypy.aotuhc.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftShapedRecipe;
import org.bukkit.inventory.ItemStack;

import fr.slypy.aotuhc.AotUhc;
import fr.slypy.aotuhc.roles.Camps;
import fr.slypy.aotuhc.roles.RolesName;

public class RecipeUtils {
	
	public static List<AotShapedRecipe> recipes = new ArrayList<AotShapedRecipe>();
	
	public static void addRecipe(AotUhc plugin, String keyName, ItemStack result, String[] recipe, HashMap<String, Material> ingredients) throws Exception {
		
		if (recipe.length > 3 || recipe.length < 1) {
			
			throw new Exception("Invalid Recipe Exception");
			
		} else {
			
			ArrayList<String> recipeIngredients = new ArrayList<String>();
			
			for(String line : recipe) {
				
				if(line.length() > 3) {
					
					line = line.substring(0, 2);
					
				}
				
				line = line.replaceAll(" ", "");
				
				for(char letter : line.toCharArray()) {
					
					if(!recipeIngredients.contains("" + letter)) {
						
						recipeIngredients.add("" + letter);
						 
					}
					
				}
				
			}
			
			boolean allIngredientsInitialized = true;
			
			for(String recipeIngredient : recipeIngredients) {
				
				if(!ingredients.containsKey(recipeIngredient) || ingredients.get(recipeIngredient) == null) {
					
					allIngredientsInitialized = false;
					
				}
				
			}
			
			if(allIngredientsInitialized) {
				
				NamespacedKey key = new NamespacedKey(plugin, keyName);
				
				AotShapedRecipe r = new AotShapedRecipe(key, result, keyName);
				
				r.shape(recipe);
				
				for(String ingredient : recipeIngredients) {
					
					r.setIngredient(ingredient.toCharArray()[0], ingredients.get(ingredient));
					
				}
				
				recipes.add(r);
				
				Bukkit.addRecipe(r);
				
			} else {
				
				throw new Exception("Invalid Ingredients Exception");
				
			}
			
		}
		
	}
	
	public static void addRecipe(AotUhc plugin, String keyName, ItemStack result, String[] recipe, HashMap<String, Material> ingredients, RolesName lock) throws Exception {
		
		if (recipe.length > 3 || recipe.length < 1) {
			
			throw new Exception("Invalid Recipe Exception");
			
		} else {
			
			ArrayList<String> recipeIngredients = new ArrayList<String>();
			
			for(String line : recipe) {
				
				if(line.length() > 3) {
					
					line = line.substring(0, 2);
					
				}
				
				line = line.replaceAll(" ", "");
				
				for(char letter : line.toCharArray()) {
					
					if(!recipeIngredients.contains("" + letter)) {
						
						recipeIngredients.add("" + letter);
						 
					}
					
				}
				
			}
			
			boolean allIngredientsInitialized = true;
			
			for(String recipeIngredient : recipeIngredients) {
				
				if(!ingredients.containsKey(recipeIngredient) || ingredients.get(recipeIngredient) == null) {
					
					allIngredientsInitialized = false;
					
				}
				
			}
			
			if(allIngredientsInitialized) {
				
				NamespacedKey key = new NamespacedKey(plugin, keyName);
				
				AotRoleLockedShapedRecipe r = new AotRoleLockedShapedRecipe(key, result, keyName, lock);
				
				r.shape(recipe);
				
				for(String ingredient : recipeIngredients) {
					
					r.setIngredient(ingredient.toCharArray()[0], ingredients.get(ingredient));
					
				}
				
				recipes.add(r);
				
				Bukkit.addRecipe(r);
				
			} else {
				
				throw new Exception("Invalid Ingredients Exception");
				
			}
			
		}
		
	}
	
	public static void addRecipe(AotUhc plugin, String keyName, ItemStack result, String[] recipe, HashMap<String, Material> ingredients, Camps lock) throws Exception {
		
		if (recipe.length > 3 || recipe.length < 1) {
			
			throw new Exception("Invalid Recipe Exception");
			
		} else {
			
			ArrayList<String> recipeIngredients = new ArrayList<String>();
			
			for(String line : recipe) {
				
				if(line.length() > 3) {
					
					line = line.substring(0, 2);
					
				}
				
				line = line.replaceAll(" ", "");
				
				for(char letter : line.toCharArray()) {
					
					if(!recipeIngredients.contains("" + letter)) {
						
						recipeIngredients.add("" + letter);
						 
					}
					
				}
				
			}
			
			boolean allIngredientsInitialized = true;
			
			for(String recipeIngredient : recipeIngredients) {
				
				if(!ingredients.containsKey(recipeIngredient) || ingredients.get(recipeIngredient) == null) {
					
					allIngredientsInitialized = false;
					
				}
				
			}
			
			if(allIngredientsInitialized) {
				
				NamespacedKey key = new NamespacedKey(plugin, keyName);
				
				AotCampLockedShapedRecipe r = new AotCampLockedShapedRecipe(key, result, keyName, lock);
				
				r.shape(recipe);
				
				for(String ingredient : recipeIngredients) {
					
					r.setIngredient(ingredient.toCharArray()[0], ingredients.get(ingredient));
					
				}
				
				recipes.add(r);
				
				Bukkit.addRecipe(r);
				
			} else {
				
				throw new Exception("Invalid Ingredients Exception");
				
			}
			
		}
		
	}
	
	public static boolean containsRecipe(CraftShapedRecipe recipe) {
			
		for(AotShapedRecipe r : recipes) {
			
			if(r.getKey().equals(recipe.getKey())) {
					
				return true;
					
			}
				
		}
		
		return false;
		
	}
	
	public static AotShapedRecipe getRecipe(CraftShapedRecipe recipe) {
		
		for(AotShapedRecipe r : recipes) {
			
			if(r.getKey().equals(recipe.getKey())) {
					
				return r;
					
			}
				
		}
		
		return null;
		
	}
	
}