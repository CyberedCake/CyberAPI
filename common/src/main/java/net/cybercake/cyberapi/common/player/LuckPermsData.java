package net.cybercake.cyberapi.common.player;

import net.cybercake.cyberapi.common.CommonAdapter;
import net.cybercake.cyberapi.common.builders.settings.FeatureSupport;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedData;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import net.luckperms.api.util.Tristate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * The LuckPerms data for the user, if you have LuckPerms supported in {@link net.cybercake.cyberapi.common.builders.settings.Settings your settings}
 * @since 181
 */
public class LuckPermsData {

    /**
     * The LuckPerms provider for CyberAPI
     * @since 181
     */
    public static final LuckPerms luckPerms = LuckPermsProvider.get();

    /**
     * The LuckPerms user manager for CyberAPI
     * @since 181
     */
    public static final UserManager userManager = luckPerms.getUserManager();

    private final PlayerAdapter<?> adapter;
    private final User user;

    /**
     * Creates a LuckPermsData object from a specific {@link PlayerAdapter player}
     * @param adapter the player to retrieve data from
     * @since 181
     */
    public LuckPermsData(PlayerAdapter<?> adapter) {
        this.adapter = adapter;
        this.user = userManager.getUser(adapter.getUUID());
    }

    /**
     * Gets the LuckPerms user, or as LuckPerms describes it: <br>
     * "A player which holds permission data" (view more: {@link User})
     * @return a LuckPerms {@link User}
     * @since 181
     */
    public User getUser() { return this.user; }

    /**
     * Gets the player's username that LuckPerms has cached
     * @return a username as a {@link String}, usually all lower case
     * @apiNote see CyberPlayer.getUsername(), as it also caches usernames
     * @since 181
     */
    public String getLuckPermsUsername() { return this.getUser().getUsername(); }

    /**
     * Gets the {@link CachedDataManager} of the player that LuckPerms uses, or as LuckPerms describes it: <br>
     * "Holds cached permission and meta lookup data for a {@link net.luckperms.api.model.PermissionHolder PermissionHolder}." (view more: {@link CachedDataManager})
     * @return the {@link CachedDataManager} of the player
     * @since 181
     */
    public CachedDataManager getCachedData() { return this.getUser().getCachedData(); }

    /**
     * Gets the {@link CachedMetaData} of the player that LuckPerms uses, or as LuckPerms describes it: <br>
     * "Holds cached meta lookup data for a specific set of contexts." (view more: {@link CachedMetaData})
     * @return the {@link CachedMetaData} of the player
     * @since 181
     */
    public CachedMetaData getCachedMetaData() { return this.getCachedData().getMetaData(); }

    /**
     * Gets the {@link CachedPermissionData} of the player that LuckPerms uses, or as LuckPerms describes it: <br>
     * "Holds cached permission lookup data for a specific set of contexts." (view more: {@link CachedPermissionData})
     * @return the {@link CachedPermissionData} of the player
     * @since 181
     */
    public CachedPermissionData getCachedPermissionData() { return this.getCachedData().getPermissionData(); }

    /**
     * Gets the prefix of the player currently being used by LuckPerms (in {@link CachedMetaData})
     * @return the {@link String} version of the prefix held by LuckPerms
     * @since 181
     */
    public @Nullable String getPrefix() { return this.getCachedMetaData().getPrefix(); }

    /**
     * Sets a user's prefix to a specific {@link String} at a certain priority-level
     * @param priority the priority of the prefix. For example, if the user has two prefixes, "Prefix 1 " at priority = 3 and "Prefix 2" at priority = 5, only "Prefix 2" will show up on the user
     * @param prefix the new prefix of the user
     * @see LuckPermsData#setUserPrefix(String)
     * @see LuckPermsData#clearUserPrefix()
     * @since 181
     */
    public void setUserPrefix(int priority, String prefix) {
        user.data().add(PrefixNode.builder(prefix, priority).build());
        save();
    }

    /**
     * Sets a user's prefix to a specific {@link String} at the maximum priority level
     * @param prefix the new prefix of the user
     * @see LuckPermsData#setUserPrefix(int, String)
     * @see LuckPermsData#clearUserPrefix()
     * @since 181
     */
    public void setUserPrefix(String prefix) {
        setUserPrefix(Integer.MAX_VALUE, prefix);
    }

    /**
     * Clears a user prefix and defaults to whatever the group prefix is.
     * @see LuckPermsData#setUserPrefix(int, String)
     * @see LuckPermsData#setUserPrefix(String)
     * @since 181
     */
    public void clearUserPrefix() {
        user.data().clear(NodeType.PREFIX.predicate());
        save();
    }

    /**
     * Gets the suffix of the player currently being used by LuckPerms (in {@link CachedMetaData})
     * @return the {@link String} version of the prefix held by LuckPerms
     * @since 181
     */
    public @Nullable String getSuffix() { return this.getCachedMetaData().getSuffix(); }

    /**
     * Sets a user's suffix to a specific {@link String} at a certain priority-level
     * @param priority the priority of the suffix. For example, if the user has two suffixes, " Suffix 1" at priority = 3 and " Suffix 2" at priority = 5, only "Suffix 2" will show up on the user
     * @param suffix the new suffix of the user
     * @see LuckPermsData#setUserSuffix(String)
     * @see LuckPermsData#clearUserSuffix()
     * @since 181
     */
    public void setUserSuffix(int priority, String suffix) {
        user.data().add(SuffixNode.builder(suffix, priority).build());
        save();
    }

    /**
     * Sets a user's suffix to a specific {@link String} at the maximum priority level
     * @param suffix the new suffix of the user
     * @see LuckPermsData#setUserSuffix(int, String)
     * @see LuckPermsData#clearUserSuffix()
     * @since 181
     */
    public void setUserSuffix(String suffix) {
        setUserSuffix(0, suffix);
    }

    /**
     * Clears a user suffix and defaults to whatever the group prefix is.
     * @see LuckPermsData#setUserSuffix(int, String)
     * @see LuckPermsData#setUserSuffix(String)
     * @since 181
     */
    public void clearUserSuffix() {
        user.data().clear(NodeType.SUFFIX.predicate());
        save();
    }

    /**
     * Clears a user's metadata based on a certain condition
     * @param predicate condition
     * @since 181
     */
    public void clearUserMetaData(Predicate<? super MetaNode> predicate) {
        user.data().clear(NodeType.META.predicate(predicate));
        save();
    }

    /**
     * Saves the user's data and metadata. See {@link UserManager#saveUser(User) LuckPerm's saveUser(User) method} for more information.
     * @since 181
     */
    public void save() {
        luckPerms.getUserManager().saveUser(user);
    }

    /**
     * Get the user's display name, which is just their username surrounded by the {@link LuckPermsData#getPrefix() LuckPerms prefix} and {@link LuckPermsData#getSuffix() LuckPerms suffix}.
     * @return the display name of the player
     * @since 181
     */
    public String getDisplayName() { return (getPrefix() == null ? "" : getPrefix()) + this.adapter.getUsername() + (getSuffix() == null ? "" : getSuffix()); }

    /**
     * Retrieves whether the user has a certain permission as a {@link Tristate}.
     * @param permission the permission node to check
     * @return a {@link Tristate}, which has values of {@link Tristate#TRUE}, {@link Tristate#FALSE}, {@link Tristate#UNDEFINED}
     * @see CachedPermissionData#checkPermission(String)
     * @see LuckPermsData#hasPermission(String)
     * @since 181
     */
    public Tristate checkPermission(String permission) { return getCachedPermissionData().checkPermission(permission); }

    /**
     * Retrieves whether the user has a certain permission.
     * @param permission the permission node to check
     * @return a boolean, true if user has this permission and is set to true, false if user does not have permission, or it's set to false
     * @since 181
     */
    public boolean hasPermission(String permission) { return checkPermission(permission).asBoolean(); }

    /**
     * View {@link CachedMetaData#getMetaValue(String)} for more information
     * @see CachedMetaData#getMetaValue(String)
     * @since 181
     */
    public String getMetaValue(String key) { return getCachedMetaData().getMetaValue(key); }

}
