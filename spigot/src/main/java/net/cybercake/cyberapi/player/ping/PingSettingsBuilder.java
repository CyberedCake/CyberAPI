package net.cybercake.cyberapi.player.ping;

import net.cybercake.cyberapi.player.CyberPlayer;

import java.util.Arrays;

/**
 * The builder for {@link PingSettings}
 */
public class PingSettingsBuilder {

    private int loading;
    private int greenMin;
    private int greenMax;
    private int yellowMin;
    private int yellowMax;
    private int redMin;
    private int redMax;
    private int darkRedMin;

    private boolean showMS;

    /**
     * Creates a basic {@link PingSettingsBuilder} instance with all the default values, which can be viewed on each method's javadoc
     */
    public PingSettingsBuilder() {
        this.loading = 0;
        this.greenMin = 1;
        this.greenMax = 80;
        this.yellowMin = 81;
        this.yellowMax = 150;
        this.redMin = 151;
        this.redMax = 360;
        this.darkRedMin = 361;

        this.showMS = true;
    }

    /**
     * The loading (dark gray) maximum, any value less than or equal to this value will be {@link org.bukkit.ChatColor#DARK_GRAY} (because that usually means the ping is loading)
     * <br> <br>
     * 0ms is typically used by Minecraft when the ping is unknown, but it will also be used on private servers if you are locally hosting. Because the server is local, your ping will always show "Loading..." (because it is 0ms)
     * @param loading the loading maximum for {@link org.bukkit.ChatColor#DARK_GRAY}
     */
    public PingSettingsBuilder loading(int loading) {
        this.loading = loading; return this;
    }

    /**
     * The green minimum and maximum, any value between or equal to these two values will be {@link org.bukkit.ChatColor#GREEN}
     * @param min the minimum value for {@link org.bukkit.ChatColor#GREEN}
     * @param max the maximum value for {@link org.bukkit.ChatColor#GREEN}
     */
    public PingSettingsBuilder green(int min, int max) {
        this.greenMin = min;
        this.greenMax = max; return this;
    }

    /**
     * The yellow minimum and maximum, any value between or equal to these two values will be {@link org.bukkit.ChatColor#YELLOW}
     * @param min the minimum value for {@link org.bukkit.ChatColor#YELLOW}
     * @param max the maximum value for {@link org.bukkit.ChatColor#YELLOW}
     */
    public PingSettingsBuilder yellow(int min, int max) {
        this.yellowMin = min;
        this.yellowMax = max; return this;
    }

    /**
     * The red minimum and maximum, any value between or equal to these two values will be {@link org.bukkit.ChatColor#RED}
     * @param min the minimum value for {@link org.bukkit.ChatColor#RED}
     * @param max the maximum value for {@link org.bukkit.ChatColor#RED}
     */
    public PingSettingsBuilder red(int min, int max) {
        this.redMin = min;
        this.redMax = max; return this;
    }

    /**
     * The dark red minimum, any value greater than or equal to this value will be {@link org.bukkit.ChatColor#DARK_RED}
     * @param min the minimum value for {@link org.bukkit.ChatColor#DARK_RED}
     */
    public PingSettingsBuilder darkRed(int min) {
        this.darkRedMin = min; return this;
    }

    /**
     * Whether the {@link CyberPlayer.OnlineActions#getColoredPing(PingSettings)} should show the 'ms' at the end, or just return the color and ping
     * @param showMS should {@link CyberPlayer.OnlineActions#getColoredPing(PingSettings)} show 'ms' at the end
     */
    public PingSettingsBuilder showMS(boolean showMS) {
        this.showMS = showMS; return this;
    }

    /**
     * Builds the instance and saves the values
     * @return a newly-built {@link PingSettings} instance, which comes from this instance ({@link PingSettingsBuilder})
     */
    public PingSettings build() {
        Integer[] integers = new Integer[]{loading, greenMin, greenMax, yellowMin, yellowMax, redMin, redMax, darkRedMin};
        String newData = String.join("-", Arrays.toString(integers).substring(0, Arrays.toString(integers).length()-1).split(", "));
        return new PingSettings(newData.substring(1, newData.length()-1) + "-" + showMS);
    }

}
