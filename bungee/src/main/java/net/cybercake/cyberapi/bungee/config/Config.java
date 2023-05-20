package net.cybercake.cyberapi.bungee.config;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Allows the user to create, edit, and get configurations
 * @since 15
 */
public class Config {

    private final String name;
    private final File file;
    private Configuration config;
    private boolean copyDefaults;

    /**
     * Creates a configuration from the default 'config.yml'
     * @since 15
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
     * @since 15
     */
    public Config(String fileName) {
        if(!CyberAPI.getInstance().getDataFolder().exists())
            CyberAPI.getInstance().getDataFolder().mkdir();

        this.name = fileName;
        this.file = new File(CyberAPI.getInstance().getDataFolder(), fileName + ".yml");
        this.config = null;
    }

    /**
     * Creates a configuration file from an already-defined {@link File}
     * @param file the {@link File} to create the config file at
     * @since 92
     */
    public Config(File file) {
        if(!CyberAPI.getInstance().getDataFolder().exists())
            CyberAPI.getInstance().getDataFolder().mkdir();

        this.name = file.getName();
        this.file = file;
        this.config = null;
    }

    /**
     * Creates a configuration file from an already-defined {@link File parent} and child file-name
     * @param parent the {@link File} that is the parent of {@code fileName}
     * @param fileName the file name, do not include the extension at the end, automatically assumes '.yml'
     * @since 92
     */
    public Config(File parent, String fileName) {
        if(!CyberAPI.getInstance().getDataFolder().exists())
            CyberAPI.getInstance().getDataFolder().mkdir();

        this.name = fileName;
        this.file = new File(parent, fileName);
        this.config = null;
    }

    /**
     * Saves the configuration
     * @since 15
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
     * Saves the defaults for this configuration file. If the file already exists in the plugin's folder, it will simply do nothing.
     * <br> <br>
     * There <b>is a difference</b> between this and {@link Config#copyDefaults() copyDefaults}, read about it <a href="https://www.spigotmc.org/threads/whats-the-difference-between-savedefaultconfig-copydefaults.301865/#post-2876283">by clicking here</a>
     * @since 15
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
     * Makes the plugin's configuration and default configuration indistinguishable in the server's eyes.
     * <br> <br>
     * <b>What does this mean?</b> If you have a plugin default configuration that reads like this (in your 'resources' folder usually):
     * <pre> <code>
     * config:
     *      first-example-value: true
     *      second-example-value: 5.0
     * </code> </pre>
     * But the configuration in the folder of the compiled plugin (the 'data folder', usually the folder is named after the plugin and in the 'plugins' folder) looks like this:
     * <pre> <code>
     * config:
     *      first-example-value: true
     * </code> </pre>
     * And you try to run the following code:
     * <pre> <code>
     * double secondExampleValue = CyberAPI.getMainConfig().values().getDouble("second-example-value");
     * </code> </pre>
     * The server will return the value '5.0', even though the value is not in the configuration in the data folder of the plugin itself. However, if a user makes an edit to a configuration value that is currently <em>in</em>
     * the data folder, those changes will still be reflected by the plugin.
     * <br> <br>
     * There <b>is a difference</b> between this and {@link Config#saveDefaults() saveDefaults}, read about it <a href="https://www.spigotmc.org/threads/whats-the-difference-between-savedefaultconfig-copydefaults.301865/#post-2876283">by clicking here</a>
     * @param shouldCopyDefaults sets whether the plugin should copy defaults whenever the {@link Config#save()} method is called -- note, this also runs the save function anyway
     * @since 121
     * @implNote the other method, {@link Config#copyDefaults()} only copies and saves once
     */
    public void copyDefaults(boolean shouldCopyDefaults) {
        this.copyDefaults = shouldCopyDefaults;
        if(this.copyDefaults) copyDefaults();
    }

    /**
     * Makes the plugin's configuration and default configuration indistinguishable in the server's eyes.
     * <br> <br>
     * <b>What does this mean?</b> If you have a plugin default configuration that reads like this (in your 'resources' folder usually):
     * <pre> <code>
     * config:
     *      first-example-value: true
     *      second-example-value: 5.0
     * </code> </pre>
     * But the configuration in the folder of the compiled plugin (the 'data folder', usually the folder is named after the plugin and in the 'plugins' folder) looks like this:
     * <pre> <code>
     * config:
     *      first-example-value: true
     * </code> </pre>
     * And you try to run the following code:
     * <pre> <code>
     * double secondExampleValue = CyberAPI.getMainConfig().values().getDouble("second-example-value");
     * </code> </pre>
     * The server will return the value '5.0', even though the value is not in the configuration in the data folder of the plugin itself. However, if a user makes an edit to a configuration value that is currently <em>in</em>
     * the data folder, those changes will still be reflected by the plugin.
     * <br> <br>
     * There <b>is a difference</b> between this and {@link Config#saveDefaults() saveDefaults}, read about it <a href="https://www.spigotmc.org/threads/whats-the-difference-between-savedefaultconfig-copydefaults.301865/#post-2876283">by clicking here</a>
     * @since 122
     * @implNote the other method, {@link Config#copyDefaults(boolean)} sets it to continuously copy defaults
     */
    public void copyDefaults() {
        try {
            Configuration defaultConfig =
                    ConfigurationProvider.getProvider(YamlConfiguration.class)
                            .load(CyberAPI.getInstance().getResourceAsStream(name + ".yml"));
            this.config =
                    ConfigurationProvider.getProvider(YamlConfiguration.class)
                            .load(
                                    this.file,
                                    defaultConfig
                            );
        } catch (IOException exception) {
            throw new ConfigurationException("An exception occurred with copying default configuration keys ('" + name + ".yml')!", exception);
        }
    }

    /**
     * @return whether the default configuration and normal configuration are unable to be told apart
     * @see Config#copyDefaults() 
     * @see Config#copyDefaults(boolean) 
     */
    public boolean shouldCopyDefaults() {
        return this.copyDefaults;
    }

    /**
     * Reloads the config and allows for the new values the user had to be obtained
     * @since 15
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
     * @since 15
     */
    public String getName() { return name; }

    /**
     * Gets the {@link File} of the configuration that is located in the plugin's data folder
     * @return the file in the "*\plugins\*your plugin*\file.example"
     * @since 15
     */
    public File getFile() { return file; }

    /**
     * Gets the {@link Configuration} for the configuration
     * @return the {@link Configuration}, allows developers to get keys and values from the config
     * @since 15
     */
    public Configuration values() {
        return this.config;
    }

}
