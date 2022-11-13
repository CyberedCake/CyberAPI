package net.cybercake.cyberapi.spigot.chat;

import net.cybercake.cyberapi.spigot.CyberAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.logging.Level;

public class Log {

    /**
     * Logs an "[INFO]" message to the console, typically in the default color
     * @param message the message to log
     * @since 1
     */
    public static void info(String message) { log(Level.INFO, message, Thread.currentThread().getStackTrace()[2]); }

    /**
     * Logs a "[WARN]" message to the console, typically in yellow
     * @param message the message to log
     * @since 1
     */
    public static void warn(String message) { log(Level.WARNING, message, Thread.currentThread().getStackTrace()[2]); }

    /**
     * Logs an "[ERROR]" message to the console, typically in red, also known as "SEVERE"
     * @param message the message to log
     * @since 1
     */
    public static void error(String message) { log(Level.SEVERE, message, Thread.currentThread().getStackTrace()[2]); }

    /**
     * Logs a "[VERBOSE]" message to the console, typically in dark gray or the default color, but only if the verbose option for your plugin is set to true (see {@link CyberAPI#setPluginVerbose(boolean)})
     * @param message the message to send if verbose is enabled
     * @since 99
     */
    public static void verbose(String message) { verbose(Thread.currentThread().getStackTrace()[2], message); }

    /**
     * <b>internal method only, do not touch!</b>
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings({"all"}) // remove the "deprecated member is still in use" as the deprecation is just for effect
    private static void verbose(StackTraceElement stackTraceElement, String message) {
        if(CyberAPI.getInstance().isPluginVerbose())
            log(Level.INFO, ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "VERBOSE/" + stackTraceElement.getClassName() + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + message, stackTraceElement);
    }

    /**
     * Log at whatever level you want to the console
     * @param level the level at which to log
     * @param message the message to log
     * @since 1
     */
    public static void log(Level level, String message, StackTraceElement stackTraceElement) {
        if(
                CyberAPI.getInstance() == null // make sure CyberAPI is enabled
                || Arrays.stream(Thread.currentThread().getStackTrace()).anyMatch(element -> element.getMethodName().equalsIgnoreCase("onDisable")) // fix the onDisable glitch where it would not work if being disabled
                || !CyberAPI.getInstance().isEnabled() // make sure CyberAPI (and the plugin) is enabled!!!!1!
        ) {
            Bukkit.getLogger().log(level, UChat.chat(Boolean.TRUE == CyberAPI.getInstance().getSettings().shouldShowPrefixInLogs() ? "[" + CyberAPI.getInstance().getPrefix() + "] " : "") + message);
            return;
        }
        try {
            Bukkit.getScheduler().runTask(CyberAPI.getInstance(), () -> { // run task sync because it's just better
                CyberLogEvent logEvent = new CyberLogEvent(stackTraceElement, level, (Boolean.TRUE.equals(CyberAPI.getInstance().getSettings().shouldShowPrefixInLogs()) ? "[" + CyberAPI.getInstance().getPrefix() + "] " : null), message);
                Bukkit.getPluginManager().callEvent(logEvent);
                if(logEvent.isCancelled()) return;
                Bukkit.getLogger().log(logEvent.getLevel(), UChat.chat((logEvent.getPrefix() == null ? "" : logEvent.getPrefix()) + logEvent.getMessage()));
            });
        } catch (Exception exception) {
            throw new IllegalStateException("Error occurred whilst logging in " + Log.class.getCanonicalName() + " (potential caller: " + stackTraceElement + ")", exception);
        }
    }

}
