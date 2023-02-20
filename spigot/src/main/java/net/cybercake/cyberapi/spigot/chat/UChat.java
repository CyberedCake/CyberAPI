package net.cybercake.cyberapi.spigot.chat;

import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.Validators;
import net.cybercake.cyberapi.spigot.player.CyberPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UChat {

    /**
     * A short-form way of doing {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param message the message containing the alternate color code
     * @return the {@link String} containing the formatted message
     * @since 1
     */
    public static String chat(Character character, String message) {
        return ChatColor.translateAlternateColorCodes(character, message);
    }

    /**
     * A short-form way of doing {@link UChat#chat(Character, String)}. This method automatically assumes the alternate color code is '{@literal &}'
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link String} containing the formatted message
     * @since 1
     */
    public static String chat(String message) {
        return chat('&', message);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param messages the messages containing the alternate color code
     * @return the {@link List} of {@link String}s that are formatted
     * @since 3
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
     * @since 3
     */
    public static List<String> listChat(String... messages) {
        return listChat('&', messages);
    }

    /**
     * Get a {@link net.md_5.bungee.api.ChatColor} from an RGB value
     * @param rgb the RGB value
     * @return the {@link net.md_5.bungee.api.ChatColor}
     * @since 42
     */
    public static net.md_5.bungee.api.ChatColor getColorFromRGB(int rgb) {
        return net.md_5.bungee.api.ChatColor.of(new java.awt.Color(rgb));
    }

    /**
     * Gets a {@link net.md_5.bungee.api.ChatColor} from a separated red, green, blue value
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     * @return the {@link net.md_5.bungee.api.ChatColor}
     * @since 42
     */
    public static net.md_5.bungee.api.ChatColor getColorFromRGB(int red, int green, int blue) {
        return net.md_5.bungee.api.ChatColor.of(new java.awt.Color(red, green, blue));
    }

    /**
     * Gets a {@link net.md_5.bungee.api.ChatColor} from a separated red, green, blue values as floats
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     * @return the {@link net.md_5.bungee.api.ChatColor}
     * @since 42
     */
    public static net.md_5.bungee.api.ChatColor getColorFromRGB(float red, float green, float blue) {
        return net.md_5.bungee.api.ChatColor.of(new java.awt.Color(red, green, blue));
    }

    /**
     * Gets a {@link net.md_5.bungee.api.ChatColor} from a hex value
     * @param hex the hex value
     * @return the {@link net.md_5.bungee.api.ChatColor}
     * @since 42
     */
    public net.md_5.bungee.api.ChatColor getColorFromHex(String hex) {
        return net.md_5.bungee.api.ChatColor.of(hex);
    }

    /**
     * A short-form way of creating a new {@link net.md_5.bungee.api.chat.BaseComponent} with {@link ChatColor#translateAlternateColorCodes(char, String)}.
     * The name simply means '<b>b</b>ase<b>Component</b>'
     * @param character the alternate color code to replace
     * @param message the message containing the alternate color code
     * @return the {@link net.md_5.bungee.api.chat.BaseComponent} containing the formatted message
     * @since 1
     */
    public static BaseComponent bComponent(Character character, String message) { return new TextComponent(chat(character, message)); }

    /**
     * A short-form way of creating a new {@link net.md_5.bungee.api.chat.BaseComponent} with {@link ChatColor#translateAlternateColorCodes(char, String)}.
     * This method automatically assumes the alternate color code is '{@literal &}'.
     * The name simply means '<b>b</b>ase<b>Component</b>'
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link net.md_5.bungee.api.chat.BaseComponent} containing the formatted message
     * @since 1
     */
    public static BaseComponent bComponent(String message) { return bComponent('&', message); }

    /**
     * A short-form way of creating a list from an array with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param messages the messages containing the alternate color code
     * @return the {@link List} of {@link BaseComponent}s that are formatted
     * @since 3
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
     * @since 3
     */
    public static List<BaseComponent> listBComponent(String... messages) {
        return listBComponent('&', messages);
    }

    /**
     * Returns a {@link Component#text(String)} but with an alternate color code and message
     * @param character the alternate color code to replace
     * @param message the message containing the alternate color code
     * @return the {@link Component} containing the formatted message
     * @apiNote requires Adventure API support
     * @since 1
     */
    public static Component component(Character character, String message) {
        Validators.validateAdventureSupport();
        return LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().character('&').build().deserialize(chat(character, message));
    }

    /**
     * Returns a {@link Component#text(String)} but with an assumed alternate color code of '{@literal &}' and a message
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link Component} containing the formatted message
     * @apiNote requires Adventure API support
     * @since 1
     */
    public static Component component(String message) {
        Validators.validateAdventureSupport();
        return component('&', message);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param messages the messages containing the alternate color code
     * @return the {@link List} of {@link Component}s that are formatted
     * @since 3
     * @apiNote requires Adventure API support
     */
    public static List<Component> listComponent(Character character, String... messages) {
        Validators.validateAdventureSupport();
        List<Component> returned = new ArrayList<>();
        for(String message : messages) returned.add(UChat.component(character, message));
        return returned;
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param messages the messages containing the alternate color code of '{@literal &}'
     * @return the {@link List} of {@link Component}s that are formatted
     * @since 3
     * @apiNote requires Adventure API support
     */
    public static List<Component> listComponent(String... messages) {
        return listComponent('&', messages);
    }

    /**
     * Returns a {@link MiniMessage} message but formatted with {@link MiniMessage#deserialize(Object)}
     * @param message the message containing MiniMessage formats
     * @return the {@link Component} containing the formatted message
     * @apiNote requires MiniMessage support
     * @since 1
     */
    public static Component miniMessage(String message) {
        Validators.validateMiniMessageSupport();
        return miniMessage(false, message);
    }

    /**
     * Returns a {@link MiniMessage} message but formatted with {@link MiniMessage#deserialize(Object)}
     * @param strict whether the message is held to be strict, default: false
     * @param message the message containing MiniMessage formats
     * @see <a href="https://docs.adventure.kyori.net/minimessage/api.html#error-handling">MiniMessage's Builder Documentation</a>
     * @return the {@link Component} containing the formatted message
     * @apiNote requires MiniMessage support
     * @since 1
     */
    public static Component miniMessage(boolean strict, String message) {
        Validators.validateMiniMessageSupport();
        return MiniMessage.builder().strict(strict).build().deserialize(message);
    }

    /**
     * Returns a {@link MiniMessage} message but formatted with {@link MiniMessage#deserialize(Object)}
     * @param builder the built {@link MiniMessage} instance
     * @param message the message containing MiniMessage formats
     * @see <a href="https://docs.adventure.kyori.net/minimessage/api.html#error-handling">MiniMessage's Builder Documentation</a>
     * @return the {@link Component} containing the formatted message
     * @apiNote requires MiniMessage support
     * @since 1
     */
    public static Component miniMessage(MiniMessage builder, String message) {
        Validators.validateMiniMessageSupport();
        return builder.deserialize(message);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link MiniMessage}
     * @param messages the messages containing MiniMessage format
     * @return the {@link List} of {@link Component}s in MiniMessage format
     * @since 3
     * @apiNote requires MiniMessage support
     */
    public static List<Component> listMiniMessage(String... messages) {
        Validators.validateMiniMessageSupport();
        return listMiniMessage(MiniMessage.builder().build(), messages);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link MiniMessage}
     * @param strict whether the message is held to be strict, default: false
     * @param messages the messages containing MiniMessage format
     * @return the {@link List} of {@link Component}s in MiniMessage format
     * @see <a href="https://docs.adventure.kyori.net/minimessage/api.html#error-handling">MiniMessage's Builder Documentation</a>
     * @apiNote requires MiniMessage support
     * @since 3
     */
    public static List<Component> listMiniMessage(boolean strict, String... messages) {
        Validators.validateMiniMessageSupport();
        return listMiniMessage(MiniMessage.builder().strict(strict).build(), messages);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link MiniMessage}
     * @param builder the built {@link MiniMessage} instance
     * @param messages the messages containing MiniMessage format
     * @return the {@link List} of {@link Component}s in MiniMessage format
     * @see <a href="https://docs.adventure.kyori.net/minimessage/api.html#error-handling">MiniMessage's Builder Documentation</a>
     * @since 3
     * @apiNote requires MiniMessage support
     */
    public static List<Component> listMiniMessage(MiniMessage builder, String... messages) {
        Validators.validateMiniMessageSupport();
        List<Component> returned = new ArrayList<>();
        for(String message : messages) returned.add(miniMessage(builder, message));
        return returned;
    }

    /**
     * Converts {@link Component} to legacy bukkit color codes {@literal <}-- NOT RECOMMENDED FOR PAPER SERVERS
     * @param component the component to convert to legacy string
     * @return the formatted message
     * @apiNote requires Adventure API support
     * @since 1
     */
    public static String toLegacy(Component component) {
        Validators.validateAdventureSupport();
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    /**
     * Converts {@link String} legacy bukkit color codes to a {@link Component}
     * @param string the string to convert to adventure component
     * @return the component message
     * @apiNote requires Adventure API support
     * @since 1
     */
    public static Component fromLegacy(String string) {
        Validators.validateAdventureSupport();
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
    }

    /**
     * Converts {@link BaseComponent} to legacy bukkit color codes
     * @param component the component to convert to legacy string
     * @return the formatted message
     * @since 15
     */
    public static String toLegacyBungee(BaseComponent component) {
        return UChat.chat(ComponentSerializer.toString(component));
    }

    /**
     * Converts {@link String} legacy bukkit color codes to a {@link BaseComponent}
     * @param string the string to convert to adventure component
     * @return the component message
     * @since 15
     */
    public static BaseComponent fromLegacyBungee(String string) {
        Validators.validateAdventureSupport();
        return bComponent(string);
    }

    /**
     * Gets a separator with 80 characters, specify the amount of characters by using the {@link UChat#getSeparator(ChatColor, int)} method
     * @param color the {@link ChatColor} to make the separator
     * @return the separator
     * @since 1
     */
    public static String getSeparator(ChatColor color) { return getSeparator(color, 80); }

    /**
     * Gets a separator
     * @param color the {@link ChatColor} to make the separator
     * @param characters the amount of characters of "-" to put in
     * @return the separator
     * @throws IllegalArgumentException when the character amount is less than 1 or greater than 999
     * @since 1
     */
    public static String getSeparator(ChatColor color, int characters) {
        if(characters <= 0) throw new IllegalArgumentException("You cannot have 0 or less characters in a separator!");
        else if(characters >= 1000) throw new IllegalArgumentException("You cannot have 1,000 or more characters in a separator!");
        return (String.valueOf(color) + ChatColor.STRIKETHROUGH + " ").repeat(characters);
    }

    /**
     * Gets a {@link String} that can be used for chat clearing, as it's just 999 empty lines
     * @return 999 empty lined String
     * @since 1
     */
    public static String getClearedChat() {
        return getClearedChat(999);
    }

    /**
     * Gets a {@link String} that can be used for chat clearing, as it's just {@code amount} of empty lines
     * @param amount the amount of empty lines to return
     * @return {@code amount} of empty lines
     * @since 1
     */
    public static String getClearedChat(int amount) {
        return " \n".repeat(amount);
    }

    /**
     * Clears a chat for a specified player by printing a bunch of {@code \n}
     * @param player the player to clear the chat of
     * @since 1
     */
    public static void printClearChat(Player player) {
        player.sendMessage(getClearedChat());
    }

    /**
     * Clears a chat for a specified {@link CyberPlayer} by printing a bunch of {@code \n}
     * @param cyberPlayer the {@link CyberPlayer} to clear the chat of
     * @throws IllegalStateException if the {@link CyberPlayer} is offline or {@link CyberPlayer#getOnlineActions()} returns null
     * @since 1
     */
    public static void printClearChat(CyberPlayer cyberPlayer) {
        if(cyberPlayer.getOnlineActions() == null) throw new IllegalStateException("Cannot send a cleared chat to an offline player!");
        cyberPlayer.getOnlineActions().sendColored(getClearedChat());
    }

    /**
     * Paginate strings into different lines based on length. This means that strings will be put on a separate item in a
     * list if the line length does not match.
     * @param string the string you want to input, will be split up accordingly
     * @param length the length of before the string should paginate itself
     * @return a list in which every item is a string
     * @see ChatPaginator
     * @since 1
     */
    public static List<String> paginate(String string, int length) {
        List<String> pagination = new ArrayList<>();
        for(String str : ChatPaginator.wordWrap(string, length)) {
            pagination.add(ChatColor.getLastColors(pagination.size() > 1 ? pagination.get(pagination.size()-1) : "") + chat(str));
        }
        return pagination;
    }

    /**
     * Paginate strings into different lines based on length. This means that strings will be put on a separate item in a
     * list if the line length does not match. You can set the line length by doing the same method but adding a number to
     * the end.
     * @param string the string you want to input, will be split up accordingly
     * @return a list in which every item is a string
     * @see ChatPaginator
     * @since 1
     */
    public static List<String> paginate(String string) {
        return paginate(string, 30);
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param permission the permission required to see the message
     * @since 1
     */
    public static void broadcast(String message, @Nullable String permission) {
        for(Player player : CyberAPI.getInstance().getOnlinePlayers()) {
            if(!((permission == null || permission.strip().equalsIgnoreCase("")) || player.hasPermission(permission))) continue;

            player.sendMessage(chat(message));
        }
        Log.info(message);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send
     * @see UChat#broadcast(String, String)
     * @since 1
     */
    public static void broadcast(String message) {
        broadcast(message, null);
    }

}
