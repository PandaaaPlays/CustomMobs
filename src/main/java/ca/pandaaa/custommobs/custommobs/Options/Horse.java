package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Horse extends CustomMobOption {
    /**
     *
     */
    private static final String HORSE_COLOR = "mob.horse-color";
    private org.bukkit.entity.Horse.Color horseColor;
    /**
     *
     */
    private static final String HORSE_STYLE = "mob.horse-style";
    private org.bukkit.entity.Horse.Style horseStyle;
    /**
     *
     */
    private static final String HORSE_ARMOR = "mob.horse-armor";
    private Material horseArmor;

    public Horse(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.horseColor = horseColor;
        this.horseStyle = horseStyle;
        this.horseArmor = horseArmor;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Horse))
            return;

        if(horseColor != null)
            ((org.bukkit.entity.Horse) customMob).setColor(horseColor);
        if(horseStyle != null)
            ((org.bukkit.entity.Horse) customMob).setStyle(horseStyle);
        if(horseArmor != null)
            ((org.bukkit.entity.Horse) customMob).getInventory().setArmor(new ItemStack(horseArmor));
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getHorseStyleItem(), true, true));
        items.add(getOptionItemStack(getHorseColorItem(), true, true));
        items.add(getOptionItemStack(getHorseArmorItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "horsecolor": {
                if(clickType.isRightClick()) {
                    this.horseColor = null;
                } else {
                    List<org.bukkit.entity.Horse.Color> horseColors = Arrays.asList(org.bukkit.entity.Horse.Color.values());

                    if (horseColors.indexOf(horseColor) == horseColors.size() - 1)
                        this.horseColor = horseColors.get(0);
                    else
                        this.horseColor = horseColors.get(horseColors.indexOf(horseColor) + 1);
                }
                customMob.getCustomMobConfiguration().setHorseColor(horseColor);
                return getOptionItemStack(getHorseColorItem(), true, true);
            }

            case "horsestyle": {
                if(clickType.isRightClick()) {
                    this.horseStyle = null;
                } else {
                    List<org.bukkit.entity.Horse.Style> horseStyles = Arrays.asList(org.bukkit.entity.Horse.Style.values());

                    if (horseStyles.indexOf(horseStyle) == horseStyles.size() - 1)
                        this.horseStyle = horseStyles.get(0);
                    else
                        this.horseStyle = horseStyles.get(horseStyles.indexOf(horseStyle) + 1);
                }
                customMob.getCustomMobConfiguration().setHorseStyle(horseStyle);
                return getOptionItemStack(getHorseStyleItem(), true, true);
            }

            case "horsearmor": {
                if(clickType.isRightClick()) {
                    this.horseArmor = null;
                } else {
                    List<Material> armors = new ArrayList<>();
                    armors.add(Material.LEATHER_HORSE_ARMOR);
                    armors.add(Material.IRON_HORSE_ARMOR);
                    armors.add(Material.GOLDEN_HORSE_ARMOR);
                    armors.add(Material.DIAMOND_HORSE_ARMOR);
                    if (armors.indexOf(horseArmor) == armors.size() - 1)
                        this.horseArmor = armors.get(0);
                    else
                        this.horseArmor = armors.get(armors.indexOf(horseArmor) + 1);
                }
                customMob.getCustomMobConfiguration().setHorseArmor(horseArmor);

                if(horseArmor != null) {
                    customMob.addDrop(new Drop(new ItemStack(horseArmor), 1, "HorseArmor"));
                } else {
                    customMob.removeDropItem("HorseArmor");
                }

                return getOptionItemStack(getHorseArmorItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    public CustomMobsItem getHorseColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&b&lHorse color");
        String color = horseColor == null ? "&fRandom" : Utils.getChatColorOfColor(horseColor.name()) + Utils.getSentenceCase(horseColor.name());
        item.addLore("&eColor: " + color);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "HorseColor");
        return item;
    }

    public CustomMobsItem getHorseStyleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.HORSE_SPAWN_EGG);
        item.setName("&b&lHorse style");
        String type = horseStyle == null ? "&fRandom" : "&f" + Utils.getSentenceCase(horseStyle.toString());
        item.addLore("&eType: &f" + type);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "HorseStyle");
        return item;
    }

    public CustomMobsItem getHorseArmorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLDEN_HORSE_ARMOR);
        item.setName("&b&lHorse armor");
        String armor = horseArmor == null ? "&fNone" : "&f" + Utils.getSentenceCase(horseArmor.toString());
        item.addLore("&eArmor: &f" + armor);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "HorseArmor");
        return item;
    }
}
