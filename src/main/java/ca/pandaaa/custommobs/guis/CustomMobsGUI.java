package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.regex.Matcher;

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

        title = Utils.applyFormat(title);

        int titleLength = 29;
        Matcher matcher = ChatColor.STRIP_COLOR_PATTERN.matcher(title);
        while(matcher.find())
            titleLength += 2;

        title = (title.length() > titleLength ? title.substring(0, titleLength) + "." : title);

        this.inventory = Bukkit.createInventory(null, size, title);
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
