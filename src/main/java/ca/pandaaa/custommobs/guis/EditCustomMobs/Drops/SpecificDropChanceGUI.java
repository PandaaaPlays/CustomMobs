package ca.pandaaa.custommobs.guis.EditCustomMobs.Drops;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
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
import java.util.function.Consumer;

public class SpecificDropChanceGUI extends CustomMobsGUI {

    private final Consumer<Double> consumer;
    private final CustomMob customMob;
    private final int dropIndex;
    private final ItemStack minusSuperBig;
    private final ItemStack minusBig;
    private final ItemStack minusSmall;
    private final ItemStack minusSuperSmall;
    private final ItemStack confirm;
    private final ItemStack plusSuperBig;
    private final ItemStack plusBig;
    private final ItemStack plusSmall;
    private final ItemStack plusSuperSmall;
    private final Drop dropItem;
    private final double maximum = 100;
    private final double minimum = 0.001;

    public SpecificDropChanceGUI(CustomMob customMob, Drop dropItem, int dropIndex, Consumer<Double> consumer) {
        super(9, "&8CustomMobs &8&l» &8Drop chance");
        this.consumer = consumer;
        minusSuperBig = getMenuItem(Utils.createHead("a8c67fed7a2472b7e9afd8d772c13db7b82c32ceeff8db977474c11e4611"), true);
        minusBig = getMenuItem(Utils.createHead("7b75ea4d37fca4bfa8f6a3ce56d1473607073a456d33b1227435b99ad71d147"), true);
        minusSmall = getMenuItem(Utils.createHead("cc8e7d46d693341f91d286726f2555ef15514e3460b275e9747842bc9e53df"), true);
        minusSuperSmall = getMenuItem(Utils.createHead("e6a8464990cd7666811d6f292fe869692a184e0c7446fa587ed1c9b1fea724"), true);
        confirm = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
        plusSuperBig = getMenuItem(Utils.createHead("0a21eb4c57750729a48b88e9bbdb987eb6250a5bc2157b59316f5f1887db5"), true);
        plusBig = getMenuItem(Utils.createHead("55d58de8eb283eed0e4122f1c5113d0c3a229eec5b6d7c1481dd23520bc45db"), true);
        plusSmall = getMenuItem(Utils.createHead("69b861aabb316c4ed73b4e5428305782e735565ba2a053912e1efd834fa5a6f"), true);
        plusSuperSmall = getMenuItem(Utils.createHead("5a51a58967f3b7af026c9c289dfdca4c436268575166954a694921541ec0fc"), true);
        this.dropItem = dropItem;
        this.customMob = customMob;
        this.dropIndex = dropIndex;
    }

    public void openInventory(Player player) {
        inventory.setItem(0, getMinusItem(minusSuperBig, 1));
        inventory.setItem(1, getMinusItem(minusBig, 0.1));
        inventory.setItem(2, getMinusItem(minusSmall, 0.01));
        inventory.setItem(3, getMinusItem(minusSuperSmall, 0.001));
        inventory.setItem(4, getConfirmItem(confirm, dropItem.getProbability()));
        inventory.setItem(5, getPlusItem(plusSuperSmall, 0.001));
        inventory.setItem(6, getPlusItem(plusSmall, 0.01));
        inventory.setItem(7, getPlusItem(plusBig, 0.1));
        inventory.setItem(8, getPlusItem(plusSuperBig, 1));

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

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Drop.Chance");
        double current = Math.round(event.getInventory().getItem(4).getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.DOUBLE) * 1000.0) / 1000.0;
        boolean shifting = event.isShiftClick();

        switch (event.getSlot()) {
            case 0:
                if (((!shifting && current >= minimum + 1) || (shifting && current >= minimum + 5)))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 5 : current - 1));
                break;
            case 1:
                if (((!shifting && current >= minimum + 0.1) || (shifting && current >= minimum + 0.5)))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 0.5 : current - 0.1));
                break;
            case 2:
                if (((!shifting && current >= minimum + 0.01) || (shifting && current >= minimum + 0.05)))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 0.05 : current - 0.01));
                break;
            case 3:
                if (((!shifting && current >= minimum + 0.001) || (shifting && current >= minimum + 0.005)))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current - 0.005 : current - 0.001));
                break;
            case 4:
                consumer.accept(current);
                new SpecificDropGUI(customMob, dropItem, dropIndex).openInventory((Player) event.getWhoClicked());
                break;
            case 5:
                if ((!shifting && current <= maximum - 0.001) || (shifting && current <= maximum - 0.005))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 0.005 : current + 0.001));
                break;
            case 6:
                if ((!shifting && current <= maximum - 0.01) || (shifting && current <= maximum - 0.05))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 0.05 : current + 0.01));
                break;
            case 7:
                if ((!shifting && current <= maximum - 0.1) || (shifting && current <= maximum - 0.5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 0.5 : current + 0.1));
                break;
            case 8:
                if ((!shifting && current <= maximum - 1) || (shifting && current <= maximum - 5))
                    inventory.setItem(4, getConfirmItem(inventory.getItem(4), shifting ? current + 5 : current + 1));
                break;
            default:
                break;
        }
    }


    private ItemStack getConfirmItem(ItemStack item, Double value) {
        ItemMeta meta = item.getItemMeta();
        value = Math.round(value * 1000.0) / 1000.0;
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Drop.Chance");
        meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        meta.setDisplayName(Utils.applyFormat("&a&l[+] Confirm"));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(Utils.applyFormat("&eCurrent value: &f" + value + " %"));
        itemLore.add("");
        itemLore.add(Utils.applyFormat("&7&o(( Click to confirm! ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getMinusItem(ItemStack item, double size) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        meta.setDisplayName(Utils.applyFormat("&c&l[-] Remove " + size));
        itemLore.add(Utils.applyFormat("&7&o(( Click to remove " + size + " ))"));
        itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to remove " + size * 5 + " ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPlusItem(ItemStack item, double size) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        meta.setDisplayName(Utils.applyFormat("&a&l[+] Add " + size));
        itemLore.add(Utils.applyFormat("&7&o(( Click to add " + size + " ))"));
        itemLore.add(Utils.applyFormat("&7&o(( Shift-Click to add " + size * 5 + " ))"));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }
}
