package ca.pandaaa.custommobs.utils;

import ca.pandaaa.custommobs.CustomMobs;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomMobsItem extends org.bukkit.inventory.ItemStack {

    // TODO test if this works with 2 players, if not, we need to clone the item before actually changing the itme
    public CustomMobsItem(Material material) {
        super(material);
    }

    public void addLore(String... loreList) {
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta != null) {
            List<String> itemLore = itemMeta.getLore();
            if(itemLore == null)
                itemLore = new ArrayList<>();
            for(String lore : loreList) {
                itemLore.add(Utils.applyFormat(lore));
            }
            itemMeta.setLore(itemLore);
            setItemMeta(itemMeta);
        }
    }

    public void setName(String name) {
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta != null) {
            itemMeta.setDisplayName(Utils.applyFormat(name));
            setItemMeta(itemMeta);
        }
    }

    public void setPersistentDataContainer(String className, String containerName) {
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta != null) {
            NamespacedKey keyIndex = new NamespacedKey(CustomMobs.getPlugin(),
                    "CustomMobs" + ".Option");

            itemMeta.getPersistentDataContainer().set(keyIndex, PersistentDataType.STRING, className + "." + containerName);

            setItemMeta(itemMeta);
        }

    }
}
