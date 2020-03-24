package io.github.muricans.prisontools.commands;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.prisontools.PrisonTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Rank implements CMD {
    private PrisonTools plugin;

    public Rank(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "rank";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        try {
            player.sendMessage(plugin.database.getRank(player.getUniqueId().toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
