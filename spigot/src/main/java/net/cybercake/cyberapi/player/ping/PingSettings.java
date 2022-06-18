package net.cybercake.cyberapi.player.ping;

import net.cybercake.cyberapi.CyberAPI;
import net.cybercake.cyberapi.player.CyberPlayer;

/**
 * The finalized methods and data for {@link PingSettingsBuilder}
 */
public class PingSettings {

    private final int loading;
    private final int greenMin;
    private final int greenMax;
    private final int yellowMin;
    private final int yellowMax;
    private final int redMin;
    private final int redMax;
    private final int darkRedMin;

    private final boolean showMS;

    /**
     * Builds a new {@link PingSettings} instance from {@link PingSettingsBuilder} data, in {@link String} form
     * @param data the data to deserialize
     */
    public PingSettings(String data) {
        try {
            this.loading = Integer.parseInt(data.split("-")[0]);
            this.greenMin = Integer.parseInt(data.split("-")[1]);
            this.greenMax = Integer.parseInt(data.split("-")[2]);
            this.yellowMin = Integer.parseInt(data.split("-")[3]);
            this.yellowMax = Integer.parseInt(data.split("-")[4]);
            this.redMin = Integer.parseInt(data.split("-")[5]);
            this.redMax = Integer.parseInt(data.split("-")[6]);
            this.darkRedMin = Integer.parseInt(data.split("-")[7]);
            this.showMS = Boolean.parseBoolean(data.split("-")[8]);
        } catch (Exception exception) {
            throw new IllegalArgumentException("An error occurred whilst trying to build the PingSettings instance, try again later and contact the developers of " + CyberAPI.getInstance().getPluginName() + "! " + exception);
        }
    }

    /**
     * Gets the loading value set by the developer in {@link PingSettingsBuilder}
     * @return the loading value
     */
    public int getLoading() { return loading; }

    /**
     * Gets the green minimum value set by the developer in {@link PingSettingsBuilder}
     * @return the green minimum value
     */
    public int getGreenMin() { return greenMin; }

    /**
     * Gets the green maximum value set by the developer in {@link PingSettingsBuilder}
     * @return the green maximum value
     */
    public int getGreenMax() { return greenMax; }

    /**
     * Gets the yellow minimum value set by the developer in {@link PingSettingsBuilder}
     * @return the yellow minimum value
     */
    public int getYellowMin() { return yellowMin; }

    /**
     * Gets the yellow maximum value set by the developer in {@link PingSettingsBuilder}
     * @return the yellow maximum value
     */
    public int getYellowMax() { return yellowMax; }

    /**
     * Gets the red minimum value set by the developer in {@link PingSettingsBuilder}
     * @return the red minimum value
     */
    public int getRedMin() { return redMin; }

    /**
     * Gets the red maximum value set by the developer in {@link PingSettingsBuilder}
     * @return the red maximum value
     */
    public int getRedMax() { return redMax; }

    /**
     * Gets the dark red minimum value set by the developer in {@link PingSettingsBuilder}
     * @return the dark red minimum value
     */
    public int getDarkRedMin() { return darkRedMin; }

    /**
     * Gets whether the API should show 'ms' at the end of {@link CyberPlayer.OnlineActions#getColoredPing(PingSettings)}, set by the developer in {@link PingSettingsBuilder}
     * @return should API show 'ms'
     */
    public boolean shouldShowMs() { return showMS; }

}
