package net.cybercake.cyberapi.spigot.inventory.exceptions;

import net.cybercake.cyberapi.spigot.Validators;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * Very internal class, do not touch please. Throwing this will essentially only throw an {@link IllegalStateException}.
 * @since 143
 * @apiNote internal class
 */
@ApiStatus.Internal
public class HaltConsumerListException extends IllegalStateException {

    /**
     * Very internal constructor, do not touch please.
     * @since 143
     * @apiNote internal constructor
     */
    @ApiStatus.Internal
    public HaltConsumerListException() {
        super(Consumer.class.getCanonicalName() + " was not meant to continue functioning. Report this error to CyberAPI if you see it. (potential cause: " + Validators.getCaller() + ")");
    }

}
