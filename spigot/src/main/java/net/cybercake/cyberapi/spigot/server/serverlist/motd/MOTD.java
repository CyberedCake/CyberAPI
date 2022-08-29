package net.cybercake.cyberapi.spigot.server.serverlist.motd;

import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.server.ServerProperties;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 9
 */
public class MOTD {

    /**
     * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link MOTD}
     * @param id the ID of the new {@link MOTD}
     * @return the Builder instance
     * @since 10
     */
    public static Builder builder(String id) { return new Builder(id); }

    /**
     * @since 10
     */
    public static class Builder {
        private final String id;

        private String motd;
        private boolean centered;
        private MOTDTextFormatter motdTextFormatter;
        private @Nullable File iconFile;
        private @Nullable URL iconURL;
        private MOTDIconType iconType;

        /**
         * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link MOTD}
         * @param id the ID of the new {@link MOTD}
         * @since 10
         */
        public Builder(String id) {
            if(!net.cybercake.cyberapi.common.basic.StringUtils.isAlphanumericSpace(id.replace("_", "").replace("-", "")))
                throw parseError("The 'id' must be alpha-numeric space (only contains A-Za-z0-9 _-)");
            if(id.isEmpty())
                throw parseError("The 'id' cannot be empty!");
            if(id.length() > 20)
                throw parseError("The 'id' must be less than or equal to 20 characters!");
            if(id.length() < 3)
                throw parseError("The 'id' must be more than or equal to 3 characters!");

            this.id = id;

            this.motd = String.valueOf(new ServerProperties().getProperty("motd"));
            this.centered = false;
            this.motdTextFormatter = MOTDTextFormatter.PLAIN;
            this.iconFile = null;
            this.iconURL = null;
            this.iconType = MOTDIconType.UNSET;
        }

        /**
         * Creates a parse exception, usually used internally in the {@link Builder} for {@link MOTD}
         * @param message the message to include after 'Failed to set the MOTD ID:'
         * @return the {@link IllegalArgumentException} instance
         * @since 28
         */
        private IllegalArgumentException parseError(String message) {
            return new IllegalArgumentException("Failed to set the MOTD ID: " + message);
        }

        /**
         * Sets the MOTD to a String.
         * @param motd the motd string
         */
        public Builder text(String motd) {
            this.motd = motd; return this;
        }

        /**
         * Sets the MOTD to two strings as different lines
         * @param line1 the first line of the MOTD
         * @param line2 the second line of the MOTD
         */
        public Builder text(String line1, String line2) {
            this.motd = line1 + "\n" + line2; return this;
        }

        /**
         * Sets the MOTD to multiple strings
         * @param motd the motd strings
         */
        public Builder text(String... motd) {
            this.motd = String.join("\n", motd); return this;
        }

        /**
         * Should the text be Centered (only works with legacy color codes, {@link Builder#shouldUseMiniMessage(boolean)} (boolean)} will cancel this out)
         * @param centered should the MOTD be centered
         */
        public Builder shouldCenter(boolean centered) {
            this.centered = centered; return this;
        }

        /**
         * Should the text use MiniMessage formatting instead of legacy bukkit color codes (will cancel out {@link Builder#shouldCenter(boolean)} (boolean)})
         * @param useMiniMessage use bukkit color codes (set to 'false') or use mini message (set to 'true')
         * @deprecated please use {@link Builder#motdTextFormatter(MOTDTextFormatter)} and define a {@link MOTDTextFormatter} instead
         */
        @Deprecated public Builder shouldUseMiniMessage(boolean useMiniMessage) {
            this.motdTextFormatter = MOTDTextFormatter.MINIMESSAGE; return this;
        }

        /**
         * Sets the {@link MOTDTextFormatter}, which is how color and decorations should be applied to the {@link MOTD} text
         * @param motdTextFormatter the {@link MOTDTextFormatter} of the MOTD
         */
        public Builder motdTextFormatter(MOTDTextFormatter motdTextFormatter) {
            this.motdTextFormatter = motdTextFormatter; return this;
        }

        /**
         * Sets the MOTD icon to this image, a {@link File}
         * @param icon the {@link File} to cache
         */
        public Builder icon(File icon) {
            this.iconType = MOTDIconType.FILE; this.iconFile = icon; return this;
        }

        /**
         * Sets the MOTD icon to this image, a {@link java.net.URL}
         * @param icon the {@link java.net.URL} to cache
         */
        public Builder icon(URL icon) {
            this.iconType = MOTDIconType.URL; this.iconURL = icon; return this;
        }

        /**
         * Builds the builder into an {@link MOTD} instance
         * @return the {@link MOTD} instance
         * @since 10
         */
        public MOTD build() {
            return new MOTD(this);
        }
    }

    private final Builder builder;

    /**
     * The {@link MOTD} instance, created by the {@link Builder} instance
     * @param builder the builder that can then be transformed into a {@link MOTD}
     * @since 10
     */
    public MOTD(Builder builder) {
        this.builder = builder;
    }

    /**
     * Gets the {@link String} ID of the MOTD
     * @return the MOTD ID
     * @since 10
     */
    public String getID() {
        return builder.id;
    }

    /**
     * Gets the {@link String} form of the {@link MOTD}, a.k.a. what is shown on the server list
     * @return the MOTD
     * @since 10
     */
    public String getStringMOTD() {
        return builder.motd;
    }

    /**
     * Gets whether the {@link MOTD} should be centered or not. {@link MOTD#isUsingMiniMessage()} overrides this.
     * @return whether the {@link MOTD} should be centered or not
     * @since 10
     */
    public boolean isCentered() {
        return builder.centered;
    }

    /**
     * Gets whether the {@link MOTD} should be using MiniMessage parsing or not. This overrides {@link MOTD#isCentered()}.
     * @return whether the {@link MOTD} should be using MiniMessage
     * @since 10
     * @deprecated please use {@link MOTD#getMOTDTextFormatter()} and retrieve a {@link MOTDTextFormatter} instead
     */
    @Deprecated public boolean isUsingMiniMessage() {
        return (builder.motdTextFormatter == MOTDTextFormatter.MINIMESSAGE);
    }

    /**
     * Gets how the {@link MOTD} will be formatted, using a {@link MOTDTextFormatter}
     * @return the {@link MOTDTextFormatter} type
     * @since 73
     */
    public MOTDTextFormatter getMOTDTextFormatter() { return builder.motdTextFormatter; }

    /**
     * Gets the icon type of the MOTD
     * @return the icon type of MOTD
     * @since 10
     */
    public MOTDIconType getMOTDIconType() {
        return builder.iconType;
    }

    /**
     * Gets the {@link File} of the icon
     * @return the icon in {@link File} form, {@code null} if {@link MOTDIconType} is {@link MOTDIconType#UNSET} or not {@link MOTDIconType#FILE}
     * @since 10
     */
    public File getFileIcon() {
        if(builder.iconFile != null && getMOTDIconType().equals(MOTDIconType.FILE)) {
            return builder.iconFile;
        }
        return null;
    }

    /**
     * Gets the {@link URL} of the icon
     * @return the icon in {@link URL} form, {@code null} if {@link MOTDIconType} is {@link MOTDIconType#UNSET} or not {@link MOTDIconType#URL}
     * @since 10
     */
    public URL getURLIcon() {
        if(builder.iconURL != null && getMOTDIconType().equals(MOTDIconType.URL)) {
            return builder.iconURL;
        }
        return null;
    }

    /**
     * Gets the formatted MOTD with MiniMessage and proper centering
     * @return the formatted {@link String} MOTD
     * @since 10
     */
    public String getFormattedMOTD() {
        boolean centered = isCentered();
        String text = getStringMOTD();

        if(centered) {
            List<String> newMOTD = new ArrayList<>();
            for(String str : text.split("\\n"))
                newMOTD.add(org.apache.commons.lang3.StringUtils.center(ChatColor.stripColor(str), 45));
            text = String.join("\n", newMOTD);
        }

        return switch(this.getMOTDTextFormatter()) {
            case LEGACY -> UChat.chat(text);
            case MINIMESSAGE ->
                    LegacyComponentSerializer.builder().useUnusualXRepeatedCharacterHexFormat().hexColors().build()
                            .serialize(UChat.miniMessage(text));
            default -> text;
        };
    }

}
