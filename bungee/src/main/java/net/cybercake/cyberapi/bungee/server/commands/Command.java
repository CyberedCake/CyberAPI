package net.cybercake.cyberapi.bungee.server.commands;

import net.md_5.bungee.api.CommandSender;

/**
 * Represents a command in CyberAPI
 * @deprecated please use {@link BungeeCommand} instead, as this is kinda generic naming-wise
 */
@Deprecated
public abstract class Command extends BungeeCommand {

    /**
     * Creates an instance of {@link CommandInformation.Builder}, allowing you to customize the stored information on the command.
     * @param name the name of the command, without the slash
     * @return a new {@link CommandInformation.Builder} instance
     * @since 15
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated protected static CommandInformation.Builder newCommand(String name) { return CommandInformation.builder(name); }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    public Command(CommandInformation information) {
        super(information);
    }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    public Command(CommandInformation.Builder information) {
        super(information);
    }

    /**
     * Cancels the cooldown for a specific {@link CommandSender user}, but only for a specific {@link CommandInformation command}
     * @param information the command that the cooldown needs to be cancelled for
     * @param sender the user the cooldown should affect
     * @since 79
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    @Override
    public void cancelCooldown(CommandSender sender, CommandInformation information) {
        super.cancelCooldown(sender, information);
    }

    /**
     * @return the main command attributed to this command
     * @since 41
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Override
    @Deprecated
    public CommandInformation getMainCommand() {
        return super.getMainCommand();
    }

    /**
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Override
    @Deprecated
    public boolean hasPermission(CommandSender sender) {
        return super.hasPermission(sender);
    }

    /**
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Override
    @Deprecated
    public String getName() {
        return super.getName();
    }

    /**
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Override
    @Deprecated
    public String getPermission() {
        return super.getPermission();
    }

    /**
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Override
    @Deprecated
    public String[] getAliases() {
        return super.getAliases();
    }

    /**
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Override
    @Deprecated
    public String getPermissionMessage() {
        return super.getPermissionMessage();
    }

    /**
     * @deprecated please use {@link BungeeCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Override
    @Deprecated
    protected void setPermissionMessage(String permissionMessage) {
        super.setPermissionMessage(permissionMessage);
    }
}
