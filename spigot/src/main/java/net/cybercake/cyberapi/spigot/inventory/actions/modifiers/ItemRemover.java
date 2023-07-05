package net.cybercake.cyberapi.spigot.inventory.actions.modifiers;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * A GUI action that removes items.
 * @since 143
 */
public interface ItemRemover {

    /**
     * @return the removed slots and their {@link ItemStack items}
     * @since 143
     */
    Map<Integer, ItemStack> getRemovedItems();

}
