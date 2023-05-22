package net.cybercake.cyberapi.common.server;

import net.cybercake.cyberapi.common.CommonManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.Nullable;

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
        if(message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).log(message[1]);
            return Result.NEUTRAL;
        }
        return isLoggable(msg);
    }

    @Override
    public Result filter(LogEvent event) {
        @Nullable String[] message = checkContains(event == null ? null : (event.getMessage().getFormattedMessage() == null ? null : event.getMessage().getFormattedMessage()));
        if(message != null && event != null) {
            LogBuilder builder = LogManager.getRootLogger().atLevel(getLevel(message[0]));
            if(event.getMarker() != null)
                builder = builder.withMarker(event.getMarker());
            if(event.getThrown() != null)
                builder = builder.withThrowable(event.getThrown());
            builder.log(message[1]);
            java.util.logging.LogManager.getLogManager().getLogger("").log(message[0] == null ? java.util.logging.Level.INFO : toJavaLoggerLevel(message[0]), message[1]);
            return Result.DENY;
        }
        return event == null ? Result.NEUTRAL : isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        @Nullable String[] message = checkContains(msg == null ? null : (msg.getFormattedMessage() == null ? null : msg.getFormattedMessage()));
        if(message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).withThrowable(t).log(message[1]);
            return Result.NEUTRAL;
        }
        return isLoggable(msg != null ? msg.getFormattedMessage() : null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        @Nullable String[] message = checkContains(msg);
        if(message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).log(message[1], params);
            return Result.NEUTRAL;
        }
        return isLoggable(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        @Nullable String[] message = checkContains(msg == null ? null : msg.toString());
        if(message != null) {
            logger.atLevel(getLevel(message[0])).withMarker(marker).withThrowable(t).log(message[1]);
            return Result.NEUTRAL;
        }
        return msg == null ? Result.NEUTRAL : isLoggable(msg.toString());
    }

    private @Nullable String[] checkContains(@Nullable String msg) {
        if(msg == null) return null;
        if(!msg.contains(CommonManager.THREE_SEPARATION_CHARACTERS)) return null;
        return new String[]{msg.split(CommonManager.THREE_SEPARATION_CHARACTERS)[0], msg.split(CommonManager.THREE_SEPARATION_CHARACTERS)[1]};
    }

    private java.util.logging.Level toJavaLoggerLevel(String level) {
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
        if(msg == null) return Result.NEUTRAL;
        if(!msg.contains("Reflections took") && !msg.contains(CommonManager.THREE_SEPARATION_CHARACTERS)) return Result.NEUTRAL;
        return Result.DENY;
    }
}