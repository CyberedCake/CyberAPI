package net.cybercake.cyberapi.spigot.server.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.Validators;
import net.cybercake.cyberapi.spigot.server.serverlist.ServerListInfo;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
        if(placeholders == null) placeholders = new Placeholders();
        return placeholders;
    }

    private String identifier = CyberAPI.getInstance().getPluginName().toLowerCase(Locale.ROOT);
    private boolean persist = false;
    private boolean register = true;
    private String requiredPlugin = null;
    private static final List<Placeholder> placeholderList = new ArrayList<>();

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
     * @param placeholder the placeholder to register
     * @since 52
     */
    public void addPlaceholder(Placeholder placeholder) {
        placeholderList.add(placeholder);
    }

    /**
     * Adds a few placeholders to register with PlaceholderAPI
     * @param placeholders the placeholders to register
     * @since 52
     */
    public void addPlaceholder(Placeholder... placeholders) { placeholderList.addAll(List.of(placeholders)); }

    /**
     * Gets all the registered placeholders
     * @return the {@link List} of currently registered {@link Placeholder}s
     * @since 52
     */
    public List<Placeholder> getPlaceholderList() { return placeholderList; }

    /**
     * Gets a registered placeholder from the params it uses
     * @param paramUsed the parameters used by the {@link Placeholder}
     * @return the {@link Placeholder} associated, 'null' if none is found or multiple matches are found
     * @since 52
     */
    public @Nullable Placeholder getPlaceholderFrom(String paramUsed) {
        List<Placeholder> placeholderMatcher =
                getPlaceholderList()
                        .stream()
                        .filter(placeholder -> List.of(placeholder.getParams()).contains(paramUsed))
                        .toList();
        if(placeholderMatcher.size() != 1) return null;
        return placeholderMatcher.get(0);
    }



    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        for(Placeholder placeholder : placeholderList) {
            if(!Arrays.stream(placeholder.getParams()).toList().contains(params)) continue;
            return placeholder.run(player, (player != null && player.isOnline()));
        }
        return null;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        for(Placeholder placeholder : placeholderList) {
            if(!Arrays.stream(placeholder.getParams()).toList().contains(params)) continue;
            return placeholder.run(player);
        }
        return null;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return super.getPlaceholders();
    }

    @Override
    public boolean register() {
        return super.register();
    }
}
