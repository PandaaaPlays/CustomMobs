package ca.pandaaa.custommobs.utils;

import ca.pandaaa.custommobs.CustomMobs;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomMobsItem {
    private final ItemStack item;

    public CustomMobsItem(Material material) {
        this.item = new ItemStack(material);
    }

    public CustomMobsItem(ItemStack item) {
        this.item = item.clone();
    }

    public ItemStack getItem() {
        return item;
    }

    public void addLore(String... loreList) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            List<String> itemLore = itemMeta.getLore();
            if (itemLore == null) itemLore = new ArrayList<>();
            for (String lore : loreList) {
                itemLore.add(Utils.applyFormat(lore));
            }
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
        }
    }

    public void addEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
            item.setItemMeta(itemMeta);
        }
    }

    public void setName(String name) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(Utils.applyFormat(name));
            item.setItemMeta(itemMeta);
        }
    }

    public void setType(Material type) {
        item.setType(type);
    }

    public ItemMeta getItemMeta() {
        return item.getItemMeta();
    }

    public void setItemMeta(ItemMeta itemMeta) {
        item.setItemMeta(itemMeta);
    }

    public void setOptionPersistentDataContainer(String className, String containerName) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey keyIndex = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Option");
            itemMeta.getPersistentDataContainer().set(keyIndex, PersistentDataType.STRING, className + "." + containerName);
            item.setItemMeta(itemMeta);
        }
    }

    public void setCustomEffectPersistentDataContainer(String className) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey keyIndex = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.CustomEffect");
            itemMeta.getPersistentDataContainer().set(keyIndex, PersistentDataType.STRING, className);
            item.setItemMeta(itemMeta);
        }
    }
}
