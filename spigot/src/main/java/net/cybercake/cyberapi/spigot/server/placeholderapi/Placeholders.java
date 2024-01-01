package net.cybercake.cyberapi.spigot.server.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.Validators;
import net.cybercake.cyberapi.spigot.server.serverlist.ServerListInfo;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Placeholders extends PlaceholderExpansion {

    /**
     * @deprecated Please use {@link ServerListInfo#serverListInfo()} or {@link CyberAPI#getPlaceholders()}
     */
    @SuppressWarnings({"all"})
    @Deprecated
    public Placeholders() { }

    private static Placeholders placeholders = null;

    /**
     * Gets an instance of {@link Placeholders}
     * @return the {@link Placeholders} instance
     * @since 52
     */
    public static Placeholders placeholders() {
        Validators.validatePlaceholderAPIHook();
        if (placeholders == null) placeholders = new Placeholders();
        return placeholders;
    }

    private String identifier = CyberAPI.getInstance().getPluginName().toLowerCase(Locale.ROOT);
    private boolean persist = false;
    private boolean register = true;
    private String requiredPlugin = null;
    private static final List<StoredPlaceholder> placeholderList = new ArrayList<>();

    /**
     * Gets the identifier of the PlaceholderAPI expansion, which is usually your plugin's name in all lower case, but if you use {@link Placeholders#setIdentifier(String)} you can set a custom identifier
     * @return the version
     * @since 52
     */
    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    /**
     * Sets the identifier of the PlaceholderAPI expansion, which is the pre-underscore information (example: "yourplugin_param1" or "yourplugin_param2")
     * @param identifier the new identifier
     * @since 52
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * (basically) Gets the authors of the plugin
     * @return the author (or authors)
     * @since 52
     */
    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", CyberAPI.getInstance().getDescription().getAuthors());
    }

    /**
     * (basically) Gets the version of the plugin
     * @return the version
     * @since 52
     */
    @Override
    public @NotNull String getVersion() {
        return CyberAPI.getInstance().getDescription().getVersion();
    }

    /**
     * @since 52
     * @see Placeholders#getRequiredPlugin()
     */
    public void setRequiredPlugin(String requiredPlugin) {
        this.requiredPlugin = requiredPlugin;
    }

    /**
     * @since 52
     * @see Placeholders#setRequiredPlugin(String)
     */
    @Override
    public @Nullable String getRequiredPlugin() {
        return this.requiredPlugin;
    }

    /**
     * @see Placeholders#persist(boolean)
     * @since 52
     */
    @Override
    public boolean persist() {
        return this.persist;
    }

    /**
     * @since 52
     * @see Placeholders#persist()
     */
    public void persist(boolean persist) {
        this.persist = persist;
    }

    /**
     * @see Placeholders#canRegister(boolean)
     * @since 52
     */
    @Override
    public boolean canRegister() {
        return this.register;
    }

    /**
     * @since 52
     * @see Placeholders#canRegister()
     */
    public void canRegister(boolean register) {
        this.register = register;
    }

    /**
     * Adds a placeholder to register with PlaceholderAPI
     * @param parameter the parameter that will invoke the {@link Placeholder} (i.e., "%identifier_parameter%")
     * @param placeholder the placeholder's functionality to register which executes if the parameter is called
     * @since 52
     * @see Placeholders#addPlaceholder(String[], Placeholder) 
     * @see Placeholders#addPlaceholder(List, Placeholder) 
     */
    public StoredPlaceholder addPlaceholder(String parameter, Placeholder placeholder) {
        return addPlaceholder(Collections.singletonList(parameter), placeholder);
    }

    /**
     * Adds a few placeholders to register with PlaceholderAPI
     * @param parameters the parameters that will invoke the {@link Placeholder} (i.e., "%identifier_parameter% AND %identifier_parameter2%")
     * @param placeholder the placeholder's functionality to register which executes if any of the parameters are called
     * @since 133
     * @see Placeholders#addPlaceholder(String, Placeholder) 
     * @see Placeholders#addPlaceholder(List, Placeholder) 
     */
    public StoredPlaceholder addPlaceholder(String[] parameters, Placeholder placeholder) {
        return addPlaceholder(Arrays.stream(parameters).toList(), placeholder);
    }
    
    /**
     * Adds a few placeholders to register with PlaceholderAPI
     * @param parameters the parameters that will invoke the {@link Placeholder} (i.e., "%identifier_parameter% AND %identifier_parameter2%")
     * @param placeholder the placeholder's functionality to register which executes if any of the parameters are called
     * @since 52
     * @see Placeholders#addPlaceholder(String, Placeholder) 
     * @see Placeholders#addPlaceholder(String[], Placeholder) 
     */
    public StoredPlaceholder addPlaceholder(List<String> parameters, Placeholder placeholder) {
        StoredPlaceholder storedPlaceholder = new StoredPlaceholder(parameters, placeholder);
        placeholderList.add(storedPlaceholder);
        return storedPlaceholder;
    }

    /**
     * Gets all the registered placeholders
     * @return the {@link List} of currently registered {@link StoredPlaceholder}s
     * @since 52
     */
    public List<StoredPlaceholder> getPlaceholderList() { return placeholderList; }


    // for offline users
    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        for(StoredPlaceholder placeholder : placeholderList) {
            if (!placeholder.parameters().contains(params)) continue;
            return placeholder.placeholder().run(player);
        }
        return null;
    }

    // for online users
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return null;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return super.getPlaceholders();
    }

    @Override
    public boolean register() {
        this.unregister();
        return super.register();
    }
}
