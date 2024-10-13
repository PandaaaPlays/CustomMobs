package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Creeper extends CustomMobType {
    private final Integer explosionCooldown;
    private final Integer explosionRadius;

    public Creeper(Integer explosionCooldown, Integer explosionRadius) {
        this.explosionCooldown = explosionCooldown;
        this.explosionRadius = explosionRadius;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Creeper))
            return;

        if(explosionCooldown != null)
            ((org.bukkit.entity.Creeper) customMob).setFuseTicks(explosionCooldown);
        if(explosionRadius != null)
            ((org.bukkit.entity.Creeper) customMob).setFuseTicks(explosionRadius);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack cooldown = new ItemStack(Material.CLOCK);
        ItemMeta cooldownMeta = cooldown.getItemMeta();
        cooldownMeta.setDisplayName(Utils.applyFormat("&c&lExplosion cooldown"));
        cooldown.setItemMeta(cooldownMeta);
        items.add(getOptionItemStack(cooldown));

        ItemStack radius = new ItemStack(Material.NETHER_STAR);
        ItemMeta radiusMeta = radius.getItemMeta();
        radiusMeta.setDisplayName(Utils.applyFormat("&c&lExplosion radius"));
        radius.setItemMeta(radiusMeta);
        items.add(getOptionItemStack(radius));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }

}
