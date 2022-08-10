package net.cybercake.cyberapi.bungee;

import com.google.common.collect.ImmutableList;
import net.cybercake.cyberapi.common.builders.settings.FeatureSupport;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
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
            List<StackTraceElement> elements = new ArrayList<>(Arrays.stream(thread.getStackTrace()).toList());
            elements.remove(0); // remove 0 because "java.lang.Thread" is not important to what I'm doing here
            return getFirstNonCyberAPIStack(elements);
        } catch (Exception ignored) {}
        return null;
    }

    public static @Nullable String getFirstNonCyberAPIStack(List<StackTraceElement> elements) {
        List<String> CLASSES_TO_AVOID = ImmutableList.of("net.cybercake.cyberapi", "org.bukkit.plugin");
        for(StackTraceElement element : elements) {
            if(CLASSES_TO_AVOID.stream()
                    .filter(clazz -> element.getClassName().contains(clazz))
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