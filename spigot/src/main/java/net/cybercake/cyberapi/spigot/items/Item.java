package net.cybercake.cyberapi.spigot.items;

import net.cybercake.cyberapi.spigot.chat.UChat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class Item {

    /**
     * Checks for any item stacks (actually {@link Material}s) that are similar by name to strings
     * @param strings the strings to check for {@literal <}- MUST ALL BE LOWERCASE
     * @return the {@link List} of {@link ItemStack}s that have similar {@link Material} names
     * @since 1
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
     * @since 1
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
     * @since 1
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
     * @since 1
     */
    public static boolean compare(ItemStack item1, ItemStack item2) {
        item1 = item1.clone();
        item2 = item2.clone();

        item1.setAmount(1);
        item2.setAmount(1);

        return item1.equals(item2);
    }

    /**
     * Represents a text color and decoration formatter
     * @see net.cybercake.cyberapi.spigot.items.Item.ItemTextFormatter#PLAIN
     * @see net.cybercake.cyberapi.spigot.items.Item.ItemTextFormatter#LEGACY
     * @see net.cybercake.cyberapi.spigot.items.Item.ItemTextFormatter#MINIMESSAGE
     */
    public enum ItemTextFormatter {

        /**
         * Represents using plain-text, with no text formatter
         */
        PLAIN,

        /**
         * Represents using legacy color codes as the text formatter
         * @see net.cybercake.cyberapi.spigot.chat.UChat#chat(String) UChat -> Bungee ChatColor
         * @see net.cybercake.cyberapi.spigot.chat.UChat#bComponent(String) UChat -> Base Components
         * @see net.cybercake.cyberapi.spigot.chat.UChat#component(String) UChat -> Adventure Component
         */
        LEGACY,

        /**
         * Represents using MiniMessage as the text formatter
         * @apiNote requires MiniMessage support
         * @see net.cybercake.cyberapi.spigot.chat.UChat#miniMessage(String) UChat -> MiniMessage Components
         */
        MINIMESSAGE

    }

    /**
     * Create a blank item in the item builder
     * @return the {@link ItemBuilder} instance
     * @since 90
     */
    public static ItemBuilder createItem() { return new ItemBuilder(); }

    /**
     * Creates an item in the item builder using an already-defined {@link Material}
     * @param material the {@link Material} to create the {@link ItemBuilder} (and {@link ItemStack}) with
     * @return the {@link ItemBuilder} instance
     * @since 90
     */
    public static ItemBuilder createItem(Material material) { return new ItemBuilder(material); }

    /**
     * Create an item in the item builder using an already-defined {@link ItemStack}
     * @param item the {@link ItemStack} to create the {@link ItemBuilder} with
     * @return the {@link ItemBuilder} instance
     * @since 90
     */
    public static ItemBuilder createItem(ItemStack item) { return new ItemBuilder(item); }

    /**
     * Create an item in the item builder using an already-defined {@link ItemMeta} ... note: will create an {@link ItemStack} and apply the set {@link ItemMeta} to it!
     * @param meta the {@link ItemMeta} to create the {@link ItemBuilder} with
     * @return the {@link ItemBuilder} instance
     * @since 90
     */
    public static ItemBuilder createItem(ItemMeta meta) { return new ItemBuilder(meta); }

    /**
     * Represents an {@link ItemStack} builder
     * <br>
     * This class is an easier way to create {@link ItemStack}s than having to modify the {@link ItemMeta} yourself
     * @see ItemBuilder#ItemBuilder()
     * @see ItemBuilder#ItemBuilder(Material)
     * @see ItemBuilder#ItemBuilder(ItemStack)
     */
    public static class ItemBuilder {
        private final ItemStack item;
        private final ItemMeta meta;

        private ItemTextFormatter itemTextFormatter;

        /**
         * Creates a blank item in the item builder
         * @since 90
         */
        public ItemBuilder() {
            this(Material.AIR);
        }

        /**
         * Creates an item in the item builder using an already-defined {@link Material}
         * @param material the {@link Material} to create the {@link ItemBuilder} (and {@link ItemStack}) with
         * @since 90
         */
        public ItemBuilder(@NotNull Material material) {
            this(new ItemStack(material));
        }

        /**
         * Creates an item in the item builder using an already-defined {@link ItemStack}
         * @param item the {@link ItemStack} to create the {@link ItemBuilder} with
         * @since 90
         */
        public ItemBuilder(@NotNull ItemStack item) {
            this.item = item;
            this.meta = this.item.getItemMeta();

            this.itemTextFormatter = ItemTextFormatter.PLAIN;
        }

        /**
         * Create an item in the item builder using an already-defined {@link ItemMeta} ... note: will create an {@link ItemStack} and apply the set {@link ItemMeta} to it!
         * @param meta the {@link ItemMeta} to create the {@link ItemBuilder} with
         * @since 90
         */
        public ItemBuilder(@NotNull ItemMeta meta) {
            ItemStack item = new ItemStack(Material.AIR);
            item.setItemMeta(meta);

            this.item = item;
            this.meta = this.item.getItemMeta();

            this.itemTextFormatter = ItemTextFormatter.PLAIN;
        }


        /**
         * @param itemTextFormatter how names, lores, etc. will be formatted or decorated
         * @apiNote {@link ItemTextFormatter#MINIMESSAGE} requires Mini Message support
         * @since 90
         */
        public ItemBuilder itemTextFormatter(ItemTextFormatter itemTextFormatter) {
            this.itemTextFormatter = itemTextFormatter; return this;
        }

        /**
         * <b>for internal use only</b> <br> <b>for internal use only</b> <br> <b>for internal use only</b> <br> <b>for internal use only</b>
         * @param string the string to format using the {@link ItemTextFormatter}
         * @return the string after being formatted with the {@link ItemTextFormatter}
         * @since 90
         */
        protected String formatString(String string) {
            return switch(this.itemTextFormatter) {
                case PLAIN -> string;
                case LEGACY -> UChat.chat(string);
                case MINIMESSAGE ->
                        LegacyComponentSerializer.builder().useUnusualXRepeatedCharacterHexFormat().hexColors().build().serialize(UChat.miniMessage(string));
            };
        }



        /**
         * @param name change the name of the item
         * @since 90
         */
        public ItemBuilder name(String name) {
            this.meta.setDisplayName(formatString(name)); return this;
        }

        /**
         * @param name change the name of the item using a {@link net.kyori.adventure.text.Component}
         * @since 90
         * @apiNote requires Adventure API support
         */
        public ItemBuilder name(net.kyori.adventure.text.Component name) {
            this.meta.setDisplayName(formatString(LegacyComponentSerializer.builder().build().serialize(name))); return this;
        }

        /**
         * @param material change the {@link Material} type of the item
         * @since 90
         */
        public ItemBuilder type(Material material) {
            this.item.setType(material); return this;
        }

        /**
         * @param amount change the amount of the item
         * @since 90
         */
        public ItemBuilder amount(int amount) {
            this.item.setAmount(amount); return this;
        }

        /**
         * @param enchantment add level one of an enchant to the item
         * @since 90
         * @see ItemBuilder#addEnchant(Enchantment, int)
         */
        public ItemBuilder addEnchant(Enchantment enchantment) {
            return addEnchant(enchantment, 1);
        }

        /**
         * @param enchantment add this enchantment to the item
         * @param level the level the enchantment is supposed to be (ignores level restrictions)
         * @since 90
         * @see ItemBuilder#addEnchant(Enchantment)
         */
        public ItemBuilder addEnchant(Enchantment enchantment, int level) {
            this.meta.addEnchant(enchantment, level, true); return this;
        }

        /**
         * @param enchantment remove this enchantment from the item
         * @since 90
         */
        public ItemBuilder removeEnchant(Enchantment enchantment) {
            this.meta.removeEnchant(enchantment); return this;
        }

        /**
         * removes all the enchantments from the item
         * @since 90
         */
        public ItemBuilder removeAllEnchants() {
            this.meta.getEnchants().keySet().forEach(this.meta::removeEnchant); return this;
        }

        /**
         * @param metaConsumer allows the {@link ItemMeta} to be modified through a {@link Consumer <ItemMeta>}
         * @since 90
         */
        public ItemBuilder meta(Consumer<ItemMeta> metaConsumer) {
            metaConsumer.accept(this.meta); return this;
        }

        /**
         * @param metaClass the class the {@link ItemMeta} is parameterized in
         * @param metaConsumer the {@link Consumer} for the {@link ItemMeta}
         * @param <T> must extend {@link ItemMeta}
         * @since 90
         */
        public <T extends ItemMeta> ItemBuilder meta(Class<T> metaClass, Consumer<T> metaConsumer) {
            if(metaClass.isInstance(this.meta))
                metaConsumer.accept(metaClass.cast(this.meta));
            return this;
        }

        /**
         * @param lore the lore to add to the item
         * @since 90
         * @see ItemBuilder#lore(String...)
         * @see ItemBuilder#lore(List)
         */
        public ItemBuilder lore(String lore) {
            return lore(Collections.singletonList(lore)); // string is formatted later
        }

        /**
         * @param lore the lore to add to the item, an array of {@link String}s
         * @since 90
         * @see ItemBuilder#lore(String)
         * @see ItemBuilder#lore(List)
         */
        public ItemBuilder lore(String... lore) {
            return lore(Arrays.asList(lore)); // strings are formatted later
        }

        /**
         * @param lore the lore to add to the item, a {@link List} of {@link String}s
         * @since 90
         * @see ItemBuilder#lore(String)
         * @see ItemBuilder#lore(String...)
         */
        public ItemBuilder lore(List<String> lore) {
            this.meta.setLore(lore.stream().map(this::formatString).toList()); return this;
        }

        /**
         * @param line the line to add to the lore
         * @since 90
         */
        public ItemBuilder addLore(String line) {
            return addLore(Collections.singletonList(line)); // string is formatted later
        }

        /**
         * @param lines the lines to add to the lore, an array of {@link String}s
         * @return 90
         */
        public ItemBuilder addLore(String... lines) {
            return addLore(Arrays.asList(lines)); // strings are formatted later
        }

        /**
         * @param lines the lines to add to the lore, a {@link List} of {@link String}s
         * @return 90
         */
        public ItemBuilder addLore(List<String> lines) {
            List<String> lore = this.meta.getLore();

            if(lore == null)
                return lore(lines);

            lore.addAll(lines.stream().map(this::formatString).toList());
            return lore(lore);
        }

        /**
         * @param flags the {@link ItemFlag}s to apply to the item
         * @since 90
         */
        public ItemBuilder addFlags(ItemFlag... flags) {
            this.meta.addItemFlags(flags); return this;
        }

        /**
         * applies all the {@link ItemFlag}s to the item
         * @since 90
         */
        public ItemBuilder addAllFlags() {
            return addFlags(ItemFlag.values());
        }

        /**
         * @param flags the {@link ItemFlag}s to remove from the item
         * @since 90
         */
        public ItemBuilder removeFlags(ItemFlag... flags) {
            this.meta.removeItemFlags(flags); return this;
        }

        /**
         * removes all {@link ItemFlag}s applied to the item
         * @since 90
         */
        public ItemBuilder removeAllFlags() {
            return removeFlags(ItemFlag.values());
        }

        /**
         * @param color changes the color of the armor (only if {@link ItemMeta} can be cast to {@link LeatherArmorMeta}, otherwise it will just do nothing)
         * @since 90
         */
        public ItemBuilder armorColor(Color color) {
            return meta(LeatherArmorMeta.class, m -> m.setColor(color));
        }

        /**
         * @param durability change the durability (or damage) of the item (only if {@link ItemMeta} can be cast to {@link Damageable}, otherwise it will just do nothing)
         * @since 90
         */
        public ItemBuilder durability(int durability) {
            return meta(Damageable.class, m -> m.setDamage(durability));
        }


        /**
         * @return the cloned {@link ItemStack} instance of the item along with the {@link ItemMeta} applied
         * @since 90
         */
        public ItemStack build() {
            this.item.setItemMeta(this.meta);
            return this.item;
        }

        /**
         * @return the cloned {@link ItemMeta} instance of the item being created so far
         * @since 90
         * @throws NullPointerException thrown by potentially cloning the {@link ItemMeta}
         * @deprecated it's generally better to use {@link net.cybercake.cyberapi.spigot.items.Item Item} and calling {@link ItemStack#getItemMeta()} instead of using this as this builds the instance and then get's the item meta!
         */
        @SuppressWarnings({"all"}) @Deprecated public @Nullable ItemMeta getItemMeta() { return this.build().getItemMeta().clone(); }
    }

}
