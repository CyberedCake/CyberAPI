package net.cybercake.cyberapi.spigot.inventory.premade;

import com.google.common.base.Preconditions;
import net.cybercake.cyberapi.common.basic.Pair;
import net.cybercake.cyberapi.common.basic.StringUtils;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.inventory.CustomGUI;
import net.cybercake.cyberapi.spigot.inventory.SpecialSlots;
import net.cybercake.cyberapi.spigot.inventory.utils.InventoryUtils;
import net.cybercake.cyberapi.spigot.items.ItemCreator;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a pre-made Custom GUI that allows you to open a menu of custom size that shows you the integer
 * of each slot to allow for easier GUI development. It also shows you the custom {@link SpecialSlots special slots locations}. <br> <br>
 * This menu can serve as an example to people wanting to figure out how to use CyberAPI's {@link CustomGUI} system <em>(note for people looking
 * for an example menu: you do not have to add javadoc comments to your GUI, this is just a CyberAPI convention)</em>, or it can be used literally
 * in-game! Simply call one of the constructors to open a menu for a {@link Player}. If you specify an {@link Integer} at the end of one of
 * the constructors, it will consider it the highlighted slot and flash it.
 * @since 143
 * @see GUISlots#GUISlots(Player, int, Integer)
 * @see GUISlots#GUISlots(Player, int)
 */
public class GUISlots extends CustomGUI {

    //<editor-fold desc="static">

    static String SPACE         = "   ";
    static long CLICK_COOLDOWN  = 1_000L; // in milliseconds

    //</editor-fold>

    //<editor-fold desc="constructors and variables">
    private final Player player;
    private final Map<Integer, Long> lastClickedSlot = new HashMap<>(); // to prevent click-spamming of an item (1 second cooldown)
    private final @Nullable BukkitTask task;

    /**
     * Creates a GUISlots menu and opens it for the {@link Player}
     * @param player the player to open the menu for
     * @param rows the amount of rows in the menu, any number between 1 - 6
     * @since 143
     * @see GUISlots#GUISlots(Player, int, Integer)
     */
    public GUISlots(@NotNull Player player, @IntRange(from = 1, to = 6) int rows) { this(player, rows, null); }

    /**
     * Creates a GUISlots menu and opens it for the {@link Player}
     * @param player the player to open the menu for
     * @param rows the amount of rows in the menu, any numbers between 1 - 6
     * @param highlight the slot to highlight in the menu. This means the item will flash in color and name to show the user
     *                  its location in the menu. Any number between 0 and the row number times 9
     * @since 143
     */
    public GUISlots(@NotNull Player player, @IntRange(from = 1, to = 6) int rows, @Nullable @IntRange(from = 0, to = 53) Integer highlight) {
        super(9 * rows, rows + " " + StringUtils.pluralize("row!s", rows) + ", size of " + (9 * rows));
        if(highlight != null)
            Preconditions.checkArgument(IntStream.range(0, 9*rows).anyMatch(slot -> slot == highlight), "Highlight must be within the GUI range. Must be between 0 - " + (9*rows) + ".");
        this.player = player;

        // all items
        for(int index : all())
            this.set(index,
                            ItemCreator.createItem(Material.BLACK_STAINED_GLASS_PANE)
                                    .name(colorFromIndex(index) + "Slot >> #" + index)
                                    .addLore("&bApart of:")
                                    .addLore(SPACE + "&eall()")
                                    .build()
                    );

        // checkerboard pattern
        for(Pair<SpecialSlots.CheckerboardVariant, Integer> pair : checkerboard()) {
            SpecialSlots.CheckerboardVariant variant = pair.getFirstItem();
            int index = pair.getSecondItem();
            this.set(index,
                    ItemCreator.createItem(this.getItemAt(index))
                            .addLore(SPACE + "&echeckerboard(" + variant.name() + ")")
                            .build()
                    );
        }

        // separator pattern
        for(Pair<SpecialSlots.SeparatorVariant, Integer> pair : separator()) {
            SpecialSlots.SeparatorVariant variant = pair.getFirstItem();
            int index = pair.getSecondItem();
            this.set(index,
                    ItemCreator.createItem(this.getItemAt(index))
                            .addLore(SPACE + "&eseparator(" + variant.name().toLowerCase(Locale.ROOT) + ")")
                            .build()
                    );
        }

        // all borders
        for(int index : borders())
            this.set(index,
                    ItemCreator.createItem(this.getItemAt(index))
                            .type(Material.GRAY_STAINED_GLASS_PANE)
                            .addLore(SPACE + "&eborders()")
                            .build()
                    );

        // corners
        for(int index : corners())
            this.set(index,
                    ItemCreator.createItem(this.getItemAt(index))
                            .type(Material.WHITE_STAINED_GLASS_PANE)
                            .addLore(SPACE + "&ecorners()")
                            .build()
                    );

        // show highlight
        if(highlight != null) {
            final ItemStack originalItem = this.getItemAt(highlight).clone();
            final Material originalMaterial = originalItem.getType();
            ItemMeta meta = originalItem.getItemMeta();
            if(meta == null) Bukkit.getItemFactory().getItemMeta(originalMaterial);
            final String originalName = (Objects.requireNonNull(meta).hasDisplayName() ? meta.getDisplayName() : meta.getLocalizedName());
            CustomGUI gui = this;
            this.task = new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack item = ItemCreator.createItem(originalItem)
                            .type(System.currentTimeMillis() % 1_000 // for alternating effect
                                    > 500 ? Material.ORANGE_STAINED_GLASS_PANE : originalMaterial)
                            .name(System.currentTimeMillis() % 1_000
                                    > 500 ? "&6&l" + ChatColor.stripColor(originalName) : originalName)
                            .build();
                    gui.set(highlight, item);
                }
            }.runTaskTimer(CyberAPI.getInstance(), 0L, 10L);
        }else this.task = null;

        this.open();
    }
    //</editor-fold>

    //<editor-fold desc="inventory events">
    @Override
    public void onInventoryClick(@NotNull InventoryClickEvent event, Player player) {
        int index = event.getSlot();
        if(System.currentTimeMillis() - lastClickedSlot.getOrDefault(index, 0L) < CLICK_COOLDOWN) return;
        if(event.getClickedInventory() == null) return;
        if(!event.getClickedInventory().equals(event.getView().getTopInventory())) return;

        ItemStack currentItem = event.getCurrentItem();
        if(currentItem == null) return;

        ItemMeta meta = currentItem.getItemMeta();
        if(meta == null) return;

        List<String> lore = meta.getLore();
        if(lore == null) lore = Collections.emptyList();

        TextComponent component = new TextComponent(UChat.bComponent("&6You clicked &aSlot #" + index));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(index)));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(UChat.chat(
                "&eClick to copy slot &b'" + index + "'&e!" + "\n" + // in the future, this will open a menu that is highlighted using the highlight feature
                        generateLargeGraph(event.getInventory().getSize(), event.getRawSlot()) + "\n\n" +
                        String.join("\n", lore)

        ))));
        event.getWhoClicked().spigot().sendMessage(component);
        ((Player)event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1F, 2F);
        lastClickedSlot.put(index, System.currentTimeMillis());
    }

    @Override
    public void onInventoryClose(@NotNull InventoryCloseEvent event, Player player) {
        if(this.task != null)
            this.task.cancel(); // task for highlighted item
    }
    //</editor-fold>

    //<editor-fold desc="internal methods">
    /**
     * GUISlots colors names based on the row they are on. This is used to color slots easily.
     * @since 143
     * @apiNote internal method
     */
    @ApiStatus.Internal
    private String colorFromIndex(int index) {
        return switch(InventoryUtils.getRowFromIndex(index, true)) {
            case 1 -> "&c";
            case 2 -> "&6";
            case 3 -> "&e";
            case 4 -> "&a";
            case 5 -> "&9";
            case 6 -> "&5";
            default -> "&7";
        };
    }

    /**
     * Generates a graph that is shown on the hover event after clicking a slot. In chat, this will show the
     * location of the clicked slot via the tooltip.
     * @since 143
     * @apiNote internal method
     */
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    @ApiStatus.Internal
    private String generateLargeGraph(int size, int slot) {
        return IntStream.range(0, size)
                .mapToObj(integer -> ((integer) % 9 == 0 ? "\n" : "") + (integer == slot ? "&a" : "&8") + "\u2B1B")
                .collect(Collectors.joining());
    }
    //</editor-fold>

    //<editor-fold desc="open methods">
    /**
     * <strong>Using this method to open the menu may cause a weird refresh effect for the player.</strong>
     * It is recommended you just call the constructor for GUISlots.
     * @param player the player that will open the inventory.
     * @since 143
     */
    @Override
    public void open(Player player) {
        super.open(player);
    }

    /**
     * Opens the GUI slots menu, though just calling the constructor should be enough.
     * <br> <br>
     * <strong>Using this method to open the menu may cause a weird refresh effect for the player.</strong>
     * It is recommended you just call the constructor.
     * @since 143
     */
    public void open() {
        this.open(this.player);
    }
    //</editor-fold>

}