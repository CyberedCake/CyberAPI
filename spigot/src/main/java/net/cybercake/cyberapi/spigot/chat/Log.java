package net.cybercake.cyberapi.spigot.chat;

import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.Validators;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.logging.Level;

public class Log {

    /**
     * Logs an "[INFO]" message to the console, typically in the default color
     * @param message the message to log
     * @since 1
     */
    public static void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a "[WARN]" message to the console, typically in yellow
     * @param message the message to log
     * @since 1
     */
    public static void warn(String message) { log(Level.WARNING, message); }

    /**
     * Logs a "[ERROR]" message to the console, typically in red, also known as "SEVERE"
     * @param message the message to log
     * @since 1
     */
    public static void error(String message) { log(Level.SEVERE, message); }

    /**
     * Log at whatever level you want to the console
     * @param level the level at which to log
     * @param message the message to log
     * @since 1
     */
    public static void log(Level level, String message) {
        if(
                CyberAPI.getInstance() == null
                || Arrays.stream(Thread.currentThread().getStackTrace()).anyMatch(element -> element.getMethodName().equalsIgnoreCase("onDisable"))
                || !CyberAPI.getInstance().isEnabled()
        ) {
            Bukkit.getLogger().log(level, UChat.chat(Boolean.TRUE == CyberAPI.getInstance().getSettings().shouldShowPrefixInLogs() ? "[" + CyberAPI.getInstance().getPrefix() + "] " : "") + message);
            return;
        }
        try {
            Bukkit.getScheduler().runTask(CyberAPI.getInstance(), () -> {
                CyberLogEvent logEvent = new CyberLogEvent(Validators.getCaller(), level, (Boolean.TRUE.equals(CyberAPI.getInstance().getSettings().shouldShowPrefixInLogs()) ? "[" + CyberAPI.getInstance().getPrefix() + "] " : null), message);
                Bukkit.getPluginManager().callEvent(logEvent);
                if(logEvent.isCancelled()) return;
                Bukkit.getLogger().log(logEvent.getLevel(), UChat.chat((logEvent.getPrefix() == null ? "" : logEvent.getPrefix()) + logEvent.getMessage()));
            });
        } catch (Exception exception) {
            throw new IllegalStateException("Error occurred whilst logging in " + Log.class.getCanonicalName() + " (potential caller: " + Validators.getFirstNonCyberAPIStack(Arrays.asList(exception.getStackTrace())) + ")", exception);
        }
    }

}
