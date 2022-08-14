package net.cybercake.cyberapi.common.basic;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * All static methods of general number utils, everything to do with integers, longs, doubles, floats, etc.
 */
public class NumberUtils {

    /**
     * Checks if the {@link String} provided contains a primitive-type 'byte'
     * @param string the {@link String} to check
     * @return whether the {@link String} contains a primitive-type 'byte'
     * @since 15
     */
    public static boolean isByte(String string) {
        try {
            byte integer = Byte.parseByte(string);
            integer = (byte) (integer + 5);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the {@link String} provided contains a primitive-type 'short'
     * @param string the {@link String} to check
     * @return whether the {@link String} contains a primitive-type 'short'
     * @since 15
     */
    public static boolean isShort(String string) {
        try {
            short integer = Short.parseShort(string);
            integer = (short) (integer + 5);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the {@link String} provided contains a primitive-type 'int'
     * @param string the {@link String} to check
     * @return whether the {@link String} contains a primitive-type 'int'
     * @since 15
     */
    public static boolean isInteger(String string) {
        try {
            int integer = Integer.parseInt(string);
            integer = integer + 5;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the {@link String} provided contains a primitive-type 'long'
     * @param string the {@link String} to check
     * @return whether the {@link String} contains a primitive-type 'long'
     * @since 15
     */
    public static boolean isLong(String string) {
        try {
            long integer = Long.parseLong(string);
            integer = integer + 5L;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the {@link String} provided contains a primitive-type 'float'
     * @param string the {@link String} to check
     * @return whether the {@link String} contains a primitive-type 'float'
     * @since 15
     */
    public static boolean isFloat(String string) {
        try {
            float integer = Float.parseFloat(string);
            integer = integer + 5F;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the {@link String} provided contains a primitive-type 'double'
     * @param string the {@link String} to check
     * @return whether the {@link String} contains a primitive-type 'double'
     * @since 15
     */
    public static boolean isDouble(String string) {
        try {
            double doub = Double.parseDouble(string);
            doub = doub + 1D;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if your integer is between two integers or equal to either two integers
     * @param yourInteger your integer to match against the other two
     * @param integer1 the smaller integer
     * @param integer2 the larger integer
     * @return a boolean on whether the integer you provided is between or equal two the two other integers provided
     * @since 15
     */
    public static boolean isBetweenEquals(int yourInteger, int integer1, int integer2) {
        return yourInteger >= integer1 && yourInteger <= integer2;
    }

    /**
     * Checks if your long is between two longs or equal to either two longs
     * @param yourLong your long to match against the other two
     * @param long1 the smaller long
     * @param long2 the larger long
     * @return a boolean on whether the long you provided is between or equal two the two other longs provided
     * @since 15
     */
    public static boolean isBetweenEquals(long yourLong, long long1, long long2) {
        return yourLong >= long1 && yourLong <= long2;
    }

    /**
     * Checks if your short is between two shorts or equal to either two shorts
     * @param yourShort your short to match against the other two
     * @param short1 the smaller short
     * @param short2 the larger short
     * @return a boolean on whether the long you provided is between or equal two the two other shorts provided
     * @since 15
     */
    public static boolean isBetweenEquals(short yourShort, short short1, short short2) {
        return yourShort >= short1 && yourShort <= short2;
    }

    /**
     * Checks if your float is between two floats or equal to either two floats
     * @param yourFloat your float to match against the other two
     * @param float1 the smaller float
     * @param float2 the larger float
     * @return a boolean on whether the float you provided is between or equal two the two other floats provided
     * @since 15
     */
    public static boolean isBetweenEquals(float yourFloat, float float1, float float2) {
        return yourFloat >= float1 && yourFloat <= float2;
    }

    /**
     * Checks if your double is between two doubles or equal to either two doubles
     * @param yourDouble your double to match against the other two
     * @param double1 the smaller double
     * @param double2 the larger double
     * @return a boolean on whether the double you provided is between or equal two the two other doubles provided
     * @since 15
     */
    public static boolean isBetweenEquals(double yourDouble, double double1, double double2) {
        return yourDouble >= double1 && yourDouble <= double2;
    }

    /**
     * Checks if your byte is between two bytes or equal to either two bytes
     * @param yourByte your byte to match against the other two
     * @param byte1 the smaller byte
     * @param byte2 the larger byte
     * @return a boolean on whether the byte you provided is between or equal two the two other bytes provided
     * @since 15
     */
    public static boolean isBetweenEquals(byte yourByte, byte byte1, byte byte2) {
        return yourByte >= byte1 && yourByte <= byte2;
    }

    /**
     * Formats a double decimal a certain number of places and with a certain {@link RoundingMode}
     * @param value the value to format
     * @param places the amount of places to show. for example, "3" would be "3.141" and "5" would be "3.14159"
     * @param roundingMode the {@link RoundingMode} to use for the {@link DecimalFormat}
     * @return the decimal format in {@link String} form
     * @since 15
     */
    public static String formatDecimal(double value, int places, RoundingMode roundingMode) {
        DecimalFormat decimalFormat = new DecimalFormat("0." + "#".repeat(Math.max(0, places)));
        decimalFormat.setRoundingMode(roundingMode);
        return decimalFormat.format(value);
    }

    /**
     * Formats a double decimal a certain number of places and with an assumed rounding mode: {@link RoundingMode#HALF_EVEN}
     * @param value the value to format
     * @param places the amount of places to show. for example, "3" would be "3.141" and "5" would be "3.14159"
     * @return the decimal format in {@link String} form
     * @since 15
     */
    public static String formatDecimal(double value, int places) {
        return formatDecimal(value, places, RoundingMode.HALF_EVEN);
    }

    /**
     * Formats a long number to have commas in between (for example, "321,123")
     * @param number the number to format
     * @return the formatted long
     * @since 15
     */
    public static String formatLong(long number) {
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(true);

        return format.format(number);
    }

    /**
     * Gets a random integer between a minimum and a maximum
     * @param min the minimum number, should be smaller than and not equal to maximum
     * @param max the maximum number, should be bigger than and not equal to minimum
     * @return the random integer
     * @since 15
     */
    public static int randomInt(int min, int max) {
        if(min > max) throw new IllegalArgumentException("Minimum (" + min + ") cannot be greater than maximum (" + max + ")!");
        if(min == max) throw new IllegalArgumentException("Minimum (" + min + ") and maximum (" + max + ") cannot be the exact same number!");
        return new Random().nextInt((max - min) + 1) + min;
    }

}
