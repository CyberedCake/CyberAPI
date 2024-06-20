package net.cybercake.cyberapi.common.player.adapters;

import net.cybercake.cyberapi.common.player.PlayerAdapter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents a player using {@link org.bukkit.Bukkit Bukkit}
 * @since 181
 */
public class BukkitPlayer extends PlayerAdapter<OfflinePlayer> {

    /**
     * Creates a new BukkitPlayer object using {@link OfflinePlayer org.bukkit.OfflinePlayer}
     * @param player the Bukkit representation of the player
     * @since 181
     */
    public BukkitPlayer(OfflinePlayer player) {
        super(player);
    }

    /**
     * Get the {@link UUID unique ID} of the {@link OfflinePlayer Bukkit player}.
     * @return the UUID of the player
     * @since 181
     */
    @Override
    public UUID getUUID() {
        return this.getPlayerObject().getUniqueId();
    }

    /**
     * Get the {@link String username} of the {@link OfflinePlayer Bukkit player}.
     * @return the username of the player
     * @since 181
     */
    @Override
    public String getUsername() {
        return this.getPlayerObject().getName();
    }
}
