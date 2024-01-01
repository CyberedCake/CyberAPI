package net.cybercake.cyberapi.spigot.items;

import net.cybercake.cyberapi.spigot.CyberAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Made specifically for {@link ItemCreator} ({@link ItemCreator.ItemBuilder#showEnchantGlow(boolean) this method specifically}) to allow for only glowing on an item.
 * @since 141
 */
public class Glow extends Enchantment {

    public static NamespacedKey NAMESPACED_KEY = new NamespacedKey(CyberAPI.getInstance(), "pseudo_glow_enchant");

    @NotNull
    @Override
    public String getName() {
        return "Glow";
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @NotNull
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return true;
    }


    @NotNull @Override public NamespacedKey getKey() { return NAMESPACED_KEY; }
}
