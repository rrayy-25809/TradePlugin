package de.stecher42.plugins.tradeplugin.main;

import de.stecher42.plugins.tradeplugin.commands.TradeCommand;
import de.stecher42.plugins.tradeplugin.utils.DealMaker;
import de.stecher42.plugins.tradeplugin.utils.TradingWindow;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public final static String PREFIX = "§a§l[Trador] §r";
    private static Main plugin;
    private DealMaker dealMaker;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.plugin = this;
        this.dealMaker = new DealMaker();
        this.getCommand("trade").setExecutor(new TradeCommand());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new TradingWindow(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getPlugin() {
        return plugin;
    }

    public DealMaker getDealMaker() {
        return this.dealMaker;
    }
}
