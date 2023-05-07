package net.cybercake.cyberapi.bungee;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.cybercake.cyberapi.bungee.basic.BetterStackTraces;
import net.cybercake.cyberapi.bungee.chat.Log;
import net.cybercake.cyberapi.bungee.chat.UChat;
import net.cybercake.cyberapi.bungee.config.Config;
import net.cybercake.cyberapi.bungee.player.BungeeTitle;
import net.cybercake.cyberapi.bungee.player.CyberPlayer;
import net.cybercake.cyberapi.bungee.server.commands.BungeeCommand;
import net.cybercake.cyberapi.bungee.server.commands.CommandManager;
import net.cybercake.cyberapi.bungee.server.serverlist.ServerListInfo;
import net.cybercake.cyberapi.bungee.server.serverlist.ServerListInfoListener;
import net.cybercake.cyberapi.common.CommonManager;
import net.cybercake.cyberapi.common.basic.Time;
import net.cybercake.cyberapi.common.basic.logs.Logs;
import net.cybercake.cyberapi.common.builders.player.UserHeadSettings;
import net.cybercake.cyberapi.common.builders.settings.FeatureSupport;
import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
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
 * @Website: <a href="https://github.com/CyberedCake/CyberAPI-testing">github.com/CyberedCake/CyberAPI-testing</a>
 */
@SuppressWarnings({"unused"})
public class CyberAPI extends Plugin implements CommonManager {

    public CyberAPI() { api = this; }
    private static CyberAPI api;

    /**
     * Gets the instance of your {@link Plugin} and {@link CyberAPI}
     * @return the {@link CyberAPI} instance, a.k.a. your main class
     * @since 15
     */
    public static CyberAPI getInstance() { return api; }

    /**
     * Gets the main configuration file values, using {@link Config#values()} and {@link CyberAPI#getConfig()}.
     * @return the main configuration file
     * @since 15
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
     * @since 15
     */
    @SuppressWarnings({"deprecation"})
    protected CyberAPI startCyberAPI(@Nullable Settings settings) {
        long mss = System.currentTimeMillis();
        this.serverStarted = mss;

        log = new APILog(); // for private use only

        if(settings == null) settings = Settings.builder().build();
        this.settings = settings;
        log.verbose("Starting CyberAPI...");

        this.pluginVerbose = false;

        // load all support variables, so they're not 'null'
        // automatically sets the variable in these methods, so returned values are not used
        getAdventureAPISupport();
        getMiniMessageSupport();
        getLuckPermsSupport();
        getProtocolizeSupport();

        registerListener(new ServerListInfoListener());

        registerLog4jModifiers(); // deprecated because I don't want anyone else using it

        @Nullable String mainPackagePath =  this.getSettings().getMainPackagePath();
        long timedPackageSearcher = System.currentTimeMillis();
        this.classes = (mainPackagePath == null ? new Reflections() : new Reflections(mainPackagePath)).getAll(new SubTypesScanner(false))
                .stream()
                .filter(clazz -> {
                    if(mainPackagePath == null) return true;
                    return clazz.startsWith(mainPackagePath);
                })
                .map(clazz -> {
                    try {
                        return Class.forName(clazz);
                    } catch (ClassNotFoundException | NoClassDefFoundError exception) {
                        if(mainPackagePath != null && clazz.startsWith(mainPackagePath))
                            throw new IllegalArgumentException("Class not found, despite it being included in the package scan! This is likely not your fault, please report to CyberAPI: https://github.com/CyberedCake/CyberAPI-testing", exception);
                        return null;
                    }
                })
                .toList();

        CommandManager.commandManager().init(settings.getMainPackagePath());

        if(mainPackagePath == null) {
            try {
                Method method = CyberAPI.class.getDeclaredMethod("startCyberAPI", Settings.class);
                CyberAPI.getInstance().getAPILogger().warn("Please specify a main package to speed up CyberAPI start time in " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "(" + Settings.class.getCanonicalName() + ")! (registering took " + (System.currentTimeMillis()-timedPackageSearcher) + "ms!)");
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new IllegalStateException("Failed to find a method", noSuchMethodException);
            }
        }

        CyberAPISpecific specific = getCyberAPISpecific();

        if(!settings.shouldMuteStartMessage()) log.info(specific.getVersionString()); // print version string and print build information if user set CyberAPI to be verbose
        if(getSettings().isVerbose()) specific.printBuildInformation();

        specific.checkForUpdates();

        log.verbose("Finished! CyberAPI took " + (System.currentTimeMillis()-mss) + "ms to start.");
        return this;
    }

    // variables
    private long serverStarted;

    private Settings settings;

    private boolean pluginVerbose;
    private APILog log = null;

    private Config mainConfig = null;
    private final HashMap<String, Config> configs = new HashMap<>();

    private List<? extends Class<?>> classes = null;

    private FeatureSupport adventureAPISupport = null;
    private FeatureSupport miniMessageSupport = null;
    private FeatureSupport luckPermsSupport = null;
    private FeatureSupport protocolLibSupport = null;
    private FeatureSupport protocolizeSupport = null;

    // override methods from CommonManager

    /**
     * Creates a log file that you can then add logs to
     * @param id the ID of the {@link Logs} instance
     * @param fileNameWithoutExtension the name of the file without the extension
     * @return the {@link Logs} instance
     * @since 43
     */
    @Override
    public Logs createOrGetLogs(String id, String fileNameWithoutExtension) {
        File logs = new File(getDataFolder(), "logs");
        if(!logs.exists()) logs.mkdirs();
        return createOrGetLogs(id, new File(logs, fileNameWithoutExtension + ".log"));
    }

    /**
     * Creates a log file that you can then add logs to
     * @param id the ID of the {@link Logs} instance
     * @param file the {@link File} where the logs are housed
     * @return the {@link Logs} instance
     * @since 43
     */
    @SuppressWarnings({"deprecation"})
    @Override
    public Logs createOrGetLogs(String id, File file) {
        return (Logs.getFromID(id) == null ? new Logs(id, file) : Logs.getFromID(id));
    }



    /**
     * Gets the settings CyberAPI is using to determine the developer's preferences
     * @return the settings
     * @since 15
     */
    public Settings getSettings() { return settings; }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * The name of the plugin that is different from the bungee.yml
     * @return {@code getSettings().getName()}
     * @since 15
     */
    public String getPluginName() { return getSettings().getName(); }

    /**
     * This is a setting from {@link CyberAPI#getSettings()}
     * <br> <br>
     * The prefix that will show before all logging methods
     * @return {@code getSettings().getPrefix()}
     * @since 15
     */
    public String getPrefix() { return getSettings().getPrefix(); }

    /**
     * Gets the classes that the plugin (not CyberAPI) has set up. For example, if they include the main package name, this method will return all the classes under that package.
     * @return the classes of the plugin, assuming {@link Settings#getMainPackagePath() the main package path} is set
     * @since 98
     */
    public List<? extends Class<?>> getPluginClasses() { return this.classes; }

    /**
     * Gets the name of the server implementation being used
     * @return the server type, usually looks like 'Bungeecord' or 'Waterfall'
     * @since 15
     */
    public String getServerType() { return ProxyServer.getInstance().getName(); }

    /**
     * Gets the server type of the server along with the version
     * @return the server version string, usually looks like 'git:Waterfall-Bootstrap:1.19-R0.1-SNAPSHOT:fc30291:498'
     * @since 15
     */
    public String getServerTypeVersion() { return ProxyServer.getInstance().getVersion(); }

    /**
     * Gets the version of the Minecraft server
     * @return the Minecraft server version, for example, my server shows '{@code 1.8.x, 1.9.x, 1.10.x, 1.11.x, 1.12.x, 1.13.x, 1.14.x, 1.15.x, 1.16.x, 1.17.x, 1.18.x, 1.19.x}' {@literal <}-- as it's all the versions the server supports
     * @since 15
     */
    @SuppressWarnings({"deprecation"}) public String getMinecraftVersion() { return ProxyServer.getInstance().getGameVersion(); }

    /**
     * Gets the main config of the server. If no config exists, create one and save.
     * @return the main {@link Config} (main config is always 'config.yml')
     * @since 15
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
     * @since 15
     */
    public Config getConfig() {
        if(mainConfig == null) mainConfig = new Config();
        return mainConfig;
    }

    /**
     * Gets a config of the server. If no config exists, create one and save.
     * @param fileName the name of the config file, usually from the "resources/" folder
     * @return the {@link Config}
     * @since 15
     */
    public Config getConfig(String fileName) {
        if(!configs.containsKey(fileName)) configs.put(fileName, new Config(fileName));
        return configs.get(fileName);
    }

    /**
     * Reloads the main configuration file ('config.yml')
     * @since 15
     */
    public void reloadConfig() { getConfig().reload(); }

    /**
     * Saves the main configuration file ('config.yml')
     * @since 15
     */
    public void saveConfig() { getConfig().save(); }

    /**
     * Saves the defaults for the main configuration file ('config.yml')
     * @since 15
     */
    public void saveDefaultConfig() { getConfig().saveDefaults(); }

    /**
     * Returns when the server started in Unix time
     * @return the server start date, in unix time with milliseconds
     * @since 15
     */
    public long getServerStartedUnix() { return serverStarted; }

    /**
     * Returns when the server started with a specific time unit
     * @param unit the {@link TimeUnit} to convert the unix time to
     * @return the unix time with the applied {@link TimeUnit}
     * @since 15
     */
    public long getServerStartedUnix(TimeUnit unit) { return (unit.convert(Duration.ofMillis(serverStarted))); }

    /**
     * Returns when the server started as formatted with {@link java.text.SimpleDateFormat}'s formats with a specified timezone offset
     * @param pattern the pattern to format with {@link java.text.SimpleDateFormat}
     * @param timeOffset the timezone offset
     * @return the human-readable server start-date with timezone offset
     * @since 15
     */
    public String getServerStartedDate(String pattern, int timeOffset) { return Time.getFormattedDateUnix(getServerStartedUnix(TimeUnit.SECONDS), pattern, timeOffset); }

    /**
     * Returns when the server started as formatted with {@link java.text.SimpleDateFormat}'s formats
     * @param pattern the pattern to format with {@link java.text.SimpleDateFormat}
     * @return the human-readable server start-date
     * @since 15
     */
    public String getServerStartedDate(String pattern) { return getServerStartedDate(pattern, 0); }

    /**
     * Returns how long the server has been online
     * @param showAll whether to show all units, see description of {@link Time#getBetterTimeDisplay(long, long, boolean)}
     * @return the time since the server has started
     * @since 15
     * @see Time#getBetterTimeDisplay(long, long, boolean)
     */
    public String getServerUptime(boolean showAll) { return Time.getBetterTimeDisplay(Time.getUnix(), getServerStartedUnix(TimeUnit.SECONDS), showAll); }

    /**
     * Returns how long the server has been online
     * @return the time since the server started
     * @since 15
     * @see CyberAPI#getServerUptime(boolean)
     */
    public String getServerUptime() { return getServerUptime(true); }

    /**
     * Gets a new CyberPlayer. Please note: It's better to use {@link CyberPlayer} constructors or static methods instead of this method.
     * @param uuid the unique ID
     * @return the {@link CyberPlayer}
     * @deprecated It's better to use {@link CyberPlayer} constructors or static methods instead of this method.
     * @since 15
     */
    @Deprecated
    public CyberPlayer getCyberPlayer(UUID uuid) { return new CyberPlayer(uuid); }

    /**
     * <b>--{@literal >} THIS IS A CYBERAPI <em>ONLY</em> METHOD {@literal <}--</b> <br> <br>
     * Checks the plugin depends and soft-depends for a certain plugin
     * @param plugin the plugin to find
     * @throws UnsupportedOperationException if the plugin is not found
     * @since 60
     */
    private void checkPluginDependsFor(String plugin) {
        if(this.getDescription().getDepends()
                .stream()
                .map(String::toLowerCase)
                .toList()
                .contains(plugin.toLowerCase(Locale.ROOT))
        ) return;
        if(this.getDescription().getSoftDepends()
                .stream()
                .map(String::toLowerCase)
                .toList()
                .contains(plugin.toLowerCase(Locale.ROOT))
        ) return;
        throw new UnsupportedOperationException("Will not attempt to use " + plugin + " since it's not a depend or soft-depend of " + this.getDescription().getName() + " v" + this.getDescription().getVersion() + "!");
    }

    /**
     * Gets the Adventure API support. This method assumes the best of the developer as if they marked Adventure Support as {@link FeatureSupport#SUPPORTED}, it will try to use Adventure API.
     * @return the {@link FeatureSupport} enum of the value
     * @since 73
     */
    public FeatureSupport getAdventureAPISupport() {
        if(adventureAPISupport == null) {
            adventureAPISupport = settings.supportsAdventureAPI();

            if(adventureAPISupport.equals(FeatureSupport.AUTO)) {
                try {
                    Class.forName("net.kyori.adventure.text.Component");
                    this.adventureAPISupport = FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.adventureAPISupport = FeatureSupport.UNSUPPORTED;
                }
                log.verbose("Adventure API support was set to auto, detected: " + adventureAPISupport.name());
            }
        }
        return this.adventureAPISupport;
    }

    /**
     * Gets the MiniMessage support. This method assumes the best of the developer as if they marked MiniMessage as {@link FeatureSupport#SUPPORTED}, it will allow the use of MiniMessage methods.
     * @return the {@link FeatureSupport} enum of the value
     * @since 73
     */
    public FeatureSupport getMiniMessageSupport() {
        if(miniMessageSupport == null) {
            miniMessageSupport = settings.supportsMiniMessage();

            if(miniMessageSupport.equals(FeatureSupport.AUTO)) {
                try {
                    Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
                    this.miniMessageSupport = FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.miniMessageSupport = FeatureSupport.UNSUPPORTED;
                }
                log.verbose("MiniMessage support was set to auto, detected: " + miniMessageSupport.name());
            }
        }
        return this.miniMessageSupport;
    }

    /**
     * Gets the LuckPerms support. This method assumes the best of the developer as if they have marked LuckPerms support as {@link FeatureSupport#SUPPORTED}, it will allow use of it.
     * @return the {@link FeatureSupport} enum of the value
     * @since 15
     */
    public FeatureSupport getLuckPermsSupport() {
        if(luckPermsSupport == null) {
            luckPermsSupport = settings.supportsLuckPerms();

            if(luckPermsSupport.equals(FeatureSupport.AUTO)) {
                try {
                    checkPluginDependsFor("LuckPerms");
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
     * Gets the ProtocolLib support. This method will always return {@link FeatureSupport#UNSUPPORTED} because this CyberAPI server type does not support it!
     * @return the {@link FeatureSupport} enum of the value, always {@link FeatureSupport#UNSUPPORTED}
     * @since 25
     */
    public FeatureSupport getProtocolLibSupport() {
        if(this.protocolLibSupport == null) this.protocolLibSupport = FeatureSupport.UNSUPPORTED;
        return this.protocolLibSupport;
    }

    /**
     * Gets the Protocolize support. This method assumes the best of the developer as if they have marked Protocolize support as {@link FeatureSupport#SUPPORTED}, it will allow use of it.
     * @return the {@link FeatureSupport} enum of the value
     * @since 28
     */
    public FeatureSupport getProtocolizeSupport() {
        if(protocolizeSupport == null) {
            protocolizeSupport = settings.supportsProtocolize();

            if(protocolizeSupport.equals(FeatureSupport.AUTO)) {
                try {
                    checkPluginDependsFor("Protocolize");
                    Class.forName("dev.simplix.protocolize");
                    this.protocolizeSupport = FeatureSupport.SUPPORTED;
                } catch (Exception exception) {
                    this.protocolizeSupport = FeatureSupport.UNSUPPORTED;
                }
                log.verbose("Protocolize support was set to auto, detected: " + protocolizeSupport.name());
            }
        }
        return this.protocolizeSupport;
    }

    /**
     * Sends a title to a player with specified settings
     * @param player the player to send the title to
     * @param title the title to send
     * @param subtitle the subtitle to send
     * @param fadeIn the amount of ticks to fadein
     * @param stay the amount of ticks to stay
     * @param fadeOut the amount of ticks to fadeout
     * @since 15
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
     * @since 15
     */
    public void sendTitle(ProxiedPlayer player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 20, 100, 20);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send
     * @since 15
     * @see CyberAPI#broadcast(String, String)
     */
    public void broadcast(String message) { broadcast(message, ""); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param permission the permission required to see the message
     * @since 15
     * @see UChat#broadcast(String)
     * @see UChat#broadcast(String, String)
     */
    public void broadcast(String message, String permission) { UChat.broadcast(message, permission); }

    /**
     * Performs a command as a {@link ProxiedPlayer} if {@link CommandSender} is not a player. Please include a '/' in your command when typing it here
     * @param sender the {@link CommandSender} to execute the command
     * @param command the command to execute, including a '/' in the beginning
     * @since 15
     */
    public void performCommand(CommandSender sender, String command) {
        ProxyServer.getInstance().getPluginManager().dispatchCommand(sender, command.substring(1));
    }

    /**
     * Returns an instance of {@link ServerListInfo}, which allows you to change things like the MOTD, player count, icon, etc.
     * @return the {@link ServerListInfo} instance
     * @since 28
     */
    public ServerListInfo getServerListInfo() {
        return ServerListInfo.serverListInfo();
    }

    /**
     * Returns a list of online players in forms of {@link ProxiedPlayer} objects
     * @return the online players in {@link ProxiedPlayer} objects
     * @since 15
     */
    public List<ProxiedPlayer> getOnlinePlayers() { return new ArrayList<>(ProxyServer.getInstance().getPlayers()); }

    /**
     * Returns a list of online players in form of their usernames
     * @return the online players' usernames, a {@link List} of {@link String}s
     * @since 15
     */
    public List<String> getOnlinePlayersUsernames() {
        List<String> usernames = new ArrayList<>();
        getOnlinePlayers().forEach(player -> usernames.add(player.getName()));
        return usernames;
    }

    /**
     * Registers a command with the Bungee server. <br> <br>
     * <b>Tab Completers are automatically registered by Bungee using this method, so there is no need for a {@code registerTabCompleter(TabCompleter)} method!</b>
     * @param command the {@link Command} object to execute the command
     * @since 15
     * @see CyberAPI#registerCommand(BungeeCommand)
     */
    public void registerCommand(Command command) {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, command);
    }

    /**
     * Registers a {@link BungeeCommand CyberAPI command} with the Bungee server
     * @param command the command to register
     * @see CyberAPI#registerCommand(Command)
     * @since 94
     */
    public void registerCommand(BungeeCommand command) {
        CommandManager.commandManager().resolveInformationAndRegister(command);
    }

    /**
     * Registers a listener with the Bungee server
     * @param listener the {@link Listener} class with listeners in it
     * @since 15
     */
    public void registerListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(this, listener);
    }

    /**
     * Registers a {@link Runnable} with the server, essentially a task
     * @param runnable the runnable to register
     * @param period the amount of ticks in between each execution
     * @return {@link ScheduledTask}: the task form of the runnable (during execution -- used for cancelling/getting the id of)
     * @since 15
     */
    public ScheduledTask registerRunnable(Runnable runnable, long period) {
        return registerRunnable(runnable, 20, period);
    }

    /**
     * Registers a {@link Runnable} with the server, essentially a task
     * @param runnable the runnable to register
     * @param delay the amount of ticks before the task is started
     * @param period the amount of ticks in between each execution
     * @return {@link ScheduledTask}: the task form of the runnable (during execution -- used for cancelling/getting the id of)
     * @since 106
     */
    public ScheduledTask registerRunnable(Runnable runnable, long delay, long period) {
        return ProxyServer.getInstance().getScheduler().schedule(this, runnable, delay*50L, period*50L, TimeUnit.MILLISECONDS);
    }

    /**
     * Checks the status of the plugin's verbose mode.
     * <br> <br>
     * If this is set to {@link Boolean#TRUE true}, then the plugin will send any messages logged using {@link Log#verbose(String) the verbose logging option}. Otherwise, it will not send those messages.
     * <br> <br>
     * <b>NOTE! This is not the same as CyberAPI's "verbose" option</b>, to enable that one, modify it in the {@link net.cybercake.cyberapi.common.builders.settings.Settings.Builder#verbose(boolean) Settings}!
     * @return whether CyberAPI will send your verbose messages or not
     * @since 99
     * @see CyberAPI#setPluginVerbose(boolean) set this value here!
     */
    public boolean isPluginVerbose() { return this.pluginVerbose; }

    /**
     * Set's the plugin's verbose mode to a {@link Boolean}.
     * <br> <br>
     * If this is set to {@link Boolean#TRUE true}, then the plugin will send any messages logged using {@link Log#verbose(String) the verbose logging option}. Otherwise, it will not send these messages.
     * <br> <br>
     * <b>NOTE! This is not the same as CyberAPI's "verbose" option</b>, to enable that one, modify it in the {@link net.cybercake.cyberapi.common.builders.settings.Settings.Builder#verbose(boolean) Settings}!
     * @param pluginVerbose whether the CyberAPI should send your verbose messages or not
     * @since 99
     * @see CyberAPI#isPluginVerbose() check this value here!
     */
    public void setPluginVerbose(boolean pluginVerbose) { this.pluginVerbose = pluginVerbose; }

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
     * @since 15
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
                log(Level.INFO, ChatColor.DARK_GRAY + " [" + ChatColor.GRAY + "VERBOSE/" + canonical + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + message);
        }

        public void verboseException(Throwable throwable) {
            for(String element : BetterStackTraces.get(throwable)) { verbose(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName(), element);}
        }

        public void log(Level level, String message) {
            if(Boolean.TRUE.equals(CyberAPI.this.settings.isSilenced())) return;
            Log.log(level, ChatColor.LIGHT_PURPLE + "[CyberAPI]" + ChatColor.RESET + " " + message, Thread.currentThread().getStackTrace()[2]);
        }
    }

    /**
     * Returns an instance of {@link CyberAPISpecific}, which is things that are relating to CyberAPI, not your plugin, CyberAPI
     * @return the {@link CyberAPISpecific} instance
     * @since 15
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
         * @since 15
         */
        public String getVersion() { return "v" + getBuildProperties().getProperty("version"); }

        /**
         * Gets the current build number of {@link CyberAPI}
         * @return the build number of {@link CyberAPI}
         * @since 15
         */
        public int getBuild() {
            try {
                return Integer.parseInt(getBuildProperties().getProperty("buildNumber"));
            } catch (Exception ignored) {
                return 0;
            }
        }

        /**
         * Gets the latest version of {@link CyberAPI}
         * @return the latest version of {@link CyberAPI}
         * @since 15
         */
        public String getLatestVersion() { return latestVersion; }

        /**
         * Gets the latest build number of {@link CyberAPI}
         * @return the latest build number of {@link CyberAPI}
         * @since 15
         */
        public int getLatestBuild() { return latestBuild; }

        /**
         * Gets the website associated with {@link CyberAPI}
         * @return the website of {@link CyberAPI}
         * @since 15
         */
        public String getWebsite() { return getBuildProperties().getProperty("website"); }

        /**
         * Gets the version string that is shown to the user on startup
         * @return the version string from server startup
         * @since 15
         */
        public String getVersionString() {
            return "&fThe plugin &a" + getPluginName() + " &fis using CyberAPI &9BUNGEE &fbuild &e#" + getBuild() + "&f!";
        }

        /**
         * Check for updates, prints output to console
         * @since 15
         */
        public void checkForUpdates() {
            if(!getSettings().shouldCheckForUpdates()) return;

            ProxyServer.getInstance().getScheduler().runAsync(CyberAPI.getInstance(), () -> {
                log.verbose("Checking for updates...");
                try {
                    // thanks stack overflow (https://stackoverflow.com/a/21964051/15519255)
                    URL url = new URL("https://api.github.com/repos/CyberedCake/CyberAPI-testing/releases/latest");
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream)connection.getContent()));
                    try {
                        String tag = element.getAsJsonObject().get("tag_name").getAsString();
                        latestVersion = getVersion();
                        latestBuild = Integer.parseInt(tag);
                    } catch (Exception exception) {
                        log.error("An error occurred fetching the latest version for GitHub repo 'CyberAPI', tag=" + element.getAsJsonObject().get("tag_name").getAsString() + ": " + ChatColor.DARK_GRAY + exception.toString());
                    }
                } catch (Exception exception) {
                    log.error("Failed version checking for CyberAPI build #" + getBuild() + "! " + ChatColor.DARK_GRAY + exception); getAPILogger().verboseException(exception);return;
                }

                net.md_5.bungee.api.ChatColor DEFAULT_WARN_LOG = net.md_5.bungee.api.ChatColor.of(new java.awt.Color(249, 241, 165));
                if(getBuild() != latestBuild) {
                    if(latestBuild - getBuild() > 0) {
                        log.warn(DEFAULT_WARN_LOG + "CyberAPI is outdated! The latest build is #" + ChatColor.GREEN + latestBuild + DEFAULT_WARN_LOG + ", using #" + ChatColor.RED + getBuild() + ChatColor.GRAY + " (" + (latestBuild -  getBuild()) + " version(s) behind!)" + DEFAULT_WARN_LOG + "!");
                        log.warn(DEFAULT_WARN_LOG + "Notify author of " + ChatColor.GOLD + getPluginName() + DEFAULT_WARN_LOG + " to download latest CyberAPI at " + ChatColor.LIGHT_PURPLE + getWebsite().replace("https://", ""));
                        log.warn(DEFAULT_WARN_LOG + "It is especially important that you update as you are running a " + ChatColor.DARK_PURPLE + "DEVELOPER VERSION" + DEFAULT_WARN_LOG + " of CyberAPI!");
                    }
                }

                log.verbose("Checked for updates! (build=" + getCyberAPISpecific().getBuild() + ", latest=" + getCyberAPISpecific().getLatestBuild() + ", testing=" + Boolean.TRUE.toString() + ")");
            });
        }

        /**
         * Gets the build information from the inner-class {@link BuildInformation}
         * @return the build information
         * @since 15
         * @apiNote All of this build information relates to **CYBERAPI**, not your project
         */
        public BuildInformation getBuildInformation() { return new BuildInformation(); }
        public class BuildInformation {
            protected BuildInformation() {}

            /**
             * Gets the name of the build file that was used to build CyberAPI
             * @return the build file name
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildFile() { return getBuildProperties().getProperty("buildFile"); }

            /**
             * Gets the arch that was used to compile CyberAPI
             * @return the builder's arch
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderArch() { return getBuildProperties().getProperty("builderArch"); }

            /**
             * Gets the OS that was used to compile CyberAPI
             * @return the builder's OS
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderOS() { return getBuildProperties().getProperty("builderOS"); }

            /**
             * Gets the OS's version that was used to compile CyberAPI
             * @return the OS's version
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public double getBuilderVersion() { return Double.parseDouble(getBuildProperties().getProperty("builderVersion")); }

            /**
             * Gets the unix time that CyberAPI was last built, in MILLISECONDS (not SECONDS)
             * @return the unix time of CyberAPI compile
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public long getBuildUnix() { return Long.parseLong(getBuildProperties().getProperty("built")); }

            /**
             * Gets the {@link Date} that CyberAPI was built
             * @return the {@link Date} time of when CyberAPI was compiled
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public Date getBuildDate() { return new Date(getBuildUnix()); }

            /**
             * Gets the human-readable date of when CyberAPI was built (please specify a {@link java.text.SimpleDateFormat} pattern)
             * @param pattern the {@link java.text.SimpleDateFormat} pattern that should be applied to the human-readable date
             * @return the date at which CyberAPI was compiled, in human-readable time
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildDate(String pattern) { return getBuildDate(pattern, 0); }

            /**
             * Gets the human-readable date of when CyberAPI was built (please specify a {@link java.text.SimpleDateFormat} pattern) with a time offset
             * @param pattern the {@link java.text.SimpleDateFormat} pattern that should be applied to the human-readable date
             * @param timeOffset the time offset of the date
             * @return the date at which CyberAPI was compiled, in human-readable time
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildDate(String pattern, int timeOffset) { return Time.getFormattedDateUnix(getBuildUnix()/1000L, pattern, timeOffset); }


            /**
             * Gets the builder's Java vendor (where they got Java from) for CyberAPI
             * @return the builder's Java vendor
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderJavaVendor() { return getBuildProperties().getProperty("currentJavaVendor"); }

            /**
             * Gets the builder's Java version (what their java version was when they built CyberAPI) for CyberAPI
             * @return the builder's Java version
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuilderJavaVersion() { return getBuildProperties().getProperty("currentJavaVersion"); }

            /**
             * Gets the group ID used by CyberAPI
             * @return the group ID used when compiling CyberAPI
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getGroup() { return getBuildProperties().getProperty("group"); }

            /**
             * Gets the name of the CyberAPI project being used
             * @return the name of the CyberAPI project
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getName() { return getBuildProperties().getProperty("name"); }

            /**
             * Gets the targeted java version for CyberAPI's compilation
             * @return the targeted java version
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public int getTargetJavaVersion() { return Integer.parseInt(getBuildProperties().getProperty("targetJavaVersion")); }

            /**
             * Gets the user of the computer that was used to build CyberAPI
             * @return the user that built CyberAPI
             * @since 15
             * @apiNote All of this build information relates to **CYBERAPI**, not your project
             */
            public String getBuildUser() { return getBuildProperties().getProperty("user"); }

            /**
             * Converts all properties to a {@link String} that looks like this:
             * <p>"property1=value1, property2=value2"</p>
             * @return all properties to a string
             * @since 15
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
             * @since 15
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
         * @since 15
         */
        public void printBuildInformation() {
            for(String line : getPrettyBuildInformation().split("\\n")) {
                log.info(line);
            }
        }

        /**
         * Gets a list of string's of the pretty build information, usually used by {@link CyberAPISpecific#printBuildInformation()}, but you can use it to print it to a player as well!
         * @return the pretty build information in string with \n form
         * @since 15
         * @see CyberAPISpecific#getPrettyBuildInformation(boolean)
         */
        public String getPrettyBuildInformation() {
            return getPrettyBuildInformation(true);
        }

        /**
         * Gets a list of string's of the pretty build information, usually used by {@link CyberAPISpecific#printBuildInformation()}, but you can use it to print it to a player as well!
         * @param separators whether separators should be present at the start and end of the message
         * @return the pretty build information in string with \n form
         * @since 15
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
         * @since 15
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
                                .character('\u2B1B')
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
