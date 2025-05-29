package ca.pandaaa.custommobs.guis.BasicTypes;

import ca.pandaaa.custommobs.CustomMobs;
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

public class DoubleRangeGUI extends CustomMobsGUI implements Listener {

    private final Consumer<double[]> consumer;
    private final ItemStack minusBig;
    private final ItemStack minusSmall;
    private final ItemStack confirm;
    private final ItemStack plusBig;
    private final ItemStack plusSmall;
    private final boolean small;
    private final double maximum;
    private final double minimum;

    public DoubleRangeGUI(String option, boolean small, double minValue, double maxValue, Consumer<double[]> consumer) {
        super(18, "&8Parameter &8&l» &8" + option);
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

    public void openInventory(Player player, double valueMin, double valueMax) {
        inventory.setItem(0, filler);
        inventory.setItem(1, small ? filler : getMinusItem(minusBig, true,"Minimum"));
        inventory.setItem(2, getMinusItem(minusSmall, false, "Minimum"));
        inventory.setItem(3, filler);
        inventory.setItem(4, getConfirmItem(confirm, valueMin,"Minimum"));
        inventory.setItem(5, filler);
        inventory.setItem(6, getPlusItem(plusSmall, false,"Minimum"));
        inventory.setItem(7, small ? filler : getPlusItem(plusBig, true,"Minimum"));
        inventory.setItem(8, filler);

        inventory.setItem(9, filler);
        inventory.setItem(10, small ? filler : getMinusItem(minusBig, true,"Maximum"));
        inventory.setItem(11, getMinusItem(minusSmall, false,"Maximum"));
        inventory.setItem(12, filler);
        inventory.setItem(13, getConfirmItem(confirm, valueMax,"Maximum"));
        inventory.setItem(14, filler);
        inventory.setItem(15, getPlusItem(plusSmall, false,"Maximum"));
        inventory.setItem(16, small ? filler : getPlusItem(plusBig, true,"Maximum"));
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
        NamespacedKey keyMin = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Minimum");
        NamespacedKey keyMax = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Maximum");
        double currentMin = Math.round(event.getInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(keyMin, PersistentDataType.DOUBLE) * 10.0) / 10.0;
        double currentMax = Math.round(event.getInventory().getItem(13).getItemMeta().getPersistentDataContainer().get(keyMax, PersistentDataType.DOUBLE) * 10.0) / 10.0;

        boolean shifting = !small && event.isShiftClick();

        switch (event.getSlot()) {
            case 1:
                if(((!shifting && currentMin - 1 <= minimum) || (shifting && currentMin - 5 <= minimum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), minimum,"Minimum"));
                else if (((!shifting && currentMin >= minimum + 1) || (shifting && currentMin >= minimum + 5)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin - 5 : currentMin - 1, "Minimum"));
                break;
            case 2:
                if(((!shifting && currentMin - 0.1 <= minimum) || (shifting && currentMin - 0.5 <= minimum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), minimum,"Minimum"));
                else if ((!shifting && currentMin >= minimum + 0.1) || (shifting && currentMin >= minimum + 0.5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin - 0.5 : currentMin - 0.1, "Minimum"));
                break;
            case 4:
                consumer.accept(new double[]{currentMin, currentMax});
                break;
            case 6:
                if(((!shifting && currentMin + 0.1 >= currentMax) || (shifting && currentMin + 0.5 >= currentMax)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), currentMax,"Minimum"));
                else if ((!shifting && currentMin <= currentMax - 0.1) || (shifting && currentMin <= currentMax - 0.5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin + 0.5 : currentMin + 0.1,"Minimum"));
                break;
            case 7:
                if(((!shifting && currentMin + 1 >= currentMax) || (shifting && currentMin + 5 >= currentMax)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), currentMax,"Minimum"));
                else if (((!shifting && currentMin <= currentMax - 1) || (shifting && currentMin <= currentMax - 5)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? currentMin + 5 : currentMin + 1,"Minimum"));
                break;
            case 10:
                if(((!shifting && currentMax - 1 <= currentMin) || (shifting && currentMax - 5 <= currentMin)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(13), currentMin,"Maximum"));
                else if (((!shifting && currentMax >= currentMin + 1) || (shifting && currentMax >= currentMin + 5)) && !small)
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax - 5 : currentMax - 1,"Maximum"));
                break;
            case 11:
                if(((!shifting && currentMax - 0.1 <= currentMin) || (shifting &&  currentMax - 0.5 <= currentMin)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(13), currentMin, "Maximum"));
                else if ((!shifting && currentMax >= currentMin + 0.1) || (shifting && currentMax >= currentMin + 0.5))
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax - 0.5 : currentMax - 0.1,"Maximum"));
                break;
            case 13:
                consumer.accept(new double[]{currentMin, currentMax});
                break;
            case 15:
                if(((!shifting && currentMax + 0.1 >= maximum) || (shifting && currentMax + 0.5 >= maximum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(13), maximum,"Maximum"));
                else if ((!shifting && currentMax <= maximum - 0.1) || (shifting && currentMax <= maximum - 0.5))
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax + 0.5 : currentMax + 0.1,"Maximum"));
                break;
            case 16:
                if(((!shifting && currentMax + 1 >= maximum) || (shifting && currentMax + 5 >= maximum)) && !small)
                    inventory.setItem(4, getConfirmItem(inventory.getItem(13), maximum,"Maximum"));
                else if (((!shifting && currentMax <= maximum - 1) || (shifting && currentMax <= maximum - 5)) && !small)
                    inventory.setItem(13, getConfirmItem(inventory.getItem(13), shifting ? currentMax + 5 : currentMax + 1,"Maximum"));
                break;
            default:
                break;
        }
    }

    private ItemStack getConfirmItem(ItemStack item, double value, String type) {
        ItemMeta meta = item.getItemMeta();
        value = Math.round(value * 10.0) / 10.0;
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs." + type);
        meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        meta.setDisplayName(Utils.applyFormat("&a&l[+] Confirm"));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(Utils.applyFormat("&eCurrent value: &f" + value));
        itemLore.add("");
        itemLore.add(Utils.applyFormat("&7&o(( Click to confirm! ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return getMenuItem(item, true);
    }

    private ItemStack getMinusItem(ItemStack item, boolean big, String type) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.RangeType");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        if (big) {
            meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove 1"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to remove 1 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove 5 ))"));
        } else {
            meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove 0.1"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to remove 0.1 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove 0.5 ))"));
        }
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return getMenuItem(item, true);
    }

    private ItemStack getPlusItem(ItemStack item, boolean big, String type) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.RangeType");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        if (big) {
            meta.setDisplayName(Utils.applyFormat("&a&l[+] Add 1"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to add 1 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add 5 ))"));
        } else {
            meta.setDisplayName(Utils.applyFormat("&a&l[+] Add 0.1"));
            itemLore.add(Utils.applyFormat("&7&o(( Click to add 0.1 ))"));
            if (!small)
                itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add 0.5 ))"));
        }
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return getMenuItem(item, true);
    }
}
