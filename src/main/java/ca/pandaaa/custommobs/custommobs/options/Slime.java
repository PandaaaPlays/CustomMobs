package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Slime extends CustomMobType {
    private final Integer size;

    public Slime(Integer size) {
        this.size = size;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Slime))
            return;

        if(size != null)
            ((org.bukkit.entity.Slime) customMob).setSize(size);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack slimeSize = new ItemStack(Material.SLIME_BALL);
        ItemMeta slimeSizeMeta = slimeSize.getItemMeta();
        slimeSizeMeta.setDisplayName(Utils.applyFormat("&a&lSlime size"));
        slimeSize.setItemMeta(slimeSizeMeta);
        items.add(getOptionItemStack(slimeSize));
// TODO other mobs scaling?
        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
