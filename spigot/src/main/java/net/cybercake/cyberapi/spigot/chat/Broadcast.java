package net.cybercake.cyberapi.spigot.chat;

import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.Validators;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Broadcast {

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send
     * @since 1 (creation), 139 (moved to {@link Broadcast})
     * @see Broadcast#chat(String, String)
     * @see Broadcast#chat(String, Predicate)
     */
    public static void chat(String message) { chat(message, (Predicate<? super CommandSender>) null); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param permission the permission required to see the message
     * @since 1 (creation), 139 (moved to {@link Broadcast})
     * @see Broadcast#chat(String)
     * @see Broadcast#chat(String, Predicate)
     */
    public static void chat(String message, @Nullable String permission) {
        chat(message, player ->
                permission == null
                        || permission.strip().equalsIgnoreCase("")
                        || player.hasPermission(permission)
        );
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send
     * @param filter the filter of command senders that can see this message, note: only {@link Player players} and {@link org.bukkit.command.ConsoleCommandSender console} are checked!
     * @since 126 (creation), 139 (moved to {@link Broadcast})
     * @see Broadcast#chat(String)
     * @see Broadcast#chat(String, String)
     */
    public static void chat(String message, @Nullable Predicate<? super CommandSender> filter) {
        for(Player player : CyberAPI.getInstance().getOnlinePlayers()) {
            if(filter != null && !filter.test(player)) continue;
            player.sendMessage(UChat.chat(message));
        }
        if(filter == null || filter.test(CyberAPI.getInstance().getServer().getConsoleSender()))
            Log.info(message);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send, as a {@link Component}
     * @since 126 (creation), 139 (moved to {@link Broadcast})
     * @apiNote requires Adventure API support
     * @see Broadcast#component(Component, String)
     * @see Broadcast#component(Component, Predicate)
     */
    public static void component(Component message) { component(message, (Predicate<? super CommandSender>) null); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link Component}
     * @param permission the permission required to see the message
     * @since 126 (creation), 139 (moved to {@link Broadcast})
     * @apiNote requires Adventure API Support
     * @see Broadcast#component(Component)
     * @see Broadcast#component(Component, Predicate)
     */
    public static void component(Component message, @Nullable String permission) {
        Validators.validateAdventureSupport();
        chat(LegacyComponentSerializer.legacyAmpersand().serialize(message), permission);
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link Component}
     * @param filter the filter of command senders that can see this message, note: only {@link Player players} and {@link org.bukkit.command.ConsoleCommandSender console} are checked!
     * @since 126 (creation), 139 (moved to {@link Broadcast})
     * @apiNote requires Adventure API support
     * @see Broadcast#component(Component)
     * @see Broadcast#component(Component, String)
     */
    public static void component(Component message, @Nullable Predicate<? super CommandSender> filter) {
        Validators.validateAdventureSupport();
        chat(LegacyComponentSerializer.legacyAmpersand().serialize(message), filter);
    }

    /**
     * Broadcast a message to all online players and logs to console
     * @param message the message to send, as a {@link BaseComponent}
     * @since 126 (creation), 139 (moved to {@link Broadcast})
     * @see Broadcast#bComponent(BaseComponent, String)
     * @see Broadcast#bComponent(BaseComponent, Predicate)
     */
    public static void bComponent(BaseComponent message) { bComponent(message, (Predicate<? super CommandSender>) null); }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link BaseComponent}
     * @param permission the permission required to see the message
     * @since 126 (creation), 139 (moved to {@link Broadcast})
     * @see Broadcast#bComponent(BaseComponent)
     * @see Broadcast#bComponent(BaseComponent, Predicate)
     */
    public static void bComponent(BaseComponent message, @Nullable String permission) {
        chat(BaseComponent.toLegacyText(message), permission);
    }

    /**
     * Broadcast a message to all online players if they have a specified permission and logs to console
     * @param message the message to send, as a {@link BaseComponent}
     * @param filter the filter of command senders that can see this message, note: only {@link Player players} and {@link org.bukkit.command.ConsoleCommandSender console} are checked!
     * @since 126 (creation), 139 (moved to {@link Broadcast})
     * @see Broadcast#bComponent(BaseComponent)
     * @see Broadcast#bComponent(BaseComponent, String)
     */
    public static void bComponent(BaseComponent message, @Nullable Predicate<? super CommandSender> filter) {
        chat(BaseComponent.toLegacyText(message), filter);
    }

}
