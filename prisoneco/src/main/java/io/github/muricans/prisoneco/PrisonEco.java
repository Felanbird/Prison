package io.github.muricans.prisoneco;

import io.github.muricans.murapi.api.cmd.CMDManager;
import io.github.muricans.murapi.api.config.Config;
import io.github.muricans.prisoneco.commands.Balance;
import io.github.muricans.prisoneco.commands.Sell;
import io.github.muricans.prisoneco.commands.SetBalance;
import io.github.muricans.prisoneco.pickaxe.BlockBreakListener;
import io.github.muricans.prisoneco.pickaxe.CraftingListener;
import io.github.muricans.prisoneco.pickaxe.EnhancedPickaxe;
import io.github.muricans.prisoneco.listeners.BuySignListener;
import io.github.muricans.prisoneco.listeners.SellSignListener;
import io.github.muricans.prisoneco.util.AccountDatabase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.NumberFormat;

public final class PrisonEco extends JavaPlugin implements Listener {
    public Config config;
    private static NumberFormat format = NumberFormat.getInstance();
    public AccountDatabase cache;

    @Override
    public void onEnable() {
        cache = new AccountDatabase("cache.db");
        cache.connect();

        CMDManager cmdManager = new CMDManager(this);
        cmdManager.registerCommand(new Balance());
        cmdManager.registerCommand(new SetBalance(this));
        cmdManager.registerCommand(new Sell(this));

        config = new Config(this, "config.yml");
        config.create();
        config.options().copyDefaults(true);
        config.loadFromJar();
        config.save();

        EnhancedPickaxe.initiate(this);

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new BuySignListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SellSignListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    public static String formatBalance(double balance) {
        return format.format(balance);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(!cache.isCached(e.getPlayer().getName())) {
            cache.cache(e.getPlayer().getName(), e.getPlayer().getUniqueId().toString());
        }
    }

}
