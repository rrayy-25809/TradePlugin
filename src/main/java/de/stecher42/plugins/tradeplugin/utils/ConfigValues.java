package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigValues {
    private File customConfigFile;
    FileConfiguration cfg;
    public final int TIME_TRADING_REQUEST_SURVIVES;
    public final String LANGUAGE_VERSION;

    public ConfigValues(File file) {
        this.customConfigFile = file;
        cfg = Main.getPlugin().getCustomConfig();
        final String TIME_REQUEST_SURVIVES_PATH = "time_until_trade_request_gets_invalid";
        final String LANGUAGE_VERSION_PATH = "language_version";

        if(!cfg.contains(TIME_REQUEST_SURVIVES_PATH)) {
            cfg.set(TIME_REQUEST_SURVIVES_PATH, 1);
            this.saveCfg();
        }

        if(!cfg.contains((LANGUAGE_VERSION_PATH))) {
            cfg.set(LANGUAGE_VERSION_PATH, "en_us");
            this.saveCfg();
        }

        TIME_TRADING_REQUEST_SURVIVES = cfg.getInt(TIME_REQUEST_SURVIVES_PATH);
        LANGUAGE_VERSION = cfg.getString(LANGUAGE_VERSION_PATH);
    }

    private void saveCfg() {
        try {
            cfg.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
