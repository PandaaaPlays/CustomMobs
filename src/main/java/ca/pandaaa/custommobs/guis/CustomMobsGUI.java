package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CustomMobsGUI {
    protected ItemStack filler;
    protected final Inventory inventory;

    public CustomMobsGUI(int size, String title) {
        this.filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerItemMeta = filler.getItemMeta();
        if(fillerItemMeta != null)
            fillerItemMeta.setDisplayName(" ");
        this.filler.setItemMeta(fillerItemMeta);
        this.filler = getMenuItem(this.filler);

        this.inventory = Bukkit.createInventory(null, size, Utils.applyFormat(title));
    }

    protected ItemStack getMenuItem(ItemStack item) {
        ItemStack menuItem = item.clone();
        ItemMeta itemMeta = menuItem.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MenuItem");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        menuItem.setItemMeta(itemMeta);
        return menuItem;
    }
}
