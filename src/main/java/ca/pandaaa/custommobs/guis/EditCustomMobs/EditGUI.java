package ca.pandaaa.custommobs.guis.EditCustomMobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Equipment;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.Drops.DropsGUI;
import ca.pandaaa.custommobs.guis.MainGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class EditGUI extends CustomMobsGUI {

    private final ItemStack mainHand;
    private final ItemStack offHand;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack types;
    private final ItemStack options;
    private final ItemStack drops;
    private final ItemStack name;
    private final ItemStack previous;
    private final ItemStack item;
    private final ItemStack spawner;
    private final ItemStack delete;
    private final CustomMob customMob;
    private final Manager customMobsManager;
    private final Player player;

    private boolean waitingForRename;

    public EditGUI(CustomMob customMob, Manager customMobsManager, Player player) {
        super(54, "&8CustomMobs &8&l» &8"
                + customMob.getCustomMobFileName().replace(".yml", "").substring(0, 1).toUpperCase()
                + customMob.getCustomMobFileName().replace(".yml", "").substring(1).toLowerCase());

        this.customMob = customMob;
        this.player = player;

        this.mainHand = getMenuItem(new ItemStack(Material.NETHERITE_SWORD), true);
        this.offHand = getMenuItem(new ItemStack(Material.SHIELD), true);
        this.helmet = getMenuItem(new ItemStack(Material.NETHERITE_HELMET), true);
        this.chestplate = getMenuItem(new ItemStack(Material.NETHERITE_CHESTPLATE), true);
        this.leggings = getMenuItem(new ItemStack(Material.NETHERITE_LEGGINGS), true);
        this.boots = getMenuItem(new ItemStack(Material.NETHERITE_BOOTS), true);
        this.types = getMenuItem(new ItemStack(Material.ALLAY_SPAWN_EGG), true);
        this.options = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
        this.name = getMenuItem(new ItemStack(Material.SPRUCE_HANGING_SIGN), true);
        this.drops = getMenuItem(new ItemStack(Material.PITCHER_POD), true);
        this.previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        this.item = getMenuItem(customMob.getItem(), true);
        this.spawner = getMenuItem(customMob.getSpawnerItem(), true);
        this.delete = getMenuItem(new ItemStack(Material.BARRIER), true);

        this.customMobsManager = customMobsManager;
        this.waitingForRename = false;
    }

    public void openInventory() {
        for(int i = 0; i < 54; i++)
            inventory.setItem(i, filler);

        // Setting the main hand item
        inventory.setItem(10, getHandItem(mainHand, true));

        // Setting the offhand item
        inventory.setItem(11, getHandItem(offHand, false));

        // Setting the helmet item
        inventory.setItem(13, getArmorItem(helmet, "Helmet"));

        // Setting the chestplate item
        inventory.setItem(14, getArmorItem(chestplate, "Chestplate"));

        // Setting the leggings item
        inventory.setItem(15, getArmorItem(leggings, "Leggings"));

        // Setting the boots item
        inventory.setItem(16, getArmorItem(boots, "Boots"));

        // Setting the type item
        inventory.setItem(28, getTypesItem(types));

        // Setting the variant item
        inventory.setItem(29, getOptionsItem(options));

        // Renaming item (prompts for name)
        inventory.setItem(30, getRenameItem(name));

        // Drops item
        inventory.setItem(31, getDropsItem(drops));

        // Spawner item
        inventory.setItem(32, getSpawnerItem());

        // Custom effects item
        inventory.setItem(33, getCustomEffectsItem());

        // Other item
        inventory.setItem(34, getOtherItem());

        // Previous page (back to main menu)
        inventory.setItem(45, getPreviousItem(previous));

        // Get the item (Shift click for 64)
        inventory.setItem(48, getGiveItem(item));

        // Spawner item (Shift click for 64)
        inventory.setItem(50, getGiveItem(spawner));

        // Delete item (prompt for confirm)
        ItemMeta deleteItemMeta = delete.getItemMeta();
        if(deleteItemMeta != null)
            deleteItemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Delete CustomMob"));
        delete.setItemMeta(deleteItemMeta);
        inventory.setItem(53, getDeleteItem(delete, false));

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

        Player clicker = (Player) event.getWhoClicked();
        ItemStack cursorItem = clicker.getItemOnCursor();
        Equipment equipment = customMob.getEquipment();

        switch (event.getSlot()) {
            case 10:
                if (event.isRightClick())
                    equipment.setMainHand(null);
                else if (cursorItem.getType() != Material.AIR) {
                    equipment.setMainHand(cursorItem.clone());
                    clicker.setItemOnCursor(null);
                } else
                    clicker.setItemOnCursor(equipment.getMainHand());
                break;
            case 11:
                if (event.isRightClick())
                    equipment.setOffHand(null);
                else if (cursorItem.getType() != Material.AIR) {
                    equipment.setOffHand(cursorItem.clone());
                    clicker.setItemOnCursor(null);
                } else
                    clicker.setItemOnCursor(equipment.getOffHand());
                break;
            case 13:
                if (event.isRightClick())
                    equipment.setHelmet(null);
                else if (cursorItem.getType() != Material.AIR) {
                    equipment.setHelmet(cursorItem.clone());
                    clicker.setItemOnCursor(null);
                } else
                    clicker.setItemOnCursor(equipment.getHelmet());
                break;
            case 14:
                if (event.isRightClick())
                    equipment.setChestplate(null);
                else if (cursorItem.getType() != Material.AIR) {
                    equipment.setChestplate(cursorItem.clone());
                    clicker.setItemOnCursor(null);
                } else
                    clicker.setItemOnCursor(equipment.getChestplate());
                break;
            case 15:
                if (event.isRightClick())
                    equipment.setLeggings(null);
                else if (cursorItem.getType() != Material.AIR) {
                    equipment.setLeggings(cursorItem.clone());
                    clicker.setItemOnCursor(null);
                } else
                    clicker.setItemOnCursor(equipment.getLeggings());
                break;
            case 16:
                if (event.isRightClick())
                    equipment.setBoots(null);
                else if (cursorItem.getType() != Material.AIR) {
                    equipment.setBoots(cursorItem.clone());
                    clicker.setItemOnCursor(null);
                } else
                    clicker.setItemOnCursor(equipment.getBoots());
                break;
            case 28:
                new TypesGUI(customMob).openInventory(clicker, 1);
                break;
            case 29:
                new OptionsGUI(customMob).openInventory(clicker, 1);
                break;
            case 30:
                clicker.closeInventory();
                clicker.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eCustomMob renaming"));
                clicker.sendMessage(Utils.applyFormat(" &6&l- &fEnter the name you want &e&nin the chat&f."));
                clicker.sendMessage(Utils.applyFormat(" &6&l- &fThe naming supports &4c&co&6l&eo&ar&bs&f ( &* (&ebasic&f) and &#* (&#cbff0fhex&f) )"));
                clicker.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel renaming."));
                waitingForRename = true;
                break;
            case 31:
                new DropsGUI(customMob).openInventory(clicker, 1);
                break;
            case 32:
                new SpawnerGUI(customMob, customMobsManager).openInventory(clicker);
                break;
            case 33:
                // TODO Special effects
                break;
            case 34:
                new OthersGUI(customMob).openInventory(clicker);
                break;
            case 45:
                new MainGUI().openInventory(clicker, 1);
                break;
            case 48:
                if (event.isRightClick()) {
                    customMob.setItem(null);
                    event.getInventory().setItem(48, getMenuItem(getGiveItem(customMob.getItem()), false));
                } else if (cursorItem.getType() != Material.AIR) {
                    ItemStack changeItem = cursorItem.clone();
                    changeItem.setAmount(1);
                    customMob.setItem(changeItem);
                    event.getInventory().setItem(48, getMenuItem(getGiveItem(customMob.getItem()), false));
                    clicker.setItemOnCursor(null);
                } else {
                    clicker.setItemOnCursor(customMobsManager.getCustomMobItem(customMob, "item", event.isShiftClick() ? 64 : 1));
                }
                break;
            case 50:
                if (event.isRightClick()) {
                    customMob.setSpawnerItem(null);
                    event.getInventory().setItem(50, getMenuItem(getGiveItem(customMob.getSpawnerItem()), false));
                } else if (cursorItem.getType() != Material.AIR) {
                    ItemStack spawner = cursorItem.clone();
                    spawner.setAmount(1);
                    customMob.setSpawnerItem(spawner);
                    event.getInventory().setItem(50, getMenuItem(getGiveItem(customMob.getSpawnerItem()), false));
                    clicker.setItemOnCursor(null);
                } else {
                    clicker.setItemOnCursor(customMobsManager.getCustomMobItem(customMob, "spawner", event.isShiftClick() ? 64 : 1));
                }
                break;
            case 53:
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) {
                    customMob.delete();
                    customMobsManager.removeCustomMob(customMob.getCustomMobFileName().replaceAll(".yml", ""));
                    new MainGUI().openInventory(player, 1);
                } else {
                    inventory.setItem(53, getDeleteItem(delete, true));

                    for(int i = 0; i < 54; i++) {
                        if (inventory.getItem(i).getType() == Material.GRAY_STAINED_GLASS_PANE)
                            inventory.getItem(i).setType(Material.RED_STAINED_GLASS_PANE);
                    }
                }
                break;
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() != player)
            return;
        if (!waitingForRename)
            return;

        event.setCancelled(true);

        String message = event.getMessage();
        if (!message.equalsIgnoreCase("cancel")) {
            customMob.setName(message);
            player.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eSuccessfully renamed to : &r" + message));
        }

        waitingForRename = false;
        Bukkit.getScheduler().runTask(CustomMobs.getPlugin(), new EditGUI(customMob, customMobsManager, player)::openInventory);
    }

    private ItemStack getHandItem(ItemStack item, boolean main) {
        ItemMeta itemMeta = item.getItemMeta();
        if(main)
            itemMeta.setDisplayName(Utils.applyFormat("&6&lMain hand Item"));
        else
            itemMeta.setDisplayName(Utils.applyFormat("&6&lOff hand Item"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Drag & drop an item to change this item ))"));
        lore.add(Utils.applyFormat("&7&o(( Left-Click to get the current item ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-Click to delete (reset) the current item ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getArmorItem(ItemStack item, String armor) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&6&l" + armor + " Item"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Drag & drop an item to change this item ))"));
        lore.add(Utils.applyFormat("&7&o(( Left-Click to get the current item ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-Click to delete (reset) the current item ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getTypesItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&6&lSelect type"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&eCurrent type: &f" + Utils.getStartCase(customMob.getType().name())));
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to select the type of this CustomMob ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getOptionsItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&6&lSelect options"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to change the options of this CustomMob ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getRenameItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&6&lRename"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&eCurrent name: &f" + customMob.getName()));
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to rename this CustomMob ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getPreviousItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getGiveItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Drag & drop an item to change this item ))"));
        lore.add(Utils.applyFormat("&7&o(( Left-Click to get the current item ))"));
        lore.add(Utils.applyFormat("&7&o(( Shift-Left-Click to get the current item (x64) ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-Click to delete (reset) the current item ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getDeleteItem(ItemStack item, boolean confirm) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        if(confirm) {
            itemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Confirm CustomMob deletion"));
            lore.add(Utils.applyFormat("&7&o(( Click again to confirm the deletion ))"));
        }
        lore.add(Utils.applyFormat("&c&l[!] &cThis will permanently delete this CustomMob."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getDropsItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&6&lDrops"));
        lore.add(Utils.applyFormat("&7&o(( Click to edit this CustomMob's drop(s) ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getSpawnerItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TRIAL_SPAWNER);
        item.setName("&6&lSpawner");
        item.addLore("", "&7&o(( Click to edit this CustomMob's spawner ))");
        return getMenuItem(item, true);
    }

    private ItemStack getCustomEffectsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TORCHFLOWER);
        item.setName("&6&lCustom effects");
        item.addLore("", "&c&l[!] Coming soon!", "", "&7&o(( Click to edit this CustomMob's custom effect(s) ))");
        return getMenuItem(item, true);
    }

    private ItemStack getOtherItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BELL);
        item.setName("&6&lOthers");
        item.addLore("", "&7&o(( Click to edit this CustomMob's other characteristics ))");
        return getMenuItem(item, true);
    }
}

