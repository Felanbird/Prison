package io.github.muricans.prisontools.commands;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.prisontools.PrisonTools;
import io.github.muricans.prisontools.ranks.RankHandler;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class World implements CMD {
    private PrisonTools plugin;

    public World(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "world";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(Bukkit.getWorld(player.getUniqueId().toString()) == null) {
            player.sendMessage("Generating world...");
            org.bukkit.World playerWorld = Bukkit.getServer().createWorld(new WorldCreator((player.getUniqueId().toString()))
                    .generateStructures(false));
            playerWorld.setDifficulty(Difficulty.PEACEFUL);
            playerWorld.getWorldBorder().setCenter(playerWorld.getSpawnLocation());
            RankHandler currentRank = plugin.nextRanks.get(player.getUniqueId());
            playerWorld.getWorldBorder().setSize(currentRank.getBorderSize());
            player.sendMessage("World generated, teleporting now...");
            player.teleport(playerWorld.getSpawnLocation());
            player.sendMessage("Teleported to world successfully!");
        } else {
            player.teleport(Bukkit.getServer().getWorld(player.getUniqueId().toString()).getSpawnLocation());
            player.sendMessage("Teleported to world successfully!");
        }
        return false;
    }
}
