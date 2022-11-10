package net.cybercake.cyberapi.bungee.server.commands;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.bungee.Validators;
import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.md_5.bungee.api.ChatColor;

import java.lang.reflect.Method;
import java.util.Arrays;

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

    public static int autoRegisteredCommands = 0;

    public void init(String path) {
        try {
            long mss = System.currentTimeMillis();
            for(Class<?> clazz : CyberAPI.getInstance().getPluginClasses()) {
                if(!(Validators.isSubtype(clazz, BungeeCommand.class)) && !(Validators.isSubtype(clazz, Command.class))) continue;
                BungeeCommand command = (BungeeCommand) clazz.getDeclaredConstructors()[0].newInstance();
                try {
                    if(!command.getMainCommand().shouldAutoRegister()) continue;
                    if(CyberAPI.getInstance().getSettings().getDisabledAutoRegisteredClasses() != null && Arrays.asList(CyberAPI.getInstance().getSettings().getDisabledAutoRegisteredClasses()).contains(clazz)) continue;
                    autoRegisteredCommands++;
                    resolveInformationAndRegister(command);
                    CyberAPI.getInstance().getAPILogger().verbose("Registered command automatically: " + clazz.getCanonicalName() + " -> /" + command.getMainCommand().getName() + " (with aliases: " + String.join(", ", Arrays.stream(command.getAliases()).map(alias -> "/" + alias).toArray(String[]::new)) + ")");
                } catch (Exception exception) {
                    CyberAPI.getInstance().getAPILogger().error("An error occurred whilst registering command /" + command.getName() + ": " + ChatColor.DARK_GRAY + exception);
                    CyberAPI.getInstance().getAPILogger().verboseException(exception);
                }
            }
            if(path == null) {
                Method method = CyberAPI.class.getDeclaredMethod("startCyberAPI", Settings.class);
                CyberAPI.getInstance().getAPILogger().warn("Please specify a main package to speed up command registering in " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "(" + Settings.class.getCanonicalName() + ")! (registering took " + (System.currentTimeMillis()-mss) + "ms!)");
            }
        } catch (Exception exception) {
            throw new RuntimeException("An error occurred while registering commands!", exception);
        }
    }

    public void resolveInformationAndRegister(BungeeCommand command) {
        CyberAPI.getInstance().registerCommand((net.md_5.bungee.api.plugin.Command) command);
    }

}
