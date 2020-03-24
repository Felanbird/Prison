package io.github.muricans.prisoneco.commands;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.murapi.api.config.Config;
import io.github.muricans.prisoneco.PrisonEco;
import io.github.muricans.prisontools.PrisonTools;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Sell implements CMD {
    private PrisonEco plugin;

    public Sell(PrisonEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        try {
            Config config = plugin.config;
            Set<String> sellableItems = config.getFile().getConfigurationSection("prices").getKeys(false);
            List<Material> materials = new ArrayList<>();
            for(String itemName : sellableItems) {
                materials.add(Material.matchMaterial(itemName));
            }
            double balance = PrisonTools.instance.database.getBalance(player.getUniqueId().toString());
            final double oldBalance = balance;
            for(ItemStack item : player.getInventory().getContents()) {
                if(item == null || !materials.contains(item.getType())) continue;
                if(materials.contains(item.getType())) {
                    double price = config.getFile().getDouble("prices." + item.getType().name());
                    price = price * item.getAmount();
                    balance = balance + price;
                    player.getInventory().remove(item);
                }
            }
            PrisonTools.instance.database.setBalance(player.getUniqueId().toString(), balance);
            player.sendMessage(Color.addColor("Sold items for +&2$&6" + PrisonEco.formatBalance(balance - oldBalance) + "\n&rNew balance is: &2$&6" + PrisonEco.formatBalance(balance)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
