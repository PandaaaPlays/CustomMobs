package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Rabbit extends CustomMobType {
    private final org.bukkit.entity.Rabbit.Type rabbitType;

    public Rabbit(org.bukkit.entity.Rabbit.Type rabbitType) {
        this.rabbitType = rabbitType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Rabbit))
            return;

        if(rabbitType != null)
            ((org.bukkit.entity.Rabbit) customMob).setRabbitType(rabbitType);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack type = new ItemStack(Material.RABBIT_SPAWN_EGG);
        ItemMeta typeMeta = type.getItemMeta();
        typeMeta.setDisplayName(Utils.applyFormat("&b&lRabbit type"));
        type.setItemMeta(typeMeta);
        items.add(getOptionItemStack(type));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
