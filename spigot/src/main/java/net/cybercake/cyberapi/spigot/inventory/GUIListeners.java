package net.cybercake.cyberapi.spigot.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * An internal CyberAPI class.
 * @since 143
 */
@ApiStatus.Internal
public class GUIListeners implements Listener {

    @ApiStatus.Internal
    public GUIListeners() { }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event){
        if(!(event.getPlayer() instanceof Player player)) return;
        if(!(event.getInventory().getHolder() instanceof CustomGUI gui)) return;

        gui.open(event, player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;
        if(!(event.getInventory().getHolder() instanceof CustomGUI gui)) return;

        event.setCancelled(true);
        gui.click(event, player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getPlayer() instanceof Player player)) return;
        if(!(event.getInventory().getHolder() instanceof CustomGUI gui)) return;

        gui.close(event, player);
    }

}