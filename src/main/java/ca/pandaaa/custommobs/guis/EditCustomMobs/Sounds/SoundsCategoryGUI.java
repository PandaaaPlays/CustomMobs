package ca.pandaaa.custommobs.guis.EditCustomMobs.Sounds;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.SoundEnum;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SoundsCategoryGUI extends CustomMobsGUI {
    private final List<ItemStack> sounds;
    private final List<SoundEnum> categorySounds;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;
    private final String category;
    private int currentPage = 1;
    private final Consumer<ca.pandaaa.custommobs.custommobs.Sound> consumer;
    public SoundsCategoryGUI(CustomMob customMob, String category, Consumer<ca.pandaaa.custommobs.custommobs.Sound> consumer) {
        super(54, "&8CustomMobs &8&l» &8Sounds ("+ category+")");
        this.customMob = customMob;
        this.category =category;
        this.consumer = consumer;
        this.categorySounds = SoundEnum.getSoundsByCategory(category.toUpperCase());
        this.sounds = getSounds();
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);
    }

    public void openInventory(Player player, int page) {
        this.currentPage = page;
        boolean nextPage = sounds.size() > (page * 45);

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for (int i = 0; i < 45; i++) {
            int position = ((currentPage - 1) * 45) + i;
            if (sounds.size() > position) {
                inventory.setItem(i, sounds.get(position));
            } else {
                inventory.setItem(i, filler);
                nextPage = false;
            }
        }
        if(page > 1) {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
            previous.setItemMeta(previousItemMeta);
        } else {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if(previousItemMeta != null)
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
        switch (event.getSlot()) {
            case 45:
                if(!name.contains("(")){
                    new AddSoundsGUI(customMob, consumer).openInventory(clicker);
                }
                else {
                    openInventory(clicker, currentPage-1);
                }
                break;
            case 53:
                if(name.contains("(")){
                    openInventory(clicker, currentPage+1);
                }
                break;
            default:
                if (event.getSlot() < 45){
                    ca.pandaaa.custommobs.custommobs.Sound customMobSound = new ca.pandaaa.custommobs.custommobs.Sound(categorySounds.get(event.getSlot()).getSound(), 1, SoundCategory.MASTER, 1, 1, event.getCurrentItem().getType(), true);
                    consumer.accept(customMobSound);
                }
                break;
        }
    }
    private List<ItemStack> getSounds() {
        List<ItemStack> items = new ArrayList<>();

        for (SoundEnum sound : categorySounds) {
            if(sound.getMaterial().isItem() && sound.getMaterial() != Material.AIR)
                items.add(createItem(sound.getMaterial(), sound.toString()));
            else
                items.add(createItem(Material.BARRIER, sound.toString()));
        }

        return items;
    }

    private ItemStack createItem(Material material, String string){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        itemMeta.setItemName(Utils.applyFormat("&6&l" + Utils.getStartCase(string)));
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to select this CustomMob sound ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return getMenuItem(item,true);
    }
}