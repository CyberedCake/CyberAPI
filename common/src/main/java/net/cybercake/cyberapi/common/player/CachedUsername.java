package net.cybercake.cyberapi.common.player;

import net.cybercake.cyberapi.common.CommonAdapter;
import net.cybercake.cyberapi.common.basic.Time;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a cached player's username
 */
public class CachedUsername {

    private UUID uuid;
    private long lastCached;
    private String username;

    private static final HashMap<UUID, CachedUsername> cachedUsernames = new HashMap<>();

    @Deprecated private CachedUsername() { }

    /**
     * Gets an instance of the cached username
     * @param uuid the UUID associated with the username you want to get
     * @deprecated there is no need to instantiate this class in this way, as it can throw an {@link IllegalArgumentException}; use the static methods instead! <br><b>(Not for deletion, just not recommended)</b>
     * @see CachedUsername#cachedUsername(UUID)
     * @throws IllegalArgumentException if the UUID is already stored
     * @since 1
     */
    @Deprecated
    @SuppressWarnings({"all"})
    public CachedUsername(@NotNull UUID uuid) {
        if(cachedUsernames.containsKey(uuid)) throw new IllegalArgumentException("That UUID is already stored in cached usernames!");

        this.uuid = uuid;
        this.lastCached = 0L;
        this.username = null;

        forceRecache();

        cachedUsernames.put(uuid, this);
    }

    /**
     * Gets an instance of the cached username
     * @param uuid the UUID associated with the username you want to get
     * @return the {@link CachedUsername} instance
     * @see CachedUsername#getUsername()
     * @since 15
     */
    public static CachedUsername cachedUsername(@NotNull UUID uuid) {
        if(cachedUsernames.containsKey(uuid)) return cachedUsernames.get(uuid);
        return new CachedUsername(uuid);
    }

    /**
     * Forces the instance to re-cache the known username, uses {@code CyberAPI.getInstance().getName(UUID)}
     * <p>This automatically happens every 30 minutes anyway!</p>
     * @since 15
     */
    public void forceRecache() {
        this.lastCached = Time.getUnix();
        this.username = CommonAdapter.adapter().getName(uuid);
    }

    /**
     * Gets the {@link UUID} of the player involved
     * @return the player's UUID
     * @since 15
     */
    public UUID getUUID() { return uuid; }

    /**
     * Gets when the last cache happened, in unix time (SECONDS)
     * @return when the last cached occurred, unix time
     * @since 15
     */
    public long getLastCached() { return lastCached; }

    /**
     * Gets how long it's been since the last cache, returns -1 if it has not been cached yet
     * @return amount of seconds since last cache
     * @since 15
     */
    public long getSecondsSinceLastCache() {
        if(lastCached == 0L) return -1L;
        return (Time.getUnix()-lastCached);
    }

    /**
     * Gets the cached username of the player involved
     * @return the {@link String} username
     * @since 15
     */
    public String getUsername() {
        if(getSecondsSinceLastCache() == -1 || getSecondsSinceLastCache() >= (30*60)) forceRecache();
        return username;
    }

}
