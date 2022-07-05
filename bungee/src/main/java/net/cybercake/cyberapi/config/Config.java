package net.cybercake.cyberapi.config;

import net.cybercake.cyberapi.CyberAPI;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Allows the user to create, edit, and get configurations
 * @since 3.3
 */
public class Config {

    private final String name;
    private final File file;
    private Configuration config;

    /**
     * Creates a configuration from the default 'config.yml'
     * @since 3.3
     */
    public Config() {
        if(!CyberAPI.getInstance().getDataFolder().exists())
            CyberAPI.getInstance().getDataFolder().mkdir();

        this.name = "config";
        this.file = new File(CyberAPI.getInstance().getDataFolder(), name + ".yml");
        this.config = null;
    }

    /**
     * Creates a configuration from a specified file name
     * @param fileName the file name, do not include the extension at the end, automatically assumes '.yml'
     * @since 3.3
     */
    public Config(String fileName) {
        if(!CyberAPI.getInstance().getDataFolder().exists())
            CyberAPI.getInstance().getDataFolder().mkdir();

        this.name = fileName;
        this.file = new File(CyberAPI.getInstance().getDataFolder(), fileName + ".yml");
        this.config = null;
    }

    /**
     * Saves the configuration
     * @since 3.3
     * @throws ConfigurationException when an error occurs with saving the config
     */
    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(values(), file);
        } catch (IOException exception) {
            throw new ConfigurationException("An exception occurred with saving the configuration ('" + name + ".yml')!", exception);
        }
    }

    /**
     * Saves the defaults to the config from the "resources/" folder
     * @since 3.3
     * @throws ConfigurationException when an error occurs with saving the defaults for the config
     */
    public void saveDefaults() {
        try {
            if(!CyberAPI.getInstance().getDataFolder().exists())
                CyberAPI.getInstance().getDataFolder().mkdir();
            if(file.exists()) return;

            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = CyberAPI.getInstance().getResourceAsStream(name + ".yml");
            inputStream.transferTo(outputStream);
            outputStream.close();
            inputStream.close();
        } catch (IOException exception) {
            throw new ConfigurationException("An exception occurred with saving the configuration ('" + name + ".yml')!", exception);
        }
    }

    /**
     * Reloads the config and allows for the new values the user had to be obtained
     * @since 3.3
     * @throws ConfigurationException when an error occurrs with reloading the config
     */
    public void reload() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException exception) {
            throw new ConfigurationException("An exception occurred with saving the configuration ('" + name + ".yml')!", exception);
        }
    }

    /**
     * Gets the name of the configuration file
     * @return the config file name
     * @since 3.3
     */
    public String getName() { return name; }

    /**
     * Gets the {@link File} of the configuration that is located in the plugin's data folder
     * @return the file in the "*\plugins\*your plugin*\file.example"
     * @since 3.3
     */
    public File getFile() { return file; }

    /**
     * Gets the {@link Configuration} for the configuration
     * @return the {@link Configuration}, allows developers to get keys and values from the config
     * @since 3.3
     */
    public Configuration values() {
        return config;
    }

}
