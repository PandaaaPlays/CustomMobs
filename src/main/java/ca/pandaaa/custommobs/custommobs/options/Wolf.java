package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Wolf extends CustomMobOption {
    private DyeColor collarColor;
    private boolean angry;

    public Wolf(DyeColor collarColor, boolean angry) {
        this.collarColor = collarColor;
        this.angry = angry;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Wolf))
            return;

        if(collarColor != null)
            ((org.bukkit.entity.Wolf) customMob).setCollarColor(collarColor);
        ((org.bukkit.entity.Wolf) customMob).setAngry(angry);

    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getCollarColorItem(), true, true));
        items.add(getOptionItemStack(getAngryItem(), true, true));
        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "collarcolor": {
                if(clickType.isRightClick()) {
                    this.collarColor = null;
                } else {
                    this.collarColor = NextOptions.getNextDyeColor(collarColor);
                }
                customMob.getCustomMobConfiguration().setCollarColor(collarColor);
                return getOptionItemStack(getCollarColorItem(), true, true);
            }

            case "angry": {
                this.angry = !angry;
                customMob.getCustomMobConfiguration().setAngryWolf(angry);
                return getOptionItemStack(getAngryItem(), false, false);
            }

        }
        return null;
    }

    public CustomMobsItem getCollarColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&b&lCollar color");
        String color = collarColor == null ? "&fDefault" : Utils.getChatColorOfColor(collarColor.name()) + collarColor.name();
        item.addLore("&eColor: " + color);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "CollarColor");
        return item;
    }

    public CustomMobsItem getAngryItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_SWORD);
        String angry = this.angry ? "&a&lOn" : "&c&lOff";
        item.setName("&c&lAngry");
        item.addLore("&eAngry: " + angry);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Angry");
        return item;
    }
}
