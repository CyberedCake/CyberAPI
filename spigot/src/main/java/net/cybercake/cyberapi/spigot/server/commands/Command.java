package net.cybercake.cyberapi.spigot.server.commands;

import net.cybercake.cyberapi.spigot.chat.TabCompleteType;
import net.cybercake.cyberapi.spigot.chat.UTabComp;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"unused"})
public abstract class Command implements CommandExecutor, TabCompleter {

    public static class CommandInformation {
        private final String name;
        private String permission = "";
        private String permissionMessage = "";
        private String description = "";
        private String usage = "";
        private String[] aliases = new String[]{};
        private TabCompleteType tabCompleteType = TabCompleteType.NONE;
        
        /**
         * Creates an instance of {@link CommandInformation}, allowing you to customize the stored information on the command.
         * @param name the name of the command, without the slash
         * @since 3.2
         * @deprecated there is no reason to make a {@link CommandInformation} this way, please use {@link Command#newCommand(String)}
         */
        @Deprecated
        @SuppressWarnings({"all"})
        public CommandInformation(String name) {
            this.name = name;
        }

        /**
         * Sets the permission required to run the command
         * @param permission the required permission
         * @since 3.2
         */
        public CommandInformation setPermission(String permission) {
            this.permission = permission; return this;
        }

        /**
         * Sets the permission required to run the command and the message that sends if that permission is not met
         * @param permission the required permission
         * @param permissionMessage what to send if {@code permission} is not met
         * @since 3.2
         */
        public CommandInformation setPermission(String permission, String permissionMessage) {
            this.permission = permission; this.permissionMessage = permissionMessage; return this;
        }

        /**
         * Sets the description of the command
         * @param description the command's description
         * @since 3.2
         */
        public CommandInformation setDescription(String description) {
            this.description = description; return this;
        }

        /**
         * Sets the correct usage of the command
         * @param usage the command's correct usage
         * @since 3.2
         */
        public CommandInformation setUsage(String usage) {
            this.usage = usage; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command
         * @since 3.2
         */
        public CommandInformation setAliases(String... aliases) {
            this.aliases = aliases; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command, in {@link List} form
         * @since 3.2
         */
        public CommandInformation setAliases(List<String> aliases) {
            this.aliases = aliases.toArray(new String[0]); return this;
        }

        /**
         * Sets the types of tab completions for {@link Command#tab(CommandSender, org.bukkit.command.Command, String, String[])}
         * @param tabCompleteType the type of tab completions
         * @since 3.2
         * @see UTabComp#tabCompletions(TabCompleteType, String, List)
         */
        public CommandInformation setTabCompleteType(TabCompleteType tabCompleteType) {
            this.tabCompleteType = tabCompleteType; return this;
        }
    }

    /**
     * Creates an instance of {@link CommandInformation}, allowing you to customize the stored information on the command.
     * @param name the name of the command, without the slash
     * @return a new {@link CommandInformation} instance
     * @since 3.2
     */
    protected static CommandInformation newCommand(String name) {
        return new CommandInformation(name);
    }

    private final CommandInformation information;
    private Command() { this.information = newCommand(""); }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     */
    public Command(CommandInformation information) {
        this.information = information;
    }

    /**
     * @return the name of the command with the slash omitted
     * @since 3.2
     */
    public String getName() { return information.name; }

    /**
     * @return the permission required to execute the command
     * @since 3.2
     */
    public String getPermission() { return information.permission; }

    /**
     * @return the permission message that is sent when the sender doesn't have the correct permission
     * @since 3.2
     */
    public String getPermissionMessage() { return information.permissionMessage; }

    /**
     * @return the description of the command
     * @since 3.2
     */
    public String getDescription() { return information.description; }

    /**
     * @return the correct usage of the command
     * @since 3.2
     */
    public String getUsage() { return information.usage; }

    /**
     * @return the aliases (other ways to execute the main command)
     * @since 3.2
     */
    public String[] getAliases() { return information.aliases; }

    /**
     * @return the tab complete type
     * @since 3.2
     * @see UTabComp#tabCompletions(TabCompleteType, String, List)
     */
    public TabCompleteType getTabCompleteType() { return information.tabCompleteType; }
    
    /**
     * The Bukkit command's execution
     * @param sender the sender that executes the command
     * @param label the command name {@code sender} ran
     * @param args the command arguments {@code sender} inputted
     * @return whether the command was successful or not
     * @since 3.2
     */
    public abstract boolean perform(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args);

    /**
     * The Bukkit command's tab completions
     * @param sender the sender that is tab completing a command
     * @param label the command name {@code sender} is attempting to run
     * @param args the command arguments {@code sender} has inputted so far
     * @return what to tab complete
     * @since 3.2
     */
    public abstract List<String> tab(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        return perform(commandSender, command, command.getLabel(), strings);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tab = tab(commandSender, command, command.getLabel(), strings);
        if(tab == null) return UTabComp.emptyList;
        return UTabComp.tabCompletions(getTabCompleteType(), List.of(strings).get(strings.length-1), tab);
    }
}
