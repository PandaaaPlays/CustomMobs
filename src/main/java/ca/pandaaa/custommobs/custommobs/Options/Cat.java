package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cat extends CustomMobOption {
    private static final String CAT_TYPE = "mob.cat-type";
    private org.bukkit.entity.Cat.Type catType;
    private static final String COLLAR_COLOR = "mob.collar-color";
    private DyeColor collarColor;

    public Cat(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.catType = getOption(CAT_TYPE, Registry.CAT_VARIANT);
        this.collarColor = getOption(COLLAR_COLOR, DyeColor.class);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Cat))
            return;

        if(catType != null)
            ((org.bukkit.entity.Cat) customMob).setCatType(catType);
        if(collarColor != null)
            ((org.bukkit.entity.Cat) customMob).setCollarColor(collarColor); // TODO MAKE SURE THIS WORKS
    }

    public void resetOptions() {
        setOption(CAT_TYPE, null);
        setOption(COLLAR_COLOR, null);
    }

    public List<ItemStack> getOptionItems() {
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
                setOption(COLLAR_COLOR, collarColor.name());
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
                setOption(CAT_TYPE, catType.toString());
                return getOptionItemStack(getCatTypeItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return entityType.getEntityClass() == org.bukkit.entity.Cat.class;
    }

    public CustomMobsItem getCollarColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&b&lCollar color");
        String color = collarColor == null ? "&fNone" : Utils.getChatColorOfColor(collarColor.name()) + Utils.getSentenceCase(collarColor.name());
        item.addLore("&eColor: " + color);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CollarColor");
        return item;
    }

    public CustomMobsItem getCatTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CAT_SPAWN_EGG);
        item.setName("&b&lCat type");
        String type = catType == null ? "&fRandom" : "&f" + Utils.getSentenceCase(catType.toString());
        item.addLore("&eType: &f" + type);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CatType");
        return item;
    }
}
