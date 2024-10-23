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

public class Sittable extends CustomMobType {
    private boolean sitting;

    public Sittable(boolean sitting) {
        this.sitting = sitting;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Sittable))
            return;

        ((org.bukkit.entity.Sittable) customMob).setSitting(sitting);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getSittingItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "sitting": {
                this.sitting = !sitting;
                customMob.getCustomMobConfiguration().setSitting(sitting);
                return getOptionItemStack(getSittingItem(), false, false);
            }
        }
        return null;
    }

    public CustomMobsItem getSittingItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BONE);
        String sitting = this.sitting ? "&a&lOn" : "&c&lOff";
        item.setName("&b&lSitting");
        item.addLore("&eSitting: " + sitting);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Sitting");
        return item;
    }
}
