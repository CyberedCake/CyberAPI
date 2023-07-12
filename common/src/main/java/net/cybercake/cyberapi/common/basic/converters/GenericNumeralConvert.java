package net.cybercake.cyberapi.common.basic.converters;

import net.cybercake.cyberapi.common.basic.converters.romannumerals.NumeralConverter;

import java.lang.constant.Constable;

/**
 * A converter to and from specific numeral systems
 * @param <I> the input numeral type (has to extend {@link Number})
 * @param <O> the output numeral type (has to extend {@link Number})
 * @since 146
 * @author CyberedCake
 */
public interface GenericNumeralConvert<I extends Number, O extends Constable> extends GenericConverter<I, O> {

    GenericNumeralConvert<Integer, String> ROMAN_NUMERALS = new NumeralConverter.InternalConvertRomanNumerals();

}
