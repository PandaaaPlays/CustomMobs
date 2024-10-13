package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Fox extends CustomMobType {
    private final org.bukkit.entity.Fox.Type foxType;

    public Fox(org.bukkit.entity.Fox.Type foxType) {
        this.foxType = foxType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Fox))
            return;

        if(foxType != null)
            ((org.bukkit.entity.Fox) customMob).setFoxType(foxType);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack type = new ItemStack(Material.FOX_SPAWN_EGG);
        ItemMeta typeMeta = type.getItemMeta();
        typeMeta.setDisplayName(Utils.applyFormat("&b&lFox type"));
        type.setItemMeta(typeMeta);
        items.add(getOptionItemStack(type));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
