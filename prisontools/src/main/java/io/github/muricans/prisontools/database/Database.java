package io.github.muricans.prisontools.database;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Database {
    private String name;
    public Connection connection;

    public Database(String name) {
        this.name = name;
    }

    public Connection getConnection() throws SQLException {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("PrisonTools").getDataFolder(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Connection con = DriverManager.getConnection("jdbc:sqlite:" + file);
        return con;
    }

}
