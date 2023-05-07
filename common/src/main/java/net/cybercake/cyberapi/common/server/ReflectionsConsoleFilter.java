package net.cybercake.cyberapi.common.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class ReflectionsConsoleFilter extends AbstractFilter implements Filter {

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
        return isLoggable(msg);
    }

    @Override
    public Result filter(LogEvent event) {
        return event == null ? Result.NEUTRAL : isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return isLoggable(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return isLoggable(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : isLoggable(msg.toString());
    }

    // my hard-coded way to remove Reflection's messages
    // create an issue on GitHub (https://github.com/CyberedCake/CyberAPI-testing/issues) if you have a better way!
    private Result isLoggable(String msg) {
        if(msg == null) return Result.NEUTRAL;
        if(!msg.contains("Reflections took")) return Result.NEUTRAL;
        return Result.DENY;
    }
}