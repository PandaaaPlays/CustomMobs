package ca.pandaaa.custommobs.guis.BasicTypes;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
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

public class DoubleIntegerGUI extends CustomMobsGUI implements Listener {

    private final Consumer<int[]> consumer;
    private final ItemStack minusBig;
    private final ItemStack minusSmall;
    private final ItemStack confirm;
    private final ItemStack plusBig;
    private final ItemStack plusSmall;
    private final CustomMob customMob;
    private final boolean small;
    private final double maximum;
    private final double minimum;
    public DoubleIntegerGUI(String option, CustomMob customMob, boolean small, int minValue, int maxValue, Consumer<int[]> consumer) {
        super(18, "&8Parameter &8&l» &8" + option);
        this.consumer = consumer;
        this.customMob = customMob;
        this.small = small;
        this.maximum = maxValue;
        this.minimum = minValue;
        minusBig = getMenuItem(Utils.createHead("cc8e7d46d693341f91d286726f2555ef15514e3460b275e9747842bc9e53df"), true);
        minusSmall = getMenuItem(Utils.createHead("e6a8464990cd7666811d6f292fe869692a184e0c7446fa587ed1c9b1fea724"), true);
        plusBig = getMenuItem(Utils.createHead("69b861aabb316c4ed73b4e5428305782e735565ba2a053912e1efd834fa5a6f"), true);
        plusSmall = getMenuItem(Utils.createHead("5a51a58967f3b7af026c9c289dfdca4c436268575166954a694921541ec0fc"), true);
        confirm = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
    }

    public void openInventory(Player player, Integer valueMin, Integer valueMax) {
        inventory.setItem(0, filler);
        inventory.setItem(1, small ? filler : getMinusItem(minusBig, true,1));
        inventory.setItem(2, getMinusItem(minusSmall, false,1));
        inventory.setItem(3, filler);
        inventory.setItem(4, getConfirmItem(confirm, valueMin,1));
        inventory.setItem(5, filler);
        inventory.setItem(6, getPlusItem(plusSmall, false,1));
        inventory.setItem(7, small ? filler : getPlusItem(plusBig, true,1));
        inventory.setItem(8, filler);

        inventory.setItem(9, filler);
        inventory.setItem(10, small ? filler : getMinusItem(minusBig, true,2));
        inventory.setItem(11, getMinusItem(minusSmall, false,2));
        inventory.setItem(12, filler);
        inventory.setItem(13, getConfirmItem(confirm, valueMax,2));
        inventory.setItem(14, filler);
        inventory.setItem(15, getPlusItem(plusSmall, false,2));
        inventory.setItem(16, small ? filler : getPlusItem(plusBig, true,2));
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
        NamespacedKey keyMin = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Integer1");
        NamespacedKey keyMax = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Integer2");
        int currentMin = event.getInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(keyMin, PersistentDataType.INTEGER);
        int currentMax = event.getInventory().getItem(13).getItemMeta().getPersistentDataContainer().get(keyMax, PersistentDataType.INTEGER);


        boolean shifting = !small && event.isShiftClick();

        switch (event.getSlot()) {
            case 1:
                if (((!shifting && currentMin >= minimum + 10) || (shifting && currentMin >= minimum + 50)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin - 50 : currentMin - 10,+ 1));
                break;
            case 2:
                if ((!shifting && currentMin >= minimum + 1) || (shifting && currentMin >= minimum + 5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin - 5 : currentMin - 1,+ 1));
                break;
            case 4:
                consumer.accept(new int[]{currentMin, currentMax});
                new OptionsGUI(customMob).openInventory((Player) event.getWhoClicked(), 1);
                break;
            case 6:
                if ((!shifting && currentMin < currentMax - 1) || (shifting && currentMin < currentMax - 5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin + 5 : currentMin + 1,+ 1));
                break;
            case 7:
                if (((!shifting && currentMin < currentMax - 10) || (shifting && currentMin < currentMax - 50)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin + 50 : currentMin + 10,+ 1));
                break;
            case 10:
                if (((!shifting && currentMax > currentMin + 10) || (shifting && currentMax > currentMin + 50)) && !small)
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax - 50 : currentMax - 10,+ 2));
                break;
            case 11:
                if ((!shifting && currentMax > currentMin + 1) || (shifting && currentMax > currentMin + 5))
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax - 5 : currentMax - 1,+ 2));
                break;
            case 13:
                consumer.accept(new int[]{currentMin, currentMax});
                break;
            case 15:
                if ((!shifting && currentMax <= maximum - 1) || (shifting && currentMax <= maximum - 5))
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax + 5 : currentMax + 1,+ 2));
                break;
            case 16:
                if (((!shifting && currentMax <= maximum - 10) || (shifting && currentMax <= maximum - 50)) && !small)
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax + 50 : currentMax + 10,+ 2));
                break;
            default:
                break;
        }
    }

    private ItemStack getConfirmItem(ItemStack item, Integer value, Integer index) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Integer"+index);
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

    private ItemStack getMinusItem(ItemStack item, boolean big, int index) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Integer");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, index);
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

    private ItemStack getPlusItem(ItemStack item, boolean big, int index) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Integer");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, index);
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
