package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.CustomMobsManager;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

public class EditCustomMobsGUI extends CustomMobsGUI implements Listener {

    private final ItemStack mainHand;
    private final ItemStack offHand;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack types;
    private final ItemStack options;
    private final ItemStack drops;
    private final ItemStack potions;
    private final ItemStack name;
    private final ItemStack message;
    private final ItemStack sound;
    private final ItemStack previous;
    private final ItemStack item;
    private final ItemStack spawner;
    private final ItemStack delete;
    private final CustomMob customMob;
    private final CustomMobsManager customMobsManager;
    private final Player player;

    private boolean waitingForRename;

    public EditCustomMobsGUI(CustomMob customMob, CustomMobsManager customMobsManager, Player player) {
        super(54, "&8CustomMobs &8&l» &8"
                + customMob.getCustomMobFileName().replace(".yml", "").substring(0, 1).toUpperCase()
                + customMob.getCustomMobFileName().replace(".yml", "").substring(1).toLowerCase());

        CustomMobs plugin = CustomMobs.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.customMob = customMob;
        this.player = player;

        this.mainHand = getMenuItem(new ItemStack(Material.NETHERITE_SWORD));
        this.offHand = getMenuItem(new ItemStack(Material.SHIELD));
        this.helmet = getMenuItem(new ItemStack(Material.NETHERITE_HELMET));
        this.chestplate = getMenuItem(new ItemStack(Material.NETHERITE_CHESTPLATE));
        this.leggings = getMenuItem(new ItemStack(Material.NETHERITE_LEGGINGS));
        this.boots = getMenuItem(new ItemStack(Material.NETHERITE_BOOTS));
        this.types = getMenuItem(new ItemStack(Material.ALLAY_SPAWN_EGG));
        this.options = getMenuItem(new ItemStack(Material.END_CRYSTAL));
        this.name = getMenuItem(new ItemStack(Material.SPRUCE_HANGING_SIGN));
        this.message = getMenuItem(new ItemStack(Material.WRITABLE_BOOK));
        this.drops = getMenuItem(new ItemStack(Material.PITCHER_POD));
        this.sound = getMenuItem(new ItemStack(Material.JUKEBOX));
        this.potions = getMenuItem(new ItemStack(Material.OMINOUS_BOTTLE));
        this.previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"));
        this.item = getMenuItem(customMob.getItem());
        this.spawner = getMenuItem(customMob.getSpawner());
        this.delete = getMenuItem(new ItemStack(Material.BARRIER));

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

        // Potions item
        inventory.setItem(32, getPotionsItem(potions));

        // Sounds item
        inventory.setItem(33, getSoundsItem(sound));

        // Messages item
        inventory.setItem(34, getMessageItem(message));

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
    private void onInventoryClick(InventoryClickEvent event) {
        if (!Objects.equals(event.getClickedInventory(), inventory))
            return;

        event.setCancelled(true);

        Player clicker = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 10:
                if(clicker.getItemOnCursor().getType() != Material.AIR) {
                    // TODO set the item for main hain
                } else {
                    // TODO get the main hand item
                }
                break;
            case 11:
                if(clicker.getItemOnCursor().getType() != Material.AIR) {
                    // TODO set the item for off hain
                } else {
                    // TODO get the off hand item
                }
                break;
            case 13:
                if(clicker.getItemOnCursor().getType() != Material.AIR) {
                    // TODO set the item for helmet
                } else {
                    // TODO get the helmet item
                }
                break;
            case 14:
                if(clicker.getItemOnCursor().getType() != Material.AIR) {
                    // TODO set the item for chestplate
                } else {
                    // TODO get the chestplate item
                }
                break;
            case 15:
                if(clicker.getItemOnCursor().getType() != Material.AIR) {
                    // TODO set the item for leggings
                } else {
                    // TODO get the leggings item
                }
                break;
            case 16:
                if(clicker.getItemOnCursor().getType() != Material.AIR) {
                    // TODO set the item for boots
                } else {
                    // TODO get the boots item
                }
                break;
            case 28:
                new TypesCustomMobsGUI(customMob).openInventory(clicker, 1);
                break;
            case 29:
                new OptionsCustomMobsGUI(customMob).openInventory(clicker, 1);
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
                // TODO drops
                break;
            case 32:
                // TODO potions
                break;
            case 33:
                // TODO sounds
                break;
            case 34:
                // TODO message
                break;
            case 45:
                new MainCustomMobsGUI().openInventory(clicker, 1);
                break;
            case 48:
                customMobsManager.giveCustomMob(clicker, customMob, "item", event.isShiftClick() ? 64 : 1);
                break;
            case 50:
                customMobsManager.giveCustomMob(clicker, customMob, "spawner", event.isShiftClick() ? 64 : 1);
                break;
            case 53:
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) {
                    // TODO DELETE
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
        Bukkit.getScheduler().runTask(CustomMobs.getPlugin(), new EditCustomMobsGUI(customMob, customMobsManager, player)::openInventory);
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
        lore.add(Utils.applyFormat("&7&o(( Click to get the current item ))"));
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
        lore.add(Utils.applyFormat("&7&o(( Click to get the current item ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getTypesItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.applyFormat("&6&lSelect type"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&eCurrent type: &f" + customMob.getType().name().replace("_", " ")));
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
        lore.add(Utils.applyFormat("&7&o(( Click to get this item ))"));
        lore.add(Utils.applyFormat("&7&o(( Shift+Click to get this item (x64) ))"));
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

    private ItemStack getMessageItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        itemMeta.setDisplayName(Utils.applyFormat("&6&lMessages"));
        lore.add(Utils.applyFormat("&eCurrent message(s):"));

        int i = 1;
        for (String message : customMob.getCustomMobMessage().getMessages()) {
            lore.add(Utils.applyFormat("&f " + i++ + ". &r" + message));
        }

        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( To add messages, use : /custommobs message [name] add [message] ))"));
        lore.add(Utils.applyFormat("&7&o(( To edit messages, use : /custommobs message [name] edit [number] [message] ))"));
        lore.add(Utils.applyFormat("&7&o(( To remove messages, use : /custommobs message [name] remove [number/all] ))"));
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

    private ItemStack getSoundsItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&6&lSounds"));
        lore.add(Utils.applyFormat("&7&o(( Click to edit this CustomMob's spawn sound(s) ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getPotionsItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&6&lPotions"));
        lore.add(Utils.applyFormat("&7&o(( Click to edit this CustomMob's potion effect(s) ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}

