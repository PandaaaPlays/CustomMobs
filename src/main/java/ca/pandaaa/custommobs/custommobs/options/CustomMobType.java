package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class CustomMobType {
    public abstract void applyOptions(Entity customMobs);
    public abstract List<ItemStack> getOptionItems(CustomMob customMob);
    public abstract ItemStack modifyOption(CustomMob customMob, String option);

    protected ItemStack getOptionItemStack(CustomMobsItem item) {
        item.addLore("", "&7&o(( Click to edit this option ))");
        return item;
    }

    @Deprecated
    protected ItemStack getOptionItemStack(ItemStack item) {
        return item;
    }
}
