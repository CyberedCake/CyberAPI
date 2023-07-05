package net.cybercake.cyberapi.spigot.inventory.actions;

import net.cybercake.cyberapi.spigot.inventory.FollowUpGUIAction;
import net.cybercake.cyberapi.spigot.inventory.UpdateGUIAction;
import net.cybercake.cyberapi.spigot.inventory.actions.modifiers.ItemAdder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * See {@link SetGUIAction#SetGUIAction(UpdateGUIAction, int[], ItemStack) the constructor} for more information
 * @since 143
 * @author CyberedCake
 */
public class SetGUIAction extends FollowUpGUIAction implements ItemAdder {

    private final ItemStack item;
    private final int[] indices;

    /**
     * This is called when the setItems method is called. This sets an item to all the indices
     * provided by the developer.
     * @param action the parent action
     * @param indices the indices (slots) where the item should be placed
     * @param item the {@link ItemStack item} to be set
     * @since 143
     */
    public SetGUIAction(UpdateGUIAction action, int[] indices, ItemStack item) {
        super(action);
        this.indices = indices;
        this.item = item;

        Arrays.stream(indices).forEach(integer -> this.inventory.setItem(integer, item));
    }

    @Override
    public Consumer<InventoryClickEvent> getRealEvent(Consumer<InventoryClickEvent> event) {
        return (e) -> {
            int clickedItem = e.getSlot();
            if(Arrays.stream(indices).noneMatch(integer -> integer == clickedItem)) return;
            event.accept(e);
        };
    }

    @Override
    public Map<Integer, ItemStack> getAddedItems() { return IntStream.of(indices).boxed().collect(Collectors.toMap((index) -> index, (index) -> item)); }

}