package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class DealMaker {
    private HashMap<UUID, Player> pairs = new HashMap<UUID, Player>(); // Owner saved as UUID in key
    private ArrayList<TradingWindow> currentDealInvs = new ArrayList<TradingWindow>();


    public boolean makeTradeOffer(Player owner, Player target) {
        MessageStrings messageStrings = Main.getPlugin().getMessageStrings();
        if(owner.getUniqueId().equals(target.getUniqueId())) {
            owner.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.CAN_NOT_TRADE_WITH_YOURSELF));
            return false;
        } else if(pairs.containsKey(owner.getUniqueId())) {
            owner.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.ALREADY_SENT_TRADE_REQUEST), pairs.get(owner.getUniqueId()).getName()));
            return false;
        } else {
            pairs.put(owner.getUniqueId(), target);
            target.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.YOU_GOT_A_NEW_TRADE_OFFER), owner.getName()));
            target.playSound(target.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 1.0f, 1.0f);
            owner.playSound(owner.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    Player opponent = Main.getPlugin().getDealMaker().cancelTrade(owner);
                    if(opponent != null) {
                        opponent.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.TRADE_REQUEST_BY_EXPIRED), owner.getName()));
                        opponent.playSound(opponent.getLocation(), Sound.ENTITY_RAVAGER_CELEBRATE, 1.0f, 1.0f);
                        owner.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.OWN_TRADE_REQUEST_EXPIRED), opponent.getName()));
                        owner.playSound(owner.getLocation(), Sound.ENTITY_WARDEN_HURT, 1.0f, 1.0f);
                    }
                }
            }, 20L * 60 * Main.getPlugin().getConfigValues().TIME_TRADING_REQUEST_SURVIVES);
            return true;
        }
    }

    public void acceptTrade(Player targeted, Player acceptedPlayer) {
        MessageStrings messageStrings = Main.getPlugin().getMessageStrings();
        if(pairs.containsKey(acceptedPlayer.getUniqueId()) && pairs.get(acceptedPlayer.getUniqueId()).equals(targeted)) {
            TradingWindow trade = new TradingWindow(acceptedPlayer, targeted);
            pairs.remove(acceptedPlayer.getUniqueId());
        } else {
            targeted.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.PLAYER_DID_NOT_SENT_YOU_A_TRADE_REQUEST));
        }
    }

    public void acceptTrade(Player targetted) {
        MessageStrings messageStrings = Main.getPlugin().getMessageStrings();
        if(pairs.containsValue(targetted)) {
            boolean found = false;
            for(Player v : pairs.values()) {
                if(v.equals(targetted)) {
                    if(!found)
                        found = true;
                    else {
                        targetted.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.YOU_GOT_MORE_THAN_ONE_OFFER));
                        return;
                    }
                }
            }
            for(UUID t : pairs.keySet()) {
                if(pairs.get(t).equals(targetted)) {
                    // Found trade offer pair
                    if(Bukkit.getPlayer(t) != null) { // Checking if trading partner is online
                        TradingWindow trade = new TradingWindow(Objects.requireNonNull(Bukkit.getPlayer(t)), targetted);
                    } else {
                        targetted.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.PLAYER_WENT_OFFLINE));
                    }
                    pairs.remove(t);
                    return;
                }
            }
        } else {
            targetted.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.YOU_GOT_NO_TRADING_OFFER));
        }
    }

    public Player cancelTrade(Player owner) {
        // returns opponent player
        if(pairs.containsKey(owner.getUniqueId())) {
            Player opposite = pairs.get(owner.getUniqueId());
            pairs.remove(owner.getUniqueId());
            return opposite;
        } else {
            return null;
        }
    }

    public void cancelOwnTrade(Player owner) {
        MessageStrings messageStrings = Main.getPlugin().getMessageStrings();
        Player opposite = cancelTrade(owner);
        if(opposite != null) {
            owner.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.YOU_CANCELED_YOUR_TRADE_REQUEST), opposite.getName()));
            opposite.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.OPPONENT_CANCELED_TRADE_OFFER), owner.getName()));
        } else {
            owner.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.NO_TRADES_TO_CANCEL));
        }
    }

    public void denyTrade(Player target) {
        MessageStrings messageStrings = Main.getPlugin().getMessageStrings();
        boolean found = false;
        for(UUID key : pairs.keySet()) {
            if(pairs.get(key).equals(target)) {
                if(Bukkit.getPlayer(key) != null) {
                    Bukkit.getPlayer(key).sendMessage(String.format(Main.PREFIX +
                            messageStrings.getTranslation(Translations.OPPONENT_DENIED_TRADE_REQUEST), target.getName()));
                    target.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.YOU_DECLINED_TRADE_REQUEST),
                            Bukkit.getPlayer(key).getName()));
                    found = true;
                }
                pairs.remove(key);
            }
        }
        if(!found)
            target.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.GOT_NO_REQUESTS_TO_DENY));
    }

    public void denyTrade(Player target, Player requester) {
        if(pairs.containsKey(requester.getUniqueId())) {
            requester.sendMessage(Main.PREFIX + target.getName() + " denied your trade request!");
            target.sendMessage(Main.PREFIX + "Declined trade request by " +
                    target.getName());
            pairs.remove(requester.getUniqueId());
            return;
        } else {
            target.sendMessage(Main.PREFIX + "You got no trade requests to deny!");
        }
    }

    public void addTradingWindow(TradingWindow tw) {
        this.currentDealInvs.add(tw);
    }

    public void removeTradingWindow(TradingWindow tw) {
        currentDealInvs.remove(tw);
    }

    public boolean isInventoryInList(Inventory inv) {
        if(inv != null) {
            for (TradingWindow c : currentDealInvs) {
                if (inv.equals(c.playerInventory) || inv.equals(c.oppositeInventory)) {
                    return true;
                }
            }
        }
        return false;
    }

    public TradingWindow getTradingWindow(Inventory inv) {
        if(inv != null) {
            for (TradingWindow c : currentDealInvs) {
                if (inv.equals(c.playerInventory) || inv.equals(c.oppositeInventory))
                    return c;
            }
        }
        return null;
    }

    public boolean isPlayerCurrentlyDealing(Player p) {
        for(TradingWindow c : currentDealInvs) {
            if(c.player.equals(p) || c.opposite.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public TradingWindow getTradingWindowByPlayer(Player p) {
        for(TradingWindow c : currentDealInvs) {
            if(p.equals(c.player) || p.equals(c.opposite))
                return c;
        }
        return null;
    }

    public ArrayList<TradingWindow> getCurrentDealInvs() {
        return this.currentDealInvs;
    }

    public void closeAllTrades() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(this.isPlayerCurrentlyDealing(p)) {
                TradingWindow tw = this.getTradingWindowByPlayer(p);
                tw.closeTrade(p);
            }
        }
    }
}
