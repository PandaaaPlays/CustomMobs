package ca.pandaaa.custommobs.guis.EditCustomMobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
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

public class MessagesGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private final ItemStack minusBig;
    private final ItemStack minusSmall;
    private final ItemStack plusBig;
    private final ItemStack plusSmall;

    public MessagesGUI(CustomMob customMob) {
        super(18, "&8CustomMobs &8&l» &8Messages");
        this.customMob = customMob;
        minusBig = getMenuItem(Utils.createHead("cc8e7d46d693341f91d286726f2555ef15514e3460b275e9747842bc9e53df"), true);
        minusSmall = getMenuItem(Utils.createHead("e6a8464990cd7666811d6f292fe869692a184e0c7446fa587ed1c9b1fea724"), true);
        plusBig = getMenuItem(Utils.createHead("69b861aabb316c4ed73b4e5428305782e735565ba2a053912e1efd834fa5a6f"), true);
        plusSmall = getMenuItem(Utils.createHead("5a51a58967f3b7af026c9c289dfdca4c436268575166954a694921541ec0fc"), true);
    }

    public void openInventory(Player player) {
        inventory.setItem(0, filler);
        inventory.setItem(1, getMinusItem(minusBig, true));
        inventory.setItem(2, getMinusItem(minusSmall, false));
        inventory.setItem(3, filler);
        inventory.setItem(4, getMessageItem(new ItemStack(Material.WRITABLE_BOOK), customMob.getCustomMobMessages().getRadius(), false));
        inventory.setItem(5, filler);
        inventory.setItem(6, getPlusItem(plusSmall, false));
        inventory.setItem(7, getPlusItem(plusBig, true));
        inventory.setItem(8, filler);
        inventory.setItem(9, filler);
        inventory.setItem(10, getMinusItem(minusBig, true));
        inventory.setItem(11, getMinusItem(minusSmall, false));
        inventory.setItem(12, filler);
        inventory.setItem(13, getMessageItem(new ItemStack(Material.DEAD_BUSH), customMob.getCustomMobMessages().getDeathRadius(), true));
        inventory.setItem(14, filler);
        inventory.setItem(15, getPlusItem(plusSmall, false));
        inventory.setItem(16, getPlusItem(plusBig, true));
        inventory.setItem(17, filler);
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

        boolean shifting = event.isShiftClick();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Message");

        double current = Math.round(event.getInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.DOUBLE) * 10.0) / 10.0;
        double currentDeath = Math.round(event.getInventory().getItem(13).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.DOUBLE) * 10.0) / 10.0;

        int slot = event.getSlot();

        switch (slot) {
            case 1:
                inventory.setItem(4, getMessageItem(inventory.getItem(4), shifting ? current - 5 : current - 1, false));
                break;
            case 2:
                inventory.setItem(4, getMessageItem(inventory.getItem(4), shifting ? current - 0.5 : current - 0.1, false));
                break;
            case 4: case 13:
                customMob.getCustomMobConfiguration().setMessagesRadius(current, false);
                customMob.getCustomMobConfiguration().setMessagesRadius(currentDeath, true);
                new OthersGUI(customMob).openInventory((Player) event.getWhoClicked());
                break;
            case 6:
                if (current < 0)
                    current = 0;
                inventory.setItem(4, getMessageItem(inventory.getItem(4), shifting ? current + 0.5 : current + 0.1, false));
                break;
            case 7:
                if (current < 0)
                    current = 0;
                inventory.setItem(4, getMessageItem(inventory.getItem(4), shifting ? current + 5 : current + 1, false));
                break;
            case 10:
                inventory.setItem(13, getMessageItem(inventory.getItem(13), shifting ? currentDeath - 5 : currentDeath - 1, true));
                break;
            case 11:
                inventory.setItem(13, getMessageItem(inventory.getItem(13), shifting ? currentDeath - 0.5 : currentDeath - 0.1, true));
                break;
            case 15:
                if (currentDeath < 0)
                    currentDeath = 0;
                inventory.setItem(13, getMessageItem(inventory.getItem(13), shifting ? currentDeath + 0.5 : currentDeath + 0.1, true));
                break;
            case 16:
                if (currentDeath < 0)
                    currentDeath = 0;
                inventory.setItem(13, getMessageItem(inventory.getItem(13), shifting ? currentDeath + 5 : currentDeath + 1, true));
                break;
            default:
                break;
        }
    }

    private ItemStack getMessageItem(ItemStack item, double value, boolean death) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        value = Math.round(value * 10.0) / 10.0;

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs." + "Message");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);

        itemMeta.setDisplayName(Utils.applyFormat("&6&lMessages"));
        lore.add(Utils.applyFormat("&eCurrent radius value: &f" + (value <= 0 ? "Everyone" : value)));
        if((!death && !customMob.getCustomMobMessages().getMessages().isEmpty())
                || (death && !customMob.getCustomMobMessages().getDeathMessages().isEmpty())) {
            lore.add("");
            lore.add(Utils.applyFormat("&eCurrent message(s):"));
            int i = 1;
            // TODO Message are both in death and not in death?
            for (String message : death ? customMob.getCustomMobMessages().getMessages() : customMob.getCustomMobMessages().getDeathMessages()) {
                lore.add(Utils.applyFormat("&f " + i++ + ". &r" + message));
            }
        }

        lore.add("");
        if(!death) {
            lore.add(Utils.applyFormat("&7&o(( To add messages, use : /custommobs message [name] add [message] ))"));
            lore.add(Utils.applyFormat("&7&o(( To edit messages, use : /custommobs message [name] edit [number] [message] ))"));
            lore.add(Utils.applyFormat("&7&o(( To remove messages, use : /custommobs message [name] remove [number/all] ))"));
        } else {
            lore.add(Utils.applyFormat("&7&o(( To add death-messages, use : /custommobs death-message [name] add [message] ))"));
            lore.add(Utils.applyFormat("&7&o(( To edit death-messages, use : /custommobs death-message [name] edit [number] [message] ))"));
            lore.add(Utils.applyFormat("&7&o(( To remove death-messages, use : /custommobs death-message [name] remove [number/all] ))"));
        }
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to update messages radius ! ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getMinusItem(ItemStack item, boolean big) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        if (big) {
            meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove 1 to radius"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to remove 1 ))"));
            itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove 5 ))"));
        } else {
            meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove 0.1 to radius"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to remove 0.1 ))"));
            itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove 0.5 ))"));
        }
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPlusItem(ItemStack item, boolean big) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        if (big) {
            meta.setDisplayName(Utils.applyFormat("&a&l[+] Add 1 to radius"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to add 1 ))"));
            itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add 5 ))"));
        } else {
            meta.setDisplayName(Utils.applyFormat("&a&l[+] Add 0.1 to radius"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to add 0.1 ))"));
            itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add 0.5 ))"));
        }
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }
}


