package net.cybercake.cyberapi.server;

import net.cybercake.cyberapi.server.serverlist.ServerListInfoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CyberAPIListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ServerListInfoListener.users.put(event.getPlayer().getAddress().getAddress(), event.getPlayer());
    }

}
