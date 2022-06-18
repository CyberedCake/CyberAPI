package net.cybercake.cyberapi.chat;

import net.cybercake.cyberapi.CyberAPI;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Log {

    /**
     * Logs an "[INFO]" message to the console, typically in the default color
     * @param message the message to log
     * @since 3.0.0
     */
    public static void info(String message) { log(Level.INFO, message); }

    /**
     * Logs a "[WARN]" message to the console, typically in yellow
     * @param message the message to log
     * @since 3.0.0
     */
    public static void warn(String message) { log(Level.WARNING, message); }

    /**
     * Logs a "[ERROR]" message to the console, typically in red, also known as "SEVERE"
     * @param message the message to log
     * @since 3.0.0
     */
    public static void error(String message) { log(Level.SEVERE, message); }

    /**
     * Log at whatever level you want to the console
     * @param level the level at which to log
     * @param message the message to log
     * @since 3.0.0
     */
    public static void log(Level level, String message) {
        Bukkit.getLogger().log(level, UChat.chat((Boolean.TRUE.equals(CyberAPI.getInstance().getSettings().getBoolean("showPrefixInLogs")) ? "[" + CyberAPI.getInstance().getPrefix() + "] " : "") + message)); }

}
