package net.cybercake.cyberapi.server.commands;

import net.cybercake.cyberapi.chat.TabCompleteType;
import net.cybercake.cyberapi.chat.UChat;
import net.cybercake.cyberapi.chat.UTabComp;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.List;

public abstract class Command extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

    public static class CommandInformation {
        private final String name;
        private String permission = "";
        private String usage = "";
        private String[] aliases = new String[]{};
        private TabCompleteType tabCompleteType;

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
         * @since 3.3
         */
        public CommandInformation setPermission(String permission) {
            this.permission = permission; return this;
        }

        /**
         * Sets the correct usage of the command
         * @param usage the command's correct usage
         * @since 3.3
         */
        public CommandInformation setUsage(String usage) {
            this.usage = usage; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command
         * @since 3.3
         */
        public CommandInformation setAliases(String... aliases) {
            this.aliases = aliases; return this;
        }

        /**
         * Sets the aliases of the command, a.k.a. new commands that do the same thing as this command
         * @param aliases the aliases of the command, in {@link List} form
         * @since 3.3
         */
        public CommandInformation setAliases(List<String> aliases) {
            this.aliases = aliases.toArray(new String[0]); return this;
        }

        /**
         * Sets the types of tab completions for {@link Command#tab(CommandSender, String[])}
         * @param tabCompleteType the type of tab completions
         * @since 3.3
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
     * @since 3.3
     */
    protected static CommandInformation newCommand(String name) { return new CommandInformation(name); }

    private final CommandInformation information;

    private Command() {
        super("", "", "");
        this.information = newCommand("");
    }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     */
    public Command(CommandInformation information) {
        super(information.name, information.permission, information.aliases);
        this.information = information;
    }

    /**
     * @return the name of the command with the slash omitted
     * @since 3.3
     */
    public String getName() { return information.name; }

    /**
     * @return the permission required to execute the command
     * @since 3.3
     */
    public String getPermission() { return information.permission; }

    /**
     * @return the correct usage of the command
     * @since 3.3
     */
    public String getUsage() { return information.usage; }

    /**
     * @return the aliases (other ways to execute the main command)
     * @since 3.3
     */
    public String[] getAliases() { return information.aliases; }

    /**
     * @return the tab complete type
     * @since 3.3
     * @see UTabComp#tabCompletions(TabCompleteType, String, List)
     */
    public TabCompleteType getTabCompleteType() { return information.tabCompleteType; }

    /**
     * The Bungee command's execution
     * @param sender the sender that executes the command
     * @param args the command arguments {@code sender} inputted
     * @return whether the command was successful or not
     * @since 3.3
     */
    public abstract boolean perform(CommandSender sender, String[] args);

    /**
     * The Bungee command's tab completions
     * @param sender the sender that is tab completing a command
     * @param args the command arguments {@code sender} has inputted so far
     * @return what to tab complete
     * @since 3.3
     */
    public abstract List<String> tab(CommandSender sender, String[] args);

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean perform = perform(sender, args);
        if(!perform) {
            sender.sendMessage(UChat.bComponent(getUsage()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tab = tab(sender, args);
        if(tab == null) return UTabComp.emptyList;
        return UTabComp.tabCompletions(getTabCompleteType(), List.of(args).get(args.length-1), tab);
    }
}
