package io.github.muricans.prisoneco.pickaxe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PickaxeLevel {
    private int level;
    private int blocksBroken;
    private ItemStack pickaxe;

    public PickaxeLevel(int level, int blocksBroken, ItemStack pickaxe) {
        this.level = level;
        this.blocksBroken = blocksBroken;
        this.pickaxe = pickaxe;
    }

    public void breakBlock() {
        blocksBroken++;
        ItemMeta meta = getPickaxeMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Level: [" + level + "]");
        lore.add("Blocks Broken: [" + blocksBroken + "]");
        meta.setLore(lore);
        pickaxe.setItemMeta(meta);
    }

    public void levelUp() {

    }

    public ItemMeta getPickaxeMeta() {
        return pickaxe.getItemMeta();
    }

    public ItemStack getPickaxe() {
        return pickaxe;
    }

}
