package net.cybercake.cyberapi.spigot.inventory.exceptions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface GUIErrorHandler<E extends Event> {

    void handle(Exception exception,
                E event,
                Player player
    );

}
