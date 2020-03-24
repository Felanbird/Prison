package io.github.muricans.prisontools.commands.home;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.prisontools.PrisonTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class DelHome implements CMD {
    private PrisonTools plugin;

    public DelHome(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "delhome";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(plugin.homes.get(player.getUniqueId().toString()) == null) {
            player.sendMessage(Color.addColor("&cYou have not yet set any homes!"));
            return true;
        }
        Set<String> playerHomes = plugin.homes.getFile().getConfigurationSection(player.getUniqueId().toString()).getKeys(false);
        if(playerHomes == null) {
            player.sendMessage(Color.addColor("&cYou have not yet set any homes!"));
            return true;
        }
        String name;
        if(args.length == 0) {
            if(!playerHomes.contains("home")) {
                player.sendMessage(Color.addColor("&cPlease supply the name of a home!"));
                return true;
            }
            if(playerHomes.toArray().length == 1) {
                plugin.homes.set(player.getUniqueId().toString(), null);
                plugin.homes.reload();
                player.sendMessage(Color.addColor(&e"Home 'home' has been deleted!"));
            } else {
                plugin.homes.set(player.getUniqueId().toString() + ".home", null);
                plugin.homes.reload();
                player.sendMessage(Color.addColor(&e"Home 'home' has been deleted!"));
            }
        } else {
            name = args[0].toLowerCase();
            if(!playerHomes.contains(name)) {
                player.sendMessage(Color.addColor("&cCould not find a home with that name!"));
                return true;
            }
            if(playerHomes.toArray().length == 1) {
                plugin.homes.set(player.getUniqueId().toString(), null);
                plugin.homes.reload();
                player.sendMessage(Color.addColor("&cHome '" + name + "' has been deleted!"));
            } else {
                plugin.homes.set(player.getUniqueId().toString() + "." + name, null);
                plugin.homes.reload();
                player.sendMessage(Color.addColor("&cHome '" + name + "' has been deleted!"));
            }
        }
        return false;
    }
}
