package ca.pandaaa.custommobs.guis.EditCustomMobs.Sounds;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
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

import java.util.ArrayList;
import java.util.List;

public class SoundsGUI extends CustomMobsGUI {
    private List<ItemStack> soundsItem;
    private final List<Sound> sounds;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;
    private final ItemStack addSound;
    private int currentPage = 1;

    public SoundsGUI(CustomMob customMob) {
        super(54, "&8CustomMobs &8&l» &8Current sounds");

        this.customMob = customMob;
        sounds = customMob.getSounds();
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);
        addSound = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
    }

    public void openInventory(Player player, int page) {
        soundsItem = getSounds();
        this.currentPage = page;
        boolean nextPage = sounds.size() > (page * 45);

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (sounds.size() > position) {
                inventory.setItem(i, soundsItem.get(position));
            }
            else {
                inventory.setItem(i, new ItemStack(Material.AIR));
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
        ItemMeta addItemMeta = addSound.getItemMeta();
        if(addItemMeta != null) {
            addItemMeta.setDisplayName(Utils.applyFormat("&a&l[+] Add sound"));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Click to add sound effects ))"));
            addItemMeta.setLore(lore);
            addSound.setItemMeta(addItemMeta);
        }
        inventory.setItem(49, addSound);
        inventory.setItem(50, filler);
        inventory.setItem(51, filler);
        inventory.setItem(52, filler);
        if(nextPage) {
            ItemMeta nextItemMeta = next.getItemMeta();
            if(nextItemMeta != null)
                nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
            next.setItemMeta(nextItemMeta);
            inventory.setItem(53, next);
        } else
            inventory.setItem(53, filler);

        player.openInventory(inventory);
    }

    private List<ItemStack> getSounds() {
        List<ItemStack> items = new ArrayList<>();

        for (Sound sound : sounds) {
            ItemStack musicDisc = new ItemStack(sound.getMaterial());
            ItemMeta itemMeta = musicDisc.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Utils.applyFormat("&f&l* &eVolume:&f " + sound.getVolume()));
            lore.add(Utils.applyFormat("&f&l* &bPitch:&f " +sound.getPitch()));
            lore.add(Utils.applyFormat("&f&l* &aRadius:&f " + sound.getRadius()));
            lore.add(Utils.applyFormat("&f&l* &dCategory:&f " + Utils.getStartCase(sound.getCategory().toString())));
            String onDeath = sound.getOnDeath() ? "&c&lOn death" : "&a&lOn spawn";
            lore.add(Utils.applyFormat("&f&l* &9Sound event:&f " + onDeath));
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Left-Click to edit this sound ))"));
            lore.add(Utils.applyFormat("&7&o(( Right-Click to remove this sound ))"));
            itemMeta.setLore(lore);
            itemMeta.setItemName(Utils.applyFormat("&6&l" + Utils.getSentenceCase(sound.getSoundType().name())));
            musicDisc.setItemMeta(itemMeta);
            items.add(getMenuItem(musicDisc, false));
        }
        return items;
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
        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;
        ItemMeta itemMeta = item.getItemMeta();
        String name = itemMeta.getDisplayName();
        Player clicker = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 45:
                if(!name.contains("("))
                    new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                else {
                    openInventory(clicker, currentPage);
                }
                break;
            case 49:
                new AddSoundsGUI(customMob, value ->{
                    customMob.addSound(value);
                    new SoundsGUI(customMob).openInventory(clicker,currentPage);
                }).openInventory(clicker);
                break;
            case 53:
                if(name.contains("(")){
                    openInventory(clicker, currentPage);
                }
                break;
            default:
                if (event.getSlot() < 45) {
                    int soundItemIndex = (currentPage - 1) * 45 + event.getSlot();
                    NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Sound.Remove.Confirm");
                    if (event.isRightClick()) {
                        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key)) {
                            customMob.removeSound(soundItemIndex);
                            boolean currentPageEmpty = sounds.size() - 1 <= ((currentPage - 1) * 45);
                            if (currentPageEmpty)
                                currentPage = currentPage == 1 ? currentPage : currentPage - 1;
                            openInventory(clicker, currentPage);
                        } else {
                            openInventory(clicker, currentPage);
                            event.getInventory().setItem(event.getSlot(), getMenuItem(getDeleteItem(new ItemStack(Material.BARRIER)), true));
                        }
                    } else {
                        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key))
                            openInventory(clicker, currentPage);
                        else
                            new SpecificSoundGUI(customMob, (currentPage - 1) * 45 + event.getSlot()).openInventory(clicker);
                    }
                }
                break;
        }

    }

    private ItemStack getDeleteItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Sound.Remove.Confirm");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Confirm sound deletion"));
        lore.add(Utils.applyFormat("&7&o(( Left-click to cancel the deletion ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-click again to confirm the deletion ))"));
        lore.add(Utils.applyFormat("&c&l[!] &cThis will permanently delete this sound."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

}