package net.cybercake.cyberapi.common.basic.converters.romannumerals;

import com.google.common.base.Preconditions;
import net.cybercake.cyberapi.common.basic.Pair;
import net.cybercake.cyberapi.common.basic.Sort;
import net.cybercake.cyberapi.common.basic.converters.GenericConverter;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

import static net.cybercake.cyberapi.common.basic.converters.romannumerals.RomanNumeral.isValidRomanNumerals;

/**
 * A numeral converter for easy-conversion between arabic numerals (1, 2, 3) and roman numerals (I, II, III)
 * @since 146
 * @author <a href="https://www.baeldung.com/java-convert-roman-arabic">thanks to <strong>baeldung.com</strong> for code inspiration</a>
 * @author CyberedCake
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class NumeralConverter implements GenericConverter<Integer, String> {

    private static int globalLoopMaximum = 50;

    /**
     * The global loop maximum. This is used for all loops and is used as a default
     * for local loops. It will only allow methods to loop {@code x} (the returned value)
     * amount of times when converting.
     * @return the global loop maximum that affects everything
     * @apiNote to change this value, create a new instance of {@link NumeralConverter} and
     *          use method {@link NumeralConverter#setGlobalLoopMaximum(int)}
     * @since 146
     */
    public static int getLoopMaximum() { return globalLoopMaximum; }

    private int loopMaximum;

    /**
     * Instantiating this class should only be used for changing the loop maximums and running conversions using a local one!
     * @since 146
     */
    public NumeralConverter() {
        this(null);
    }

    /**
     * Instantiating this class should only be used for changing the loop maximums and running conversions using a local one!
     * @param localLoopMaximum the loop maximum to use for the conversion methods
     * @since 146
     */
    public NumeralConverter(@Nullable @IntRange(from = 1, to = 99999) Integer localLoopMaximum) {
        this.loopMaximum = (localLoopMaximum == null ? globalLoopMaximum : localLoopMaximum);
    }

    /**
     * The local loop maximum. This is used for loops involving the {@link NumeralConverter#to(Integer)} and {@link NumeralConverter#from(String)}
     * methods. It will only allow these methods to loop {@code x} (the returned value) amount of times. This will take priority over the global
     * loop maximum.
     * @return only allow the two functions mentioned above to loop <em>this</em> many times
     * @since 146
     */
    public int getLocalLoopMaximum() { return this.loopMaximum; }

    /**
     * The global loop maximum. This is used for loops outside of this instance and used as a default
     * for local loops. It will only allow methods to loop {@code x} (the returned value) amount of times.
     * @return only allow all outside methods to loop <em>this</em> many times
     * @since 146
     */
    public int getGlobalLoopMaximum() { return globalLoopMaximum; }

    /**
     * The local loop maximum. This is used for loops involving the {@link NumeralConverter#to(Integer)} and {@link NumeralConverter#from(String)}
     * methods. It will only allow these methods to loop {@code x} (the integer parameter) amount of times. This will take priority over the global
     * loop maximum.
     * @param localLoopMaximum the new local loop maximum for the two methods mentioned above
     * @since 146
     */
    public void setLocalLoopMaximum(@Nullable Integer localLoopMaximum) {
        this.loopMaximum = localLoopMaximum == null ? globalLoopMaximum : localLoopMaximum;
    }

    /**
     * The global loop maximum. This is used for loops outside of this instance and used as a default
     * for local loops. It will only allow methods to loop {@code x} (the integer parameter) amount of times.
     * @param globalLoopMaximum the new global loop maximum for outside methods
     * @since 146
     */
    public void setGlobalLoopMaximum(int globalLoopMaximum) {
        NumeralConverter.globalLoopMaximum = globalLoopMaximum;
    }

    @Override
    public Pair<Class<Integer>, Class<String>> getTypes() {
        return Pair.of(Integer.class, String.class);
    }

    /**
     * @apiNote view {@link NumeralConverter#convertToRomanNumeral(int)} for more detailed documentation
     * @since 146
     */
    @Override
    public String to(@NotNull Integer input) {
        return internal_convertToRomanNumeral(input, this.getLocalLoopMaximum());
    }

    /**
     * @apiNote view {@link NumeralConverter#convertToArabicNumeral(String)} for more detailed documentation
     * @since 146
     */
    @Override
    public Integer from(@NotNull String output) {
        return internal_convertToArabicNumeral(output, this.getLocalLoopMaximum());
    }

    /**
     * Converts an arabic numeral (digits) into a roman numeral.
     * For example: this would convert an arabic numeral, like "123", into "CXXIII"
     * @param digits the arabic numeral to convert into a roman numeral. The range is limited to
     *               1 (inclusive) to 3,999 (inclusive).
     * @return the roman numeral from the initial integer
     * @since 146
     * @throws ExcessiveLoopsException when the while loop used to technically make the conversion work exceeds the
     *                                 {@link NumeralConverter#getGlobalLoopMaximum() global loop maximum}, or if local,
     *                                 the {@link NumeralConverter#getLocalLoopMaximum() local loop maximum} (takes priority)
     * @throws IllegalArgumentException when the integer provided is not between 1 (inclusive) and 3999 (inclusive)
     */
    public static String convertToRomanNumeral(@IntRange(from = 1, to = 3999) int digits) throws ExcessiveLoopsException, IllegalArgumentException {
        return internal_convertToRomanNumeral(digits, null);
    }

    /**
     * Converts a roman numeral into an arabic numeral (digits).
     * For example: this would convert a roman numeral, like "CXXIII", into "123"
     * @param roman the roman numeral to convert into an arabic numeral. Make sure you follow all rules
     *              of a <a href="https://en.wikipedia.org/wiki/Roman_numerals#Standard_form">valid roman numeral</a>
     *              or {@link RomanNumeral#NUMEROUS_NUMERAL_VALIDATION view the regular expression that governs this}
     * @return the arabic numeral from the initial roman numeral
     * @since 146
     * @throws ExcessiveLoopsException when the while loop used to technically make the conversion work exceeds the
     *                                 {@link NumeralConverter#getGlobalLoopMaximum() global loop maximum}, or if local,
     *                                 the {@link NumeralConverter#getLocalLoopMaximum() local loop maximum} (takes priority)
     * @throws IllegalArgumentException when the roman numeral is not valid as governed by {@link RomanNumeral#isValidRomanNumerals(String) this method}
     */
    public static int convertToArabicNumeral(String roman) throws ExcessiveLoopsException, IllegalArgumentException {
        return internal_convertToArabicNumeral(roman, null);
    }

    //<editor-fold desc="actual conversions (internal)">
    @ApiStatus.Internal
    private static String internal_convertToRomanNumeral(int digits, @Nullable Integer loopMaximum) throws ExcessiveLoopsException, IllegalArgumentException {
        Preconditions.checkArgument(digits < 4000, "Cannot convert a value greater than or equal to 4,000 to a roman numeral.");
        Preconditions.checkArgument(digits > 0, "Cannot convert a value less than or equal to 0 to a roman numeral.");
        List<RomanNumeral> numerals = RomanNumeral.getValues(Sort.SortType.DESCENDING);
        StringBuilder roman = new StringBuilder();

        int     loopAmount = 0,
                index = 0,
                subtractionRule = digits,
                actualLoopMax = (loopMaximum == null ? globalLoopMaximum : loopMaximum);
        while ((subtractionRule > 0) && (index < numerals.size())) {
            if(loopAmount > actualLoopMax)
                throw new ExcessiveLoopsException("Failed to convert arabic numeral -> roman numeral within sufficient loops (" + loopAmount + "). Maybe the input isn't convertable");
            loopAmount++;
            RomanNumeral currentNumeral = numerals.get(index);
            if(currentNumeral.getArabicNumeral() <= subtractionRule) {
                roman.append(currentNumeral.getRomanNumeral());
                subtractionRule -= currentNumeral.getArabicNumeral();
            }else index++;
        }

        return roman.toString();
    }

    @ApiStatus.Internal
    private static int internal_convertToArabicNumeral(String roman, @Nullable Integer loopMaximum) throws ExcessiveLoopsException, IllegalArgumentException {
        Preconditions.checkArgument(isValidRomanNumerals(roman), "Cannot convert invalid roman numerals. Try again with only these characters: " + RomanNumeral.getPrimitives(Sort.SortType.ASCENDING));
        List<RomanNumeral> numerals = RomanNumeral.getValues(Sort.SortType.DESCENDING);
        int value = 0;

        int actualLoopMax = (loopMaximum == null ? globalLoopMaximum : loopMaximum);
        int loopAmount = 0;
        String modificationRule = roman.toUpperCase(Locale.ROOT);
        while (modificationRule.length() > 0) {
            if(loopAmount > actualLoopMax)
                throw new ExcessiveLoopsException("Failed to convert roman numeral -> arabic numeral within sufficient loops (" + loopAmount + "). Maybe the input isn't convertable?");
            loopAmount++;
            for(RomanNumeral numeral : numerals) {
                if(!modificationRule.startsWith(numeral.getRomanNumeral())) continue;
                value += numeral.getArabicNumeral();
                modificationRule = modificationRule.substring(numeral.getRomanNumeral().length());
            }
        }

        return value;
    }
    //</editor-fold>
}
