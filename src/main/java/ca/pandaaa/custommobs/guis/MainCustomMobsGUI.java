package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class MainCustomMobsGUI implements Listener {
    private final Inventory inventory;
    private final List<ItemStack> items;
    private final ItemStack filler;
    private final ItemStack previous;
    private final ItemStack addCustomMob;
    private final ItemStack next;

    public MainCustomMobsGUI(List<ItemStack> items) {
        this.inventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&8CustomMobs &8&l» &8Mobs"));
        this.items = items;
        filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerItemMeta = filler.getItemMeta();
        fillerItemMeta.setDisplayName(" ");
        filler.setItemMeta(fillerItemMeta);

        previous = Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe");

        next = Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94");


        addCustomMob = new ItemStack(Material.END_CRYSTAL);
    }

    public void openInventory(Player player, int page) {
        ItemMeta previousItemMeta = previous.getItemMeta();
        previousItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lPrevious (" + (page - 1) + ")"));
        previous.setItemMeta(previousItemMeta);
        ItemMeta nextItemMeta = next.getItemMeta();
        nextItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lNext (" + (page + 1) + ")"));
        next.setItemMeta(nextItemMeta);

        boolean nextPage = true;

        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 44) + i;
            if (items.size() > position)
                inventory.setItem(i, items.get(position));
            else {
                inventory.setItem(i, new ItemStack(Material.AIR));
                nextPage = false;
            }
        }

        if(page > 1)
            inventory.setItem(45, previous);
        else
            inventory.setItem(45, filler);
        inventory.setItem(46, filler);
        inventory.setItem(47, filler);
        inventory.setItem(48, filler);
        inventory.setItem(49, addCustomMob);
        inventory.setItem(50, filler);
        inventory.setItem(51, filler);
        inventory.setItem(52, filler);
        if(nextPage)
            inventory.setItem(53, next);
        else
            inventory.setItem(53, filler);

        player.openInventory(inventory);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if(!Objects.equals(event.getClickedInventory(), inventory))
            return;

        event.setCancelled(true);
        String name = event.getCurrentItem().getItemMeta().getDisplayName();
        if(name.contains("Previous")) {
            openInventory((Player) event.getWhoClicked(), Character.getNumericValue(name.charAt(name.indexOf('(') + 1)));
        } else if (name.contains("Next")) {
            openInventory((Player) event.getWhoClicked(), Character.getNumericValue(name.charAt(name.indexOf('(') + 1)));
        }
    }

}
