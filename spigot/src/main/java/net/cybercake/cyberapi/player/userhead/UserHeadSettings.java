package net.cybercake.cyberapi.player.userhead;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * The build version of {@link UserHeadSettingsBuilder}
 * @since 3.0.0
 */
public class UserHeadSettings {

    private final String data;
    private final HashMap<String, Object> values;

    private final int imageScale;
    private final boolean showHelmet;
    private final Character character;
    private final String[] lines;

    private UserHeadSettings() { this.data = null; this.values = null; this.imageScale = 0; this.showHelmet = false; this.character = ' '; this.lines = new String[]{}; }
    public UserHeadSettings(String data) {
        this.data = data;
        this.values = fromData(data);

        this.imageScale = Integer.parseInt(getString("imageScale"));
        this.showHelmet = getBoolean("showHelmet");
        this.character = getString("character").toCharArray()[0];

        String[] lines = getString("lines").equals("NONE") ? new String[]{} : getString("lines").split("%nl%");
        int index = 0;
        for(String line : lines) {
            lines[index] = line.replace("${COMMA}", ","); index++;
        }
        this.lines = lines;
    }

    /**
     * The scale of the image to be placed into the URL when getting the data
     */
    public int getImageScale() { return imageScale; }

    /**
     * Whether the helmet (top layer of skin) should show
     */
    public boolean shouldShowHelmet() { return showHelmet; }

    /**
     * What character should be used for the message in chat
     */
    public Character getCharacter() { return character; }

    /**
     * What the lines next to the character's head should say
     */
    public String[] getLines() { return lines; }

    /**
     * Gets the "stringified" version of the data
     * @return the data in {@link String} form
     */
    public String getData() { return data; }

    /**
     * Gets the values from the {@link HashMap} directly
     * @return the data in {@link HashMap} form
     */
    public HashMap<String, Object> getValues() { return values; }

    /**
     * Get a value from a key
     * @param key the key, will return an object
     * @return the value associated with the key provided
     */
    @Nullable
    public Object get(String key) {
        validateKey(key);
        return values.get(key);
    }

    /**
     * Get a value, in {@link String} form, from a key
     * @param key the key, will return a {@link String}
     * @return the value associated with the key provided, in {@link String} form
     */
    @Nullable
    public String getString(String key) {
        validateKey(key);
        return String.valueOf(values.get(key));
    }

    /**
     * Get a value, in {@link Boolean} form, from a key
     * @param key the key, will return a {@link Boolean}
     * @return the value associated with the key provided, in {@link Boolean} form
     */
    public @NotNull Boolean getBoolean(String key) {
        return Boolean.valueOf(getString(key));
    }

    /**
     * Check if a key exists in the data
     * @param key the key
     * @return a boolean on whether the key exists, 'true' if yes, 'false' if no
     */
    public boolean keyExists(String key) { return values.containsKey(key); }
    private void validateKey(String key) { if(!keyExists(key)) throw new IllegalArgumentException("There is no key '" + key + "' in Settings!"); }

    /**
     * Convert the "stringified" version of the data into a {@link HashMap}
     * @param data the {@link String} version of the data
     * @return a {@link HashMap} with the data
     */
    public HashMap<String, Object> fromData(String data) {
        HashMap<String, Object> values = new HashMap<>();
        data = data.substring(24, data.length()-1);
        for(String value : data.split(", ")) {
            if(value.split("=").length != 2) throw new IllegalStateException("Values must be key-value pairs!");

            values.put(value.split("=")[0], value.split("=")[1]);
        }
        return values;
    }

}
