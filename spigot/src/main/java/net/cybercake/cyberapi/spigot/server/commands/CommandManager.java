package net.cybercake.cyberapi.spigot.server.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.chat.UChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

// and no, this isn't documented because, for the most part, it isn't used by non-CyberAPI classes
public class CommandManager {

    private static CommandManager commandManager = null;
    public static CommandManager commandManager() {
        if(commandManager == null) commandManager = new CommandManager();
        return commandManager;
    }

    private static final HashMap<String, String> fakeCommandsList = new HashMap<>();
    public static HashMap<String, String> getFakeCommands() { return fakeCommandsList; }

    public void init(String path) {
        try {
            long mss = System.currentTimeMillis();
            for(Class<?> clazz : (path == null ? new Reflections() : new Reflections(path)).getSubTypesOf(Command.class)) {
                Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                try {
                    command.getCommands().forEach(information -> {
                        if(information.getName().chars().anyMatch(Character::isUpperCase)) { // (for name) if an uppercase letter exists, kick into "create alias to simulate uppercase letters" mode
                            String upperCaseInclusiveString = information.getName();

                            try { // reflection methods
                                Field nameField = information.getCommandInformationBuilder().getClass().getDeclaredField("name");
                                nameField.setAccessible(true);
                                nameField.set(information.getCommandInformationBuilder(), upperCaseInclusiveString.toLowerCase(Locale.ROOT));

                                // add to aliases
                                List<String> aliases = new ArrayList<>(Arrays.asList(information.getAliases()));
                                aliases.add(upperCaseInclusiveString);
                                Field aliasesField = information.getCommandInformationBuilder().getClass().getDeclaredField("aliases");
                                aliasesField.setAccessible(true);
                                aliasesField.set(information.getCommandInformationBuilder(), aliases.toArray(String[]::new));

                                fakeCommandsList.put(upperCaseInclusiveString, "remove:" + upperCaseInclusiveString.toLowerCase(Locale.ROOT));
                            } catch (NoSuchFieldException | IllegalAccessException reflectionException) {
                                CyberAPI.getInstance().getAPILogger().error("Failed to automatically fix uppercase-containing command: " + information.getName() + " (maybe try removing any uppercase letters in it's name?)");
                                CyberAPI.getInstance().getAPILogger().verboseException(reflectionException);
                            }
                        }
                        if(Arrays.stream(information.getAliases()).anyMatch(alias -> alias.chars().anyMatch(Character::isUpperCase))) { // (for aliases) if an uppercase letter exists, kick into "create alias to simulate uppercase letters" mode
                            for(String alias : information.getAliases()) {
                                if(alias.equalsIgnoreCase(information.getName())) continue; // don't wanna re-do what we already did for prev
                                if(alias.chars().noneMatch(Character::isUpperCase)) continue; // no uppercase letters in that alias :(
                                fakeCommandsList.put(alias, information.getName());
                            }
                        }
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
        boolean commodoreSupported = me.lucko.commodore.CommodoreProvider.isSupported();

        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
        if(info.getAliases().length > 0) pluginCommand.setAliases(List.of(info.getAliases()));
        if(!info.getDescription().strip().equals("")) pluginCommand.setDescription(UChat.chat(info.getDescription()));
        if(!info.getPermission().strip().equals("")) pluginCommand.setPermission(info.getPermission());
        if(!info.getPermissionMessage().strip().equals("")) pluginCommand.setPermissionMessage(UChat.chat(info.getPermissionMessage()));
        if(!info.getUsage().strip().equals("")) pluginCommand.setUsage(UChat.chat(info.getUsage()));
        if(commodoreSupported && info.shouldUseFolderCommodore()) registerCommodore(pluginCommand, info.getName());
        if(commodoreSupported && info.getCommodoreNode() != null) registerCommodore(pluginCommand, info.getCommodoreNode());

        getCommandMap().register(CyberAPI.getInstance().getDescription().getName(), pluginCommand);
    }

    public void registerCommodore(PluginCommand pluginCommand, String fileName) {
        try {
            @Nullable InputStream resource = CyberAPI.getInstance().getResource("commodore/" + fileName + ".commodore");
            if(resource == null) {
                CyberAPI.getInstance().getAPILogger().warn("Failed to register commodore for '/" + pluginCommand.getName() + "': File '" + pluginCommand.getName() + ".commodore' does not exist in 'commodore' folder in " + CyberAPI.getInstance().getPluginName() + "'s resource folder!");
                return;
            }
            LiteralCommandNode<?> commandProvided = me.lucko.commodore.file.CommodoreFileReader.INSTANCE.parse(resource);
            me.lucko.commodore.CommodoreProvider.getCommodore(CyberAPI.getInstance()).register(pluginCommand, commandProvided);
        } catch (Exception exception) {
            CyberAPI.getInstance().getAPILogger().warn("Failed to register commodore for '/" + pluginCommand.getName() + "': " + exception);
            CyberAPI.getInstance().getAPILogger().verboseException(exception);
        }
    }

    public void registerCommodore(PluginCommand pluginCommand, LiteralArgumentBuilder<?> node) {
        try {
            me.lucko.commodore.CommodoreProvider.getCommodore(CyberAPI.getInstance()).register(pluginCommand, node);
        } catch (Exception exception) {
            CyberAPI.getInstance().getAPILogger().warn("Failed to register commodore for '/" + pluginCommand.getName() + "': " + exception);
            CyberAPI.getInstance().getAPILogger().verboseException(exception);
        }
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
