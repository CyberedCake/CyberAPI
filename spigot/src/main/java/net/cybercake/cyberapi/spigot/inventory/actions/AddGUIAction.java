package net.cybercake.cyberapi.spigot.inventory.actions;

import net.cybercake.cyberapi.spigot.inventory.FollowUpGUIAction;
import net.cybercake.cyberapi.spigot.inventory.UpdateGUIAction;
import net.cybercake.cyberapi.spigot.inventory.actions.modifiers.ItemAdder;
import net.cybercake.cyberapi.spigot.inventory.pagination.NaturallyPaginatedGUI;
import net.cybercake.cyberapi.spigot.inventory.pagination.PaginatedGUI;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * See {@link AddGUIAction#AddGUIAction(UpdateGUIAction, ItemStack...) the constructor} for more information
 * @since 143
 * @author CyberedCake
 */
public class AddGUIAction extends FollowUpGUIAction implements ItemAdder {

    private final Map<Integer, ItemStack> map;

    /**
     * This is called when the addItems method is called. This adds items to the GUI by finding the next available
     * slot and adding to that.
     * @param action the parent action
     * @param items the items to be added
     * @since 143
     */
    public AddGUIAction(UpdateGUIAction action, ItemStack... items) {
        super(action);

        this.map = new HashMap<>(this.gui.getInventory().addItem(items));
    }

    /**
     * This is called when the addItemsForPagination method is called. This adds items to a hashmap
     * in an implementation class.
     * @param gui the parent action
     * @param items the items to be added
     * @since 148
     */
    public AddGUIAction(PaginatedGUI gui, Map<Integer, ItemStack> items) {
        super(gui.action());

        this.map = new HashMap<>(items);
    }

    @Override
    protected Consumer<InventoryClickEvent> getRealEvent(Consumer<InventoryClickEvent> event) {
        return (e) -> {
            int clickedItem = e.getSlot();
            if(map.keySet().stream().noneMatch(slot -> clickedItem == slot)) return;
            event.accept(e);
        };
    }

    @Override
    public Map<Integer, ItemStack> getAddedItems() { return this.map; }

}