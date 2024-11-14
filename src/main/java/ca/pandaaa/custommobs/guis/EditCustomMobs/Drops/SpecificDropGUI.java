package ca.pandaaa.custommobs.guis.EditCustomMobs.Drops;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.custommobs.options.NextOptions;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.DropConditions;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpecificDropGUI extends CustomMobsGUI {

    private final Drop drop;
    private final CustomMob customMob;
    private final int dropIndex;

    public SpecificDropGUI(CustomMob customMob, Drop drop, int dropIndex) {
        super(36, "&8CustomMobs &8&l» &8Drop configuration");
        this.customMob = customMob;
        this.drop = drop;
        this.dropIndex = dropIndex;
    }

    public void openInventory(Player player) {
        for(int i = 0; i < 36; i++)
            inventory.setItem(i, filler);

        inventory.setItem(10, getChanceItem());
        inventory.setItem(11, getLootingItem());
        inventory.setItem(12, getCommandItem());
        inventory.setItem(14, getConditionItem());
        inventory.setItem(15, getGroupItem(drop.getDropCondition() == DropConditions.NEARBY));
        inventory.setItem(16, getMessageItem());
        inventory.setItem(27, getPreviousItem());
        inventory.setItem(31, getDropItem());
        inventory.setItem(35, getDeleteItem(false));

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
        ItemStack cursorItem = clicker.getItemOnCursor();

        switch (event.getSlot()) {
            case 10:
                new SpecificDropChanceGUI(customMob, drop, dropIndex, (value) -> {
                    drop.setProbability(value);
                    customMob.editDrop(drop, dropIndex);
                }).openInventory(clicker);
                break;
            case 11:
                drop.setLooting(!drop.isLooting());
                customMob.editDrop(drop, dropIndex);
                event.getView().setItem(event.getSlot(), getLootingItem());
                break;
            case 14:
                drop.setDropCondition(DropConditions.getNextCondition(drop.getDropCondition()));
                customMob.editDrop(drop, dropIndex);
                event.getView().setItem(event.getSlot(), getConditionItem());
                event.getView().setItem(event.getSlot() + 1, getGroupItem(drop.getDropCondition() == DropConditions.NEARBY));
                break;
            case 15:
                if(event.getClick().isRightClick())
                    drop.setGroupColor(null);
                else
                    drop.setGroupColor(NextOptions.getNextDyeColor(drop.getGroupColor()));

                customMob.editDrop(drop, dropIndex);
                event.getView().setItem(event.getSlot(), getGroupItem(false));
                break;
            case 16:
                new SpecificDropMessageGUI().openInventory(clicker);
                break;
            case 27:
                new DropsGUI(customMob).openInventory(clicker, 1);
                break;
            case 31:
                if (cursorItem.getType() != Material.AIR) {
                    drop.setItemStack(cursorItem.clone());
                    customMob.editDrop(drop, dropIndex);
                    event.getInventory().setItem(31, getDropItem());
                    clicker.setItemOnCursor(null);
                } else {
                    clicker.setItemOnCursor(drop.getItemStack());
                }
                break;
            case 35:
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) {
                    customMob.removeDropItem(dropIndex);
                    new DropsGUI(customMob).openInventory(clicker, 1);
                } else {
                    inventory.setItem(35, getDeleteItem(true));

                    for(int i = 0; i < 36; i++) {
                        if (inventory.getItem(i).getType() == Material.GRAY_STAINED_GLASS_PANE)
                            inventory.getItem(i).setType(Material.RED_STAINED_GLASS_PANE);
                    }
                }
                break;
        }
    }

    private ItemStack getChanceItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BLAZE_POWDER);
        item.setName("&d&lProbability");
        item.addLore("&eCurrent proability:&f " + drop.getProbability() + "%", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getLootingItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WITHER_SKELETON_SKULL);
        String looting = drop.isLooting() ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lLooting");
        item.addLore("&eAffected by looting: " + looting, "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getCommandItem() {
        CustomMobsItem item = new CustomMobsItem(Material.COMMAND_BLOCK);
        item.setName("&4&lCommands");
        // TODO cycle the NEARBY, KILLER, MOST DAMAGE
        item.addLore("", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getConditionItem() {
        CustomMobsItem item = new CustomMobsItem(Material.MACE);
        item.setName("&c&lCondition");
        String dropCondtionName = drop.getDropCondition().name();
        item.addLore("&eCondition: &f"
                + dropCondtionName.toUpperCase().charAt(0)
                + dropCondtionName.toLowerCase().substring(1).replaceAll("_", " "));
        item.addLore("", "&7&o(( Click to cycle this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getGroupItem(boolean nearby) {
        CustomMobsItem item = new CustomMobsItem(Material.CANDLE);
        item.setName("&a&lGroup");
        String color = "&fNone";
        if(drop.getGroupColor() != null) {
            color = Utils.getChatColorOfColor(drop.getGroupColor().name())
                    + drop.getGroupColor().name().toUpperCase().substring(0, 1)
                    + drop.getGroupColor().name().toLowerCase().substring(1).replaceAll("_", " ");
            Material material = Material.getMaterial(drop.getGroupColor() + "_CANDLE");
            item.setType(material);
        }
        // We do not have a choice to have different groups for Nearby, as it would not make sense to have a player cancelling the whole dropping for others because they dropped an item.
        if(nearby)
            item.addLore("&eNearby group: &f" + color);
        else
            item.addLore("&eGroup: &f" + color);
        item.addLore("", "&7&o(( Only one drop of this group will be dropped on mob death ))");
        item.addLore("", "&7&o(( Left-Click to cycle this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getMessageItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WRITABLE_BOOK);
        item.setName("&6&lMessages");
        item.addLore("", "&7&o(( Click to edit this drop's message(s) ))");
        return getMenuItem(item, true);
    }

    private ItemStack getDropItem() {
        ItemStack item = drop.getItemStack().clone();
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Drag & drop an item to change this item ))"));
        lore.add(Utils.applyFormat("&7&o(( Left-Click to get the current item ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return getMenuItem(item, false);
    }

    private ItemStack getDeleteItem(boolean confirm) {
        CustomMobsItem item = new CustomMobsItem(Material.BARRIER);
        item.addLore("");
        if (confirm) {
            item.setName("&c&l[-] Confirm drop deletion");
            item.addLore("&7&o(( Click again to confirm the deletion ))");
        } else
            item.setName("&c&l[-] Delete drop");
        item.addLore("&c&l[!] &cThis will permanently delete this drop.");
        return getMenuItem(item, true);
    }

}
