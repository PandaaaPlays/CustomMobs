package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMobsManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CustomMobsGUIEvents implements Listener {

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().contains("CustomMobs")
                || event.getClickedInventory() == null
                || event.getClickedInventory().getType() == InventoryType.PLAYER)
            return;

        event.setCancelled(true);

        final ItemStack item = event.getCurrentItem();
        if(item == null)
            return;

        final ItemMeta itemMeta = item.getItemMeta();
        final int slot = event.getSlot();
        final Player clicker = (Player) event.getWhoClicked();

        if (ChatColor.stripColor(event.getView().getTitle()).equals("CustomMobs » Mobs")) {
            onMainCustomMobsGUIClick(clicker, itemMeta, slot);
        }
    }

    private void onMainCustomMobsGUIClick(Player clicker, ItemMeta itemMeta, int clickedSlot) {
        String itemName = itemMeta.getDisplayName();
        int page = Character.getNumericValue(itemName.charAt(itemName.indexOf('(') + 1));
        if(itemName.contains("Previous")) {
            new MainCustomMobsGUI().openInventory(clicker, page);
        } else if (itemName.contains("Next")) {
            new MainCustomMobsGUI().openInventory(clicker, page);
        } else if (!(clickedSlot > 45)) {
            NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName");
            String fileName = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            CustomMobsManager customMobsManager = CustomMobs.getPlugin().getCustomMobsManager();
            new EditCustomMobsGUI(customMobsManager.getCustomMob(fileName.toLowerCase()), customMobsManager, clicker).openInventory();
        }
    }
}
