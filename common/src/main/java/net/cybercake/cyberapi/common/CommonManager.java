package net.cybercake.cyberapi.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.util.internal.UnstableApi;
import net.cybercake.cyberapi.common.basic.logs.Logs;
import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.cybercake.cyberapi.common.server.ConsoleModifiers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public interface CommonManager {

    String THREE_SEPARATION_CHARACTERS = "\uE22B\uE22B\uE22B";

    /**
     * Registers {@link ConsoleModifiers}
     * <br>
     * <b>I would not recommend calling this method yourself, just let CyberAPI handle it!</b>
     * @deprecated please do not use this method, just what-so-ever, let CyberAPI handle it
     */
    @ApiStatus.Internal
    @SuppressWarnings({"all"})
    default void registerLog4jModifiers() {
        try {
            ((Logger) LogManager.getRootLogger()).addFilter(new ConsoleModifiers(this));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to register Log4J modified in CyberAPI! This is CyberAPI's fault.", exception);
        }
    }

    /**
     * Creates a log file that you can then add logs to
     * @param id the ID of the {@link Logs} instance
     * @param fileNameWithoutExtension the name of the file without the extension
     * @return the {@link Logs} instance
     * @since 43
     */
    default Logs createOrGetLogs(String id, String fileNameWithoutExtension) {
        throw new UnsupportedOperationException("createLogs(String, String) is not currently supported with your server type!");
    }

    /**
     * Creates a log file that you can then add logs to
     * @param id the ID of the {@link Logs} instance
     * @param file the {@link File} where the logs are housed
     * @return the {@link Logs} instance
     * @since 43
     */
    @SuppressWarnings({"deprecation"})
    default Logs createOrGetLogs(String id, File file) {
        return (Logs.getFromID(id) != null ? new Logs(id, file) : Logs.getFromID(id));
    }

    /**
     * Gets a player's {@link UUID} from a given {@link String} username
     * <br>
     * <b>Note: This is obtaining the {@link UUID} from a URL, meaning you should cache this or use asynchronous events</b>
     * @param name the name to retrieve the UUID from
     * @return the UUID associated with the name
     * @since 1
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
     * @since 1
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

    /**
     * Gets the settings that were set in CyberAPI when the server was initialized.
     * @return the {@link Settings} object that was used to start CyberAPI
     * @since 181
     * @throws UnsupportedOperationException if your API-type does not support this method
     */
    default Settings getSettings() {
        throw new UnsupportedOperationException("getSettings() is currently not supported with your server type!");
    }

}
