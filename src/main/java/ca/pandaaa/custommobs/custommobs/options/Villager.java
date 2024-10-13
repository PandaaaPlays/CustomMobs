package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Villager extends CustomMobType {
    private final org.bukkit.entity.Villager.Type villagerType;
    private final org.bukkit.entity.Villager.Profession villagerProfession;

    public Villager(org.bukkit.entity.Villager.Type villagerType, org.bukkit.entity.Villager.Profession villagerProfession) {
        this.villagerType = villagerType;
        this.villagerProfession = villagerProfession;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Villager))
            return;

        if(villagerType != null)
            ((org.bukkit.entity.Villager) customMob).setVillagerType(villagerType);
        if(villagerProfession != null)
            ((org.bukkit.entity.Villager) customMob).setProfession(villagerProfession);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack type = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta typeMeta = type.getItemMeta();
        typeMeta.setDisplayName(Utils.applyFormat("&3&lVillager type"));
        type.setItemMeta(typeMeta);
        items.add(getOptionItemStack(type));

        ItemStack profession = new ItemStack(Material.BOOK);
        ItemMeta professionMeta = profession.getItemMeta();
        professionMeta.setDisplayName(Utils.applyFormat("&b&lVillager profession"));
        profession.setItemMeta(professionMeta);
        items.add(getOptionItemStack(profession));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
