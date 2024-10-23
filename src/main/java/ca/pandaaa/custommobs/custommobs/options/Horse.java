package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Horse extends CustomMobType {
    private org.bukkit.entity.Horse.Color horseColor;
    private org.bukkit.entity.Horse.Style horseStyle;

    public Horse(org.bukkit.entity.Horse.Color horseColor, org.bukkit.entity.Horse.Style horseStyle) {
        this.horseColor = horseColor;
        this.horseStyle = horseStyle;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Horse))
            return;

        if(horseColor != null)
            ((org.bukkit.entity.Horse) customMob).setColor(horseColor);
        if(horseStyle != null)
            ((org.bukkit.entity.Horse) customMob).setStyle(horseStyle);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getHorseStyleItem(), true, true));
        items.add(getOptionItemStack(getHorseColorItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "horsecolor": {
                if(clickType.isRightClick()) {
                    this.horseColor = null;
                } else {
                    this.horseColor = NextOptions.getNextHorseColor(horseColor);
                }
                customMob.getCustomMobConfiguration().setHorseColor(horseColor);
                return getOptionItemStack(getHorseColorItem(), true, true);
            }

            case "horsestyle": {
                if(clickType.isRightClick()) {
                    this.horseStyle = null;
                } else {
                    this.horseStyle = NextOptions.getNextHorseStyle(horseStyle);
                }
                customMob.getCustomMobConfiguration().setHorseStyle(horseStyle);
                return getOptionItemStack(getHorseStyleItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getHorseColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&b&lHorse color");
        String color = horseColor == null ? "&fRandom" : Utils.getChatColorOfColor(horseColor.name()) + horseColor.name();
        item.addLore("&eColor: " + color);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "HorseColor");
        return item;
    }

    public CustomMobsItem getHorseStyleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.HORSE_SPAWN_EGG);
        item.setName("&b&lHorse style");
        String type = horseStyle == null ? "&fRandom" : "&f" + horseStyle.toString();
        item.addLore("&eType: &f" + type);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "HorseStyle");
        return item;
    }
}
