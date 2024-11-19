package ca.pandaaa.custommobs.guis.EditCustomMobs.Potions;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.Drops.SpecificDropGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SpecificPotionDurationGUI extends CustomMobsGUI {

    private final Consumer<Integer> consumer;
    private final CustomMob customMob;
    private final ItemStack minusHour;
    private final ItemStack minusMinute;
    private final ItemStack minusSecond;
    private final ItemStack confirm;
    private final ItemStack plusHour;
    private final ItemStack plusMinute;
    private final ItemStack plusSecond;
    private final int potionDuration;
    private final int potionIndex;
    private final int maximum = 107374182;
    private final int minimum = -1;

    public SpecificPotionDurationGUI(CustomMob customMob, int potionDuration, int potionIndex, Consumer<Integer> consumer) {
        super(9, "&8CustomMobs &8&l» &8Drop chance");
        this.consumer = consumer;
        minusHour = getMenuItem(Utils.createHead("a8c67fed7a2472b7e9afd8d772c13db7b82c32ceeff8db977474c11e4611"), true);
        minusMinute = getMenuItem(Utils.createHead("7b75ea4d37fca4bfa8f6a3ce56d1473607073a456d33b1227435b99ad71d147"), true);
        minusSecond = getMenuItem(Utils.createHead("cc8e7d46d693341f91d286726f2555ef15514e3460b275e9747842bc9e53df"), true);
        confirm = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
        plusHour = getMenuItem(Utils.createHead("0a21eb4c57750729a48b88e9bbdb987eb6250a5bc2157b59316f5f1887db5"), true);
        plusMinute = getMenuItem(Utils.createHead("55d58de8eb283eed0e4122f1c5113d0c3a229eec5b6d7c1481dd23520bc45db"), true);
        plusSecond = getMenuItem(Utils.createHead("69b861aabb316c4ed73b4e5428305782e735565ba2a053912e1efd834fa5a6f"), true);
        this.customMob = customMob;
        this.potionDuration = potionDuration;
        this.potionIndex = potionIndex;
    }

    public void openInventory(Player player) {
        inventory.setItem(0, getMinusItem(minusHour, 3600));
        inventory.setItem(1, getMinusItem(minusMinute, 60));
        inventory.setItem(2, getMinusItem(minusSecond, 1));
        inventory.setItem(3, filler);
        inventory.setItem(4, getConfirmItem(confirm, potionDuration));
        inventory.setItem(5, filler);
        inventory.setItem(6, getPlusItem(plusSecond, 1));
        inventory.setItem(7, getPlusItem(plusMinute, 60));
        inventory.setItem(8, getPlusItem(plusHour, 3600));

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

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Potion.Duration");
        int current = event.getInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        boolean shifting = event.isShiftClick();

        switch (event.getSlot()) {
            case 0:
                if (((!shifting && current >= minimum + 3600) || (shifting && current >= minimum + 36000)))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 36000 : current - 3600));
                break;
            case 1:
                if (((!shifting && current >= minimum + 60) || (shifting && current >= minimum + 600)))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 600 : current - 60));
                break;
            case 2:
                if (((!shifting && current >= minimum + 1) || (shifting && current >= minimum + 10)))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 10 : current - 1));
                break;
            case 4:
                consumer.accept(current);
                new SpecificPotionGUI(customMob, potionIndex).openInventory((Player) event.getWhoClicked());
                break;
            case 6:
                if ((!shifting && current <= maximum - 1) || (shifting && current <= maximum - 10))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 10 : current + 1));
                break;
            case 7:
                if ((!shifting && current <= maximum - 60) || (shifting && current <= maximum - 600))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 600 : current + 60));
                break;
            case 8:
                if ((!shifting && current <= maximum - 3600) || (shifting && current <= maximum - 36000))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 36000 : current + 3600));
                break;
            default:
                break;
        }
    }


    private ItemStack getConfirmItem(ItemStack item, int value) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Potion.Duration");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        meta.setDisplayName(Utils.applyFormat("&a&l[+] Confirm"));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(Utils.applyFormat("&eCurrent value: &f" + getFormattedSize(value)));
        itemLore.add("");
        itemLore.add(Utils.applyFormat("&7&o(( Click to confirm! ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getMinusItem(ItemStack item, int size) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove " + getFormattedSize(size)));
        itemLore.add(Utils.applyFormat("&7&o(( Click to remove " + getFormattedSize(size) + " ))"));
        itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove " + getFormattedSize(size * 10) + " ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPlusItem(ItemStack item, int size) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");

        meta.setDisplayName(Utils.applyFormat("&a&l[+] Add " + getFormattedSize(size)));
        itemLore.add(Utils.applyFormat("&7&o(( Click to add " + getFormattedSize(size) + " ))"));
        itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add " + getFormattedSize(size * 10) + " ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    private String getFormattedSize(int size) {
        if(size <= 0)
            return "Infinite";
        String formattedSize = "";

        boolean hours = false;
        if (size >= 3600) {
            formattedSize += size / 3600 + " hour" + (size >= 7200 ? "(s) " : " ");
            hours = true;
        }
        size = size % 3600;

        boolean minutes = false;
        if (size >= 60) {
            formattedSize += (hours ? "& " : "") + size / 60 + " minute" + (size >= 120 ? "(s) " : " ");
            minutes = true;
        }
        size = size % 60;

        if (size > 0)
            formattedSize += (minutes ? "& " : "") + size + " second" +  (size > 1 ? "(s) " : " ");

        return formattedSize;
    }
}
