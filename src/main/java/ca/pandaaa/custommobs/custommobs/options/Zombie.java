package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Zombie extends CustomMobType {
    private final boolean canBreakDoors;

    public Zombie(boolean canBreakDoors) {
        this.canBreakDoors = canBreakDoors;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Zombie))
            return;

        ((org.bukkit.entity.Zombie) customMob).setCanBreakDoors(canBreakDoors);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack canBreakDoor = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta canBreakDoorMeta = canBreakDoor.getItemMeta();
        canBreakDoorMeta.setDisplayName(Utils.applyFormat("&c&lCan break doors"));
        canBreakDoor.setItemMeta(canBreakDoorMeta);
        items.add(getOptionItemStack(canBreakDoor));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
