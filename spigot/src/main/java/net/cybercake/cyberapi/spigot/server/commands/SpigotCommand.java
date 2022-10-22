package net.cybercake.cyberapi.spigot.server.commands;

import net.cybercake.cyberapi.common.basic.Time;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.chat.UTabComp;
import net.cybercake.cyberapi.spigot.server.commands.cooldown.ActiveCooldown;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents a command in CyberAPI
 */
@SuppressWarnings({"unused"})
public abstract class SpigotCommand implements CommandExecutor, TabCompleter {

    /**
     * Creates an instance of {@link CommandInformation.Builder CommandInformationBuilder}, allowing you to customize the stored information on the command.
     * @param name the name of the command, without the slash
     * @return a new {@link CommandInformation.Builder} instance
     * @since 12
     */
    protected static CommandInformation.Builder newCommand(String name) {
        return CommandInformation.builder(name);
    }

    private final List<CommandInformation> information;
    private SpigotCommand() { this.information = List.of(newCommand("").build()); }

    /**
     * Creates a new {@link SpigotCommand} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link SpigotCommand#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     */
    public SpigotCommand(CommandInformation... information) {
        if(information.length < 1) throw new IllegalArgumentException("There must be at least **one** " + information.getClass().getCanonicalName() + " provided for constructor in " + SpigotCommand.class.getCanonicalName() + "!");
        this.information = List.of(information);
    }

    /**
     * Creates a new {@link SpigotCommand} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link SpigotCommand#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     */
    public SpigotCommand(CommandInformation.Builder... information) {
        if(information.length < 1) throw new IllegalArgumentException("There must be at least **one** " + information.getClass().getCanonicalName() + " provided for constructor in " + SpigotCommand.class.getCanonicalName() + "!");
        List<CommandInformation> builtInformations = new ArrayList<>();
        List.of(information).forEach(info -> builtInformations.add(info.build()));
        this.information = builtInformations;
    }

    /**
     * @return the main command attributed to this command
     * @since 41
     */
    public CommandInformation getMainCommand() { return information.get(0); }

    /**
     * @return all the additional commands
     * @since 41
     * @see SpigotCommand#getCommand(int) get command by index
     * @see SpigotCommand#getCommand(String) get command by name
     */
    public List<CommandInformation> getCommands() { return information; }

    /**
     * @param index the index of a command to get
     * @return the {@link CommandInformation} for that command index
     * @since 41
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    public CommandInformation getCommand(int index) { return getCommands().get(index); }

    /**
     * @param name the name of a command to match to
     * @return the {@link CommandInformation} with that name
     * @since 41
     */
    public @Nullable CommandInformation getCommand(String name) {
        for(CommandInformation info : getCommands()) {
            if(
                    info.getName().equalsIgnoreCase(name)
                    || Arrays.stream(info.getAliases()).anyMatch(info1 -> info1.equalsIgnoreCase(name))
            )
                return info;
        }
        return null;
    }

    /**
     * Cancels the cooldown for a specific {@link CommandSender user}, but only for a specific {@link CommandInformation command}
     * @param information the command that the cooldown needs to be cancelled for
     * @param sender the user the cooldown should affect
     * @since 79
     */
    public void cancelCooldown(CommandSender sender, CommandInformation information) {
        ActiveCooldown.cancelCooldownFor(sender, information);
    }

    /**
     * The Bukkit command's execution
     * @param sender the sender that executes the command
     * @param command the command name {@code sender} ran
     * @param args the command arguments {@code sender} inputted
     * @return whether the command was successful or not
     * @since 12
     */
    public abstract boolean perform(@NotNull CommandSender sender, @NotNull String command, CommandInformation information, String[] args);

    /**
     * The Bukkit command's tab completions
     * @param sender the sender that is tab completing a command
     * @param command the command name {@code sender} is attempting to run
     * @param args the command arguments {@code sender} has inputted so far
     * @return what to tab complete
     * @since 12
     */
    public abstract List<String> tab(@NotNull CommandSender sender, @NotNull String command, CommandInformation information, String[] args);

    @Override
    public final boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        CommandInformation info = getCommand(s);
        if(info != null && info.getCooldown() != null) {
            ActiveCooldown cooldown = ActiveCooldown.getCooldownFor(commandSender, info);
            if(cooldown != null && cooldown.getExpiration() > System.currentTimeMillis()  && (info.getCooldown().getBypassPermission() == null || (info.getCooldown().getBypassPermission() != null && !commandSender.hasPermission(info.getCooldown().getBypassPermission())))) { // if the user currently has a cooldown active
                long timeLeft = TimeUnit.SECONDS.convert(cooldown.getExpiration(), TimeUnit.MILLISECONDS)-TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

                String timeDuration = Time.getBetterTimeDisplay(timeLeft, true).replace(" and ", ", ");
                String timeDurationSimplified = Time.getBetterTimeDisplay(timeLeft, false);
                String timeDurationMilliseconds = Time.formatBasicMs(cooldown.getExpiration()-System.currentTimeMillis(), false);

                if(info.getCooldown().getMessage() != null) {
                    commandSender.sendMessage(info.getCooldown().getMessage()
                            .replace("%remaining_time%", timeDuration)
                            .replace("%remaining_time_simplified%", timeDurationSimplified)
                            .replace("%remaining_time_ms%", timeDurationMilliseconds)
                    );
                }else if(info.getCooldown().getMessage() == null) {
                    commandSender.sendMessage(UChat.chat("&cYou cannot use this command for another &6" + timeDuration + "&c!"));
                }

                return true;
            }

            // sets a new cooldown since the execution of the command is about to occur
            if((info.getCooldown().getBypassPermission() != null && !commandSender.hasPermission(info.getCooldown().getBypassPermission())) || info.getCooldown().getBypassPermission() == null) {
                cancelCooldown(commandSender, info);
                ActiveCooldown.setNewCooldown(info, commandSender, TimeUnit.MILLISECONDS.convert(Time.getUnix(info.getCooldown().getUnit())+info.getCooldown().getTime(), info.getCooldown().getUnit()));
            }
        }
        return perform(commandSender, s, info, strings);
    }

    @Nullable
    @Override
    public final List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tab = tab(commandSender, s, getCommand(s), strings);
        if(tab == null) return UTabComp.emptyList;
        return UTabComp.tabCompletions(getMainCommand().getTabCompleteType(), List.of(strings).get(strings.length-1), tab);
    }
}
