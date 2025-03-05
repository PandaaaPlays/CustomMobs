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
import java.util.Arrays;
import java.util.List;

public class Shulker extends CustomMobOption {
    private DyeColor color;

    public Shulker(DyeColor color) {
        this.color = color;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Shulker))
            return;

        if(color != null)
            ((org.bukkit.entity.Shulker) customMob).setColor(color);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getShulkerColorItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch (option.toLowerCase()) {
            case "shulkercolor": {
                if (clickType.isRightClick()) {
                    this.color = null;
                } else {
                    List<DyeColor> colors = Arrays.asList(DyeColor.values());

                    if (colors.indexOf(color) == colors.size() - 1)
                        this.color = colors.get(0);
                    else
                        this.color = colors.get(colors.indexOf(color) + 1);
                }
                customMob.getCustomMobConfiguration().setShulkerColor(color);
                return getOptionItemStack(getShulkerColorItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getShulkerColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SHULKER_BOX);
        item.setName("&b&lShulker color");
        String color = this.color == null ? "&fNone" : Utils.getChatColorOfColor(this.color.name()) + Utils.getSentenceCase(this.color.name());
        item.addLore("&eColor: " + color);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "ShulkerColor");
        return item;
    }
}
