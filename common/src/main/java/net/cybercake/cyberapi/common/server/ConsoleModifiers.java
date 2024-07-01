package net.cybercake.cyberapi.common.server;

import net.cybercake.cyberapi.common.CommonAdapter;
import net.cybercake.cyberapi.common.CommonManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public class ConsoleModifiers extends AbstractFilter implements Filter {

    private static final Level DEFAULT_LEVEL = Level.INFO;

    @Override
    public LifeCycle.State getState() {
        return State.STARTED;
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg) {
        @Nullable String[] message = checkContains(msg);
        if (message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).log(message[1]);
            return Result.NEUTRAL;
        }
        return isLoggable(msg);
    }

    @Override
    public Result filter(LogEvent event) {
        @Nullable String[] message = checkContains(event == null ? null : (event.getMessage().getFormattedMessage() == null ? null : event.getMessage().getFormattedMessage()));
        if (message != null) {
            java.util.logging.LogManager.getLogManager().getLogger("").log(message[0] == null ? java.util.logging.Level.INFO : toJavaLoggerLevel(message[0]), message[1]);
            return Result.DENY;
        }
        return event == null ? Result.NEUTRAL : isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        @Nullable String[] message = checkContains(msg == null ? null : (msg.getFormattedMessage() == null ? null : msg.getFormattedMessage()));
        if (message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).withThrowable(t).log(message[1]);
            return Result.NEUTRAL;
        }
        return isLoggable(msg != null ? msg.getFormattedMessage() : null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        @Nullable String[] message = checkContains(msg);
        if (message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).log(message[1], params);
            return Result.NEUTRAL;
        }
        return isLoggable(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        @Nullable String[] message = checkContains(msg == null ? null : msg.toString());
        if (message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).withThrowable(t).log(message[1]);
            return Result.NEUTRAL;
        }
        return msg == null ? Result.NEUTRAL : isLoggable(msg.toString());
    }

    @TestOnly
    private void $log(String message) {
        if (message.contains("[INTERNAL]")) return;
        java.util.logging.Logger.getAnonymousLogger().log(java.util.logging.Level.INFO, "[INTERNAL] " + message);
//        try {
//            Class<?> clazz = Class.forName("net.cybercake.cyberapi.spigot.chat.UChat");
//            Method method = clazz.getDeclaredMethod("broadcast", String.class, Predicate.class);
//            if (checkClassExists("net.md_5.bungee.api.CommandSender")) {
//                Predicate<? super net.md_5.bungee.api.CommandSender> predicate = (user) -> !user.getName().equalsIgnoreCase("console");
//                method.invoke(null, message, predicate);
//            }else if (checkClassExists("org.bukkit.command.CommandSender")) {
//                Predicate<? super org.bukkit.command.CommandSender> predicate = (user) -> !user.getServer().getConsoleSender().equals(user);
//                method.invoke(null, message, predicate);
//            }else{
//                method = clazz.getDeclaredMethod("broadcast", String.class);
//                method.invoke(null, message);
//            }
//        } catch (Exception exception) {
//            java.util.logging.LogManager.getLogManager().getLogger("").log(java.util.logging.Level.SEVERE, "Failed: " + exception);
//        }
    }

    private boolean checkClassExists(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
            return false;
        }
    }

    private @Nullable String[] checkContains(@Nullable String msg) {
        if (msg == null) return null;
        if (!msg.contains(CommonManager.THREE_SEPARATION_CHARACTERS)) return null;
        return new String[]{msg.split(CommonManager.THREE_SEPARATION_CHARACTERS)[0], msg.split(CommonManager.THREE_SEPARATION_CHARACTERS)[1]};
    }

    private java.util.logging.Level toJavaLoggerLevel(String level) {
        if (level.contains("LEVEL_")) level = level.substring("LEVEL_".length());
        return switch(level) {
            case "INFO" -> java.util.logging.Level.INFO;
            case "WARN" -> java.util.logging.Level.WARNING;
            case "ERROR" -> java.util.logging.Level.SEVERE;
            default -> java.util.logging.Level.parse(DEFAULT_LEVEL.toString());
        };
    }

    private Level getLevel(String level) {
        try {
            return Level.getLevel(level);
        } catch (Exception exception) {
            return DEFAULT_LEVEL;
        }
    }

    // my hard-coded way to remove Reflection's messages
    // create an issue on GitHub (https://github.com/CyberedCake/CyberAPI/issues) if you have a better way!
    private Result isLoggable(String msg) {
        if (msg == null) return Result.NEUTRAL;
        if (!msg.contains("Reflections took") && !msg.contains(CommonManager.THREE_SEPARATION_CHARACTERS)) return Result.NEUTRAL;
        return Result.DENY;
    }
}