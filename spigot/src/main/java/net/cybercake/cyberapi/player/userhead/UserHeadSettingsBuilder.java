package net.cybercake.cyberapi.player.userhead;

/**
 * The builder for {@link UserHeadSettings}
 * @since 3.0.0
 */
public class UserHeadSettingsBuilder {

    private int imageScale;
    private boolean showHelmet;
    private Character character;
    private String[] lines;

    /**
     * Creates a new instance of {@link UserHeadSettingsBuilder} to be used by {@link UserHeadSettings} when built with {@link UserHeadSettingsBuilder#build()}
     */
    public UserHeadSettingsBuilder() {
        this.imageScale = 8;
        this.showHelmet = false;
        this.character = '\u2B1B';
        this.lines = new String[]{};
    }

    /**
     * The scale of the image to be placed into the URL when getting the data
     * <br> <br>
     * <em>Default Value:</em> {@code 8}
     * @param imageScale the scale of the image
     */
    public UserHeadSettingsBuilder imageScale(int imageScale) {
        this.imageScale = imageScale; return this;
    }

    /**
     * Whether the helmet (top layer of skin) should show
     * <br> <br>
     * <em>Default Value:</em> {@code false}
     * @param showHelmet should the helmet show
     */
    public UserHeadSettingsBuilder showHelmet(boolean showHelmet) {
        this.showHelmet = showHelmet; return this;
    }

    /**
     * What character should be used for the message in chat
     * <br> <br>
     * <em>Default Value:</em> {@code ⬛}
     * @param character the character that should be used in chat
     */
    public UserHeadSettingsBuilder character(Character character) {
        this.character = character; return this;
    }

    /**
     * What the lines next to the character's head should say
     * <br> <br>
     * <em>Default Value:</em> {@code **empty**}
     * @param lines the lines to print next to the character's head
     */
    public UserHeadSettingsBuilder lines(String... lines) {
        this.lines = lines; return this;
    }

    /**
     * Builds the settings into a {@link UserHeadSettings} object
     */
    public UserHeadSettings build() {
        return new UserHeadSettings(this.toString());
    }

    /**
     * Builds into a blank {@link UserHeadSettings} object that contains the default values
     */
    public UserHeadSettings buildEmpty() {
        return new UserHeadSettingsBuilder().build();
    }

    /**
     * Returns a string form of the data here. This is what {@link UserHeadSettings} uses.
     * @return string of the data, looks like -> UserHeadSettings{key=value, key1=value1}
     */
    public String toString() {
        return "UserHeadSettingsBuilder{" +
                attachValue("imageScale", imageScale) +
                attachValue("showHelmet", showHelmet) +
                attachValue("character", character.toString()) +
                attachValue("lines", (String.join("!", lines).strip().equals("") ? "NONE" : String.join("%nl%", lines)).replace(",", "${COMMA}"), true);
    }

    private String attachValue(String name, Object value) { return name + "=" + value + ", "; }
    private String attachValue(String name, Object value, boolean last) { return attachValue(name, value).substring(0, last ? attachValue(name, value).length()-2 : 0) + (last ? "}" : ""); }

}
