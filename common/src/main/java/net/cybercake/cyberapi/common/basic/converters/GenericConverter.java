package net.cybercake.cyberapi.common.basic.converters;

import net.cybercake.cyberapi.common.basic.Pair;

/**
 * Any converter that should be of an instance should extend this class. This is what every CyberAPI converter should extend.
 * @param <I> the input type
 * @param <O> the output type
 * @see GenericBase64Converter
 * @since 144
 * @author CyberedCake
 */
public interface GenericConverter<I, O> {

    /**
     * Gets the types involved in the converter transaction
     * @return the first item in the {@link Pair pair} is the <strong>input</strong> type in the form of a {@link Class class} whereas
     *         the second item will be the <strong>output</strong> type in the form of a {@link Class class}
     * @since 144
     */
    Pair<Class<I>, Class<O>> getTypes();

    /**
     * Converts the suggested input of type "{@code I}" into the appropriate output of type "{@code O}"
     * @param input the input object of required type
     * @return the output object after the conversion
     * @since 146
     */
    O to(I input);

    /**
     * Converts the suggested output of type "{@code O}" into the appropriate input of type "{@code I}"
     * @param output the output object of required type
     * @return the input object from before the conversion
     * @since 146
     */
    I from(O output);

}
