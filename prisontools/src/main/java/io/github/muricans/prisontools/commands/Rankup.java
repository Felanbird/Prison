package io.github.muricans.prisontools.commands;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.prisontools.PrisonTools;
import io.github.muricans.prisontools.ranks.RankHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Rankup implements CMD {
    private PrisonTools plugin;

    public Rankup(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "rankup";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        try {
            Player player = ((Player) sender).getPlayer();
            double balance = (double) plugin.database.getBalance(player.getUniqueId().toString());
            RankHandler rankHandler = plugin.nextRanks.get(player.getUniqueId());
            if(balance >= rankHandler.getPrice()) {
                sender.sendMessage("Ranked up");
                rankHandler.rankup(player.getUniqueId().toString());
            } else {
                sender.sendMessage("Not enough money in your balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
