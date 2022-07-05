package net.cybercake.cyberapi.config;

import com.google.common.base.Charsets;
import net.cybercake.cyberapi.CyberAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Allows the user to create, edit, and get configurations
 * @since 3.0.0
 */
public class Config {

    private final String name;
    private final File file;
    private YamlConfiguration config;

    /**
     * Creates a configuration from the default 'config.yml'
     * @since 3.0.0
     */
    public Config() {
        this.name = "config";
        this.file = new File(CyberAPI.getInstance().getDataFolder(), name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Creates a configuration from a specified file name
     * @param fileName the file name, do not include the extension at the end, automatically assumes '.yml'
     * @since 3.0.0
     */
    public Config(String fileName) {
        this.name = fileName;
        this.file = new File(CyberAPI.getInstance().getDataFolder(), fileName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves the config
     * @throws IOException when an error occurs with saving the config
     * @since 3.0.0
     */
    public void save() throws IOException {
        this.values().save(file);
    }

    /**
     * Saves the defaults to the config from the "resources/" folder
     * @since 3.0.0
     */
    public void saveDefaults() {
        if(!file.exists()) CyberAPI.getInstance().saveResource(name + ".yml", false);
    }

    /**
     * Reloads the config and allows for the new values the user has edited to be obtained
     * @since 3.0.0
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);

        final InputStream configStream = CyberAPI.getInstance().getResource(name);
        if(configStream != null) config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configStream, Charsets.UTF_8)));
    }

    /**
     * Gets the name of the configuration file
     * @return the config file name
     * @since 3.0.0
     */
    public String getName() { return name; }

    /**
     * Gets the {@link File} of the configuration that is located in the plugin's data folder
     * @return the file in the "*\plugins\*your plugin*\file.example"
     * @since 3.0.0
     */
    public File getFile() { return file; }

    /**
     * Gets the {@link FileConfiguration} for the configuration.
     * @return the {@link FileConfiguration}, allows developer to get keys and values from the config
     * @since 3.0.0
     */
    public FileConfiguration values() {
        if(config == null) reload();
        return config;
    }

}
