package net.cybercake.cyberapi.spigot.server.serverlist.motd;

import java.io.File;

/**
 * Represents the MOTD icon type
 * @since 10
 */
public enum MOTDIconType {
    /**
     * Represents the {@link File} MOTD icon type
     */
    FILE(File.class),

    /**
     * Represents the {@link java.net.URL} MOTD icon type
     */
    URL(java.net.URL.class),

    /**
     * Represents if the MOTD icon type is unknown
     */
    UNSET(null);

    private final Class<?> type;
    MOTDIconType(Class<?> type) {
        this.type = type;
    }
    public Class<?> getType() { return type; }
}