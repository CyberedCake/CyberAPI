package net.cybercake.cyberapi.spigot.inventory.actions;

import net.cybercake.cyberapi.spigot.inventory.FollowUpGUIAction;
import net.cybercake.cyberapi.spigot.inventory.UpdateGUIAction;
import net.cybercake.cyberapi.spigot.inventory.actions.modifiers.ItemAdder;
import net.cybercake.cyberapi.spigot.inventory.actions.modifiers.ItemRemover;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * See {@link ClearGUIAction#ClearGUIAction(UpdateGUIAction, ItemStack...) the constructor} for more information
 * @since 143
 * @author CyberedCake
 */
public class ClearGUIAction extends FollowUpGUIAction implements ItemRemover {

    private final Map<Integer, ItemStack> map;

    /**
     * This is called when the clearItems method is called. This clears items from the GUI by finding any items
     * relating to the items in the parameter and subtracting them from the menu. If the items provided in the
     * parameters are null, then the action will clear <strong>all items</strong> from the menu.
     * @param action the parent action
     * @param items the items to be removed, or null for all items
     * @since 143
     */
    public ClearGUIAction(UpdateGUIAction action, @Nullable ItemStack... items) {
        super(action);

        if (items == null) { // CLEARS EVERYTHING
            this.map = new HashMap<>(this.gui.getContents()); // literally removing everything so just put this lol
            this.gui.getInventory().clear();
            $clearClickEvents((consumer) -> true);
            return;
        }

        this.map = new HashMap<>(this.gui.getInventory().removeItem(items));
        $clearClickEvents((consumer) ->
                consumer.getExtraInformation() instanceof FollowUpGUIAction followUp && followUp instanceof ItemAdder
        );
    }

    @Override
    protected Consumer<InventoryClickEvent> getRealEvent(Consumer<InventoryClickEvent> event) { return null; }

    @Override
    public Map<Integer, ItemStack> getRemovedItems() { return this.map; }

}