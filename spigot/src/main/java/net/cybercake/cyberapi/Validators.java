package net.cybercake.cyberapi;

import net.cybercake.cyberapi.settings.Settings;

/**
 * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
 * <br> <br>
 * Validators that are used within CyberAPI to validate arguments, server states, etc.
 */
public class Validators {

    public static void validateIsNotAuto(Settings.FeatureSupport featureSupport) {
        if(featureSupport.equals(Settings.FeatureSupport.AUTO)) throw new IllegalStateException("Feature Support cannot be set to auto in CyberAPI instance. Maybe it hasn't finished loading yet? (" + featureSupport.getFeature() + ")");
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Validates that Adventure API is supported and working
     */
    public static void validateAdventureSupport() {
        validateIsNotAuto(CyberAPI.getInstance().getAdventureAPISupport());
        if(!CyberAPI.getInstance().getAdventureAPISupport().equals(Settings.FeatureSupport.SUPPORTED)) throw new UnsupportedOperationException("Adventure API in " + CyberAPI.getInstance().getPluginName() + " is not marked as supported in CyberAPI!");
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Validates that Mini Message is supported and working
     */
    public static void validateMiniMessageSupport() {
        validateIsNotAuto(CyberAPI.getInstance().getMiniMessageSupport());
        validateAdventureSupport();
        if(!CyberAPI.getInstance().getMiniMessageSupport().equals(Settings.FeatureSupport.SUPPORTED)) throw new UnsupportedOperationException("MiniMessage in " + CyberAPI.getInstance().getPluginName() + " is not marked as supported in CyberAPI!");
    }

    public static void validateLuckPermsHook() {
        validateIsNotAuto(CyberAPI.getInstance().getLuckPermsSupport());
        if(!CyberAPI.getInstance().getLuckPermsSupport().equals(Settings.FeatureSupport.SUPPORTED)) throw new UnsupportedOperationException("LuckPerms in " + CyberAPI.getInstance().getPluginName() + " is not marked as supported in CyberAPI!");
    }

}
