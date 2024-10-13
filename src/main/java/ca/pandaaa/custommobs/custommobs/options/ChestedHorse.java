package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChestedHorse extends CustomMobType {
    private final boolean chested;

    public ChestedHorse(boolean chested) {
        this.chested = chested;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.ChestedHorse))
            return;

        ((org.bukkit.entity.ChestedHorse) customMob).setCarryingChest(chested);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack chested = new ItemStack(Material.CHEST);
        ItemMeta chestedMeta = chested.getItemMeta();
        chestedMeta.setDisplayName(Utils.applyFormat("&e&lChested"));
        chested.setItemMeta(chestedMeta);
        items.add(getOptionItemStack(chested));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
