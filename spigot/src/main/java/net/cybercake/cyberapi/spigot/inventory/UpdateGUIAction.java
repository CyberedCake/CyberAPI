package net.cybercake.cyberapi.spigot.inventory;

import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.inventory.actions.AddGUIAction;
import net.cybercake.cyberapi.spigot.inventory.actions.ClearGUIAction;
import net.cybercake.cyberapi.spigot.inventory.actions.SetGUIAction;
import net.cybercake.cyberapi.spigot.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.cybercake.cyberapi.spigot.items.ItemCreator.getItemFrom;

/**
 * Represents generic GUI update actions.
 * @since 143
 */
public abstract class UpdateGUIAction {


    private CustomGUI gui = null;
    private Inventory inventory;

    /**
     * Do <strong>NOT</strong> use this method, it will likely throw an {@link IllegalStateException} for you any time you use it anyway.
     * @throws IllegalStateException almost always thrown unless the instance hasn't been created yet (just don't use it, man)
     */
    @ApiStatus.Internal
    @ApiStatus.Obsolete /* only marked to deter usage */
    protected void setInstance(@NotNull CustomGUI gui) throws IllegalStateException {
        if (this.gui != null)
            throw new IllegalStateException("Cannot access this method as it has already been called - " + UpdateGUIAction.class.getCanonicalName() + ".setInstance(" + CustomGUI.class.getCanonicalName() + ")");
        this.gui = gui;
        this.inventory = this.gui.getInventory();
    }

    @ApiStatus.Internal
    CustomGUI instance() { return gui; }

    /**
     * Convert a {@link Material} to a blank {@link ItemStack}
     * @param material the material to convert
     * @return the item stack with no meta
     * @since 143
     * @see UpdateGUIAction#convert(Material[])
     */
    public ItemStack convert(Material material) { return getItemFrom(material); }

    /**
     * Convert an array of {@link Material Materials} to an array of blank {@link ItemStack ItemStacks}
     * @param materials the array of materials to convert
     * @return the item stacks with no meta
     * @since 143
     * @see UpdateGUIAction#convert(Material)
     */
    public ItemStack[] convert(Material[] materials) { return Arrays.stream(materials).map(ItemCreator::getItemFrom).toArray(ItemStack[]::new); }

    @ApiStatus.Internal
    ItemStack addSpecialityDataTo(int[] slots, @NotNull ItemStack old) {
        ItemStack newItem = old.clone();
        ItemMeta meta = newItem.getItemMeta();
        if (meta == null) return newItem;
        meta.getPersistentDataContainer().set(new NamespacedKey(CyberAPI.getInstance(), instance().getClass().getCanonicalName()),
                PersistentDataType.INTEGER_ARRAY,
                slots
                );
        newItem.setItemMeta(meta);
        return newItem;
    }

    /**
     * Sets an item in the custom inventory at certain indices (slots) to an {@link ItemStack}
     * @param indices the indices (slots) to set the {@link ItemStack} in the current inventory to
     * @param item the item to set
     * @since 143
     * @see UpdateGUIAction#set(int[], Material)
     */
    public FollowUpGUIAction set(int[] indices, ItemStack item) {
        return new SetGUIAction(this, indices, addSpecialityDataTo(indices, item));
    }
    /**
     * Sets an item in the custom inventory at certain indices (slots) to a {@link Material}
     * @param indices the indices (slots) to set the {@link Material} in the current inventory to
     * @param material the material to set
     * @since 143
     * @see UpdateGUIAction#set(int[], ItemStack)
     */
    public FollowUpGUIAction set(int[] indices, Material material) {
        return set(indices, convert(material));
    }

    /**
     * Sets an item in the custom inventory at a certain index to an {@link ItemStack}
     * @param index the index to set the {@link ItemStack} in the current inventory to
     * @param item the item to set
     * @since 143
     * @see UpdateGUIAction#set(int, Material)
     */
    public FollowUpGUIAction set(int index, ItemStack item) { return set(new int[]{index}, item); }

    /**
     * Sets an item in the custom inventory at a certain index to a {@link Material}
     * @param index the index to set the {@link Material} in the current inventory to
     * @param material the material to set
     * @since 143
     * @see UpdateGUIAction#set(int, ItemStack)
     */
    public FollowUpGUIAction set(int index, Material material) {
        return set(index, convert(material));
    }

    /**
     * Sets an item in the custom inventory, using a {@link SpecialSlots} instance, to an {@link ItemStack}
     * @param specialSlot the special slots that the item stack should apply to
     * @param item the item to set
     * @since 143
     * @see UpdateGUIAction#set(SpecialSlots, Material)
     */
    public FollowUpGUIAction set(SpecialSlots specialSlot, ItemStack item) { return set(specialSlot.getSlotsForSize(this.inventory.getSize()), item); }

    /**
     * Sets an item in the custom inventory, using a {@link SpecialSlots} instance, to a {@link Material}
     * @param specialSlot the special slots that the material should apply to
     * @param material the material to set
     * @since 143
     * @see UpdateGUIAction#set(SpecialSlots, ItemStack)
     */
    public FollowUpGUIAction set(SpecialSlots specialSlot, Material material) { return set(specialSlot, convert(material)); }

    /**
     * Adds an item to the custom inventory. This will add the item to the next available slot in the inventory starting at slot 0.
     * @param items the item (or items) to add to the custom GUI
     * @since 143
     * @see UpdateGUIAction#add(Material...)
     */
    public FollowUpGUIAction add(ItemStack... items) {
        return new AddGUIAction(this, items);
    }

    /**
     * Adds a material to the custom inventory. This will add the material to the next available slot in the inventory starting at slot 0.
     * @param materials the material (or materials) to add to the custom GUI
     * @since 143
     * @see UpdateGUIAction#add(ItemStack...)
     */
    public FollowUpGUIAction add(Material... materials) { return add(convert(materials)); }

    /**
     * Clears the GUI of <strong>all items and events. <em>Nothing is spared.</em></strong>
     * @since 143
     */
    public void clear() { new ClearGUIAction(this, (ItemStack) null); }

    /**
     * Clears the GUI of all items relating to the {@link ItemStack} and events.
     * @param items the specific items to remove
     * @since 143
     * @see UpdateGUIAction#clear(Material...)
     */
    public void clear(ItemStack... items) { // returning void because FollowUpGUIAction doesn't work with things that remove
        new ClearGUIAction(this, items);
    }

    /**
     * Clears the GUI of all items relating to the {@link Material} and events.
     * @param materials the specific materials to remove
     * @since 143
     * @see UpdateGUIAction#clear(ItemStack...)
     */
    public void clear(Material... materials) { clear(convert(materials)); }

}
