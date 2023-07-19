package net.cybercake.cyberapi.spigot.inventory.pagination;

import net.cybercake.cyberapi.spigot.inventory.CustomGUI;
import net.cybercake.cyberapi.spigot.inventory.FollowUpGUIAction;
import net.cybercake.cyberapi.spigot.inventory.UpdateGUIAction;
import net.cybercake.cyberapi.spigot.inventory.actions.AddGUIAction;
import net.cybercake.cyberapi.spigot.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class NaturallyPaginatedGUI extends CustomGUI implements PaginatedGUI {

    protected NaturallyPaginatedGUI(Function<NaturallyPaginatedGUI, Inventory> createInventory) {
        super((gui) -> createInventory.apply((NaturallyPaginatedGUI) gui));
    }

    protected NaturallyPaginatedGUI(int size, @NotNull String title) {
        this((gui) -> Bukkit.createInventory(gui, size, title));
    }

    protected NaturallyPaginatedGUI(@NotNull InventoryType type, @NotNull String title) {
        this((gui) -> Bukkit.createInventory(gui, type, title));
    }

    protected NaturallyPaginatedGUI(int size) {
        this((gui) -> Bukkit.createInventory(gui, size, InventoryType.CHEST.getDefaultTitle()));
    }

    protected NaturallyPaginatedGUI(@NotNull InventoryType type) {
        this((gui) -> Bukkit.createInventory(gui, type, type.getDefaultTitle()));
    }

    private int currentSlot;
    private int[] naturalSlots;

    public void setNaturalSlots(int[] naturalSlots) {
        this.naturalSlots = naturalSlots;
    }

    public int[] getNaturalSlots() { return this.naturalSlots; }

    public void addNewNaturalSlot(int newSlot) {
        int[] newNaturalSlots = new int[this.naturalSlots.length+1];
        newNaturalSlots[this.naturalSlots.length] = newSlot;
        this.naturalSlots = newNaturalSlots;
    }

    public int nextSlot() {
        int oldSlot = currentSlot;
        int newSlot = -1;
        boolean isNew = false;
        for (int naturalSlot : naturalSlots) {
            if(naturalSlot == oldSlot)
                isNew = true;
            if(isNew) {
                newSlot = naturalSlot;
                break;
            }
        }
        return newSlot;
    }

    private FollowUpGUIAction internal_addForPage(int slot, ItemStack item) {
        return new AddGUIAction(this, new HashMap<>(Map.of(slot, item)));
    }

    public FollowUpGUIAction addForPage(ItemStack item) {
        return internal_addForPage(nextSlot(), item);
    }

    public FollowUpGUIAction addForPage(Material material) {
        int slot = nextSlot();
        return internal_addForPage(slot, UpdateGUIAction.addSpecialityDataTo(slot, UpdateGUIAction.convert(material)));
    }

    public void showPage(int page) {
        Inventory inventory = getInventory();
        for(int slot : this.naturalSlots) {

        }
    }

}