package io.github.muricans.prisontools.commands;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.murapi.api.config.Config;
import io.github.muricans.prisontools.PrisonTools;
import io.github.muricans.prisontools.util.Cooldown;
import io.github.muricans.prisontools.util.Util;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ResetWorld implements CMD {
    private HashMap<UUID, Long> cooldowns = new HashMap<>();
    private PrisonTools plugin;
    private Config config;

    public ResetWorld(PrisonTools plugin) {
        this.plugin = plugin;
        config = plugin.config;
    }

    @Override
    public String getName() {
        return "resetworld";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        int time = config.getInt("commands.resetworld.cooldown");
        Cooldown cooldown = Util.hasCooldown(cooldowns, player.getUniqueId(), time);
        if(cooldown.hasCooldown()) {
            player.sendMessage("Please wait " + cooldown.getTimeLeft() + " more seconds before using this command again!");
            return true;
        }
        if(Bukkit.getWorld(player.getUniqueId().toString()) == null){
            player.sendMessage("You haven't created a world yet! Type /world to make one.");
        } else {
            World playerWorld = Bukkit.getServer().getWorld(player.getUniqueId().toString());
            player.sendMessage("Beginning deletion of world...");
            for(Player pl : playerWorld.getPlayers()) {
                pl.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
            }
            Bukkit.getServer().unloadWorld(playerWorld, false);
            File worldDir = new File(Bukkit.getServer().getWorldContainer(), player.getUniqueId().toString());
            try {
                FileUtils.deleteDirectory(worldDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage("World deleted...");
            player.performCommand("world");
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        }
        return false;
    }
}
