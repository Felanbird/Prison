package io.github.muricans.prisoneco.util;

import io.github.muricans.prisontools.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class AccountDatabase extends Database {

    public AccountDatabase(String name) {
        super(name);
    }

    public void connect() {
        try {
            connection = getConnection();
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TABLE if not exists cache(name TEXT, uuid TEXT, UNIQUE(name))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cache(String playerName, String playerUUID) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO cache(name, uuid) VALUES(?,?)");
            ps.setString(1, playerName.toLowerCase());
            ps.setString(2, playerUUID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isCached(String playerName) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT uuid FROM cache WHERE name = '" + playerName.toLowerCase() + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("uuid") != null;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UUID getUUID(String playerName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT uuid FROM cache WHERE name = '" + playerName.toLowerCase() + "'");
        try {
            ResultSet rs = ps.executeQuery();
            return UUID.fromString(rs.getString("uuid"));
        } finally {
            ps.close();
        }
    }

}
