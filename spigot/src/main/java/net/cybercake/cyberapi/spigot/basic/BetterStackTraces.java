package net.cybercake.cyberapi.spigot.basic;

import net.cybercake.cyberapi.spigot.chat.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BetterStackTraces {

    /**
     * Gets a list of the {@link String}s
     * @param exception the {@link Throwable} to get the stacktrace from
     * @return the stacktrace in a {@link List} of {@link String}s form
     * @since 15
     */
    public static List<String> get(Throwable exception) {
        List<String> exceptionList = new ArrayList<>();

        exceptionList.add(exception.toString());
        StackTraceElement[] trace = exception.getStackTrace();
        for (StackTraceElement traceElement : trace)
            exceptionList.add("\tat " + traceElement);

        Throwable cause = exception.getCause();
        if(cause != null) {
            List<String> causedBy = get(cause);
            exceptionList.add("Caused by: " + causedBy.get(0));
            causedBy.remove(0);
            exceptionList.addAll(causedBy);
        }

        return exceptionList;
    }

    /**
     * Prints the exception to the console with a specific log level
     * @param level the level to log the exception
     * @param exception the exception to print
     * @since 15
     */
    public static void print(Level level, Throwable exception) {
        for(String element : get(exception)) {
            Log.log(level, element, Thread.currentThread().getStackTrace()[2]);
        }
    }

    /**
     * Prints the exception to the console as {@link java.util.logging.Logger#severe(String)}
     * @param exception the exception to print
     * @since 15
     */
    public static void print(Throwable exception) {
        print(Level.SEVERE, exception);
    }

}
