package net.cybercake.cyberapi.spigot.server.serverlist.protocol;

/**
 * Represents the version name's visibility
 */
public enum VersionVisibility {

    /**
     * Represents always showing the version in the server list
     */
    VISIBLE,

    /**
     * Represents never showing the version in the version list
     */
    HIDDEN,

    /**
     * Represents showing the version if outdated (a.k.a. default)
     */
    IF_OUTDATED
}
