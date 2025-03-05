package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cat extends CustomMobOption {
    private org.bukkit.entity.Cat.Type catType;
    private DyeColor collarColor;

    public Cat(org.bukkit.entity.Cat.Type catType, DyeColor collarColor) {
        this.catType = catType;
        this.collarColor = collarColor;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Cat))
            return;

        if(catType != null)
            ((org.bukkit.entity.Cat) customMob).setCatType(catType);
        if(collarColor != null)
            ((org.bukkit.entity.Cat) customMob).setCollarColor(collarColor);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getCollarColorItem(), true, true));
        items.add(getOptionItemStack(getCatTypeItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "collarcolor": {
                if(clickType.isRightClick()) {
                    this.collarColor = null;
                } else {
                    List<DyeColor> colors = Arrays.asList(DyeColor.values());

                    if (colors.indexOf(collarColor) == colors.size() - 1)
                        this.collarColor = colors.get(0);
                    else
                        this.collarColor = colors.get(colors.indexOf(collarColor) + 1);
                }
                customMob.getCustomMobConfiguration().setCollarColor(collarColor);
                return getOptionItemStack(getCollarColorItem(), true, true);
            }

            case "cattype": {
                if(clickType.isRightClick()) {
                    this.catType = null;
                } else {
                    List<org.bukkit.entity.Cat.Type> catTypes = Registry.CAT_VARIANT.stream().toList();

                    if (catTypes.indexOf(catType) == catTypes.size() - 1)
                        this.catType = catTypes.get(0);
                    else
                        this.catType = catTypes.get(catTypes.indexOf(catType) + 1);
                }
                customMob.getCustomMobConfiguration().setCatType(catType);
                return getOptionItemStack(getCatTypeItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getCollarColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&b&lCollar color");
        String color = collarColor == null ? "&fNone" : Utils.getChatColorOfColor(collarColor.name()) + Utils.getSentenceCase(collarColor.name());
        item.addLore("&eColor: " + color);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "CollarColor");
        return item;
    }

    public CustomMobsItem getCatTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CAT_SPAWN_EGG);
        item.setName("&b&lCat type");
        String type = catType == null ? "&fRandom" : "&f" + Utils.getSentenceCase(catType.toString());
        item.addLore("&eType: &f" + type);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "CatType");
        return item;
    }
}
