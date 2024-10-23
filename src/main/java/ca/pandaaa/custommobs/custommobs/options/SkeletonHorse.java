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

public class SkeletonHorse extends CustomMobType {
    private boolean trapped;
    public SkeletonHorse(boolean trapped) {
        this.trapped = trapped;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.SkeletonHorse))
            return;

        ((org.bukkit.entity.SkeletonHorse) customMob).setTrapped(trapped);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getTrappedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "trapped": {
                this.trapped = !trapped;
                customMob.getCustomMobConfiguration().setSkeletonTrap(trapped);
                return getOptionItemStack(getTrappedItem(), false, false);
            }
        }
        return null;
    }

    public CustomMobsItem getTrappedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TRIPWIRE_HOOK);
        String trapped = this.trapped ? "&a&lOn" : "&c&lOff";
        item.setName("&4&lTrapped");
        item.addLore("&eTrapped: " + trapped);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Trapped");
        return item;
    }
}
