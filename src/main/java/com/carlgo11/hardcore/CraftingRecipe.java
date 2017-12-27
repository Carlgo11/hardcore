package com.carlgo11.hardcore;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class CraftingRecipe {

    public static ShapelessRecipe setCraftingRecipeTNT()
    {
        ItemStack TNT = new ItemStack(Material.TNT, 1);
        ShapelessRecipe tntrecipe = new ShapelessRecipe(TNT);
        tntrecipe.addIngredient(Material.SULPHUR);
        return tntrecipe;
    }

}
