package io.github.muricans.prisontools.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class PrisonDatabase extends Database {

    public PrisonDatabase(String name) {
        super(name);
    }

    public void connect() {
        try {
            connection = getConnection();
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TABLE if not exists players(uuid TEXT, rank TEXT, balance REAL, UNIQUE(uuid))");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerPlayer(String playerUUID) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO players(uuid, rank, balance) VALUES(?,?,?)");
            ps.setString(1, playerUUID);
            ps.setString(2, "Inmate");
            ps.setDouble(3, 500.00);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UUID> getPlayers() {
        ArrayList<UUID> uuids = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM players");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                uuids.add(UUID.fromString(rs.getString("uuid")));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuids;
    }

    public String getRank(String playerUUID) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT rank FROM players WHERE uuid = '" + playerUUID + "'");
        try {
            ResultSet rs = ps.executeQuery();
            return rs.getString("rank");
        } finally {
            ps.close();
        }
    }

    public void setRank(String playerUUID, String newRank) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE players SET rank = ? WHERE uuid = ?");
            ps.setString(1, newRank);
            ps.setString(2, playerUUID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(String playerUUID) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT balance FROM players WHERE uuid = '" + playerUUID + "'");
        try {
            ResultSet rs = ps.executeQuery();
            return rs.getDouble("balance");
        } finally {
            ps.close();
        }
    }

    public void setBalance(String playerUUID, double newBalance) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE players SET balance = ? WHERE uuid = ?");
            ps.setDouble(1, newBalance);
            ps.setString(2, playerUUID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
