package net.cybercake.cyberapi.bungee.server.serverlist;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.bungee.chat.UChat;
import net.cybercake.cyberapi.bungee.server.serverlist.motd.MOTD;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ServerListInfoListener implements Listener {

    private static final HashMap<File, Favicon> faviconsFromFile = new HashMap<>();
    private static final HashMap<URL, Favicon> faviconsFromURL = new HashMap<>();

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing ping = handlePing(event.getConnection(), event.getResponse());
        if(ping == null) return;
        event.setResponse(ping);
    }

    @SuppressWarnings({"deprecation"})
    private ServerPing handlePing(PendingConnection address, ServerPing ping) {
        try {
            ServerListInfo info = CyberAPI.getInstance().getServerListInfo();
            ServerListPingEvent serverListPingEvent =
                    new ServerListPingEvent(
                            address,
                            info.getProtocolManager().getVersionName(),
                            info.getProtocolManager().getProtocolNumber(),
                            info.getPlayerListManager().shouldShowPlayers(),
                            info.getProtocolManager().getVersionVisibility(),
                            info.getMOTDManager().getRandomMOTD(),
                            info.getPlayerListManager().getMaxPlayers(),
                            info.getPlayerListManager().getOnlinePlayers(),
                            info.getPlayerListManager().getCustomOnlinePlayers()
                    );
            if(serverListPingEvent.isCancelled()) return null;
            ProxyServer.getInstance().getPluginManager().callEvent(serverListPingEvent);

            // MOTD
            MOTD motd = serverListPingEvent.getMOTD();
            if(motd == null) motd = MOTD.builder("_default").build();
            ping.setDescription(motd.getFormattedMOTD());
            try {
                Favicon image = null;
                switch(motd.getMOTDIconType()) {
                    case FILE -> {
                        // image = Favicon.create(Base64.getEncoder().encodeToString(Files.readAllBytes(motd.getFileIcon().toPath())));
                        if(motd.getFileIcon() != null && faviconsFromFile.get(motd.getFileIcon()) == null) {
                            CyberAPI.getInstance().getAPILogger().verbose("New icon found (type=FILE)! Caching into temporary storage...");
                            CyberAPI.getInstance().getAPILogger().verbose("This may take a second, and Bungee will likely provide a 'Listener took <x>ms...', just ignore it!");
                            image = Favicon.create(ImageIO.read(motd.getFileIcon()));
                            faviconsFromFile.put(motd.getFileIcon(), image);
                        } else if(faviconsFromFile.get(motd.getFileIcon()) != null) {
                            image = faviconsFromFile.get(motd.getFileIcon());
                        }
                    }
                    case URL -> {
                        if(motd.getURLIcon() != null && faviconsFromURL.get(motd.getURLIcon()) == null) {
                            CyberAPI.getInstance().getAPILogger().verbose("New icon found (type=URL)! Caching into temporary storage...");
                            CyberAPI.getInstance().getAPILogger().verbose("This may take a second, and Bungee will likely provide a 'Listener took <x>ms...', just ignore it!");
                            HttpURLConnection connection = (HttpURLConnection) motd.getURLIcon().openConnection();
                            connection.connect();
                            BufferedImage bufferedImage = ImageIO.read(connection.getInputStream());
                            connection.disconnect();
                            image = Favicon.create(bufferedImage);
                            faviconsFromURL.put(motd.getURLIcon(), image);
                        } else if(faviconsFromURL.get(motd.getURLIcon()) != null) {
                            image = faviconsFromURL.get(motd.getURLIcon());
                        }
                    }
                }
                if(image != null) ping.setFavicon(image);
            } catch (Exception exception) {
                CyberAPI.getInstance().getAPILogger().error("An exception occurred whilst creating the Favicon for the server: " + ChatColor.DARK_GRAY + exception);
                CyberAPI.getInstance().getAPILogger().verboseException(exception);
            }

            // player count
            List<ServerPing.PlayerInfo> profiles = new ArrayList<>();
            for(String name : serverListPingEvent.getOnlinePlayers()) {
                profiles.add(new ServerPing.PlayerInfo(name, UUID.randomUUID()));
            }

            if(serverListPingEvent.isPlayerListVisible()) ping.setPlayers(new ServerPing.Players(serverListPingEvent.getMaxPlayers(), serverListPingEvent.getOnlinePlayerCount(), (serverListPingEvent.isPlayerListVisible() ? profiles.toArray(new ServerPing.PlayerInfo[]{}) : null)));
            else ping.setPlayers(null);

            int protocol = switch(serverListPingEvent.getVersionVisibility()) {
                case VISIBLE -> 0;
                case HIDDEN -> address.getVersion();
                case IF_OUTDATED -> (
                        serverListPingEvent.getProtocolVersion() == Integer.MIN_VALUE
                        ? ProxyServer.getInstance().getProtocolVersion()
                                : serverListPingEvent.getProtocolVersion()
                        );
            };
            ping.setVersion(new ServerPing.Protocol(
                    UChat.chat(serverListPingEvent.getVersionName()),
                    protocol
            ));

            return ping;
        } catch (Exception exception){
            CyberAPI.getInstance().getAPILogger().error("An exception occurred whilst sending server ping packet: " + ChatColor.DARK_GRAY + exception);
            CyberAPI.getInstance().getAPILogger().verboseException(exception);
        }
        return null;
    }
}
