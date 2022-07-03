package net.cybercake.cyberapi.settings;

import net.cybercake.cyberapi.CyberAPI;

/**
 * Creates a basic Settings builder where developers can set CyberAPI settings, and then build them with the {@link Settings#build()} method
 */
public class Settings {

    private boolean verbose;
    private boolean silenced;
    private boolean checkForUpdates;
    private boolean showPrefixInLogs;
    private FeatureSupport adventureSupport;
    private FeatureSupport miniMessageSupport;
    private FeatureSupport luckPermsSupport;
    private FeatureSupport protocolLibSupport;
    private String name;
    private String prefix;

    public Settings() {
        this.verbose = false;
        this.silenced = false;
        this.checkForUpdates = true;
        this.showPrefixInLogs = true;
        this.adventureSupport = FeatureSupport.AUTO;
        this.miniMessageSupport = FeatureSupport.AUTO;
        this.luckPermsSupport = FeatureSupport.AUTO;
        this.protocolLibSupport = FeatureSupport.AUTO;
        this.name = CyberAPI.getInstance().getDescription().getName();
        this.prefix = CyberAPI.getInstance().getDescription().getPrefix();
    }

    /**
     * Allows plugin to use 'SUPPORTED', 'UNSUPPORTED', or 'AUTO' for determining a certain feature's support
     */
    public enum FeatureSupport {
        /**
         * Represents a feature that is supported
         */
        SUPPORTED,

        /**
         * Represents a feature that is unsupported
         */
        UNSUPPORTED,

        /**
         * Allows CyberAPI to determine if a feature is {@link FeatureSupport#SUPPORTED} or {@link FeatureSupport#UNSUPPORTED}
         */
        AUTO;

        private String feature;
        FeatureSupport() {
            this.feature = "__UNKNOWN__";
        }

        public String getFeature() { return feature; }
        public void setFeature(String feature) { this.feature = feature; }
    }

    /**
     * Should CyberAPI print more debug information than usual?
     * <br> <br>
     * <em>Default Value:</em> {@code false}
     * @param verbose set this to 'true' to provide more information in console, 'false' is default
     */
    public Settings verbose(boolean verbose) { this.verbose = verbose; return this; }

    /**
     * Should CyberAPI not print anything to the console?
     * <br> <br>
     * <em>Default Value:</em> {@code false}
     * @param silenced set this to 'true' to prevent any console logging, 'false' is default
     */
    public Settings silenced(boolean silenced) { this.silenced = silenced; return this; }

    /**
     * Should CyberAPI check for updates and print if there is a newer version to console?
     * <br> <br>
     * <em>Default Value:</em> {@code true}
     * @param checkForUpdates set this to 'true' to allow CyberAPI to check for updates, 'false' is not default
     */
    public Settings checkForUpdates(boolean checkForUpdates) { this.checkForUpdates = checkForUpdates; return this; }

    /**
     * Should CyberAPI put the prefix before any logging message?
     * <br> <br>
     * <em>Default Value:</em> {@code true}
     * @param showPrefixInLogs set this to 'true' to show prefix in logs, 'false' is not default
     */
    public Settings showPrefixInLogs(boolean showPrefixInLogs) { this.showPrefixInLogs = showPrefixInLogs; return this; }

    /**
     * Should CyberAPI attempt and allow the use of Adventure components? {@link net.kyori.adventure.text.Component}
     * <br> <br>
     * If you are using a PAPER or above server (paper, purpur, pufferfish, etc.), you *should* keep this enabled
     * If you are using a SPIGOT or below server (spigot, bukkit, etc.), you *should* keep this disabled
     * <br> <br>
     * <em>Default Value:</em> {@link FeatureSupport#AUTO}
     * @param adventureSupport set this to whether or not Adventure API will be supported
     */
    public Settings adventureSupport(FeatureSupport adventureSupport) { adventureSupport.feature = "ADVENTURE_API"; this.adventureSupport = adventureSupport; return this; }

    /**
     * Should CyberAPI attempt and allow the use of Adventure's MiniMessage? {@link net.kyori.adventure.text.minimessage.MiniMessage}
     * <br> <br>
     * If you are using a PAPER or above server (paper, purpur, pufferfish, etc.), you *should* keep both of these enabled
     * If you are using a SPIGOT or below server (spigot, bukkit, etc.), you *should* keep both of these disabled
     * <br> <br>
     * <em>Default Value:</em> {@link FeatureSupport#AUTO}
     * @param miniMessageSupport set this to whether or not MiniMessage will be supported
     */
    public Settings miniMessageSupport(FeatureSupport miniMessageSupport) { adventureSupport.feature = "MINI_MESSAGE"; this.miniMessageSupport = miniMessageSupport; return this; }

    /**
     * Should CyberAPI attempt and allow the use of LuckPerms data? {@link net.luckperms.api.LuckPerms}
     * <br> <br>
     * If you have LuckPerms installed on your server (check via <em>/plugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
     * <br> <br>
     * <em>Default Value:</em> {@link FeatureSupport#AUTO}
     * @param luckPermsSupport set this to whether or not LuckPerms will be supported
     */
    public Settings luckPermsSupport(FeatureSupport luckPermsSupport) { adventureSupport.feature = "LUCKPERMS"; this.luckPermsSupport = luckPermsSupport; return this; }

    /**
     * Should CyberAPI attempt and allow the use of LuckPerms data? {@link net.luckperms.api.LuckPerms}
     * <br> <br>
     * If you have ProtocolLib installed on your server (check via <em>/plugins</em>), this should be fine to set to {@link FeatureSupport#SUPPORTED}
     * <br> <br>
     * <em>Default Value:</em> {@link FeatureSupport#AUTO}
     * @param protocolLibSupport set this to whether or not ProtocolLib will be supported
     */
    public Settings protocolLibSupport(FeatureSupport protocolLibSupport) { protocolLibSupport.feature = "PROTOCOLLIB"; this.protocolLibSupport = protocolLibSupport; return this; }

    /**
     * Sets the name of the plugin CyberAPI uses
     * <br> <br>
     * <em>Default Value:</em> *your plugin's name in the <b>plugin.yml</b>*
     * @param name set this to the name of your plugin if it's not the same as the 'plugin.yml'
     */
    public Settings name(String name) { this.name = name; return this; }

    /**
     * Sets the prefix of the plugin in console logging
     * <br> <br>
     * <em>Default Value:</em> *your plugin's prefix in the <b>plugin.yml</b>*
     * @param prefix set this to the prefix to be used when you use a "Log" method of CyberAPI
     */
    public Settings prefix(String prefix) { this.prefix = prefix; return this; }


    /**
     * Builds the settings into a {@link FinalizedSettings} object, to be used by CyberAPI
     */
    public FinalizedSettings build() {
        return new FinalizedSettings(this.toString());
    }

    /**
     * Returns a string form of the data here. This is what {@link FinalizedSettings} uses.
     * @return string of the data, looks like -> Settings{key=value, key1=value1}
     */
    public String toString() {
        return "Settings{" +
                attachValue("verbose", verbose) +
                attachValue("silenced", silenced) +
                attachValue("checkForUpdates", checkForUpdates) +
                attachValue("showPrefixInLogs", showPrefixInLogs) +
                attachValue("name", name) +
                attachValue("adventureSupport", adventureSupport) +
                attachValue("miniMessageSupport", miniMessageSupport) +
                attachValue("luckPermsSupport", luckPermsSupport) +
                attachValue("protocolLibSupport", protocolLibSupport) +
                attachValue("prefix", prefix, true);
    }

    private String attachValue(String name, FeatureSupport value) { return name + "=" + value.name() + "-" + value.getFeature() + ", "; }
    private String attachValue(String name, Object value) { return name + "=" + String.valueOf((value == null ? "${ISNULL}" : value)).replace("=", "${EQUALS}").replace(",", "${COMMA}")  + ", "; }
    private String attachValue(String name, Object value, boolean last) { return attachValue(name, value).substring(0, last ? attachValue(name, value).length()-2 : 0) + (last ? "}" : ""); }

}
