package net.cybercake.cyberapi.spigot.chat;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.logging.Level;

public class CyberLogEvent extends Event implements Cancellable {

    public static final HandlerList HANDLER_LIST = new HandlerList(); // required for custom event

    private boolean cancelled;

    private final String caller;

    private Level level;
    private String message;
    private @Nullable String prefix;

    public CyberLogEvent(String caller, Level level, @Nullable String prefix, String message) {
        this.caller = caller;
        this.level = level;
        this.prefix = prefix;
        this.message = message;
    }

    public void setLevel(Level level) { this.level = level; }
    public void setMessage(String message) { this.message = message; }
    public void setPrefix(@Nullable String prefix) { this.prefix = prefix; }

    public String getCaller() { return this.caller; }
    public Level getLevel() { return this.level; }
    public @Nullable String getPrefix() { return this.prefix; }
    public String getMessage() { return this.message; }

    @Override public boolean isCancelled() { return this.cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    @NotNull @Override public HandlerList getHandlers() { return HANDLER_LIST; } // required for custom event
}
