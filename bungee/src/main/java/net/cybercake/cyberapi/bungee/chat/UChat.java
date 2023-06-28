package net.cybercake.cyberapi.bungee.chat;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.bungee.Validators;
import net.cybercake.cyberapi.bungee.player.CyberPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class UChat {

    /**
     * Formats an input with a certain syntax type, these are how methods in UChat like
     * {@link UChat#chat(String)}, {@link UChat#component(String)}, {@link UChat#miniMessage(String)},
     * and {@link UChat#combined(String)} are formatted.
     * @param formatType the format type to use for the input and how it will be parsed
     * @param input the input to be parsed, this could be a {@link String} but could be anything else
     * @return the formatted output, usually the only two options are {@link String a String} or {@link Component a Component} (which requires Adventure API)
     * @apiNote sometimes requires Adventure API if using any {@link ChatFormatType} requiring it
     * @since 139
     */
    public static <T, R> R format(ChatFormatType<T, R> formatType, T input) {
        if(formatType.getReturnType().getCanonicalName().toLowerCase(Locale.ROOT).contains("net.kyori.adventure"))
            Validators.validateAdventureSupport();
        return formatType.execute(input);
    }

    /**
     * A short-form way of doing {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param message the message containing the alternate color code
     * @return the {@link String} containing the formatted message
     * @since 15
     */
    public static String chat(Character character, String message) {
        return ((ChatFormatType.LegacyFormatType<String, String>) ChatFormatType.LEGACY).execute(message, character);
    }

    /**
     * A short-form way of doing {@link UChat#chat(Character, String)}. This method automatically assumes the alternate color code is '{@literal &}'
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link String} containing the formatted message
     * @since 15
     */
    public static String chat(String message) {
        return chat('&', message);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param messages the messages containing the alternate color code
     * @return the {@link List} of {@link String}s that are formatted
     * @since 15
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
     * @since 15
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
     * @since 15
     */
    public static BaseComponent bComponent(Character character, String message) {
        return (((ChatFormatType.LegacyFormatType<String, BaseComponent>)ChatFormatType.BUNGEE_COMPONENT).execute(message, character));
    }

    /**
     * A short-form way of creating a new {@link net.md_5.bungee.api.chat.BaseComponent} with {@link ChatColor#translateAlternateColorCodes(char, String)}.
     * This method automatically assumes the alternate color code is '{@literal &}'.
     * The name simply means '<b>b</b>ase<b>Component</b>'
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link net.md_5.bungee.api.chat.BaseComponent} containing the formatted message
     * @since 15
     */
    public static BaseComponent bComponent(String message) { return bComponent('&', message); }

    /**
     * A short-form way of creating a list from an array with {@link ChatColor#translateAlternateColorCodes(char, String)}
     * @param character the alternate color code to replace
     * @param messages the messages containing the alternate color code
     * @return the {@link List} of {@link BaseComponent}s that are formatted
     * @since 15
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
     * @since 15
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
     * @since 73
     */
    public static Component component(Character character, String message) {
        Validators.validateAdventureSupport();
        return (((ChatFormatType.LegacyFormatType<String, Component>)ChatFormatType.COMPONENT).execute(message, character));
    }

    /**
     * Returns a {@link Component#text(String)} but with an assumed alternate color code of '{@literal &}' and a message
     * @param message the message containing the alternate color code of '{@literal &}'
     * @return the {@link Component} containing the formatted message
     * @apiNote requires Adventure API support
     * @since 73
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
     * @since 73
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
     * @since 73
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
     * @since 73
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
     * @since 73
     */
    public static Component miniMessage(boolean strict, String message) {
        Validators.validateMiniMessageSupport();
        return miniMessage(MiniMessage.builder().strict(strict).build(), message);
    }

    /**
     * Returns a {@link MiniMessage} message but formatted with {@link MiniMessage#deserialize(Object)}
     * @param builder the built {@link MiniMessage} instance
     * @param message the message containing MiniMessage formats
     * @see <a href="https://docs.adventure.kyori.net/minimessage/api.html#error-handling">MiniMessage's Builder Documentation</a>
     * @return the {@link Component} containing the formatted message
     * @apiNote requires MiniMessage support
     * @since 73
     */
    public static Component miniMessage(MiniMessage builder, String message) {
        Validators.validateMiniMessageSupport();
        return ((ChatFormatType.MiniMessageFormatType)ChatFormatType.MINI_MESSAGE).execute(message, builder);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link MiniMessage}
     * @param messages the messages containing MiniMessage format
     * @return the {@link List} of {@link Component}s in MiniMessage format
     * @since 73
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
     * @since 73
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
     * @since 73
     * @apiNote requires MiniMessage support
     */
    public static List<Component> listMiniMessage(MiniMessage builder, String... messages) {
        Validators.validateMiniMessageSupport();
        List<Component> returned = new ArrayList<>();
        for(String message : messages) returned.add(miniMessage(builder, message));
        return returned;
    }

    /**
     * Returns a {@link Component} from a {@link String}, using both Legacy, using an alternate color code, and MiniMessage systems, using the more modern (and preferred) way of chat parsing.
     * <br> <br>
     * This means you can type "{@code &c}" and "{@code <red>}", both of which will parse correctly.
     * @param miniMessage the Mini Message builder to use
     * @param message the message containing the alternate color code and/or MiniMessage syntax
     * @return the {@link Component} containing the formatted message
     * @apiNote requires Adventure API support
     * @since 139
     */
    public static Component combined(MiniMessage miniMessage, String message) {
        Validators.validateAdventureSupport();
        return (((ChatFormatType.MiniMessageFormatType)ChatFormatType.MINI_MESSAGE).execute(message, miniMessage));
    }

    /**
     * Returns a {@link Component} from a {@link String}, using both Legacy, using an alternate color code (assumed to be "{@literal &}" in this case), and MiniMessage systems, using the more modern (and preferred) way of chat parsing.
     * <br> <br>
     * This means you can type "{@code &c}" and "{@code <red>}", both of which will parse correctly.
     * @param message the message containing the alternate color code of '{@literal &}' and/or MiniMessage syntax
     * @return the {@link Component} containing the formatted message
     * @apiNote requires Adventure API support
     * @since 139
     */
    public static Component combined(String message) {
        Validators.validateAdventureSupport();
        return combined(MiniMessage.miniMessage(), message);
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link UChat#combined(MiniMessage, String)}
     * @param miniMessage the Mini Message builder to use
     * @param messages the messages containing the alternate color code and/or MiniMessage syntax
     * @return the {@link List} of {@link Component}s that are formatted
     * @since 139
     * @apiNote requires Adventure API support
     */
    public static List<Component> listCombined(MiniMessage miniMessage, String... messages) {
        Validators.validateAdventureSupport();
        List<Component> returned = new ArrayList<>();
        for(String message : messages) returned.add(UChat.combined(miniMessage, message));
        return returned;
    }

    /**
     * A short-form way of creating a list from an array that are all formatted with {@link UChat#combined(String)}
     * @param messages the messages containing the alternate color code of '{@literal &}' and/or MiniMessage syntax
     * @return the {@link List} of {@link Component}s that are formatted
     * @since 139
     * @apiNote requires Adventure API support
     */
    public static List<Component> listCombined(String... messages) {
        return listCombined(MiniMessage.miniMessage(), messages);
    }

    /**
     * Converts {@link BaseComponent} to legacy bukkit color codes
     * @param component the bungee component to convert to legacy string
     * @return the formatted message
     * @since 15
     */
    public static String toLegacy(BaseComponent component) {
        return UChat.chat(ComponentSerializer.toString(component));
    }

    /**
     * Converts {@link String} legacy bukkit color codes to a {@link BaseComponent}
     * @param string the string to convert to bungee component
     * @return the bungee component message
     * @since 15
     */
    public static BaseComponent fromLegacy(String string) {
        return bComponent(string);
    }

    /**
     * Converts {@link BaseComponent} to its JSON content ({@code {"text":"testing text","color":"red","bold":true}})
     * @param component the component to convert to JSON
     * @return the formatted message
     * @since 140
     */
    public static String toJson(BaseComponent component) {
        return ComponentSerializer.toString(component);
    }

    /**
     * Converts {@link String a JSON String} (i.e., {@code {"text":"testing text","color":"red","bold":true}}) into to a formatted {@link BaseComponent}
     * @param string the string to convert to adventure component
     * @return the component message
     * @since 140
     */
    public static BaseComponent fromJson(String string) {
        BaseComponent component = new TextComponent();
        Arrays.stream(ComponentSerializer.parse(string)).forEach(component::addExtra);
        return component;
    }

    /**
     * Gets a separator with 80 characters, specify the amount of characters by using the {@link UChat#getSeparator(ChatColor, int)} method
     * @param color the {@link ChatColor} to make the separator
     * @return the separator
     * @since 15
     */
    public static String getSeparator(ChatColor color) { return getSeparator(color, 80); }

    /**
     * Gets a separator
     * @param color the {@link ChatColor} to make the separator
     * @param characters the amount of characters of "-" to put in
     * @return the separator
     * @throws IllegalArgumentException when the character amount is less than 1 or greater than 999
     * @since 15
     */
    public static String getSeparator(ChatColor color, int characters) {
        if(characters <= 0) throw new IllegalArgumentException("You cannot have 0 or less characters in a separator!");
        else if(characters >= 1000) throw new IllegalArgumentException("You cannot have 1,000 or more characters in a separator!");
        return (String.valueOf(color) + ChatColor.STRIKETHROUGH + " ").repeat(characters);
    }

    /**
     * Gets a {@link String} that can be used for chat clearing, as it's just 999 empty lines
     * @return 999 empty lined String
     * @since 15
     */
    public static String getClearedChat() {
        return getClearedChat(999);
    }

    /**
     * Gets a {@link String} that can be used for chat clearing, as it's just {@code amount} of empty lines
     * @param amount the amount of empty lines to return
     * @return {@code amount} of empty lines
     * @since 15
     */
    public static String getClearedChat(int amount) {
        return " \n".repeat(amount);
    }

    /**
     * Clears a chat for a specified player by printing a bunch of {@code \n}
     * @param player the player to clear the chat of
     * @since 15
     */
    public static void printClearChat(ProxiedPlayer player) {
        player.sendMessage(getClearedChat());
    }

    /**
     * Clears a chat for a specified {@link CyberPlayer} by printing a bunch of {@code \n}
     * @param cyberPlayer the {@link CyberPlayer} to clear the chat of
     * @throws IllegalStateException if the {@link CyberPlayer} is offline or {@link CyberPlayer#getOnlineActions()} returns null
     * @since 15
     */
    public static void printClearChat(CyberPlayer cyberPlayer) {
        if(cyberPlayer.getOnlineActions() == null) throw new IllegalStateException("Cannot send a cleared chat to an offline player!");
        cyberPlayer.getOnlineActions().sendColored(getClearedChat());
    }





    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send
     * @since 1
     * @see UChat#broadcast(String, String)
     * @see UChat#broadcast(Component, Predicate)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(String message) { broadcast(message, (Predicate<? super CommandSender>) null); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param permission the permission required to see the message
     * @since 1
     * @see UChat#broadcast(String)
     * @see UChat#broadcast(String, Predicate)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(String message, @org.jetbrains.annotations.Nullable String permission) {
        broadcast(new TextComponent(message), permission);
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param filter the filter of command senders that can see this message, note: only {@link ProxiedPlayer players} and {@link CommandSender console} are checked!
     * @since 126
     * @see UChat#broadcast(String)
     * @see UChat#broadcast(String, String)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(String message, @org.jetbrains.annotations.Nullable Predicate<? super CommandSender> filter) {
        broadcast(new TextComponent(message), filter);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send, as a {@link Component}
     * @since 125
     * @apiNote requires Adventure API support
     * @see UChat#broadcast(Component, String)
     * @see UChat#broadcast(Component, Predicate)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(Component message) { broadcast(message, (Predicate<? super CommandSender>) null); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link Component}
     * @param permission the permission required to see the message
     * @since 125
     * @apiNote requires Adventure API Support
     * @see UChat#broadcast(Component)
     * @see UChat#broadcast(Component, Predicate)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(Component message, @org.jetbrains.annotations.Nullable String permission) {
        Validators.validateAdventureSupport();
        broadcast(LegacyComponentSerializer.legacyAmpersand().serialize(message), permission);
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link Component}
     * @param filter the filter of command senders that can see this message, note: only {@link ProxiedPlayer players} and {@link CommandSender console} are checked!
     * @since 126
     * @apiNote requires Adventure API support
     * @see UChat#broadcast(Component)
     * @see UChat#broadcast(Component, String)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(Component message, @org.jetbrains.annotations.Nullable Predicate<? super CommandSender> filter) {
        Validators.validateAdventureSupport();
        broadcast(LegacyComponentSerializer.legacyAmpersand().serialize(message), filter);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send, as a {@link BaseComponent}
     * @since 125
     * @see UChat#broadcast(BaseComponent, String)
     * @see UChat#broadcast(BaseComponent, Predicate)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(BaseComponent message) { broadcast(message, (Predicate<? super CommandSender>) null); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link BaseComponent}
     * @param permission the permission required to see the message
     * @since 125
     * @see UChat#broadcast(BaseComponent)
     * @see UChat#broadcast(BaseComponent, Predicate)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(BaseComponent message, @Nullable String permission) {
        broadcast(message, player ->
                permission == null
                        || permission.strip().equalsIgnoreCase("")
                        || player.hasPermission(permission)
        );
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link BaseComponent}
     * @param filter the filter of command senders that can see this message, note: only {@link ProxiedPlayer players} and {@link CommandSender console} are checked!
     * @since 126
     * @see UChat#broadcast(BaseComponent)
     * @see UChat#broadcast(BaseComponent, String)
     * @deprecated use the method from {@link Broadcast} instead! This is being removed in the next 30 builds.
     */
    @Deprecated(forRemoval = true, since = "139")
    public static void broadcast(BaseComponent message, @Nullable Predicate<? super CommandSender> filter) {
        for(ProxiedPlayer player : CyberAPI.getInstance().getOnlinePlayers()) {
            if(filter != null && !filter.test(player)) continue;
            player.sendMessage(message);
        }
        if(filter == null || filter.test(CyberAPI.getInstance().getProxy().getConsole()))
            Log.info(BaseComponent.toLegacyText(message));
    }

}
