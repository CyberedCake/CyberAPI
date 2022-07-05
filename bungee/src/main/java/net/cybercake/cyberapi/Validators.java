package net.cybercake.cyberapi;

import net.cybercake.cyberapi.settings.FeatureSupport;

/**
 * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
 * <br> <br>
 * Validators that are used within CyberAPI to validate arguments, server states, etc.
 */
public class Validators {

    public static void validateIsNotAuto(FeatureSupport featureSupport) {
        if(featureSupport.equals(FeatureSupport.AUTO)) throw new IllegalStateException("Feature Support cannot be set to auto in CyberAPI instance. Maybe it hasn't finished loading yet? (" + featureSupport.getFeature() + ")");
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Validates that LuckPerms is supported and working
     */
    public static void validateLuckPermsHook() {
        validateIsNotAuto(CyberAPI.getInstance().getLuckPermsSupport());
        if(!CyberAPI.getInstance().getLuckPermsSupport().equals(FeatureSupport.SUPPORTED)) throw new UnsupportedOperationException("LuckPerms in " + CyberAPI.getInstance().getPluginName() + " is not marked as supported in CyberAPI!");
    }

}