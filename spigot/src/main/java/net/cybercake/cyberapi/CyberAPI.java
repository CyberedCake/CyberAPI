package net.cybercake.cyberapi;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.cybercake.cyberapi.basic.BetterStackTraces;
import net.cybercake.cyberapi.basic.NumberUtils;
import net.cybercake.cyberapi.basic.Time;
import net.cybercake.cyberapi.chat.Log;
import net.cybercake.cyberapi.chat.UChat;
import net.cybercake.cyberapi.config.Config;
import net.cybercake.cyberapi.player.CyberPlayer;
import net.cybercake.cyberapi.player.userhead.UserHeadSettingsBuilder;
import net.cybercake.cyberapi.server.CyberAPIListeners;
import net.cybercake.cyberapi.server.commands.CommandManager;
import net.cybercake.cyberapi.server.serverlist.ServerListInfo;
import net.cybercake.cyberapi.server.serverlist.ServerListInfoListener;
import net.cybercake.cyberapi.settings.FinalizedSettings;
import net.cybercake.cyberapi.settings.Settings;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * The main class for CyberAPI!
 * <br> <br>
 * Welcome to CyberAPI!
 * @author CyberedCake
 * @version use {@link CyberAPI#getCyberAPISpecific()} and then {@link CyberAPISpecific#getVersion()} to see the version!
 * @Website: <a href="https://github.com/CyberedCake/CyberAPI">github.com/CyberedCake/CyberAPI</a>
 * @see UChat
 * @see Log
 * @see CyberPlayer
 * @see Config
 * @see net.cybercake.cyberapi.basic.Sort
 * @see Time
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class CyberAPI extends JavaPlugin {

    public CyberAPI() {
        api = this;
    }

    private static CyberAPI api;
    public static CyberAPI getInstance() { return api; }

    /**
     * Start CyberAPI!
     * <br> <br>
     * This line of code **must** be at the top of your onEnable() in your main class. It **has**
     * to be the first thing that loads to allow for anything in CyberAPI to work correctly.
     * <br> <br>
     * In order to Start CyberAPI, you must provide a 'FinalizedSettings' object. To get this,
     * you must do:
     * <br>
     * <pre>startCyberAPI(new Settings().*include settings you want*.build());</pre>
     * <br>
     * This will get you your FinalizedSettings object that you can then input into CyberAPI
     * and will automatically be read by CyberAPI
     * <br>
     * FinalizedSettings can be null! That just means all the settings will be their defaults.
     * @param settings the settings object as described in the method description
     * @since 3.0.0
     */
    protected CyberAPI startCyberAPI(@Nullable FinalizedSettings settings) {
        long mss = System.currentTimeMillis();
        this.serverStarted = mss;

        log = new APILog(); // for private use only

        if(settings == null) settings = new Settings().build(); // use default values
        this.settings = settings;
        log.verbose("Starting CyberAPI...");
        log.verbose("-> with settings " + ChatColor.DARK_GRAY + settings.getData());

        // load all support variables, so they're not 'null'
        // automatically sets the variables in the methods
        getAdventureAPISupport();
        getMiniMessageSupport();
        getLuckPermsSupport();
        getProtocolLibSupport();

        if(getProtocolLibSupport().equals(Settings.FeatureSupport.SUPPORTED)) {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            new ServerListInfoListener().init();
            registerListener(new CyberAPIListeners());
        }

        CyberAPISpecific specific = getCyberAPISpecific();

        log.info(specific.getVersionString()); // print version string and print build information if user set CyberAPI to be verbose
        if(isVerbose()) specific.printBuildInformation();

        specific.checkForUpdates(); // check for CyberAPI updates

        log.verbose("Finished! CyberAPI took " + (System.currentTimeMillis()-mss) + "ms to start.");
        return this;
    }

    // variables
    private long serverStarted;

    private FinalizedSettings settings;

    private APILog log;

    private Config mainConfig = null;
    private final HashMap<String, Config> configs = new HashMap<>();

    private Settings.FeatureSupport adventureAPISupport = null;
    private Settings.FeatureSupport miniMessageSupport = null;
    private Settings.FeatureSupport luckPermsSupport = null;
    private Settings.FeatureSupport protocolLibSupport = null;

    /**
     * Gets the finalized settings CyberAPI is using to determine the developer's preferences
     * @return the finalized settings
     * @since 3.0.0
     */
    public FinalizedSettings getSettings() { return settings; }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * Should the server be verbose?
     * @return {@code getSettings().getBoolean("verbose")}
     * @since 3.0.0
     */
    public boolean isVerbose() { return getSettings().getBoolean("verbose"); }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * Should CyberAPI shut up and be quiet?
     * @return {@code getSettings().getBoolean("silenced")}
     * @since 3.0.0
     */
    public boolean isSilenced() { return getSettings().getBoolean("silenced"); }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * Should CyberAPI check for updates?
     * @return {@code getSettings().getBoolean("checkForUpdates")}
     * @since 3.0.0
     */
    public boolean shouldCheckForUpdates() { return getSettings().getBoolean("checkForUpdates"); }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * The name of the plugin that is different from the plugin.yml
     * @return {@code getSettings().getString("name")}
     * @since 3.0.0
     */
    public String getPluginName() { return getSettings().getString("name"); }
    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * The prefix of the plugin that is different from the plugin.yml
     * @return {@code getSettings().getString("prefix")}
     * @since 3.0.0
     */
    public String getPrefix() { return getSettings().getString("prefix"); }

    /**
     * Gets the name of the server implementation being used
     * @return the server type, usually looks like 'Spigot' or 'Paper'
     * @since 3.0.0
     */
    public String getServerType() {
        return Bukkit.getServer().getName();
    }

    /**
     * The server type of the server along with the version
     * @return the server version string, my server shows 'git-Purpur-1668 (MC: 1.19)' (as of June 17th, 2022)
     * @since 3.0.0
     */
    public String getServerTypeVersion() {
        return Bukkit.getServer().getVersion();
    }

    /**
     * Gets the Bukkit version string that the server is using
     * @return the Bukkit version string, for example, my server shows '1.19-R0.1-SNAPSHOT' (as of June 17th, 2022)
     * @since 3.0.0
     */
    public String getBukkitVersionString() {
        return Bukkit.getServer().getBukkitVersion();
    }

    /**
     * Gets the version of the Minecraft server
     * @return the Minecraft server version, looks something like '1.19'
     * @since 3.0.0
     * @apiNote if you are using the PaperSpigot API, a more conventional way of doing this would be {@code Bukkit.getServer().getMinecraftVersion()} (as that would return a more standard '1.19')
     */
    public String getMinecraftVersion() {
        return Bukkit.getBukkitVersion().split("-")[0];
    }

    /**
     * <b>THIS METHOD IS DISABLED, IT WILL THROW AN {@link UnsupportedOperationException} WHENEVER YOU TRY TO UES IT</b>
     * <br> PLEASE USE {@link CyberAPI#getMainConfig()} AS AN ALTERNATIVE OR {@link CyberAPI#getConfig(String)}
     * @see CyberAPI#getMainConfig()
     * @see CyberAPI#getConfig(String)
     * @deprecated method disabled!
     * @since 3.0.0
     */
    @Override
    @Deprecated(forRemoval = true)
    public @NotNull FileConfiguration getConfig() {
        throw new UnsupportedOperationException("The method, `getConfig()`, is disabled. Please use `CyberAPI.getPlugin().getMainConfig()` or `CyberAPI.getPlugin().getConfig(name)`");
    }

    /**
     * Gets the main config of the server. If no config exists, create one and save.
     * @return the main {@link Config} (main config is always 'config.yml')
     * @since 3.0.0
     */
    public Config getMainConfig() {
         if(mainConfig == null) mainConfig = new Config();
         return mainConfig;
    }

    /**
     * Gets a config of the server. If no config exists, create one and save.
     * @param fileName the name of the config file, usually from the "resources/" folder
     * @return the {@link Config}
     * @since 3.0.0
     */
    public Config getConfig(String fileName) {
        if(!configs.containsKey(fileName)) configs.put(fileName, new Config(fileName));
        return configs.get(fileName);
    }

    @Override
    public void reloadConfig() {
        getMainConfig().reload();
    }

    @Override
    public void saveConfig() {
        try {
            getMainConfig().save();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to save the main configuration! (config.yml)", ex);
        }
    }

    @Override
    public void saveDefaultConfig() {
        getMainConfig().saveDefaults();
    }

    /**
     * Returns when the server started in Unix time.
     * @return the server start date, in unix time with milliseconds
     * @since 3.0.7
     */
    public long getServerStartedUnix() { return serverStarted; }

    /**
     * Returns when the server started with a specific time unit.
     * @param unit the {@link TimeUnit} to convert the unix time to
     * @return the unix time with the applied {@link TimeUnit}
     * @since 3.0.7
     */
    public long getServerStartedUnix(TimeUnit unit) { return (unit.convert(Duration.ofMillis(serverStarted))); }

    /**
     * Returns when the server started as formatted with {@link java.text.SimpleDateFormat}'s formats with a specified timezone offset
     * @param pattern the pattern to format with {@link java.text.SimpleDateFormat}
     * @param timeOffset the timezone offset
     * @return the human-readable server start-date with timezone offset
     * @since 3.0.7
     */
    public String getServerStartedDate(String pattern, int timeOffset) { return Time.getFormattedDateUnix(getServerStartedUnix(TimeUnit.SECONDS), pattern, timeOffset); }

    /**
     * Returns when the server started as formatted with {@link java.text.SimpleDateFormat}'s formats
     * @param pattern the pattern to format with {@link java.text.SimpleDateFormat}
     * @return the human-readable server start-date
     * @since 3.0.7
     */
    public String getServerStartedDate(String pattern) { return getServerStartedDate(pattern, 0); }

    /**
     * Returns how long the server has been online
     * @param showAll whether to show all units, see description of {@link Time#getBetterTimeDisplay(long, long, boolean)}
     * @return the time since the server has started
     * @since 3.0.7
     */
    public String getServerUptime(boolean showAll) { return Time.getBetterTimeDisplay(Time.getUnix(), getServerStartedUnix(TimeUnit.SECONDS), showAll); }

    /**
     * Returns how long the server has been online
     * @return the time since the server started
     * @since 3.0.7
     */
    public String getServerUptime() { return getServerUptime(true); }

    /**
     * Gets a new CyberPlayer. Please note: It's better to use {@link CyberPlayer} constructors or static methods instead of this method.
     * @param uuid the unique ID
     * @return the Cyber Player
     * @deprecated It's better to use {@link CyberPlayer} constructors or static methods instead of this method.
     * @since 3.0.0
     */
    @Deprecated
    public CyberPlayer getCyberPlayer(UUID uuid) {
        return new CyberPlayer(uuid);
    }

    /**
     * Gets the Adventure API support. This method assumes the best of the developer as if they marked Adventure Support as {@link Settings.FeatureSupport#SUPPORTED}, it will try to use Adventure API.
     * @return the {@link Settings.FeatureSupport} enum of the value
     * @since 3.0.0
     */
    public Settings.FeatureSupport getAdventureAPISupport() {
        if(adventureAPISupport == null) {
            adventureAPISupport = settings.getFeatureSupportStatus("adventureSupport");

            if(adventureAPISupport.equals(Settings.FeatureSupport.AUTO)) {
                try {
                    Class.forName("net.kyori.adventure.text.Component");
                    this.adventureAPISupport = Settings.FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.adventureAPISupport = Settings.FeatureSupport.UNSUPPORTED;
                }
                log.verbose("Adventure API support was set to auto, detected: " + adventureAPISupport.name());
            }
        }
        return this.adventureAPISupport;
    }

    /**
     * Gets the Mini Message support. This method assumes the best of the developer as if they marked Mini Message as {@link Settings.FeatureSupport#SUPPORTED}, it will allow the use of Mini Message methods.
     * @return the {@link Settings.FeatureSupport} enum of the value
     * @since 3.0.0
     */
    public Settings.FeatureSupport getMiniMessageSupport() {
        if(miniMessageSupport == null) {
            miniMessageSupport = settings.getFeatureSupportStatus("miniMessageSupport");

            if(miniMessageSupport.equals(Settings.FeatureSupport.AUTO)) {
                try {
                    Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
                    this.miniMessageSupport = Settings.FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.miniMessageSupport = Settings.FeatureSupport.UNSUPPORTED;
                }
                log.verbose("MiniMessage support was set to auto, detected: " + miniMessageSupport.name());
            }
        }
        return this.miniMessageSupport;
    }

    /**
     * Gets the LuckPerms support. This method assumes the best of the developer as if they marked LuckPerms Support as {@link Settings.FeatureSupport#SUPPORTED}, it will allow the use of LuckPerms.
     * @return the {@link Settings.FeatureSupport} enum of the value
     * @since 3.0.0
     */
    public Settings.FeatureSupport getLuckPermsSupport() {
        if(luckPermsSupport == null) {
            luckPermsSupport = settings.getFeatureSupportStatus("luckPermsSupport");

            if(luckPermsSupport.equals(Settings.FeatureSupport.AUTO)) {
                try {
                    Class.forName("net.luckperms.api.LuckPermsProvider");
                    this.luckPermsSupport = Settings.FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.luckPermsSupport = Settings.FeatureSupport.UNSUPPORTED;
                }
                log.verbose("LuckPerms support was set to auto, detected: " + luckPermsSupport.name());
            }
        }
        return this.luckPermsSupport;
    }

    /**
     * Gets the ProtocolLib support. This method assumes the best of the developer as if they marked ProtocolLib Support as {@link Settings.FeatureSupport#SUPPORTED}, it will allow the use of ProtocolLib.
     * @return the {@link Settings.FeatureSupport} enum of the value
     * @since 3.1.0
     */
    public Settings.FeatureSupport getProtocolLibSupport() {
        if(protocolLibSupport == null) {
            protocolLibSupport = settings.getFeatureSupportStatus("protocolLibSupport");

            if(protocolLibSupport.equals(Settings.FeatureSupport.AUTO)) {
                try {
                    Class.forName("com.comphenix.protocol.ProtocolManager");
                    this.protocolLibSupport = Settings.FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.protocolLibSupport = Settings.FeatureSupport.UNSUPPORTED;
                }
                log.verbose("ProtocolLib support was set to auto, detected: " + protocolLibSupport.name());
            }
        }
        return this.protocolLibSupport;
    }

    /**
     * Sends a title to a player with a title and subtitle
     * @param player the player to send the title to
     * @param title the title to send
     * @param subtitle the subtitle to send
     * @since 3.0.0
     * @apiNote if you support Adventure API, it will attempt to use Adventure API's {@link Audience#showTitle(Title)} feature
     */
    public void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 20, 100, 20);
    }

    /**
     * Sends a title to a player with specified settings
     * @param player the player to send the title to
     * @param title the title to send
     * @param subtitle the subtitle to send
     * @param fadeIn the amount of ticks to fadein
     * @param stay the amount of ticks to stay
     * @param fadeOut the amount of ticks to fadeout
     * @since 3.0.0
     * @apiNote if you support Adventure API, it will attempt to use Adventure API's {@link Audience#showTitle(Title)} feature
     */
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send
     * @since 3.0.0
     * @see CyberAPI#broadcast(String, String) 
     */
    public void broadcast(String message) {
        broadcast(message, "");
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param permission the permission required to see the message
     * @since 3.0.0
     * @see UChat#broadcast(String)
     * @see UChat#broadcast(String, String)
     */
    public void broadcast(String message, String permission) {
        UChat.broadcast(message, permission);
    }
    
    /**
     * Performs a command as a {@link Player} if {@link CommandSender} is not a player. Please include a '/' in your command when typing it here
     * @param sender the {@link CommandSender} to execute the command
     * @param command the command to execute, including a '/' in the beginning
     * @since 3.0.0
     */
    public void performCommand(CommandSender sender, String command) {
        if(!(sender instanceof Player player)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.substring(1)); return;
        }
        player.performCommand(command);
    }

    /**
     * Returns an instance of {@link ServerListInfo}, which allows you to change things like the MOTD, player count, icon, etc.
     * @return the {@link ServerListInfo} instance
     * @since 3.1.0
     * @apiNote requires ProtocolLib support
     */
    public ServerListInfo getServerListInfo() {
        return ServerListInfo.serverListInfo();
    }

    /**
     * Returns a list of online players in forms of {@link Player} objects
     * @return the online players in {@link Player} objects
     * @since 3.0.0
     */
    public List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
    }

    /**
     * Returns a list of online players in form of their usernames
     * @return the online players' usernames, a {@link List<String>}
     * @since 3.0.0
     */
    public List<String> getOnlinePlayersUsernames() {
        List<String> usernames = new ArrayList<>();
        for(Player player : getOnlinePlayers()) {
            usernames.add(player.getName());
        }
        return usernames;
    }

    /**
     * Gets the main world of the current spigot server
     * @return the main world of the spigot server
     * @since 3.0.0
     */
    public @Nullable World getMainWorld() {
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("server.properties");
            properties.load(in);
            String levelName = properties.getProperty("level-name");
            return Bukkit.getWorld(levelName);
        } catch (IOException ioException) {
            return null;
        }
    }

    /**
     * Plays a sound to a certain {@link CommandSender}. If the {@link CommandSender} is not a {@link Player}, the method returns.
     * @param sender the sender to play a sound to
     * @param sound the sound to play
     * @param volume the volume to play the sound at
     * @param pitch the pitch to play the sound at
     * @since 3.0.0
     */
    public void playSound(CommandSender sender, Sound sound, float volume, float pitch) {
        if(!(sender instanceof Player player)) return;

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Gets the distance between two {@link Location}s on the X and Z axis, but not the Y axis
     * @param location1 the first location
     * @param location2 the second location
     * @return the distance between {@code location1} and {@code location2}
     * @throws IllegalArgumentException when the two locations are not in the same world
     * @see CyberAPI#getDistance(Location, Location)
     * @since 3.0.0
     */
    public double get2DDistance(Location location1, Location location2) {
        if(!location1.getWorld().getName().equals(location2.getWorld().getName())) throw new IllegalArgumentException("The two locations, location1 and location2, must be in the same world!");
        return Math.sqrt(NumberConversions.square(location1.getX() - location2.getX()) + NumberConversions.square(location1.getZ() - location2.getZ()));
    }

    /**
     * Gets the distance between two {@link Location}s on all three axis'
     * @param location1 the first location
     * @param location2 the second location
     * @return the distance between {@code location1} and {@code location2}
     * @throws IllegalArgumentException when the two locations are not in the same world
     * @see CyberAPI#get2DDistance(Location, Location)
     * @since 3.0.0
     */
    public double getDistance(Location location1, Location location2) {
        if(!location1.getWorld().getName().equals(location2.getWorld().getName())) throw new IllegalArgumentException("The two locations, location1 and location2, must be in the same world!");
        return location1.distance(location2);
    }

    /**
     * Gets a player's {@link UUID} from a given {@link String} username
     * <br>
     * <b>Note: This is obtaining the {@link UUID} from a URL, meaning you should cache this or use asynchronous events</b>
     * @param name the name to retrieve the UUID from
     * @return the UUID associated with the name
     * @since 3.0.0
     */
    public UUID getUUID(String name) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            String strUUID = (((JsonObject)JsonParser.parseReader(reader)).get("id")).toString().replaceAll("\"", "");
            strUUID = strUUID.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            reader.close();
            return UUID.fromString(strUUID);
        } catch (ClassCastException | IOException exception) {
            throw new IllegalArgumentException("Unable to get the UUID of " + name, exception);
        }
    }

    /**
     * Gets a player's {@link String} username from a given {@link UUID}
     * <br>
     * <b>Note: This is obtaining the {@link UUID} from a URL, meaning you should cache this or use asynchronous events</b>
     * @param uuid the uuid to retrieve the name from
     * @return the name associated with the UUID
     * @since 3.0.0
     */
    public String getName(UUID uuid) {
        try {
            String name;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid).openStream()));
            name = (((JsonObject)JsonParser.parseReader(reader)).get("name")).toString().replaceAll("\"", "");
            reader.close();
            return name;
        } catch (ClassCastException | IOException exception) {
            throw new IllegalArgumentException("Unable to get the username of " + uuid.toString(), exception);
        }
    }

    /**
     * Gets the top-most block that isn't 'empty' (using {@link Block#isEmpty()}) {@literal <}-- this method assumes 'yStartChecking' is the max height of the world
     * @param location the location to check, will start at y=*world height*
     * @return the new location that would be the highest block that isn't {@link Block#isEmpty()}
     * @since 3.0.0
     */
    public Location getTopBlock(@NotNull Location location) {
        return getTopBlock(location, location.getWorld().getMaxHeight());
    }

    /**
     * Gets the top-most block that isn't 'empty' (using {@link Block#isEmpty()})
     * @param location the location to check, will start at y=*world height*
     * @param yStartChecking where the method should start checking
     * @return the new location that would be the highest block that isn't {@link Block#isEmpty()}
     * @since 3.0.0
     */
    public Location getTopBlock(@NotNull Location location, long yStartChecking) {
        location = location.clone();
        location.setY(yStartChecking);
        for(int y=0; y<yStartChecking; y++) {
            if(location.getWorld().getBlockAt(location).isEmpty() || !(location.getWorld().getBlockAt(location.clone().add(0, 1, 0)).isEmpty())) {
                location.setY(location.getY() - 1);
            }else if(!location.getWorld().getBlockAt(location).isEmpty()) {
                location = location.add(0, 1, 0);
                return location;
            }
        }
        return null;
    }

    /**
     * Gets all registered recipes, {@link ShapedRecipe}s and {@link ShapelessRecipe}s.
     * <br>
     * <em>If anyone has any better idea on how to do this, let me know</em> - Cyber
     * <br> <br>
     * This code was originally from <a href="https://www.spigotmc.org/threads/learn-all-recipes.450378/#post-3871579/">Husky on SpigotMC Forums</a>, so thank you to them {@literal <3}
     * @return list of namespace keys that contain all recipes
     * @since 3.0.0
     */
    public List<NamespacedKey> getAllRegisteredRecipes() {
        List<NamespacedKey> keys = new ArrayList<>();
        getServer().recipeIterator().forEachRemaining(recipe -> {
            if(recipe instanceof ShapedRecipe shaped) {
                keys.add(shaped.getKey());
            }else if(recipe instanceof ShapelessRecipe shapeless) {
                keys.add(shapeless.getKey());
            }
        });
        return keys;
    }

    /**
     * Registers a command with the Spigot server
     * @param name the name of the command, without a slash
     * @param commandExecutor the executor to parse with the command
     * @see CyberAPI#registerTabCompleter(String, TabCompleter)
     * @since 3.0.0
     */
    public void registerCommand(String name, CommandExecutor commandExecutor) {
        this.getCommand(name).setExecutor(commandExecutor);
    }

    /**
     * Registers a tab completer that is associated with a command with the Spigot server
     * @param name the name of the command, without a slash
     * @param tabCompleter the tab completer to parse with the command
     * @see CyberAPI#registerCommand(String, CommandExecutor)
     * @since 3.0.0
     */
    public void registerTabCompleter(String name, TabCompleter tabCompleter) {
        this.getCommand(name).setTabCompleter(tabCompleter);
    }

    /**
     * Registers a {@link Listener} with the server
     * @param listener the listener to register
     * @since 3.0.0
     */
    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers a {@link Runnable} with the server, essentially a task
     * @param runnable the runnable to register
     * @param period the amount of ticks in between each execution
     * @since 3.0.0
     */
    public void registerRunnable(Runnable runnable, long period) {
        Bukkit.getScheduler().runTaskTimer(this, runnable, 20L, period);
    }

    /**
     * Gets the API logger, <b>only for use inside CyberAPI!</b>
     * @return the API logger
     */
    public APILog getAPILogger() {
        return log;
    }

    /**
     * CyberAPI logging methods only
     * <br> <br>
     *
     * Not documented because it's only for this class and not for anything else
     * @since 3.0.0
     */
    public class APILog {
        protected APILog() {}

        public void info(String message) { log(Level.INFO, ChatColor.RESET + message); }
        public void warn(String message) { log(Level.WARNING, message); }
        public void error(String message) { log(Level.SEVERE, ChatColor.RED + message); }

        public void verbose(String message) { if(isVerbose()) log(Level.INFO, ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "VERBOSE" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + message); }
        public void verbose(String type, String message) { if(isVerbose()) log(Level.INFO, ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "VERBOSE/" + type + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + message); }

        public void verboseException(String type, Throwable throwable) {
            for(String element : BetterStackTraces.get(throwable)) { verbose(type, element); }
        }
        public void verboseException(Throwable throwable) { verboseException("EXCEPTION", throwable); }

        public void log(Level level, String message) {
            if(Boolean.TRUE.equals(CyberAPI.this.settings.getBoolean("silenced"))) return;
            Log.log(level, ChatColor.LIGHT_PURPLE + "[CyberAPI]" + ChatColor.RESET + " " + message); }
    }

    /**
     * Returns an instance of {@link CyberAPISpecific}, which is things that are relating to CyberAPI only, not your plugin, CyberAPI
     * @return the {@link CyberAPISpecific} instance
     * @since 3.0.0
     */
    public CyberAPISpecific getCyberAPISpecific() { return new CyberAPISpecific(); }
    public class CyberAPISpecific {

        private static String latestVersion;
        private static int latestBuild;

        /**
         * Gets a new instance of {@link CyberAPISpecific}, but you're not supposed to use this, please instead use {@link CyberAPI#getCyberAPISpecific()}
         */
        private CyberAPISpecific() { }

        /**
         * Gets the gradle project's properties
         * @return the {@link Properties} of the Gradle project
         * @since 3.0.0
         */
        public Properties getBuildProperties() {
            InputStream in = getClassLoader().getResourceAsStream("spigot-build.properties");
            Properties properties = new Properties();
            try {
                properties.load(in);
            } catch (Exception exception){
                log.error("An error occurred whilst getting the project properties!");
                BetterStackTraces.print(exception);
            }
            return properties;
        }

        /**
         * Gets the current version of {@link CyberAPI}
         * @return the version of {@link CyberAPI}
         * @since 3.0.0
         */
        public String getVersion() {
            return "v" + getBuildProperties().getProperty("version");
        }

        /**
         * Gets the current build number of {@link CyberAPI}
         * @return the build number of {@link CyberAPI}
         * @since 3.0.0
         */
        public int getBuild() {
            return Integer.parseInt(getBuildProperties().getProperty("buildNumber"));
        }

        /**
         * Gets the latest version of {@link CyberAPI}
         * @return the latest version of {@link CyberAPI}
         * @since 3.0.0
         */
        public String getLatestVersion() { return latestVersion; }

        /**
         * Gets the latest build number of {@link CyberAPI}
         * @return the latest build number of {@link CyberAPI}
         * @since 3.0.0
         */
        public int getLatestBuild() { return latestBuild; }

        /**
         * Gets the website associated with {@link CyberAPI}
         * @return the website of {@link CyberAPI}
         * @since 3.0.0
         */
        public String getWebsite() { return getBuildProperties().getProperty("website"); }

        /**
         * Gets the version string that is shown to the user on startup
         * @return the version string from server startup
         * @since 3.0.0
         */
        public String getVersionString() {
            return "&fThe plugin &a" + getPluginName() + " &fis using CyberAPI version &e" + getVersion() + " &6(" + getBuild() + ")&f!";
        }

        /**
         * Checks for updates, prints output to the console
         * @since 3.0.0
         */
        public void checkForUpdates() {
            if(!shouldCheckForUpdates()) return;

            log.verbose("VERSION_CHECKER", "Checking for updates...");
            try {
                // thanks stack overflow (https://stackoverflow.com/a/21964051/15519255)
                URL url = new URL("https://api.github.com/repos/CyberedCake/CyberAPI/releases/latest");
                URLConnection connection = url.openConnection();
                connection.connect();

                JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream)connection.getContent()));
                try {
                    String tag = element.getAsJsonObject().get("tag_name").getAsString();
                    latestVersion = tag.split("-")[0];
                    latestBuild = Integer.parseInt(tag.split("-")[1]);
                } catch (Exception exception) {
                    log.error("An error occurred fetching the latest version for GitHub repo 'CyberAPI', tag=" + element.getAsJsonObject().get("tag_name").getAsString() + ": " + ChatColor.DARK_GRAY + exception.toString());
                }
            } catch (Exception exception) {
                log.error("Failed version checking for CyberAPI version " + getVersion() + "! " + ChatColor.DARK_GRAY + exception); getAPILogger().verboseException("VERSION_CHECKER", exception);return;
            }

            net.md_5.bungee.api.ChatColor DEFAULT_WARN_LOG = net.md_5.bungee.api.ChatColor.of(new java.awt.Color(249, 241, 165));
            if(getBuild() != latestBuild) {
                if(latestBuild - getBuild() > 0) {
                    log.warn( DEFAULT_WARN_LOG + "CyberAPI is outdated! The latest version is " + ChatColor.GREEN + latestVersion + DEFAULT_WARN_LOG + ", using " + ChatColor.RED + getVersion() + ChatColor.GRAY + " (" + (latestBuild -  getBuild()) + " version(s) behind!)" + DEFAULT_WARN_LOG + "!");
                    log.warn(DEFAULT_WARN_LOG + "Notify author of " + ChatColor.GOLD + getPluginName() + DEFAULT_WARN_LOG + " to download latest CyberAPI at " + ChatColor.LIGHT_PURPLE + getWebsite().replace("https://", ""));
                }
            }

            log.verbose("VERSION_CHECKER", "Checked for updates! (version=" + getCyberAPISpecific().getVersion() + ", latest=" + getCyberAPISpecific().getLatestVersion() + ")");
        }

        /**
         * Gets the build information from the inner-class {@link BuildInformation}
         * @return the build information
         * @since 3.0.0
         * @apiNote All of this build information relates to **CYBERAPI**, not your project
         */
        public BuildInformation getBuildInformation() { return new BuildInformation(); }
        public class BuildInformation {
            protected BuildInformation() {}

            /**
             * Gets the name of the build file that was used to build CyberAPI
             * @return the build file name
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildFile() { return getBuildProperties().getProperty("buildFile"); }

            /**
             * Gets the arch that was used to compile CyberAPI
             * @return the builder's arch
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderArch() { return getBuildProperties().getProperty("builderArch"); }

            /**
             * Gets the OS that was used to compile CyberAPI
             * @return the builder's OS
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderOS() { return getBuildProperties().getProperty("builderOS"); }

            /**
             * Gets the OS's version that was used to compile CyberAPI
             * @return the OS's version
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public double getBuilderVersion() { return Double.parseDouble(getBuildProperties().getProperty("builderVersion")); }

            /**
             * Gets the unix time that CyberAPI was last built, in MILLISECONDS (not SECONDS)
             * @return the unix time of CyberAPI compile
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public long getBuildUnix() { return Long.parseLong(getBuildProperties().getProperty("built")); }

            /**
             * Gets the {@link Date} that CyberAPI was built
             * @return the {@link Date} time of when CyberAPI was compiled
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public Date getBuildDate() { return new Date(getBuildUnix()); }

            /**
             * Gets the human-readable date of when CyberAPI was built (please specify a {@link java.text.SimpleDateFormat} pattern)
             * @param pattern the {@link java.text.SimpleDateFormat} pattern that should be applied to the human-readable date
             * @return the date at which CyberAPI was compiled, in human-readable time
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildDate(String pattern) { return getBuildDate(pattern, 0); }

            /**
             * Gets the human-readable date of when CyberAPI was built (please specify a {@link java.text.SimpleDateFormat} pattern) with a time offset
             * @param pattern the {@link java.text.SimpleDateFormat} pattern that should be applied to the human-readable date
             * @param timeOffset the time offset of the date
             * @return the date at which CyberAPI was compiled, in human-readable time
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildDate(String pattern, int timeOffset) { return Time.getFormattedDateUnix(getBuildUnix()/1000L, pattern, timeOffset); }


            /**
             * Gets the builder's Java vendor (where they got Java from) for CyberAPI
             * @return the builder's Java vendor
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderJavaVendor() { return getBuildProperties().getProperty("currentJavaVendor"); }

            /**
             * Gets the builder's Java version (what their java version was when they built CyberAPI) for CyberAPI
             * @return the builder's Java version
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderJavaVersion() { return getBuildProperties().getProperty("currentJavaVersion"); }

            /**
             * Gets the group ID used by CyberAPI
             * @return the group ID used when compiling CyberAPI
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getGroup() { return getBuildProperties().getProperty("group"); }

            /**
             * Gets the name of the CyberAPI project being used
             * @return the name of the CyberAPI project
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getName() { return getBuildProperties().getProperty("name"); }

            /**
             * Gets the targeted java version for CyberAPI's compilation
             * @return the targeted java version
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public int getTargetJavaVersion() { return Integer.parseInt(getBuildProperties().getProperty("targetJavaVersion")); }

            /**
             * Gets the user of the computer that was used to build CyberAPI
             * @return the user that built CyberAPI
             * @since 3.0.0
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildUser() { return getBuildProperties().getProperty("user"); }

            /**
             * Converts all properties to a {@link String} that looks like this:
             * <p>"property1=value1, property2=value2"</p>
             * @return all properties to a string
             * @since 3.0.0
             */
            public String toString() {
                List<String> items = new ArrayList<>();
                for(String key : getBuildProperties().stringPropertyNames()) {
                    items.add(key + "=\"" + getBuildProperties().get(key) + "\"");
                }
                return String.join(", ", items);
            }

            /**
             * Converts all properties to a {@link HashMap} that can be used by the user
             * @return the {@link HashMap} of all properties
             * @since 3.0.0
             */
            public HashMap<String, Object> getMappedProperties() {
                HashMap<String, Object> map = new HashMap<>();
                for(String key : getBuildProperties().stringPropertyNames()) {
                    map.put(key, getBuildProperties().get(key));
                }
                return map;
            }
        }

        /**
         * Prints the CyberAPI's compile information and debug information to console!
         * @see CyberAPISpecific#getPrettyBuildInformation()
         * @see CyberAPISpecific#getPrettyBuildInformation(boolean)
         * @since 3.0.0
         */
        public void printBuildInformation() {
            for(String line : getPrettyBuildInformation().split("\\n")) {
                log.info(line);
            }
        }

        /**
         * Gets a list of string's of the pretty build information, usually used by {@link CyberAPISpecific#printBuildInformation()}, but you can use it to print it to a player as well!
         * @return the pretty build information in string with \n form
         * @since 3.0.0
         * @see CyberAPISpecific#getPrettyBuildInformation(boolean)
         */
        public String getPrettyBuildInformation() {
            return getPrettyBuildInformation(true);
        }

        /**
         * Gets a list of string's of the pretty build information, usually used by {@link CyberAPISpecific#printBuildInformation()}, but you can use it to print it to a player as well!
         * @param separators whether separators should be present at the start and end of the message
         * @return the pretty build information in string with \n form
         * @since 3.0.0
         * @see CyberAPISpecific#printBuildInformation()
         */
        public String getPrettyBuildInformation(boolean separators) {
            StringBuilder builder = new StringBuilder();
            BuildInformation info = getBuildInformation();

            if(separators) builder.append("&9---------------------------------------------------------------------------------------------------------").append("\n&f");
            builder.append("&5CyberAPI Version String: &f").append(getVersionString()).append("\n");
            builder.append("&5Running on Server: &f")
                    .append("\n\t&c\u250D &fServer Type: &6").append(getServerType())
                    .append("\n\t&c\u251C &fServer Type And Version: &6").append(getServerTypeVersion())
                    .append("\n\t&c\u251C &fBukkit Version String: &6").append(getBukkitVersionString())
                    .append("\n\t&c\u2515 &fMinecraft Version: &6").append(getMinecraftVersion());
            builder.append(" ").append("\n").append(" ").append("\n");
            builder.append("&5Build Properties: &b").append(info.toString()
                    .replace("\"", "&c\"&r&a")
                    .replace("=", "&e=&r")
                    .replace(",", "&e,&r&b")).append("\n");
            builder.append(" ").append("\n");
            builder
                    .append("&6").append(info.getGroup()).append("&f.&c").append(info.getName())
                    .append("&f (&aCyberAPI&f) was compiled on &d")
                    .append(info.getBuildDate("MMM dd, yyyy HH:mm:ss z"))
                    .append(" &fby &b").append(info.getBuildUser())
                    .append(" &fwith &eJava ").append(info.getBuilderJavaVersion())
                    .append(" (").append(info.getBuilderJavaVendor()).append(")")
                    .append("\n");
            builder.append(" ").append("\n&f");
            builder.append("&fPlugin &3").append(getPluginName()).append(" &b(").append(getDescription().getVersion()).append("&b) &fhas requested this build information!").append("\n");
            if(separators) builder.append("&9---------------------------------------------------------------------------------------------------------");

            return UChat.chat(builder.toString());
        }

        /**
         * A tribute to one of the best Minecraft content creators in existence! <br> <br>
         * <em>"If I had another hundred lives, I think I would choose to be Technoblade again every single time."</em> <br>
         * - Alexander "Technoblade" (1999 - 2022)
         * @since 3.1.1
         */
        public void technoblade() {
            Log.info("&d-".repeat(60));
            UUID uuid = UUID.fromString("b876ec32-e396-476b-a115-8438d83c67d4"); // Technoblade UUID
            try {
                CyberPlayer cyberPlayer = new CyberPlayer(uuid);
                UChat.broadcast(cyberPlayer.getUserHead(
                        new UserHeadSettingsBuilder()
                                .showHelmet(true)
                                .lines(
                                        " ",
                                        " ",
                                        "&f\"If I had another hundred lives, I think",
                                        "&fI would choose to be &dTechnoblade &fagain",
                                        "&fevery single time.\"",
                                        " &7- Alexander \"Technoblade\" (1999 - 2022)"
                                        )
                                .build()
                ));
                int amount = 0;
                int distance = 16;
                int amountOfFireworks = 16;
                for(Player player : Bukkit.getOnlinePlayers()) {
                    for(int i = 0; i < 360; i += 360/amountOfFireworks) {
                        double angle = (i * Math.PI / 180);
                        double x = 16 * Math.cos(angle);
                        double z = 16 * Math.sin(angle);
                        Location location = player.getLocation().add(x, 1, z);
                        Firework firework = player.getWorld().spawn(location, Firework.class);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();
                        fireworkMeta.addEffect(FireworkEffect.builder()
                                .withColor(Color.fromRGB(245, 66, 215))
                                .flicker(true)
                                .trail(true)
                                .with(FireworkEffect.Type.STAR)
                                .build());
                        fireworkMeta.setDisplayName("Technoblade-Tribute-" + (amount > 999 ? "" : "0" ) + (amount > 99 ? "" : "0") + (amount > 9 ? "" : "0") + amount);
                        fireworkMeta.setPower(NumberUtils.randomInt(1, 3));
                        firework.setFireworkMeta(fireworkMeta);
                        amount++;
                    }
                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta meta = (SkullMeta) skull.getItemMeta();
                    meta.setOwnerProfile(Bukkit.createPlayerProfile(uuid, "Technoblade"));
                    meta.setDisplayName(UChat.chat("&d[PIG&b+++&d] Technoblade"));
                    meta.setLore(UChat.listChat(ChatPaginator.paginate(
                            "&7\"I hope you guys enjoyed my content, and that I made &7some of you laugh. And I hope you all " +
                                    "go on to live &7long, prosperous, and happy lives. Because I love you &7guys.\"\n\n&fYou will be in our hearts forever more, Technoblade, &fand " +
                                    "may you rest in peace and fly high!\n\n&dIn Memoriam: Technoblade &8(1999 - 2022)", 25).getLines()));
                    skull.setItemMeta(meta);
                    player.getInventory().addItem(skull);
                    amount++;
                }
            } catch (Exception exception) {
                getAPILogger().error("An error occurred with Technoblade's tribute! " + ChatColor.DARK_GRAY + exception);
                getAPILogger().verboseException("TECHNOBLADE", exception);
            }
            Log.info("&d-".repeat(60));
        }
    }

}