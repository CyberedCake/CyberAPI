package net.cybercake.cyberapi.common.player.adapters;

import net.cybercake.cyberapi.common.player.PlayerAdapter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * Represents a player using {@link net.md_5.bungee.api.ProxyServer BungeeCord}
 * @since 181
 */
public class BungeePlayer extends PlayerAdapter<ProxiedPlayer> {

    /**
     * Creates a new BungeePlayer object using {@link ProxiedPlayer net.md_5.bungee.api.connection.ProxiedPlayer}
     * @param player the Bungee representation of the player
     * @since 181
     */
    public BungeePlayer(ProxiedPlayer player) {
        super(player);
    }

    /**
     * Get the {@link UUID unique ID} of the {@link ProxiedPlayer Bungee player}.
     * @return the UUID of the player
     * @since 181
     */
    @Override
    public UUID getUUID() {
        return this.getPlayerObject().getUniqueId();
    }

    /**
     * Get the {@link String username} of the {@link ProxiedPlayer Bungee player}.
     * @return the username of the player
     * @since 181
     */
    @Override
    public String getUsername() {
        return this.getPlayerObject().getName();
    }

}
