package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class CustomMobType {
    public abstract void applyOptions(Entity customMobs);
    public abstract List<ItemStack> getOptionItems(CustomMob customMob);
    public abstract ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType);

    protected ItemStack getOptionItemStack(CustomMobsItem item, boolean hasTwoClicks, boolean cycle) {
        if (hasTwoClicks && cycle)
            item.addLore("", "&7&o(( Left-Click to cycle this option ))", "&7&o(( Right-Click to reset this option ))");
        else if (hasTwoClicks)
            item.addLore("", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        else if (cycle)
            item.addLore("", "&7&o(( Click to cycle this option ))");
        else
            item.addLore("", "&7&o(( Click to edit this option ))");
        return item;
    }
}
