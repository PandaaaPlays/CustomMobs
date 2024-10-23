package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Ageable extends CustomMobType {
    private Boolean isBaby;
    public Ageable(Boolean isBaby) {
        this.isBaby = isBaby;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Ageable))
            return;

        if(isBaby != null) {
            if (isBaby) {
                ((org.bukkit.entity.Ageable) customMob).setBaby();
                if (customMob instanceof Breedable)
                    ((org.bukkit.entity.Breedable) customMob).setAgeLock(true);
            } else
                ((org.bukkit.entity.Ageable) customMob).setAdult();
        }
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getAgeItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "baby": {
                if(clickType.isRightClick())
                    this.isBaby = null;
                else
                    this.isBaby = this.isBaby == null ? true : !isBaby;

                customMob.getCustomMobConfiguration().setBaby(isBaby);
                return getOptionItemStack(getAgeItem(), true, false);
            }
        }
        return null;
    }

    public CustomMobsItem getAgeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.EGG);
        String age;
        if (this.isBaby == null)
            age = "&fRandom";
        else
            age = this.isBaby ? "&bBaby" : "&2Adult";
        item.setName("&b&lAge");
        item.addLore("&eAge: " + age);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "baby");
        return item;
    }
}
