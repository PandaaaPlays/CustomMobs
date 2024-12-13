package ca.pandaaa.custommobs.guis.EditCustomMobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class OptionsGUI extends CustomMobsGUI {

    private final List<ItemStack> optionsItems;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;

    public OptionsGUI(CustomMob customMob) {
        super(54, "&8CustomMobs &8&l» &8Options");

        this.customMob = customMob;

        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);

        this.optionsItems = new ArrayList<>();
        customMob.getCustomMobOptions().stream()
                .flatMap(customMobType -> customMobType.getOptionItems(customMob).stream())
                .map(item -> getMenuItem(item, true))
                .forEach(this.optionsItems::add);
    }

    public void openInventory(Player player, int page) {
        ItemMeta nextItemMeta = next.getItemMeta();
        if(nextItemMeta != null)
            nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
        next.setItemMeta(nextItemMeta);

        boolean nextPage = true;

        // Make sure we set the extra ca.pandaaa.custommobs.items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (optionsItems.size() > position)
                inventory.setItem(i, optionsItems.get(position));
            else {
                inventory.setItem(i, new ItemStack(Material.AIR));
                nextPage = false;
            }
        }

        ItemMeta previousItemMeta = previous.getItemMeta();
        if(page > 1) {
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
        } else {
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
        }
        previous.setItemMeta(previousItemMeta);
        inventory.setItem(45, previous);
        inventory.setItem(46, filler);
        inventory.setItem(47, filler);
        inventory.setItem(48, filler);
        inventory.setItem(49, filler);
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
        if (!isEventRelevant(event.getView().getTopInventory()))
            return;
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }

        event.setCancelled(true);

        final ItemStack item = event.getCurrentItem();
        if(item == null)
            return;

        final ItemMeta itemMeta = item.getItemMeta();
        final String name = itemMeta.getDisplayName();
        Player clicker = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 45:
                if(!name.contains("("))
                    new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                else {
                    int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                    openInventory(clicker, page);
                }
                break;
            case 53:
                int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                openInventory(clicker, page);
                break;
            default:
                for(NamespacedKey key : itemMeta.getPersistentDataContainer().getKeys()) {
                    String keyString = key.getKey();
                    if(keyString.contains("custommobs.option")) {
                        String[] value = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING).split("\\.");
                        event.setCurrentItem(getMenuItem(customMob.getCustomMobOption(value[0]).modifyOption(clicker, customMob, value[1], event.getClick()), true));
                    }
                }
                break;
        }
    }
}
