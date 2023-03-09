package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigValues {
    private final File customConfigFile;
    FileConfiguration cfg;
    public final int TIME_TRADING_REQUEST_SURVIVES;
    public final String LANGUAGE_VERSION;
    public boolean USE_WITHOUT_PERMISSION;
    public boolean ENABLE_TRADE_BY_RIGHTCLICK_PLAYER;

    // --- PATHS

    final String TIME_REQUEST_SURVIVES_PATH = "time_until_trade_request_gets_invalid";
    final String LANGUAGE_VERSION_PATH = "language_version";
    final String USE_WITHOUT_PERMISSION_PATH = "use_without_permission";
    final String ENABLE_TRADE_BY_RIGHTCLICK_PLAYER_PATH = "enable_trade_by_right_click_player";

    public ConfigValues(File file) {
        this.customConfigFile = file;
        cfg = Main.getPlugin().getCustomConfig();

        if(!cfg.contains(TIME_REQUEST_SURVIVES_PATH)) {
            cfg.set(TIME_REQUEST_SURVIVES_PATH, 1);
            this.saveCfg();
        }

        if(!cfg.contains((LANGUAGE_VERSION_PATH))) {
            cfg.set(LANGUAGE_VERSION_PATH, "en_us");
            this.saveCfg();
        }

        if(!cfg.contains((USE_WITHOUT_PERMISSION_PATH))) {
            cfg.set(USE_WITHOUT_PERMISSION_PATH, true);
            this.saveCfg();
        }

        if(!cfg.contains((ENABLE_TRADE_BY_RIGHTCLICK_PLAYER_PATH))) {
            cfg.set(ENABLE_TRADE_BY_RIGHTCLICK_PLAYER_PATH, true);
            this.saveCfg();
        }

        TIME_TRADING_REQUEST_SURVIVES = cfg.getInt(TIME_REQUEST_SURVIVES_PATH);
        LANGUAGE_VERSION = cfg.getString(LANGUAGE_VERSION_PATH);
        USE_WITHOUT_PERMISSION = cfg.getBoolean(USE_WITHOUT_PERMISSION_PATH);
        ENABLE_TRADE_BY_RIGHTCLICK_PLAYER = cfg.getBoolean(ENABLE_TRADE_BY_RIGHTCLICK_PLAYER_PATH);
    }

    public boolean toggleUseWithoutPermission() {
        boolean useWithoutPermission = true;
        if(!cfg.contains((USE_WITHOUT_PERMISSION_PATH))) {
            cfg.set(USE_WITHOUT_PERMISSION_PATH, useWithoutPermission);
        } else {
            useWithoutPermission = cfg.getBoolean(USE_WITHOUT_PERMISSION_PATH);
            cfg.set(USE_WITHOUT_PERMISSION_PATH, !useWithoutPermission);
        }
        this.saveCfg();
        return useWithoutPermission;
    }

    private void saveCfg() {
        try {
            cfg.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
