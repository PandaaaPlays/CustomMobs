package ca.pandaaa.custommobs.guis.BasicTypes;

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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IntegerGUI extends CustomMobsGUI implements Listener {

    private final Consumer<Integer> consumer;
    private final ItemStack minusBig;
    private final ItemStack minusSmall;
    private final ItemStack confirm;
    private final ItemStack plusBig;
    private final ItemStack plusSmall;
    private final boolean small;
    private final int maximum;
    private final int minimum;
    public IntegerGUI(String option, CustomMob customMob, boolean small, int minValue, int maxValue, Consumer<Integer> consumer) {
        super(9, "&8Parameter &8&l» &8" + option);
        this.consumer = consumer;
        this.small = small;
        this.maximum = maxValue;
        this.minimum = minValue;
        minusBig = getMenuItem(Utils.createHead("cc8e7d46d693341f91d286726f2555ef15514e3460b275e9747842bc9e53df"), true);
        minusSmall = getMenuItem(Utils.createHead("e6a8464990cd7666811d6f292fe869692a184e0c7446fa587ed1c9b1fea724"), true);
        plusBig = getMenuItem(Utils.createHead("69b861aabb316c4ed73b4e5428305782e735565ba2a053912e1efd834fa5a6f"), true);
        plusSmall = getMenuItem(Utils.createHead("5a51a58967f3b7af026c9c289dfdca4c436268575166954a694921541ec0fc"), true);
        confirm = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
    }

    public void openInventory(Player player, Integer value) {
        inventory.setItem(0, filler);
        inventory.setItem(1, small ? filler : getMinusItem(minusBig, true));
        inventory.setItem(2, getMinusItem(minusSmall, false));
        inventory.setItem(3, filler);
        inventory.setItem(4, getConfirmItem(confirm, value));
        inventory.setItem(5, filler);
        inventory.setItem(6, getPlusItem(plusSmall, false));
        inventory.setItem(7, small ? filler : getPlusItem(plusBig, true));
        inventory.setItem(8, filler);

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

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Integer");
        int current = event.getInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

        boolean shifting = !small && event.isShiftClick();

        switch (event.getSlot()) {
            case 1:
                if(((!shifting && current - 10 <= minimum) || (shifting && current - 50 <= minimum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), minimum));
                else if (((!shifting && current >= minimum + 10) || (shifting && current >= minimum + 50)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 50 : current - 10));
                break;
            case 2:
                if(((!shifting && current - 1 <= minimum) || (shifting && current - 5 <= minimum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), minimum));
                else if ((!shifting && current >= minimum + 1) || (shifting && current >= minimum + 5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 5 : current - 1));
                break;
            case 4:
                consumer.accept(current);
                break;
            case 6:
                if(((!shifting && current + 1 >= maximum) || (shifting && current + 5 >= maximum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), maximum));
                else if ((!shifting && current <= maximum - 1) || (shifting && current <= maximum - 5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 5 : current + 1));
                break;
            case 7:
                if(((!shifting && current + 10 >= maximum) || (shifting && current + 50 >= maximum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), maximum));
                else if (((!shifting && current <= maximum - 10) || (shifting && current <= maximum - 50)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 50 : current + 10));
                break;
            default:
                break;
        }
    }

    private ItemStack getConfirmItem(ItemStack item, Integer value) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Integer");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        meta.setDisplayName(Utils.applyFormat("&a&l[+] Confirm"));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(Utils.applyFormat("&eCurrent value: &f" + value));
        itemLore.add("");
        itemLore.add(Utils.applyFormat("&7&o(( Click to confirm! ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getMinusItem(ItemStack item, boolean big) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        if (big) {
            meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove 10"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to remove 10 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove 50 ))"));
        } else {
            meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove 1"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to remove 1 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove 5 ))"));
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
            meta.setDisplayName(Utils.applyFormat("&a&l[+] Add 10"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to add 10 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add 50 ))"));
        } else {
            meta.setDisplayName(Utils.applyFormat("&a&l[+] Add 1"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to add 1 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add 5 ))"));
        }
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }
}
