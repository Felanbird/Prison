package io.github.muricans.prisoneco.listeners;

import io.github.muricans.prisontools.PrisonTools;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.UUID;

public class SellSign {
    UUID uuid;
    Material material;
    double price;
    Chest chest;

    public SellSign(UUID ownerUUID, Material material, double price, Chest chest) {
        this.uuid = ownerUUID;
        this.material = material;
        this.price = price;
        this.chest = chest;
    }

    public boolean sell(Player ePlayer) {
        try {
            double ePlayerBal = PrisonTools.instance.database.getBalance(ePlayer.getPlayer().getUniqueId().toString());
            double rPlayerBal = PrisonTools.instance.database.getBalance(uuid.toString());
            if(rPlayerBal >= price) {
                if(ePlayer.getInventory().contains(material)) {
                    double rNewBal = rPlayerBal - price;
                    double eNewBal = ePlayerBal + price;
                    PrisonTools.instance.database.setBalance(uuid.toString(), rNewBal);
                    PrisonTools.instance.database.setBalance(ePlayer.getPlayer().getUniqueId().toString(), eNewBal);
                    chest.getInventory().addItem(new ItemStack(material, 1));
                    ePlayer.getInventory().setContents(removeItem(ePlayer));
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ItemStack[] removeItem(Player ePlayer) {
        ItemStack[] contents = ePlayer.getInventory().getContents();
        for(int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if(item.getType() == material) {
                item.setAmount(item.getAmount() - 1);
                contents[i] = item;
                break;
            }
        }
        return contents;
    }
}
