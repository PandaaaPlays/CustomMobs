package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AbstractHorse extends CustomMobType {
    private final Double jumpStrength;

    public AbstractHorse(Double jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.AbstractHorse))
            return;

        if(jumpStrength != null)
            ((org.bukkit.entity.AbstractHorse) customMob).setJumpStrength(jumpStrength);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack jumpStrength = new ItemStack(Material.SLIME_BLOCK);
        ItemMeta jumpStrengthMeta = jumpStrength.getItemMeta();
        jumpStrengthMeta.setDisplayName(Utils.applyFormat("&a&lJump strength"));
        jumpStrength.setItemMeta(jumpStrengthMeta);
        items.add(getOptionItemStack(jumpStrength));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
