package net.cybercake.cyberapi.spigot.server.placeholderapi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class Placeholder {

    private final String[] params;

    /**
     * @throws UnsupportedOperationException THIS IS NOT SUPPORTED! USE {@link Placeholder#Placeholder(String, String...)}!
     * @since 52
     */
    private Placeholder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new {@link Placeholder} instance
     * @param params the parameters required for this function to be executed
     * @since 52
     */
    public Placeholder(String param, String... params) {
        params = (param + "${split}" + String.join("${split}", params)).split("\\$\\{split}");
        this.params = params;
    }

    public abstract String run(OfflinePlayer player, boolean isOnline);

    public String run(Player player) { return null; }

    /**
     * Gets the parameters for the {@link Placeholder}
     * @return the parameters
     * @since 52
     */
    public String[] getParams() { return params; }

}
