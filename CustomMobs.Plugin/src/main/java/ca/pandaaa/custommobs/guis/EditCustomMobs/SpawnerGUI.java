package ca.pandaaa.custommobs.guis.EditCustomMobs;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.custommobs.Spawner;
import ca.pandaaa.custommobs.guis.BasicTypes.TimeIntegerRangeGUI;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SpawnerGUI extends CustomMobsGUI {
    private final CustomMob customMob;
    private final Spawner spawner;
    private final Manager customMobsManager;

    public SpawnerGUI(CustomMob customMob, Manager customMobsManager) {
        super(36, "&8CustomMobs &8&l» &8Spawner");

        this.customMob = customMob;
        this.customMobsManager = customMobsManager;
        spawner = customMob.getSpawner();
    }

    public void openInventory(Player player) {

        for(int i = 0; i < 36; i++)
            inventory.setItem(i, filler);


        inventory.setItem(10, getSpawnCountItem());
        inventory.setItem(11, getMaxNearbyCountItem());
        inventory.setItem(12, getSpawnRangeItem());
        inventory.setItem(13, getSpawnDelayItem());
        inventory.setItem(14, getSpawnDelayRangeItem());
        inventory.setItem(15, getRequiredPlayerRangeItem());
        inventory.setItem(16, getDisableRequirementsItem());

        inventory.setItem(27, getPreviousItem());
        inventory.setItem(31, getSpawnerItem());

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

        Player clicker = (Player) event.getWhoClicked();
        ItemStack cursorItem = clicker.getItemOnCursor();
        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null)
            return;
        switch (event.getSlot()) {
            case 10:
                if(event.isRightClick()) {
                    spawner.setSpawnCount(4);
                    customMob.setSpawner(spawner);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                } else {
                    new IntegerGUI("Spawn Count", true, 1, 25, (value) -> {
                        spawner.setSpawnCount(value);
                        customMob.setSpawner(spawner);
                        new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker, spawner.getSpawnCount());
                }
                break;
            case 11:
                if(event.isRightClick()) {
                    spawner.setMaxNearbyCount(6);
                    customMob.setSpawner(spawner);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                } else {
                    new IntegerGUI("Maximum nearby count", false, 1, 150, (value) -> {
                        spawner.setMaxNearbyCount(value);
                        customMob.setSpawner(spawner);
                        new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker, spawner.getMaxNearbyCount());
                }
                break;
            case 12:
                if(event.isRightClick()) {
                    spawner.setSpawnRange(4);
                    customMob.setSpawner(spawner);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                } else {
                    new IntegerGUI("Spawn range", true, 0, 128, (value) -> {
                        spawner.setSpawnRange(value);
                        customMob.setSpawner(spawner);
                        new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker, spawner.getSpawnRange());
                }
                break;
            case 13:
                if(event.isRightClick()) {
                    spawner.setSpawnDelay(0);
                    customMob.setSpawner(spawner);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                } else {
                    new IntegerGUI("Spawn delay", false, 0, 72000, (value) -> {
                        spawner.setSpawnDelay(value);
                        customMob.setSpawner(spawner);
                        new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker, spawner.getSpawnDelay());
                }
                break;
            case 14:
                if(event.isRightClick()) {
                    spawner.setMinSpawnDelay(200);
                    spawner.setMaxSpawnDelay(799);
                    customMob.setSpawner(spawner);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                } else {
                    new TimeIntegerRangeGUI("Spawn delay range", false, 1, 72000, (value) -> {
                        spawner.setMinSpawnDelay(value[0]);
                        spawner.setMaxSpawnDelay(value[1]);
                        customMob.setSpawner(spawner);
                        new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker, spawner.getMinSpawnDelay(), spawner.getMaxSpawnDelay());
                }
                break;
            case 15:
                if(event.isRightClick()) {
                    spawner.setRequiredPlayerRange(16);
                    customMob.setSpawner(spawner);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                } else {
                    new IntegerGUI("Required player range", false, 0, 128, (value) -> {
                        spawner.setRequiredPlayerRange(value);
                        customMob.setSpawner(spawner);
                        new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker, spawner.getRequiredPlayerRange());
                }
                break;
            case 16:
                spawner.setDisableRequirements(!spawner.areRequirementsDisabled());
                customMob.setSpawner(spawner);
                new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                break;

            case 27:
                new EditGUI(customMob,customMobsManager, clicker.getPlayer()).openInventory();
                break;

            case 31:
                if (event.isRightClick()) {
                    customMob.setSpawnerItem(null);
                    event.getInventory().setItem(31, getMenuItem(getSpawnerItem(), false));
                } else if (cursorItem.getType() != Material.AIR) {
                    ItemStack spawner = cursorItem.clone();
                    spawner.setAmount(1);
                    customMob.setSpawnerItem(spawner);
                    event.getInventory().setItem(31, getMenuItem(getSpawnerItem(), false));
                    clicker.setItemOnCursor(null);
                } else {
                    clicker.setItemOnCursor(customMobsManager.getCustomMobItem(customMob, "spawner", event.isShiftClick() ? 64 : 1));
                }
                break;
        }
    }

    private ItemStack getSpawnerItem() {
        CustomMobsItem item = new CustomMobsItem(customMob.getSpawnerItem());
        item.setName(Utils.applyFormat("&6&l" + Utils.getSentenceCase(customMob.getType() + " spawner")));
        item.addLore("");
        item.addLore("&7&o(( Drag & drop an item to change this item ))");
        item.addLore("&7&o(( Left-Click to get the current item ))");
        item.addLore("&7&o(( Shift-Left-Click to get the current item (x64) ))");
        item.addLore("&7&o(( Right-Click to delete (reset) the current item ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnCountItem() {
        CustomMobsItem item = new CustomMobsItem(Material.HEAVY_CORE);
        item.setName("&6&lMaximum spawn count");
        item.addLore("&eSpawn count:&f 1-" + spawner.getSpawnCount(), " ", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getMaxNearbyCountItem() {
        CustomMobsItem item = new CustomMobsItem(Material.COCOA_BEANS);
        item.setName("&6&lNearby count");
        item.addLore("&eMaximum amount:&f " + spawner.getMaxNearbyCount(), " ", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.FIRE_CHARGE);
        item.setName("&6&lSpawn range");
        item.addLore("&eRange:&f " + spawner.getSpawnRange() + " block(s)", " ", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnDelayItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CHAIN);
        item.setName("&6&lInitial spawn delay");
        String delay = spawner.getSpawnDelay() > 20 ? "(" + Utils.getFormattedTime(spawner.getSpawnDelay() / 20, true, false) + ")" : "";
        item.addLore("&eFirst spawn delay:&f " + spawner.getSpawnDelay() + " tick(s) " + delay, " ", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnDelayRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&6&lSpawn delay");
        String minDelay = spawner.getMinSpawnDelay() > 20 ? "(" + Utils.getFormattedTime(spawner.getMinSpawnDelay() / 20, true, false) + ")" : "";
        String maxDelay = spawner.getMaxSpawnDelay() > 20 ? "(" + Utils.getFormattedTime(spawner.getMaxSpawnDelay() / 20, true, false) + ")" : "";
        item.addLore("&eRange:&f " + spawner.getMinSpawnDelay() + " tick(s) " + minDelay + " - " + spawner.getMaxSpawnDelay() + " tick(s) " + maxDelay, "", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getRequiredPlayerRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPYGLASS);
        item.setName("&6&lRequired player range");
        item.addLore("&eRange:&f " + spawner.getRequiredPlayerRange() + " block(s)", " ", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getDisableRequirementsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LIGHT);
        item.setName("&6&lSpawner requirements");
        String spawnerRequirements = !spawner.areRequirementsDisabled() ? "&a&lOn" : "&c&lOff";
        item.addLore("&eRequirements:&f " + spawnerRequirements, " ", "&7&o(( Enabling the requirements will default to the spawner's", "&7&odefault behaviors (e.g.: require darkness, grass, etc.) ))", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

}