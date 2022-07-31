package net.cybercake.cyberapi.spigot.server.serverlist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftProtocolVersion;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.server.serverlist.motd.MOTD;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.InetAddress;
import java.nio.file.Files;
import java.util.*;

public class ServerListInfoListener {

    public static class JoinListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            ServerListInfoListener.users.put(event.getPlayer().getAddress().getAddress(), event.getPlayer());
        }

    }

    public static HashMap<InetAddress, OfflinePlayer> users = new HashMap<>();

    public void init() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(CyberAPI.getInstance(), ListenerPriority.NORMAL,
                        Collections.singletonList(PacketType.Status.Server.SERVER_INFO), ListenerOptions.ASYNC) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        handlePing(event.getPlayer(), event.getPacket().getServerPings().read(0));
                    }
                }
        );
    }

    private void handlePing(Player player, WrappedServerPing ping) {
        try {
            InetAddress address = player.getAddress().getAddress();
            ServerListInfo info = CyberAPI.getInstance().getServerListInfo();
            ServerListPingEvent serverListPingEvent =
                    new ServerListPingEvent(
                            address,
                            users.get(address),
                            info.getProtocolManager().getVersionName(),
                            info.getProtocolManager().getProtocolNumber(),
                            info.getPlayerListManager().shouldShowPlayers(),
                            info.getProtocolManager().getVersionVisibility(),
                            info.getMOTDManager().getRandomMOTD(),
                            info.getPlayerListManager().getMaxPlayers(),
                            info.getPlayerListManager().getOnlinePlayers(),
                            info.getPlayerListManager().getCustomOnlinePlayers()
                    );
            if(serverListPingEvent.isCancelled()) return;
            Bukkit.getPluginManager().callEvent(serverListPingEvent);

            // MOTD
            MOTD motd = serverListPingEvent.getMOTD();
            if(motd == null) motd = MOTD.builder("_default").build();
            ping.setMotD(motd.getFormattedMOTD());
            WrappedServerPing.CompressedImage image = null;
            switch(motd.getMOTDIconType()) {
                case FILE -> {
                    if(motd.getFileIcon() != null)
                        image = WrappedServerPing.CompressedImage.fromBase64Png(Base64.getEncoder().encodeToString(Files.readAllBytes(motd.getFileIcon().toPath())));
                }
                case URL -> {
                    if(motd.getURLIcon() != null)
                        image = WrappedServerPing.CompressedImage.fromPng(motd.getURLIcon().openStream());
                }
            }
            if(image != null) ping.setFavicon(image);

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

            int protocol = switch(serverListPingEvent.getVersionVisibility()) {
                case VISIBLE -> 0;
                case HIDDEN -> ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
                case IF_OUTDATED -> (
                        serverListPingEvent.getProtocolVersion() == Integer.MIN_VALUE
                        ? MinecraftProtocolVersion.getVersion(ProtocolLibrary.getProtocolManager().getMinecraftVersion())
                                : serverListPingEvent.getProtocolVersion()
                        );
            };
            ping.setVersionProtocol(protocol);

            ping.setPlayersVisible(serverListPingEvent.isPlayerListVisible()); // overrides the 'player count' and 'hover over player count' sections
        } catch (Exception exception){
            CyberAPI.getInstance().getAPILogger().error("An exception occurred whilst sending server ping packet: " + ChatColor.DARK_GRAY + exception);
            CyberAPI.getInstance().getAPILogger().verboseException(exception);
        }
    }

}
