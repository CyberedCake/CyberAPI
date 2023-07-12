package net.cybercake.cyberapi.common.basic.converters;

/**
 * A converter to and from Base64
 * @param <I> the input type
 * @param <B> the Base64 type, most commonly a {@link String}
 * @since 144
 * @author CyberedCake
 */
public interface GenericBase64Converter<I, B> extends GenericConverter<I, B> {

    /**
     * Converts an object to Base64
     * @param converter the converter instance to use
     * @param object the object to convert to a {@link String Base64 string}
     * @return the {@link String Base64 string}
     * @param <I> the input type
     * @throws IllegalStateException on any fail
     * @since 144
     */
    static <I> String toBase64(GenericBase64Converter<I, String> converter, I object) throws IllegalStateException {
        try {
            return converter.convertToBase64(object);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to convert '" + converter.getTypes().getFirstItem().getCanonicalName() + "' to Base64", exception);
        }
    }

    /**
     * Converts an object from Base64 back into its native Java object
     * @param converter the converter instance to use
     * @param base64 the {@link String Base64 string} to convert into an object
     * @return the newly-converted object
     * @param <I> the input type
     * @throws IllegalStateException on any fail
     * @since 144
     */
    static <I> I fromBase64(GenericBase64Converter<I, String> converter, String base64) {
        try {
            return converter.convertFromBase64(base64);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to convert Base64 to '" + converter.getTypes().getFirstItem().getCanonicalName() + "'", exception);
        }
    }

    /**
     * Converts an object into Base64
     * @param input the object to convert to Base64
     * @return the, most commonly {@link String string}, representation of the Base64 of the inputted object
     * @since 146
     * @see GenericBase64Converter#convertFromBase64(Object)
     */
    @Override
    default B to(I input) {
        return convertToBase64(input);
    }

    /**
     * Converts a Base64 ({@link String string usually}) object into its native Java counterpart
     * @param output the Base64 to convert back into an object, most commonly {@link String a string}
     * @return the new object from Base64
     * @since 146
     * @see GenericBase64Converter#convertToBase64(Object)
     */
    @Override
    default I from(B output) {
        return convertFromBase64(output);
    }

    /**
     * Converts an object into Base64
     * @param input the object to convert to Base64
     * @return the, most commonly {@link String string}, representation of the Base64 of the inputted object
     * @since 144
     * @see GenericBase64Converter#convertFromBase64(Object)
     */
    B convertToBase64(I input) ;

    /**
     * Converts a Base64 ({@link String string usually}) object into its native Java counterpart
     * @param data the Base64 to convert back into an object, most commonly {@link String a string}
     * @return the new object from Base64
     * @since 144
     * @see GenericBase64Converter#convertToBase64(Object)
     */
    I convertFromBase64(B data) ;

}
