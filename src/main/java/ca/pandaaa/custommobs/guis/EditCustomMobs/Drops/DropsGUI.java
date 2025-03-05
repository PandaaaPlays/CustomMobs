package ca.pandaaa.custommobs.guis.EditCustomMobs.Drops;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class DropsGUI extends CustomMobsGUI {
    private List<Drop> dropsItems;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;
    private final ItemStack addDropItem;
    private int currentPage = 1;

    public DropsGUI(CustomMob customMob) {
        super(54, "&8CustomMobs &8&l» &8Drops");

        this.customMob = customMob;
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);
        addDropItem = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
    }

    public void openInventory(Player player, int page) {
        dropsItems = customMob.getDrops();
        this.currentPage = page;
        boolean nextPage = dropsItems.size() > (page * 45);

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;

            if (dropsItems.size() > position)
                inventory.setItem(i, getDropItem(dropsItems.get(position)));
            else
                inventory.setItem(i, new ItemStack(Material.AIR));
        }

        if(page > 1) {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
            previous.setItemMeta(previousItemMeta);
        } else {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
            previous.setItemMeta(previousItemMeta);
        }
        inventory.setItem(45, previous);
        inventory.setItem(46, filler);
        inventory.setItem(47, filler);
        inventory.setItem(48, filler);
        ItemMeta addItemMeta = addDropItem.getItemMeta();
        if(addItemMeta != null) {
            addItemMeta.setDisplayName(Utils.applyFormat("&a&l[+] Add Drop"));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Drag & drop an item to add a drop ))"));
            addItemMeta.setLore(lore);
            addDropItem.setItemMeta(addItemMeta);
        }
        inventory.setItem(49, addDropItem);
        inventory.setItem(50, filler);
        inventory.setItem(51, filler);
        inventory.setItem(52, filler);
        if(nextPage) {
            ItemMeta nextItemMeta = next.getItemMeta();
            if(nextItemMeta != null)
                nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
            next.setItemMeta(nextItemMeta);
            inventory.setItem(53, next);
        } else
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

        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null)
            return;
        ItemMeta itemMeta = item.getItemMeta();
        final String name = itemMeta.getDisplayName();
        Player clicker = (Player) event.getWhoClicked();
        ItemStack cursorItem = clicker.getItemOnCursor();

        switch (event.getSlot()) {
            case 45:
                if(!name.contains("("))
                    new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                else {
                    openInventory(clicker, currentPage - 1);
                }
                break;
            case 49:
                if (cursorItem.getType() != Material.AIR) {
                    Drop newDropItem = new Drop(cursorItem.clone());
                    customMob.addDrop(newDropItem);

                    if (inventory.firstEmpty() == -1) {  // Overflow in the next page
                        openInventory(clicker.getPlayer(), currentPage + 1);
                        inventory.setItem(0, getDropItem(newDropItem));
                    } else
                        inventory.setItem(inventory.firstEmpty(), getDropItem(newDropItem));
                }
                break;
            case 53:
                openInventory(clicker, currentPage + 1);
                break;
            default:
                if (event.getSlot() < 45) {
                    int dropItemIndex = (currentPage - 1) * 45 + event.getSlot();
                    NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Drop.Remove.Confirm");
                    if (event.isRightClick() && dropsItems.get(event.getSlot()).isDeletable()) {
                        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key)) {
                            customMob.removeDropItem(dropItemIndex);
                            // DropItems is desynchronized until the reopening of the inventory.
                            boolean currentPageEmpty = dropsItems.size() - 1 <= ((currentPage - 1) * 45);
                            if (currentPageEmpty)
                                currentPage = currentPage == 1 ? currentPage : currentPage - 1;
                            openInventory(clicker, currentPage);
                        } else {
                            openInventory(clicker, currentPage);
                            event.getInventory().setItem(event.getSlot(), getMenuItem(getDeleteItem(new ItemStack(Material.BARRIER)), true));
                        }
                    } else {
                        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key))
                            openInventory(clicker, currentPage);
                        else
                            new SpecificDropGUI(customMob, dropsItems.get(dropItemIndex), dropItemIndex).openInventory(clicker);
                    }
                }
                break;
        }
    }

    private ItemStack getDropItem(Drop dropItem) {
        ItemStack item = dropItem.getItemStack().clone();
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : (ArrayList<String>) itemMeta.getLore();
        lore.add(Utils.applyFormat("&f&l* &dProbability:&f " + dropItem.getProbability() + "%"));
        String looting = dropItem.isLooting() ? "&a&lOn" : "&c&lOff";
        lore.add(Utils.applyFormat("&f&l* &6Looting:&f " + looting));
        lore.add(Utils.applyFormat("&f&l* &cCondition:&f " + Utils.getStartCase(dropItem.getDropCondition().name())));
        String color = "&fNone";
        if(dropItem.getGroupColor() != null) {
            color = Utils.getChatColorOfColor(dropItem.getGroupColor().name()) + Utils.getSentenceCase(dropItem.getGroupColor().toString());
        }

        lore.add(Utils.applyFormat("&f&l* &aGroup:&f " + color));
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Left-Click to edit this drop ))"));
        if(dropItem.isDeletable())
            lore.add(Utils.applyFormat("&7&o(( Right-Click to remove this drop ))"));
        if(!dropItem.isDeletable())
            lore.add(Utils.applyFormat("&c&l[!] &cThis drop cannot be deleted from this menu."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return getMenuItem(item, false);
    }

    private ItemStack getDeleteItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Drop.Remove.Confirm");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Confirm drop deletion"));
        lore.add(Utils.applyFormat("&7&o(( Left-click to cancel the deletion ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-click again to confirm the deletion ))"));
        lore.add(Utils.applyFormat("&c&l[!] &cThis will permanently delete this drop."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
