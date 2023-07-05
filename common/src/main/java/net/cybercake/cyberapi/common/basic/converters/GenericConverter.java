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

}
