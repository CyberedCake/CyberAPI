package net.cybercake.cyberapi.common.builders.settings;

import org.jetbrains.annotations.Nullable;

/**
 * @since 15
 */
@SuppressWarnings({"all"})
public class Settings {

    /**
     * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link Settings} instance
     * @return the Builder instance
     * @since 15
     */
    public static Builder builder() { return new Builder(); }

    /**
     * @since 15
     */
    public static class Builder {
        private boolean verbose, silenced, checkForUpdates, showPrefixInLogs, muteStartMessage;
        private FeatureSupport adventureSupport, miniMessageSupport, luckPermsSupport, protocolLibSupport, placeholderAPISupport, protocolizeSupport;
        private String name, prefix, mainPackage;
        private Class<?>[] disableAutoRegisterFor;

        /**
         * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link Settings}
         * @since 15
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
            this.placeholderAPISupport = FeatureSupport.AUTO;
            this.protocolizeSupport = FeatureSupport.AUTO;
            this.name = null;
            this.prefix = null;
            this.mainPackage = null;
            this.disableAutoRegisterFor = null;
        }

        /**
         * Should CyberAPI print more debug information than usual?
         * <br> <br>
         * <em>Has no effect on whether the plugin is verbose or not.</em>
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
         * Should CyberAPI attempt and allow the use of Adventure components?
         * <br> <br>
         * If you are using a PAPER or above server (paper, purpur, pufferfish, etc.), you *should* keep this enabled
         * If you are using a SPIGOT or below server (spigot, bukkit, etc.), you *should* keep this disabled
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param adventureSupport set this to whether or not Adventure API will be supported
         * @apiNote This is only available for **SPIGOT**
         */
        public Builder adventureSupport(FeatureSupport adventureSupport) { this.adventureSupport.setFeature("ADVENTURE_API"); this.adventureSupport = adventureSupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of Adventure's MiniMessage?
         * <br> <br>
         * If you are using a PAPER or above server (paper, purpur, pufferfish, etc.), you *should* keep both of these enabled
         * If you are using a SPIGOT or below server (spigot, bukkit, etc.), you *should* keep both of these disabled
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param miniMessageSupport set this to whether or not MiniMessage will be supported
         * @apiNote This is only available for **SPIGOT**
         */
        public Builder miniMessageSupport(FeatureSupport miniMessageSupport) { this.miniMessageSupport.setFeature("MINI_MESSAGE"); this.miniMessageSupport = miniMessageSupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of LuckPerms data?
         * <br> <br>
         * If you have LuckPerms installed on your server (check via <em>/plugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param luckPermsSupport set this to whether or not LuckPerms will be supported
         * @apiNote This is only available for SPIGOT and BUNGEECORD
         */
        public Builder luckPermsSupport(FeatureSupport luckPermsSupport) { this.luckPermsSupport.setFeature("LUCKPERMS"); this.luckPermsSupport = luckPermsSupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of ProtocolLib data?
         * <br> <br>
         * If you have ProtocolLib installed on your server (check via <em>/plugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param protocolLibSupport set this to whether or not ProtocolLib will be supported
         * @apiNote This is only available for **SPIGOT**
         */
        public Builder protocolLibSupport(FeatureSupport protocolLibSupport) { this.protocolLibSupport.setFeature("PROTOCOLLIB"); this.protocolLibSupport = protocolLibSupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of PlaceholderAPI data?
         * <br> <br>
         * If you have PlaceholderAPI installed on your server (check via <em>/plugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param placeholderAPISupport set this to whether or not PlaceholderAPI will be supported
         * @apiNote This is only available for **SPIGOT**
         */
        public Builder placeholderAPISupport(FeatureSupport placeholderAPISupport) { this.placeholderAPISupport.setFeature("PLACEHOLDERAPI"); this.placeholderAPISupport = placeholderAPISupport; return this; }

        /**
         * Should CyberAPI attempt and allow the use of Protocolize data?
         * <br> <br>
         * If you have Protocolize installed on your server (check via <em>/gplugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
         * <br> <br>
         * <em>Default Value:</em> {@link FeatureSupport#AUTO}
         * @param protocolizeSupport set this to whether or not Protocolize will be supported
         * @apiNote This is only available for **BUNGEECORD**
         */
        public Builder protocolizeSupport(FeatureSupport protocolizeSupport) { this.protocolizeSupport.setFeature("PROTOCOLIZE"); this.protocolizeSupport = protocolizeSupport; return this; }

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
         * @param commandsPath set this to the path where all your commands are located (usually like 'net.cybercake.myplugin.commands')
         * @deprecated please use {@link Builder#mainPackage(String)} instead, as the other method will define the actual package rather than just the commands path
         */
        @Deprecated
        public Builder commandsPath(String commandsPath) { this.mainPackage = commandsPath; return this; }

        /**
         * Sets the main package of the plugin to a certain path. This is for registering commands and registering listeners, and if no path is given, it will attempt to get your path for you, and it can take a while everytime your server starts if this value is not set.
         * <br> <br>
         * <em>Default Value:</em> **your project's group ID*
         * @param mainPackage set this to the path where your package is located (usually like 'net.cybercake.myplugin')
         */
        public Builder mainPackage(String mainPackage) { this.mainPackage = mainPackage; return this; }

        public Builder disableAutoRegistering(Class<?>... disableAutoRegisterFor) { this.disableAutoRegisterFor = disableAutoRegisterFor; return this; }

        /**
         * Builds the builder into an {@link Settings} instance
         * @return the {@link Settings} instance
         * @since 15
         */
        public Settings build() {
            return new Settings(this);
        }
    }

    private final Builder builder;

    /**
     * The {@link Settings} instance, created by the {@link Builder} intsance
     * @param builder the builder that can then be transformed into {@link Settings} and read by {@code CyberAPI}
     * @since 15
     */
    public Settings(Builder builder) { this.builder = builder; }

    /**
     * Gets whether CyberAPI should print verbose information
     * <br> <br>
     * <em>Has no effect on whether the plugin is verbose or not.</em>
     * @return should print verbose
     * @since 15
     */
    public boolean isVerbose() { return builder.verbose; }

    /**
     * Gets whether CyberAPI should silence all logging (except for exceptions)
     * @return should be silenced
     * @since 15
     */
    public boolean isSilenced() { return builder.silenced; }

    /**
     * Gets whether CyberAPI should check for updates
     * @return should check for updates
     * @since 15
     */
    public boolean shouldCheckForUpdates() { return builder.checkForUpdates; }

    /**
     * Gets whether CyberAPI should print the plugin's prefix in console every time they use a {@code CyberAPI Log Class} feature
     * @return should show prefix in logs/console
     * @since 15
     */
    public boolean shouldShowPrefixInLogs() { return builder.showPrefixInLogs; }

    /**
     * Gets whether CyberAPI should mute the start message
     * @return should mute start message
     * @since 17
     */
    public boolean shouldMuteStartMessage() { return builder.muteStartMessage; }

    /**
     * Gets whether CyberAPI supports Adventure API or not
     * @return supports Adventure API
     * @since 24
     */
    public FeatureSupport supportsAdventureAPI() { return builder.adventureSupport; }

    /**
     * Gets whether CyberAPI supports MiniMessage or not
     * @return supports MiniMessage
     * @since 24
     */
    public FeatureSupport supportsMiniMessage() { return builder.miniMessageSupport; }

    /**
     * Gets whether CyberAPI supports LuckPerms or not
     * @return supports LuckPerms
     * @since 15
     */
    public FeatureSupport supportsLuckPerms() { return builder.luckPermsSupport; }

    /**
     * Gets whether CyberAPI supports ProtocolLib or not
     * @return supports ProtocolLib
     * @since 24
     */
    public FeatureSupport supportsProtocolLib() { return builder.protocolLibSupport; }

    /**
     * Gets whether CyberAPI supports PlaceholderAPI or not
     * @return supports PlaceholderAPI
     * @since 52
     */
    public FeatureSupport supportsPlaceholderAPI() { return builder.placeholderAPISupport; }

    /**
     * Gets whether CyberAPI supports Protocolize or not
     * @return supports Protocolize
     * @since 28
     */
    public FeatureSupport supportsProtocolize() { return builder.protocolizeSupport; }

    /**
     * Gets the name of the plugin
     * @return the plugin name
     * @since 15
     */
    public @Nullable String getName() { return builder.name; }

    /**
     * Gets the prefix of the plugin that will show up before logging messages
     * @return the plugin prefix
     * @since 15
     */
    public @Nullable String getPrefix() { return builder.prefix; }

    /**
     * Gets the package name where all the developer's commands are stored
     * @return the commands' path <em>(deprecated note: this will return the same thing as the main package path, so this no longer accurately reflects the plugin's command's path)</em>>
     * @since 15
     * @deprecated please use {@link Settings#getMainPackagePath()} instead, as that will return the same value anyway
     */
    @Deprecated
    public @Nullable String getCommandsPath() { return builder.mainPackage; }

    /**
     * Gets the main package where the plugin is located
     * @return the main package path
     * @since 98
     */
    public @Nullable String getMainPackagePath() { return builder.mainPackage; }

    /**
     * Gets the classes where the auto-registering has been disabled (usually Listeners or Commands)
     * @return the classes where auto-registering via CyberAPI is disabled
     * @since 98
     */
    public @Nullable Class<?>[] getDisabledAutoRegisteredClasses() { return builder.disableAutoRegisterFor; }

}
