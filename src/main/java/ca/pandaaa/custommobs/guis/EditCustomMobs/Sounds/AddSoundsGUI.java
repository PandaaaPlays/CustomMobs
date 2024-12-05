package ca.pandaaa.custommobs.guis.EditCustomMobs.Sounds;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.*;
import ca.pandaaa.custommobs.custommobs.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Consumer;
//TODO faire la shit de son hardcode enum
public class AddSoundsGUI extends CustomMobsGUI {
    private final CustomMob customMob;
    private final ItemStack previous;
    private final NamespacedKey keyFavorite;
    private final Consumer<Sound> consumer;

    public AddSoundsGUI(CustomMob customMob, Consumer<Sound> consumer) {
        super(54, "&8CustomMobs &8&l» &8Sounds");
        this.keyFavorite = new NamespacedKey(CustomMobs.getPlugin(),"CustomMobs.Favorite");
        this.customMob = customMob;
        this.consumer = consumer;
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
    }
    public void openInventory(Player player) {

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, filler);
        }
        ItemMeta previousItemMeta = previous.getItemMeta();
        if (previousItemMeta != null) previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
        previous.setItemMeta(previousItemMeta);
        inventory.setItem(10, getCategory(Material.WIND_CHARGE, "Ambient"));
        inventory.setItem(11, getCategory(Material.GRASS_BLOCK, "Block"));
        inventory.setItem(12, getCategory(Material.ZOMBIE_SPAWN_EGG, "Entity"));
        inventory.setItem(14, getCategory(Material.STICK, "Item"));
        inventory.setItem(15, getCategory(Material.MUSIC_DISC_5, "Music"));
        //TODO
        inventory.setItem(16, getCategory(Material.BELL, "Others"));
        inventory.setItem(28, getFavorite(Material.EXPERIENCE_BOTTLE, "Experience orb pickup", "entity.experience_orb.pickup"));
        inventory.setItem(34, getFavorite(Material.DRAGON_HEAD, "Dragon death", "entity.ender_dragon.death"));
        inventory.setItem(45, previous);
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
        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        Player clicker = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 10:
                new SoundsCategoryGUI(customMob, "Ambient", consumer).openInventory(clicker,1);
                break;
            case 11:
                new SoundsCategoryGUI(customMob, "Block", consumer).openInventory(clicker,1);
                break;
            case 12:
                new SoundsCategoryGUI(customMob, "Entity", consumer).openInventory(clicker,1);
                break;
            case 14:
                new SoundsCategoryGUI(customMob, "Item", consumer).openInventory(clicker,1);
                break;
            case 15:
                new SoundsCategoryGUI(customMob, "Music", consumer).openInventory(clicker,1);
                break;
            case 16:
                new SoundsCategoryGUI(customMob, "Others", consumer).openInventory(clicker,1);
                break;
            case 45:
                new SoundsGUI(customMob).openInventory(clicker, 1);
                break;
            default:
                if (event.getSlot() >= 28 && event.getSlot() <= 34 && event.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE){
                    org.bukkit.Sound sound = Registry.SOUNDS.get(NamespacedKey.minecraft(itemMeta.getPersistentDataContainer().get(keyFavorite, PersistentDataType.STRING)));
                    Sound customMobSound = new Sound( sound, 1, SoundCategory.MASTER, 1, 1, event.getCurrentItem().getType(), true);
                    consumer.accept(customMobSound);
                }
                break;
        }
    }

    private ItemStack getCategory(Material material, String name) {
        CustomMobsItem item = new CustomMobsItem(material);

        item.setName("&6&l" +name);
        item.addLore("");
        item.addLore( "&7&o(( Click to view this category ))");
        return getMenuItem(item, true);
    }

    private ItemStack getFavorite(Material material, String name, String sound) {
        CustomMobsItem item = new CustomMobsItem(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(keyFavorite, PersistentDataType.STRING,sound);
        item.setItemMeta(itemMeta);
        item.setName("&6&l" +name);
        item.addLore("");
        item.addLore( "&7&o(( Click to choose this sound ))");

        return getMenuItem(item, true);
    }
}
