package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PigZombie extends CustomMobType {
    private final int anger;

    public PigZombie(int anger) {
        this.anger = anger;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.PigZombie))
            return;

        ((org.bukkit.entity.PigZombie) customMob).setAnger(anger);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack anger = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta angerMeta = anger.getItemMeta();
        angerMeta.setDisplayName(Utils.applyFormat("&6&lAnger"));
        anger.setItemMeta(angerMeta);
        items.add(getOptionItemStack(anger));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
