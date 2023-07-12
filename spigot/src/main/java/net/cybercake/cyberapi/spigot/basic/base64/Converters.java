package net.cybercake.cyberapi.spigot.basic.base64;

import net.cybercake.cyberapi.common.basic.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * All the converters implementations for CyberAPI spigot
 * @since 144
 * @see ItemStackConverter
 * @see InventoryConverter
 */
public class Converters {

    /**
     * A converter that converts {@link Location locations} to Base64 and vice versa
     * @author CyberedCake
     * @since 147
     */
    public static class LocationConverter implements Base64Convert<Location, String> {
        @Override public Pair<Class<Location>, Class<String>> getTypes() { return Pair.of(Location.class, String.class); }

        @Override
        public String convertToBase64(Location location) throws IllegalStateException {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(location);

                dataOutput.close();
                return Base64.getEncoder().encodeToString(outputStream.toByteArray());
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to convert '" + getTypes().getFirstItem().getCanonicalName() + "' to Base64", exception);
            }
        }

        @Override
        public Location convertFromBase64(String data) throws IllegalStateException {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                Location read = (Location) dataInput.readObject();

                dataInput.close();
                return read;
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to convert Base64 to '" + getTypes().getFirstItem().getCanonicalName() + "'", exception);
            }
        }
    }

    /**
     * A converter that converts {@link ItemStack items} to Base64 and vice versa
     * @author CyberedCake
     * @since 144
     */
    public static class ItemStackConverter implements Base64Convert<ItemStack, String> {
        @Override public Pair<Class<ItemStack>, Class<String>> getTypes() { return Pair.of(ItemStack.class, String.class); }

        @Override
        public String convertToBase64(ItemStack itemStack) throws IllegalStateException {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(itemStack);

                dataOutput.close();
                return Base64.getEncoder().encodeToString(outputStream.toByteArray());
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to convert '" + getTypes().getFirstItem().getCanonicalName() + "' to Base64", exception);
            }
        }

        @Override
        public ItemStack convertFromBase64(String data) throws IllegalStateException {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                ItemStack read = (ItemStack) dataInput.readObject();

                dataInput.close();
                return read;
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to convert Base64 to '" + getTypes().getFirstItem().getCanonicalName() + "'", exception);
            }
        }
    }

    /**
     * A converter that converts {@link Inventory inventories} to Base64 and vice versa
     * @author <a href="https://gist.github.com/graywolf336/8153678">graywolf336 on GitHub</a>
     * @apiNote thank you, Luke and Pruper, for bringing this to my attention and getting me to write
     *          an entire Converter into CyberAPI (see: {@link net.cybercake.cyberapi.common.basic.converters.GenericConverter GenericConverter}
     *          and {@link net.cybercake.cyberapi.common.basic.converters.GenericBase64Converter GenericBase64Converter})
     * @since 144
     */
    public static class InventoryConverter implements Base64Convert<Inventory, String> {
        @Override public Pair<Class<Inventory>, Class<String>> getTypes() { return Pair.of(Inventory.class, String.class); }

        @Override
        public String convertToBase64(Inventory inventory) throws IllegalStateException {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(inventory.getHolder());

                // Write the size of the inventory + 4 additional slots
                dataOutput.writeInt(inventory.getSize() + 4);

                // Save every element in the list
                for (int i = 0; i < inventory.getSize(); i++) {
                    dataOutput.writeObject(inventory.getItem(i));
                }

                // Write additional 4 null elements for the added slots
                for (int i = 0; i < 4; i++) {
                    dataOutput.writeObject(null);
                }

                // Serialize that array
                dataOutput.close();
                return Base64.getEncoder().encodeToString(outputStream.toByteArray());
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to convert '" + getTypes().getFirstItem().getCanonicalName() + "' to Base64", exception);
            }
        }

        @Override
        public Inventory convertFromBase64(String data) throws IllegalStateException {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                InventoryHolder holder = (InventoryHolder) dataInput.readObject();
                int size = dataInput.readInt();
                Inventory inventory = Bukkit.getServer().createInventory(holder, size);

                // Read the serialized inventory
                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, (ItemStack) dataInput.readObject());
                }

                dataInput.close();
                return inventory;
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to convert Base64 to '" + getTypes().getFirstItem().getCanonicalName() + "'", exception);
            }
        }
    }

}
