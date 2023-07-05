package net.cybercake.cyberapi.spigot.inventory.actions.modifiers;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * A GUI action that adds items.
 * @since 143
 */
public interface ItemAdder {

    /**
     * @return the set slots and their {@link ItemStack items}
     * @since 143
     */
    Map<Integer, ItemStack> getAddedItems();

}
