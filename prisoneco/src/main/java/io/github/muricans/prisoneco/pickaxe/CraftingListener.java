package io.github.muricans.prisoneco.pickaxe;

import io.github.muricans.murapi.api.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftingListener implements Listener {

    @EventHandler
    public void onCraftEvent(CraftItemEvent e) {
        CraftingInventory inventory = e.getInventory();
        if(!(inventory.getRecipe() instanceof ShapedRecipe)) return;
        ShapedRecipe sr = (ShapedRecipe) inventory.getRecipe();
        if(sr.getKey().equals(EnhancedPickaxe.enhancedPickaxeRecipe().getKey())) {
            ItemStack gold = inventory.getMatrix()[1];
            if(!gold.hasItemMeta() || !gold.getItemMeta().getDisplayName().equals(Color.addColor("&cEnhancer"))) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                e.setCancelled(true);
            }
        }
    }

}
