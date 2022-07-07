package net.cybercake.cyberapi.common.builders.player;

import net.md_5.bungee.api.ChatColor;

/**
 * @since 15
 */
@SuppressWarnings({"all"})
public class PingSettings {

    /**
     * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link PingSettings} instance
     * @return the Builder instance
     * @since 15
     */
    public static Builder builder() { return new Builder(); }

    /**
     * @since 15
     */
    public static class Builder {
        private int loading;
        private int greenMin;
        private int greenMax;
        private int yellowMin;
        private int yellowMax;
        private int redMin;
        private int redMax;
        private int darkRedMin;
        private boolean showMS;

        /**
         * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link PingSettings} instance
         * @since 15
         * @deprecated use {@link PingSettings#builder()} instead
         */
        @Deprecated
        public Builder() {
            this.loading = 0;
            this.greenMin = 1;
            this.greenMax = 80;
            this.yellowMin = 81;
            this.yellowMax = 150;
            this.redMin = 151;
            this.redMax = 360;
            this.darkRedMin = 361;
            this.showMS = true;
        }

        /**
         * The loading (dark gray) maximum, any value less than or equal to this value will be {@link ChatColor#DARK_GRAY} (because that usually means the ping is loading)
         * <br> <br>
         * 0ms is typically used by Minecraft when the ping is unknown, but it will also be used on private servers if you are locally hosting. Because the server is local, your ping will always show "Loading..." (because it is 0ms)
         * @param loading the loading maximum for {@link ChatColor#DARK_GRAY}
         */
        public Builder loading(int loading) {
            this.loading = loading; return this;
        }

        /**
         * The green minimum and maximum, any value between or equal to these two values will be {@link ChatColor#GREEN}
         * @param min the minimum value for {@link ChatColor#GREEN}
         * @param max the maximum value for {@link ChatColor#GREEN}
         */
        public Builder green(int min, int max) {
            this.greenMin = min;
            this.greenMax = max; return this;
        }

        /**
         * The yellow minimum and maximum, any value between or equal to these two values will be {@link ChatColor#YELLOW}
         * @param min the minimum value for {@link ChatColor#YELLOW}
         * @param max the maximum value for {@link ChatColor#YELLOW}
         */
        public Builder yellow(int min, int max) {
            this.yellowMin = min;
            this.yellowMax = max; return this;
        }

        /**
         * The red minimum and maximum, any value between or equal to these two values will be {@link ChatColor#RED}
         * @param min the minimum value for {@link ChatColor#RED}
         * @param max the maximum value for {@link ChatColor#RED}
         */
        public Builder red(int min, int max) {
            this.redMin = min;
            this.redMax = max; return this;
        }

        /**
         * The dark red minimum, any value greater than or equal to this value will be {@link ChatColor#DARK_RED}
         * @param min the minimum value for {@link ChatColor#DARK_RED}
         */
        public Builder darkRed(int min) {
            this.darkRedMin = min; return this;
        }

        /**
         * Whether the {@code CyberPlayer#getOnlineActions().getColoredPing(PingSettings)} should show the 'ms' at the end, or just return the color and ping
         * @param showMS should {@code CyberPlayer#getOnlineActions().getColoredPing(PingSettings)} show 'ms' at the end
         */
        public Builder showMS(boolean showMS) {
            this.showMS = showMS; return this;
        }

        /**
         * Builds the builder into a {@link PingSettings} instance
         * @return the {@link PingSettings} instance
         * @since 15
         */
        public PingSettings build() { return new PingSettings(this); }
    }

    private final Builder builder;

    /**
     * The {@link PingSettings} instance, created by the {@link Builder} instance
     * @param builder the builder that can then be transformed into {@link PingSettings} and read by CyberAPI
     * @since 15
     */
    public PingSettings(Builder builder) { this.builder = builder; }

    /**
     * Gets the loading maximum that is the highest value when it says 'Loading...' instead of a number (usually 0ms)
     * @return the loading maximum
     * @since 15
     */
    public int getLoadingMaximum() { return builder.loading; }

    /**
     * Gets the minimum amount for the ping to display as {@link ChatColor#GREEN}
     * @return the green minimum
     * @since 15
     */
    public int getGreenMinimum() { return builder.greenMin; }

    /**
     * Gets the maximum amount for the ping to display as {@link ChatColor#GREEN}
     * @return the green maximum
     * @since 15
     */
    public int getGreenMaximum() { return builder.greenMax; }

    /**
     * Gets the minimum amount for the ping to display as {@link ChatColor#YELLOW}
     * @return the yellow minimum
     * @since 15
     */
    public int getYellowMinimum() { return builder.yellowMin; }

    /**
     * Gets the maximum amount for the ping to display as {@link ChatColor#YELLOW}
     * @return the yellow maximum
     * @since 15
     */
    public int getYellowMaximum() { return builder.yellowMax; }

    /**
     * Gets the minimum amount for the ping to display as {@link ChatColor#RED}
     * @return the red minimum
     * @since 15
     */
    public int getRedMinimum() { return builder.redMin; }

    /**
     * Gets the maximum amount for the ping to display as {@link ChatColor#RED}
     * @return the red maximum
     * @since 15
     */
    public int getRedMaximum() { return builder.redMax; }

    /**
     * Gets the minimum amount for the ping to display as {@link ChatColor#DARK_RED}
     * @return the dark red minimum
     * @since 15
     */
    public int getDarkRedMinimum() { return builder.darkRedMin; }

    /**
     * Gets whether {@code CyberPlayer#getOnlineActions().getColoredPing()} should show 'ms' at the end of the {@link String} of colored ping
     * @return should show 'ms' trailing {@link String}
     * @since 15
     */
    public boolean shouldShowMS() { return builder.showMS; }

}
