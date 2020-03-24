package io.github.muricans.prisoneco.commands;

import io.github.muricans.murapi.api.Color;
import io.github.muricans.murapi.api.cmd.CMD;
import io.github.muricans.prisoneco.PrisonEco;
import io.github.muricans.prisontools.PrisonTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Balance implements CMD {

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        try {
            if(args.length == 0) {
                double balance = PrisonTools.instance.database.getBalance(((Player) sender).getUniqueId().toString());
                sender.sendMessage("Your balance is: $" + PrisonEco.formatBalance(balance));
            } else {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if(target == null) {
                    sender.sendMessage(Color.addColor("Could not find that player! Are they online?"));
                    return true;
                }
                double balance = PrisonTools.instance.database.getBalance(target.getUniqueId().toString());
                sender.sendMessage("Balance of " + target.getName() + " is: $" + PrisonEco.formatBalance(balance));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
