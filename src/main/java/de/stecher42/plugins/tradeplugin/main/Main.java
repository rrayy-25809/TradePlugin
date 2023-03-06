package de.stecher42.plugins.tradeplugin.main;

import de.stecher42.plugins.tradeplugin.commands.TradeCommand;
import de.stecher42.plugins.tradeplugin.utils.ConfigValues;
import de.stecher42.plugins.tradeplugin.utils.DealMaker;
import de.stecher42.plugins.tradeplugin.utils.MessageStrings;
import de.stecher42.plugins.tradeplugin.utils.TradingWindow;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {
    public final static String PREFIX = "§3§l[Trado] §r";
    private static Main plugin;
    private DealMaker dealMaker;
    private File customConfigFile;
    private FileConfiguration customConfig;
    private ConfigValues configValues;
    private MessageStrings messageStrings;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.plugin = this;
//        plugin.saveDefaultConfig();
        this.createCustomConfig();
        this.configValues = new ConfigValues(this.customConfigFile);


        this.dealMaker = new DealMaker();
        this.messageStrings = new MessageStrings();
        this.getCommand("trade").setExecutor(new TradeCommand());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new TradingWindow(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.dealMaker.closeAllTrades();
    }


    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public static Main getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public ConfigValues getConfigValues() {
        return this.configValues;
    }

    public DealMaker getDealMaker() {
        return this.dealMaker;
    }

    public MessageStrings getMessageStrings() {
        return this.messageStrings;
    }

    public void reloadConfigValues() {
        this.configValues = new ConfigValues(this.customConfigFile);
    }
}
