package net.cybercake.cyberapi.bungee.server.commands;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.md_5.bungee.api.ChatColor;
import org.reflections.Reflections;

import java.lang.reflect.Method;

/**
 * and no, this isn't documented because, for the most part, it is internal
 * @apiNote **<b>any methods in this class are subject to change without warning</b>**
 */
public class CommandManager {

    private static CommandManager commandManager = null;
    public static CommandManager commandManager() {
        if(commandManager == null) commandManager = new CommandManager();
        return commandManager;
    }

    public void init(String path) {
        try {
            long mss = System.currentTimeMillis();
            for(Class<?> clazz : (path == null ? new Reflections() : new Reflections(path)).getSubTypesOf(BungeeCommand.class)) {
                BungeeCommand command;
                try {
                    command = (BungeeCommand) clazz.getDeclaredConstructor().newInstance();
                } catch (Exception exception) {
                    throw new IllegalStateException(exception + ": " + exception.getMessage() + " || potentially remove any constructor arguments, as CyberAPI searches for no arguments in a command constructor", exception);
                }
                try {
                    if(!command.getMainCommand().shouldAutoRegister()) break;
                    resolveInformationAndRegister(command);
                } catch (Exception exception) {
                    CyberAPI.getInstance().getAPILogger().error("An error occurred whilst registering command /" + command.getName() + ": " + ChatColor.DARK_GRAY + exception);
                    CyberAPI.getInstance().getAPILogger().verboseException(exception);
                }
            }
            if(path == null) {
                Method method = CyberAPI.class.getDeclaredMethod("startCyberAPI", Settings.class);
                CyberAPI.getInstance().getAPILogger().warn("Please specify a commands path/package to speed up command registering in " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "(" + Settings.class.getCanonicalName() + ")! (registering took " + (System.currentTimeMillis()-mss) + "ms!)");
            }
        } catch (Exception exception) {
            throw new RuntimeException("An error occurred while registering commands!", exception);
        }
    }

    public void resolveInformationAndRegister(BungeeCommand command) {
        CyberAPI.getInstance().registerCommand(command);
    }

}
