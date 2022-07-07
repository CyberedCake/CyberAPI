package net.cybercake.cyberapi.bungee.player;

import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.bungee.Validators;
import net.cybercake.cyberapi.bungee.chat.UChat;
import net.cybercake.cyberapi.bungee.chat.centered.CenteredMessage;
import net.cybercake.cyberapi.bungee.chat.centered.TextType;
import net.cybercake.cyberapi.common.basic.NumberUtils;
import net.cybercake.cyberapi.common.builders.player.PingSettings;
import net.cybercake.cyberapi.common.builders.player.UserHeadSettings;
import net.cybercake.cyberapi.common.builders.settings.FeatureSupport;
import net.cybercake.cyberapi.common.player.CachedUsername;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.util.Tristate;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class CyberPlayer {

    private final UUID uuid;

    /**
     * Creates a CyberAPI player from a {@link ProxiedPlayer} object
     * @param player the {@link ProxiedPlayer} object
     * @since 15
     */
    public CyberPlayer(ProxiedPlayer player) {
        this.uuid = player.getUniqueId();
    }

    /**
     * Creates a CyberAPI player from a unique ID
     * @param uuid the unique ID
     * @since 15
     */
    public CyberPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Creates a CyberAPI player from a username
     * @param name the username
     * @since 15
     * @deprecated it is not recommended you use this method, please use {@link CyberPlayer#CyberPlayer(UUID)} or {@link CyberPlayer#CyberPlayer(ProxiedPlayer)} instead
     */
    @Deprecated
    public CyberPlayer(String name) {
        this.uuid = ProxyServer.getInstance().getPlayer(name).getUniqueId();
    }

    /**
     * Creates a CyberAPI player from a {@link ProxiedPlayer} object
     * @param player the {@link ProxiedPlayer} object
     * @since 15
     */
    public static CyberPlayer from(ProxiedPlayer player) { return new CyberPlayer(player); }

    /**
     * Creates a CyberAPI player from a unique ID
     * @param uuid the unique ID
     * @since 15
     */
    public static CyberPlayer from(UUID uuid) { return new CyberPlayer(uuid); }

    /**
     * Creates a CyberAPI player from a username
     * @param name the username
     * @since 15
     * @deprecated it is not recommended you use this method, please use {@link CyberPlayer#CyberPlayer(UUID)} or {@link CyberPlayer#CyberPlayer(ProxiedPlayer)} instead
     */
    @Deprecated
    public static CyberPlayer from(String name) { return new CyberPlayer(name); }


    /**
     * Gets the {@link ProxiedPlayer} object
     * @return the ProxiedPlayer object, {@code null} if player is offline
     * @since 15
     */
    @Nullable public ProxiedPlayer getPlayer() { return ProxyServer.getInstance().getPlayer(uuid); }

    /**
     * Gets the {@link UUID} object of the player
     * @return the unique ID of the proxied player object
     * @since 15
     */
    public UUID getUniqueID() { return this.uuid; }

    /**
     * Returns the cached username of the player if they are offline and returns their online username if they are online
     * @return the player's username
     * @since 15
     */
    public String getUsername() { return getUsername(false); }

    /**
     * Returns the cached username of the player if they are offline and returns their online username if they are online
     * <p>{@literal  >> }and asks you if you wish to re-cache the username (which automatically happens every 30 minutes anyway)</p>
     * @param recache whether to re-get and re-cache the username from Mojang's servers
     * @return the player's username, now recached
     * @since 15
     */
    public String getUsername(boolean recache) {
        if(recache) recacheUsername();
        if(isOnline() && getPlayer() != null) return getPlayer().getName();
        return CachedUsername.cachedUsername(uuid).getUsername();
    }

    /**
     * Re-gets and re-caches the username of the player. If you would like to recache and get the username object, use {@link CyberPlayer#getUsername(boolean)}
     * @since 15
     */
    public void recacheUsername() { CachedUsername.cachedUsername(uuid).forceRecache(); }

    /**
     * Gets whether the player is online or not
     * @return the online status of the player
     * @since 15
     */
    public boolean isOnline() { return getPlayer() != null; }

    /**
     * Gets a user's head with specified settings in {@link String} form
     * <br>
     * <b>Note: This is obtaining the user head from a URL, meaning you should cache this or use asynchronous events</b>
     * @param settings the settings to apply to the chat {@link String}
     * @return the character's head, preferably to be printed in chat
     * @throws IOException if the player or the URL cannot resolve
     * @see OnlineActions#printUserHead(UserHeadSettings)
     * @since 15
     */
    public String getUserHead(@Nullable UserHeadSettings settings) throws IOException {
        if(settings == null) settings = UserHeadSettings.builder().build();

        String trimmedUUID = uuid.toString().replace("-", "");

        URL url = new URL("https://minotar.net/avatar/" + trimmedUUID + "/" + settings.getImageScale() + ".png");
        if(settings.shouldShowHelmet()) {
            url = new URL("https://minotar.net/helm/" + trimmedUUID + "/" + settings.getImageScale() + ".png");
        }

        BufferedImage img = ImageIO.read(url);

        StringBuilder columnBuilder = new StringBuilder();
        for(int row=0; row<img.getHeight(); row++) {
            StringBuilder rowBuilder = new StringBuilder();
            for (int column=0; column<img.getWidth(); column++) {
                String hex = "#" + Integer.toHexString(img.getRGB(column, row)).substring(2);
                rowBuilder.append(net.md_5.bungee.api.ChatColor.of(hex)).append(settings.getCharacter());
            }
            try {
                columnBuilder.append(rowBuilder).append(" ").append(settings.getLines()[row]).append("\n");
            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                columnBuilder.append("\n");
            }
        }
        return columnBuilder.toString();
    }

    /**
     * Gets a user's head with specified settings in different {@code params} in {@link String} form
     * <br>
     * <b>Note: This is obtaining the user head from a URL, meaning you should cache this or use asynchronous events</b>
     * @param imageScale the scale of the image
     * @param helmet whether the outside layer of the player's skin should show
     * @param character what character should be used for the message in chat
     * @param lines the lines that would be next to the character's head
     * @return the character's head, preferably to be printed in chat
     * @throws IOException if the player or the URL cannot resolve
     * @see CyberPlayer#getUserHead(UserHeadSettings)
     * @see OnlineActions#printUserHead(UserHeadSettings)
     * @since 15
     * @deprecated not going to be removed, just discouraged to use this, please use {@link CyberPlayer#getUserHead(UserHeadSettings)} instead
     */
    @Deprecated
    public String getUserHead(int imageScale, boolean helmet, Character character, String[] lines) throws IOException {
        return getUserHead(UserHeadSettings.builder().imageScale(imageScale).showHelmet(helmet).character(character).lines(lines).build());
    }

    /**
     * Gets the {@link LuckPermsData} that the {@link ProxiedPlayer} has attributed to their {@link UUID}
     * @return returns the {@link LuckPermsData} instance, {@code null} if none could be retrieved or an unknown error occurred
     * @since 15
     * @apiNote requires LuckPerms support
     */
    @Nullable
    public LuckPermsData getLuckPermsData() {
        Validators.validateLuckPermsHook();
        if(!CyberAPI.getInstance().getLuckPermsSupport().equals(FeatureSupport.SUPPORTED)) return null;
        UserManager manager = LuckPermsProvider.get().getUserManager();
        return new LuckPermsData(manager.getUser(getUniqueID()));
    }

    /**
     * The LuckPerms data for the user, if you have LuckPerms support
     * @apiNote requires LuckPerms support
     * @since 15
     */
    public class LuckPermsData {
        private User user;

        private LuckPermsData() { }
        protected LuckPermsData(User user) {
            this.user = user;
        }

        /**
         * Gets the LuckPerms user, or as LuckPerms describes it: <br>
         * "A player which holds permission data" (view more: {@link User})
         * @return a LuckPerms {@link User}
         * @since 15
         */
        public User getUser() { return user; }

        /**
         * Gets the {@link CyberPlayer}'s username that LuckPerms is using/cached into
         * @return a username as a {@link String}, usually all lower case
         * @since 15
         * @see CyberPlayer#getUsername()
         */
        public String getLuckpermsUsername() { return user.getUsername(); }

        /**
         * Gets the {@link CachedMetaData} of the {@link CyberPlayer} that LuckPerms uses, or as LuckPerms describes it: <br>
         * "Holds cached meta lookup data for a specific set of contexts." (view more: {@link CachedMetaData})
         * @return the {@link CachedMetaData} of the player
         * @since 15
         */
        public CachedMetaData getCachedMetaData() { return user.getCachedData().getMetaData(); }

        /**
         * Gets the {@link CachedPermissionData} of the {@link CyberPlayer} that LuckPerms uses, or as LuckPerms describes it: <br>
         * "Holds cached permission lookup data for a specific set of contexts." (view more: {@link CachedPermissionData})
         * @return the {@link CachedPermissionData} of the player
         * @since 15
         */
        public CachedPermissionData getCachedPermissionData() { return user.getCachedData().getPermissionData(); }

        /**
         * Gets the prefix of the {@link CyberPlayer} that is currently set in LuckPerms using {@link CachedMetaData}
         * @return the {@link String} version of the prefix held by LuckPerms
         * @since 15
         */
        public @Nullable String getPrefix() { return getCachedMetaData().getPrefix(); }

        /**
         * Gets the suffix of the {@link CyberPlayer} that is currently set in LuckPerms using {@link CachedMetaData}
         * @return the {@link String} version of the suffix held by LuckPerms
         * @since 15
         */
        public @Nullable String getSuffix() { return getCachedMetaData().getSuffix(); }

        /**
         * Gets the display name of the {@link CyberPlayer} by combining {@link LuckPermsData#getPrefix()}, {@link CyberPlayer#getUsername()}, and {@link LuckPermsData#getSuffix()}
         * @return the {@link String} version of the display name, which is just the prefix, username, and suffix combined
         * @since 15
         */
        public String getDisplayName() { return (getPrefix() == null ? "" : getPrefix()) + getUsername() + (getSuffix() == null ? "" : getSuffix()); }

        /**
         * Checks if the {@link CyberPlayer} has a certain permission and returns a {@link Tristate}.
         * <br> <br>
         * What is a {@link Tristate}? Here's what LuckPerms says: <br>
         * "Represents three different states of a setting. (TRUE, FALSE, UNDEFINED)" (view more: {@link Tristate})
         * @param permission the permission to check
         * @return the {@link Tristate} ({@link Tristate#TRUE}/{@link Tristate#FALSE}/{@link Tristate#UNDEFINED}) of whether the {@link CyberPlayer} does or doesn't have that permission
         * @since 15
         */
        public Tristate checkPermission(String permission) { return getCachedPermissionData().checkPermission(permission); }

        /**
         * Checks if the {@link CyberPlayer} has a certain permission and returns a boolean
         * <br>
         * This converts the {@link Tristate} of the permission checker to a boolean using the method {@link Tristate#asBoolean()} <br>
         * This is directly connected to {@link LuckPermsData#checkPermission(String)}
         * @param permission the permission to check
         * @return the boolean (true/false) of whether {@link CyberPlayer} does or doesn't have that permission
         * @since 15
         */
        public boolean hasPermission(String permission) { return checkPermission(permission).asBoolean(); }

        /**
         * Gets the meta value from a key, or as LuckPerms describes it: <br>
         * "Gets a value for the given meta key." (using: {@link CachedMetaData#getMetaValue(String)})
         * @param key the key
         * @return the value
         * @since 15
         */
        public String getMetaValue(String key) { return getCachedMetaData().getMetaValue(key); }
    }

    /**
     * Gets the {@link OnlineActions} that the plugin can run only if the player is online
     * @return returns the {@link OnlineActions} instance, {@code null} if the player is offline
     * @since 15
     */
    @Nullable public OnlineActions getOnlineActions() { return (isOnline() ? new OnlineActions() : null); }

    /**
     * The online actions that can be used only if the player is online
     * @since 15
     */
    public class OnlineActions {
        private final ProxiedPlayer player;
        protected OnlineActions() { this.player = ProxyServer.getInstance().getPlayer(uuid); }

        /**
         * Gets the online {@link ProxiedPlayer} instance
         * @return the player object
         * @since 15
         */
        public ProxiedPlayer getPlayer() { return player; }

        /**
         * Sends a colored message ({@link net.md_5.bungee.api.ChatColor#translateAlternateColorCodes(char, String)}) to the player
         * @param message the message to send
         * @since 15
         */
        public void sendColored(String message) { player.sendMessage(UChat.bComponent(message)); }

        /**
         * Sends a blank character to the player <br>
         * <em>equivalent of doing</em> {@code player.sendMessage(" "); }
         * @since 15
         */
        public void sendBlank() { player.sendMessage(UChat.bComponent(" ")); }

        /**
         * Send a message to the player's action bar
         * @param message the message to send
         * @since 15
         */
        public void sendActionBar(String message) { player.sendMessage(ChatMessageType.ACTION_BAR, UChat.bComponent(message)); }

        /**
         * Sends a {@link CenteredMessage} to the player
         * @param message the message to send
         * @since 15
         */
        public void sendCenteredMessage(String message) { sendCenteredMessage(message, TextType.CHAT); }

        /**
         * Sends a {@link CenteredMessage} to the player with a specified {@link TextType}
         * @param message the message to send
         * @param textType the type of message -> in this case, usually chat
         * @since 15
         */
        public void sendCenteredMessage(String message, TextType textType) { sendCenteredMessage(message, textType.getLength()); }

        /**
         * Sends a {@link CenteredMessage} to the player with a specified chat length
         * @param message the message to send
         * @param length the length that will be used in determining the center of the message
         * @since 15
         */
        public void sendCenteredMessage(String message, int length) { player.sendMessage(UChat.bComponent(new CenteredMessage(message, length).getString())); }

        /**
         * Gets the player's ping, in milliseconds
         * @return the player's ping, using {@link ProxiedPlayer#getPing()}
         * @since 15
         */
        public int getPing() { return player.getPing(); }

        /**
         * Gets the colored ping of the player using {@link PingSettings} as settings.
         * <br> <br>
         * To create a {@link PingSettings} instance, you must create a new {@link PingSettings.Builder} instance by doing {@link PingSettings#builder()} and modify the settings, and then use {@link PingSettings.Builder#build() the builder's build method} to build the contents.
         * @param settings the {@link PingSettings} that should apply to this {@link String}
         * @return the colored ping
         * @since 15
         */
        public String getColoredPing(@Nullable PingSettings settings) {
            if(settings == null) settings = PingSettings.builder().build();
            String coloredPing = getColorFromPing(getPing(), settings) + String.valueOf(getPing()) + (settings.shouldShowMS() ? "ms" : "");
            return (getPing() <= settings.getLoadingMaximum() ? ChatColor.DARK_GRAY + "Loading..." : coloredPing);
        }

        /**
         * Gets the colored ping of the player using no {@link PingSettings}
         * @return the colored ping using the default values of {@link PingSettings}
         * @since 15
         */
        public String getColoredPing() { return getColoredPing(null); }

        /**
         * Gets the {@link ChatColor} used for a certain number for player's ping. Used in {@link OnlineActions#getColoredPing(PingSettings)}
         * @param ping the player's ping, without "ms" at the end
         * @param settings the {@link PingSettings}
         * @return the {@link ChatColor} that should be used to represent that ping number
         * @since 15
         */
        public ChatColor getColorFromPing(int ping, @Nullable PingSettings settings) {
            if(settings == null) settings = PingSettings.builder().build();

            if(ping <= settings.getLoadingMaximum()) return ChatColor.DARK_GRAY;
            else if(NumberUtils.isBetweenEquals(ping, settings.getGreenMinimum(), settings.getGreenMaximum())) return ChatColor.GREEN;
            else if(NumberUtils.isBetweenEquals(ping, settings.getYellowMinimum(), settings.getYellowMaximum())) return ChatColor.YELLOW;
            else if(NumberUtils.isBetweenEquals(ping, settings.getRedMinimum(), settings.getRedMaximum())) return ChatColor.RED;
            else if(ping >= settings.getDarkRedMinimum()) return ChatColor.DARK_RED;
            throw new IllegalArgumentException("An error occurred with the " + PingSettings.class.getCanonicalName() + " for " + CyberAPI.getInstance().getPluginName() + " -> a value was somehow missed in the " + PingSettings.Builder.class.getCanonicalName() + " (" + ping + ")");
        }

        /**
         * Kicks the player from the server for a specified reason
         * @param reason the kick message/reason
         * @since 15
         */
        public void kick(String reason) { player.disconnect(UChat.bComponent(reason)); }

        /**
         * Kicks a player from the server
         * @since 15
         */
        public void kick() { player.disconnect(); }

        /**
         * Prints the user's head in their chat using specified {@link UserHeadSettings}
         * <br>
         * <b>Note: This is obtaining the user head from a URL, meaning you should cache this or use asynchronous events</b>
         * @param settings the settings to apply to {@link CyberPlayer#getUserHead(UserHeadSettings)}
         * @throws IOException if the player or the URL cannot resolve
         * @see CyberPlayer#getUserHead(UserHeadSettings)
         * @since 15
         */
        public void printUserHead(@Nullable UserHeadSettings settings) throws IOException {
            player.sendMessage(UChat.bComponent(getUserHead(settings)));
        }

        /**
         * Prints the user's head in their chat using default {@link UserHeadSettings} settings
         * <br>
         * <b>Note: This is obtaining the user head from a URL, meaning you should cache this or use asynchronous events</b>
         * @throws IOException if the player or the URL cannot resolve
         * @see CyberPlayer#getUserHead(UserHeadSettings)
         * @see OnlineActions#printUserHead(UserHeadSettings)
         * @since 15
         */
        public void printUserHead() throws IOException {
            printUserHead(UserHeadSettings.builder().build());
        }
    }

}
