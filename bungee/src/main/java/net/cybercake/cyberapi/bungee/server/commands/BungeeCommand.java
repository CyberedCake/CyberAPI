package net.cybercake.cyberapi.bungee.server.commands;

import net.cybercake.cyberapi.bungee.chat.UChat;
import net.cybercake.cyberapi.bungee.chat.UTabComp;
import net.cybercake.cyberapi.bungee.server.commands.cooldown.ActiveCooldown;
import net.cybercake.cyberapi.common.basic.Time;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BungeeCommand extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

    /**
     * Creates an instance of {@link CommandInformation.Builder}, allowing you to customize the stored information on the command.
     * @param name the name of the command, without the slash
     * @return a new {@link CommandInformation.Builder} instance
     * @since 15
     */
    protected static CommandInformation.Builder newCommand(String name) { return CommandInformation.builder(name); }

    private final CommandInformation information;

    private BungeeCommand() {
        super("", "", "");
        this.information = newCommand("").build();
    }

    /**
     * Creates a new {@link BungeeCommand} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link BungeeCommand#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     */
    public BungeeCommand(CommandInformation information) {
        super(information.getName(), information.getPermission(), information.getAliases());
        this.information = information;
        if(!information.getPermissionMessage().isEmpty()) setPermissionMessage(information.getPermissionMessage());
    }

    /**
     * Creates a new {@link BungeeCommand} for the server. This command automatically registers itself and doesn't require a plugin.yml entry.
     * @param information the {@link CommandInformation} that allows the customization of the stored information of the command, use {@link BungeeCommand#newCommand(String)} to create a {@link CommandInformation} instance
     * @since 41
     */
    public BungeeCommand(CommandInformation.Builder information) {
        this(information.build());
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
    public final void execute(CommandSender sender, String[] args) {
        if(information != null && information.getCooldown() != null) {
            ActiveCooldown cooldown = ActiveCooldown.getCooldownFor(sender, information);
            if(cooldown != null && cooldown.getExpiration() > System.currentTimeMillis()  && (information.getCooldown().getBypassPermission() == null || (information.getCooldown().getBypassPermission() != null && !sender.hasPermission(information.getCooldown().getBypassPermission())))) { // if the user currently has a cooldown active
                long timeLeft = TimeUnit.SECONDS.convert(cooldown.getExpiration(), TimeUnit.MILLISECONDS)-TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

                String timeDuration = Time.getBetterTimeDisplay(timeLeft, true).replace(" and ", ", ");
                String timeDurationSimplified = Time.getBetterTimeDisplay(timeLeft, false);
                String timeDurationMilliseconds = Time.formatBasicMs(cooldown.getExpiration()-System.currentTimeMillis(), false);

                if(information.getCooldown().getMessage() != null) {
                    sender.sendMessage(information.getCooldown().getMessage()
                            .replace("%remaining_time%", timeDuration)
                            .replace("%remaining_time_simplified%", timeDurationSimplified)
                            .replace("%remaining_time_ms%", timeDurationMilliseconds)
                    );
                }else if(information.getCooldown().getMessage() == null) {
                    sender.sendMessage(UChat.bComponent("&cYou cannot use this command for another &6" + timeDuration + "&c!"));
                }

                return;
            }

            // sets a new cooldown since the execution of the command is about to occur
            if((information.getCooldown().getBypassPermission() != null && !sender.hasPermission(information.getCooldown().getBypassPermission())) || information.getCooldown().getBypassPermission() == null) {
                cancelCooldown(sender, information);
                ActiveCooldown.setNewCooldown(information, sender, TimeUnit.MILLISECONDS.convert(Time.getUnix(information.getCooldown().getUnit())+information.getCooldown().getTime(), information.getCooldown().getUnit()));
            }
        }
        boolean perform = perform(sender, information, args);
        if(!perform) {
            sender.sendMessage(UChat.bComponent(information.getUsage()));
        }
    }

    @Override
    public final Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tab = tab(sender, information, args);
        if(tab == null) return UTabComp.emptyList;
        return UTabComp.tabCompletions(information.getTabCompleteType(), List.of(args).get(args.length-1), tab);
    }
}
