package net.cybercake.cyberapi.spigot.server.serverlist.players;

import net.cybercake.cyberapi.spigot.server.serverlist.managers.PlayerListManager;

import javax.annotation.Nullable;

/**
 * The new player count type used in {@link PlayerListManager}
 */
public enum NewPlayerCountType {
    /**
     * Represents the value the player count should stay at.
     * <br> <br>
     * <em>Example Stay At Value:</em> current player count is 5, but server shows stay-at value of '20'
     */
    STAY_AT(Integer.class),

    /**
     * Represents a constant addition or subtraction from the player count.
     * <br> <br>
     * <em>Example Constant Value:</em> 5 - (constant)4 = 1 <br>
     * <em>Example Constant Value:</em> 5 + (constant)3 = 8
     * <br> <br>
     * If a value goes below 0 (i.e. -2), the player count will stay at 0
     */
    CONSTANT(Integer.class),

    /**
     * Represents a percentage addition or subtraction from the player count.
     * <br> <br>
     * <em>Example Percentage Value:</em> 5 - (percentage)0.10 (which would be 0.5, rounded to 1.0) = 4 <br>
     * <em>Example Percentage Value:</em> 5 + (percentage)0.50 (which would be 2.5, rounded to 3.0) = 8
     * <br> <br>
     * If a value goes below 0 (i.e. -2), the player count will stay at 0
     */
    PERCENT(Double.class),

    /**
     * Represents a random value between two integers addition or subtraction from the player count.
     * <br> <br>
     * <em>Example Random Between Value:</em> 5 - (random)3:8 (chose '4') = 1 <br>
     * <em>Example Random Between Value:</em> 5 + (random)3:8 (chose '8') = 13
     * <br> <br>
     * If a value goes below 0 (i.e. -2), the player count will stay at 0
     */
    RANDOM_BETWEEN(String.class),

    /**
     * Represents keeping the player count the same and not changing it what-so-ever.
     */
    KEEP_SAME(null);

    private final Class<?> type;
    NewPlayerCountType(@Nullable Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() { return type; }
}
