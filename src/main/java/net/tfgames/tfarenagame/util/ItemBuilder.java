package net.tfgames.tfarenagame.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
    }

    public ItemMeta getMeta() {
        return itemStack.getItemMeta();
    }

    public ItemBuilder setAmount(int a) {
        itemStack.setAmount(a);
        return this;
    }

    public ItemBuilder setColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(color);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = getMeta();
        meta.setDisplayName(name);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setEnchantment(Enchantment e, int i) {
        ItemMeta meta = getMeta();
        meta.addEnchant(e, i, true);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setFlag(ItemFlag i) {
        ItemMeta meta = getMeta();
        meta.addItemFlags(i);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        ItemMeta meta = getMeta();
        meta.setUnbreakable(true);
        setFlag(ItemFlag.HIDE_UNBREAKABLE);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> l) {
        ItemMeta meta = getMeta();
        meta.setLore(l);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String l) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', l));
        ItemMeta meta = getMeta();
        meta.setLore(lore);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setMeta(ItemMeta meta) {
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setGlow() {
        ItemMeta meta = getMeta();
        meta.addEnchant(Enchantment.LURE, 1, true);
        setFlag(ItemFlag.HIDE_ENCHANTS);
        setMeta(meta);
        return this;
    }

    public ItemBuilder setSkull(String name) {
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        setMeta(meta);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }

}
