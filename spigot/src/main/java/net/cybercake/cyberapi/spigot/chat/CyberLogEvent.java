package net.cybercake.cyberapi.spigot.chat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.logging.Level;

public class CyberLogEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList(); // required for custom event

    private boolean cancelled;

    private final StackTraceElement stackTraceElement;

    private Level level;
    private String message;
    private @Nullable String prefix;

    public CyberLogEvent(StackTraceElement stackTraceElement, Level level, @Nullable String prefix, String message) {
        this.stackTraceElement = stackTraceElement;
        this.level = level;
        this.prefix = prefix;
        this.message = message;
    }

    /**
     * Sets the log's {@link Level}
     * @param level the new log {@link Level}
     * @since 88
     */
    public void setLevel(Level level) { this.level = level; }

    /**
     * Set's the contents of the log to a message
     * @param message the new, now overridden, message
     * @since 88
     */
    public void setMessage(String message) { this.message = message; }

    /**
     * Set's the prefix of the log, usually the plugin's name
     * @param prefix the new prefix
     * @since 88
     */
    public void setPrefix(@Nullable String prefix) { this.prefix = prefix; }

    /**
     * @return the caller in {@link String} form, looks like: {@code me.package.myplugin.Main.onEnable(Main.java:33)}
     * @deprecated please use {@link CyberLogEvent#getStackTraceElementCaller()} instead, as it can do more than this {@link String} can
     * @since 88
     */
    @Deprecated(forRemoval = true) public String getCaller() { return this.stackTraceElement.toString(); }

    /**
     * @return the {@link StackTraceElement} of what called the log event
     * @since 88
     */
    public StackTraceElement getStackTraceElementCaller() { return this.stackTraceElement; }

    /**
     * @return the log level {@link Level}
     * @since 88
     */
    public Level getLevel() { return this.level; }

    /**
     * @return the prefix of the message
     * @since 88
     */
    public @Nullable String getPrefix() { return this.prefix; }

    /**
     * @return the contents of the log message
     * @since 88
     */
    public String getMessage() { return this.message; }

    @Override public boolean isCancelled() { return this.cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public static HandlerList getHandlerList() { return HANDLER_LIST; }
    @NotNull @Override public HandlerList getHandlers() { return HANDLER_LIST; } // required for custom event
}
