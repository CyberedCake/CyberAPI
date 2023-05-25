package net.cybercake.cyberapi.spigot.server.placeholderapi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface Placeholder {

    /**
     * Execute a PlaceholderAPI action with an offline player as the main argument
     * @param player the offline player to run the check on
     * @return the result with the parameter that will be applied
     * @since 133
     */
    String run(OfflinePlayer player);

}
