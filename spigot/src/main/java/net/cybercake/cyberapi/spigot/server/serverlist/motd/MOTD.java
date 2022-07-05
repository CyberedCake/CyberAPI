package net.cybercake.cyberapi.spigot.server.serverlist.motd;

import net.cybercake.cyberapi.spigot.chat.centered.CenteredMessage;
import net.cybercake.cyberapi.spigot.server.ServerProperties;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.chat.centered.TextType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;

/**
 * @since 3.1.0
 */
public class MOTD {

    /**
     * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link MOTD}
     * @param id the ID of the new {@link MOTD}
     * @return the Builder instance
     * @since 3.1.1
     */
    public static Builder builder(String id) { return new Builder(id); }

    /**
     * @since 3.1.1
     */
    public static class Builder {
        private final String id;

        private String motd;
        private boolean centered;
        private boolean useMiniMessage;
        private @Nullable File iconFile;
        private @Nullable URL iconURL;
        private MOTDIconType iconType;

        /**
         * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link MOTD}
         * @param id the ID of the new {@link MOTD}
         * @since 3.1.1
         */
        public Builder(String id) {
            this.id = id;

            this.motd = String.valueOf(new ServerProperties().getProperty("motd"));
            this.centered = false;
            this.useMiniMessage = false;
            this.iconFile = null;
            this.iconURL = null;
            this.iconType = MOTDIconType.UNSET;
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
         */
        public Builder shouldUseMiniMessage(boolean useMiniMessage) {
            this.useMiniMessage = useMiniMessage; return this;
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
         * @since 3.1.1
         */
        public MOTD build() {
            return new MOTD(this);
        }
    }

    private final Builder builder;

    /**
     * The {@link MOTD} instance, created by the {@link Builder} instance
     * @param builder the builder that can then be transformed into a {@link MOTD}
     * @since 3.1.1
     */
    public MOTD(Builder builder) {
        this.builder = builder;
    }

    /**
     * Gets the {@link String} ID of the MOTD
     * @return the MOTD ID
     * @since 3.1.1
     */
    public String getID() {
        return builder.id;
    }

    /**
     * Gets the {@link String} form of the {@link MOTD}, a.k.a. what is shown on the server list
     * @return the MOTD
     * @since 3.1.1
     */
    public String getStringMOTD() {
        return builder.motd;
    }

    /**
     * Gets whether the {@link MOTD} should be centered or not. {@link MOTD#isUsingMiniMessage()} overrides this.
     * @return whether the {@link MOTD} should be centered or not
     * @since 3.1.1
     */
    public boolean isCentered() {
        return builder.centered;
    }

    /**
     * Gets whether the {@link MOTD} should be using MiniMessage parsing or not. This overrides {@link MOTD#isCentered()}.
     * @return whether the {@link MOTD} should be using MiniMessage
     * @since 3.1.1
     */
    public boolean isUsingMiniMessage() {
        return builder.useMiniMessage;
    }

    /**
     * Gets the icon type of the MOTD
     * @return the icon type of MOTD
     * @since 3.1.1
     */
    public MOTDIconType getMOTDIconType() {
        return builder.iconType;
    }

    /**
     * Gets the {@link File} of the icon
     * @return the icon in {@link File} form, {@code null} if {@link MOTDIconType} is {@link MOTDIconType#UNSET} or not {@link MOTDIconType#FILE}
     * @since 3.1.1
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
     * @since 3.1.1
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
     * @since 3.1.1
     */
    public String getFormattedMOTD() {
        boolean centered = isCentered();
        boolean mini =isUsingMiniMessage();
        String text = getStringMOTD();

        if(mini) return UChat.chat(LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(text)));
        else if(centered) return new CenteredMessage(text, TextType.MOTD).getString(CenteredMessage.Method.TWO);
        return UChat.chat(text);
    }

}
