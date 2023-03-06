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
import java.util.Objects;

public class TradeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            final String WRONG_USAGE = Main.PREFIX + "§cWrong usage of the command /trade! Please use §6/trade <Name>§c or " +
                    "§6/trade accept§c, to accept an incoming trade.";
            final String GITHUB_URL = "§6https://https://github.com/Robby3St/TradePlugin/§r";

            Player p = (Player) sender;
            if(p.hasPermission("trade.trade") || p.hasPermission("trade.*")) {
                DealMaker dm = Main.getPlugin().getDealMaker();
                if(args.length == 1 && !args[0].replace(" ", "").equals("")) {
                    if(args[0].equalsIgnoreCase("accept")) {
                        dm.acceptTrade(p);
                        return true;
                    } else if(args[0].equalsIgnoreCase("cancel")) {
                        dm.cancelOwnTrade(p);
                    } else if(args[0].equalsIgnoreCase("deny")) {
                        dm.denyTrade(p);
                    } else if(Bukkit.getPlayer(args[0]) != null) {
                        Player opposite = Bukkit.getPlayer(args[0]);
                        boolean success = dm.makeTradeOffer(p, opposite);
                        if(success)
                            p.sendMessage(String.format("%s§aThe trade request was now sent to §6%s!", Main.PREFIX, args[0]));
                    } else if(args[0].equalsIgnoreCase("reload") && p.hasPermission("trade.reload")) {
                        Main.getPlugin().reloadConfig();
                        p.sendMessage(Main.PREFIX + "Reloaded the config!");
                    } else if(args[0].equalsIgnoreCase("author")) {
                        p.sendMessage(Main.PREFIX + "§aAuthor of the trade plugin is §6Robby3St. §a" +
                                "Find the plugin on §6GitHub: " + GITHUB_URL);
                    } else if(args[0].equalsIgnoreCase("version") && p.hasPermission("trade.version")
                            || p.hasPermission("trade.*")) {
                        p.sendMessage(Main.PREFIX + "§aThe current used version of the pluin is: §6" +
                                Main.getPlugin().getPluginMeta().getVersion() + ". §aYou can " +
                                "check for original newer versions here: " +
                                "§6GitHub: " + GITHUB_URL);
                    } else if(args[0].equalsIgnoreCase("download")) {
                        p.sendMessage(Main.PREFIX + "§aYou can download the original " +
                                "trade plugin by Robby3St here: " + GITHUB_URL);
                    } else {
                        p.sendMessage(String.format("%s§cCould not find a player with the name §6'%s'§c. Please use " +
                                "§6/trade <Name>§c or §6/trade accept§c, to accept an incoming trade!", Main.PREFIX, args[0]));
                    }
                } else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("accept")) {
                        if(Bukkit.getPlayer(args[0]) != null) {
                            dm.acceptTrade(p, Objects.requireNonNull(Bukkit.getPlayer(args[0])));
                            return true;
                        } else {
                            p.sendMessage(String.format("%s§cCould not find a player with the name §6'%s'§c. Please use " +
                                    "§6/trade <Name>§c or §6/trade accept§c, to accept an incoming trade!", Main.PREFIX, args[0]));
                        }
                    } else if(args[0].equalsIgnoreCase("deny")) {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            dm.denyTrade(p, Objects.requireNonNull(Bukkit.getPlayer(args[1])));
                        } else {
                            p.sendMessage(Main.PREFIX + "§cCould not find a player with that name!");
                        }
                    }
                } else {
                    p.sendMessage(WRONG_USAGE);
                }
            } else {
                p.sendMessage(MessageStrings.NO_PERMISSION);
            }
        } else {
            if(args[0].equalsIgnoreCase("reload")) {
                Main.getPlugin().reloadConfig();
                sender.sendMessage("Reload the config!");
            } else
                sender.sendMessage("You must be a player, to do this!");
        }
        return true;
    }
}
