package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SkeletonHorse extends CustomMobType {
    private final boolean trapped;
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

        ItemStack trapped = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta trappedMeta = trapped.getItemMeta();
        trappedMeta.setDisplayName(Utils.applyFormat("&4&lTrapped"));
        trapped.setItemMeta(trappedMeta);
        items.add(getOptionItemStack(trapped));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
