package net.cybercake.cyberapi.spigot.inventory.utils;

import org.bukkit.inventory.Inventory;

import java.util.stream.IntStream;

/**
 * General Inventory Utils to be used
 * @since 143
 * @author CyberedCake
 * @author <a href="https://gist.github.com/graywolf336/8153678">graywolf336 on GitHub</a> for Base64 conversions
 */
public class InventoryUtils {

    /**
     * Retrieves a row number from an index. If you, say, input slot 4, it would return "1" (row 1). If you instead input
     * slot 23, it would return "3" (row 3).
     * @param index the index (or slot number) that is being referenced
     * @param isSize whether the index is a slot number or a size. If it is the size of the inventory, make sure
     *               to set this to TRUE, otherwise set it to FALSE. Most of the time, you would want this value
     *               to be false, however, incorrectly setting it to false can lead to an incorrect value. A simple
     *               way to put it: if this is an inventory size and was obtained from {@code inventory.getSize()},
     *               then it should be true, and all else should be false
     * @return the row number from the index
     * @apiNote this method considers {@link org.bukkit.event.inventory.InventoryType#CHEST a standard double-chest in size and type}
     * @since 143
     */
    public static int getRowFromIndex(int index, boolean isSize) {
        // + 1 to avoid 0 / 9 (would result in 0)
        return (int)Math.round(Math.ceil((((double)index) + (isSize ? 0d : 1d)) / 9d));
    }

    /**
     * Gets the indices (slots) that are a part of a certain row. For example, if you enter "5" (interpreted as row 5), the
     * method will return an array of integers from 36 to 44 (slots on row 5 from a zero-based inventory).
     * @param row the row number
     * @return the array of slots that appear on the inputted row
     * @apiNote this method considers {@link org.bukkit.event.inventory.InventoryType#CHEST a standard double-chest in size and type}
     * @since 143
     */
    public static int[] getIndicesFromRow(int row) {
        return IntStream.range((row*9)-9, (row*9)).toArray();
    }


}
