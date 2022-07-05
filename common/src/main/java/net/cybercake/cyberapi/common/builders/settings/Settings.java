package net.cybercake.cyberapi.common.builders.settings;

import org.jetbrains.annotations.Nullable;

/**
 * @since 3.3
 */
@SuppressWarnings({"all"})
public class Settings {

    /**
     * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link Settings} instance
     * @return the Builder instance
     * @since 3.3
     */
    public static Builder builder() { return new Builder(); }

    /**
     * @since 3.3
     */
    public static class Builder {
        private boolean verbose, silenced, checkForUpdates, showPrefixInLogs, muteStartMessage;
        private FeatureSupport adventureSupport, miniMessageSupport, luckPermsSupport, protocolLibSupport;
        private String name, prefix, commandsPath;

        /**
         * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link Settings}
         * @since 3.3
         * @deprecated use {@link Settings#builder()} instead
         */
        @Deprecated
        public Builder() {
            this.verbose = false;
            this.silenced = false;
            this.checkForUpdates = true;
            this.showPrefixInLogs = false;
            this.muteStartMessage = false;
            this.adventureSupport = FeatureSupport.AUTO;
            this.miniMessageSupport = FeatureSupport.AUTO;
            this.luckPermsSupport = FeatureSupport.AUTO;
            this.protocolLibSupport = FeatureSupport.AUTO;
            this.name = null;
            this.prefix = null;
            this.commandsPath = null;
        }

        /**
         * Should CyberAPI print more debug information than usual?
         * <br> <br>
         * <em>Default Value:</em> {@code false}
         * @param verbose set this to 'true' to provide more information in console, 'false' is default
         */
        public Builder verbose(boolean verbose) { this.verbose = verbose; return this; }

        /**
         * Should CyberAPI not print anything to the console?
         * <br> <br>
         * <em>Default Value:</em> {@code false}
         * @param silenced set this to 'true' to prevent any console logging, 'false' is default
         */
        public Builder silenced(boolean silenced) { this.silenced = silenced; return this; }

        /**
         * Should CyberAPI check for updates and print if there is a newer version to console?
         * <br> <br>
         * <em>Default Value:</em> {@code true}
         * @param checkForUpdates set this to 'true' to allow CyberAPI to check for updates, 'false' is not default
         */
        public Builder checkForUpdates(boolean checkForUpdates) { this.checkForUpdates = checkForUpdates; return this; }

        /**
         * Should CyberAPI put the prefix before any logging message?
         * <br> <br>
         * <em>Default Value:</em> {@code true}
         * @param showPrefixInLogs set this to 'true' to show prefix in logs, 'false' is not default
         */
        public Builder showPrefixInLogs(boolean showPrefixInLogs) { this.showPrefixInLogs = showPrefixInLogs; return this; }

        /**
         * Should CyberAPI mute the start message?
         * <br> <br>
         * <em>Default Value:</em> {@code false}
         * @param muteStartMessage set this to 'true' to mute the start message, 'false' is default
         */
        public Builder muteStartMessage(boolean muteStartMessage) { this.muteStartMessage = muteStartMessage; return this; }


        /**
         * Should CyberAPI attempt and allow the use of Adventure components? {@link net.kyori.adventure.text.Component}
         * <br> <br>
         * If you are using a PAPER or above server (paper, purpur, pufferfish, etc.), you *should* keep this enabled
         * If you are using a SPIGOT or below server (spigot, bukkit, etc.), you *should* keep this disabled
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param adventureSupport set this to whether or not Adventure API will be supported
         */
        public Builder adventureSupport(FeatureSupport adventureSupport) { this.adventureSupport.setFeature("ADVENTURE_API"); this.adventureSupport = adventureSupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of Adventure's MiniMessage? {@link net.kyori.adventure.text.minimessage.MiniMessage}
         * <br> <br>
         * If you are using a PAPER or above server (paper, purpur, pufferfish, etc.), you *should* keep both of these enabled
         * If you are using a SPIGOT or below server (spigot, bukkit, etc.), you *should* keep both of these disabled
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param miniMessageSupport set this to whether or not MiniMessage will be supported
         */
        public Builder miniMessageSupport(FeatureSupport miniMessageSupport) { this.miniMessageSupport.setFeature("MINI_MESSAGE"); this.miniMessageSupport = miniMessageSupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of LuckPerms data? {@link net.luckperms.api.LuckPerms}
         * <br> <br>
         * If you have LuckPerms installed on your server (check via <em>/plugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param luckPermsSupport set this to whether or not LuckPerms will be supported
         */
        public Builder luckPermsSupport(FeatureSupport luckPermsSupport) { this.luckPermsSupport.setFeature("LUCKPERMS"); this.luckPermsSupport = luckPermsSupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of LuckPerms data? {@link net.luckperms.api.LuckPerms}
         * <br> <br>
         * If you have ProtocolLib installed on your server (check via <em>/plugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param protocolLibSupport set this to whether or not ProtocolLib will be supported
         */
        public Builder protocolLibSupport(FeatureSupport protocolLibSupport) { this.protocolLibSupport.setFeature("PROTOCOLLIB"); this.protocolLibSupport = protocolLibSupport; return this; }

        /**
         * Sets the name of the plugin CyberAPI uses
         * <br> <br>
         * <em>Default Value:</em> *your plugin's name in the <b>plugin.yml</b>*
         * @param name set this to the name of your plugin if it's not the same as the 'plugin.yml'
         */
        public Builder name(String name) { this.name = name; return this; }

        /**
         * Sets the prefix of the plugin in console logging
         * <br> <br>
         * <em>Default Value:</em> *your plugin's prefix in the <b>plugin.yml</b>*
         * @param prefix set this to the prefix to be used when you use a "Log" method of CyberAPI
         */
        public Builder prefix(String prefix) { this.prefix = prefix; return this; }

        /**
         * Sets the commands' path to a certain path. This is for registering commands, if no path is given, it will attempt to get your path for you, and it can take a while everytime your server starts if this value is not set.
         * <br> <br>
         * <em>Default Value:</em> *your project's group ID*
         * @param commandsPath set this to the path where all your commands are localed (usually like 'net.cybercake.myplugin.commands')
         */
        public Builder commandsPath(String commandsPath) { this.commandsPath = commandsPath; return this; }

        /**
         * Builds the builder into an {@link Settings} instance
         * @return the {@link Settings} instance
         * @since 3.3
         */
        public Settings build() {
            return new Settings(this);
        }
    }

    private final Builder builder;

    /**
     * The {@link Settings} instance, created by the {@link Builder} intsance
     * @param builder the builder that can then be transformed into {@link Settings} and read by {@link CyberAPI}
     * @since 3.3
     */
    public Settings(Builder builder) { this.builder = builder; }

    /**
     * Gets whether CyberAPI should print verbose information
     * @return should print verbose
     * @since 3.3
     */
    public boolean isVerbose() { return builder.verbose; }

    /**
     * Gets whether CyberAPI should silence all logging (except for exceptions)
     * @return should be silenced
     * @since 3.3
     */
    public boolean isSilenced() { return builder.silenced; }

    /**
     * Gets whether CyberAPI should check for updates
     * @return should check for updates
     * @since 3.3
     */
    public boolean shouldCheckForUpdates() { return builder.checkForUpdates; }

    /**
     * Gets whether CyberAPI should print the plugin's prefix in console every time they use a {@link Log} feature
     * @return should show prefix in logs/console
     * @since 3.3
     */
    public boolean shouldShowPrefixInLogs() { return builder.showPrefixInLogs; }

    /**
     * Gets whether CyberAPI should mute the start message
     * @return should mute start message
     * @since 3.3.2
     */
    public boolean shouldMuteStartMessage() { return builder.muteStartMessage; }

    /**
     * Gets whether CyberAPI supports Adventure API or not
     * @return supports Adventure API
     * @since 3.4
     */
    public FeatureSupport supportsAdventureAPI() { return builder.adventureSupport; }

    /**
     * Gets whether CyberAPI supports MiniMessage or not
     * @return supports MiniMessage
     * @since 3.4
     */
    public FeatureSupport supportsMiniMessage() { return builder.miniMessageSupport; }

    /**
     * Gets whether CyberAPI supports LuckPerms or not
     * @return supports LuckPerms
     * @since 3.3
     */
    public FeatureSupport supportsLuckPerms() { return builder.luckPermsSupport; }

    /**
     * Gets whether CyberAPI supports ProtocolLib or not
     * @return supports ProtocolLib
     * @since 3.4
     */
    public FeatureSupport supportsProtocolLib() { return builder.protocolLibSupport; }

    /**
     * Gets the name of the plugin
     * @return the plugin name
     * @since 3.3
     */
    public @Nullable String getName() { return builder.name; }

    /**
     * Gets the prefix of the plugin that will show up before logging messages
     * @return the plugin prefix
     * @since 3.3
     */
    public @Nullable String getPrefix() { return builder.prefix; }

    /**
     * Gets the package name where all the developer's commands are stored
     * @return the commands' path
     * @since 3.3
     */
    public @Nullable String getCommandsPath() { return builder.commandsPath; }

}
