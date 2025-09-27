package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class CustomMobsGUI implements Listener {
    protected ItemStack filler;
    protected final Inventory inventory;

    public CustomMobsGUI(int size, String title) {
        this.filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerItemMeta = filler.getItemMeta();
        if(fillerItemMeta != null)
            fillerItemMeta.setDisplayName(" ");
        this.filler.setItemMeta(fillerItemMeta);
        this.filler = getMenuItem(this.filler, true);

        title = Utils.applyFormat(title);

        int titleLength = 27;
        Matcher matcher = ChatColor.STRIP_COLOR_PATTERN.matcher(title);
        while(matcher.find())
            titleLength += 2;

        title = (title.length() > titleLength ? title.substring(0, titleLength) + "." : title);

        this.inventory = Bukkit.createInventory(null, size, title);
        CustomMobs plugin = CustomMobs.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected boolean isEventRelevant(Inventory clickedInventory) {
        if (!Objects.equals(clickedInventory, inventory))
            return false;

        return true;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if(!Objects.equals(event.getView().getTopInventory(), inventory)) {
            return;
        }
        for (int slot : event.getRawSlots()) {
            if (slot < event.getView().getTopInventory().getSize() - 1) {
                event.setCancelled(true);
                return;
            }
        }
    }

    private static boolean DEBUG_MENU_ITEM = false;
    protected static ItemStack getMenuItem(ItemStack item, boolean hideItemFlags) {
        ItemStack menuItem = item.clone();
        ItemMeta itemMeta = menuItem.getItemMeta();
        if(DEBUG_MENU_ITEM) {
            List<String> lore = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
            lore.add(Utils.applyFormat("&c&lMENU_ITEM"));
            itemMeta.setLore(lore);
        }
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MenuItem");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        if(hideItemFlags) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            itemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        }
        menuItem.setItemMeta(itemMeta);
        return menuItem;
    }

    protected static ItemStack getMenuItem(CustomMobsItem customMobsItem, boolean hideItemFlags) {
        return getMenuItem(customMobsItem.getItem(), hideItemFlags);
    }

    protected static ItemStack getPreviousItem() {
        ItemStack item = Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe");
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
        item.setItemMeta(itemMeta);
        return getMenuItem(item, true);
    }

    protected static ItemStack getNextItem() {
        ItemStack item = Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94");
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&e&lNext"));
        item.setItemMeta(itemMeta);
        return getMenuItem(item, true);
    }
}
