package io.github.muricans.prisoneco.pickaxe;

import io.github.muricans.murapi.api.Color;
import io.github.muricans.prisoneco.PrisonEco;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

public class EnhancedPickaxe {
    private static PrisonEco plugin;

    public static void initiate(PrisonEco p) {
        plugin = p;
        NamespacedKey enhancerKey = new NamespacedKey(plugin, "enhancer");
        ShapedRecipe enhancerRecipe = new ShapedRecipe(enhancerKey, enhancer());
        enhancerRecipe.shape("RRR", "IDI", "GRG");
        enhancerRecipe.setIngredient('R', Material.REDSTONE);
        enhancerRecipe.setIngredient('I', Material.IRON_INGOT);
        enhancerRecipe.setIngredient('D', Material.DIAMOND);
        enhancerRecipe.setIngredient('G', Material.GOLD_INGOT);
        plugin.getServer().addRecipe(enhancerRecipe);
        plugin.getServer().addRecipe(enhancedPickaxeRecipe());
    }

    protected static ItemStack enhancer() {
        ItemStack enhancer = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta enhancerMeta = enhancer.getItemMeta();
        enhancerMeta.setDisplayName(Color.addColor("&cEnhancer"));
        enhancer.setItemMeta(enhancerMeta);
        return enhancer;
    }

    protected static ItemStack enhancedPickaxe() {
        ItemStack enhancedPick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta enhancedPickMeta = enhancedPick.getItemMeta();
        enhancedPickMeta.setDisplayName(Color.addColor("&cEnhanced &bPickaxe"));
        enhancedPickMeta.addEnchant(Enchantment.DIG_SPEED, 1, false);
        enhancedPickMeta.setUnbreakable(true);
        enhancedPick.setItemMeta(enhancedPickMeta);
        return enhancedPick;
    }

    protected static ShapedRecipe enhancedPickaxeRecipe() {
        NamespacedKey enhancedPickKey = new NamespacedKey(plugin, "enhanced_pickaxe");
        ShapedRecipe enhancedPickRecipe = new ShapedRecipe(enhancedPickKey, enhancedPickaxe());
        enhancedPickRecipe.shape("DE ", "AAA", "AAA");
        enhancedPickRecipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        enhancedPickRecipe.setIngredient('E', Material.GOLD_INGOT);
        enhancedPickRecipe.setIngredient('A', Material.AIR);
        return enhancedPickRecipe;
    }

}
