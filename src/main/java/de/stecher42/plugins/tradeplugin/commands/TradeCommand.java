package de.stecher42.plugins.tradeplugin.commands;

import de.stecher42.plugins.tradeplugin.main.Main;
import de.stecher42.plugins.tradeplugin.utils.DealMaker;
import de.stecher42.plugins.tradeplugin.utils.MessageStrings;
import de.stecher42.plugins.tradeplugin.utils.Translations;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TradeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        MessageStrings messageStrings = Main.getPlugin().getMessageStrings();

        if(sender instanceof Player) {
            final String WRONG_USAGE = Main.PREFIX + messageStrings.getTranslation(Translations.WRONG_USAGE);
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
                            p.sendMessage(Main.PREFIX + String.format(messageStrings.getTranslation(Translations.TRADE_REQUEST_SENT), args[0]));


                    } else if(args[0].equalsIgnoreCase("reload") && p.hasPermission("trade.reload")) {
                        Main.getPlugin().reloadConfig();
                        p.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.RELOADED_CONFIG));


                    } else if(args[0].equalsIgnoreCase("author")) {
                        p.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.AUTHOR_OF_PLUGIN_IS) + GITHUB_URL);


                    } else if(args[0].equalsIgnoreCase("version") && (p.hasPermission("trade.version")
                            || p.hasPermission("trade.*"))) {
                        p.sendMessage(String.format(Main.PREFIX + messageStrings.getTranslation(Translations.PLUGIN_VERSION_IS),
                                Main.getPlugin().getDescription().getVersion(), GITHUB_URL));


                    } else if(args[0].equalsIgnoreCase("download")) {
                        p.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.DOWNLOAD_PLUGIN_HERE) + GITHUB_URL);


                    } else {
                        p.sendMessage(String.format(messageStrings.getTranslation(
                                Translations.COULD_NOT_FIND_PLAYER_WITH_THAT_NAME_PLEASE_USE_COMMAND),
                                Main.PREFIX, args[0]));
                    }
                } else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("accept")) {
                        if(Bukkit.getPlayer(args[0]) != null) {
                            dm.acceptTrade(p, Objects.requireNonNull(Bukkit.getPlayer(args[0])));
                            return true;
                        } else {
                            p.sendMessage(String.format(messageStrings.getTranslation(
                                    Translations.COULD_NOT_FIND_PLAYER_WITH_THAT_NAME_PLEASE_USE_COMMAND),
                                    Main.PREFIX, args[0]));
                        }


                    } else if(args[0].equalsIgnoreCase("deny")) {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            dm.denyTrade(p, Objects.requireNonNull(Bukkit.getPlayer(args[1])));
                        } else {
                            p.sendMessage(Main.PREFIX + messageStrings.getTranslation(Translations.COULD_NOT_FIND_PLAYER_WITH_THAT_NAME));
                        }
                    }
                } else {
                    p.sendMessage(WRONG_USAGE);
                }
            } else {
                p.sendMessage(messageStrings.getTranslation(Translations.NO_PERMISSION));
            }
        } else {
            if(args[0].equalsIgnoreCase("reload")) {
                Main.getPlugin().reloadConfig();
                Main.getPlugin().reloadConfigValues();
                Main.getPlugin().getMessageStrings().reloadConfig();
                sender.sendMessage(messageStrings.getTranslation(Translations.RELOADED_CONFIG));
            } else
                sender.sendMessage(messageStrings.getTranslation(Translations.MUST_BE_A_PLAYER));
        }
        return true;
    }
}
