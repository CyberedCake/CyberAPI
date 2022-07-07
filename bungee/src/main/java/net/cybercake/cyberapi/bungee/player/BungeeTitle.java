package net.cybercake.cyberapi.bungee.player;

import net.cybercake.cyberapi.bungee.chat.UChat;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;

/**
 * Represents the bungee title packet manager, thanks <a href="https://www.javatips.net/api/BungeeCord-master/proxy/src/main/java/net/md_5/bungee/BungeeTitle.java">to this website for most of this code</a>
 * @since 15
 */
public class BungeeTitle implements Title {

    private net.md_5.bungee.protocol.packet.Title title, subtitle, times, clear, reset;

    /**
     * Creates a basic title packet in {@link BungeeTitle}
     * <br> <br>
     * Usually you would not want to use this, and instead leave this up to {@link BungeeTitle this class}
     * @param action the {@link net.md_5.bungee.protocol.packet.Title.Action} that packet is performing
     * @return the {@link net.md_5.bungee.protocol.packet.Title} packet
     * @since 15
     */
    public static net.md_5.bungee.protocol.packet.Title createPacket(net.md_5.bungee.protocol.packet.Title.Action action) {
        net.md_5.bungee.protocol.packet.Title title = new net.md_5.bungee.protocol.packet.Title();
        title.setAction(action);
        if(action == net.md_5.bungee.protocol.packet.Title.Action.TIMES) {
            title.setFadeIn(20);
            title.setStay(100);
            title.setFadeOut(20);
        }
        return title;
    }

    /**
     * Sets the title for the {@link BungeeTitle}
     * @param text The text to use as the title.
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title title(BaseComponent text) {
        if(title == null)
            title = createPacket(net.md_5.bungee.protocol.packet.Title.Action.TITLE);
        title.setText(UChat.toLegacy(text));
        return this;
    }

    /**
     * Sets the title for the {@link BungeeTitle}
     * @param text The text to use as the title.
     * @return the {@link BungeeTitle} object
     * @since 15
     * @deprecated this <b>will</b> thorw a {@link UnsupportedOperationException}
     * @throws UnsupportedOperationException this method is <b>not</b> supported by CyberAPI
     */
    @Override
    @Deprecated(forRemoval = true)
    public Title title(BaseComponent... text) {
        throw new UnsupportedOperationException("CyberAPI: The method title(BaseComponent...), with multiple " + BaseComponent.class.getCanonicalName() + "s, is not supported!");
    }

    /**
     * Sets the subtitle for the {@link BungeeTitle}
     * @param text The text to use as the subtitle.
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title subTitle(BaseComponent text) {
        if(subtitle == null)
            subtitle = createPacket(net.md_5.bungee.protocol.packet.Title.Action.SUBTITLE);
        subtitle.setText(UChat.toLegacy(text));
        return this;
    }

    /**
     * Sets the subtitle for the {@link BungeeTitle}
     * @param text The text to use as the subtitle.
     * @return the {@link BungeeTitle} object
     * @since 15
     * @deprecated this <b>will</b> thorw a {@link UnsupportedOperationException}
     * @throws UnsupportedOperationException this method is <b>not</b> supported by CyberAPI
     */
    @Override
    @Deprecated(forRemoval = true)
    public Title subTitle(BaseComponent... text) {
        throw new UnsupportedOperationException("CyberAPI: The method subTitle(BaseComponent...), with multiple " + BaseComponent.class.getCanonicalName() + "s, is not supported!");
    }

    /**
     * Sets the fade-in time for the {@link BungeeTitle}
     * @param ticks The amount of ticks (1/20 second) for the fade in effect.
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title fadeIn(int ticks) {
        if(times == null)
            times = createPacket(net.md_5.bungee.protocol.packet.Title.Action.TIMES);
        times.setFadeIn(ticks);
        return this;
    }

    /**
     * Sets the stay time for the {@link BungeeTitle}
     * @param ticks The amount of ticks (1/20 second) for the stay effect.
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title stay(int ticks) {
        if(times == null)
            times = createPacket(net.md_5.bungee.protocol.packet.Title.Action.TIMES);
        times.setStay(ticks);
        return this;
    }

    /**
     * Sets the fade-out time for the {@link BungeeTitle}
     * @param ticks The amount of ticks (1/20 second) for the fade out effect
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title fadeOut(int ticks) {
        if(times == null)
            times = createPacket(net.md_5.bungee.protocol.packet.Title.Action.TIMES);
        times.setFadeOut(ticks);
        return this;
    }

    /**
     * Clears the player's title and subtitle
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title clear() {
        if(clear == null)
            clear = createPacket(net.md_5.bungee.protocol.packet.Title.Action.TIMES);
        title = null; // No need to send a title if we clear it after that again anyway
        subtitle = null;
        return this;
    }

    /**
     * Resets the player's title, subtitle, and times
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title reset() {
        if(reset == null)
            reset = createPacket(net.md_5.bungee.protocol.packet.Title.Action.RESET);
        // No need to send these packets if we are going to reset them later
        title = null;
        subtitle = null;
        times = null;
        return this;
    }

    /**
     * Sends packets to a {@link ProxiedPlayer}
     * @param player the player to send the packets to
     * @param packets the packets to send
     * @since 15
     */
    public void sendPacket(ProxiedPlayer player, DefinedPacket... packets) {
        for(DefinedPacket packet : packets) {
            if(packet == null) continue;
            player.unsafe().sendPacket(packet);
        }
    }

    /**
     * Sends the title to a {@link ProxiedPlayer}
     * @param player The player to send the title to.
     * @return the {@link BungeeTitle} object
     * @since 15
     */
    @Override
    public Title send(ProxiedPlayer player) {
        sendPacket(player, clear, reset, times, subtitle, title);
        return this;
    }
}
