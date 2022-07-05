package net.cybercake.cyberapi.spigot.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Item {

    /**
     * Checks for any item stacks (actually {@link Material}s) that are similar by name to strings
     * @param strings the strings to check for {@literal <}- MUST ALL BE LOWERCASE
     * @return the {@link List} of {@link ItemStack}s that have similar {@link Material} names
     * @since 3.0.0
     */
    public static List<ItemStack> getSimilarItemStacks(String... strings) {
        List<ItemStack> returned = new ArrayList<>();
        for(Material material : Material.values()) {
            if(!Arrays.asList(strings).contains(material.toString().toLowerCase(Locale.ROOT))) continue;

            returned.add(new ItemStack(material));
        }
        return returned;
    }

    public enum SimilarItem {
        SWORD, SHOVEL, PICKAXE, AXE, HOE, ARMOR;
    }

    /**
     * Get all items that are similar with a certain category
     * @param similarItem a {@link SimilarItem}, what category to check against
     * @return get similar items
     * @since 3.0.0
     */
    public static List<ItemStack> getAll(SimilarItem similarItem) {
        return switch (similarItem) {
            case SWORD, SHOVEL, PICKAXE, HOE -> getSimilarItemStacks(similarItem.name().toLowerCase(Locale.ROOT));
            case AXE -> getSimilarItemStacks("_axe");
            case ARMOR -> getSimilarItemStacks("boots", "leg", "pants", "chestplate", "tunic", "helmet", "cap");
        };
    }

    /**
     * Gets all MC items in namespaced form
     * @return an {@link ArrayList} of MC items
     * @since 3.0.0
     */
    public static ArrayList<String> getMCItemsNamespace() {
        ArrayList<String> mcItems = new ArrayList<>();
        for(Material material : Material.values()) {
            if(material.isItem()) {
                mcItems.add(material.getKey().toString());
            }
        }
        return mcItems;
    }

    /**
     * Compare two item stacks, ignoring the amounts
     * @param item1 the first item to compare
     * @param item2 the second item to compare
     * @return whether the items are the same/similar, disregarding the amount
     * @since 3.0.0
     */
    public static boolean compare(ItemStack item1, ItemStack item2) {
        item1 = item1.clone();
        item2 = item2.clone();

        item1.setAmount(1);
        item2.setAmount(1);

        return item1.equals(item2);
    }

}
