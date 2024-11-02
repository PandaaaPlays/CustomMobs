package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainGUI extends CustomMobsGUI implements Listener {
    private final List<ItemStack> items;
    private final ItemStack previous;
    private final ItemStack addCustomMob;
    private final ItemStack next;

    public MainGUI() {
        super(54, "&8CustomMobs &8&l» &8Mobs");

        CustomMobs plugin = CustomMobs.getPlugin();

        this.items = new ArrayList<>();
        for (CustomMob customMob : plugin.getCustomMobsManager().getCustomMobs()) {
            ItemStack item = new ItemStack(customMob.getItem().getType());

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Utils.applyFormat(customMob.getName()));
            itemMeta.setLore(getItemLore());
            NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName");
            itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, customMob.getCustomMobFileName().replace(".yml", ""));
            item.setItemMeta(itemMeta);

            items.add(getMenuItem(item, true));
        }

        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);

        addCustomMob = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
    }

    public void openInventory(Player player, int page) {
        ItemMeta previousItemMeta = previous.getItemMeta();
        if(previousItemMeta != null)
            previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
        previous.setItemMeta(previousItemMeta);

        ItemMeta nextItemMeta = next.getItemMeta();
        if(nextItemMeta != null)
            nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
        next.setItemMeta(nextItemMeta);

        ItemMeta addItemMeta = addCustomMob.getItemMeta();
        if(addItemMeta != null)
            addItemMeta.setDisplayName(Utils.applyFormat("&a&l[+] Add CustomMob"));
        addCustomMob.setItemMeta(addItemMeta);

        boolean nextPage = true;

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
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
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory())) {
            return;
        }
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null)
            return;
        ItemMeta itemMeta = item.getItemMeta();
        Player clicker = (Player) event.getWhoClicked();
        int clickedSlot = event.getSlot();

        String itemName = itemMeta.getDisplayName();
        if(itemName.contains("Previous")) {
            int page = Character.getNumericValue(itemName.charAt(itemName.indexOf('(') + 1));
            new MainGUI().openInventory(clicker, page);
        } else if (itemName.contains("Next")) {
            int page = Character.getNumericValue(itemName.charAt(itemName.indexOf('(') + 1));
            new MainGUI().openInventory(clicker, page);
        } else if (itemName.contains("Add CustomMob")) {
            // TODO Add custommob
        } else if (clickedSlot < 45) {
            try {
                NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName");
                String fileName = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                Manager customMobsManager = CustomMobs.getPlugin().getCustomMobsManager();
                new EditGUI(customMobsManager.getCustomMob(fileName.toLowerCase()), customMobsManager, clicker).openInventory();
            } catch (Exception exception) {
                clicker.sendMessage(Utils.applyFormat("&c&l[!] &cNo CustomMob was associated to this item. This is a bug."));
            }
        }
    }

    private List<String> getItemLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to edit this CustomMob ))"));
        return lore;
    }
}
