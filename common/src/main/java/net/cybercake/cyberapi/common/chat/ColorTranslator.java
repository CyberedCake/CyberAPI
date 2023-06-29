package net.cybercake.cyberapi.common.chat;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Allows you to translate colors between multiple different color systems, namely: Bukkit Chat Color, Bungee Chat Color, Adventure Text Format, and Mini Message
 * @since 142
 */
@SuppressWarnings({"unused"})
public enum ColorTranslator {

    // color codes
    //<editor-fold desc="Color Codes">
    BLACK           (org.bukkit.ChatColor.BLACK,
                        ChatColor.BLACK,
                        NamedTextColor.BLACK,
                        new MiniMessageTranslation("<black>", NamedTextColor.BLACK)
                    ),
    DARK_BLUE       (org.bukkit.ChatColor.DARK_BLUE,
                        ChatColor.DARK_BLUE,
                        NamedTextColor.DARK_BLUE,
                        new MiniMessageTranslation("<dark_blue>", NamedTextColor.DARK_BLUE)
                    ),
    DARK_GREEN      (org.bukkit.ChatColor.DARK_GREEN,
                        ChatColor.DARK_GREEN,
                        NamedTextColor.DARK_GREEN,
                        new MiniMessageTranslation("<dark_green>", NamedTextColor.DARK_GREEN)
                    ),
    DARK_AQUA       (org.bukkit.ChatColor.DARK_AQUA,
                        ChatColor.DARK_AQUA,
                        NamedTextColor.DARK_AQUA,
                        new MiniMessageTranslation("<dark_aqua>", NamedTextColor.DARK_AQUA)
                    ),
    DARK_RED        (org.bukkit.ChatColor.DARK_RED,
                        ChatColor.DARK_RED,
                        NamedTextColor.DARK_RED,
                        new MiniMessageTranslation("<dark_red>", NamedTextColor.DARK_RED)
                    ),
    DARK_PURPLE     (org.bukkit.ChatColor.DARK_PURPLE,
                        ChatColor.DARK_PURPLE,
                        NamedTextColor.DARK_PURPLE,
                        new MiniMessageTranslation("<dark_purple>", NamedTextColor.DARK_PURPLE)
                    ),
    GOLD            (org.bukkit.ChatColor.GOLD,
                        ChatColor.GOLD,
                        NamedTextColor.GOLD,
                        new MiniMessageTranslation("<gold>", NamedTextColor.GOLD)
                    ),
    GRAY            (org.bukkit.ChatColor.GRAY,
                        ChatColor.GRAY,
                        NamedTextColor.GRAY,
                        new MiniMessageTranslation("<gray>", NamedTextColor.GRAY)
                    ),
    DARK_GRAY       (org.bukkit.ChatColor.DARK_GRAY,
                        ChatColor.DARK_GRAY,
                        NamedTextColor.DARK_GRAY,
                        new MiniMessageTranslation("<dark_gray>", NamedTextColor.DARK_GRAY)
                    ),
    BLUE            (org.bukkit.ChatColor.BLUE,
                        ChatColor.BLUE,
                        NamedTextColor.BLUE,
                        new MiniMessageTranslation("<blue>", NamedTextColor.BLUE)
                    ),
    GREEN           (org.bukkit.ChatColor.GREEN,
                        ChatColor.GREEN,
                        NamedTextColor.GREEN,
                        new MiniMessageTranslation("<green>", NamedTextColor.GREEN)
                    ),
    AQUA            (org.bukkit.ChatColor.AQUA,
                        ChatColor.AQUA,
                        NamedTextColor.AQUA,
                        new MiniMessageTranslation("<aqua>", NamedTextColor.AQUA)
                    ),
    RED             (org.bukkit.ChatColor.RED,
                        ChatColor.RED,
                        NamedTextColor.RED,
                        new MiniMessageTranslation("<red>", NamedTextColor.RED)
                    ),
    LIGHT_PURPLE    (org.bukkit.ChatColor.LIGHT_PURPLE,
                        ChatColor.LIGHT_PURPLE,
                        NamedTextColor.LIGHT_PURPLE,
                        new MiniMessageTranslation("<light_purple>", NamedTextColor.LIGHT_PURPLE)
                    ),
    YELLOW          (org.bukkit.ChatColor.YELLOW,
                        ChatColor.YELLOW,
                        NamedTextColor.YELLOW,
                        new MiniMessageTranslation("<yellow>", NamedTextColor.YELLOW)
                    ),
    WHITE           (org.bukkit.ChatColor.WHITE,
                        ChatColor.WHITE,
                        NamedTextColor.WHITE,
                        new MiniMessageTranslation("<white>", NamedTextColor.WHITE)
                    ),
    //</editor-fold>

    // format codes
    //<editor-fold desc="Format Codes">
    BOLD            (org.bukkit.ChatColor.BOLD,
                        ChatColor.BOLD,
                        TextDecoration.BOLD,
                        new MiniMessageTranslation("<bold>", TextDecoration.BOLD)
                    ),
    STRIKETHROUGH   (org.bukkit.ChatColor.STRIKETHROUGH,
                        ChatColor.STRIKETHROUGH,
                        TextDecoration.STRIKETHROUGH,
                        new MiniMessageTranslation("<strikethrough>", TextDecoration.STRIKETHROUGH)
                    ),
    ITALIC          (org.bukkit.ChatColor.ITALIC,
                        ChatColor.ITALIC,
                        TextDecoration.ITALIC,
                        new MiniMessageTranslation("<italic>", TextDecoration.ITALIC)
                    ),
    OBFUSCATED      (org.bukkit.ChatColor.MAGIC,
                        ChatColor.MAGIC,
                        TextDecoration.OBFUSCATED,
                        new MiniMessageTranslation("<obfuscated>", TextDecoration.OBFUSCATED)
                    ),
    UNDERLINED      (org.bukkit.ChatColor.UNDERLINE,
                        ChatColor.UNDERLINE,
                        TextDecoration.UNDERLINED,
                        new MiniMessageTranslation("<underlined>", TextDecoration.UNDERLINED)
                    ),
    RESET           (org.bukkit.ChatColor.RESET,
                        ChatColor.RESET,
                        null,
                        new MiniMessageTranslation("<reset>", null)
                    );
    //</editor-fold>


    private final org.bukkit.ChatColor bukkit;
    private final ChatColor bungee;
    private final TextFormat adventure;
    private final MiniMessageTranslation miniMessage;

    ColorTranslator(@NotNull org.bukkit.ChatColor bukkit, @NotNull ChatColor bungee, @Nullable TextFormat adventure, @NotNull MiniMessageTranslation miniMessage) {
        this.bukkit = tryValue(org.bukkit.ChatColor.class, bukkit);
        this.bungee = tryValue(ChatColor.class, bungee);
        this.adventure = tryValue(TextFormat.class, adventure);
        this.miniMessage = tryValue(MiniMessageTranslation.class, miniMessage);
    }

    /**
     * Gets the Bukkit Chat Color, from {@link org.bukkit.ChatColor}
     * @return the chat color from org.bukkit.ChatColor
     * @since 142
     */
    public @NotNull org.bukkit.ChatColor getBukkit() { return this.bukkit; }

    /**
     * Gets the Bungee Chat Color, from {@link ChatColor}
     * @return the chat color from net.md_5.bungee.api.ChatColor
     * @since 142
     */
    public @NotNull ChatColor getBungee() { return this.bungee; }

    /**
     * Gets the Adventure Text Format, from {@link TextFormat} (which supports both {@link NamedTextColor} and {@link TextDecoration}
     * -- both are used, TextFormat is just the interface version of both of these)
     * @return the text format from net.kyori.adventure.text.format.TextFormat
     * @since 142
     */
    public @Nullable TextFormat getAdventure() { return this.adventure; }

    /**
     * Gets the Mini Message Translation, from {@link MiniMessageTranslation} (this is a CyberAPI thing that allows you to get
     * the literal -- {@link MiniMessageTranslation#getLiteral() getLiteral()} -- or the actual color --
     * {@link MiniMessageTranslation#getFormat() getFormat()}. One should note that the format --
     * {@link MiniMessageTranslation#getFormat() getFormat()} -- is sometimes null, especially in the case of the
     * {@code <reset>} tag)
     * @return the mini message translation from net.cybercake.cyberapi.common.chat.MiniMessageTranslation
     * @since 142
     */
    public @NotNull MiniMessageTranslation getMiniMessage() { return this.miniMessage; }

    /**
     * @param bukkitColor the {@link org.bukkit.ChatColor Bukkit Chat Color} to translate
     * @return the {@link ColorTranslator} for the associated input color
     * @since 142
     */
    public static @Nullable ColorTranslator getColorFor(org.bukkit.ChatColor bukkitColor) {
        return Arrays.stream(values())
                .filter(translate -> translate.getBukkit().equals(bukkitColor))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param bungeeColor the {@link ChatColor Bungee Chat Color} to translate
     * @return the {@link ColorTranslator} for the associated input color
     * @since 142
     */
    public static @Nullable ColorTranslator getColorFor(ChatColor bungeeColor) {
        return Arrays.stream(values())
                .filter(translate -> translate.getBungee().equals(bungeeColor))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param adventureColor the {@link TextFormat Adventure Text Format} to translate
     * @return the {@link ColorTranslator} for the associated input color
     * @since 142
     */
    public static @Nullable ColorTranslator getColorFor(TextFormat adventureColor) {
        return Arrays.stream(values())
                .filter(translate ->
                        translate.getAdventure() != null
                        && translate.getAdventure().equals(adventureColor))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param miniMessage the {@link String Mini Message Syntax} to translate (for example: "{@code <red>}" or "{@code red}" -- lowercase and only one format)
     * @return the {@link ColorTranslator} for the associated input color
     * @since 142
     */
    public static @Nullable ColorTranslator getColorFor(String miniMessage) {
        String withoutPotentialBrackets = miniMessage.replace(">", "").replace("<", "");
        return Arrays.stream(values())
                .filter(translate ->
                        translate.getMiniMessage().getLiteral().equalsIgnoreCase(miniMessage)
                        || translate.getMiniMessage().getLiteral().equalsIgnoreCase(withoutPotentialBrackets)
                )
                .findFirst()
                .orElse(null);
    }


    @ApiStatus.Internal
    private @Nullable TextFormat retrieveTextFormat(@Nullable String input) {
        if(input == null) return null;
        Map<String, NamedTextColor> map = new HashMap<>(NamedTextColor.NAMES.keyToValue());
        if(!map.containsKey(input.toLowerCase(Locale.ROOT))) return TextDecoration.valueOf(input.toUpperCase(Locale.ROOT));
        return map.get(input.toLowerCase(Locale.ROOT));
    }

    @ApiStatus.Internal
    private <T> T tryValue(Class<T> output, @Nullable T potentialInput) {
        if(potentialInput == null) return null;
        try {
            Class.forName(output.getCanonicalName());
            return potentialInput;
        } catch (NoClassDefFoundError | ClassNotFoundException noClassFound) {
            throw new IllegalStateException("CyberAPI has found no viable class (nullable:" + false + ") while trying to find " + output.getCanonicalName(), noClassFound);
        }
    }

}
