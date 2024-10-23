package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Zombie extends CustomMobType {
    private boolean canBreakDoors;

    public Zombie(boolean canBreakDoors) {
        this.canBreakDoors = canBreakDoors;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Zombie))
            return;

        ((org.bukkit.entity.Zombie) customMob).setCanBreakDoors(canBreakDoors);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getCanBreakDoorsItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "canbreakdoors": {
                this.canBreakDoors = !canBreakDoors;
                customMob.getCustomMobConfiguration().setCanBreakDoors(canBreakDoors);
                return getOptionItemStack(getCanBreakDoorsItem(), false, false);
            }
        }
        return null;
    }

    public CustomMobsItem getCanBreakDoorsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.OAK_DOOR);
        String canBreakDoors = this.canBreakDoors ? "&a&lOn" : "&c&lOff";
        item.setName("&c&lCan break doors");
        item.addLore("&eCan break doors: " + canBreakDoors);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "CanBreakDoors");
        return item;
    }
}
