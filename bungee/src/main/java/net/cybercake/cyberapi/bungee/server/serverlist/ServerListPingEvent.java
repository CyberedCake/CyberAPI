package net.cybercake.cyberapi.bungee.server.serverlist;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.bungee.server.serverlist.motd.MOTD;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * The server list ping event for CyberAPI, an alternative way to change the server list. It is, however, still recommended that you use {@link CyberAPI#getServerListInfo()} instead!
 */
@SuppressWarnings({"unused"})
public class ServerListPingEvent extends Event implements Cancellable {
    private boolean cancelled;

    private final PendingConnection connection;

    private String versionName;
    private int protocolVersion;
    private boolean versionNameAlwaysVisible;
    private boolean playerListVisible;
    private MOTD motd;
    private int maxPlayers;
    private int onlinePlayerCount;
    private List<String> onlinePlayers;

    public ServerListPingEvent(PendingConnection connection, String versionName, int protocolVersion, boolean playerListVisible, boolean versionNameAlwaysVisible, MOTD motd, int maxPlayers, int onlinePlayerCount, List<String> onlinePlayers) {
        this.connection = connection;

        this.versionName = versionName;
        this.protocolVersion = protocolVersion;
        this.playerListVisible = playerListVisible;
        this.versionNameAlwaysVisible = versionNameAlwaysVisible;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
        this.onlinePlayerCount = onlinePlayerCount;
        this.onlinePlayers = onlinePlayers;
        this.cancelled = false;
    }

    public void setVersionName(String versionName) { this.versionName = versionName; }
    public void setProtocolVersion(int protocolVersion) { this.protocolVersion = protocolVersion; }
    public void setPlayerListVisible(boolean playerListVisible) { this.playerListVisible = playerListVisible; }
    public void setVersionNameAlwaysVisible(boolean versionNameAlwaysVisible) { this.versionNameAlwaysVisible = versionNameAlwaysVisible; }
    public void setMOTD(MOTD motd) { this.motd = motd; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setOnlinePlayerCount(int onlinePlayerCount) { this.onlinePlayerCount = onlinePlayerCount; }
    public void setOnlinePlayers(List<String> onlinePlayers) { this.onlinePlayers = onlinePlayers; }
    public void setOnlinePlayers(String... onlinePlayers) { this.onlinePlayers = new ArrayList<>(List.of(onlinePlayers)); }

    public PendingConnection getPendingConnection() { return connection; }

    public String getVersionName() { return this.versionName; }
    public int getProtocolVersion() { return this.protocolVersion; }
    public boolean isPlayerListVisible() { return this.playerListVisible; }
    public boolean isVersionNameAlwaysVisible() { return this.versionNameAlwaysVisible; }
    public MOTD getMOTD() { return this.motd; }
    public int getMaxPlayers() { return this.maxPlayers; }
    public int getOnlinePlayerCount() { return this.onlinePlayerCount; }
    public List<String> getOnlinePlayers() { return onlinePlayers; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
