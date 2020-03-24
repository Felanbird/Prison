package io.github.muricans.prisontools.ranks;

import io.github.muricans.prisontools.PrisonTools;
import io.github.muricans.prisontools.database.PrisonDatabase;

import java.sql.SQLException;
import java.util.UUID;

public class Guard implements RankHandler {
    PrisonTools plugin;

    public Guard(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public String getName() {
        return "Guard";
    }

    @Override
    public double getPrice() {
        return 100000;
    }

    @Override
    public int getBorderSize() {
        return 100;
    }

    @Override
    public void rankup(String playerUUID) {
        try {
            PrisonDatabase database = plugin.database;
            double balance = (double) database.getBalance(playerUUID);
            database.setRank(playerUUID, "Warden");
            database.setBalance(playerUUID, balance - getPrice());
            plugin.nextRanks.remove(UUID.fromString(playerUUID));
            plugin.nextRanks.put(UUID.fromString(playerUUID), new Warden());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
