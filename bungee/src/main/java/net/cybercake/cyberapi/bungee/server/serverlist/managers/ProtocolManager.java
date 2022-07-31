package net.cybercake.cyberapi.bungee.server.serverlist.managers;

import net.cybercake.cyberapi.bungee.server.serverlist.ServerListInfo;
import net.cybercake.cyberapi.bungee.server.serverlist.protocol.VersionVisibility;
import net.md_5.bungee.api.ProxyServer;

public class ProtocolManager {

    /**
     * Creates an instance of Protocol manager
     * @deprecated Please use {@link ServerListInfo#getProtocolManager()} instead!
     */
    @Deprecated
    @SuppressWarnings({"all"})
    public ProtocolManager() {
        resetVersionName();
        resetProtocolNumber();
        resetVersionVisibility();
    }

    private static ProtocolManager protocolManager = null;
    public static ProtocolManager protocolManager() {
        if(ProtocolManager.protocolManager == null) ProtocolManager.protocolManager = new ProtocolManager();
        return ProtocolManager.protocolManager;
    }

    private VersionVisibility versionVisibility;
    private String versionName;
    private int protocol;

    /**
     * Sets the name of the version to show when the client is outdated or {@link ProtocolManager#setAlwaysShowVersion(boolean)} is true
     * @param versionName the name to set the version to
     * @since 28
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * Gets the name of the version that is shown when the client is outdated or {@link ProtocolManager#setAlwaysShowVersion(boolean)} is true
     * @return the {@link String} version name
     * @since 28
     */
    public String getVersionName() {
        return this.versionName;
    }

    /**
     * Resets the version name back to what it should be ({@link ProxyServer#getName()} and {@link ProxyServer#getGameVersion()} - 'Waterfall 1.19')
     * @since 28
     */
    @SuppressWarnings({"deprecation"})
    public void resetVersionName() {
        this.versionName = ProxyServer.getInstance().getName() + " " + ProxyServer.getInstance().getGameVersion();
    }

    /**
     * Sets the protocol to a specified number, you can see all version protocols here: <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
     * @param protocol what to set the fake protocol number to
     * @since 28
     */
    public void setProtocolNumber(int protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the protocol, either set by the server or a custom protocol number, you can see all version protocols here: <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
     * @return the custom or server protocol number
     * @since 28
     */
    public int getProtocolNumber() {
        return this.protocol;
    }

    /**
     * Resets the protocol number to its default value (aka what the server's protocol version number is)
     * @since 28
     */
    public void resetProtocolNumber() { this.protocol = Integer.MIN_VALUE; }

    /**
     * Sets whether the server should always display like a client is outdated, never display like a client is outdated, or use default behavior
     * @param versionVisibility whether the display an outdated client
     * @since 59
     */
    public void setVersionVisibility(VersionVisibility versionVisibility) { this.versionVisibility = versionVisibility; }

    /**
     * Gets the version name's visibility, whether to show, not show, or use default behavior for {@link ProtocolManager#setVersionName(String)}
     * @return whether the server should display the version
     * @since 59
     */
    public VersionVisibility getVersionVisibility() { return this.versionVisibility; }

    /**
     * Resets the version visibility to it's default value ({@link VersionVisibility#IF_OUTDATED})
     * @since 59
     */
    public void resetVersionVisibility() { this.versionVisibility = VersionVisibility.IF_OUTDATED; }

    /**
     * Sets whether the server should always show an outdated client, which would mean the version name is constantly shown
     * @param alwaysShowVersion whether to always display outdated client
     * @since 28
     * @deprecated please use {@link ProtocolManager#setVersionVisibility(VersionVisibility)} and set to {@link VersionVisibility#VISIBLE} instead
     */
    @Deprecated
    public void setAlwaysShowVersion(boolean alwaysShowVersion) {
        this.versionVisibility = (alwaysShowVersion ? VersionVisibility.VISIBLE : VersionVisibility.IF_OUTDATED);
    }

    /**
     * Should the server always show the version from {@link ProtocolManager#setVersionName(String)}
     * @return whether the server should always show the version
     * @since 28
     * @deprecated please use {@link ProtocolManager#getVersionVisibility()} instead
     */
    @Deprecated
    public boolean shouldAlwaysShowVersion() {
        return (this.versionVisibility == VersionVisibility.VISIBLE);
    }

}
