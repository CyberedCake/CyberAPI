package net.cybercake.cyberapi.spigot.items;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Stores data for every attribute, data includes; minimum, maximum, and default values, along with the attribute key.
 * <p> <i> <small>
 * Most of the data in the Javadoc notes is from the <a href="https://minecraft.wiki/w/Attribute">wiki</a>
 *
 * @since 188
 */
public enum AttributeData {

    /**
     * Maximum health of an Entity.
     * <i><p>
     * 1 = half heart
     */
    MAX_HEALTH("max_health", 1.0f, 1024.0f, 20.0f),

    /**
     * Range at which an Entity will follow others in blocks.
     * <i><p>
     * This attribute doesn't affect players, only entities that can aggro onto other entities.
     */
    FOLLOW_RANGE("follow_range", 0.0f, 2048.0f, 32.0f),

    /**
     * Resistance of an Entity to knockback.
     * <i><p>
     * This attribute only affects horizontal knockback, it does not affect explosions.
     * <p>
     * 0 would be 100% and 1 would be 0% knockback taken.
     */
    KNOCKBACK_RESISTANCE("knockback_resistance", 0.0f, 1.0f, 0.0f),

    /**
     * Movement speed of an Entity.
     */
    MOVEMENT_SPEED("movement_speed", 0.0f, 1024.0f, 0.7f),

    /**
     * Flying speed of an Entity.
     * <i><p>
     * This value only affects entities that fly, excluding the player;
     * there isn't much information on how this attribute works.
     */
    FLYING_SPEED("flying_speed", 0.0f, 1024.0f, 0.4f),

    /**
     * Attack damage of an Entity.
     * <i><p>
     * This is mostly common knowledge but if for some reason you don't know;
     * 1 attack damage translates to half of a heart.
     */
    ATTACK_DAMAGE("attack_damage", 0.0f, 2048.0f, 2.0f),

    /**
     * Attack knockback of an Entity.
     * <i><p>
     * I'm unsure how this attribute works exactly, the wiki also doesn't have much information;
     * from my testing, ~0.4 is the equivalent of the default attack knockback, 0 is also the default.
     * <p>
     * Anything less than ~0.4 will make you do less knockback.
     */
    ATTACK_KNOCKBACK("attack_knockback", 0.0f, 5.0f, 0.0f),

    /**
     * Attack speed of an Entity.
     * <i><p>
     * Its value is the number of full-strength attacks per second.
     */
    ATTACK_SPEED("attack_speed", 0.0f, 1024.0f, 4.0f),

    /**
     * Armor bonus of an Entity.
     */
    ARMOR("armor", 0.0f, 30.0f, 0.0f),

    /**
     * Armor durability bonus of an Entity.
     */
    ARMOR_TOUGHNESS("armor_toughness", 0.0f, 20.0f, 0.0f),

    /**
     * The fall damage multiplier of an Entity.
     * <i><p>
     * The value acts as a multiplier, so 1 is default, 0.5 is half, and 2 is double.
     */
    FALL_DAMAGE_MULTIPLIER("fall_damage_multiplier", 0.0f, 100.0f, 1.0f),

    /**
     * Luck bonus of an Entity.
     * <i><p>
     * Luck increases chances for good drops and rolls for loot tables, fishing, and mob drops.
     */
    LUCK("luck", -1024.0f, 1024.0f, 0.0f),

    /**
     * Maximum absorption of an Entity.
     * <i><p>
     * This attribute lets you keep a certain amount of absorption hearts after the effect wears off.
     */
    MAX_ABSORPTION("max_absorption", 0.0f, 2048.0f, 0.0f),

    /**
     * The distance which an Entity can fall without damage in blocks.
     * <i><p>
     * Unsure on what happens when the value is negative right now.
     */
    SAFE_FALL_DISTANCE("safe_fall_distance", -1024.0f, 1024.0f, 3.0f),

    /**
     * The relative scale of an Entity.
     */
    SCALE("scale", 0.0625f, 16.0f, 1.0f),

    /**
     * The height which an Entity can walk over in blocks.
     */
    STEP_HEIGHT("step_height", 0.0f, 10.0f, 0.6f),

    /**
     * The gravity applied to an Entity.
     * <i><p>
     * The value is in blocks per tick squared.
     */
    GRAVITY("gravity", -1.0f, 1.0f, 0.08f),

    /**
     * Strength with which an Entity will jump.
     * <i><p>
     * The value is in blocks per tick.
     */
    JUMP_STRENGTH("jump_strength", 0.0f, 32.0f, 0.41999998688697815f),

    /**
     * How long an entity remains burning after ignition.
     * <i><p>
     * The value acts as a multiplier, so 1 is default, 0.5 is half, and 2 is double.
     */
    BURNING_TIME("burning_time", 0.0f, 1024.0f, 1.0f),

    /**
     * The distance at which the camera is placed away when in third-person view in blocks.
     * <i><p>
     * Camera distance is affected by entity size and scale.
     */
    CAMERA_DISTANCE("camera_distance", 0.0f, 32.0f, 4.0f),

    /**
     * Resistance to knockback from explosions.
     * <i><p>
     * The value is a percentage of knockback from explosions an entity resists;
     * a value of 1 eliminates the knockback.
     */
    EXPLOSION_KNOCKBACK_RESISTANCE("explosion_knockback_resistance", 0.0f, 1.0f, 0.0f),

    /**
     * Movement speed through difficult terrain.
     * <i><p>
     * A value of 1 removes any terrain slowdown.
     */
    MOVEMENT_EFFICIENCY("movement_efficiency", 0.0f, 1.0f, 0.0f),

    /**
     * Oxygen use underwater.
     * <i><p>
     * The value determines the chance that an entity's air decreases in any game tick, while underwater.
     * <p>
     * The chance is given by 1 / (oxygen_bonus + 1).
     */
    OXYGEN_BONUS("oxygen_bonus", 0.0f, 1024.0f, 0.0f),

    /**
     * Movement speed through water.
     * <p><i>
     * A value of 1 removes any water slowdown.
     */
    WATER_MOVEMENT_EFFICIENCY("water_movement_efficiency", 0.0f, 1.0f, 0.0f),

    /**
     * Range at which mobs will be tempted by items in blocks.
     */
    TEMPT_RANGE("tempt_range", 0.0f, 2024.0f, 10.0f),

    /**
     * The block reach distance of a Player in blocks.
     * <i><p>
     * Being in creative increases this value by 0.5, it won't appear on the attribute,
     * but it is still capped at 64, so the max value in creative is 63.5.
     * <p>
     * Setting the value to -0.5 while in creative will have the same effect as setting the value to 0.
     */
    BLOCK_INTERACTION_RANGE("block_interaction_range", 0.0f, 64.0f, 4.5f),

    /**
     * The entity reach distance of a Player in blocks.
     * <i><p>
     * Being in creative increases this value by 2, it won't appear on the attribute,
     * but it is still capped at 64, so the max value in creative is 62.
     * <p>
     * Setting the value to -2 while in creative will have the same effect as setting the value to 0.
     */
    ENTITY_INTERACTION_RANGE("entity_interaction_range", 0.0f, 64.0f, 3.0f),

    /**
     * Block break speed of a Player.
     * <i><p>
     * The value acts as a multiplier, so 1 is default, 0.5 is half, and 2 is double.
     */
    BLOCK_BREAK_SPEED("block_break_speed", 0.0f, 1024.0f, 1.0f),

    /**
     * Mining speed for correct tools.
     * <i><p>
     * 'A factor' to speed up the mining of blocks when using the right tool, unsure on exactly how it works.
     */
    MINING_EFFICIENCY("mining_efficiency", 0.0f, 1024.0f, 0.0f),

    /**
     * Sneaking speed.
     * <i><p>
     * If the value is 1, sneaking or crawling will be the same speed as walking, if it's 0 you will be unable to move.
     */
    SNEAKING_SPEED("sneaking_speed", 0.0f, 1.0f, 0.3f),

    /**
     * Underwater mining speed.
     * <i><p>
     * This value is a multiplier where 1 makes your underwater mining speed the same as normal, and 0 makes you unable to mine underwater.
     */
    SUBMERGED_MINING_SPEED("submerged_mining_speed", 0.0f, 20.0f, 0.2f),

    /**
     * Sweeping damage.
     * <i><p>
     * This value attribute determines how much of the base attack damage gets
     * transferred to secondary targets in a sweep attack.
     * <p>
     * A value of 1 means that all the base attack damage is transferred.
     */
    SWEEPING_DAMAGE_RATIO("sweeping_damage_ratio", 0.0f, 1.0f, 0.0f),

    /**
     * Chance of a zombie to spawn reinforcements.
     */
    SPAWN_REINFORCEMENTS("spawn_reinforcements", 0.0f, 1.0f, 0.0f),

    /**
     * Waypoint transmission range.
     * <i><p>
     * This attribute only works on players
     * <p>
     * The value determines the maximum distance (in blocks) from the player to a waypoint at
     * which the waypoint is displayed on the locator bar.
     */
    WAYPOINT_TRANSMIT_RANGE("waypoint_transmit_range", 0.0f, 60_000_000.0f, 0.0f),

    /**
     * Waypoint receive range.
     * <i><p>
     * This attribute only works on players
     * <p>
     * The value determines the distance at which an entity displays as a waypoint on the locator bar.
     */
    WAYPOINT_RECEIVE_RANGE("waypoint_receive_range", 0.0f, 60_000_000.0f, 0.0f),
    ;

    private final String key;
    private final Float minValue;
    private final Float maxValue;
    private final Float defaultValue;

    AttributeData(String key, Float minValue, Float maxValue, Float defaultValue) {
        this.key = key;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
    }

    /**
     * Gets {@link AttributeData} from an {@link Attribute}.
     * <p> <i>
     * {@link AttributeData} contains the minimum, maximum, and default values for each attribute relative to the player.
     *
     * @return {@link AttributeData} according to the {@link Attribute} provided; null if no matching data is found.
     * @since 188
     */
    public static @Nullable AttributeData getAttributeData(@Nullable Attribute attribute) {
        if (attribute == null) return null;

        for (AttributeData attributeData : AttributeData.values()) {

            NamespacedKey key = attribute.getKeyOrNull();

            if (key == null || !Objects.equals(attributeData.key, key.getKey()))
                continue;

            return attributeData;
        }

        return null;
    }

    public String getKey() {
        return key;
    }

    public Float getMinValue() {
        return minValue;
    }

    public Float getDefaultValue() {
        return defaultValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

}
