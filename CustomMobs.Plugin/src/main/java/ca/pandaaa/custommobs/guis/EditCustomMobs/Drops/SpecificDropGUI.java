package ca.pandaaa.custommobs.guis.EditCustomMobs.Drops;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.DropConditions;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
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

        inventory.setItem(11, getChanceItem());
        inventory.setItem(12, getLootingItem());
        inventory.setItem(13, getConditionItem());
        inventory.setItem(14, getGroupItem(drop.getDropCondition() == DropConditions.NEARBY));
        inventory.setItem(15, getMessageItem());
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
            case 11:
                new SpecificDropChanceGUI(customMob, drop, dropIndex, (value) -> {
                    drop.setProbability(value);
                    customMob.editDrop(drop, dropIndex);
                }).openInventory(clicker);
                break;
            case 12:
                drop.setLooting(!drop.isLooting());
                customMob.editDrop(drop, dropIndex);
                event.getView().setItem(event.getSlot(), getLootingItem());
                event.getView().setItem(event.getSlot() - 1, getChanceItem());
                break;
            case 13:
                if(event.isRightClick() && drop.getDropCondition().name().equals("NEARBY")){
                    new DoubleGUI("Maximum range", false, 1, 255, (value) -> {
                        drop.setNearbyRange(value);
                        customMob.editDrop(drop, dropIndex);
                        new SpecificDropGUI(customMob, drop, dropIndex).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker, drop.getNearbyRange());
                    break;
                }
                drop.setDropCondition(DropConditions.getNextCondition(drop.getDropCondition()));
                customMob.editDrop(drop, dropIndex);
                event.getView().setItem(event.getSlot(), getConditionItem());
                event.getView().setItem(event.getSlot() + 1, getGroupItem(drop.getDropCondition() == DropConditions.NEARBY));
                break;
            case 14:
                if(event.isRightClick())
                    drop.setGroupColor(null);
                else {
                    List<DyeColor> colors = Arrays.asList(DyeColor.values());

                    if (colors.indexOf(drop.getGroupColor()) == colors.size() - 1)
                        drop.setGroupColor(colors.get(0));
                    else
                        drop.setGroupColor(colors.get(colors.indexOf(drop.getGroupColor()) + 1));
                }

                customMob.editDrop(drop, dropIndex);
                event.getView().setItem(event.getSlot(), getGroupItem(false));
                break;
            case 15:
                new DropMessagesGUI(customMob, drop, "Drop", clicker).openInventory();
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
        item.addLore("&eCurrent proability:&f " + drop.getProbability() + "%");
        if(drop.isLooting()) {
            item.addLore("&f&l* &cWith looting I:&f " + Math.round(drop.getProbability() * 2 * 1000.0) / 1000.0 + "%");
            item.addLore("&f&l* &cWith looting II:&f " + Math.round(drop.getProbability() * 3 * 1000.0) / 1000.0 + "%");
            item.addLore("&f&l* &cWith looting III:&f " + Math.round(drop.getProbability() * 4 * 1000.0) / 1000.0 + "%");
        }
        item.addLore("", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getLootingItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WITHER_SKELETON_SKULL);
        String looting = drop.isLooting() ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lLooting");;
        item.addLore("&eAffected by looting: " + looting, "", "&7&o(( Adds (probability * looting level) to the probability ))", "", "&7&o(( Click to edit this option ))");
        if(drop.getDropCondition() == DropConditions.NEARBY)
            item.addLore("&c&l[!] &cNearby drops cannot be affected by looting.");
        return getMenuItem(item, true);
    }

    private ItemStack getConditionItem() {
        CustomMobsItem item = new CustomMobsItem(Material.MACE);
        item.setName("&c&lCondition");
        String dropCondtionName = drop.getDropCondition().name();
        item.addLore("&eCondition: &f" + Utils.getStartCase(dropCondtionName));
        switch (drop.getDropCondition().name()){
            case "NEARBY":
                item.addLore("&f&l* &bRange:&f " + drop.getNearbyRange());
                item.addLore("","&7&o(( Only nearby players have a chance to receive this drop ))");
                break;
            case "KILLER":
                item.addLore("","&7&o(( Only the killer has a chance to receive this drop ))");
                break;
            case "DROP":
                item.addLore("","&7&o(( This drop has a chance of dropping on the ground ))");
                break;
            case "MOST_DAMAGE":
                item.addLore("","&7&o(( Only the player that dealt the most damage has a chance to receive this drop ))");
                break;
        }
        item.addLore("", "&7&o(( Click to cycle this option ))");
        if(drop.getDropCondition().name().equals("NEARBY"))
            item.addLore("&7&o(( Right-Click to modify range value ))");
        return getMenuItem(item, true);
    }

    private ItemStack getGroupItem(boolean nearby) {
        CustomMobsItem item = new CustomMobsItem(Material.CANDLE);
        item.setName("&a&lGroup");
        String color = "&fNone";
        if(drop.getGroupColor() != null) {
            color = Utils.getChatColorOfColor(drop.getGroupColor().name()) + Utils.getSentenceCase(drop.getGroupColor().toString());
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
