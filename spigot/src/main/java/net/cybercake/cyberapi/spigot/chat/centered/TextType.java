package net.cybercake.cyberapi.spigot.chat.centered;

/**
 * The TextType that will be being used by a {@link CenteredMessage} method.
 * <br>
 * This is required because the different text type's have different length's, as one length does not and will not fit all
 * @since 136
 * @see TextType#CHAT
 * @see TextType#MOTD
 */
public enum TextType {
    /**
     * The Chat Text Type
     * <br> <br>
     * Used for chat messages in most player's chat
     * <br> <br>
     * <b>160 characters {@literal <}- default</b>
     * @see TextType#MOTD
     */
    CHAT(160),

    /**
     * The MOTD Text Type
     * <br> <br>
     * Used for the MOTD of the server on the server list
     * <br> <br>
     * <b>45 characters {@literal <}- default</b>
     * @see TextType#CHAT
     */
    MOTD(60);

    private final int length;

    TextType(int length) {
        this.length = length;
    }

    /**
     * Gets the length of a TextType
     * @return the {@link TextType} length
     */
    public int getLength() {
        return length;
    }
}