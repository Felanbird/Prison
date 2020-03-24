package io.github.muricans.prisoneco.listeners;

import io.github.muricans.prisoneco.PrisonEco;
import io.github.muricans.prisontools.PrisonTools;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Sign;

import java.sql.SQLException;

public class SellSignListener implements Listener {
    private PrisonEco plugin;

    public SellSignListener(PrisonEco plugin) {
        this.plugin = plugin;
    }

    private boolean isSellSign(SignChangeEvent sign, Block behind) {
        if(behind.getType() != Material.CHEST) return false;
        if(sign.getLine(0).equalsIgnoreCase("[Sell]")) {
            Material sellMaterial = Material.matchMaterial(sign.getLine(1));
            if(sellMaterial == null || !sign.getLine(2).startsWith("$")) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean isSellSign(org.bukkit.block.Sign sign, Block behind) {
        if(behind.getType() != Material.CHEST) return false;
        if(sign.getLine(0).equalsIgnoreCase("[Sell]")) {
            Material sellMaterial = Material.matchMaterial(sign.getLine(1));
            if(sellMaterial == null || !sign.getLine(2).startsWith("$")) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Sign s = (Sign) e.getBlock().getState().getData();
        Block behind = e.getBlock().getRelative(s.getAttachedFace());
        if(isSellSign(e, behind)) {
            e.setLine(3, e.getPlayer().getName());
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock().getState() instanceof org.bukkit.block.Sign) {
            org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getClickedBlock().getState();
            Sign s = (Sign) e.getClickedBlock().getState().getData();
            Block behind = e.getClickedBlock().getRelative(s.getAttachedFace());
            if(isSellSign(sign, behind)) {
                try {
                    Chest chest = (Chest) behind.getState();
                    Player ePlayer = e.getPlayer();
                    SellSign sellSign = new SellSign(plugin.cache.getUUID(sign.getLine(3)), Material.matchMaterial(sign.getLine(1)), Double.valueOf(sign.getLine(2).substring(1)), chest);
                    if(e.getPlayer().getUniqueId() == sellSign.uuid) return;
                    double currentBal = PrisonTools.instance.database.getBalance(ePlayer.getUniqueId().toString());
                    double price = Double.valueOf(sign.getLine(2).substring(1));
                    boolean success = sellSign.sell(ePlayer);
                    if(success) {
                        e.getPlayer().sendMessage(Color.addColor("Sold item for +&2$&6" + PrisonEco.formatBalance(price) + "\n&rNew balance: &2$&6" + PrisonEco.formatBalance(currentBal + price)));
                    } else {
                        e.getPlayer().sendMessage(Color.addColor("&cCould not sell item!"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
