package net.cybercake.cyberapi.spigot.server.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a command in CyberAPI
 * @deprecated please use {@link SpigotCommand} instead, as this is kinda generic naming-wise
 */
@Deprecated
public abstract class Command extends SpigotCommand {

    /**
     * Creates an instance of {@link CommandInformation.Builder CommandInformationBuilder}, allowing you to customize the stored information on the command.
     * @param name the name of the command, without the slash
     * @return a new {@link CommandInformation.Builder} instance
     * @since 12
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    protected static CommandInformation.Builder newCommand(String name) {
        return CommandInformation.builder(name);
    }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    public Command(CommandInformation... information) {
        super(information);
    }

    /**
     * Creates a new {@link Command} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link Command#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    public Command(CommandInformation.Builder... information) {
        super(information);
    }

    /**
     * @return the main command attributed to this command
     * @since 41
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    @Override
    public CommandInformation getMainCommand() {
        return super.getMainCommand();
    }

    /**
     * @return all the additional commands
     * @since 41
     * @see Command#getCommand(int) get command by index
     * @see Command#getCommand(String) get command by name
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    @Override
    public List<CommandInformation> getCommands() {
        return super.getCommands();
    }

    /**
     * @param index the index of a command to get
     * @return the {@link CommandInformation} for that command index
     * @since 41
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    @Override
    public CommandInformation getCommand(int index) {
        return super.getCommand(index);
    }

    /**
     * @param name the name of a command to match to
     * @return the {@link CommandInformation} with that name
     * @since 41
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    @Override
    public @Nullable CommandInformation getCommand(String name) {
        return super.getCommand(name);
    }

    /**
     * Cancels the cooldown for a specific {@link CommandSender user}, but only for a specific {@link CommandInformation command}
     * @param information the command that the cooldown needs to be cancelled for
     * @param sender the user the cooldown should affect
     * @since 79
     * @deprecated please use {@link SpigotCommand} instead of extending this class, as this is kinda generic naming-wise
     */
    @Deprecated
    @Override
    public void cancelCooldown(CommandSender sender, CommandInformation information) {
        super.cancelCooldown(sender, information);
    }
}
