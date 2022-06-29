package net.cybercake.cyberapi.server.serverlist.motd;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import net.cybercake.cyberapi.chat.UChat;
import net.cybercake.cyberapi.chat.centered.CenteredMessage;
import net.cybercake.cyberapi.chat.centered.TextType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * The built version of {@link MOTDBuilder}
 * @since 3.1.0
 */
public class MOTD {

    private final String data;
    private final HashMap<String, Object> values;

    private MOTD() { this.data = null; this.values = null; }
    public MOTD(String data) {
        this.data = data;
        this.values = fromData(data);
    }

    /**
     * Returns the ID provided by {@link MOTDBuilder}
     * @return the ID
     */
    public String getID() { return getString("id"); }

    /**
     * Returns the MOTD in String form
     * @return the MOTD
     */
    public String getStringMOTD() { return getString("motd"); }

    /**
     * Returns whether the MOTD should be centered or not
     * @return should be centered
     */
    public boolean shouldBeCentered() { return getBoolean("centered"); }

    /**
     * Returns whether the MOTD should use MiniMessage parsing
     * @return should use MiniMessage
     */
    public boolean shouldUseMiniMessage() { return getBoolean("useMiniMessage"); }

    /**
     * Returns the {@link File} at which the server icon is stored for this MOTD
     * @return the server icon
     */
    public File getIconFile() { return (getString("iconDir").equalsIgnoreCase("unset") ? null : new File(getString("iconDir"))); }

    /**
     * Returns the {@link CachedServerIcon} at which the server icon is stored for this MOTD
     * @return the server icon
     */
    public CachedServerIcon getIcon() {
        if(getIconFile() == null) return Bukkit.getServerIcon();
        try {
            return Bukkit.loadServerIcon(getIconFile());
        } catch (Exception exception) {
            return Bukkit.getServerIcon();
        }
    }

    /**
     * Gets a formatted MOTD
     * @return the formatted {@link String} MOTD
     */
    public String getFormattedMOTD() {
        boolean centered = shouldBeCentered();
        boolean mini = shouldUseMiniMessage(); // cancels out centered
        String text = getStringMOTD();

        if(mini) return UChat.chat(LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize(text)));
        else if(centered) return new CenteredMessage(text, TextType.MOTD).getString(CenteredMessage.Method.TWO);
        return UChat.chat(text);
    }

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
     * Check if a key exists in the data
     * @param key the key
     * @return a boolean on whether the key exists, 'true' if yes, 'false' if no
     */
    public boolean keyExists(String key) { return values.containsKey(key); }
    private void validateKey(String key) { if(!keyExists(key)) throw new IllegalArgumentException("There is no key '" + key + "' in " + this.getClass().getSimpleName() + "!"); }


    /**
     * Convert the "stringified" version of the data into a {@link HashMap}
     * @param data the {@link String} version of the data
     * @return a {@link HashMap} with the data
     */
    public HashMap<String, Object> fromData(String data) {
        HashMap<String, Object> values = new HashMap<>();
        data = data.substring((MOTDBuilder.class.getSimpleName() + "{").length(), data.length()-1);
        for(String value : data.split(", ")) {
            if(value.split("=").length != 2) throw new IllegalStateException("Values must be key-value pairs!");

            values.put(value.split("=")[0], value.split("=")[1].replace("${EQUALS}", "=").replace("${COMMA}", ","));
        }
        return values;
    }

}
