package ca.pandaaa.custommobs.guis.EditCustomMobs.Others;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class OthersGUI extends CustomMobsGUI {

    private final CustomMob customMob;

    public OthersGUI(CustomMob customMob) {
        super(27, "&8CustomMobs &8&l» &8Others");
        this.customMob = customMob;
    }

    public void openInventory(Player player) {
        for(int i = 0; i < 27; i++)
            inventory.setItem(i, filler);

        inventory.setItem(12, getPotionsItem());
        inventory.setItem(13, getSoundsItem());
        inventory.setItem(14, getMessageItem());
        inventory.setItem(18, getPreviousItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory()))
            return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null)
            return;
        Player clicker = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 12:
                // TODO Potions
                break;
            case 13:
                // TODO Sounds
                break;
            case 14:
                new MessagesGUI(customMob).openInventory(clicker);
                break;
            case 18:
                new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                break;
        }
    }

    private ItemStack getPotionsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.OMINOUS_BOTTLE);
        item.setName("&6&lPotions");
        item.addLore("", "&7&o(( Click to edit this CustomMob's potion effect(s) ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSoundsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NOTE_BLOCK);
        item.setName("&6&lSounds");
        item.addLore("", "&7&o(( Click to edit this CustomMob's spawn sound(s) ))");
        return getMenuItem(item, true);
    }

    private ItemStack getMessageItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WRITABLE_BOOK);
        item.setName("&6&lMessages");
        item.addLore("", "&7&o(( Click to edit this CustomMob's message(s) ))");
        return getMenuItem(item, true);
    }
}
