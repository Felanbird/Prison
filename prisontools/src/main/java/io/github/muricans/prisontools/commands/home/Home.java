package io.github.muricans.prisontools.commands.home;

import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.murapi.api.config.Config;
import io.github.muricans.prisontools.PrisonTools;
import io.github.muricans.prisontools.util.Cooldown;
import io.github.muricans.prisontools.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Home implements CMD {
    private PrisonTools plugin;
    public static HashMap<UUID, BukkitTask> tasks = new HashMap<>();
    private HashMap<UUID, Long> cooldowns = new HashMap<>();

    public Home(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        int time = plugin.config.getInt("commands.home.cooldown");
        Cooldown cooldown = Util.hasCooldown(cooldowns, player.getUniqueId(), time);
        if(cooldown.hasCooldown()) {
            player.sendMessage("Please wait " + cooldown.getTimeLeft() + " more seconds before using this command again!");
            return true;
        }
        if(plugin.homes.get(player.getUniqueId().toString()) == null) {
            player.sendMessage("You have not yet set any homes!");
            return true;
        }
        Set<String> playerHomes = plugin.homes.getFile().getConfigurationSection(player.getUniqueId().toString()).getKeys(false);
        if(playerHomes == null) {
            player.sendMessage("You have not yet set any homes!");
            return true;
        }
        if(args.length == 0) {
            List<String> homes = new ArrayList<>();
            homes.addAll(playerHomes);
            if(homes.contains("home")) {
                wait(player, "home");
            } else {
                wait(player, homes.get(0));
            }
        } else {
            String home = args[0].toLowerCase();
            if(!playerHomes.contains(home)) {
                player.sendMessage("Could not find a home with that name registered to your player!");
                return true;
            }
            wait(player, home);
        }
        return false;
    }

    private void wait(Player p, String hName) {
        final int waitTime = plugin.config.getInt("commands.home.wait");
        final String homeName = hName;
        final Player player = p;

        player.sendMessage("Teleporting... Don't move for " + waitTime + " seconds.");
        if(!tasks.containsKey(player.getUniqueId())) {
            tasks.put(player.getUniqueId(), new BukkitRunnable() {
                @Override
                public void run() {
                    teleport(player, homeName);
                    player.sendMessage("Teleported you to home '" + homeName + "'");
                    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                    tasks.remove(player.getUniqueId());
                }
            }.runTaskLater(plugin, 20L * waitTime));
        }
    }

    private void teleport(Player player, String homeName) {
        Config homes = plugin.homes;
        String path = player.getUniqueId().toString() + "." + homeName;
        String worldName = homes.getString(path + ".world");
        int x = homes.getInt(path + ".x");
        int y = homes.getInt(path + ".y");
        int z = homes.getInt(path + ".z");
        if(Bukkit.getServer().getWorld(worldName) == null) {
            Bukkit.getServer().createWorld(new WorldCreator(player.getUniqueId().toString())).setDifficulty(Difficulty.PEACEFUL);
        }
        Location location = new Location(Bukkit.getServer().getWorld(worldName), x, y, z);
        player.teleport(location);
    }
}
