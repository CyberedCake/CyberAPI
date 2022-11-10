package net.cybercake.cyberapi.bungee;

import com.google.common.collect.ImmutableList;
import net.cybercake.cyberapi.common.builders.settings.FeatureSupport;

import javax.annotation.Nullable;
import java.util.List;

/**
 * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
 * <br> <br>
 * Validators that are used within CyberAPI to validate arguments, server states, etc.
 */
public class Validators {

    private static UnsupportedOperationException dependency(String name) {
        return new UnsupportedOperationException(name + " is not a dependency of " + CyberAPI.getInstance().getPluginName() + ", and therefore method " + getCaller() + " does not work!");
    }

    private static UnsupportedOperationException serverHook(String name) {
        return new UnsupportedOperationException(name + " is not in the server's plugin folder, and therefore method " + getCaller() + " does not work!");
    }

    public static void validateIsNotAuto(FeatureSupport featureSupport) {
        if(featureSupport.equals(FeatureSupport.AUTO)) throw new IllegalStateException("Feature Support cannot be set to auto in CyberAPI instance. Maybe it hasn't finished loading yet? (" + featureSupport.getFeature() + ")");
    }

    public static @Nullable String getCaller() { return getCaller(Thread.currentThread()); }

    public static @Nullable String getCaller(Thread thread) {
        try {
            StackTraceElement[] elements = thread.getStackTrace();
            return getFirstNonCyberAPIStack(elements);
        } catch (Exception ignored) {}
        return null;
    }

    public static @Nullable String getFirstNonCyberAPIStack(StackTraceElement[] elements) {
        List<String> PACKAGES_TO_AVOID = ImmutableList.of(
                "net.cybercake.cyberapi",
                "jdk.internal",
                "java.lang",
                "io.netty",
                "net.md_5.bungee",
                "net.minecraft"
        );
        for(StackTraceElement element : elements) {
            if(PACKAGES_TO_AVOID.stream()
                    .filter(clazz -> element.getClassName().startsWith(clazz))
                    .findFirst()
                    .orElse(null)
                    != null)
                continue;
            return element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
        }
        return null;
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Weird code, I know, but checks if a {@link ClassCastException} is not thrown when using {@link Class#asSubclass(Class)}
     */
    public static boolean isSubtype(Class<?> clazz, Class<?> subtype) {
        try {
            clazz.asSubclass(subtype);
            return true;
        } catch (ClassCastException ignored) { // ignored because it's not needed to determine the outcome
            return false;
        }
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Validates that Adventure API is supported and working
     */
    public static void validateAdventureSupport() {
        validateIsNotAuto(CyberAPI.getInstance().getAdventureAPISupport());
        if(!CyberAPI.getInstance().getAdventureAPISupport().equals(FeatureSupport.SUPPORTED)) throw dependency("Adventure API");
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Validates that Mini Message is supported and working
     */
    public static void validateMiniMessageSupport() {
        validateIsNotAuto(CyberAPI.getInstance().getMiniMessageSupport());
        validateAdventureSupport();
        if(!CyberAPI.getInstance().getMiniMessageSupport().equals(FeatureSupport.SUPPORTED)) throw dependency("MiniMessage");
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Validates that LuckPerms is supported and working
     */
    public static void validateLuckPermsHook() {
        validateIsNotAuto(CyberAPI.getInstance().getLuckPermsSupport());
        if(!CyberAPI.getInstance().getLuckPermsSupport().equals(FeatureSupport.SUPPORTED)) throw serverHook("LuckPerms");
    }

    /**
     * --{@literal >} <b>MAINLY FOR USE INSIDE CYBERAPI ONLY</b> {@literal <}--
     * <br><br>
     * Validates that Protocolize is supported and working
     */
    public static void validateProtocolizeHook() {
        validateIsNotAuto(CyberAPI.getInstance().getProtocolizeSupport());
        if(!CyberAPI.getInstance().getProtocolizeSupport().equals(FeatureSupport.SUPPORTED)) throw serverHook("Protocolize");
    }

}