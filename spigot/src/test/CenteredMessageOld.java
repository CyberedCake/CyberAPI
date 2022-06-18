//package net.cybercake.cyberapi.spigot.chat.centered;
//
//import net.cybercake.cyberapi.spigot.chat.UChat;
//import net.kyori.adventure.text.Component;
//import org.bukkit.command.CommandSender;
//
///**
// * THIS CODE IS NOT MINE (I JUST MODIFIED IT) <br>
// * <a href="https://www.spigotmc.org/threads/center-motds-and-messages.354209">THE ORIGINAL CODE CAN BE FOUND HERE (click here)</a>
// * <br> <br>
// * lineLength = 80 (Chat Length) <br>
// * lineLength = 45 (MOTD Length) <br>
// */
//public class CenteredMessageOld {
//
//    // THIS CODE IS NOT MINE (I JUST MODIFIED IT)
//    // THE ORIGINAL CODE CAN BE FOUND HERE:
//    // https://www.spigotmc.org/threads/center-motds-and-messages.354209
//
//    // lineLength = 80 (Chat Length)
//    // lineLength = 45 (MOTD Length)
//
//    /**
//     * Get a centered message from a {@link String} and return as a {@link String}
//     * <br> <br>
//     * If you are looking to center a <b>chat</b> message, use {@link CenteredMessage#get(String, TextType)} with {@link TextType#CHAT} <br>
//     * If you are looking to center an <b>motd</b>, use {@link CenteredMessage#get(String, TextType)} with {@link TextType#MOTD}
//     * @param message the message to center
//     * @param lineLength the length of the line
//     * @return the centered message
//     */
//    public static String get(String message, int lineLength) {
//        if(message == null || message.equals("")) { return ""; }
//        char[] chars = message.toCharArray(); // Get a list of all characters in text
//        boolean isBold = false;
//        double length = 0;
//        ChatColor pholder;
//        for (int i = 0; i < chars.length; i++) { // Loop through all characters
//            // Check if the character is a ColorCode..
//            if (chars[i] == '&' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
//                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
//                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
//                    isBold = (chars[i + 1] == 'l'); // Setting bold  to true or false, depending on if the ChatColor is Bold.
//                    length--; // Removing one from the length, of the string, because we don't wanna count color codes.
//                    i += isBold ? 1 : 0;
//                }
//            } else {
//                // If the character is not a color code:
//                length++; // Adding a space
//                length += (isBold ? (chars[i] != ' ' ? 0.1555555555555556 : 0) : 0); // Adding 0.156 spaces if the character is bold.
//            }
//        }
//
//        double spaces = (lineLength - length) / 2; // Getting the spaces to add by (max line length - length) / 2
//
//        // Adding the spaces
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < spaces; i++) {
//            builder.append(' ');
//        }
//        String copy = builder.toString();
//        builder.append(message).append(copy);
//
//        return builder.toString();
//    }
//
//    /**
//     * Get a centered message from a {@link Component} and return as a {@link Component}
//     * <br> <br>
//     * If you are looking to center a <b>chat</b> message, use {@link CenteredMessage#get(Component, TextType)} with {@link TextType#CHAT} <br>
//     * If you are looking to center an <b>motd</b>, use {@link CenteredMessage#get(Component, TextType)} with {@link TextType#MOTD}
//     * @param message the message to center
//     * @param lineLength the length of the line
//     * @return the centered message
//     * @apiNote requires Adventure API support
//     */
//    public static Component get(Component message, int lineLength) {
//        Validators.validateAdventureSupport();
//        if(message == null) { return Component.empty(); }
//        String componentString = LegacyComponentSerializer.legacyAmpersand().serialize(message);
//
//        char[] chars = componentString.toCharArray(); // Get a list of all characters in text
//        boolean isBold = false;
//        double length = 0;
//        ChatColor pholder;
//        for (int i = 0; i < chars.length; i++) { // Loop through all characters
//            // Check if the character is a ColorCode
//            if (chars[i] == '&' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
//                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
//                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
//                    isBold = (chars[i + 1] == 'l'); // Setting bold  to true or false, depending on if the ChatColor is Bold.
//                    length--; // Removing one from the length, of the string, because we don't wanna count color codes.
//                    i += isBold ? 1 : 0;
//                }
//            } else {
//                // If the character is not a color code:
//                length++; // Adding a space
//                length += (isBold ? (chars[i] != ' ' ? 0.1555555555555556 : 0) : 0); // Adding 0.156 spaces if the character is bold.
//            }
//        }
//
//        double spaces = (lineLength - length) / 2; // Getting the spaces to add by (max line length - length) / 2
//
//        // Adding the spaces
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < spaces; i++) {
//            builder.append(' ');
//        }
//        String copy = builder.toString();
//        builder.append(componentString).append(copy);
//
//        return LegacyComponentSerializer.legacy('&').deserialize(builder.toString());
//    }
//
//    /**
//     * The TextType that will be being used by a {@link CenteredMessage} method.
//     * <br>
//     * This is required because the different text type's have different length's, as one length does not and will not fit all
//     */
//    public enum TextTypeOld {
//        /**
//         * The Chat Text Type
//         * <br> <br>
//         * Used for chat messages in most player's chat
//         * <br> <br>
//         * <b>74 characters <- default</b>
//         */
//        CHAT(74),
//
//        /**
//         * The MOTD Text Type
//         * <br> <br>
//         * Used for the MOTD of the server on the server list
//         * <br> <br>
//         * <b>45 characters <- default</b>
//         */
//        MOTD(45);
//
//        private final int length;
//
//        TextType(int length) {
//            this.length = length;
//        }
//
//        /**
//         * Gets the length of a TextType
//         * @return the {@link TextType} length
//         */
//        public int getLength() {
//            return length;
//        }
//    }
//
//    /**
//     * Gets the default TextType, which is {@link TextType#CHAT}
//     * @return the default text type -> {@link TextType#CHAT}
//     */
//    public static TextType getDefaultTextType() {
//        return TextType.CHAT;
//    }
//
//    /**
//     * Gets a centered message from a {@link String} and {@link TextType}.
//     * {@link TextType} is <b>required</b> in order for CyberAPI to correctly judge the amount of characters to space.
//     * @param message the message to center
//     * @param textType the type of text
//     * @return the {@link String} version of a centered message
//     */
//    public static String get(String message, TextType textType) { return get(message, fromTextType(textType)); }
//
//    /**
//     * Gets a centered message from a {@link Component} and {@link TextType}.
//     * {@link TextType} is <b>required</b> in order for CyberAPI to correctly judge the amount of characters to space.
//     * @param message the message to center, a {@link Component}
//     * @param textType the type of text
//     * @return the {@link Component} version of a centered message
//     * @apiNote requires Adventure API support
//     */
//    public static Component get(Component message, TextType textType) { return get(message, fromTextType(textType)); }
//
//    /**
//     * Gets a centered message from a {@link String}.
//     * This method assumes that the text type is {@link TextType#CHAT}
//     * @param message the message to center
//     * @return the {@link String} version of a centered message
//     */
//    public static String get(String message) { return get(message, fromTextType(TextType.CHAT)); }
//
//    /**
//     * Gets a centered message from a {@link Component}.
//     * This method assumes that the text type is {@link TextType#CHAT}.
//     * @param message the message to center, a {@link Component}
//     * @return the {@link Component} version of a centered message
//     * @apiNote requires Adventure API support
//     */
//    public static Component get(Component message) { return get(message, fromTextType(TextType.CHAT)); }
//
//    /**
//     * Sends a centered message to a {@link CommandSender}.
//     * This method assumes that the text type is {@link TextType#CHAT}.
//     * @param player the player or sender to send the centered message to
//     * @param message the message that needs centering
//     */
//    public static void send(CommandSender player, String message) { send(player, message, TextType.CHAT); }
//
//    /**
//     * Sends a centered message to a {@link CommandSender} and allows the developer to select a {@link TextType}.
//     * @param player the player or sender to send the centered message to
//     * @param message the message that needs centering
//     * @param type the general type of the message
//     */
//    public static void send(CommandSender player, String message, TextType type) { player.sendMessage(UChat.chat(get(message, type))); }
//
//    /**
//     * Sends a centered message to a {@link CommandSender}.
//     * This method assumes that the text type is {@link TextType#CHAT}.
//     * @param player the player or sender to send the centered message to
//     * @param message the message that needs centering, a {@link Component}
//     * @apiNote requires Adventure API support
//     */
//    public static void send(CommandSender player, Component message) { send(player, message, TextType.CHAT); }
//
//    /**
//     * Sends a centered message to a {@link CommandSender} and allows the developer to select a {@link TextType}.
//     * @param player the player or sender to send the centered message to
//     * @param message the message that needs centering, a {@link Component}
//     * @param type the general type of the message
//     * @apiNote requires Adventure API support
//     */
//    public static void send(CommandSender player, Component message, TextType type) { player.sendMessage(UChat.toLegacy(get(message, type))); }
//
//
//}
