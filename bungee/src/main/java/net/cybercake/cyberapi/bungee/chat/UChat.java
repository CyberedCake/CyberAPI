package net.cybercake.cyberapi.bungee.chat;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.bungee.player.CyberPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UChat {

    /**
     * A short-form way of doing {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param message the message containing the alternate color code
     * @return the {@link String} containing the formatted message
     * @since 3.3
     */
    public static String chat(Character character, String message) {
        return ChatColor.translateAlternateColorCodes(character, message);
    }

    /**
     * A short-form way of doing {@link UChat#chat(Character, String)}. This method automatically assumes the alternate color code is '{@literal &}'
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link String} containing the formatted message
     * @since 3.3
     */
    public static String chat(String message) {
        return chat('&', message);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param messages the messages containing the alternate color code
     * @return the {@link List} of {@link String}s that are formatted
     * @since 3.3
     */
    public static List<String> listChat(Character character, String... messages) {
        List<String> returned = new ArrayList<>();
        for(String message : messages) { returned.add(chat(character, message)); }
        return returned;
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param messages the messages containing the alternate color codes of '{@literal &}'
     * @return the {@link List} of {@link String}s that are formatted
     * @since 3.3
     */
    public static List<String> listChat(String... messages) {
        return listChat('&', messages);
    }

    /**
     * A short-form way of creating a new {@link net.md_5.bungee.api.chat.BaseComponent} with {@link ChatColor#translateAlternateColorCodes(char, String)}.
     * The name simply means '<b>b</b>ase<b>Component</b>'
     * @param character the alternate color code to replace
     * @param message the message containing the alternate color code
     * @return the {@link net.md_5.bungee.api.chat.BaseComponent} containing the formatted message
     * @since 3.3
     */
    public static BaseComponent bComponent(Character character, String message) { return new TextComponent(chat(character, message)); }

    /**
     * A short-form way of creating a new {@link net.md_5.bungee.api.chat.BaseComponent} with {@link ChatColor#translateAlternateColorCodes(char, String)}.
     * This method automatically assumes the alternate color code is '{@literal &}'.
     * The name simply means '<b>b</b>ase<b>Component</b>'
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link net.md_5.bungee.api.chat.BaseComponent} containing the formatted message
     * @since 3.3
     */
    public static BaseComponent bComponent(String message) { return bComponent('&', message); }

    /**
     * A short-form way of creating a list from an array with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param messages the messages containing the alternate color code
     * @return the {@link List} of {@link BaseComponent}s that are formatted
     * @since 3.3
     */
    public static List<BaseComponent> listBComponent(Character character, String... messages) {
        List<BaseComponent> returned = new ArrayList<>();
        for(String message : messages) { returned.add(UChat.bComponent(character, message)); }
        return returned;
    }

    /**
     * A short-form way of creating a list from an array with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param messages the messages containing the alternate color code of '{@literal &}'
     * @return the {@link List} of {@link BaseComponent}s that are formatted
     * @since 3.3
     */
    public static List<BaseComponent> listBComponent(String... messages) {
        return listBComponent('&', messages);
    }

    /**
     * Converts {@link BaseComponent} to legacy bukkit color codes
     * @param component the component to convert to legacy string
     * @return the formatted message
     * @since 3.3
     */
    public static String toLegacy(BaseComponent component) {
        return UChat.chat(ComponentSerializer.toString(component));
    }

    /**
     * Converts {@link String} legacy bukkit color codes to a {@link BaseComponent}
     * @param string the string to convert to adventure component
     * @return the component message
     * @since 3.3
     */
    public static BaseComponent fromLegacy(String string) {
        return bComponent(string);
    }

    /**
     * Gets a separator with 80 characters, specify the amount of characters by using the {@link UChat#getSeparator(ChatColor, int)} method
     * @param color the {@link ChatColor} to make the separator
     * @return the separator
     * @since 3.3
     */
    public static String getSeparator(ChatColor color) { return getSeparator(color, 80); }

    /**
     * Gets a separator
     * @param color the {@link ChatColor} to make the separator
     * @param characters the amount of characters of "-" to put in
     * @return the separator
     * @throws IllegalArgumentException when the character amount is less than 1 or greater than 999
     * @since 3.3
     */
    public static String getSeparator(ChatColor color, int characters) {
        if(characters <= 0) throw new IllegalArgumentException("You cannot have 0 or less characters in a separator!");
        else if(characters >= 1000) throw new IllegalArgumentException("You cannot have 1,000 or more characters in a separator!");
        return (String.valueOf(color) + ChatColor.STRIKETHROUGH + " ").repeat(characters);
    }

    /**
     * Gets a {@link String} that can be used for chat clearing, as it's just 999 empty lines
     * @return 999 empty lined String
     * @since 3.3
     */
    public static String getClearedChat() {
        return getClearedChat(999);
    }

    /**
     * Gets a {@link String} that can be used for chat clearing, as it's just {@code amount} of empty lines
     * @param amount the amount of empty lines to return
     * @return {@code amount} of empty lines
     * @since 3.3
     */
    public static String getClearedChat(int amount) {
        return " \n".repeat(amount);
    }

    /**
     * Clears a chat for a specified player by printing a bunch of {@code \n}
     * @param player the player to clear the chat of
     * @since 3.3
     */
    public static void printClearChat(ProxiedPlayer player) {
        player.sendMessage(getClearedChat());
    }

    /**
     * Clears a chat for a specified {@link CyberPlayer} by printing a bunch of {@code \n}
     * @param cyberPlayer the {@link CyberPlayer} to clear the chat of
     * @throws IllegalStateException if the {@link CyberPlayer} is offline or {@link CyberPlayer#getOnlineActions()} returns null
     * @since 3.3
     */
    public static void printClearChat(CyberPlayer cyberPlayer) {
        if(cyberPlayer.getOnlineActions() == null) throw new IllegalStateException("Cannot send a cleared chat to an offline player!");
        cyberPlayer.getOnlineActions().sendColored(getClearedChat());
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param permission the permission required to see the message
     * @since 3.3
     */
    public static void broadcast(String message, @Nullable String permission) {
        for(ProxiedPlayer player : CyberAPI.getInstance().getOnlinePlayers()) {
            if(!((permission == null || permission.strip().equalsIgnoreCase("")) || player.hasPermission(permission))) continue;

            player.sendMessage(bComponent(message));
        }
        Log.info(message);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send
     * @see UChat#broadcast(String, String)
     * @since 3.3
     */
    public static void broadcast(String message) {
        broadcast(message, null);
    }

}
