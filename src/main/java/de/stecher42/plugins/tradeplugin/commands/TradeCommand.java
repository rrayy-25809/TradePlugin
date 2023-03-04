package de.stecher42.plugins.tradeplugin.commands;

import de.stecher42.plugins.tradeplugin.main.Main;
import de.stecher42.plugins.tradeplugin.utils.DealMaker;
import de.stecher42.plugins.tradeplugin.utils.MessageStrings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TradeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            final String WRONG_USAGE = Main.PREFIX + "Wrong usage of the command /trade! Please use /trade <Name> or " +
                    "/trade accept, to accept an incoming trade.";

            Player p = (Player) sender;
            if(p.hasPermission("trade.trade")) {
                DealMaker dm = Main.getPlugin().getDealMaker();
                if(args.length >= 1 && args[0].replace(" ", "") != "") {
                    if(args[0].equals("accept")) {
                        dm.acceptTrade(p);
                        return true;
                    } else if(Bukkit.getPlayer(args[0]) != null) {
                        Player opposite = Bukkit.getPlayer(args[0]);
                        dm.makeTradeOffer(p, opposite);
                        p.sendMessage(String.format("%sThe trade request was now send to %s!", Main.PREFIX, args[0]));
                    } else {
                        p.sendMessage(String.format("%sCould not find a player with the name '%s'. Please use " +
                                "/trade <Name> or /trade accept, to accept an incoming trade!", Main.PREFIX, args[0]));
                    }
                } else {
                    p.sendMessage(WRONG_USAGE);
                }
            } else {
                p.sendMessage(MessageStrings.NO_PERMISSION);
            }
        } else {
            sender.sendMessage("You must be a player, to do this!");
        }
        return true;
    }
}
