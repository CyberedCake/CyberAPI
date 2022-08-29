package net.cybercake.cyberapi.bungee.server.commands.cooldown;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * Represents a command's cooldown
 * @see CommandCooldown#CommandCooldown(TimeUnit, long)
 * @see CommandCooldown#CommandCooldown(long)
 * @see CommandCooldown#of(TimeUnit, long)
 * @see CommandCooldown#of(long)
 */
public class CommandCooldown {

    private final TimeUnit unit;
    private final long time;
    private final String message;
    private final String bypassPermission;

    /**
     * Creates a new instance of command cooldown
     * @param unit the unit of time the {@code time} variable will be using (a {@link TimeUnit})
     * @param time the amount of {@code unit}s between each command usage
     * @param message the message that will be sent when a user is currently in cooldown, use %remaining_time% for the remaining time or %remaining_time_simplified% for the largest unit only or %remaining_time_ms% for the millisecond version
     * @param bypassPermission the bypass permission that will ignore the permission
     * @since 79
     */
    public CommandCooldown(TimeUnit unit, long time, @Nullable String message, @Nullable String bypassPermission) {
        Preconditions.checkArgument(unit != null, "unit (" + TimeUnit.class + ") cannot be null!");
        Preconditions.checkArgument(time >= 0, "time (" + Long.class + ") must be greater than or equal to 0!");

        this.unit = unit;
        this.time = time;
        this.message = message;
        this.bypassPermission = bypassPermission;
    }

    /**
     * Creates a new instance of command cooldown
     * @param unit the unit of time the {@code time} variable will be using (a {@link TimeUnit})
     * @param time the amount of {@code unit}s between each command usage
     * @param message the message that will be sent when a user is currently in cooldown, use %remaining_time% for the remaining time or %remaining_time_simplified% for the largest unit only or %remaining_time_ms% for the millisecond version
     * @since 79
     */
    public CommandCooldown(TimeUnit unit, long time, @Nullable String message) {
        this(unit, time, message, null);
    }

    /**
     * Creates a new instance of command cooldown
     * @param unit the unit of time the {@code time} variable will be using (a {@link TimeUnit})
     * @param time the amount of {@code unit}s between each command usage
     * @since 79
     */
    public CommandCooldown(TimeUnit unit, long time) {
        this(unit, time, null, null);
    }

    /**
     * Creates a new instance of command cooldown
     * @param seconds the amount of {@code seconds} ({@link TimeUnit#SECONDS}) between each command usage
     * @since 79
     */
    public CommandCooldown(long seconds) {
        this(TimeUnit.SECONDS, seconds, null, null);
    }

    /**
     * Creates a new instance of command cooldown
     * @param unit the unit of time the {@code time} variable will be using (a {@link TimeUnit})
     * @param time the amount of {@code unit}s between each command usage
     * @param message the message that will be sent when a user is currently in cooldown, use %remaining_time% for the remaining time or %remaining_time_simplified% for the largest unit only or %remaining_time_ms% for the millisecond version
     * @param bypassPermission the bypass permission that will ignore the permission
     * @return the {@link CommandCooldown} instance, usually being utilized by CyberAPI
     * @since 79
     */
    public static CommandCooldown of(TimeUnit unit, long time, @Nullable String message, @Nullable String bypassPermission) {
        return new CommandCooldown(unit, time, message, bypassPermission);
    }

    /**
     * Creates a new instance of command cooldown
     * @param unit the unit of time the {@code time} variable will be using (a {@link TimeUnit})
     * @param time the amount of {@code unit}s between each command usage
     * @param message the message that will be sent when a user is currently in cooldown, use %remaining_time% for the remaining time or %remaining_time_simplified% for the largest unit only or %remaining_time_ms% for the millisecond version
     * @return the {@link CommandCooldown} instance, usually being utilized by CyberAPI
     * @since 79
     */
    public static CommandCooldown of(TimeUnit unit, long time, @Nullable String message) {
        return of(unit, time, message, null);
    }

    /**
     * Creates a new instance of command cooldown
     * @param unit the unit of time the {@code time} variable will be using (a {@link TimeUnit})
     * @param time the amount of {@code unit}s between each command usage
     * @return the {@link CommandCooldown} instance, usually being utilized by CyberAPI
     * @since 79
     */
    public static CommandCooldown of(TimeUnit unit, long time) {
        return of(unit, time, null);
    }

    /**
     * Creates a new instance of command cooldown
     * @param seconds the amount of {@code seconds} ({@link TimeUnit#SECONDS}) between each command usage
     * @return the {@link CommandCooldown} instance, usually being utilized by CyberAPI
     * @since 79
     */
    public static CommandCooldown of(long seconds) {
        return new CommandCooldown(seconds);
    }

    /**
     * @return the {@link TimeUnit} provided by the developer
     * @since 79
     * @see CommandCooldown#getBypassPermission()
     */
    public TimeUnit getUnit() { return this.unit; }

    /**
     * @return the time in between each command usage
     * @since 79
     * @see CommandCooldown#getUnit()
     */
    public long getTime() { return this.time; }

    /**
     * @return the message sent if the player is currently in cooldown
     * @since 79
     * @see CommandCooldown#getTime()
     */
    public @Nullable String getMessage() { return this.message; }

    /**
     * @return the permission required to bypass the cooldown
     * @since 79
     * @see CommandCooldown#getMessage()
     */
    public @Nullable String getBypassPermission() { return this.bypassPermission; }

}