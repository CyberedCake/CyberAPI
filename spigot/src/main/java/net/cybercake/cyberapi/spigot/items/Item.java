package net.cybercake.cyberapi.spigot.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This class <b>will be removed</b> in the near future. Please
 * switch to {@link ItemCreator} as soon as possible!
 * @deprecated use {@link ItemCreator} instead, this name is entirely too generic to execute functions correctly
 */
@Deprecated
public class Item extends ItemCreator {

    /**
     * @deprecated use {@link ItemCreator.SimilarItem} instead!
     */
    @Deprecated
    public enum SimilarItem {
        /**
         * @deprecated use {@link ItemCreator.SimilarItem} instead!
         */
        SWORD,
        /**
         * @deprecated use {@link ItemCreator.SimilarItem} instead!
         */
        SHOVEL,
        /**
         * @deprecated use {@link ItemCreator.SimilarItem} instead!
         */
        PICKAXE,
        /**
         * @deprecated use {@link ItemCreator.SimilarItem} instead!
         */
        AXE,
        /**
         * @deprecated use {@link ItemCreator.SimilarItem} instead!
         */
        HOE,
        /**
         * @deprecated use {@link ItemCreator.SimilarItem} instead!
         */
        ARMOR;
    }

    /**
     * Represents a text color and decoration formatter
     * @see net.cybercake.cyberapi.spigot.items.Item.ItemTextFormatter#PLAIN
     * @see net.cybercake.cyberapi.spigot.items.Item.ItemTextFormatter#LEGACY
     * @see net.cybercake.cyberapi.spigot.items.Item.ItemTextFormatter#MINIMESSAGE
     * @deprecated use {@link ItemCreator.ItemTextFormatter} instead!
     */
    @Deprecated
    public enum ItemTextFormatter {

        /**
         * Represents using plain-text, with no text formatter
         * @deprecated use {@link ItemCreator.ItemTextFormatter#PLAIN}
         */
        @Deprecated
        PLAIN,

        /**
         * Represents using legacy color codes as the text formatter
         * @see net.cybercake.cyberapi.spigot.chat.UChat#chat(String) UChat -> Bungee ChatColor
         * @see net.cybercake.cyberapi.spigot.chat.UChat#bComponent(String) UChat -> Base Components
         * @see net.cybercake.cyberapi.spigot.chat.UChat#component(String) UChat -> Adventure Component
         * @deprecated use {@link ItemCreator.ItemTextFormatter#LEGACY}
         */
        @Deprecated
        LEGACY,

        /**
         * Represents using MiniMessage as the text formatter
         * @apiNote requires MiniMessage support
         * @see net.cybercake.cyberapi.spigot.chat.UChat#miniMessage(String) UChat -> MiniMessage Components
         * @deprecated use {@link ItemCreator.ItemTextFormatter#MINIMESSAGE}
         */
        @Deprecated
        MINIMESSAGE

    }

    /**
     * Represents an {@link ItemStack} builder
     * <br>
     * This class is an easier way to create {@link ItemStack}s than having to modify the {@link ItemMeta} yourself
     * @see ItemCreator.ItemBuilder#ItemBuilder()
     * @see ItemCreator.ItemBuilder#ItemBuilder(Material)
     * @see ItemCreator.ItemBuilder#ItemBuilder(ItemStack)
     * @deprecated use {@link ItemCreator.ItemBuilder} instead!
     */
    @Deprecated
    public static class ItemBuilder extends ItemCreator.ItemBuilder {

    }

}