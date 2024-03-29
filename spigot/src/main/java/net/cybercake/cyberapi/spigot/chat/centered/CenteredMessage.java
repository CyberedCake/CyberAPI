package net.cybercake.cyberapi.spigot.chat.centered;

import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.Validators;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.player.CyberPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * {@link CenteredMessage} handles message centering, including MOTDs and chat
 * @since 136
 */
@SuppressWarnings({"unused"})
public class CenteredMessage {

    private String message;
    private int length;

    /**
     * Creates a blank {@link CenteredMessage} instance, usually if you are going to use the {@link CenteredMessage#setMessage(String)}, {@link CenteredMessage#setLength(int)}, or {@link CenteredMessage#setTextType(TextType)} methods
     * @since 136
     */
    public CenteredMessage() {
        this.message = "";
        this.length = TextType.CHAT.getLength();
    }

    /**
     * Creates a {@link CenteredMessage} instance with an already pre-defined message and length
     * @param message the message to center
     * @param length the length of the message that will be used in determine the exact center of the message, in number of characters
     * @since 136
     */
    public CenteredMessage(String message, int length) {
        this.message = message;
        this.length = length;
    }

    /**
     * Creates a {@link CenteredMessage} instance with an already pre-defined message and text type
     * @param message the message to center
     * @param type the type of the message
     * @since 136
     */
    public CenteredMessage(String message, TextType type) {
        this.message = message;
        this.length = type.getLength();
    }

    /**
     * Creates a {@link CenteredMessage} instance with an already pre-defined message <br>
     * This method assumes the text type is {@link TextType#CHAT}
     * @param message the chat message to center
     * @since 136
     */
    public CenteredMessage(String message) {
        this.message = message;
        this.length = TextType.CHAT.getLength();
    }

    /**
     * Represents the method of centering messaging that is used
     * @since 136
     * @see Method#ONE
     * @see Method#TWO
     */
    public enum Method {
        /**
         * Represents the first centering method, usually better for non-chat related operations
         * @since 136
         * @see Method#TWO
         */
        ONE,

        /**
         * Represents the second centering method, usually better for only-chat related operations
         * @since 136
         * @see Method#ONE
         * @Author: <a href="https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/">SirSpoodles on the SpigotMC forums</a>
         */
        TWO
    }

    /**
     * A {@link String} representing the centered message
     * <p>If {@link TextType} is {@link TextType#CHAT}, it will use {@link Method#TWO}, if it is anything other than CHAT, it will use {@link Method#ONE}</p>
     * @return the centered message
     * @since 136
     */
    public String getString() {
        if (TextType.CHAT.getLength() == length) return getString(Method.TWO);
        return getString(Method.ONE);
    }

    /**
     * A {@link String} representing the centered message
     * @param method the method at which to get the centered message
     * @return the centered message
     * @since 136
     */
    public String getString(Method method) {
        switch(method) {
            // METHOD ONE
            // (TYPICALLY BETTER FOR ANYTHING OTHER THAN CHAT)
            case ONE -> {
                if (message == null || message.equals("")) { return ""; }
                char[] chars = message.toCharArray(); // Get a list of all characters in text
                boolean isBold = false;
                double length = 0;
                ChatColor pholder;
                for (int i = 0; i < chars.length; i++) { // Loop through all characters
                    // Check if the character is a ColorCode...
                    if (chars[i] == '&' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
                        if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
                                && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
                            isBold = (chars[i + 1] == 'l'); // Setting bold  to true or false, depending on if the ChatColor is Bold.
                            length--; // Removing one from the length, of the string, because we don't want to count color codes.
                            i += isBold ? 1 : 0;
                        }
                    } else {
                        // If the character is not a color code:
                        length++; // Adding a space
                        length += (isBold ? (chars[i] != ' ' ? 0.1555555555555556 : 0) : 0); // Adding 0.156 spaces if the character is bold.
                    }
                }

                double spaces = (this.length - length) / 2; // Getting the spaces to add by (max line length - length) / 2

                // Adding the spaces
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < spaces; i++) {
                    builder.append(' ');
                }
                String copy = builder.toString();
                builder.append(message).append(copy);

                return UChat.chat(builder.toString()).stripTrailing();
            }
            // METHOD TWO
            // (TYPICALLY BETTER FOR CHAT)
            case TWO -> {
                if (TextType.CHAT.getLength() != length) CyberAPI.getInstance().getAPILogger().warn("The CenteredMessage method, 'Method.TWO', recommends using it only for Chat Options, as the 'length' is not used in method two!");

                String[] lines = (this.message.contains("\n") ? this.message.split("\n") : new String[]{this.message});
                StringBuilder centeredLines = new StringBuilder();
                for(String line : lines) {
                    if (line == null || line.equals("")) continue;
                    if (!centeredLines.isEmpty()) centeredLines.append("\n");

                    line = UChat.chat(line);

                    int messagePxSize = 0;
                    boolean previousCode = false;
                    boolean isBold = false;

                    for(char c : line.toCharArray()){
                        if (c == '\u00A7'){
                            previousCode = true;
                        }else if (previousCode){
                            previousCode = false;
                            isBold = c == 'l' || c == 'L';
                        }else{
                            DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                            messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                            messagePxSize++;
                        }
                    }

                    int halvedMessageSize = messagePxSize / 2;
                    int toCompensate = this.length - halvedMessageSize;
                    int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
                    int compensated = 0;
                    StringBuilder sb = new StringBuilder();
                    while(compensated < toCompensate){
                        sb.append(" ");
                        compensated += spaceLength;
                    }
                    centeredLines.append(sb).append(line);
                }

                return UChat.chat(centeredLines.toString()).stripTrailing();
            }
        }
        throw new IllegalArgumentException("Invalid method name, valid methods are 'Method.ONE' and 'Method.TWO'");
    }

    /**
     * A {@link Component} representing the centered message
     * @return the centered message, a {@link Component}
     * @apiNote requires Adventure API support
     * @since 136
     */
    public Component getComponent() {
        if (TextType.CHAT.getLength() == length) return getComponent(Method.TWO);
        return getComponent(Method.ONE);
    }

    /**
     * A {@link Component} with a certain {@link Method} representing how the centered message is obtained
     * @param method the method to use in centering the message
     * @return the centered message, a {@link Component} using {@link Method a certain method}
     * @apiNote requires Adventure API support
     * @since 136
     */
    public Component getComponent(Method method) {
        Validators.validateAdventureSupport();
        return LegacyComponentSerializer.legacySection().deserialize(getString(method));
    }

    /**
     * A {@link BaseComponent} representing the centered message
     * @return the centered message, a {@link BaseComponent}
     * @since 136
     */
    public BaseComponent[] getBaseComponent() {
        if (TextType.CHAT.getLength() == length) return getBaseComponent(Method.TWO);
        return getBaseComponent(Method.ONE);
    }

    /**
     * A {@link BaseComponent} with a certain {@link Method} representing how the centered message is obtained
     * @param method the method to use in centering the message
     * @return the centered message, a {@link BaseComponent} using {@link Method a certain method}
     * @since 136
     */
    public BaseComponent[] getBaseComponent(Method method) {
        return TextComponent.fromLegacyText(getString(method));
    }

    /**
     * Sends a message to a {@link CommandSender} using the {@code sender.sendMessage()} method
     * @param sender the {@link CommandSender} to send the centered message to
     * @since 136
     */
    public void send(CommandSender sender) { sender.sendMessage(getString()); }

    /**
     * Sends a message to a {@link Player} using the {@code player.sendMessage()} method
     * @param player the {@link Player} to send the centered message to
     * @since 136
     */
    public void send(Player player) { player.sendMessage(getString()); }

    /**
     * Sends a message to a {@link CyberPlayer} using the {@code cyberPlayer.getOnlineActions().sendColored()} method
     * @param cyberPlayer the {@link CyberPlayer} to send the centered message to
     * @since 136
     */
    public void send(CyberPlayer cyberPlayer) {
        if (cyberPlayer.getOnlineActions() == null) throw new IllegalStateException("That Cyber Player (" + cyberPlayer.getUniqueID() + "), must be online to send them a " + this.getClass().getCanonicalName() + "!");
        cyberPlayer.getOnlineActions().sendColored(getString());
    }

    /**
     * Sets the message to a {@link String}, this var will then be used from now on as the message string
     * @param message what to set the message to
     * @return the current instance of {@link CenteredMessage}
     * @since 136
     */
    public CenteredMessage setMessage(String message) {
        this.message = message; return this;
    }

    /**
     * Sets the length to an integer, this var will then be used from now as the length
     * @param length what to set the length to
     * @return the current instance of {@link CenteredMessage}
     * @since 136
     */
    public CenteredMessage setLength(int length) {
        this.length = length; return this;
    }

    /**
     * Sets the {@link TextType}, this var will then be used from now on as the text type
     * @param textType what to set the text type to
     * @return the current instance of {@link CenteredMessage}
     * @since 136
     */
    public CenteredMessage setTextType(TextType textType) {
        this.length = textType.getLength(); return this;
    }

    /**
     * Gets the current length of the {@link CenteredMessage} instance
     * @return the current length
     * @since 136
     */
    public int getLength() { return length; }

    /**
     * Checks if this instance of {@link CenteredMessage} is equal to another instance of {@link CenteredMessage}
     * @param centeredMessage the other instance, must be {@link CenteredMessage}
     * @return whether the two instances match, true (yes, they match) or false (no, they do not match)
     * @since 136
     */
    public boolean equals(CenteredMessage centeredMessage) {
        return centeredMessage.getString().equalsIgnoreCase(this.getString()) &&
                centeredMessage.getLength() == this.getLength();
    }

}
