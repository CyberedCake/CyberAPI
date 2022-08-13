package net.cybercake.cyberapi.bungee.server.serverlist.motd;

/**
 * Represents a text color and decoration formatter
 * @see MOTDTextFormatter#PLAIN
 * @see MOTDTextFormatter#LEGACY
 * @see MOTDTextFormatter#MINIMESSAGE
 */
public enum MOTDTextFormatter {

    /**
     * Represents using plain-text, with no text formatter
     */
    PLAIN,

    /**
     * Represents using legacy color codes as the text formatter
     * @see net.cybercake.cyberapi.bungee.chat.UChat#chat(String) UChat -> Bungee ChatColor
     * @see net.cybercake.cyberapi.bungee.chat.UChat#bComponent(String) UChat -> Base Components
     * @see net.cybercake.cyberapi.bungee.chat.UChat#component(String) UChat -> Adventure Component
     */
    LEGACY,

    /**
     * Represents using MiniMessage as the text formatter
     * @apiNote requires MiniMessage support
     * @see net.cybercake.cyberapi.bungee.chat.UChat#miniMessage(String) UChat -> MiniMessage Components
     */
    MINIMESSAGE

}
