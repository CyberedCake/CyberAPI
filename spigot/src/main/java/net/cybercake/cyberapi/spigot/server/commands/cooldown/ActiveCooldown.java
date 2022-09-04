package net.cybercake.cyberapi.spigot.server.commands.cooldown;

import com.google.common.base.Preconditions;
import net.cybercake.cyberapi.spigot.server.commands.CommandInformation;
import org.bukkit.command.CommandSender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActiveCooldown implements Serializable {

    private final static List<ActiveCooldown> activeCooldowns = new ArrayList<>();

    /**
     * @return the current list of active cooldowns
     * @since 79
     */
    public static List<ActiveCooldown> getActiveCooldowns() { return activeCooldowns; }

    /**
     * Retrieves a cooldown using {@link ActiveCooldown#getActiveCooldowns()} but for a specific {@link CommandSender}
     * @param sender the {@link CommandSender} who has an {@link ActiveCooldown}
     * @return the {@link List} of {@link ActiveCooldown} for that user
     * @since 79
     */
    public static List<ActiveCooldown> getCooldownFor(CommandSender sender) {
        return getActiveCooldowns().stream().filter(cooldown -> cooldown.getSender().equals(sender)).toList(); }

    /**
     * Retrieves a cooldown using {@link ActiveCooldown#getActiveCooldowns()} but for a specific {@link CommandInformation} (command)
     * @param commandInformation the {@link CommandInformation} who has an {@link ActiveCooldown}
     * @return the {@link List} of {@link ActiveCooldown} for that command
     * @since 79
     */
    public static List<ActiveCooldown> getCooldownFor(CommandInformation commandInformation) {
        return getActiveCooldowns().stream().filter(cooldown -> cooldown.getInformation().equals(commandInformation)).toList(); }

    /**
     * Retrieves a cooldown using {@link ActiveCooldown#getActiveCooldowns()} but for a specific {@link CommandSender} and {@link CommandInformation} (command)
     * @param sender the {@link CommandSender} who has an {@link ActiveCooldown}
     * @param commandInformation the {@link CommandInformation} who has an {@link ActiveCooldown}
     * @return the {@link ActiveCooldown} of that users current cooldown with the command
     * @since 79
     */
    public static ActiveCooldown getCooldownFor(CommandSender sender, CommandInformation commandInformation) {
        return getActiveCooldowns()
                .stream()
                .filter(cooldown -> cooldown.getInformation().equals(commandInformation) && cooldown.getSender().equals(sender))
                .findFirst()
                .orElse(null); }

    /**
     * Cancels the cooldown for a specific {@link CommandSender sender}
     * @param sender the {@link CommandSender sender} to cancel all cooldowns for
     * @since 79
     */
    public static void cancelCooldownFor(CommandSender sender) {
        getActiveCooldowns().stream().filter(cooldown -> cooldown.getSender().equals(sender)).toList().forEach(activeCooldowns::remove);
    }

    /**
     * Cancels the cooldown for a specific {@link CommandInformation command}
     * @param commandInformation the {@link CommandInformation command} to cancel all cooldowns for
     * @since 79
     */
    public static void cancelCooldownFor(CommandInformation commandInformation) {
        getActiveCooldowns().stream().filter(cooldown -> cooldown.getInformation().equals(commandInformation)).toList().forEach(activeCooldowns::remove);
    }

    /**
     * Cancels the cooldown for a specific {@link CommandSender sender} and {@link CommandInformation command}
     * @param sender the {@link CommandSender sender} to cancel all cooldowns for
     * @param commandInformation the {@link CommandInformation command} to cancel all cooldowns for
     * @since 79
     */
    public static void cancelCooldownFor(CommandSender sender, CommandInformation commandInformation) {
        getActiveCooldowns().stream().filter(cooldown -> cooldown.getInformation().equals(commandInformation) && cooldown.getSender().equals(sender)).toList().forEach(activeCooldowns::remove);
    }

    /**
     * Creates a new cooldown for a {@link CommandInformation command}, {@link CommandSender sender}, and with an expiration date (in {@link TimeUnit#MILLISECONDS})
     * @return the new {@link ActiveCooldown} instance
     * @since 79
     */
    public static ActiveCooldown setNewCooldown(CommandInformation information, CommandSender sender, long expiration) {
        ActiveCooldown cooldown = new ActiveCooldown(information, sender, expiration);
        activeCooldowns.add(cooldown);
        return cooldown;
    }

    /**
     * Creates a new cooldown for a {@link CommandInformation command}, {@link CommandSender sender}, and with an expiration {@link Date date}
     * @return the new {@link ActiveCooldown} instance
     * @since 79
     */
    public static ActiveCooldown setNewCooldown(CommandInformation information, CommandSender sender, Date expiration) {
        return setNewCooldown(information, sender, expiration.getTime());
    }

    private final CommandInformation information;
    private final CommandSender sender;
    private final long started;
    private final long expiration;

    private ActiveCooldown() { this.information = null; this.sender = null; this.started = -1L; this.expiration = -1L; }
    private ActiveCooldown(CommandInformation information, CommandSender sender, long expiration) {
        Preconditions.checkNotNull(information, "information");
        Preconditions.checkNotNull(sender, "sender");
        Preconditions.checkArgument(expiration > System.currentTimeMillis(), "expires (" + Long.class + ") must be greater than the current system unix time (" + TimeUnit.MILLISECONDS.name() + ", " + System.currentTimeMillis() + ")");

        this.information = information;
        this.sender = sender;
        this.started = System.currentTimeMillis();
        this.expiration = expiration;
    }

    /**
     * @return the {@link CommandInformation command} associated with this cooldown
     * @since 79
     */
    public CommandInformation getInformation() { return this.information; }

    /**
     * @return the {@link CommandSender sender} associated with this cooldown
     * @since 79
     */
    public CommandSender getSender() { return this.sender; }

    /**
     * @return the unix timestamp (represented in milliseconds) of when the cooldown started
     * @since 79
     */
    public long getStarted() { return this.started; }

    /**
     * @return the unix timestamp (represented in milliseconds) of when the cooldown is set to expire
     * @since 79
     */
    public long getExpiration() { return this.expiration; }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "information=" + information +
                ", sender=" + sender +
                ", started=" + started +
                ", expiration=" + expiration +
                '}';
    }
}
