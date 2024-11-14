package ca.pandaaa.custommobs.guis.EditCustomMobs.Sounds;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
//ajouter catégorie
//nmmettre recherche

public class SoundsGUI extends CustomMobsGUI {
    private final List<ItemStack> soundItems;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;

    public SoundsGUI(CustomMob customMob) {
        super(54, "&8CustomMobs &8&l» &8Sounds");

        this.customMob = customMob;
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);
        soundItems = getSoundsItems();
    }

    private List<ItemStack> getSoundsItems() {
        List<ItemStack> items = new ArrayList<>();

        List<Sound> sounds = Arrays.asList(Sound.values());
        sounds.sort(Comparator.comparing(sound -> sound.getKey().getKey()));
        String[] categorie = {""};

        for (Sound sound : sounds) {
            if(categorie[0].equalsIgnoreCase(sound.toString().split(".")[0])){

            }
            Bukkit.broadcastMessage(sound.toString().replace("."," "));
            ItemStack musicDisc = new ItemStack(Material.MUSIC_DISC_5);
            ItemMeta itemMeta = musicDisc.getItemMeta();

            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Click to select this CustomMob sound ))"));
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(Utils.applyFormat("&6&l" + Utils.getStartCase(sound.name())));

            musicDisc.setItemMeta(itemMeta);
            items.add(getMenuItem(musicDisc, false));
        }
        return items;
    }

    public void openInventory(Player player, int page) {
        boolean nextPage = true;

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for (int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (soundItems.size() > position)
                inventory.setItem(i, soundItems.get(position));
            else {
                inventory.setItem(i, new ItemStack(Material.AIR));
                nextPage = false;
            }
        }

        if (page > 1) {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if (previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
            previous.setItemMeta(previousItemMeta);
        } else {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if (previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
            previous.setItemMeta(previousItemMeta);
        }
        inventory.setItem(45, previous);
        inventory.setItem(46, filler);
        inventory.setItem(47, filler);
        inventory.setItem(48, filler);
        inventory.setItem(49, filler);
        inventory.setItem(50, filler);
        inventory.setItem(51, filler);
        inventory.setItem(52, filler);
        if (nextPage) {
            ItemMeta nextItemMeta = next.getItemMeta();
            if (nextItemMeta != null)
                nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
            next.setItemMeta(nextItemMeta);
            inventory.setItem(53, next);
        } else
            inventory.setItem(53, filler);

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
        String name = itemMeta.getDisplayName();
        Player clicker = (Player) event.getWhoClicked();
        Bukkit.broadcastMessage(clicker + " ya click " + name);
        switch (event.getSlot()) {
            case 45:
                if(!name.contains("("))
                    new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                else {
                    int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                    openInventory(clicker, page);
                }
                break;
            case 53:
                int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                openInventory(clicker, page);
                break;
            // Every mob types
            default:
                if (event.getCurrentItem() != filler) {
                    customMob.setType(EntityType.valueOf(ChatColor.stripColor(itemMeta.getDisplayName().replace(" ", "_"))));
                    new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                }
                break;
        }
    }
}
