package ca.pandaaa.custommobs.guis.EditCustomMobs.Others.Spawner;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.custommobs.Spawner;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleIntegerGUI;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class SpawnerGUI extends CustomMobsGUI {
    private final CustomMob customMob;
    private final ItemStack previous;
    private final Spawner spawner;
    private final Manager customMobsManager;

    public SpawnerGUI(CustomMob customMob, Manager customMobsManager) {
        super(36, "&8CustomMobs &8&l» &8Spawners");

        this.customMob = customMob;
        this.customMobsManager = customMobsManager;
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
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
                new IntegerGUI("Spawn Count", customMob, true, 1, 25, (value) -> {
                    spawner.setSpawnCount(value);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, spawner.getSpawnCount());
                break;
            case 11:
                new IntegerGUI("Maximum nearby count", customMob, false, 1, 150, (value) -> {
                    spawner.setMaxNearbyCount(value);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, spawner.getMaxNearbyCount());
                break;
            case 12:
                new IntegerGUI("Spawn range", customMob, true, 0, 128, (value) -> {
                    spawner.setSpawnRange(value);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, spawner.getSpawnRange());
                break;
            case 13:
                new IntegerGUI("Spawn delay", customMob, false, 0, 72000, (value) -> {
                    spawner.setSpawnDelay(value);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, spawner.getSpawnDelay());
                break;
            case 14:
                new DoubleIntegerGUI("Spawn delay range", customMob, false, 1, 72000, (value) -> {
                    spawner.setMinSpawnDelay(value[0]);
                    spawner.setMaxSpawnDelay(value[1]);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, spawner.getMinSpawnDelay(), spawner.getMaxSpawnDelay());
                break;
            case 15:
                new IntegerGUI("Required player range", customMob, false, 0, 128, (value) -> {
                    spawner.setRequiredPlayerRange(value);
                    new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, spawner.getRequiredPlayerRange());
                break;
            case 16:
                spawner.setDisableRequirements(!spawner.getDisableRequirements());
                new SpawnerGUI(customMob, customMobsManager).openInventory((Player) event.getWhoClicked());
                break;

            case 27:
                new EditGUI(customMob,customMobsManager, clicker.getPlayer()).openInventory();
                break;
            case 31:
                if (cursorItem.getType() != Material.AIR) {
                    ItemStack spawner = item.clone();
                    spawner.setAmount(1);
                    customMob.setSpawnerItem(spawner);
                    event.getInventory().setItem(50, getMenuItem(getSpawnerItem(), false));
                    clicker.setItemOnCursor(null);
                } else {
                    clicker.setItemOnCursor(customMobsManager.getCustomMobItem(customMob, "spawner", event.isShiftClick() ? 64 : 1));
                }
                break;
        }

    }

    private ItemStack getSpawnerItem() {
        CustomMobsItem item = new CustomMobsItem(customMob.getSpawnerItem().getType());
        item.setName(Utils.applyFormat("&6&l" + Utils.getSentenceCase(customMob.getType() + " spawner")));
        item.addLore("");
        item.addLore(Utils.applyFormat("&7&o(( Left-Click to get the current item ))"));
        item.addLore(Utils.applyFormat("&7&o(( Shift-Left-Click to get the current item (x64) ))"));
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnCountItem() {
        CustomMobsItem item = new CustomMobsItem(Material.HEAVY_CORE);
        item.setName("&6&lSpawn count");
        item.addLore("&eSpawn count:&f " + spawner.getSpawnCount(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getMaxNearbyCountItem() {
        CustomMobsItem item = new CustomMobsItem(Material.COCOA_BEANS);
        item.setName("&6&lNearby count");
        item.addLore("&eMax:&f " + spawner.getMaxNearbyCount(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.FIRE_CHARGE);
        item.setName("&6&lSpawn range");
        item.addLore("&eSpawn range:&f " + spawner.getSpawnRange(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnDelayItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CHAIN);
        item.setName("&6&lSpawn delay");
        item.addLore("&eSpawn delay:&f " + spawner.getSpawnDelay(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSpawnDelayRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&6&lRange of spawn delay");
        item.addLore("&eBetween:&f " + spawner.getMinSpawnDelay()+ " and " + spawner.getMaxSpawnDelay(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getRequiredPlayerRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPYGLASS);
        item.setName("&6&lRequired player range");
        item.addLore("&eRange:&f " + spawner.getRequiredPlayerRange(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getDisableRequirementsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LIGHT);
        item.setName("&6&lSpawner requirements");
        String spawnerRequirements = spawner.getDisableRequirements() ? "&a&lOn" : "&c&lOff";
        item.addLore("&6&lRequirements:&f " + spawnerRequirements, " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

}