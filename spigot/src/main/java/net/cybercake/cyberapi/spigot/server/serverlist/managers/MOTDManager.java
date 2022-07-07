package net.cybercake.cyberapi.spigot.server.serverlist.managers;

import net.cybercake.cyberapi.common.basic.NumberUtils;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.server.serverlist.ServerListInfo;
import net.cybercake.cyberapi.spigot.server.serverlist.motd.MOTD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MOTDManager {

    /**
     * Creates an instance of MOTD manager
     * @deprecated Please use {@link ServerListInfo#getMOTDManager()} instead!
     */
    @Deprecated
    @SuppressWarnings({"all"})
    public MOTDManager() { }

    private static MOTDManager motdManager = null;
    public static MOTDManager motdManager() {
        if(MOTDManager.motdManager == null) MOTDManager.motdManager = new MOTDManager();
        return MOTDManager.motdManager;
    }

    private final List<MOTD> motds = new ArrayList<>();

    /**
     * Shows the list of MOTDs.
     * <br> <br>
     * Add an MOTD by using the {@link MOTDManager#addMOTD(MOTD)}
     * @return a list of active MOTDs created by the plugin
     * @since 9
     */
    public List<MOTD> getMOTDs() { return motds; }

    /**
     * Every MOTD has an id, because when you build the MOTD, using {@code new MOTDBuilder(String)}, it attaches the id to the MOTD.
     * @param id the id to attempt to get from
     * @return the MOTD obtained
     * @since 9
     */
    public MOTD getMOTDFromID(String id) {
        for(MOTD motd : motds) {
            if(motd.getID().equals(id)) return motd;
        } return null;
    }

    /**
     * Finds a random MOTD using the method {@link NumberUtils#randomInt(int, int)}
     * @return a random {@link MOTD} out of all the ones you have added
     * @since 9
     */
    public MOTD getRandomMOTD() {
        MOTD returned;
        try {
            if(motds.size() == 1) returned = motds.get(0);
            else {
                int number = NumberUtils.randomInt(0, motds.size()-1);
                returned = motds.get(number);
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            returned = null;
        }
        return returned;
    }

    /**
     * Add an {@link MOTD} to the
     * @param motd the {@link MOTD} to add, create by creating a new instance of {@link MOTD.Builder}
     * @since 9
     */
    public void addMOTD(MOTD motd) { motds.add(motd); }

    /**
     * Adds multiple {@link MOTD}s to the {@link MOTD} cache, the best place to put this is in your {@link CyberAPI#onEnable()}
     * @param motds the {@link MOTD} to add, create by creating a new instance of {@link MOTD.Builder}
     * @since 9
     */
    public void addMOTDs(MOTD... motds) { this.motds.addAll(Arrays.asList(motds)); }

    /**
     * Clear all the cached {@link MOTD}s that have been added
     * @since 9
     */
    public void clearMOTDs() { motds.clear(); }

    /**
     * Removes an {@link MOTD} from the cached {@link MOTD}s using its distinctive {@link String} ID
     * @param id the {@link String} ID given to an {@link MOTD} when building it with {@link MOTD.Builder}
     * @since 9
     */
    public void removeMOTD(String id) { motds.remove(getMOTDFromID(id)); }

    /**
     * Returns a formatted MOTD from a MOTD object
     * @param motd the MOTD to format
     * @return the formatted {@link String} MOTD
     * @since 9
     * @see MOTD#getFormattedMOTD()
     */
    public String getFormattedMOTD(MOTD motd) {
        return motd.getFormattedMOTD();
    }

}
