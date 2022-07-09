package net.cybercake.cyberapi.bungee.server.commands;

import net.cybercake.cyberapi.bungee.chat.UChat;
import net.cybercake.cyberapi.bungee.chat.UTabComp;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.List;

public abstract class Command extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

    /**
     * Creates an instance of {@link CommandInformation.Builder}, allowing you to customize the stored information on the command.
     * @param name the name of the command, without the slash
     * @return a new {@link CommandInformation.Builder} instance
     * @since 15
     */
    protected static CommandInformation.Builder newCommand(String name) { return CommandInformation.builder(name); }

    private final CommandInformation information;

    private Command() {
        super("", "", "");
        this.information = newCommand("").build();
    }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     */
    public Command(CommandInformation information) {
        super(information.getName(), information.getPermission(), information.getAliases());
        this.information = information;
        if(!information.getPermissionMessage().isEmpty()) setPermissionMessage(information.getPermissionMessage());
    }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     */
    public Command(CommandInformation.Builder information) {
        this(information.build());
    }

    /**
     * @return the main command attributed to this command
     * @since 41
     */
    public CommandInformation getMainCommand() { return information; }

    /**
     * The Bungee command's execution
     * @param sender the sender that executes the command
     * @param args the command arguments {@code sender} inputted
     * @return whether the command was successful or not
     * @since 15
     */
    public abstract boolean perform(CommandSender sender, CommandInformation information, String[] args);

    /**
     * The Bungee command's tab completions
     * @param sender the sender that is tab completing a command
     * @param args the command arguments {@code sender} has inputted so far
     * @return what to tab complete
     * @since 15
     */
    public abstract List<String> tab(CommandSender sender, CommandInformation information, String[] args);

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean perform = perform(sender, information, args);
        if(!perform) {
            sender.sendMessage(UChat.bComponent(information.getUsage()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tab = tab(sender, information, args);
        if(tab == null) return UTabComp.emptyList;
        return UTabComp.tabCompletions(information.getTabCompleteType(), List.of(args).get(args.length-1), tab);
    }
}
