package net.cybercake.cyberapi.spigot.inventory;

import net.cybercake.cyberapi.common.basic.NumberUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.stream.IntStream;

import static net.cybercake.cyberapi.spigot.inventory.utils.InventoryUtils.getIndicesFromRow;
import static net.cybercake.cyberapi.spigot.inventory.utils.InventoryUtils.getRowFromIndex;

/**
 * Represents special slot storage containers that contain an array of integers that
 * can be used to mark certain parts of the GUI without manual labor.
 * @since 143
 */
public enum SpecialSlots {

    /**
     * Returns all GUI slot indices.
     * @since 143
     */
    ALL((size) -> IntStream.range(0, size).toArray()),

    /**
     * Returns only the borders of a GUI based on its size. If a GUI is two rows
     * or fewer, it will return all slots of the GUI.
     * @since 143
     */
    BORDERS((size) ->
            IntStream.range(0, size).filter(integer ->
                    (integer - 8) % 9 == 0
                    || integer % 9 == 0
                    || integer < 8
                    || integer > (size - 9))
                    .toArray()
    ),

    /**
     * Returns only the vertical borders of the GUI (i.e., the borders that run up and down on either side of the menu).
     * @since 179
     */
    VERTICAL_BORDERS((size) ->
            IntStream.range(0, size).filter(integer ->
                    integer % 9 == 0
                    || (integer + 1) % 9 == 0
            ).toArray()
    ),

    /**
     * Returns only the horizontal borders of the GUI (i.e., the borders that run side to side on either side of the menu).
     * @since 179
     */
    HORIZONTAL_BORDERS((size) ->
            IntStream.range(0, size).filter(integer ->
                            NumberUtils.isBetweenEquals(integer, 0, 9)
                                    || NumberUtils.isBetweenEquals(integer,
                                     size - 10, size - 1)
                    )
                    .toArray()
    ),

    /**
     * Returns only the corners of a GUI based on its size. If a GUI is one row,
     * it will only appear to mark the left and right corners.
     * @since 143
     */
    CORNERS((size) ->
            IntStream.range(0, size).filter(integer ->
                    integer == 0
                    || integer == 8
                    || integer == (size - 9)
                    || integer == (size - 1))
                    .toArray()
    ),

    /**
     * Returns a checkerboard pattern of the GUI. This will fill the holes that
     * {@link SpecialSlots#CHECKERBOARD_B checkerboard B} would leave behind.
     * @since 143
     * @see SpecialSlots#CHECKERBOARD_B
     * @see CheckerboardVariant#B
     */
    CHECKERBOARD_A((size) ->
            IntStream.range(0, size).filter(integer ->
                    integer % 2 == 0
                    )
                    .toArray()
            ),

    /**
     * Returns a checkerboard pattern of the GUI. This will fill the holes that
     * {@link SpecialSlots#CHECKERBOARD_A checkerboard A} would leave behind.
     * @since 143
     * @see SpecialSlots#CHECKERBOARD_A
     * @see CheckerboardVariant#A
     */
    CHECKERBOARD_B((size) ->
            IntStream.range(0, size).filter(integer ->
                    integer % 2 != 0
                    )
                    .toArray()
            ),

    /**
     * Returns a vertical separator based on the size of the GUI. If the GUI is
     * only one row, only the middle item will be filled.
     * @since 143
     * @see SpecialSlots#SEPARATOR_HORIZONTAL
     * @see SeparatorVariant#HORIZONTAL
     */
    SEPARATOR_VERTICAL((size) ->
            IntStream.range(0, size).filter(integer ->
                    (integer - 4) % 9 == 0
                    )
                    .toArray()
            ),

    /**
     * Returns a horizontal separator based on the size of the GUI. If the GUI is
     * two rows or fewer, it will return all GUI slots. <br> <br>
     * This method is significantly more complicated than
     * {@link SpecialSlots#SEPARATOR_VERTICAL its vertical counterpart}. It works
     * like this:
     * <br>
     * <h3>If the row number is even:</h3>
     * It will attempt to do its best at creating a horizontal separator, but will
     * return the indices of the two middle rows on either side of the median.
     * <h3>If the row number is odd:</h3>
     * It will find the median row number and will return only the indices to
     * fill up that row.
     * @since 143
     * @see SpecialSlots#SEPARATOR_VERTICAL
     * @see SeparatorVariant#HORIZONTAL
     */
    SEPARATOR_HORIZONTAL((size) -> {
        int rows = getRowFromIndex(size, false);
        if (rows % 2 == 0) { // even
            int[] row = getIndicesFromRow(rows/2);
            int[] rowPlusOne = getIndicesFromRow((rows/2)+1);
            int[] result = new int[row.length + rowPlusOne.length];
            System.arraycopy(row, 0, result, 0, row.length);
            System.arraycopy(rowPlusOne, 0, result, row.length, rowPlusOne.length);
            return result;
        }
        else return getIndicesFromRow((int) Math.ceil((double) rows / 2)); // odd
    });

    private final Function<Integer, int[]> slots;

    SpecialSlots(Function<Integer, int[]> slots) {
        this.slots = slots;
    }

    /**
     * Gets the special slots as a function of the size of the GUI. This number would be
     * the amount of slots (zero-based). For example: If there were five rows in my GUI,
     * I would enter 44 here.
     * @param guiSize the size of the GUI (zero-based, slot numbers)
     * @return the requested speciality array of slots
     * @since 143
     */
    public int[] getSlotsForSize(int guiSize) { return this.slots.apply(guiSize); }

    /**
     * Gets the special slots as a function of the amount of rows of the GUI. For example,
     * if there were 44 slots in my GUI, I would say there are five rows.
     * @param guiRows the amount of rows in the GUI
     * @return the requested speciality array of slots
     * @since 143
     */
    public int[] getSlotsForRows(int guiRows) { return this.getSlotsForSize(guiRows * 9); }

    @ApiStatus.Internal
    public interface SimplifiedGUIEnums {
        SpecialSlots convert();
        String getName();
    }

    /**
     * Represents the checkerboard varieties.
     * @see CheckerboardVariant#A
     * @see CheckerboardVariant#B
     * @since 143
     */
    public enum CheckerboardVariant implements SimplifiedGUIEnums {
        /**
         * {@link SpecialSlots#CHECKERBOARD_A Please click here for better documentation.}
         */
        A { @Override public SpecialSlots convert() {
                return SpecialSlots.CHECKERBOARD_A; } },
        /**
         * {@link SpecialSlots#CHECKERBOARD_B Please click here for better documentation.}
         */
        B { @Override public SpecialSlots convert() {
                return SpecialSlots.CHECKERBOARD_B; } };

        @Override
        public String getName() { return "checkerboard"; }
    }

    /**
     * Represents the separator varieties.
     * @see SeparatorVariant#VERTICAL
     * @see SeparatorVariant#HORIZONTAL
     * @since 143
     */
    public enum SeparatorVariant implements SimplifiedGUIEnums {
        /**
         * {@link SpecialSlots#SEPARATOR_VERTICAL Please click here for better documentation.}
         */
        VERTICAL { @Override public SpecialSlots convert() {
            return SpecialSlots.SEPARATOR_VERTICAL; } },
        /**
         * {@link SpecialSlots#SEPARATOR_HORIZONTAL Please click here for better documentation.}
         */
        HORIZONTAL { @Override public SpecialSlots convert() {
            return SpecialSlots.SEPARATOR_HORIZONTAL; } };


        @Override
        public String getName() { return "separator"; }
    }


}
