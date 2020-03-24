package io.github.muricans.prisontools;

import io.github.muricans.murapi.api.cmd.CMDManager;
import io.github.muricans.murapi.api.config.Config;
import io.github.muricans.prisontools.commands.*;
import io.github.muricans.prisontools.commands.home.*;
import io.github.muricans.prisontools.commands.home.listener.HomeListener;
import io.github.muricans.prisontools.database.PrisonDatabase;
import io.github.muricans.prisontools.ranks.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public final class PrisonTools extends JavaPlugin implements Listener {
    public PrisonDatabase database;
    public HashMap<UUID, RankHandler> nextRanks = new HashMap<>();
    public Config homes;
    public Config config;
    public Config worlds;

    public static PrisonTools instance;

    public void onEnable() {
        instance = this;

        database = new PrisonDatabase("prison.db");
        database.connect();

        CMDManager cmdManager = new CMDManager(this);
        cmdManager.registerCommand(new Rankup(this));
        cmdManager.registerCommand(new World(this));
        cmdManager.registerCommand(new ResetWorld(this));
        cmdManager.registerCommand(new Home(this));
        cmdManager.registerCommand(new SetHome(this));
        cmdManager.registerCommand(new Homes(this));
        cmdManager.registerCommand(new DelHome(this));
        cmdManager.registerCommand(new Rank(this));

        homes = new Config(this, "homes.yml");
        homes.create();
        worlds = new Config(this, "worlds.yml");
        config = new Config(this, "config.yml");
        config.create();
        config.options().copyDefaults(true);
        config.loadFromJar();
        config.save();

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new HomeListener(), this);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent joinEvent) {
        if(joinEvent.getPlayer().hasPlayedBefore()) {
            try {
                String rank = database.getRank(joinEvent.getPlayer().getUniqueId().toString()).toLowerCase();
                switch (rank) {
                    case "inmate":
                        nextRanks.put(joinEvent.getPlayer().getUniqueId(), new Inmate(this));
                        break;
                    case "guard":
                        nextRanks.put(joinEvent.getPlayer().getUniqueId(), new Guard(this));
                        break;
                    case "warden":
                        nextRanks.put(joinEvent.getPlayer().getUniqueId(), new Warden());
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String uuid = joinEvent.getPlayer().getUniqueId().toString();
            database.registerPlayer(uuid);
            nextRanks.put(joinEvent.getPlayer().getUniqueId(), new Inmate(this));
        }
    }
}
