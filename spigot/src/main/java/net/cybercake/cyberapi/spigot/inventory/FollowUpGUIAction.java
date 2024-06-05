package net.cybercake.cyberapi.spigot.inventory;

import net.cybercake.cyberapi.spigot.inventory.exceptions.HaltConsumerListException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Anything that happens after you use a {@link UpdateGUIAction} (example method from {@link CustomGUI}: {@link UpdateGUIAction#set(int, ItemStack)}).
 * @since 143
 */
@SuppressWarnings({"unused"})
public abstract class FollowUpGUIAction {

    protected Inventory inventory;
    protected CustomGUI gui;
    protected UpdateGUIAction action;

    /**
     * CyberAPI would prefer if you didn't instantiate this class in this way
     */
    @ApiStatus.Internal
    protected FollowUpGUIAction(UpdateGUIAction action) {
        this.action = action;
        this.gui = action.instance();
        this.inventory = gui.getInventory();
    }


    /**
     * When clicked with {@link ClickType certain click type}, do {@link SpecificClickChecker this} (will ask you what to do in this class)
     * @param checkClickType check the click type with this predicate
     * @since 143
     */
    public SpecificClickChecker onClick(Predicate<ClickType> checkClickType) {
        return new SpecificClickChecker(this, checkClickType);
    }

    /**
     * Consumer for the last item you set from {@link UpdateGUIAction} (or in your case: a {@link CustomGUI})
     * @param consumer called whenever any click of any {@link ClickType type} clicks.
     * @since 143
     */
    public FollowUpGUIAction onAnyClick(Consumer<InventoryClickEvent> consumer) {
        return newConsumerItem(consumer);
    }

    /**
     * Consumer for the last item you set from {@link UpdateGUIAction} (or in your case: a {@link CustomGUI})
     * @param consumer called whenever any click is a <strong>{@link ClickType#isMouseClick() mouse click}</strong>.
     * @since 143
     */
    public FollowUpGUIAction onMouseClick(Consumer<InventoryClickEvent> consumer) {
        return newConsumerItem(checkClick(consumer, ClickType::isMouseClick));
    }

    /**
     * Consumer for the last item you set from {@link UpdateGUIAction} (or in your case: a {@link CustomGUI})
     * @param consumer called whenever any click is a <strong>{@link ClickType#isRightClick() right click}</strong>.
     * @since 143
     */
    public FollowUpGUIAction onRightClick(Consumer<InventoryClickEvent> consumer) {
        return newConsumerItem(checkClick(consumer, ClickType::isRightClick));
    }

    /**
     * Consumer for the last item you set from {@link UpdateGUIAction} (or in your case: a {@link CustomGUI})
     * @param consumer called whenever any click is a <strong>{@link ClickType#isLeftClick() left click}</strong>.
     * @since 143
     */
    public FollowUpGUIAction onLeftClick(Consumer<InventoryClickEvent> consumer) {
        return newConsumerItem(checkClick(consumer, ClickType::isLeftClick));
    }

    /**
     * Consumer for the last item you set from {@link UpdateGUIAction} (or in your case: a {@link CustomGUI})
     * @param consumer called whenever any click is a <strong>{@link ClickType#MIDDLE middle click}</strong>.
     * @since 143
     */
    public FollowUpGUIAction onMiddleClick(Consumer<InventoryClickEvent> consumer) {
        return newConsumerItem(checkClick(consumer, type -> type == ClickType.MIDDLE));
    }

    /**
     * Consumer for the last item you set from {@link UpdateGUIAction} (or in your case: a {@link CustomGUI})
     * @param consumer called whenever the user is clicking <strong>{@link ClickType#isShiftClick() while holding sneak (or 'shift')}</strong>.
     * @since 143
     */
    public FollowUpGUIAction onShiftClick(Consumer<InventoryClickEvent> consumer) {
        return newConsumerItem(checkClick(consumer, ClickType::isShiftClick));
    }

    /**
     * Consumer for the last item you set from {@link UpdateGUIAction} (or in your case: a {@link CustomGUI})
     * @param consumer called whenever the user <strong>{@link ClickType#DROP drops an item} or {@link ClickType#CONTROL_DROP drops an item while holding control}</strong>.
     * @since 143
     */
    public FollowUpGUIAction onDrop(Consumer<InventoryClickEvent> consumer) {
        return newConsumerItem(checkClick(consumer, (type) -> List.of(ClickType.DROP, ClickType.CONTROL_DROP).contains(type)));
    }


    /**
     * A condition that modifies any <strong>previous</strong> consumers. This <strong>will not</strong> affect consumers modified after this method is called.
     * @param predicate dictates whether a consumer will run by checking the {@link Player user} that clicked.
     * @since 143
     */
    public FollowUpGUIAction onlyIfPlayer(Predicate<Player> predicate) {
        gui.clickEvents.stream()
                .filter(consumer -> consumer.getObjectHashCode() != null && consumer.getObjectHashCode() == this.hashCode())
                .forEach(consumer -> consumer.setConsumer((e) -> {
                    if (!(e.getWhoClicked() instanceof Player player)) throw new HaltConsumerListException();
                    if (!predicate.test(player)) throw new HaltConsumerListException();
                 }));
        return this;
    }

    /**
     * A condition that modifies any <strong>previous</strong> consumers. This <strong>will not</strong> affect consumers modified after this method is called.
     * @param predicate dictates whether a consumer will run by checking the {@link Integer clicked slot} that the user clicked.
     * @since 143
     */
    public FollowUpGUIAction onlyIfSlot(Predicate<Integer> predicate) {
        gui.clickEvents.stream()
                .filter(consumer -> consumer.getObjectHashCode() != null && consumer.getObjectHashCode() == this.hashCode())
                .forEach(consumer -> consumer.setConsumer((e) -> {
                    if (!predicate.test(e.getRawSlot())) throw new HaltConsumerListException();
                }));
        return this;
    }

    /**
     * A condition that modifies any <strong>previous</strong> consumers. This <strong>will not</strong> affect consumers modified after this method is called.
     * @param slot dictates whether a consumer will run by checking the clicked slot is equal to {@link Integer the slot provided in the parameters} that the user clicked.
     * @since 143
     */
    public FollowUpGUIAction onlyIfSlot(int slot) {
        return onlyIfSlot((integer) -> integer == slot);
    }

    /**
     * A condition that modifies any <strong>previous</strong> consumers. This <strong>will not</strong> affect consumers modified after this method is called.
     * @param theseSlots dictates whether a consumer will run by checking the clicked slot is equal to any of {@link Integer the slots provided in the parameters} that the user may have clicked.
     * @since 143
     */
    public FollowUpGUIAction onlyIfSlots(int[] theseSlots) {
        return onlyIfSlot(integer -> Arrays.stream(theseSlots).anyMatch(integer1 -> integer == integer1));
    }

    /**
     * A condition that modifies any <strong>previous</strong> consumers. This <strong>will not</strong> affect consumers modified after this method is called.
     * @param active a {@link Boolean primitive boolean} object to determine if the previous Consumers will run
     * @since 143
     */
    public FollowUpGUIAction onlyIf(boolean active) {
        gui.clickEvents.stream()
                .filter(consumer -> consumer.getObjectHashCode() != null && consumer.getObjectHashCode() == this.hashCode())
                .forEach(consumer -> consumer.setActive(active));
        return this;
    }



    @ApiStatus.Internal
    private Consumer<InventoryClickEvent> checkClick(Consumer<InventoryClickEvent> consumer, Predicate<ClickType> type) {
        return (e) -> {
            ClickType click = e.getClick();
            if (!type.test(click)) return;
            consumer.accept(e);
        };
    }

    @ApiStatus.Internal
    private FollowUpGUIAction newConsumerItem(Consumer<InventoryClickEvent> newConsumer) {
        this.action.instance().addClickEvents(Collections.singletonList(new GUIConsumer<>(getRealEvent(newConsumer), true, this.hashCode(), this)));
        return this;
    }

    @ApiStatus.Internal
    protected void $clearClickEvents(Predicate<GUIConsumer<InventoryClickEvent>> criteria) {
        this.action.instance().clickEvents.removeAll(
                this.action.instance().clickEvents.stream()
                        .filter(criteria)
                        .toList()
        );
    }

    // should be overridden

    /**
     * Retrieves the event provided by any classes that override this method.
     * @param event the new consumer that should have conditions or otherwise applied to it
     * @return the consumer with conditions and arbitrary code applied that is ready for storage
     * @since 143
     * @apiNote an internal method for the most part
     */
    @ApiStatus.Internal
    protected abstract Consumer<InventoryClickEvent> getRealEvent(Consumer<InventoryClickEvent> event);

    /**
     * Represents the next half of the {@link FollowUpGUIAction#onClick(Predicate) onClickF in FollowUpGUIAction}
     * @since 143
     * @see SpecificClickChecker#then(Consumer)
     */
    public class SpecificClickChecker {
        private final FollowUpGUIAction action;
        private final Predicate<ClickType> clickType;

        @ApiStatus.Internal
        SpecificClickChecker(FollowUpGUIAction action, Predicate<ClickType> clickType) {
            this.action = action;
            this.clickType = clickType;
        }

        /**
         * When clicked with the previously-set {@link ClickType click type}, execute the consumer in the parameters
         * @param consumer the consumer to execute if the user clicked using the correct {@link ClickType click type}
         * @return the instance of {@link FollowUpGUIAction} the user was previously attached to
         * @since 143
         */
        public FollowUpGUIAction then(Consumer<InventoryClickEvent> consumer) {
            this.action.newConsumerItem(getRealEvent(checkClick(consumer, clickType)));
            return this.action;
        }
    }

}
