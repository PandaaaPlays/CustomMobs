package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Vindicator extends CustomMobType {
    private final boolean johnny;

    public Vindicator(boolean johnny) {
        this.johnny = johnny;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Vindicator))
            return;

        ((org.bukkit.entity.Vindicator) customMob).setJohnny(johnny);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack johnny = new ItemStack(Material.BOOK);
        ItemMeta johnnyMeta = johnny.getItemMeta();
        johnnyMeta.setDisplayName(Utils.applyFormat("&4&lJohnny vandicator"));
        johnny.setItemMeta(johnnyMeta);
        items.add(getOptionItemStack(johnny));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
