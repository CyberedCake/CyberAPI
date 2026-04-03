package net.cybercake.cyberapi.spigot.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class ItemCreator {

    /**
     * @param material the {@link Material} to convert to {@link ItemStack}
     * @return the new, essentially blank other than from its {@link Material}, {@link ItemStack}
     * @since 143
     */
    public static ItemStack getItemFrom(Material material) {
        return ItemCreator.createItem(material).build();
    }

    /**
     * Checks for any item stacks (actually {@link Material}s) that are similar by name to strings
     * @param strings the strings to check for {@literal <}- MUST ALL BE LOWERCASE
     * @return the {@link List} of {@link ItemStack}s that have similar {@link Material} names
     * @since 1
     */
    public static List<ItemStack> getSimilarItemStacks(String... strings) {
        List<ItemStack> returned = new ArrayList<>();
        for(Material material : Material.values()) {
            if (!Arrays.asList(strings).contains(material.toString().toLowerCase(Locale.ROOT))) continue;

            returned.add(new ItemStack(material));
        }
        return returned;
    }

    public enum SimilarItem {
        SWORD, SHOVEL, PICKAXE, AXE, HOE, ARMOR
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
     * @return an {@link List} of MC items
     * @since 1
     */
    public static List<String> getMCItemsNamespace() {
        return Arrays.stream(Material.values())
                .filter(Material::isItem)
                .map(material -> material.getKey().toString())
                .toList();
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
     * Creates a random uuid {@link NamespacedKey} under the 'minecraft' namespace.
     *
     * @since 188
     */
    public static @NotNull NamespacedKey randomKey() {
        return NamespacedKey.minecraft(UUID.randomUUID().toString());
    }

    /**
     * <p>Adds a value under a {@link NamespacedKey} to an {@link ItemStack}'s {@link PersistentDataContainer}.
     * <p> <i> <small>
     * This functions exactly like old custom NBT tags; you can add, get, and remove your {@link NamespacedKey}'s values.
     *
     * @param item  The {@link ItemStack} to add the data to.
     * @param key   The {@link NamespacedKey} of the custom data.
     * @param type  The {@link PersistentDataType} of the custom data.
     * @param value The value of the custom data.
     * @param <Z>   The object type of the value.
     * @since 188
     */
    @SuppressWarnings("DataFlowIssue")
    public static <T, Z> void addCustomData(final @NotNull ItemStack item, final @NotNull NamespacedKey key,
                                            final @NotNull PersistentDataType<T, Z> type, final @NotNull Z value) {

        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, type, value);
        item.setItemMeta(meta);
    }

    /**
     * <p>Gets the value to a {@link NamespacedKey} from an {@link ItemStack}'s {@link PersistentDataContainer}.
     * <p> <i> <small>
     * This functions exactly like old custom NBT tags; you can add, get, and remove your {@link NamespacedKey}'s values.
     *
     * @param item  The {@link ItemStack} to get the data from.
     * @param key   The {@link NamespacedKey} of the custom data.
     * @param type  The {@link PersistentDataType} of the custom data.
     * @param <Z>   The object type of the value.
     * @since 188
     */
    public static <T, Z> @Nullable Z getCustomData(final @NotNull ItemStack item, final @NotNull NamespacedKey key,
                                         final @NotNull PersistentDataType<T, Z> type) {

        ItemMeta meta = item.getItemMeta();

        if (meta == null)
            return null;

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        return dataContainer.has(key, type) ? dataContainer.get(key, type) : null;
    }

    /**
     * <p>Removes a {@link NamespacedKey} from an {@link ItemStack}'s {@link PersistentDataContainer}.
     * <p> <i> <small>
     * This functions exactly like old custom NBT tags; you can add, get, and remove your {@link NamespacedKey}'s values.
     *
     * @param item  The {@link ItemStack} to remove the data from.
     * @param key   The {@link NamespacedKey} of the custom data.
     * @since 188
     */
    @SuppressWarnings("DataFlowIssue")
    public static void removeCustomData(final @NotNull ItemStack item, final @NotNull NamespacedKey key) {

        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);
    }

    /**
     * Represents a text color and decoration formatter
     * @see net.cybercake.cyberapi.spigot.items.ItemCreator.ItemTextFormatter#PLAIN
     * @see net.cybercake.cyberapi.spigot.items.ItemCreator.ItemTextFormatter#LEGACY
     * @see net.cybercake.cyberapi.spigot.items.ItemCreator.ItemTextFormatter#MINIMESSAGE
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
     * @return the {@link ItemBuilder#ItemBuilder() ItemBuilder} instance
     * @since 90
     * @deprecated please do not try to create a blank item instance, see the section below for more options
     * @see ItemCreator#createItem(Material)
     * @see ItemCreator#createItem(ItemStack)
     * @see ItemCreator#createItem(ItemMeta)
     */
    @Deprecated(since = "132")
    public static ItemBuilder createItem() { return new ItemBuilder(); }

    /**
     * Creates an item in the item builder using an already-defined {@link Material}
     * @param material the {@link Material} to create the {@link ItemBuilder} (and {@link ItemStack}) with
     * @return the {@link ItemBuilder#ItemBuilder(Material) ItemBuilder} instance
     * @since 90
     */
    public static ItemBuilder createItem(Material material) { return new ItemBuilder(material); }

    /**
     * Create an item in the item builder using an already-defined {@link ItemStack}
     * @param item the {@link ItemStack} to create the {@link ItemBuilder} with
     * @return the {@link ItemBuilder#ItemBuilder(ItemStack) ItemBuilder} instance
     * @since 90
     */
    public static ItemBuilder createItem(ItemStack item) { return new ItemBuilder(item); }

    /**
     * Create an item in the item builder using an already-defined {@link ItemMeta} ... note: will create a {@link Material#STONE stone} {@link ItemStack item} and apply the set {@link ItemMeta} to it!
     * @param meta the {@link ItemMeta} to create the {@link ItemBuilder} with
     * @return the {@link ItemBuilder#ItemBuilder(ItemMeta) ItemBuilder} instance
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
        private final @NotNull ItemStack item;
        private final @NotNull ItemMeta meta;

        private ItemTextFormatter itemTextFormatter;

        private boolean hideEnchantsIsFromSystem = true;

        /**
         * Creates a blank item in the item builder
         * @since 90
         * @deprecated please do not try to create a blank item instance, see the section below for more options
         * @see ItemBuilder#ItemBuilder(Material)
         * @see ItemBuilder#ItemBuilder(ItemStack)
         * @see ItemBuilder#ItemBuilder(ItemMeta)
         */
        @Deprecated(since = "132")
        public ItemBuilder() {
            this(Material.STONE);
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
        @SuppressWarnings("DataFlowIssue")
        public ItemBuilder(@NotNull ItemStack item) {
            this.item = item;
            this.meta = this.item.getItemMeta();

            this.itemTextFormatter = ItemTextFormatter.LEGACY;
        }

        /**
         * Create an item in the item builder using an already-defined {@link ItemMeta} ... note: will create a {@link Material#STONE stone} {@link ItemStack item} and apply the set {@link ItemMeta} to it!
         * @param meta the {@link ItemMeta} to create the {@link ItemBuilder} with
         * @since 90
         */
        @SuppressWarnings("DataFlowIssue")
        public ItemBuilder(@NotNull ItemMeta meta) {
            ItemStack item = new ItemStack(Material.STONE);
            item.setItemMeta(meta);

            this.item = item;
            this.meta = this.item.getItemMeta();

            this.itemTextFormatter = ItemTextFormatter.LEGACY;
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
         * Overload of {@link #name(String)}
         * <p> <i>
         * Automatically wraps the name in {@link UChat#chat}
         *
         * @param name change the name of the item
         * @since 188
         */
        public @NotNull ItemBuilder nameFormatted(@NotNull final String name) {
            return name(UChat.chat(name));
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
         * Sets the attributes of the item to the default attributes of its {@link Material}.
         *
         * @since 188
         */
        public final @NotNull ItemBuilder setAttributesToDefault() {
            return setAttributes(getDefaultAttributeModifiers(item.getType()));
        }

        /**
         * Sets the attributes of the item.
         *
         * @since 188
         */
        public final @NotNull ItemBuilder setAttributes(
                @NotNull final Multimap<@NotNull Attribute, @NotNull AttributeModifier> attributes) {

            meta.setAttributeModifiers(attributes);
            return this;
        }

        // Dumb work around because spigot doesn't have #getDefaultAttributeModifiers() like paper does
        // and instead has #getDefaultAttributeModifiers(EquipmentSlot)
        private static @NotNull Multimap<@NotNull Attribute, @NotNull AttributeModifier> getDefaultAttributeModifiers(
                @NotNull final Material material) {

            Multimap<Attribute, AttributeModifier> defaultAttributeModifiers = HashMultimap.create();

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                defaultAttributeModifiers.putAll(material.getDefaultAttributeModifiers(slot));
            }

            return defaultAttributeModifiers;
        }

        /**
         * <p> Overload for {@link #addAttribute(Attribute, AttributeModifier)}
         * <p> <i>
         * <small> Automatically sets the {@link AttributeModifier.Operation} to {@link AttributeModifier.Operation#ADD_NUMBER}.
         *
         * @param attribute The {@link Attribute} for the modifier to affect.
         * @param key       The {@link NamespacedKey} identifier for the modifier.
         * @param amount    The value of the attribute.
         * @param slotGroup The {@link EquipmentSlotGroup} that the item needs to be in for the attribute to take effect.
         * @since 188
         */
        @SuppressWarnings("UnstableApiUsage")
        public @NotNull final ItemBuilder addAttribute(@NotNull final Attribute attribute,
                                                    @NotNull final NamespacedKey key,
                                                    final float amount,
                                                    @NotNull final EquipmentSlotGroup slotGroup) {

            return addAttribute(attribute, new AttributeModifier(key, amount, AttributeModifier.Operation.ADD_NUMBER, slotGroup));
        }

        /**
         * Overload for {@link #addAttribute(Attribute, NamespacedKey, float, AttributeModifier.Operation, EquipmentSlotGroup)}
         * <p> <i>
         * <small> Automatically sets the {@link AttributeModifier.Operation} to {@link AttributeModifier.Operation#ADD_NUMBER}.
         *
         * @param attribute The {@link Attribute} for the modifier to affect.
         * @param key       The {@link NamespacedKey} identifier for the modifier.
         * @param amount    The value of the attribute.
         * @param slot      The {@link EquipmentSlot} that the item needs to be in for the attribute to take effect.
         * @since 188
         */
        @SuppressWarnings("UnstableApiUsage")
        public @NotNull final ItemBuilder addAttribute(@NotNull final Attribute attribute,
                                                    @NotNull final NamespacedKey key,
                                                    final float amount,
                                                    @NotNull final EquipmentSlot slot) {

            return addAttribute(attribute, key, amount, AttributeModifier.Operation.ADD_NUMBER, slot.getGroup());
        }

        /**
         * Overload for {@link #addAttribute(Attribute, NamespacedKey, float, AttributeModifier.Operation)}
         * <p> <i>
         * <small> Automatically sets the {@link AttributeModifier.Operation} to {@link AttributeModifier.Operation#ADD_NUMBER}.
         *
         * @param attribute The {@link Attribute} for the modifier to affect.
         * @param key       The {@link NamespacedKey} identifier for the modifier.
         * @param amount    The value of the attribute.
         * @since 188
         */
        public @NotNull final ItemBuilder addAttribute(@NotNull final Attribute attribute,
                                                    @NotNull final NamespacedKey key,
                                                    final float amount) {

            return addAttribute(attribute, key, amount, AttributeModifier.Operation.ADD_NUMBER, item.getType().getEquipmentSlot());
        }

        /**
         * Overload for {@link #addAttribute(Attribute, NamespacedKey, float, AttributeModifier.Operation, EquipmentSlot)}
         * <p> <i>
         * <small> Automatically sets the {@link EquipmentSlot} to the result of {@link Material#getEquipmentSlot()}.
         *
         * @param attribute The {@link Attribute} for the modifier to affect.
         * @param key       The {@link NamespacedKey} identifier for the modifier.
         * @param amount    The value of the attribute.
         * @param operation The {@link org.bukkit.attribute.AttributeModifier.Operation} to use when applying the attribute.
         * @since 188
         */
        public @NotNull final ItemBuilder addAttribute(@NotNull final Attribute attribute,
                                                    @NotNull final NamespacedKey key,
                                                    final float amount,
                                                    @NotNull final AttributeModifier.Operation operation) {

            return addAttribute(attribute, key, amount, operation, item.getType().getEquipmentSlot());
        }

        /**
         * Overload for {@link #addAttribute(Attribute, NamespacedKey, float, AttributeModifier.Operation, EquipmentSlotGroup)}
         *
         * @param attribute The {@link Attribute} for the modifier to affect.
         * @param key       The {@link NamespacedKey} identifier for the modifier.
         * @param amount    The value of the attribute.
         * @param operation The {@link org.bukkit.attribute.AttributeModifier.Operation} to use when applying the attribute.
         * @param slot      The {@link EquipmentSlot} that the item needs to be in for the attribute to take effect.
         * @since 188
         */
        @SuppressWarnings("UnstableApiUsage")
        public @NotNull final ItemBuilder addAttribute(@NotNull final Attribute attribute,
                                                    @NotNull final NamespacedKey key,
                                                    final float amount,
                                                    @NotNull final AttributeModifier.Operation operation,
                                                    @NotNull final EquipmentSlot slot) {

            return addAttribute(attribute, key, amount, operation, slot.getGroup());
        }

        /**
         * <p> Overload for {@link #addAttribute(Attribute, AttributeModifier)}
         * <p>
         * <i>If two modifiers have the same {@link NamespacedKey} and affect the same attribute,
         * then they do not stack; instead, only the one most recently added takes effect,
         * overriding previous modifiers.
         * <p>
         * {@link #randomKey()} can be used to create a random minecraft-namespaced uuid key.
         * <p>
         * {@link AttributeData#getAttributeData(Attribute)} can be used to get information on the attribute; like the minimum, maximum, and default values.
         * </i>
         *
         * @param attribute The {@link Attribute} for the modifier to affect.
         * @param key       The {@link NamespacedKey} identifier for the modifier.
         * @param amount    The value of the attribute.
         * @param operation The {@link org.bukkit.attribute.AttributeModifier.Operation} to use when applying the attribute.
         * @param slotGroup The {@link EquipmentSlotGroup} that the item needs to be in for the attribute to take effect.
         * @since 188
         */
        @SuppressWarnings("UnstableApiUsage")
        public @NotNull final ItemBuilder addAttribute(@NotNull final Attribute attribute,
                                                    @NotNull final NamespacedKey key,
                                                    final float amount,
                                                    @NotNull final AttributeModifier.Operation operation,
                                                    @NotNull final EquipmentSlotGroup slotGroup) {

            return addAttribute(attribute, new AttributeModifier(key, amount, operation, slotGroup));
        }

        /**
         * <p>Adds an attribute modifier to the item.
         * <p>
         * <i>If you're not using the same modifier for different items or attributes it's recommended to use:<p>
         * {@link #addAttribute(Attribute, NamespacedKey, float, AttributeModifier.Operation, EquipmentSlotGroup)}</p>
         * <p>{@link #addAttribute(Attribute, NamespacedKey, float, AttributeModifier.Operation, EquipmentSlot)}</i></p>
         *
         * @param attribute The {@link Attribute} for the modifier to affect.
         * @param modifier  The {@link AttributeModifier}.
         * @since 188
         */
        public @NotNull final ItemBuilder addAttribute(@NotNull final Attribute attribute,
                                                    @NotNull final AttributeModifier modifier) {

            meta.addAttributeModifier(attribute, modifier);
            return this;
        }

        /**
         * Overload for {@link #addCustomData(NamespacedKey, PersistentDataType, Object)}
         *
         * @param key   The {@link NamespacedKey} of the custom data.
         * @param value The value of the custom data.
         * @since 188
         */
        public final @NotNull ItemBuilder addCustomDataString(@NotNull final NamespacedKey key, @NotNull final String value) {
            return addCustomData(key, PersistentDataType.STRING, value);
        }

        /**
         * Overload for {@link #addCustomData(NamespacedKey, PersistentDataType, Object)}
         *
         * @param key   The {@link NamespacedKey} of the custom data.
         * @param value The value of the custom data.
         * @since 188
         */
        public final @NotNull ItemBuilder addCustomDataBoolean(@NotNull final NamespacedKey key, final boolean value) {
            return addCustomData(key, PersistentDataType.BOOLEAN, value);
        }

        /**
         * Overload for {@link #addCustomData(NamespacedKey, PersistentDataType, Object)}
         *
         * @param key   The {@link NamespacedKey} of the custom data.
         * @param value The value of the custom data.
         * @since 188
         */
        public final @NotNull ItemBuilder addCustomDataFloat(@NotNull final NamespacedKey key, final float value) {
            return addCustomData(key, PersistentDataType.FLOAT, value);
        }

        /**
         * <p>Adds a value under a {@link NamespacedKey} to this item's {@link PersistentDataContainer}.
         * <p> <i> <small>
         * This functions exactly like old custom NBT tags; you can add, get, and remove your {@link NamespacedKey}'s values.
         *
         * @param key   The {@link NamespacedKey} of the custom data.
         * @param type  The {@link PersistentDataType} of the custom data.
         * @param value The value of the custom data.
         * @param <Z>   The object type of the value.
         * @since 188
         */
        public final @NotNull <T, Z> ItemBuilder addCustomData(@NotNull final NamespacedKey key,
                                                               @NotNull final PersistentDataType<T, Z> type,
                                                               @NotNull final Z value) {

            meta.getPersistentDataContainer().set(key, type, value);
            return this;
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
            if (metaClass.isInstance(this.meta))
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
         * Overload of {@link #addFormattedLore(Collection)}
         * 
         * @param line The {@link String} to add to the lore.
         * @since 188
         */
        public final ItemBuilder addFormattedLore(final @NotNull String line) {
            return addFormattedLore(Collections.singletonList(line));
        }

        /**
         * Overload of {@link #addFormattedLore(Collection)}
         *
         * @param lines The {@link String}[] to add to the lore.
         * @since 188
         */
        public final ItemBuilder addFormattedLore(final @NotNull String... lines) {
            return addFormattedLore(Arrays.asList(lines));
        }

        /**
         * Overload of {@link #addLore(List)}
         * <p> <i>
         * Automatically wraps the lore in {@link UChat#chat}
         *
         * @param lines The {@link Collection} of {@link String}s to add to the lore.
         * @since 188
         */
        public final ItemBuilder addFormattedLore(final @NotNull Collection<@NotNull String> lines) {
            List<@NotNull String> ret = new ArrayList<>(lines.size());
            lines.forEach(l -> ret.add(UChat.chat(l)));
            return addLore(ret);
        }

        /**
         * @param line the line to add to the lore
         * @since 90
         */
        public ItemBuilder addLore(String line) {
            if (line == null) return this;
            return addLore(Collections.singletonList(line)); // string is formatted later
        }

        /**
         * @param lines the lines to add to the lore, an array of {@link String}s
         * @return 90
         */
        public ItemBuilder addLore(String... lines) {
            if (lines == null) return this;
            return addLore(Arrays.asList(lines)); // strings are formatted later
        }

        /**
         * @param lines the lines to add to the lore, a {@link List} of {@link String}s
         * @return 90
         */
        public ItemBuilder addLore(List<String> lines) {
            if (lines == null) return this;

            List<String> lore = this.meta.getLore();

            if (lore == null)
                return lore(lines);

            lore.addAll(lines.stream().map(this::formatString).toList());
            return lore(lore);
        }

        /**
         * @param flags the {@link ItemFlag}s to apply to the item
         * @since 90
         */
        public ItemBuilder addFlags(ItemFlag... flags) {
            this.meta.addItemFlags(flags); this.hideEnchantsIsFromSystem = false; return this;
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
            this.meta.removeItemFlags(flags); this.hideEnchantsIsFromSystem = false; return this;
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
         * @param glow whether the item will look like it's enchanted or not
         * @since 141
         */
        public ItemBuilder showEnchantGlow(boolean glow) {
            // using luck because for some god-damn reason my old glow class broke CyberAPI, maybe I'll try to fix it at a later date
            return meta(meta -> {
                if (glow) {
                    meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
                    addFlags(ItemFlag.HIDE_ENCHANTS);
                }
                else {
                    meta.removeEnchant(Enchantment.LUCK_OF_THE_SEA);
                    if (hideEnchantsIsFromSystem)
                        addFlags(ItemFlag.HIDE_ENCHANTS);
                }
            });
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
