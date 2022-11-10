package net.cybercake.cyberapi.bungee.server.listeners;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.bungee.Validators;
import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * and no, this isn't documented because, for the most part, it is internal
 * @apiNote **<b>any methods in this class are subject to change without warning</b>**
 */
public class ListenerManager {

    private static ListenerManager listenerManager = null;
    public static ListenerManager listenerManager() {
        if(listenerManager == null) listenerManager = new ListenerManager();
        return listenerManager;
    }

    public static int autoRegisteredListeners;

    public void init(String path) {
        try {
            long mss = System.currentTimeMillis();
            for(Class<?> clazz : CyberAPI.getInstance().getPluginClasses()) {
                if(!(Validators.isSubtype(clazz, BungeeListener.class)) && !(Validators.isSubtype(clazz, BungeeListener.class))) continue;
                Listener listener;
                try {
                    listener = (Listener) clazz.getDeclaredConstructors()[0].newInstance();
                } catch (InvocationTargetException invocationTargetException) {
                    if(CyberAPI.getInstance().getDescription().getMain().startsWith(clazz.getPackageName())) {
                        listener = (Listener) CyberAPI.getInstance();
                    }else{
                        throw invocationTargetException;
                    }
                }
                try {
                    if(CyberAPI.getInstance().getSettings().getDisabledAutoRegisteredClasses() != null && Arrays.asList(CyberAPI.getInstance().getSettings().getDisabledAutoRegisteredClasses()).contains(clazz)) continue;
                    autoRegisteredListeners++;
                    CyberAPI.getInstance().registerListener(listener);
                    CyberAPI.getInstance().getAPILogger().verbose("Registered listener automatically: " + clazz.getCanonicalName());
                } catch (Exception exception) {
                    CyberAPI.getInstance().getAPILogger().error("An error occurred whilst registering listener at " + clazz.getCanonicalName() + " - " + clazz.getConstructors()[0].getName() + ": " + ChatColor.DARK_GRAY + exception);
                    CyberAPI.getInstance().getAPILogger().verboseException(exception);
                }
            }
            if(path == null) {
                Method method = CyberAPI.class.getDeclaredMethod("startCyberAPI", Settings.class);
                CyberAPI.getInstance().getAPILogger().warn("Please specify a main package path to speed up listener registering in " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "(" + Settings.class.getCanonicalName() + ")! (registering took " + (System.currentTimeMillis()-mss) + "ms!)");
            }
        } catch (Exception exception) {
            throw new RuntimeException("An error occurred while registering listeners!", exception);
        }
    }

}
