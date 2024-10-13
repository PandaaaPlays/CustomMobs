package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Phantom extends CustomMobType {
    private final Integer size;

    public Phantom(Integer size) {
        this.size = size;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Phantom))
            return;

        if(size != null)
            ((org.bukkit.entity.Phantom) customMob).setSize(size);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack phantomSize = new ItemStack(Material.PHANTOM_MEMBRANE);
        ItemMeta phantomSizeMeta = phantomSize.getItemMeta();
        phantomSizeMeta.setDisplayName(Utils.applyFormat("&a&lPhantom size"));
        phantomSize.setItemMeta(phantomSizeMeta);
        items.add(getOptionItemStack(phantomSize));
// TODO other mobs scaling?
        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
