package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigValues {
    private File customConfigFile;
    FileConfiguration cfg;
    public final int TIME_TRADING_REQUEST_SURVIVES;

    public ConfigValues(File file) {
        this.customConfigFile = file;
        cfg = Main.getPlugin().getCustomConfig();
        final String TIME_REQUEST_SURVIVES_PATH = "time_until_trade_request_gets_invalid";

        if(!cfg.contains(TIME_REQUEST_SURVIVES_PATH)) {
            cfg.set(TIME_REQUEST_SURVIVES_PATH, 5);
            try {
                cfg.save(customConfigFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TIME_TRADING_REQUEST_SURVIVES = cfg.getInt(TIME_REQUEST_SURVIVES_PATH);
    }
}
