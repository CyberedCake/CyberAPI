package net.cybercake.cyberapi.common.basic.converters.romannumerals;

import org.jetbrains.annotations.ApiStatus;

/**
 * Only thrown by CyberAPI when {@link NumeralConverter} exceeds its allocated amount of loops.
 * @since 146
 */
@ApiStatus.Internal
public class ExcessiveLoopsException extends RuntimeException {

    @ApiStatus.Internal
    public ExcessiveLoopsException(String message) {
        super(message);
    }

}
