package net.cybercake.cyberapi.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public interface CommonManager {

    /**
     * Gets a player's {@link UUID} from a given {@link String} username
     * <br>
     * <b>Note: This is obtaining the {@link UUID} from a URL, meaning you should cache this or use asynchronous events</b>
     * @param name the name to retrieve the UUID from
     * @return the UUID associated with the name
     * @since 3.0.0
     */
    default UUID getUUID(String name) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            String strUUID = (((JsonObject) JsonParser.parseReader(reader)).get("id")).toString().replaceAll("\"", "");
            strUUID = strUUID.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            reader.close();
            return UUID.fromString(strUUID);
        } catch (ClassCastException | IOException exception) {
            throw new IllegalArgumentException("Unable to get the UUID of " + name, exception);
        }
    }

    /**
     * Gets a player's {@link String} username from a given {@link UUID}
     * <br>
     * <b>Note: This is obtaining the {@link UUID} from a URL, meaning you should cache this or use asynchronous events</b>
     * @param uuid the uuid to retrieve the name from
     * @return the name associated with the UUID
     * @since 3.0.0
     */
    default String getName(UUID uuid) {
        try {
            String name;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid).openStream()));
            name = (((JsonObject)JsonParser.parseReader(reader)).get("name")).toString().replaceAll("\"", "");
            reader.close();
            return name;
        } catch (ClassCastException | IOException exception) {
            throw new IllegalArgumentException("Unable to get the username of " + uuid.toString(), exception);
        }
    }

}
