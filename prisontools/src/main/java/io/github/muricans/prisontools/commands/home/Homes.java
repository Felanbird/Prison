package io.github.muricans.prisontools.commands.home;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.prisontools.PrisonTools;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class Homes implements CMD {
    private PrisonTools plugin;

    public Homes(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "homes";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(plugin.homes.get(player.getUniqueId().toString()) == null) {
            player.sendMessage("You have not yet set any homes!");
            return true;
        }
        Set<String> playerHomes = plugin.homes.getFile().getConfigurationSection(player.getUniqueId().toString()).getKeys(false);
        if(playerHomes == null) {
            player.sendMessage("You have not yet set any homes!");
            return true;
        }
        String homes = StringUtils.join(playerHomes, ", ");
        player.sendMessage("Homes:\n" + homes);
        return false;
    }
}
