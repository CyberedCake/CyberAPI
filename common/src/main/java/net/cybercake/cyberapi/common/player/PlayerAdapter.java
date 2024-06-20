package net.cybercake.cyberapi.common.player;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

/**
 * An adapter for a player object in a certain API
 * @param <P> the player object only using {@link PlayerAdapter#allowedClasses the list of allowed classes}
 * @since 181
 */
public abstract class PlayerAdapter<P> {

    /**
     * Classes that are allowed and supported by CyberAPI
     * @since 181
     */
    protected static final List<String> allowedClasses = List.of(
            "org.bukkit.OfflinePlayer", "net.md_5.bungee.api.connection.ProxiedPlayer"
    );

    private final P player;

    /**
     * Creates a generic player adapter using a {@link PlayerAdapter player object}
     * @since 181
     */
    public PlayerAdapter(P player) {
        Preconditions.checkArgument(
                allowedClasses.contains(player.getClass().getCanonicalName())
                || allowedClasses.stream().anyMatch(str -> {
                    try { return Class.forName(str).isAssignableFrom(player.getClass());
                    } catch (ClassNotFoundException exception) { return false; }
                }),
                "Player must be of type (or assignable from type) " + String.join(", ", allowedClasses) + ", not " + player.getClass().getCanonicalName());

        this.player = player;
    }

    /**
     * Get the {@link UUID unique ID} of the player.
     * @return the UUID of the player
     * @since 181
     */
    public abstract UUID getUUID();

    /**
     * Get the {@link String username} of the player.
     * @return the UUID of the player
     * @since 181
     */
    public abstract String getUsername();

    /**
     * Gets the player object using the generic type.
     * @return the player object represented by the API being used
     * @since 181
     */
    public P getPlayerObject() {
        return this.player;
    }

}
