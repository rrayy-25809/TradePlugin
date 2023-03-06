package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageStrings {
    File languageFile;
    FileConfiguration languageFileConfiguration;
    public static final String NO_PERMISSION = Main.PREFIX + "§cYou don't have the permissions to do that!";

    public MessageStrings() {
        this.createLanguageFile();
    }

    private void createLanguageFile() {
        languageFile = new File(Main.getPlugin().getDataFolder() + "/languages/",
                Main.getPlugin().getConfigValues().LANGUAGE_VERSION + ".yml");
        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            try {
                languageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        languageFileConfiguration = new YamlConfiguration();
        try {
            languageFileConfiguration.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
