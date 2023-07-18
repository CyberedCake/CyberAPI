package net.cybercake.cyberapi.spigot.inventory.pagination;

import net.cybercake.cyberapi.spigot.inventory.CustomGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.checkerframework.common.value.qual.ArrayLen;
import org.checkerframework.common.value.qual.ArrayLenRange;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class NaturallyPaginatedGUI extends CustomGUI {

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

    public @ArrayLenRange(from = 1, to = 1) int[] naturalSlot() {
        return this.naturalSlots;
    }

}