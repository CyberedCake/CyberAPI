package net.cybercake.cyberapi.spigot.basic.base64;

import net.cybercake.cyberapi.common.basic.converters.GenericBase64Converter;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * This class contains a list of all available converters for Bukkit
 * @param <I> the input type
 * @param <B> the Base64 type, most commonly a {@link String}
 * @since 144
 * @author CyberedCake
 */
public interface Base64Convert<I, B> extends GenericBase64Converter<I, B> {

    /**
     * Base64 {@literal <}-> {@link Inventory org.bukkit.inventory.Inventory}
     * @since 144
     */
    Base64Convert<Inventory, String> BUKKIT_INVENTORY = new Converters.InventoryConverter();

    /**
     * Base64 {@literal <}-> {@link ItemStack org.bukkit.inventory.ItemStack}
     * @since 144
     */
    Base64Convert<ItemStack, String> ITEM_STACK = new Converters.ItemStackConverter();

    /**
     * Base64 {@literal <}-> {@link Location org.bukkit.Location}
     * @since 147
     */
    Base64Convert<Location, String> LOCATION = new Converters.LocationConverter();

}
