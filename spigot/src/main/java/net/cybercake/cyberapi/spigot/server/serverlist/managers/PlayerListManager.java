package net.cybercake.cyberapi.spigot.server.serverlist.managers;

import net.cybercake.cyberapi.common.basic.NumberUtils;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.server.ServerProperties;
import net.cybercake.cyberapi.spigot.server.serverlist.ServerListInfo;
import net.cybercake.cyberapi.spigot.server.serverlist.players.NewPlayerCountType;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PlayerListManager {

    /**
     * Creates an instance of Player List manager
     * @deprecated Please use {@link ServerListInfo#getMOTDManager()} instead!
     */
    @Deprecated
    @SuppressWarnings({"all"})
    public PlayerListManager() {
        resetMaxPlayers();
        resetOnlinePlayerCount();
        resetShowPlayers();
    }

    private static PlayerListManager playerListManager = null;
    public static PlayerListManager playerListManager() {
        if(PlayerListManager.playerListManager == null) PlayerListManager.playerListManager = new PlayerListManager();
        return PlayerListManager.playerListManager;
    }

    private final List<String> players = new ArrayList<>();
    private Integer maxPlayers;
    private Object newCurrentPlayers;
    private NewPlayerCountType newPlayerCountType;
    private boolean showPlayers;

    /**
     * Sets the players online. <b>Note: This has no effect on the current player count/max player count!</b>.
     * <br>
     * This method resets the hover event when hovering over the player count and sets the players to the parameter.
     * @param players the players to show when hovering over the player count
     * @since 9
     */
    public void setOnlinePlayers(String... players) {
        clearOnlinePlayers();
        List.of(players).forEach(this::addOnlinePlayer);
    }

    /**
     * Adds a player to the players online without resetting it first. <b>Note: This has no effect on the current player count/max player count!</b>
     * @param player the player to add to the hover event
     * @since 9
     */
    public void addOnlinePlayer(String player) {
        this.players.add(UChat.chat(player));
    }

    /**
     * Removes a player from the players online using the exact {@link String} match. <b>Note: This has no effect on the current player count/max player count!</b>
     * @param player the player to remove from the hover event, must be an exact match to a currently online player to remove (alternatively, you could use {@link PlayerListManager#removeOnlinePlayer(int)})
     * @since 9
     */
    public void removeOnlinePlayer(String player) {
        this.players.remove(player);
    }

    /**
     * Removes a player from the players online using an index number. <b>Note: This has no effect on the current player count/max player count!</b>
     * @param index the index of the player to remove, 0 being the first item on the list (alternatively, you could use {@link PlayerListManager#removeOnlinePlayer(String)})
     * @since 9
     * @throws IndexOutOfBoundsException if the index is out of bounds ({@code index < 0 || index >= size()})
     */
    public void removeOnlinePlayer(int index) {
        this.players.remove(index);
    }

    /**
     * Get a list of the fake online players provided by the user in {@link PlayerListManager#addOnlinePlayer(String)} or {@link PlayerListManager#setOnlinePlayers(String...)}.
     * @return the fake online players (that show up in the hover event when hovering over the player count in the multiplayer menu)
     * @since 9
     */
    public List<String> getCustomOnlinePlayers() {
        return this.players;
    }

    /**
     * Clears the fake online players that show when hovering over the player count in the multiplayer menu.
     * @since 9
     */
    public void clearOnlinePlayers() {
        this.players.clear();
    }

    /**
     * The fake players online, that show up when you hover over the player count in the multiplayer menu, current size.
     * @return the amount of fake online players
     * @since 9
     */
    public int getCustomOnlinePlayersSize() {
        return this.players.size();
    }

    /**
     * Sets the maximum player count displayed on the server list. <b>Note: This has no effect on the actual max player count! Players can still join after it hits the maximum, it's just for show.</b>
     * @param maxPlayers the fake max player count
     * @since 9
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Gets the current maximum player count displayed on the server list.
     * @return the amount of fake max players
     * @since 9
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Resets the fake maximum player count to it's value from the server.properties
     * @since 9
     */
    public void resetMaxPlayers() {
        this.maxPlayers = Integer.parseInt(String.valueOf(new ServerProperties().getProperty("max-players")));
    }

    /**
     * Sets the current player count for the players when viewing the server in the multiplayer menu
     * @param newPlayerCountType what effect {@code newCurrentPlayers} should have on the server
     * @param newCurrentPlayers the new current players that show up on the multiplayer menu
     * @since 9
     */
    public void setOnlinePlayerCount(NewPlayerCountType newPlayerCountType, @Nullable Object newCurrentPlayers) {
        validateSetCurrentPlayerCountArgs(newPlayerCountType, newCurrentPlayers);
        this.newPlayerCountType = newPlayerCountType;
        this.newCurrentPlayers = switch(newPlayerCountType) {
            case CONSTANT, STAY_AT -> Integer.parseInt(String.valueOf(newCurrentPlayers));
            case PERCENT -> Double.parseDouble(String.valueOf(newCurrentPlayers));
            case RANDOM_BETWEEN -> String.valueOf(newCurrentPlayers);
            case KEEP_SAME -> -1;
        };
    }

    /**
     * Gets the raw version that was entered into {@link PlayerListManager#setOnlinePlayerCount(NewPlayerCountType, Object)}
     * @return the raw new current players value
     * @since 9
     */
    public Object getOnlinePlayersRaw() {
        return newCurrentPlayers;
    }

    /**
     * Gets the new player count type that is used in the calculation for {@link PlayerListManager#getOnlinePlayers()}
     * @return the {@link NewPlayerCountType}
     * @since 9
     */
    public NewPlayerCountType getOnlinePlayersType() {
        return newPlayerCountType;
    }

    /**
     * Gets the online players list according to the settings set using {@link PlayerListManager#setOnlinePlayerCount(NewPlayerCountType, Object)}
     * @return the custom player count
     * @since 9
     */
    public int getOnlinePlayers() {
        int onlinePlayers = CyberAPI.getInstance().getOnlinePlayers().size();
        String newCurrentPlayersString = String.valueOf(newCurrentPlayers);
        if(getOnlinePlayersType() == null) return onlinePlayers;
        return switch(getOnlinePlayersType()) {
            case STAY_AT -> Integer.parseInt(newCurrentPlayersString);
            case CONSTANT -> onlinePlayers+Integer.parseInt(newCurrentPlayersString);
            case PERCENT -> Integer.parseInt(NumberUtils.formatDecimal(onlinePlayers+(onlinePlayers*Double.parseDouble(newCurrentPlayersString)), 0, RoundingMode.HALF_EVEN));
            case RANDOM_BETWEEN -> onlinePlayers+(NumberUtils.randomInt(Integer.parseInt(newCurrentPlayersString.split(":")[0]), Integer.parseInt(newCurrentPlayersString.split(":")[1])));
            default -> onlinePlayers;
        };
    }

    /**
     * Resets the current player count for the players viewing the server in the multiplayer menu
     * @since 9
     */
    public void resetOnlinePlayerCount() {
        this.newPlayerCountType = null;
        this.newCurrentPlayers = 0.0D;
    }

    /**
     * Should the server show the players online or the player count. This will make your custom player hover clear and set the player count to '???'
     * @param showPlayers should the plugin show the online players
     * @since 9
     */
    public void setShowPlayers(boolean showPlayers) {
        this.showPlayers = showPlayers;
    }

    /**
     * Resets the showPlayers boolean to the server.properties value
     * @since 9
     */
    public void resetShowPlayers() {
        this.showPlayers = !Boolean.parseBoolean(String.valueOf(new ServerProperties().getProperty("hide-online-players")));
    }

    /**
     * Should the plugin show the player count and player list. If set to false, it will show '???' as the player count.
     * <br>
     * <b>Note: Will override your custom player count and the hoverable player list!</b>
     * @return should the server send a player count and player hover packet to the client
     * @since 9
     */
    public boolean shouldShowPlayers() {
        return this.showPlayers;
    }




    /**
     * Private method used in {@link PlayerListManager#setOnlinePlayerCount(NewPlayerCountType, Object)}
     * @since 9
     */
    private void validateSetCurrentPlayerCountArgs(NewPlayerCountType newPlayerCountType, Object newCurrentPlayers) {
        if(newPlayerCountType == NewPlayerCountType.KEEP_SAME) return;

        if(newPlayerCountType.getType() != null && !(newCurrentPlayers.getClass() == newPlayerCountType.getType()))
            throw new IllegalArgumentException("newCurrentPlayers must be a " + newPlayerCountType.getType().getCanonicalName() + " (currently " + newCurrentPlayers.getClass().getCanonicalName() + ") because newPlayerCountType is set to " + newPlayerCountType.name() + "!");

        switch(newPlayerCountType) {
//            case CONSTANT -> {
//                if(!NumberUtils.isInteger(String.valueOf(newCurrentPlayers))) throw validateSetCurrentPlayerCountArgsIllegalArgument("The constant number must be an integer!", null);
//            }
            case PERCENT -> {
//                if(!NumberUtils.isDouble(String.valueOf(newCurrentPlayers))) throw validateSetCurrentPlayerCountArgsIllegalArgument("The percent number must be a double!", null);
                if(NumberUtils.isBetweenEquals(Double.parseDouble(String.valueOf(newCurrentPlayers)), 0.0, 1.0)) throw validateSetCurrentPlayerCountArgsIllegalArgument("The percent number must be between 0.0 and 1.0!", null);
            }
            case RANDOM_BETWEEN -> {
                String newCurrentPlayersString = String.valueOf(newCurrentPlayers);
                if(newCurrentPlayersString.split(":").length != 1) throw validateSetCurrentPlayerCountArgsIllegalArgument("Must contain a ':' to separate values!", null);
                String firstValue = newCurrentPlayersString.split(":")[0];
                String secondValue = newCurrentPlayersString.split(":")[1];
                if(!(NumberUtils.isInteger(firstValue)) || !(NumberUtils.isInteger(secondValue))) throw validateSetCurrentPlayerCountArgsIllegalArgument("Both values on either side of the colon ':' must be integers!", null);
                int firstInt = Integer.parseInt(firstValue);
                int secondInt = Integer.parseInt(secondValue);
                if(firstInt > secondInt) throw validateSetCurrentPlayerCountArgsIllegalArgument("The first integer must be smaller than the second integer!", null);
                // this means that what CyberAPI is going to use in setCurrentPlayerCount() can be safely assumed to be correct
            }
        }
    }

    /**
     * Private method used in {@link PlayerListManager#validateSetCurrentPlayerCountArgs(NewPlayerCountType, Object)}
     * @since 9
     */
    private IllegalArgumentException validateSetCurrentPlayerCountArgsIllegalArgument(String why, Throwable cause) {
        String reason = "Failed to parse newCurrentPlayers: " + why;
        if(cause != null) return new IllegalArgumentException(why, cause);
        return new IllegalArgumentException(reason);
    }

}
