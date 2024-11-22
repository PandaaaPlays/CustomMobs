package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class MainGUI extends CustomMobsGUI {
    private final List<ItemStack> items;
    private final ItemStack previous;
    private final ItemStack addCustomMob;
    private final ItemStack next;
    private int currentPage = 1;
    private Player waitingForCreation;

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
        this.currentPage = page;
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
// TODO WE cannot check for item name, do the slot instead
        String itemName = itemMeta.getDisplayName();
        if(itemName.contains("Previous")) {
            new MainGUI().openInventory(clicker, currentPage - 1);
        } else if (itemName.contains("Next")) {
            new MainGUI().openInventory(clicker, currentPage + 1);
        } else if (itemName.contains("Add CustomMob")) {
            clicker.closeInventory();
            clicker.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eCustomMob creation"));
            clicker.sendMessage(Utils.applyFormat(" &6&l- &fEnter the name you want &e&nin the chat&f."));
            clicker.sendMessage(Utils.applyFormat(" &6&l- &fThis name must be unique, it is only used to refer to the mob in the CustomMob menu."));
            clicker.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel creation."));
            waitingForCreation = clicker;
        } else if (clickedSlot < 45) {
            try {
                NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName");
                String fileName = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING).toLowerCase();
                Manager customMobsManager = CustomMobs.getPlugin().getCustomMobsManager();

                NamespacedKey removeKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Remove.Confirm");
                if (event.isRightClick()) {
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(removeKey)) {
                        customMobsManager.getCustomMob(fileName).delete();
                        boolean currentPageEmpty = items.size() - 1 <= ((currentPage - 1) * 45);
                        if (currentPageEmpty)
                            currentPage = currentPage == 1 ? currentPage : currentPage - 1;
                        new MainGUI().openInventory(clicker, currentPage);
                    } else {
                        openInventory(clicker, currentPage);
                        event.getInventory().setItem(event.getSlot(), getMenuItem(getDeleteItem(new ItemStack(Material.BARRIER), fileName), true));
                    }
                } else {
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(removeKey))
                        openInventory(clicker, currentPage);
                    else
                        new EditGUI(customMobsManager.getCustomMob(fileName), customMobsManager, clicker).openInventory();
                }
            } catch (Exception exception) {
                clicker.sendMessage(Utils.applyFormat("&c&l[!] &cNo CustomMob was associated to this item. This is a bug."));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(waitingForCreation == null)
            return;
        if (event.getPlayer() != waitingForCreation)
            return;

        event.setCancelled(true);

        CustomMob customMob = null;

        Manager customMobsManager = CustomMobs.getPlugin().getCustomMobsManager();
        String message = event.getMessage();
        if (!message.equalsIgnoreCase("cancel")) {
            if(!customMobsManager.getCustomMobNames().contains(message.replaceAll(" ", "_"))) {
                customMob = customMobsManager.addCustomMob(message, EntityType.PIG);
                waitingForCreation.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eSuccessfully created : &r" + message));
            } else {
                // TODO message
                return;
            }
        }

        waitingForCreation = null;
        Bukkit.getScheduler().runTask(CustomMobs.getPlugin(), new EditGUI(customMob, customMobsManager, event.getPlayer())::openInventory);
    }

    private List<String> getItemLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to edit this CustomMob ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-Click to remove this drop ))"));
        return lore;
    }

    private ItemStack getDeleteItem(ItemStack item, String fileName) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Remove.Confirm");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        NamespacedKey fileKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName");
        itemMeta.getPersistentDataContainer().set(fileKey, PersistentDataType.STRING, fileName);
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Confirm CustomMob deletion"));
        lore.add(Utils.applyFormat("&7&o(( Left-click to cancel the deletion ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-click again to confirm the deletion ))"));
        lore.add(Utils.applyFormat("&c&l[!] &cThis will permanently delete this CustomMob."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
