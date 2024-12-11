package ca.pandaaa.custommobs.guis.EditCustomMobs.Sounds;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Sound;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SpecificSoundGUI  extends CustomMobsGUI {
    private final CustomMob customMob;
    private final int soundIndex;
    private final Sound sound;
    private final List<SoundCategory> soundCategoryList;

    public SpecificSoundGUI(CustomMob customMob, int soundIndex){
        super(36, "&8CustomMobs &8&l» &8Sound configuration");
        this.customMob = customMob;
        this.soundIndex = soundIndex;
        this.sound = customMob.getSounds().get(soundIndex);
        this.soundCategoryList = Arrays.asList(SoundCategory.values());
    }

    public void openInventory(Player player) {
        for(int i = 0; i < 36; i++)
            inventory.setItem(i, filler);

        inventory.setItem(11, getVolumeItem());
        inventory.setItem(12, getPitchItem());
        inventory.setItem(13, getRadiusItem());
        inventory.setItem(14, getCategoryItem());
        inventory.setItem(15, getDeathSpawnItem());
        inventory.setItem(27, getPreviousItem());
        inventory.setItem(31, getSoundItem());
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
        switch (event.getSlot()) {
            // Volume
            case 11:
                new DoubleGUI("Volume", customMob, true, 0, 1, (value) -> {
                    sound.setVolume(value.floatValue());
                    customMob.editSound(sound, soundIndex);
                    new SpecificSoundGUI(customMob, soundIndex).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, (double) sound.getVolume());
                break;
            // Pitch
           case 12:
               new DoubleGUI("Pitch", customMob, true, 0.5, 2, (value) -> {
                   sound.setPitch(value.floatValue());
                   customMob.editSound(sound, soundIndex);
                   new SpecificSoundGUI(customMob, soundIndex).openInventory((Player) event.getWhoClicked());
               }).openInventory(clicker, (double) sound.getPitch());
                break;
             // Radius
            case 13:
                new DoubleGUI("Radius", customMob, false, -1, 250, (value) -> {
                    sound.setRadius(value.floatValue());
                    customMob.editSound(sound, soundIndex);
                    new SpecificSoundGUI(customMob, soundIndex).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, (double) sound.getRadius());
                break;
            // Category
            case 14:
                if (event.isRightClick()) {
                    if(soundCategoryList.indexOf(sound.getCategory())== 0){
                        break;
                    }
                    sound.setCategory(soundCategoryList.get(soundCategoryList.indexOf(sound.getCategory()) - 1));
                } else {
                    if (soundCategoryList.indexOf(sound.getCategory())== soundCategoryList.size()-1){
                        break;
                    }
                    sound.setCategory(soundCategoryList.get(soundCategoryList.indexOf(sound.getCategory()) + 1));
                }
                customMob.editSound(sound, soundIndex);
                new SpecificSoundGUI(customMob, soundIndex).openInventory((Player) event.getWhoClicked());
                break;
            // On Death
            case 15:
                sound.setOnDeath(!sound.getOnDeath());
                customMob.editSound(sound, soundIndex);
                openInventory(clicker);
                break;
            // Previous page
            case 27:
                new SoundsGUI(customMob).openInventory(clicker, 1);
                break;
            case 31:
                new AddSoundsGUI(customMob, value ->{
                    sound.setSoundType(value.getSoundType());
                    sound.setMaterial(value.getMaterial());
                    customMob.editSound(sound, soundIndex);
                    new SpecificSoundGUI(customMob,soundIndex).openInventory(clicker);
                }).openInventory(clicker);
                break;
            case 35:
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) {
                    customMob.removeSound(soundIndex);
                    new SoundsGUI(customMob).openInventory(clicker, 1);
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

    private ItemStack getVolumeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.IRON_BLOCK);
        item.setName("&6&lVolume");
        item.addLore("&eVolume:&f " + sound.getVolume(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getPitchItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLD_BLOCK);
        item.setName("&6&lPitch");
        item.addLore("&ePitch:&f " + sound.getPitch(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_BLOCK);
        item.setName("&6&lRadius");
        item.addLore("&eRadius:&f " + sound.getRadius(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getCategoryItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NETHERITE_BLOCK);
        item.setName("&6&lCategory");
        item.addLore("&eCategory:&f " + sound.getCategory(), " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getSoundItem() {
        CustomMobsItem item = new CustomMobsItem(sound.getMaterial());
        item.setName(Utils.applyFormat("&6&l" + Utils.getSentenceCase(sound.getSoundType().name())));
        item.addLore("");
        item.addLore(Utils.applyFormat("&7&o(( Click to change the current sound ))"));
        return getMenuItem(item, true);
    }

    private ItemStack getDeathSpawnItem() {
        CustomMobsItem item = new CustomMobsItem(Material.STICK);
        item.setName("&6&lDeath/Spawn");
        if(sound.getOnDeath()){
            item.setType(Material.STONE_SWORD);
            item.addLore("&eDeath/Spawn:&f On Death", "&7&o(( Click to edit this option ))");
        }else{
            item.setType(Material.VILLAGER_SPAWN_EGG);
            item.addLore("&eDeath/Spawn:&f On Spawn", "&7&o(( Click to edit this option ))");
        }
        return getMenuItem(item, true);
    }

    private ItemStack getDeleteItem(boolean confirm) {
        CustomMobsItem item = new CustomMobsItem(Material.BARRIER);
        item.addLore("");
        if (confirm) {
            item.setName("&c&l[-] Confirm sound deletion");
            item.addLore("&7&o(( Click again to confirm the deletion ))");
        }
        else{
            item.setName("&c&l[-] Delete sound");
            item.addLore("&c&l[!] &cThis will permanently delete this sound.");
        }
        return getMenuItem(item, true);
    }
}
