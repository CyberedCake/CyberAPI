package net.cybercake.cyberapi.common.basic.logs;

import net.cybercake.cyberapi.common.basic.Time;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents ease-of-access creating log files for your plugin
 * @since 43
 */
public class Logs {

    /**
     * Creates a new configuration instance
     * @return the new {@link Configuration} instance
     * @since 43
     */
    public static Configuration newConfiguration() {
        return new Configuration();
    }

    /**
     * Represents the configuration for {@link Logs}
     * @since 43
     */
    public static class Configuration {
        private String dateFormat = "HH:mm:ss";
        private int dateOffset = 0;

        private Configuration() {}

        /**
         * Sets the date time format, using {@link java.text.SimpleDateFormat} formats
         * @param pattern the pattern to set the format to
         * @since 43
         */
        public Configuration setDateTimeFormat(String pattern) { this.dateFormat = pattern; return this; }

        /**
         * Sets the date time offset, imagine a GMT[+|-] in front of the integer, see {@link Time#getFormattedDate(String, int)}
         * @param offset the offset to set the date with
         * @since 43
         * @see Time#getFormattedDate(String, int)
         */
        public Configuration setDateTimeOffset(int offset) { this.dateOffset = offset; return this; }
    }

    public enum LogLevel {
        INFO, WARN, ERROR, DEBUG;
    }

    private static final List<Logs> logs = new ArrayList<>();

    /**
     * Get an already existing {@link Logs} instance
     * @param file the {@link File} that matches a previously-created instance
     * @return the {@link Logs} instance
     * @since 43
     */
    public static Logs getFromFile(File file) {
        for(Logs log : logs) {
            if(log.getFile().equals(file)) return log;
        }
        return null;
    }

    /**
     * Get an already existing {@link Logs} instance
     * @param id the {@link String} ID that matches a previously-created instance
     * @return the {@link Logs} instance
     * @since 43
     */
    public static Logs getFromID(String id) {
        for(Logs log : logs) {
            if(log.getID().equalsIgnoreCase(id)) return log;
        }
        return null;
    }

    private final String id;
    private final File file;
    private Configuration configuration;
    private List<String> cachedLogs = new ArrayList<>();
    private boolean requireRecache = true;

    @Deprecated
    private Logs() { this.id = null; this.file = null; }

    /**
     * Creates a new {@link Logs} instance using a {@link File} to represent where the logs will be stored
     * @param file the {@link File} where the logs will be stored
     * @since 43
     * @deprecated it's recommended you use {@code CyberAPI.getInstance().createOrGetLogs(String)} instead!
     */
    @Deprecated
    @SuppressWarnings({"all"})
    public Logs(String id, File file) {
        if(getFromID(id) != null || getFromFile(file) != null)
            throw new IllegalArgumentException("A " + Logs.class.getCanonicalName() + " instance with that ID/" + File.class.getCanonicalName() + " already exists! (id=" + id + ", file=" + file.getAbsolutePath() + ")");
        this.id = id;
        this.file = file;
        this.configuration = new Configuration();
    }

    /**
     * Sets the configuration to a certain configuration instance
     * @param newConfiguration the new configuration instance
     * @since 43
     */
    public void setConfiguration(Configuration newConfiguration) {
        this.configuration = newConfiguration;
    }

    /**
     * Writes certain information to the log file
     * @param logLevel the {@link LogLevel} to log
     * @param logged the {@link String} content to log
     * @param extras <b>(optional)</b> the extra information to append to the end, usually variables
     * @since 43
     */
    public void write(LogLevel logLevel, String logged, String... extras) {
        try {
            if(file.exists()) file.createNewFile();
            this.requireRecache = true;

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write("[" + Time.getFormattedDate(configuration.dateFormat, configuration.dateOffset) + " // " + logLevel + "] " + logged + (extras.length < 1 ? "" : " (" + String.join(", ", extras) + ")") + "\n");

            writer.close();
            fileWriter.close();
        } catch (Exception exception) {
            throw new LoggingException("Failed to write log: " + logLevel.name() + " " + logged + " (with extras: " + String.join(", ", extras) + ")", exception);
        }
    }

    /**
     * Writes certain information to the log file
     * @param logged the {@link String} content to log
     * @param extras <b>(optional)</b> the extra information to append to the end, usually variables
     * @since 43
     */
    public void write(String logged, String... extras) { write(LogLevel.INFO, logged, extras); }

    /**
     * Writes certain information to the log file
     * @param logged the {@link String} content to log
     * @since 43
     */
    public void write(String logged) { write(LogLevel.INFO, logged); }

    /**
     * Requires the cached logs to re-cache
     * @see Logs#getLogs()
     * @since 43
     */
    public void recacheReadLogs() { this.requireRecache = true; }

    /**
     * Gets the most recent log in the log {@link File}
     * @return the most recent {@link String} log
     * @since 43
     */
    public @Nullable String getMostRecentLog() {
        return (getLogs() == null ? null : getLogs().get(getLogs().size()-1));
    }

    /**
     * Gets all the logs from the log {@link File}
     * @return all the logs in a {@link List} of {@link String}s
     * @since 43
     */
    public @Nullable List<String> getLogs() {
        try {
            if(this.requireRecache)
                this.cachedLogs = Files.readAllLines(file.toPath());
            return cachedLogs;
        } catch (Exception exception) {
            throw new LoggingException("Failed reading the logs for " + getID() + " (file=" + getFile().getAbsolutePath() + ")", exception);
        }
    }

    /**
     * Gets the log {@link String} ID
     * @return the log id
     * @since 43
     */
    public String getID() { return id; }

    /**
     * Gets the log {@link File}
     * @return the log {@link File}
     * @since 43
     */
    public File getFile() { return file; }

    /**
     * Gets the set date format from the set {@link Configuration}
     * @return the date format
     * @since 43
     */
    public String getDateFormat() { return configuration.dateFormat; }

    /**
     * Gets the set date offset from the set {@link Configuration}
     * @return the date offset
     * @since 43
     */
    public int getDateOffset() { return configuration.dateOffset; }

}
