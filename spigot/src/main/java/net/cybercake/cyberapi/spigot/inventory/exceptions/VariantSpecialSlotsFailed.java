package net.cybercake.cyberapi.spigot.inventory.exceptions;

import net.cybercake.cyberapi.spigot.inventory.SpecialSlots;
import org.jetbrains.annotations.ApiStatus;

/**
 * When the Special Slots variants fail. This is the {@link net.cybercake.cyberapi.spigot.inventory.SpecialSlots.SeparatorVariant separator variants} and
 * {@link net.cybercake.cyberapi.spigot.inventory.SpecialSlots.CheckerboardVariant checkerboard variants} of the world.
 * @since 143
 */
@ApiStatus.Internal
public class VariantSpecialSlotsFailed extends IllegalStateException {

    /**
     * Message supered: <br>
     * <em>Special Slots variant %%enum_name%% failed to return a list of Pairs. This is likely not your fault.
     * Please report to CyberAPI or use the alternate method: %%enum_name%%(%%enum_class_canonical%%)</em>
     * @since 143
     */
    @ApiStatus.Internal
    public VariantSpecialSlotsFailed(SpecialSlots.SimplifiedGUIEnums enumInQuestion, Throwable cause) {
        super("Special Slots variant '" + enumInQuestion.getName() + "' failed to return a list of Pairs. This is likely not your fault. Please report to CyberAPI or use the alternate method: " + enumInQuestion.getName() + "(" + enumInQuestion.getClass().getCanonicalName() + ")", cause);
    }

}
