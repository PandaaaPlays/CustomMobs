package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shulker extends CustomMobOption {
    /**
     * Determines the dye color of the shulker CustomMob's shell. If left unset, the color will
     * be the purple-ish (default) one.
     */
    private static final String SHULKER_COLOR = "mob.shulker-color";
    private DyeColor color;

    public Shulker(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.color = getOption(SHULKER_COLOR, DyeColor.class);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Shulker))
            return;

        if(color != null)
            ((org.bukkit.entity.Shulker) customMob).setColor(color);
    }

    @Override
    public void resetOptions() {
        setOption(SHULKER_COLOR, null);
    }

    public List<ItemStack> getOptionItems() {
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
                setOption(SHULKER_COLOR, this.color != null ? this.color.name() : null);
                return getOptionItemStack(getShulkerColorItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Shulker.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getShulkerColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SHULKER_BOX);
        item.setName("&b&lShulker color");
        String color = this.color == null ? "&fNone" : Utils.getChatColorOfColor(this.color.name()) + Utils.getSentenceCase(this.color.name());
        item.addLore("&eColor: " + color);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ShulkerColor");
        return item;
    }
}
