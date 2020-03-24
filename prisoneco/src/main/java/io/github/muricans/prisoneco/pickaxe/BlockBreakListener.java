package io.github.muricans.prisoneco.pickaxe;

import io.github.muricans.murapi.api.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(Color.addColor("&cEnhanced &bPickaxe"))) {
            ItemStack enhancedPickaxe = player.getInventory().getItemInMainHand();
            if(enhancedPickaxe.getItemMeta().hasLore()) {
                int level = 0;
                int blocksbroken = -1;
                String pattern = "[A-Za-z\\[\\]: ]+";
                for(String lore : enhancedPickaxe.getItemMeta().getLore()) {
                    if(lore.contains("Level")) {
                        level = Integer.valueOf(lore.trim().replaceAll(pattern, ""));
                    } else if(lore.contains("Blocks Broken")) {
                        blocksbroken = Integer.valueOf(lore.trim().replaceAll(pattern, ""));
                    }
                }
                if(level == 0 || blocksbroken == -1) {
                    System.out.println("Pickaxe level was 0 or blocksBroken was -1");
                    System.out.println(blocksbroken);
                    return;
                }
                PickaxeLevel pickaxeLevel = new PickaxeLevel(level, blocksbroken, enhancedPickaxe);
                pickaxeLevel.breakBlock();
                enhancedPickaxe.setItemMeta(pickaxeLevel.getPickaxeMeta());
            } else {
                ItemMeta meta = enhancedPickaxe.getItemMeta();
                ArrayList<String> lore = new ArrayList<>();
                lore.add("Level: [1]");
                lore.add("Blocks Broken: [0]");
                meta.setLore(lore);
                enhancedPickaxe.setItemMeta(meta);
            }
        }
    }

}
