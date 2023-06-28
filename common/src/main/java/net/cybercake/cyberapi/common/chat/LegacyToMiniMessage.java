package net.cybercake.cyberapi.common.chat;

/**
 * An enumeration which maps old legacy color codes, using the '{@literal &}' character, to its more modern counterpart in MiniMessage
 * @since 139
 */
public enum LegacyToMiniMessage {

    BLACK("&0", "<black>"),
    DARK_BLUE("&1", "<dark_blue>"),
    DARK_GREEN("&2", "<dark_green>"),
    DARK_AQUA("&3", "<dark_aqua>"),
    DARK_RED("&4", "<dark_red>"),
    DARK_PURPLE("&5", "<dark_purple>"),
    GOLD("&6", "<gold>"),
    GRAY("&7", "<gray>"),
    DARK_GRAY("&8", "<dark_gray>"),
    BLUE("&9", "<blue>"),
    GREEN("&a", "<green>"),
    AQUA("&b", "<aqua>"),
    RED("&c", "<red>"),
    LIGHT_PURPLE("&d", "<light_purple>"),
    YELLOW("&e", "<yellow>"),
    WHITE("&f", "<white>"),
    UNDERLINED("&n", "<underlined>"),
    STRIKETHROUGH("&m", "<strikethrough>"),
    OBFUSCATED("&k", "<obfuscated>"),
    ITALIC("&o", "<italic>"),
    BOLD("&l", "<bold>"),
    RESET("&r", "<reset>");

    /**
     * Represents the legacy section symbol
     * @since 139
     */
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public static final char LEGACY_SECTION_SYMBOL = '\u00A7';

    /**
     * Represents the legacy ampersand symbol often used in place of {@link LegacyToMiniMessage#LEGACY_SECTION_SYMBOL the section symbol}
     * @since 139
     */
    public static final char LEGACY_AMPERSAND_SYMBOL = '&';

    private final String old;
    private final String newer;
    LegacyToMiniMessage(String old, String newer) {
        this.old = old;
        this.newer = newer;
    }

    /**
     * Retrieve the legacy character and symbol to access the color or chat decoration
     * @return the legacy character and symbol, formatted like '{@literal &}' and '{@literal c}' ("{@code &c}") for {@link LegacyToMiniMessage#RED red}
     * @since 139
     */
    public String getOld() { return this.old; }

    /**
     * Retrieve the MiniMessage way to access the color or chat decoration
     * @return the MiniMessage string, formatted like '{@literal <}', '{@code red}', and '{@literal >}' ("{@code <red>}") for {@link LegacyToMiniMessage#RED red}
     * @since 139
     */
    public String getNewer() { return this.newer; }

    /**
     * Modify a legacy {@link String} to a modern MiniMessage string by replacing the legacy format with MiniMessage format ("{@code &c}" -> "{@code <red>}" for {@link LegacyToMiniMessage#RED red})
     * @param old the legacy string, which contains a format such as "{@code &c}"
     * @return the modern string, which would contain "{@code <red>}" if inputted with "{@code &c}"
     * @since 139
     * @apiNote this method will also check and replace {@link LegacyToMiniMessage#LEGACY_SECTION_SYMBOL the legacy section symbol}
     */
    public String cleanse(String old) {
        return old
                .replace(LEGACY_SECTION_SYMBOL, LEGACY_AMPERSAND_SYMBOL) // §c -> &c
                .replace(getOld(), getNewer()); // &c -> <red>
    }

    /**
     * Modify a legacy {@link String} to a modern MiniMessage string by replacing the legacy format with MiniMessage format ("{@code &c}" -> "{@code <red>}" for {@link LegacyToMiniMessage#RED red})
     * @param alternateCharacter the character which would replace the "{@code &c}" in that {@link String} -- often this could be {@link LegacyToMiniMessage#LEGACY_SECTION_SYMBOL the section symbol}
     * @param old the legacy string, which contains a format such as "{@code &c}"
     * @return the modern string, which would contain "{@code <red>}" if inputted with "{@code &c}"
     * @since 139
     */
    public String cleanse(char alternateCharacter, String old) { return cleanse(old.replace(alternateCharacter, LEGACY_AMPERSAND_SYMBOL)); }

}
