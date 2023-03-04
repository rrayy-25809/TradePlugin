package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DealMaker {
    private HashMap<UUID, Player> pairs = new HashMap<UUID, Player>(); // Owner saved as UUID in key
    private ArrayList<Inventory> currentDealInvs = new ArrayList<Inventory>();

    public void makeTradeOffer(Player owner, Player target) {
        if(pairs.containsKey(owner.getUniqueId())) {
            owner.sendMessage(Main.PREFIX + "You already send a trade request to " +
                    pairs.get(owner.getUniqueId()).getName() +
                    "! Please cancel the trade, by using /trade cancel first,");
        } else {
            pairs.put(owner.getUniqueId(), target);
            target.sendMessage(Main.PREFIX + "You got a new trade offer by " + owner.getName() +
                    "! Use /trade accept <Name>, to deal.");
        }
    }

    public void acceptTrade(Player targeted, Player acceptedPlayer) {
        if(pairs.containsKey(acceptedPlayer.getUniqueId()) && pairs.get(acceptedPlayer.getUniqueId()).equals(targeted)) {
            TradingWindow trade = new TradingWindow(acceptedPlayer, targeted);
            pairs.remove(acceptedPlayer.getUniqueId());
        } else {
            targeted.sendMessage(Main.PREFIX + "This player is not in a trade offer with you. Sorry.");
        }
    }

    public void acceptTrade(Player targetted) {
        if(pairs.containsValue(targetted)) {
            boolean found = false;
            for(Player v : pairs.values()) {
                if(v.equals(targetted)) {
                    if(!found)
                        found = true;
                    else {
                        targetted.sendMessage(Main.PREFIX + "You got more than 1 trade offer! " +
                                "Please use /trade accept <Name> to accept a specific trade by a player");
                        return;
                    }
                }
            }
            for(UUID t : pairs.keySet()) {
                if(pairs.get(t).equals(targetted)) {
                    // Found trade offer pair
                    if(Bukkit.getPlayer(t) != null) { // Checking if trading partner is online
                        TradingWindow trade = new TradingWindow(Bukkit.getPlayer(t), targetted);
                    } else {
                        targetted.sendMessage(Main.PREFIX + "Sorry, but this player went offline!");
                    }
                    pairs.remove(t);
                    return;
                }
            }
        } else {
            targetted.sendMessage(Main.PREFIX + "Sorry, but this player did't made you an trading offer!");
        }
    }

    public void cancelOwnTrade(Player owner) {
        if(pairs.containsKey(owner.getUniqueId())) {
            String opposite = pairs.get(owner.getUniqueId()).getName();
            pairs.remove(owner.getUniqueId());
            owner.sendMessage(Main.PREFIX + "You cancelled your trade with " + opposite + "!");
        } else {
            owner.sendMessage(Main.PREFIX + "Sorry, but you got no trade offers to cancel!");
        }
    }

    public void addInv(Inventory inv) {
        this.currentDealInvs.add(inv);
    }

    public void removeInv(Inventory inv) {
        currentDealInvs.remove(inv);
    }
}
