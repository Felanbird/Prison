package io.github.muricans.prisontools.commands.home;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.murapi.api.config.Config;
import io.github.muricans.prisontools.PrisonTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHome implements CMD {
    private PrisonTools plugin;

    public SetHome(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        String name;
        Config homes = plugin.homes;
        if(args.length == 0) {
            name = "home";
        } else {
            name = args[0].toLowerCase();
        }
        homes.set(player.getUniqueId().toString() + "." + name + ".world", player.getWorld().getName());
        homes.set(player.getUniqueId().toString() + "." + name + ".x", player.getLocation().getBlockX());
        homes.set(player.getUniqueId().toString() + "." + name + ".y", player.getLocation().getBlockY());
        homes.set(player.getUniqueId().toString() + "." + name + ".z", player.getLocation().getBlockZ());
        homes.reload();
        player.sendMessage("New home with the name of '" + name + "' has been set!");
        return false;
    }
}
