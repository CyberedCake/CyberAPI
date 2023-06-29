package net.cybercake.cyberapi.common.chat;

import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Mini Message translation between TextFormat and String
 * @since 142
 */
public class MiniMessageTranslation {

    private final @NotNull String literal;
    private final @Nullable TextFormat format;

    MiniMessageTranslation(@NotNull String literal, @Nullable TextFormat format) {
        this.literal = literal;
        this.format = format;
    }

    /**
     * The literal for the Mini Message syntax (i.e., {@code <red>})
     * @return the Mini Message syntax literal
     * @since 142
     */
    public @NotNull String getLiteral() { return this.literal; }

    /**
     * The text format for the Mini Message syntax ... using either {@link net.kyori.adventure.text.format.NamedTextColor NamedTextColor} or {@link net.kyori.adventure.text.format.TextDecoration TextDecoration}
     * @return the Adventure text format for the Mini Message syntax
     * @since 142
     */
    public @Nullable TextFormat getFormat() { return this.format; }

}