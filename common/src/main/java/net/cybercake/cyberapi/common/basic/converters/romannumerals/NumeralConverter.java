package net.cybercake.cyberapi.common.basic.converters.romannumerals;

import com.google.common.base.Preconditions;
import net.cybercake.cyberapi.common.basic.Pair;
import net.cybercake.cyberapi.common.basic.Sort;
import net.cybercake.cyberapi.common.basic.converters.GenericNumeralConvert;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static net.cybercake.cyberapi.common.basic.converters.romannumerals.RomanNumeral.isValidRomanNumerals;

/**
 * A numeral converter for easy-conversion between arabic numerals (1, 2, 3) and roman numerals (I, II, III).
 * Each method in here to convert has three different variants. Each conversion type will describe the differences.
 * <h3>toRomanNumeral's Three Methods: What's the difference?</h3>
 * There are three methods to do with converting to Roman Numerals. The only thing that differentiates these methods
 * is how they fail. View the table below to see how that changes per method.
 * <table><tbody><tr>
 *     <th>Method             </th><th> On Success                                    </th><th> On Fail</th>
 * </tr><tr>
 *     <td>{@link NumeralConverter#toRomanNumeralThrows(int) toRomanNumeralThrows}    </td><td> returns roman numeral (i.e., V)                        </td><td> throws an exception</td>
 * </tr><tr>
 *     <td>{@link NumeralConverter#toRomanNumeralOptional(int) toRomanNumeralOptional}    </td><td> returns roman numeral in optional (i.e., Optional&lt;String&gt; may contain "V")    </td><td> empty optional (orElse of optional will be called)</td>
 * </tr><tr>
 *     <td>{@link NumeralConverter#toRomanNumeral(int) toRomanNumeral}      </td><td> returns roman numeral (i.e., V)                        </td><td> "5" (integer literal)</td>
 * </tr></tbody></table>
 * <br> <br>
 * <h3>toArabicNumeral's Three Methods: What's the difference?</h3>
 * There are three methods to do with converting to Arabic Numerals. The only thing that differentiates
 * these methods is how they fail. View the table below to see how that changes per method.
 * <table><tbody><tr>
 *     <th>Method             </th><th> On Success                </th><th> On Fail</th>
 * </tr><tr>
 *     <td>{@link NumeralConverter#toArabicNumeralThrows(String) toArabicNumeralThrows} </td><td> returns arabic numeral (i.e., 5) </td><td> throws an exception</td>
 * </tr><tr>
 *     <td>{@link NumeralConverter#toArabicNumeralOptional(String) toArabicNumeralOptional} </td><td> returns arabic numeral in optional <br>(i.e., Optional&lt;Integer&gt; may contain 5)    </td><td> empty optional (orElse of optional will be called)</td>
 * </tr><tr>
 *     <td>{@link NumeralConverter#toArabicNumeral(String) toArabicNumeral} </td><td> returns arabic numeral (i.e., 5)    </td><td> -1</td>
 * </tr></tbody></table>
 * @since 146
 * @author <a href="https://www.baeldung.com/java-convert-roman-arabic">thanks to <strong>baeldung.com</strong> for code inspiration</a>
 * @author CyberedCake
 */
public class NumeralConverter {

    private NumeralConverter() { throw new UnsupportedOperationException("Cannot instantiate " + NumeralConverter.class.getCanonicalName()); }

    private static int loopMaximum = 50;

    /**
     * Sets the loop maximum to a certain integer. See {@link NumeralConverter#getLoopMaximum() this method} for better documentation.
     * @param loopMaximum the new loop maximum
     * @since 146
     */
    public static void setLoopMaximum(int loopMaximum) {
        NumeralConverter.loopMaximum = loopMaximum;
    }

    /**
     * How many times the converter should loop in each roman numeral converter or arabic numeral converter
     * before deciding to quit. Default value is {@code 50}.
     * @since 146
     */
    public static int getLoopMaximum() { return loopMaximum; }

    /**
     * Converts an arabic numeral (digits) into a roman numeral.
     * For example: this would convert an arabic numeral, like "123", into "CXXIII"
     * <h3>Three Methods: What's the difference?</h3>
     * There are three methods to do with converting to Roman Numerals. The only thing that differentiates
     * these methods is how they fail. View the table below to see how that changes per method.
     * <table><tbody><tr>
     *     <th>Method             </th><th> On Success                                    </th><th> On Fail</th>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toRomanNumeralThrows(int) toRomanNumeralThrows}    </td><td> returns roman numeral (i.e., V)                        </td><td> throws an exception</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toRomanNumeralOptional(int) toRomanNumeralOptional}    </td><td> returns roman numeral in optional (i.e., Optional&lt;String&gt; may contain "V")    </td><td> empty optional (orElse of optional will be called)</td>
     * </tr><tr>
     *     <td><em>toRomanNumeral (this)</em>        </td><td> returns roman numeral (i.e., V)                        </td><td> "5" (integer literal)</td>
     * </tr></tbody></table>
     * @param digits the arabic numeral to convert into a roman numeral. The range is limited to
     *               1 (inclusive) to 3,999 (inclusive), otherwise the arabic numeral literal will
     *               be returned (i.e., input of "5" will be "V" but "4000" will be "4000")
     * @return the roman numeral from the initial integer
     * @since 146
     * @see NumeralConverter#toRomanNumeralOptional(int)
     * @see NumeralConverter#toRomanNumeralThrows(int)
     * @see NumeralConverter
     */
    public static String toRomanNumeral(int digits) {
        return toRomanNumeralOptional(digits).orElse(String.valueOf(digits));
    }

    /**
     * Converts an arabic numeral (digits) into a roman numeral.
     * For example: this would convert an arabic numeral, like "123", into "CXXIII"
     * <h3>Three Methods: What's the difference?</h3>
     * There are three methods to do with converting to Roman Numerals. The only thing that differentiates
     * these methods is how they fail. View the table below to see how that changes per method.
     * <table><tbody><tr>
     *     <th>Method             </th><th> On Success                                    </th><th> On Fail</th>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toRomanNumeralThrows(int) toRomanNumeralThrows}    </td><td> returns roman numeral (i.e., V)                        </td><td> throws an exception</td>
     * </tr><tr>
     *     <td><em>toRomanNumeralOptional (this)</em></td><td> returns roman numeral in optional (i.e., Optional&lt;String&gt; may contain "V")    </td><td> empty optional (orElse of optional will be called)</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toRomanNumeral(int) toRomanNumeral}        </td><td> returns roman numeral (i.e., V)                        </td><td> "5" (integer literal)</td>
     * </tr></tbody></table>
     * @param digits the arabic numeral to convert into a roman numeral. The range is limited to
     *               1 (inclusive) to 3,999 (inclusive), otherwise the optional will be empty.
     * @return the roman numeral from the initial integer
     * @since 146
     * @see NumeralConverter#toRomanNumeral(int)
     * @see NumeralConverter#toRomanNumeralThrows(int)
     * @see NumeralConverter
     */
    public static Optional<String> toRomanNumeralOptional(int digits) {
        try {
            return Optional.of(toRomanNumeralThrows(digits));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    /**
     * Converts an arabic numeral (digits) into a roman numeral.
     * For example: this would convert an arabic numeral, like "123", into "CXXIII"
     * <h3>Three Methods: What's the difference?</h3>
     * There are three methods to do with converting to Roman Numerals. The only thing that differentiates
     * these methods is how they fail. View the table below to see how that changes per method.
     * <table><tbody><tr>
     *     <th>Method             </th><th> On Success                                    </th><th> On Fail</th>
     * </tr><tr>
     *     <td><em>toRomanNumeralThrows (this)</em></td><td> returns roman numeral (i.e., V)                        </td><td> throws an exception</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toRomanNumeralOptional(int) toRomanNumeralOptional}    </td><td> returns roman numeral in optional (i.e., Optional&lt;String&gt; may contain "V")    </td><td> empty optional (orElse of optional will be called)</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toRomanNumeral(int) toRomanNumeral}        </td><td> returns roman numeral (i.e., V)                        </td><td> "5" (integer literal)</td>
     * </tr></tbody></table>
     * @param digits the arabic numeral to convert into a roman numeral. The range is limited to
     *               1 (inclusive) to 3,999 (inclusive).
     * @return the roman numeral from the initial integer
     * @since 146
     * @throws ExcessiveLoopsException when the while loop used to technically make the conversion work exceeds the
     *                                 {@link NumeralConverter#getLoopMaximum() loop maximum}
     * @throws IllegalArgumentException when the integer provided is not between 1 (inclusive) and 3999 (inclusive)
     * @see NumeralConverter#toRomanNumeral(int)
     * @see NumeralConverter#toRomanNumeralOptional(int)
     * @see NumeralConverter
     */
    public static String toRomanNumeralThrows(@IntRange(from = 1, to = 3999) int digits) {
        return GenericNumeralConvert.ROMAN_NUMERALS.to(digits);
    }

    /**
     * Converts a roman numeral into an arabic numeral (digits).
     * For example: this would convert a roman numeral, like "CXXIII", into "123"
     * <h3>Three Methods: What's the difference?</h3>
     * There are three methods to do with converting to Arabic Numerals. The only thing that differentiates
     * these methods is how they fail. View the table below to see how that changes per method.
     * <table><tbody><tr>
     *     <th>Method             </th><th> On Success                </th><th> On Fail</th>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toArabicNumeralThrows(String) toArabicNumeralThrows} </td><td> returns arabic numeral (i.e., 5) </td><td> throws an exception</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toArabicNumeralOptional(String) toArabicNumeralOptional} </td><td> returns arabic numeral in optional <br>(i.e., Optional&lt;Integer&gt; may contain 5)    </td><td> empty optional (orElse of optional will be called)</td>
     * </tr><tr>
     *     <td><em>toArabicNumeral (this)</em> </td><td> returns arabic numeral (i.e., 5)    </td><td> -1</td>
     * </tr></tbody></table>
     * @param roman the roman numeral to convert into an arabic numeral. Make sure you follow all rules
     *              of a <a href="https://en.wikipedia.org/wiki/Roman_numerals#Standard_form">valid roman numeral</a>
     *              or {@link RomanNumeral#NUMEROUS_NUMERAL_VALIDATION view the regular expression that governs this}
     * @return the arabic numeral from the initial roman numeral encapsulated in an {@link Optional}. If the roman numeral
     *         is invalid or otherwise throws an exception, this method will return "-1"
     * @since 146
     * @see NumeralConverter#toArabicNumeralOptional(String)
     * @see NumeralConverter#toArabicNumeralThrows(String)
     * @see NumeralConverter
     */
    public static int toArabicNumeral(String roman) {
        return toArabicNumeralOptional(roman).orElse(-1);
    }

    /**
     * Converts a roman numeral into an arabic numeral (digits).
     * For example: this would convert a roman numeral, like "CXXIII", into "123"
     * <h3>Three Methods: What's the difference?</h3>
     * There are three methods to do with converting to Arabic Numerals. The only thing that differentiates
     * these methods is how they fail. View the table below to see how that changes per method.
     * <table><tbody><tr>
     *     <th>Method             </th><th> On Success                </th><th> On Fail</th>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toArabicNumeralThrows(String) toArabicNumeralThrows} </td><td> returns arabic numeral (i.e., 5) </td><td> throws an exception</td>
     * </tr><tr>
     *     <td><em>toArabicNumeralOptional (this)</em> </td><td> returns arabic numeral in optional <br>(i.e., Optional&lt;Integer&gt; may contain 5)    </td><td> empty optional (orElse of optional will be called)</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toArabicNumeral(String) toArabicNumeral} </td><td> returns arabic numeral (i.e., 5)    </td><td> -1</td>
     * </tr></tbody></table>
     * @param roman the roman numeral to convert into an arabic numeral. Make sure you follow all rules
     *              of a <a href="https://en.wikipedia.org/wiki/Roman_numerals#Standard_form">valid roman numeral</a>
     *              or {@link RomanNumeral#NUMEROUS_NUMERAL_VALIDATION view the regular expression that governs this}
     * @return the arabic numeral from the initial roman numeral encapsulated in an {@link Optional}. The optional will be
     *         empty if the value is not valid or threw an exception
     * @since 146
     * @see NumeralConverter#toArabicNumeral(String)
     * @see NumeralConverter#toArabicNumeralThrows(String)
     * @see NumeralConverter
     */
    public static Optional<Integer> toArabicNumeralOptional(String roman) {
        try {
            return Optional.of(toArabicNumeralThrows(roman));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    /**
     * Converts a roman numeral into an arabic numeral (digits).
     * For example: this would convert a roman numeral, like "CXXIII", into "123"
     * <h3>Three Methods: What's the difference?</h3>
     * There are three methods to do with converting to Arabic Numerals. The only thing that differentiates
     * these methods is how they fail. View the table below to see how that changes per method.
     * <table><tbody><tr>
     *     <th>Method             </th><th> On Success                </th><th> On Fail</th>
     * </tr><tr>
     *     <td><em>toArabicNumeralThrows (this)</em> </td><td> returns arabic numeral (i.e., 5) </td><td> throws an exception</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toArabicNumeralOptional(String) toArabicNumeralOptional} </td><td> returns arabic numeral in optional <br>(i.e., Optional&lt;Integer&gt; may contain 5)    </td><td> empty optional (orElse of optional will be called)</td>
     * </tr><tr>
     *     <td>{@link NumeralConverter#toArabicNumeral(String) toArabicNumeral} </td><td> returns arabic numeral (i.e., 5)    </td><td> -1</td>
     * </tr></tbody></table>
     * @param roman the roman numeral to convert into an arabic numeral. Make sure you follow all rules
     *              of a <a href="https://en.wikipedia.org/wiki/Roman_numerals#Standard_form">valid roman numeral</a>
     *              or {@link RomanNumeral#NUMEROUS_NUMERAL_VALIDATION view the regular expression that governs this}
     * @return the arabic numeral from the initial roman numeral
     * @since 146
     * @throws ExcessiveLoopsException when the while loop used to technically make the conversion work exceeds the
     *                                 {@link NumeralConverter#getLoopMaximum() loop maximum}
     * @throws IllegalArgumentException when the roman numeral is not valid as governed by {@link RomanNumeral#isValidRomanNumerals(String) this method}
     * @see NumeralConverter#toArabicNumeral(String)
     * @see NumeralConverter#toArabicNumeralThrows(String)
     * @see NumeralConverter
     */
    public static int toArabicNumeralThrows(String roman) {
        return GenericNumeralConvert.ROMAN_NUMERALS.from(roman);
    }

    @ApiStatus.Internal
    public static class InternalConvertRomanNumerals implements net.cybercake.cyberapi.common.basic.converters.GenericNumeralConvert<Integer, String> {

        @Override
        public Pair<Class<Integer>, Class<String>> getTypes() { return Pair.of(Integer.class, String.class); }

        @Override
        public String to(Integer digits) {
            Preconditions.checkArgument(digits < 4000, "Cannot convert a value greater than or equal to 4,000 to a roman numeral.");
            Preconditions.checkArgument(digits > 0, "Cannot convert a value less than or equal to 0 to a roman numeral.");
            List<RomanNumeral> numerals = RomanNumeral.getValues(Sort.SortType.DESCENDING);
            StringBuilder roman = new StringBuilder();

            int     loopAmount = 0,
                    index = 0,
                    subtractionRule = digits;
            while ((subtractionRule > 0) && (index < numerals.size())) {
                if (loopAmount > loopMaximum)
                    throw new ExcessiveLoopsException("Failed to convert arabic numeral -> roman numeral within sufficient loops (" + loopAmount + "). Maybe the input isn't convertable");
                loopAmount++;
                RomanNumeral currentNumeral = numerals.get(index);
                if (currentNumeral.getArabicNumeral() <= subtractionRule) {
                    roman.append(currentNumeral.getRomanNumeral());
                    subtractionRule -= currentNumeral.getArabicNumeral();
                }else index++;
            }

            return roman.toString();
        }

        @Override
        public Integer from(String roman) {
            Preconditions.checkArgument(isValidRomanNumerals(roman), "Cannot convert invalid roman numerals. Try again with only these characters: " + RomanNumeral.getPrimitives(Sort.SortType.ASCENDING));
            List<RomanNumeral> numerals = RomanNumeral.getValues(Sort.SortType.DESCENDING);
            int value = 0;

            int loopAmount = 0;
            String modificationRule = roman.toUpperCase(Locale.ROOT);
            while (modificationRule.length() > 0) {
                if (loopAmount > loopMaximum)
                    throw new ExcessiveLoopsException("Failed to convert roman numeral -> arabic numeral within sufficient loops (" + loopAmount + "). Maybe the input isn't convertable?");
                loopAmount++;
                for(RomanNumeral numeral : numerals) {
                    if (!modificationRule.startsWith(numeral.getRomanNumeral())) continue;
                    value += numeral.getArabicNumeral();
                    modificationRule = modificationRule.substring(numeral.getRomanNumeral().length());
                }
            }

            return value;
        }
    }

}
