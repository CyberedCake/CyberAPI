package net.cybercake.cyberapi.bungee.chat;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import javax.annotation.Nullable;
import java.util.logging.Level;

public class CyberLogEvent extends Event implements Cancellable {

    private boolean cancelled;

    private final Class<?> caller;

    private Level level;
    private String message;
    private @Nullable String prefix;

    public CyberLogEvent(Class<?> caller, Level level, @Nullable String prefix, String message) {
        this.caller = caller;
        this.level = level;
        this.prefix = prefix;
        this.message = message;
    }

    public void setLevel(Level level) { this.level = level; }
    public void setMessage(String message) { this.message = message; }
    public void setPrefix(@Nullable String prefix) { this.prefix = prefix; }

    public Class<?> getCaller() { return this.caller; }
    public Level getLevel() { return this.level; }
    public @Nullable String getPrefix() { return this.prefix; }
    public String getMessage() { return this.message; }

    @Override public boolean isCancelled() { return this.cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

}
