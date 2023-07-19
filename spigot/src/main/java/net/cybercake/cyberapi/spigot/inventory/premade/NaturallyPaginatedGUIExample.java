package net.cybercake.cyberapi.spigot.inventory.premade;

import net.cybercake.cyberapi.spigot.inventory.pagination.NaturallyPaginatedGUI;
import net.cybercake.cyberapi.spigot.items.ItemCreator;
import org.bukkit.Material;

public class NaturallyPaginatedGUIExample extends NaturallyPaginatedGUI {
    public NaturallyPaginatedGUIExample() {
        super(9, "Example of Pagination");

        setNaturalSlots(new int[]{2, 3, 4, 5});

        set(nextSlot(), ItemCreator.createItem(Material.STONE).name("Test").build());
        set(nextSlot(), ItemCreator.createItem(Material.DIAMOND).name("&bTest 2").build());
        set(nextSlot(), ItemCreator.createItem(Material.GOLD_INGOT).name("&6gold").build());
        set(nextSlot(), ItemCreator.createItem(Material.FIRE_CHARGE).name("&cfire").build());
    }
}
