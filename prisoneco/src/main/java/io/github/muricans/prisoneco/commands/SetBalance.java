package io.github.muricans.prisoneco.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.prisoneco.PrisonEco;
import io.github.muricans.prisoneco.util.Account;
import io.github.muricans.prisontools.PrisonTools;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

public class SetBalance implements CMD {
    private PrisonEco plugin;

    public SetBalance(PrisonEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setbalance";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!NumberUtils.isNumber(args[0])) {
            sender.sendMessage("Please use numbers!");
        }
        if(args.length == 1) {
            PrisonTools.instance.database.setBalance(((Player) sender).getUniqueId().toString(), Double.valueOf(args[0]));
            sender.sendMessage("Set your balance to " + PrisonEco.formatBalance(Double.valueOf(args[0])));
        } else if(args.length > 1) {
            try {
                String target = args[1];
                if(plugin.cache.isCached(target)) {
                    UUID uuid = plugin.cache.getUUID(target);
                    PrisonTools.instance.database.setBalance(uuid.toString(), Double.valueOf(args[0]));
                    sender.sendMessage("Set balance of " + target + " to: $" + PrisonEco.formatBalance(Double.valueOf(args[0])));
                } else {
                    Account account = readJSON("https://api.mojang.com/users/profiles/minecraft/" + target);
                    if(account == null) {
                        sender.sendMessage("Could not find that player! Do they exist?");
                        return true;
                    }
                    UUID uuid = account.getUUID();
                    if(!Bukkit.getServer().getOfflinePlayer(uuid).hasPlayedBefore()) {
                        sender.sendMessage("That player has not played before!");
                        return true;
                    }
                    plugin.cache.cache(account.getName(), uuid.toString());
                    PrisonTools.instance.database.setBalance(uuid.toString(), Double.valueOf(args[0]));
                    sender.sendMessage("Set balance of " + target + " to: $" + PrisonEco.formatBalance(Double.valueOf(args[0])));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("Invalid usage of command: /setbalance <amount> [player]");
            return true;
        }
        return false;
    }
    private String readAll(Reader reader) {
        StringBuilder sb = new StringBuilder();
        int cp;
        try {
            while ((cp = reader.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private Account readJSON(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String text = readAll(rd);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.fromJson(text, Account.class);
        } finally {
            is.close();
        }
    }
}
