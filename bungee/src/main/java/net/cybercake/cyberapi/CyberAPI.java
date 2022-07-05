package net.cybercake.cyberapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.cybercake.cyberapi.basic.BetterStackTraces;
import net.cybercake.cyberapi.basic.Time;
import net.cybercake.cyberapi.chat.Log;
import net.cybercake.cyberapi.chat.UChat;
import net.cybercake.cyberapi.config.Config;
import net.cybercake.cyberapi.player.BungeeTitle;
import net.cybercake.cyberapi.player.CyberPlayer;
import net.cybercake.cyberapi.player.settings.UserHeadSettings;
import net.cybercake.cyberapi.server.commands.CommandManager;
import net.cybercake.cyberapi.server.commands.ReflectionsConsoleFilter;
import net.cybercake.cyberapi.settings.FeatureSupport;
import net.cybercake.cyberapi.settings.Settings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * The main class for CyberAPI!
 * <br> <br>
 * Welcome to CyberAPI: Bungee!
 * @author CyberedCake
 * @version use {@link CyberAPI#getCyberAPISpecific()} and then {@link CyberAPISpecific#getVersion()} to see the version!
 * @Website: <a href="https://github.com/CyberedCake/CyberAPI">github.com/CyberedCake/CyberAPI</a>
 */
@SuppressWarnings({"unused"})
public class CyberAPI extends Plugin {

    public CyberAPI() { api = this; }
    private static CyberAPI api;

    /**
     * Gets the instance of your {@link Plugin} and {@link CyberAPI}
     * @return the {@link CyberAPI} instance, a.k.a. your main class
     * @since 3.3
     */
    public static CyberAPI getInstance() { return api; }

    /**
     * Gets the main configuration file values, using {@link Config#values()} and {@link CyberAPI#getConfig()}.
     * @return the main configuration file
     * @since 3.3
     */
    public static Configuration getConf() { return getInstance().getConfig().values(); }

    /**
     * Start CyberAPI!
     * <br> <br>
     * This line of code **must** be at the top of your onEnable() in your main class. It **has**
     * to be the first thing that loads to allow for anything in CyberAPI to work correctly.
     * <br> <br>
     * In order to Start CyberAPI, you must provide a '{@link Settings}' object. To get this,
     * you must do:
     * <br>
     * {@code startCyberAPI(Settings.builder().*include settings you want*.build());}
     * <br>
     * (this is here: {@link Settings#builder()})
     * <br> <br>
     * This will get you your {@link Settings} object that you can then input into CyberAPI
     * and will automatically be read by CyberAPI
     * <br>
     * {@link Settings} can be null! That just means all the settings will be their defaults.
     * @param settings the settings object as described in the method description
     * @since 3.3
     */
    protected CyberAPI startCyberAPI(@Nullable Settings settings) {
        long mss = System.currentTimeMillis();
        this.serverStarted = mss;

        log = new APILog(); // for private use only

        if(settings == null) settings = Settings.builder().build();
        this.settings = settings;
        log.verbose("Starting CyberAPI...");

        // load all support variables, so they're not 'null'
        // automatically sets the variable in these methods, so returned values are not used
        getLuckPermsSupport();

        ((Logger) LogManager.getRootLogger()).addFilter(new ReflectionsConsoleFilter());
        CommandManager.commandManager().init(settings.getCommandsPath());

        CyberAPISpecific specific = getCyberAPISpecific();

        log.info(specific.getVersionString());
        if(getSettings().isVerbose()) specific.printBuildInformation();

        specific.checkForUpdates();

        log.verbose("Finished! CyberAPI took " + (System.currentTimeMillis()-mss) + "ms to start.");
        return this;
    }

    // variables
    private long serverStarted;

    private Settings settings;

    private APILog log = null;

    private Config mainConfig = null;
    private final HashMap<String, Config> configs = new HashMap<>();

    private FeatureSupport luckPermsSupport = null;

    /**
     * Gets the settings CyberAPI is using to determine the developer's preferences
     * @return the settings
     * @since 3.3
     */
    public Settings getSettings() { return settings; }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * The name of the plugin that is different from the bungee.yml
     * @return {@code getSettings().getName()}
     * @since 3.3
     */
    public String getPluginName() { return getSettings().getName(); }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * The prefix that will show before all logging methods
     * @return {@code getSettings().getPrefix()}
     * @since 3.3
     */
    public String getPrefix() { return getSettings().getPrefix(); }

    /**
     * Gets the name of the server implementation being used
     * @return the server type, usually looks like 'Bungeecord' or 'Waterfall'
     * @since 3.3
     */
    public String getServerType() { return ProxyServer.getInstance().getName(); }

    /**
     * Gets the server type of the server along with the version
     * @return the server version string, usually looks like 'git:Waterfall-Bootstrap:1.19-R0.1-SNAPSHOT:fc30291:498'
     * @since 3.3
     */
    public String getServerTypeVersion() { return ProxyServer.getInstance().getVersion(); }

    /**
     * Gets the version of the Minecraft server
     * @return the Minecraft server version, for example, my server shows '{@code 1.8.x, 1.9.x, 1.10.x, 1.11.x, 1.12.x, 1.13.x, 1.14.x, 1.15.x, 1.16.x, 1.17.x, 1.18.x, 1.19.x}' {@literal <}-- as it's all the versions the server supports
     * @since 3.3
     */
    @SuppressWarnings({"deprecation"}) public String getMinecraftVersion() { return ProxyServer.getInstance().getGameVersion(); }

    /**
     * Gets the main config of the server. If no config exists, create one and save.
     * @return the main {@link Config} (main config is always 'config.yml')
     * @since 3.3
     * @deprecated it is recommended that you use {@link CyberAPI#getConfig()}
     */
    @Deprecated
    public Config getMainConfig() {
        if(mainConfig == null) mainConfig = new Config();
        return mainConfig;
    }

    /**
     * Gets the main config of the server. If no config exists, create one and save.
     * @return the main {@link Config} (main config is always 'config.yml')
     * @since 3.3
     */
    public Config getConfig() {
        if(mainConfig == null) mainConfig = new Config();
        return mainConfig;
    }

    /**
     * Gets a config of the server. If no config exists, create one and save.
     * @param fileName the name of the config file, usually from the "resources/" folder
     * @return the {@link Config}
     * @since 3.3
     */
    public Config getConfig(String fileName) {
        if(!configs.containsKey(fileName)) configs.put(fileName, new Config(fileName));
        return configs.get(fileName);
    }

    /**
     * Reloads the main configuration file ('config.yml')
     * @since 3.3
     */
    public void reloadConfig() { getConfig().reload(); }

    /**
     * Saves the main configuration file ('config.yml')
     * @since 3.3
     */
    public void saveConfig() { getConfig().save(); }

    /**
     * Saves the defaults for the main configuration file ('config.yml')
     * @since 3.3
     */
    public void saveDefaultConfig() { getConfig().saveDefaults(); }

    /**
     * Returns when the server started in Unix time
     * @return the server start date, in unix time with milliseconds
     * @since 3.3
     */
    public long getServerStartedUnix() { return serverStarted; }

    /**
     * Returns when the server started with a specific time unit
     * @param unit the {@link TimeUnit} to convert the unix time to
     * @return the unix time with the applied {@link TimeUnit}
     * @since 3.3
     */
    public long getServerStartedUnix(TimeUnit unit) { return (unit.convert(Duration.ofMillis(serverStarted))); }

    /**
     * Returns when the server started as formatted with {@link java.text.SimpleDateFormat}'s formats with a specified timezone offset
     * @param pattern the pattern to format with {@link java.text.SimpleDateFormat}
     * @param timeOffset the timezone offset
     * @return the human-readable server start-date with timezone offset
     * @since 3.3
     */
    public String getServerStartedDate(String pattern, int timeOffset) { return Time.getFormattedDateUnix(getServerStartedUnix(TimeUnit.SECONDS), pattern, timeOffset); }

    /**
     * Returns when the server started as formatted with {@link java.text.SimpleDateFormat}'s formats
     * @param pattern the pattern to format with {@link java.text.SimpleDateFormat}
     * @return the human-readable server start-date
     * @since 3.3
     */
    public String getServerStartedDate(String pattern) { return getServerStartedDate(pattern, 0); }

    /**
     * Returns how long the server has been online
     * @param showAll whether to show all units, see description of {@link Time#getBetterTimeDisplay(long, long, boolean)}
     * @return the time since the server has started
     * @since 3.3
     * @see Time#getBetterTimeDisplay(long, long, boolean)
     */
    public String getServerUptime(boolean showAll) { return Time.getBetterTimeDisplay(Time.getUnix(), getServerStartedUnix(TimeUnit.SECONDS), showAll); }

    /**
     * Returns how long the server has been online
     * @return the time since the server started
     * @since 3.3
     * @see CyberAPI#getServerUptime(boolean)
     */
    public String getServerUptime() { return getServerUptime(true); }

    /**
     * Gets a new CyberPlayer. Please note: It's better to use {@link CyberPlayer} constructors or static methods instead of this method.
     * @param uuid the unique ID
     * @return the {@link CyberPlayer}
     * @deprecated It's better to use {@link CyberPlayer} constructors or static methods instead of this method.
     * @since 3.3
     */
    @Deprecated
    public CyberPlayer getCyberPlayer(UUID uuid) { return new CyberPlayer(uuid); }

    /**
     * Gets the LuckPerms support. This method assumes the best of the developer as if they have marked LuckPerms support as {@link FeatureSupport#SUPPORTED}, it will allow use of it.
     * @return the {@link FeatureSupport} enum of the value
     * @since 3.3
     */
    public FeatureSupport getLuckPermsSupport() {
        if(luckPermsSupport == null) {
            luckPermsSupport = settings.supportsLuckPerms();

            if(luckPermsSupport.equals(FeatureSupport.AUTO)) {
                try {
                    Class.forName("net.luckperms.api.LuckPermsProvider");
                    this.luckPermsSupport = FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.luckPermsSupport = FeatureSupport.UNSUPPORTED;
                }
                log.verbose("LuckPerms support was set to auto, detected: " + luckPermsSupport.name());
            }
        }
        return this.luckPermsSupport;
    }

    /**
     * Sends a title to a player with specified settings
     * @param player the player to send the title to
     * @param title the title to send
     * @param subtitle the subtitle to send
     * @param fadeIn the amount of ticks to fadein
     * @param stay the amount of ticks to stay
     * @param fadeOut the amount of ticks to fadeout
     * @since 3.3
     */
    public void sendTitle(ProxiedPlayer player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        new BungeeTitle()
                .title(UChat.bComponent(title))
                .subTitle(UChat.bComponent(subtitle))
                .fadeIn(fadeIn)
                .stay(stay)
                .fadeOut(fadeOut)
                .send(player); // finally
    }

    /**
     * Sends a title to a player with a title and subtitle
     * @param player the player to send the title to
     * @param title the title to send
     * @param subtitle the subtitle to send
     * @since 3.3
     */
    public void sendTitle(ProxiedPlayer player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 20, 100, 20);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send
     * @since 3.3
     * @see CyberAPI#broadcast(String, String)
     */
    public void broadcast(String message) { broadcast(message, ""); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param permission the permission required to see the message
     * @since 3.3
     * @see UChat#broadcast(String)
     * @see UChat#broadcast(String, String)
     */
    public void broadcast(String message, String permission) { UChat.broadcast(message, permission); }

    /**
     * Performs a command as a {@link ProxiedPlayer} if {@link CommandSender} is not a player. Please include a '/' in your command when typing it here
     * @param sender the {@link CommandSender} to execute the command
     * @param command the command to execute, including a '/' in the beginning
     * @since 3.3
     */
    public void performCommand(CommandSender sender, String command) {
        ProxyServer.getInstance().getPluginManager().dispatchCommand(sender, command.substring(1));
    }

    /**
     * Returns a list of online players in forms of {@link ProxiedPlayer} objects
     * @return the online players in {@link ProxiedPlayer} objects
     * @since 3.3
     */
    public List<ProxiedPlayer> getOnlinePlayers() { return new ArrayList<>(ProxyServer.getInstance().getPlayers()); }

    /**
     * Returns a list of online players in form of their usernames
     * @return the online players' usernames, a {@link List} of {@link String}s
     * @since 3.3
     */
    public List<String> getOnlinePlayersUsernames() {
        List<String> usernames = new ArrayList<>();
        getOnlinePlayers().forEach(player -> usernames.add(player.getName()));
        return usernames;
    }

    /**
     * Gets a player's {@link UUID} from a given {@link String} username
     * <br>
     * <b>Note: This is obtaining the {@link UUID} from a URL, meaning you should cache this or use asynchronous events</b>
     * @param name the name to retrieve the UUID from
     * @return the UUID associated with the name
     * @since 3.3
     */
    public UUID getUUID(String name) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            String strUUID = (((JsonObject) JsonParser.parseReader(reader)).get("id")).toString().replaceAll("\"", "");
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
     * @since 3.3
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
     * Registers a command with the Bungee server. <br> <br>
     * <b>Tab Completers are automatically registered by Bungee using this method, so there is no need for a {@code registerTabCompleter(TabCompleter)} method!</b>
     * @param command the {@link Command} object to execute the command
     * @since 3.3
     */
    public void registerCommand(Command command) {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, command);
    }

    /**
     * Registers a listener with the Bungee server
     * @param listener the {@link Listener} class with listeners in it
     * @since 3.3
     */
    public void registerListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(this, listener);
    }

    /**
     * Registers a {@link Runnable} with the server, essentially a task
     * @param runnable the runnable to register
     * @param period the amount of ticks in between each execution
     * @since 3.3
     */
    public void registerRunnable(Runnable runnable, long period) {
        ProxyServer.getInstance().getScheduler().schedule(this, runnable, 20L, period*50L, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the API logger, <b>only for use inside CyberAPI!</b>
     * @return the API logger
     */
    public APILog getAPILogger() {
        if(log == null) log = new APILog();
        return log;
    }

    /**
     * CyberAPI logging methods only!
     * <br> <br>
     * Not documented becasue it's only for this class and not for anything else
     * @since 3.3
     */
    public class APILog {
        protected APILog() {}

        public net.md_5.bungee.api.ChatColor DEFAULT_WARN_LOG = net.md_5.bungee.api.ChatColor.of(new java.awt.Color(249, 241, 165));

        public void info(String message) { log(Level.INFO, ChatColor.RESET + message); }
        public void warn(String message) { log(Level.WARNING, DEFAULT_WARN_LOG + message); }
        public void error(String message) { log(Level.SEVERE, ChatColor.RED + message); }

        public void verbose(String message) { verbose(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName(), message); }
        public void verbose(String canonical, String message) {
            if(getSettings().isVerbose())
                log(Level.INFO, ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + "VERBOSE/" + canonical + ChatColor.DARK_GRAY + " ] " + ChatColor.RESET + message);
        }

        public void verboseException(Throwable throwable) {
            for(String element : BetterStackTraces.get(throwable)) { verbose(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName(), element);}
        }

        public void log(Level level, String message) {
            if(Boolean.TRUE.equals(CyberAPI.this.settings.isSilenced())) return;
            Log.log(level, ChatColor.LIGHT_PURPLE + "[CyberAPI]" + ChatColor.RESET + " " + message);
        }
    }

    /**
     * Returns an instance of {@link CyberAPISpecific}, which is things that are relating to CyberAPI, not your plugin, CyberAPI
     * @return the {@link CyberAPISpecific} instance
     * @since 3.3
     */
    public CyberAPISpecific getCyberAPISpecific() { return new CyberAPISpecific(); }
    public class CyberAPISpecific {
        private static String latestVersion;
        private static int latestBuild;

        /**
         * Gets a new instance of {@link CyberAPISpecific}, but you're not supposed to use this, please instead use {@link CyberAPI#getCyberAPISpecific()}
         */
        private CyberAPISpecific() {}

        /**
         * Gets the last CyberAPI build properties
         * @return the {@link Properties} of the last CyberAPI build
         */
        public Properties getBuildProperties() {
            InputStream inputStream = getResourceAsStream("bungee-build.properties");
            Properties properties = new Properties();
            try {
                properties.load(inputStream);
            } catch (Exception exception) {
                log.error("An error occurred whilst getting the project properties!");
                log.verboseException(exception);
            }
            return properties;
        }

        /**
         * Gets the current version of {@link CyberAPI}
         * @return the version of {@link CyberAPI}
         * @since 3.3
         */
        public String getVersion() { return "v" + getBuildProperties().getProperty("version"); }

        /**
         * Gets the current build number of {@link CyberAPI}
         * @return the build number of {@link CyberAPI}
         * @since 3.3
         */
        public int getBuild() { return Integer.parseInt(getBuildProperties().getProperty("buildNumber")); }

        /**
         * Gets the latest version of {@link CyberAPI}
         * @return the latest version of {@link CyberAPI}
         * @since 3.3
         */
        public String getLatestVersion() { return latestVersion; }

        /**
         * Gets the latest build number of {@link CyberAPI}
         * @return the latest build number of {@link CyberAPI}
         * @since 3.3
         */
        public int getLatestBuild() { return latestBuild; }

        /**
         * Gets the website associated with {@link CyberAPI}
         * @return the website of {@link CyberAPI}
         * @since 3.3
         */
        public String getWebsite() { return getBuildProperties().getProperty("website"); }

        /**
         * Gets the version string that is shown to the user on startup
         * @return the version string from server startup
         * @since 3.3
         */
        public String getVersionString() {
            return "&fThe plugin &a" + getPluginName() + " &fis using CyberAPI &9BUNGEE &fversion &e" + getVersion() + " &6(" + getBuild() + ")&f!";
        }

        /**
         * Check for updates, prints output to console
         * @since 3.3
         */
        public void checkForUpdates() {
            if(!getSettings().shouldCheckForUpdates()) return;

            log.verbose("Checking for updates...");
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
                log.error("Failed version checking for CyberAPI version " + getVersion() + "! " + ChatColor.DARK_GRAY + exception); getAPILogger().verboseException(exception);return;
            }

            net.md_5.bungee.api.ChatColor DEFAULT_WARN_LOG = net.md_5.bungee.api.ChatColor.of(new java.awt.Color(249, 241, 165));
            if(getBuild() != latestBuild) {
                if(latestBuild - getBuild() > 0) {
                    log.warn(DEFAULT_WARN_LOG + "CyberAPI is outdated! The latest version is " + ChatColor.GREEN + latestVersion + DEFAULT_WARN_LOG + ", using " + ChatColor.RED + getVersion() + ChatColor.GRAY + " (" + (latestBuild -  getBuild()) + " version(s) behind!)" + DEFAULT_WARN_LOG + "!");
                    log.warn(DEFAULT_WARN_LOG + "Notify author of " + ChatColor.GOLD + getPluginName() + DEFAULT_WARN_LOG + " to download latest CyberAPI at " + ChatColor.LIGHT_PURPLE + getWebsite().replace("https://", ""));
                }
            }

            log.verbose("Checked for updates! (version=" + getCyberAPISpecific().getVersion() + ", latest=" + getCyberAPISpecific().getLatestVersion() + ")");
        }

        /**
         * Gets the build information from the inner-class {@link BuildInformation}
         * @return the build information
         * @since 3.3
         * @apiNote All of this build information relates to **CYBERAPI**, not your project
         */
        public BuildInformation getBuildInformation() { return new BuildInformation(); }
        public class BuildInformation {
            protected BuildInformation() {}

            /**
             * Gets the name of the build file that was used to build CyberAPI
             * @return the build file name
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildFile() { return getBuildProperties().getProperty("buildFile"); }

            /**
             * Gets the arch that was used to compile CyberAPI
             * @return the builder's arch
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderArch() { return getBuildProperties().getProperty("builderArch"); }

            /**
             * Gets the OS that was used to compile CyberAPI
             * @return the builder's OS
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderOS() { return getBuildProperties().getProperty("builderOS"); }

            /**
             * Gets the OS's version that was used to compile CyberAPI
             * @return the OS's version
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public double getBuilderVersion() { return Double.parseDouble(getBuildProperties().getProperty("builderVersion")); }

            /**
             * Gets the unix time that CyberAPI was last built, in MILLISECONDS (not SECONDS)
             * @return the unix time of CyberAPI compile
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public long getBuildUnix() { return Long.parseLong(getBuildProperties().getProperty("built")); }

            /**
             * Gets the {@link Date} that CyberAPI was built
             * @return the {@link Date} time of when CyberAPI was compiled
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public Date getBuildDate() { return new Date(getBuildUnix()); }

            /**
             * Gets the human-readable date of when CyberAPI was built (please specify a {@link java.text.SimpleDateFormat} pattern)
             * @param pattern the {@link java.text.SimpleDateFormat} pattern that should be applied to the human-readable date
             * @return the date at which CyberAPI was compiled, in human-readable time
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildDate(String pattern) { return getBuildDate(pattern, 0); }

            /**
             * Gets the human-readable date of when CyberAPI was built (please specify a {@link java.text.SimpleDateFormat} pattern) with a time offset
             * @param pattern the {@link java.text.SimpleDateFormat} pattern that should be applied to the human-readable date
             * @param timeOffset the time offset of the date
             * @return the date at which CyberAPI was compiled, in human-readable time
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildDate(String pattern, int timeOffset) { return Time.getFormattedDateUnix(getBuildUnix()/1000L, pattern, timeOffset); }


            /**
             * Gets the builder's Java vendor (where they got Java from) for CyberAPI
             * @return the builder's Java vendor
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderJavaVendor() { return getBuildProperties().getProperty("currentJavaVendor"); }

            /**
             * Gets the builder's Java version (what their java version was when they built CyberAPI) for CyberAPI
             * @return the builder's Java version
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderJavaVersion() { return getBuildProperties().getProperty("currentJavaVersion"); }

            /**
             * Gets the group ID used by CyberAPI
             * @return the group ID used when compiling CyberAPI
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getGroup() { return getBuildProperties().getProperty("group"); }

            /**
             * Gets the name of the CyberAPI project being used
             * @return the name of the CyberAPI project
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getName() { return getBuildProperties().getProperty("name"); }

            /**
             * Gets the targeted java version for CyberAPI's compilation
             * @return the targeted java version
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public int getTargetJavaVersion() { return Integer.parseInt(getBuildProperties().getProperty("targetJavaVersion")); }

            /**
             * Gets the user of the computer that was used to build CyberAPI
             * @return the user that built CyberAPI
             * @since 3.3
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildUser() { return getBuildProperties().getProperty("user"); }

            /**
             * Converts all properties to a {@link String} that looks like this:
             * <p>"property1=value1, property2=value2"</p>
             * @return all properties to a string
             * @since 3.3
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
             * @since 3.3
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
         * @since 3.3
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
                    .append("\n\t&c\u251C &fBungee Version String: &6").append(ProxyServer.getInstance().getVersion())
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
                    .append(" &ffor &9BUNGEE")
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
         * @since 3.3
         */
        public void technoblade() {
            Log.info("&f&oTechnoblade's Tribute, called by: " + StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName());
            Log.info("&d-".repeat(60));
            UUID uuid = UUID.fromString("b876ec32-e396-476b-a115-8438d83c67d4"); // Technoblade UUID
            try {
                CyberPlayer cyberPlayer = new CyberPlayer(uuid);
                UChat.broadcast("&d&lA TRIBUTE TO TECHNOBLADE:");
                UChat.broadcast(cyberPlayer.getUserHead(
                        UserHeadSettings.builder()
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
            } catch (Exception exception) {
                getAPILogger().error("An error occurred with Technoblade's tribute! " + ChatColor.DARK_GRAY + exception);
                getAPILogger().verboseException(exception);
            }
            Log.info("&d-".repeat(60));
        }
    }

}
