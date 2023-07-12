package net.cybercake.cyberapi.common.basic.converters.romannumerals;

import net.cybercake.cyberapi.common.basic.Sort;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Represents a valid roman numeral
 * @since 146
 */
public enum RomanNumeral {

    I   (1),
    IV  (4),
    V   (5),
    IX  (9),
    X   (10),
    XL  (40),
    L   (50),
    XC  (90),
    C   (100),
    CD  (400),
    D   (500),
    CM  (900),
    M   (1000);

    private final String romanNumeral;
    private final int arabicNumeral;
    private final boolean primitive;

    RomanNumeral(int arabicNumeral) {
        this.romanNumeral = this.name();
        this.arabicNumeral = arabicNumeral;

        // a "primitive" roman numeral in this case is a roman numeral with only one character,
        // aka the roman numerals like "I" or "C" or "D" or "M", they are "primitives" compared to
        // the other ones (i.e., "I" is primitive as it's a new character, "IV" is not as it contains
        // an already-known character in it -- I)
        this.primitive = this.romanNumeral.length() == 1;
    }

    /**
     * @return the arabic value for this roman numeral
     * @since 146
     */
    public int getArabicNumeral() { return this.arabicNumeral; }

    /**
     * @return the roman numeral for this arabic value
     */
    public String getRomanNumeral() { return this.romanNumeral; }

    /**
     * @return if the roman numeral is a primitive one
     * @apiNote a "primitive" roman numeral is a roman numeral with only one character,
     *          aka the roman numerals like "I" or "C" or "D" or "M", these are "primitives" compared to
     *          the others (i.e., "I" is primitive as it's a new character, "IV" is not as it contains
     *          an already-known character in it -- I)
     */
    public boolean isPrimitive() { return this.primitive; }


    /**
     * A regular expression to denote if a single character is considered a roman numeral. For
     * example: any character contained in this enum is valid ("I", "V", "X", "L", "C", "D", "M"),
     * anything outside of this list is <strong>not valid.</strong>
     * @since 146
     */
    public static final String SINGLE_NUMERAL_VALIDATION = "^[MDCLXVI]$";

    /**
     * A regular expression to denote if a string of a roman numeral is valid. For example: while the
     * numeral "XCC" is not valid while "CXC" (190) is.
     * @since 146
     */
    public static final String NUMEROUS_NUMERAL_VALIDATION = "^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";

    /**
     * Gets a roman numeral associated with an arabic numeral.
     * <strong><h2>It is strongly recommended to NOT use this method.</h2></strong>
     * This method will <strong>only</strong> return roman numerals that are
     * <strong>{@link RomanNumeral in this enumeration}</strong>. Therefore, "1" will return "I" but "2"
     * will return null.
     * @param arabicNumeral the arabic numeral associated with a roman numeral
     * @return the roman numeral if it exists, else null (in {@link Optional} form)
     * @since 146
     * @see NumeralConverter#toRomanNumeral(int)
     */
    public static Optional<String> find(int arabicNumeral) {
        return Arrays.stream(RomanNumeral.values())
                .filter((num) -> num.getArabicNumeral() == arabicNumeral)
                .map(RomanNumeral::getRomanNumeral)
                .findFirst();
    }

    /**
     * Gets an arabic numeral associated with a roman numeral.
     * <strong><h2>It is strongly recommended to NOT use this method.</h2></strong>
     * This method will <strong>only</strong> return arabic numerals that are
     * <strong>{@link RomanNumeral in this enumeration}</strong>. Therefore, "I" will return "1" but "II"
     * will return null.
     * @param romanNumeral the roman numeral associated with an arabic numeral
     * @return the arabic numeral if it exists, else null (in {@link Optional} form)
     * @since 146
     * @see NumeralConverter#toArabicNumeral(String)
     */
    public static Optional<Integer> find(String romanNumeral) {
        return Arrays.stream(RomanNumeral.values())
                .filter((num) -> num.getRomanNumeral().equalsIgnoreCase(romanNumeral))
                .map(RomanNumeral::getArabicNumeral)
                .findFirst();
    }

    /**
     * Gets all the values of this enumeration sorted in a certain way
     * @param sortHow how to sort the list of all values
     * @return the sorted list
     * @since 146
     */
    public static List<RomanNumeral> getValues(Sort.SortType sortHow) {
        return switch(sortHow) {
            case ASCENDING -> Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral::getArabicNumeral)))
                    .toList();
            case DESCENDING -> Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral::getArabicNumeral)).reversed())
                    .toList();
        };
    }

    /**
     * Checks if a <strong>full roman numeral</strong> (e.g., "CXXIII") is valid. Use
     * {@link RomanNumeral#isValidRomanNumeral(String)} for checking if <strong>one</strong>
     * character is valid (e.g., "I")
     * @param stringOfNumerals the full numeral
     * @return if the numeral is valid, this method will return true
     * @since 146
     */
    public static boolean isValidRomanNumerals(@NotNull String stringOfNumerals) {
        return stringOfNumerals.matches(NUMEROUS_NUMERAL_VALIDATION);
    }

    /**
     * Checks if a <strong>single roman numeral</strong> (e.g., "I") is valid. Use
     * {@link RomanNumeral#isValidRomanNumerals(String)} for checking if <strong>a full roman numeral</strong>
     * is valid (e.g., "CXXIII")
     * @param singleNumeral the single-character numeral
     * @return if the single-character numeral is valid, this method will return true
     * @since 146
     */
    public static boolean isValidRomanNumeral(@NotNull String singleNumeral) {
        return singleNumeral.matches(SINGLE_NUMERAL_VALIDATION);
    }

    /**
     * Finds all the primitives in this enumeration. See the documentation for {@link RomanNumeral#isPrimitive()}
     * for what a primitive roman numeral is.
     * @param sortHow how to sort the list
     * @return the list of primitive roman numerals in accordance to {@link RomanNumeral#isPrimitive() this method}
     * @since 146
     */
    public static List<RomanNumeral> getPrimitives(Sort.SortType sortHow) {
        return getValues(sortHow).stream().filter(RomanNumeral::isPrimitive).toList();
    }

}
