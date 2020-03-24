package io.github.muricans.prisontools.ranks;

import io.github.muricans.prisontools.PrisonTools;
import io.github.muricans.prisontools.database.PrisonDatabase;

import java.sql.SQLException;
import java.util.UUID;

public class Inmate implements RankHandler {
    private PrisonTools plugin;

    public Inmate(PrisonTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public String getName() {
        return "Inmate";
    }

    @Override
    public double getPrice() {
        return 100000;
    }

    @Override
    public int getBorderSize() {
        return 75;
    }

    @Override
    public void rankup(String playerUUID) {
        try {
            PrisonDatabase database = plugin.database;
            double balance = (double) database.getBalance(playerUUID);
            database.setRank(playerUUID, "Guard");
            database.setBalance(playerUUID, balance - getPrice());
            plugin.nextRanks.remove(UUID.fromString(playerUUID));
            plugin.nextRanks.put(UUID.fromString(playerUUID), new Guard(plugin));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
