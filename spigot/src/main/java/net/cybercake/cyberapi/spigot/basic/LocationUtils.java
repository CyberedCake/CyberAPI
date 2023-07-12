package net.cybercake.cyberapi.spigot.basic;

import net.cybercake.cyberapi.spigot.basic.base64.Base64Convert;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents utilities to be used regarding anything to do with a {@link Location Bukkit Location object}
 * @since 135
 */
public class LocationUtils {

    /**
     * Represents when the world of a particular {@link Location} object is null
     * @apiNote <b>internal</b>
     */
    @ApiStatus.Internal
    private static final String WORLD_SHOULD_NOT_BE_NULL = "Location should not have a null world";

    /**
     * Represents when two locations that are required to be in the same world are in fact <b>not</b> in the same world :(
     * @apiNote <b>internal</b>
     */
    @ApiStatus.Internal
    private static final String REQUIRE_SAME_WORLD = "The two location, location1 and location2, must be in the same world";

    /**
     * Gets the distance between two {@link Location}s on the X and Z axis, but not the Y axis
     * @param location1 the first location
     * @param location2 the second location
     * @return the distance between {@code location1} and {@code location2}
     * @throws IllegalArgumentException when the two locations are not in the same world
     * @see LocationUtils#getDistance(Location, Location)
     * @since 135
     */
    public static double get2DDistance(Location location1, Location location2) {
        if(!Objects.requireNonNull(location1.getWorld(), WORLD_SHOULD_NOT_BE_NULL).getName().equals(Objects.requireNonNull(location2.getWorld(), WORLD_SHOULD_NOT_BE_NULL).getName()))
            throw new IllegalArgumentException(REQUIRE_SAME_WORLD);
        return Math.sqrt(NumberConversions.square(location1.getX() - location2.getX()) + NumberConversions.square(location1.getZ() - location2.getZ()));
    }

    /**
     * Gets the distance between two {@link Location}s on all three axis'
     * @param location1 the first location
     * @param location2 the second location
     * @return the distance between {@code location1} and {@code location2}
     * @throws IllegalArgumentException when the two locations are not in the same world
     * @see LocationUtils#get2DDistance(Location, Location)
     * @since 135
     */
    public static double getDistance(Location location1, Location location2) {
        if(!Objects.requireNonNull(location1.getWorld(), WORLD_SHOULD_NOT_BE_NULL).getName().equals(Objects.requireNonNull(location2.getWorld(), WORLD_SHOULD_NOT_BE_NULL).getName()))
            throw new IllegalArgumentException(REQUIRE_SAME_WORLD);
        return location1.distance(location2);
    }

    /**
     * Gets the top-most block that isn't 'empty' (using {@link Block#isEmpty()}) {@literal <}-- this method assumes 'yStartChecking' is the max height of the world
     * @param location the location to check, will start at y=*world height*
     * @return the new location that would be the highest block that isn't {@link Block#isEmpty()}
     * @since 136
     */
    public static Location getTopBlock(@NotNull Location location) {
        return getTopBlock(location, Objects.requireNonNull(location.getWorld(), WORLD_SHOULD_NOT_BE_NULL).getMaxHeight());
    }

    /**
     * Gets the top-most block that isn't 'empty' (using {@link Block#isEmpty()})
     * @param location the location to check, will start at y=*world height*
     * @param yStartChecking where the method should start checking
     * @return the new location that would be the highest block that isn't {@link Block#isEmpty()}
     * @since 136
     */
    public static Location getTopBlock(@NotNull Location location, long yStartChecking) {
        location = location.clone();
        location.setY(yStartChecking);
        for(int y=0; y<yStartChecking; y++) {
            if(Objects.requireNonNull(location.getWorld(), WORLD_SHOULD_NOT_BE_NULL).getBlockAt(location).isEmpty() || !(location.getWorld().getBlockAt(location.clone().add(0, 1, 0)).isEmpty())) {
                location.setY(location.getY() - 1);
            }else if(!location.getWorld().getBlockAt(location).isEmpty()) {
                location = location.add(0, 1, 0);
                return location;
            }
        }
        return null;
    }

    /**
     * Using a {@link Location Bukkit Location}, converts it to a {@link String Base64 String} to be used for easy storage
     * @param location the {@link Location} you wish to encode into its Base64 counterpart
     * @return the encoded {@link Location} as a {@link String} -- this is <b>very</b> useful for storage into databases like Mongo (instead of making a mapped object)
     * @see LocationUtils#decodeBase64(String) decode a stringified location with this method! (click)
     * @since 135
     */
    public static String encodeBase64(Location location) {
        return Base64Convert.LOCATION.convertToBase64(location);
    }

    /**
     * Using a {@link String Base64 String}, converts it to a {@link Location} to be used
     * @see LocationUtils#encodeBase64(Location) encode a location with this method! (click)
     */
    @SuppressWarnings({"all"})
    public static Location decodeBase64(String location) {
        return Base64Convert.LOCATION.convertFromBase64(location);
    }

}
