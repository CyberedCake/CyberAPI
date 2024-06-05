package net.cybercake.cyberapi.spigot.inventory;

import net.cybercake.cyberapi.common.basic.Pair;
import net.cybercake.cyberapi.spigot.inventory.exceptions.VariantSpecialSlotsFailed;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a custom GUI. Extend this class in a blank class to create a new GUI using CyberAPI!
 * @since 143
 * @author CyberedCake
 * @see CustomGUI#CustomGUI(int)
 * @see CustomGUI#CustomGUI(int, String)
 * @see CustomGUI#CustomGUI(InventoryType)
 * @see CustomGUI#CustomGUI(InventoryType, String)
 * @see CustomGUI#CustomGUI(Function)
 */
@SuppressWarnings("unused")
public abstract class CustomGUI extends UpdateGUIAction implements InventoryHolder {

    //<editor-fold desc="static">
    /**
     * Represents a Chest inventory type with 3 rows (default: {@link InventoryType#CHEST})
     * @since 143
     * @apiNote constant not accessible outside of class
     */
    static final InventoryType CHEST = InventoryType.CHEST;

    /**
     * Opens a {@link CustomGUI CyberAPI Custom GUI} to a certain {@link Player player}.
     * @param gui the {@link CustomGUI gui} to open
     * @param player the {@link Player player} to open the GUI to
     * @since 143
     * @see CustomGUI#open(Player)
     */
    public static void open(CustomGUI gui, Player player) { gui.open(player); }
    //</editor-fold>

    //<editor-fold desc="package-private variables">
    /**
     * The list of {@link InventoryOpenEvent inventory open events} stored via {@link GUIConsumer the GUI consumer instance}
     * @since 143
     */
    final List<GUIConsumer<InventoryOpenEvent>> openEvents = new ArrayList<>();

    /**
     * The list of {@link InventoryClickEvent inventory click events} stored via {@link GUIConsumer the GUI consumer instance}
     * @since 143
     */
    final List<GUIConsumer<InventoryClickEvent>> clickEvents = new ArrayList<>();

    /**
     * The list of {@link InventoryCloseEvent inventory close events} stored via {@link GUIConsumer the GUI consumer instance}
     * @since 143
     */
    final List<GUIConsumer<InventoryCloseEvent>> closeEvents = new ArrayList<>();

    final Inventory inventory;
    //</editor-fold>

    //<editor-fold desc="constructors">
    /**
     * Create a GUI using a functional interface. It passes in the instance parameter so that
     * {@link Bukkit#createInventory(InventoryHolder, int) Bukkit::createInventory} will be able
     * to correctly mark an {@link InventoryHolder} even though the parameter is created before
     * the instance is.
     * @param createInventory the function, with the instance of {@link CustomGUI this} provided
     *                        and returning a newly-created {@link Inventory inventory}
     * @see CustomGUI#CustomGUI(int)
     * @see CustomGUI#CustomGUI(int, String)
     * @see CustomGUI#CustomGUI(InventoryType)
     * @see CustomGUI#CustomGUI(InventoryType, String)
     * @since 143
     */
    protected CustomGUI(Function<CustomGUI, Inventory> createInventory) {
        this.inventory = createInventory.apply(this);
        setInstance(this); // deprecated to deter usage for normal users -- not actually deprecated/obsolete
    }

    /**
     * Creates a GUI using a size (usually the number of slots). If you want to create a GUI with
     * a set number of rows, simply take the numbers of rows you wish and multiply it by nine. For
     * example: If I want a 5-rowed inventory, I would pass in the argument {@code 9 * 6}, 6 being
     * the number of rows and 9 being the amount of slots per row.
     * @param size the size of the inventory, will ony accept multiples of nine (so just use the
     *             multiplication method used above). Will only accept numbers from zero (0) to
     *             fifty-four (54)
     * @param title the title of the GUI, to use colors make sure you utilize {@link net.cybercake.cyberapi.spigot.chat.UChat#chat UChat}
     * @see CustomGUI#CustomGUI(Function)
     * @see CustomGUI#CustomGUI(int)
     * @see CustomGUI#CustomGUI(InventoryType)
     * @see CustomGUI#CustomGUI(InventoryType, String)
     * @since 143
     */
    protected CustomGUI(int size, @NotNull String title) { this((gui) -> Bukkit.createInventory(gui, size, title)); }

    /**
     * Creates a GUI using an {@link InventoryType inventory type}. Most inventories created in Spigot
     * are using the {@link InventoryType#CHEST chest inventory type}. For {@link InventoryType#CHEST chest inventory types},
     * this method is not suggested, as it presents challenges with changing the amount of rows for the
     * chest. To change the amount of rows, use {@link CustomGUI#CustomGUI(int, String) a different CustomGUI constructor.}
     * @param type the inventory type, the enum {@link InventoryType}
     * @param title the title of the GUI, to use colors make sure you utilize {@link net.cybercake.cyberapi.spigot.chat.UChat#chat(String) UChat}
     * @see CustomGUI#CustomGUI(Function)
     * @see CustomGUI#CustomGUI(int)
     * @see CustomGUI#CustomGUI(int, String)
     * @see CustomGUI#CustomGUI(InventoryType)
     * @since 143
     */
    protected CustomGUI(@NotNull InventoryType type, @NotNull String title) { this((gui) -> Bukkit.createInventory(gui, type, title)); }

    /**
     * Creates a GUI using a specific size. The title will be set to the default title for chests, which
     * is pulled from {@link InventoryType#CHEST} and the method {@link InventoryType#getDefaultTitle() getDefaultTitle()}.
     * If you want to create a GUI with a set number of rows, simply take the numbers of rows you wish and
     * multiply it by nine. For example: If I want a 5-rowed inventory, I would pass in the argument
     * {@code 9 * 6}, 6 being the number of rows and 9 being the amount of slots per row.
     * @param size the size of the inventory, will ony accept multiples of nine (so just use the
     *             multiplication method used above). Will only accept numbers from zero (0) to
     *             fifty-four (54)
     * @see CustomGUI#CustomGUI(Function)
     * @see CustomGUI#CustomGUI(int, String)
     * @see CustomGUI#CustomGUI(InventoryType)
     * @see CustomGUI#CustomGUI(InventoryType, String)
     * @since 143
     */
    protected CustomGUI(int size) {
        this(size, CHEST.getDefaultTitle());
    }

    /**
     * Creates a GUI using an {@link InventoryType inventory type}. Most inventories created in Spigot
     * are using the {@link InventoryType#CHEST chest inventory type}. For {@link InventoryType#CHEST chest inventory types},
     * this method is not suggested, as it presents challenges with changing the amount of rows for the
     * chest. To change the amount of rows, use {@link CustomGUI#CustomGUI(int, String) a different CustomGUI constructor.}
     * @param type the inventory type, the enum {@link InventoryType}
     * @see CustomGUI#CustomGUI(Function)
     * @see CustomGUI#CustomGUI(int)
     * @see CustomGUI#CustomGUI(int, String)
     * @see CustomGUI#CustomGUI(InventoryType, String)
     * @since 143
     */
    protected CustomGUI(@NotNull InventoryType type) {
        this(type, type.getDefaultTitle());
    }
    //</editor-fold>

    //<editor-fold desc="custom handlers and events as consumers">
    /**
     * Adds an open handler to the {@link CustomGUI}. This will only fire when <strong>this</strong> inventory is opened.
     * @param event the handler for opening this custom menu
     * @since 143
     * @see CustomGUI#addClickEvent(Consumer)
     * @see CustomGUI#addCloseEvent(Consumer)
     */
    public void addOpenEvent(Consumer<InventoryOpenEvent> event) { addOpenEvents(Collections.singletonList(GUIConsumer.from(event))); }

    /**
     * Adds a click handler to the {@link CustomGUI}. This will only fire when <strong>this</strong> inventory has an item clicked in it.
     * @param event the handler for clicking in this custom menu
     * @since 143
     * @see CustomGUI#addOpenEvent(Consumer)
     * @see CustomGUI#addCloseEvent(Consumer)
     */
    public void addClickEvent(Consumer<InventoryClickEvent> event) { addClickEvents(Collections.singletonList(GUIConsumer.from(event))); }

    /**
     * Adds a close handler to the {@link CustomGUI}. This will only fire when <strong>this</strong> inventory is closed.
     * @param event the handler for closing this custom menu
     * @since 143
     * @see CustomGUI#addOpenEvent(Consumer)
     * @see CustomGUI#addClickEvent(Consumer)
     */
    public void addCloseEvent(Consumer<InventoryCloseEvent> event) { addCloseEvents(Collections.singletonList(GUIConsumer.from(event))); }

    /**
     * Adds a list of open handlers to the {@link CustomGUI}. These will only fire when <strong>this</strong> inventory is opened.
     * @param events the handlers for opening this custom menu
     * @since 143
     * @see CustomGUI#addClickEvents(List)
     * @see CustomGUI#addCloseEvents(List)
     */
    public void addOpenEvents(List<GUIConsumer<InventoryOpenEvent>> events) { this.openEvents.addAll(events); }

    /**
     * Adds a list of click handlers to the {@link CustomGUI}. These will only fire when <strong>this</strong> inventory has an item clicked in it.
     * @param events the handlers for clicking in this custom menu
     * @since 143
     * @see CustomGUI#addOpenEvents(List)
     * @see CustomGUI#addCloseEvents(List)
     */
    public void addClickEvents(List<GUIConsumer<InventoryClickEvent>> events) { this.clickEvents.addAll(events); }

    /**
     * Adds a list of close handlers to the {@link CustomGUI}. These will only fire when <strong>this</strong> inventory is closed.
     * @param events the handler for closing this custom menu
     * @since 143
     * @see CustomGUI#addOpenEvents(List)
     * @see CustomGUI#addClickEvents(List)
     */
    public void addCloseEvents(List<GUIConsumer<InventoryCloseEvent>> events) { this.closeEvents.addAll(events); }

    /**
     * Clears all added open events. If you used {@link CustomGUI#addOpenEvent(Consumer) addOpenEvent} before, then
     * this method will remove that function and disable its use
     * @since 173
     * @see CustomGUI#clearAllClickEvents()
     * @see CustomGUI#clearAllCloseEvents()
     */
    public void clearAllOpenEvents() { this.openEvents.clear(); }

    /**
     * Clears all added click events. If you used {@link CustomGUI#addClickEvent(Consumer) addClickEvent} before, then
     * this method will remove that function and disable its use
     * @since 173
     * @see CustomGUI#clearAllOpenEvents()
     * @see CustomGUI#clearAllCloseEvents()
     */
    public void clearAllClickEvents() { this.clickEvents.clear(); }

    /**
     * Clears all added close events. If you used {@link CustomGUI#addCloseEvent(Consumer) addCloseEvent} before, then
     * this method will remove that function and disable its use
     * @since 173
     * @see CustomGUI#clearAllOpenEvents()
     * @see CustomGUI#clearAllClickEvents()
     */
    public void clearAllCloseEvents() { this.closeEvents.clear(); }
    //</editor-fold>

    //<editor-fold desc="special slot return types">
    /**
     * @return all slots in the currently opened menu
     * @since 143
     * @see SpecialSlots#ALL
     */
    public int[] all() { return SpecialSlots.ALL.getSlotsForSize(this.getInventory().getSize()); }

    /**
     * @return only the slots on the border of the currently opened menu
     * @since 143
     * @see SpecialSlots#BORDERS
     */
    public int[] borders() { return SpecialSlots.BORDERS.getSlotsForSize(this.getInventory().getSize()); }

    /**
     * @return only the slots for the corners of the currently opened menu
     * @since 143
     * @see SpecialSlots#CORNERS
     */
    public int[] corners() { return SpecialSlots.CORNERS.getSlotsForSize(this.getInventory().getSize()); }

    /**
     * @param variant the checkerboard variant to return
     * @return only the slots for the selected variant of the available checkerboards
     * @since 143
     * @see net.cybercake.cyberapi.spigot.inventory.SpecialSlots.CheckerboardVariant CheckerboardVariant
     * @see CustomGUI#checkerboard()
     */
    public int[] checkerboard(SpecialSlots.CheckerboardVariant variant) { return variant.convert().getSlotsForSize(this.getInventory().getSize()); }

    /**
     * @return the {@link Pair} of both {@link net.cybercake.cyberapi.spigot.inventory.SpecialSlots.CheckerboardVariant checkerboard variants} along
     *         with the slots in which that specific variant affects
     * @since 143
     * @see net.cybercake.cyberapi.spigot.inventory.SpecialSlots.CheckerboardVariant CheckerboardVariant
     * @see CustomGUI#checkerboard(SpecialSlots.CheckerboardVariant)
     */
    public Pair<SpecialSlots.CheckerboardVariant, Integer>[] checkerboard() {
        try {
            return variant(SpecialSlots.CheckerboardVariant.values());
        } catch (Exception exception) {
            // must provide an enum in that class so that VariantSpecialSlotsFailed can do its job
            throw new VariantSpecialSlotsFailed(SpecialSlots.CheckerboardVariant.A, exception);
        }
    }

    /**
     * @param variant the separator variant to return
     * @return only the slots for the selected variant of the available separators
     * @since 143
     * @see net.cybercake.cyberapi.spigot.inventory.SpecialSlots.SeparatorVariant SeparatorVariant
     * @see CustomGUI#separator()
     */
    public int[] separator(SpecialSlots.SeparatorVariant variant) { return variant.convert().getSlotsForSize(this.getInventory().getSize()); }

    /**
     * @return the {@link Pair} of both {@link net.cybercake.cyberapi.spigot.inventory.SpecialSlots.SeparatorVariant separator variants} along
     *         with the slots in which that specific variant affects
     * @since 143
     * @see net.cybercake.cyberapi.spigot.inventory.SpecialSlots.SeparatorVariant SeparatorVariant
     * @see CustomGUI#separator(SpecialSlots.SeparatorVariant)
     */
    public Pair<SpecialSlots.SeparatorVariant, Integer>[] separator() {
        try {
            return variant(SpecialSlots.SeparatorVariant.values());
        } catch (Exception exception) {
            // must provide an enum in that class so that VariantSpecialSlotsFailed can do its job
            throw new VariantSpecialSlotsFailed(SpecialSlots.SeparatorVariant.VERTICAL, exception);
        }
    }

    /**
     * @apiNote an internal method that is used by both {@link CustomGUI#checkerboard()} and {@link CustomGUI#separator()}
     *          to remove any repeated code (mainly by using very basic java reflections)
     * @since 143
     */
    @SuppressWarnings({"unchecked"})
    @ApiStatus.Internal
    private <V extends SpecialSlots.SimplifiedGUIEnums> Pair<V, Integer>[] variant(V[] values) {
        List<Pair<V, Integer>> list = new ArrayList<>();
        for(V v : values) {
            for(int slot : v.convert().getSlotsForSize(this.getInventory().getSize())) {
                list.add(new Pair<>(v, slot));
            }
        }
        return (Pair<V, Integer>[]) list.toArray(Pair[]::new);
    }
    //</editor-fold>

    //<editor-fold desc="item locations and contents">
    /**
     * Gives the ability to find an item at a specific slot integer
     * @param slot the slot number to retrieve the item from
     * @return the {@link ItemStack} at that slot location
     * @since 143
     */
    public ItemStack getItemAt(int slot) { return this.inventory.getItem(slot); }

    /**
     * Gives the ability to find the slots in the custom menu that meet certain criteria in regard to an {@link ItemStack}
     * @param predicate the {@link ItemStack} to filter the slot array by
     * @return the slots where the {@link Predicate} of an {@link ItemStack} is met
     * @since 143
     */
    public int[] getItemsWhere(Predicate<ItemStack> predicate) {
        return IntStream.range(0, this.inventory.getSize())
                .filter(slot -> predicate.test(getItemAt(slot)))
                .toArray();
    }

    /**
     * Gives the ability to find the slots in the custom menu where the parameter {@link ItemStack} matches the
     * menu {@link ItemStack}
     * @param item the {@link ItemStack} that the item in the menu must be equal to in order for the slot integers
     *             to be returned
     * @return the slots where the parameter item is equal to the inventory item
     * @since 143
     */
    public int[] getItemsThatMatch(ItemStack item) { return getItemsWhere((loopItem) -> loopItem.equals(item)); }

    /**
     * Gives the ability to find the slots in the custom menu where the item's material must equal the material inputted
     * @param material the {@link Material} items must be in order to get the slot number on this array
     * @return the slots where the parameter item is equal to the inventory item
     * @since 143
     */
    public int[] getItemsThatMatch(Material material) { return getItemsWhere((item) -> item.getType() == material); }

    /**
     * Allows you to retrieve all contents of the inventory in its current state.
     * @return the current contents of the inventory. The first item in the {@link Map map} is the {@link Integer slot number}
     *         in which the item rests. The second item in the {@link Map map} is the {@link ItemStack item stack} data itself.
     * @since 143
     */
    public Map<Integer, ItemStack> getContents() {
        return IntStream.range(0, this.getInventory().getSize())
            .boxed()
            .filter(slot -> this.getItemAt(slot) != null)
            .collect(Collectors.toMap((slot) -> slot, this::getItemAt));
    }
    //</editor-fold>

    //<editor-fold desc="normal event calls and managers">
    /**
     * Called whenever <strong>this</strong> inventory is opened
     * @param event the event that was fired
     * @param player the player that opened the inventory
     * @since 143
     */
    public void onInventoryOpen(@NotNull InventoryOpenEvent event, Player player) { throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " has not implemented onInventoryOpen(InventoryOpenEvent, Player)."); }
    @ApiStatus.Internal void open(InventoryOpenEvent event, Player player) {
        try {
            onInventoryOpen(event, player);
        } catch (UnsupportedOperationException ignored) { } // no implementation

        this.openEvents.stream()
                .filter(Objects::nonNull)
                .filter(GUIConsumer::isActive)
                .forEach(consumer -> consumer.getConsumer().accept(event));
    }

    /**
     * Called whenever <strong>this</strong> inventory is clicked on by a player
     * @param event the event that was fired
     * @param player the player that clicked in the inventory
     * @since 143
     */
    public void onInventoryClick(@NotNull InventoryClickEvent event, Player player) { throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " has not implemented onInventoryClick(InventoryClickEvent, Player)."); }
    @ApiStatus.Internal void click(InventoryClickEvent event, Player player) {
        try {
            onInventoryClick(event, player);
        } catch (UnsupportedOperationException ignored) { } // no implementation

        this.clickEvents.stream()
                .filter(Objects::nonNull)
                .filter(GUIConsumer::isActive)
                .forEach(consumer -> consumer.getConsumer().accept(event));
    }

    /**
     * Called whenever <strong>this</strong> inventory is opened
     * @param event the event that was fired
     * @param player the player that opened the inventory
     * @since 143
     */
    public void onInventoryClose(@NotNull InventoryCloseEvent event, Player player) { throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " has not implemented onInventoryClose(InventoryCloseEvent, Player)."); }
    @ApiStatus.Internal void close(InventoryCloseEvent event, Player player) {
        try {
            onInventoryClose(event, player);
        } catch (UnsupportedOperationException ignored) { } // no implementation

        this.closeEvents.stream()
                .filter(Objects::nonNull)
                .filter(GUIConsumer::isActive)
                .forEach(consumer -> consumer.getConsumer().accept(event));
    }
    //</editor-fold>

    //<editor-fold desc="other public methods">
    /**
     * Opens the inventory for a specific player. <br>
     * <h2>Note for implementers:</h2>
     * It is more convenient if your class that extends {@link CustomGUI} instead calls this method
     * whenever it is instantiated and instead ask for the player in the constructor. <br> This also allows
     * you to modify the inventory based on the player opening the menu instead of just creating a generic
     * menu. <br> Also, while this is a convenient option, <strong><em>it is not required of you to do so.</em></strong> It
     * is just a suggestion that would also allow per-player based inventories.
     * @param player the player that will open the inventory.
     * @since 143
     */
    public void open(Player player) { player.openInventory(this.inventory); }

    /**
     * Gets the {@link Inventory Bukkit version of this inventory}
     * @return the Bukkit inventory
     * @since 143
     * @apiNote it is recommended that you not modify the inventory using this method, as CyberAPI offers more advanced options. However,
     *          you should know that it would have no adverse affects by modifying it this way instead, you would just be losing out
     *          on some cool features {@code =D}
     */
    @NotNull @Override public org.bukkit.inventory.Inventory getInventory() { return this.inventory; }
    //</editor-fold>

}