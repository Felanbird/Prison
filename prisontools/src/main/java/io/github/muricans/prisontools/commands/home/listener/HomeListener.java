package io.github.muricans.prisontools.commands.home.listener;

import io.github.muricans.prisontools.commands.home.Home;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class HomeListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        HashMap<UUID, BukkitTask> tasks = Home.tasks;
        if (tasks.containsKey(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()
                    || e.getFrom().getBlockY() != e.getTo().getBlockY()) {
                BukkitTask task = tasks.get(e.getPlayer().getUniqueId());
                task.cancel();
                tasks.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(Color.addColor("&cTeleportation canceled, you cannot move while being teleported!"));
            }
        }
    }

}
