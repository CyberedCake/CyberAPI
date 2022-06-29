package net.cybercake.cyberapi.server.serverlist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import net.cybercake.cyberapi.CyberAPI;
import net.cybercake.cyberapi.server.serverlist.motd.MOTD;
import net.cybercake.cyberapi.server.serverlist.motd.MOTDBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

public class ServerListInfoListener {

    public void init() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(CyberAPI.getInstance(), ListenerPriority.NORMAL,
                        Collections.singletonList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        handlePing(event.getPacket().getServerPings().read(0));
                    }
                }
        );
    }

    private void handlePing(WrappedServerPing ping) {
        try {
            ServerListInfo info = CyberAPI.getInstance().getServerListInfo();
            ServerListPingEvent serverListPingEvent =
                    new ServerListPingEvent(
                            info.getProtocolManager().getVersionName(),
                            info.getProtocolManager().getProtocolNumber(),
                            info.getPlayerListManager().shouldShowPlayers(),
                            info.getProtocolManager().shouldAlwaysShowVersion(),
                            info.getMOTDManager().getRandomMOTD(),
                            info.getPlayerListManager().getMaxPlayers(),
                            info.getPlayerListManager().getOnlinePlayers(),
                            info.getPlayerListManager().getCustomOnlinePlayers()
                    );
            if(serverListPingEvent.isCancelled()) return;
            Bukkit.getPluginManager().callEvent(serverListPingEvent);

            // MOTD
            MOTD motd = serverListPingEvent.getMOTD();
            if(motd == null) motd = new MOTDBuilder("default_temp").build();
            ping.setMotD(motd.getFormattedMOTD());
            if(motd.getIconFile() != null) ping.setFavicon(WrappedServerPing.CompressedImage.fromPng(motd.getIconFile().toURI().toURL().openStream()));

            // player count
            ping.setPlayersMaximum(serverListPingEvent.getMaxPlayers());
            ping.setPlayersOnline(serverListPingEvent.getOnlinePlayerCount());

            // hover over player count (players online)
            List<WrappedGameProfile> profiles = new ArrayList<>();
            for(String name : serverListPingEvent.getOnlinePlayers()) {
                profiles.add(new WrappedGameProfile(UUID.randomUUID(), name));
            }
            ping.setPlayers(profiles);

            ping.setVersionName(serverListPingEvent.getVersionName());
            ping.setVersionProtocol((serverListPingEvent.isVersionNameAlwaysVisible() ? 0 : serverListPingEvent.getProtocolVersion()));

            ping.setPlayersVisible(serverListPingEvent.isPlayerListVisible()); // overrides the 'player count' and 'hover over player count' sections
        } catch (Exception exception){
            CyberAPI.getInstance().getAPILogger().error("An exception occurred whilst sending server ping packet: " + ChatColor.DARK_GRAY + exception);
            CyberAPI.getInstance().getAPILogger().verboseException("SERVERLISTINFO", exception);
        }
    }

}
