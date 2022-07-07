package net.cybercake.cyberapi.common.builders.player;

public class UserHeadSettings {

    /**
     * Creates a new {@link Builder} instance, which then the method {@link Builder#build} can build into a {@link UserHeadSettings} instance
     * @return the Builder instance
     * @since 15
     */
    public static Builder builder() { return new Builder(); }

    /**
     * @since 15
     */
    public static class Builder {
        private int imageScale;
        private boolean showHelmet;
        private Character character;
        private String[] lines;

        /**
         * Creates a new {@link Builder} instance, which then the method {@link Builder#build()} can build into a {@link UserHeadSettings}
         * @since 15
         * @deprecated use {@link UserHeadSettings#builder()} instead
         */
        @Deprecated
        public Builder() {
            this.imageScale = 8;
            this.showHelmet = false;
            this.character = '\u281B';
            this.lines = new String[]{};
        }

        /**
         * The scale of the image to be placed into the URL when getting the data
         * <br> <br>
         * <em>Default Value:</em> {@code 8}
         * @param imageScale the scale of the image
         */
        public Builder imageScale(int imageScale) {
            this.imageScale = imageScale; return this;
        }

        /**
         * Whether the helmet (top layer of skin) should show
         * <br> <br>
         * <em>Default Value:</em> {@code false}
         * @param showHelmet should the helmet show
         */
        public Builder showHelmet(boolean showHelmet) {
            this.showHelmet = showHelmet; return this;
        }

        /**
         * What character should be used for the message in chat
         * <br> <br>
         * <em>Default Value:</em> {@code ⬛}
         * @param character the character that should be used in chat
         */
        public Builder character(Character character) {
            this.character = character; return this;
        }

        /**
         * What the lines next to the character's head should say
         * <br> <br>
         * <em>Default Value:</em> {@code **empty**}
         * @param lines the lines to print next to the character's head
         */
        public Builder lines(String... lines) {
            this.lines = lines; return this;
        }

        /**
         * Builds the builder into an {@link UserHeadSettings} instance
         * @return the {@link UserHeadSettings} instance
         * @since 15
         */
        public UserHeadSettings build() { return new UserHeadSettings(this); }
    }

    private final Builder builder;

    /**
     * The {@link UserHeadSettings} instance, created by the {@link Builder} intsance
     * @param builder the builder that can then be transformed into {@link UserHeadSettings} and read by CyberAPI
     * @since 15
     */
    public UserHeadSettings(Builder builder) {
        this.builder = builder;
    }

    /**
     * Gets the scale of the image to be placed in the URL when getting the data
     * @return the image scale
     * @since 15
     */
    public int getImageScale() { return builder.imageScale; }

    /**
     * Gets whether UserHead should show the outer layer of the player's skin
     * @return should show helmet
     * @since 15
     */
    public boolean shouldShowHelmet() { return builder.showHelmet; }

    /**
     * Gets the character that will be used to fill the player head in the chat
     * @return the character
     * @since 15
     */
    public Character getCharacter() { return builder.character; }

    /**
     * Gets the lines that will be to the side of the player head in chat
     * @return the lines
     * @since 15
     */
    public String[] getLines() { return builder.lines; }

}
