package net.cybercake.cyberapi.spigot.items;

import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.basic.BetterStackTraces;
import net.cybercake.cyberapi.spigot.chat.UChat;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents basic GUI functions
 */
public class GUI {

    /**
     * Fills a GUI with a certain material
     * @param inventory the inventory to fill
     * @param material the material to use in the fill
     * @param amount the amount of each item in the fill
     * @param shiny whether the item is shiny or not (has enchantment glint)
     * @param name the name of the item in the fill
     * @param lore the lore of the item in the fill
     * @see GUI#item(Material, int, boolean, boolean, String, String...)
     * @since 1
     */
    public static void fillGUI(Inventory inventory, Material material, int amount, boolean shiny, String name, String... lore) {
        ItemStack items = item(material, amount, shiny, name, lore);
        for(int i=0; i<inventory.getSize(); i++) {
            inventory.setItem(i, items);
        }
    }

    /**
     * Creates a basic {@link ItemStack} with certain settings
     * @param material the material to use
     * @param amount the amount of the item
     * @param hideNbt whether to hide the NBT (enchantments, potion effects, etc.)
     * @param shiny whether the item should be shiny (enchantment glint)
     * @param name the name of the item
     * @param lore the lore of the item
     * @return the new {@link ItemStack} with all of your settings
     * @since 1
     * @deprecated please use {@link Item#createItem()} instead!
     */
    @Deprecated(forRemoval = true)
    public static ItemStack item(Material material, int amount, boolean hideNbt, boolean shiny, String name, String... lore) {
        try {
            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(UChat.chat(name));
            List<String> blankLore = new ArrayList<>();
            for(String loreActual : lore) {
                if(!loreActual.startsWith("paginate:")) {
                    blankLore.add(UChat.chat(loreActual));
                }else{
                    String color = loreActual.substring(loreActual.length()-2);
                    loreActual = loreActual.substring(9, loreActual.length()-3);
                    for(String str : ChatPaginator.wordWrap(UChat.chat(color + loreActual), 40)) {
                        blankLore.add(UChat.chat(str));
                    }
                }
            }
            if(shiny) {
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(hideNbt) {
                meta.addItemFlags(ItemFlag.values());
            }
            meta.setLore(blankLore);
            item.setItemMeta(meta);
            return item;
        } catch (Exception exception) {
            CyberAPI.getInstance().getAPILogger().error("An exception occurred whilst creating a basic ItemStack");
            BetterStackTraces.print(exception);
        }
        return null;
    }

    /**
     * Creates a basic {@link ItemStack} with certain settings
     * @param material the material to use
     * @param amount the amount of the item
     * @param shiny whether the item should be shiny (enchantment glint)
     * @param name the name of the item
     * @param lore the lore of the item
     * @return the new {@link ItemStack} with all of your settings
     * @since 1
     * @see GUI#item(Material, int, boolean, String, String...)
     * @deprecated please use {@link Item#createItem()} instead!
     */
    @Deprecated(forRemoval = true)
    public static ItemStack item(Material material, int amount, boolean shiny, String name, ArrayList<String> lore) {
        return item(material, amount, false, shiny, name, lore.toArray(new String[0]));
    }

    /**
     * Creates a basic {@link ItemStack} with certain settings
     * @param material the material to use
     * @param amount the amount of the item
     * @param name the name of the item
     * @param lore the lore of the item
     * @return the new {@link ItemStack} with all of your settings
     * @since 1
     * @see GUI#item(Material, int, boolean, String, String...)
     * @deprecated please use {@link Item#createItem()} instead!
     */
    @Deprecated(forRemoval = true)
    public static ItemStack item(Material material, int amount, String name, ArrayList<String> lore) {
        return item(material, amount, false, false, name, lore.toArray(new String[0]));
    }

    /**
     * Creates a basic {@link ItemStack} with certain settings
     * @param material the material to use
     * @param amount the amount of the item
     * @param name the name of the item
     * @param lore the lore of the item
     * @return the new {@link ItemStack} with all of your settings
     * @since 1
     * @see GUI#item(Material, int, boolean, String, String...)
     * @deprecated please use {@link Item#createItem()} instead!
     */
    @Deprecated(forRemoval = true)
    public static ItemStack item(Material material, int amount, String name, String... lore) {
        return item(material, amount, false, false, name, lore);
    }

    /**
     * Creates a basic {@link ItemStack} with certain settings
     * @param material the material to use
     * @param amount the amount of the item
     * @param hideNbt whether to hide the NBT (enchantments, potion effects, etc.)
     * @param name the name of the item
     * @param lore the lore of the item
     * @return the new {@link ItemStack} with all of your settings
     * @since 1
     * @see GUI#item(Material, int, boolean, String, String...)
     * @deprecated please use {@link Item#createItem()} instead!
     */
    @Deprecated(forRemoval = true)
    public static ItemStack item(Material material, int amount, boolean hideNbt, String name, String... lore) {
        return item(material, amount, hideNbt, false, name, lore);
    }

    /**
     * Creates a basic {@link ItemStack} with certain settings
     * @param material the material to use
     * @param amount the amount of the item
     * @param hideNbt whether to hide the NBT (enchantments, potion effects, etc.)
     * @param name the name of the item
     * @return the new {@link ItemStack} with all of your settings
     * @since 1
     * @see GUI#item(Material, int, boolean, String, String...)
     * @deprecated please use {@link Item#createItem()} instead!
     */
    @Deprecated(forRemoval = true)
    public static ItemStack item(Material material, int amount, boolean hideNbt, String name) {
        return item(material, amount, hideNbt, false, name);
    }

    /**
     * Creates a basic {@link ItemStack} with certain settings
     * @param material the material to use
     * @param amount the amount of the item
     * @param name the name of the item
     * @return the new {@link ItemStack} with all of your settings
     * @since 1
     * @see GUI#item(Material, int, boolean, String, String...)
     * @deprecated please use {@link Item#createItem()} instead!
     */
    @Deprecated(forRemoval = true)
    public static ItemStack item(Material material, int amount, String name) {
        try {
            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(UChat.chat(name));
            item.setItemMeta(meta);
            return item;
        } catch (Exception exception) {
            CyberAPI.getInstance().getAPILogger().error("An exception occurred whilst creating a basic ItemStack");
            BetterStackTraces.print(exception);
        }
        return null;
    }

}
