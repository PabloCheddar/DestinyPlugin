package utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigManager {
    /**
     * Class used to manage custom configuration file, handling of default configuration file is left to the Destinyplugin Class
     */

    private File file;
    private FileConfiguration config;

    public ConfigManager (Plugin plugin, String path) {
        this(plugin.getDataFolder().getAbsolutePath() + File.separator + path);
    }

    public ConfigManager (String path) {
        this.file = new File(path);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean save () {
        try {
            this.config.save(this.file);
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public File getFile() {
        return this.file;
    }
}
