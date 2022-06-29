package net.cybercake.cyberapi.server.serverlist.motd;

import net.cybercake.cyberapi.CyberAPI;
import net.cybercake.cyberapi.server.ServerProperties;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.File;

/**
 * The builder for {@link MOTD}
 * @since 3.1.0
 */
public class MOTDBuilder {

    private final String id;

    private String motd;
    private boolean centered;
    private boolean useMiniMessage;
    private boolean alwaysReCache;
    private File icon;

    /**
     * Creates an instance of {@link MOTDBuilder}, must end with .build() to build!
     */
    public MOTDBuilder(String id) {
        this.id = id;
        if(!StringUtils.isAlphanumeric(id.replace("_", ""))) throw new IllegalArgumentException("ID in " + this.getClass().getCanonicalName() + " must only contain a-z, A-Z, 0-9, and _ (id=" + id + ")");
        if(CyberAPI.getInstance().getServerListInfo().getMOTDManager().getMOTDFromID(id) != null) throw new IllegalArgumentException("ID in " + this.getClass().getCanonicalName() + " already exists! (id=" + id + ")");

        this.motd = String.valueOf(new ServerProperties().getProperty("motd"));
        this.centered = false;
        this.useMiniMessage = false;
        this.alwaysReCache = false;
        this.icon = null;
    }

    /**
     * Sets the MOTD to a String.
     * @param motd the motd string
     */
    public MOTDBuilder text(String motd) {
        this.motd = motd; return this;
    }

    /**
     * Sets the MOTD to two strings as different lines
     * @param line1 the first line of the MOTD
     * @param line2 the second line of the MOTD
     */
    public MOTDBuilder text(String line1, String line2) {
        this.motd = line1 + "\n" + line2; return this;
    }

    /**
     * Sets the MOTD to multiple strings
     * @param motd the motd strings
     */
    public MOTDBuilder text(String... motd) {
        this.motd = String.join("\n", motd); return this;
    }

    /**
     * Should the text be Centered (only works with legacy color codes, {@link MOTDBuilder#shouldUseMiniMessage(boolean)} will cancel this out)
     * @param centered should the MOTD be centered
     */
    public MOTDBuilder shouldCenter(boolean centered) {
        this.centered = centered; return this;
    }

    /**
     * Should the text use MiniMessage formatting instead of legacy bukkit color codes (will cancel out {@link MOTDBuilder#shouldCenter(boolean)})
     * @param useMiniMessage use bukkit color codes (set to 'false') or use mini message (set to 'true')
     */
    public MOTDBuilder shouldUseMiniMessage(boolean useMiniMessage) {
        this.useMiniMessage = useMiniMessage; return this;
    }

    /**
     * Sets the MOTD icon to this image, a {@link File}
     * @param icon the {@link File} to cached
     */
    public MOTDBuilder icon(@Nullable File icon) {
        this.icon = icon; return this;
    }

    /**
     * Builds this builder into a finalized {@link MOTD}, to be used in CyberAPI somewhere
     * @return the finalized MOTD
     * @since 3.1.0
     */
    public MOTD build() {
        return new MOTD(this.toString());
    }

    /**
     * Returns a string form of the data here. This is what {@link MOTD} uses.
     * @return string of the data, looks like -> {@code MOTDBuilder{key=value, key1=value1}}
     */
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                attachValue("id", id) +
                attachValue("motd", motd) +
                attachValue("centered", centered) +
                attachValue("useMiniMessage", useMiniMessage) +
                attachValue("alwaysReCache", alwaysReCache) +
                attachValue("iconDir", icon == null ? "unset" : icon.getAbsolutePath(), true);
    }

    private String attachValue(String name, Object value) { return name + "=" + String.valueOf(value).replace("=", "${EQUALS}").replace(",", "${COMMA}") + ", "; }
    private String attachValue(String name, Object value, boolean last) { return attachValue(name, value).substring(0, last ? attachValue(name, value).length()-2 : 0) + (last ? "}" : ""); }

}
