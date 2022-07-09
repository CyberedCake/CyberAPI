package net.cybercake.cyberapi.spigot.server.commands;

import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.chat.UChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

// and no, this isn't documented because, for the most part, it isn't used by non-CyberAPI classes
public class CommandManager {

    private static CommandManager commandManager = null;
    public static CommandManager commandManager() {
        if(commandManager == null) commandManager = new CommandManager();
        return commandManager;
    }

    public void init(String path) {
        try {
            long mss = System.currentTimeMillis();
            for(Class<?> clazz : (path == null ? new Reflections() : new Reflections(path)).getSubTypesOf(Command.class)) {
                Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                try {
                    command.getCommands().forEach(information -> {
                        registerCommand(command, information, getCommand(information.getName(), CyberAPI.getInstance()));
                    });
                } catch (Exception exception) {
                    CyberAPI.getInstance().getAPILogger().error("An error occurred whilst registering command /" + command.getMainCommand().getName() + " - " + command.getClass().getCanonicalName() + ": " + ChatColor.DARK_GRAY + exception);
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

    private void registerCommand(Command command, CommandInformation info, PluginCommand pluginCommand) {
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
        if(info.getAliases().length > 0) pluginCommand.setAliases(List.of(info.getAliases()));
        if(!info.getDescription().strip().equals("")) pluginCommand.setDescription(UChat.chat(info.getDescription()));
        if(!info.getPermission().strip().equals("")) pluginCommand.setPermission(info.getPermission());
        if(!info.getPermissionMessage().strip().equals("")) pluginCommand.setPermissionMessage(UChat.chat(info.getPermissionMessage()));
        if(!info.getUsage().strip().equals("")) pluginCommand.setUsage(UChat.chat(info.getUsage()));

        getCommandMap().register(CyberAPI.getInstance().getDescription().getName(), pluginCommand);
    }

    public PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command;
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            command = constructor.newInstance(name, plugin);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to get a " + PluginCommand.class.getCanonicalName() + " in " + this.getClass().getCanonicalName() + "!", exception);
        }
        return command;
    }

    public CommandMap getCommandMap() {
        CommandMap map = null;
        try {
            if(Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to get the command map in " + this.getClass().getCanonicalName() + "!", exception);
        }
        return map;
    }

}
