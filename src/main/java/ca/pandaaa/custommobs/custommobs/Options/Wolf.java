package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
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

public class Wolf extends CustomMobOption {
    /**
     * Sets the dye color of the wolf CustomMob's collar. The wolf needs to be tamed and have an owner
     * in order to have the collar rendered (see Tameable option).
     */
    private static final String COLLAR_COLOR = "mob.collar-color";
    private DyeColor collarColor;
    /**
     * Indicates whether the wolf is in an aggressive (angry) state. Typically, an angry wolf will have red eyes
     * but will not attack the players unless they attack it. If you wish that the wolf mobs attacks the players by
     * themselves, see Special > Aggressive.
     */
    private static final String ANGRY_WOLF = "mob.angry-wolf";
    private boolean angry;
    /**
     * Specifies the variant of the wolf CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal wolf).
     */
    private static final String WOLF_VARIANT = "mob.wolf-variant";
    private org.bukkit.entity.Wolf.Variant wolfVariant;
    /**
     * Indicates if the wolf should be equipped with armor.
     */
    private static final String WOLF_ARMOR = "mob.wolf-armor";
    private boolean wolfArmor;

    public Wolf(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.collarColor = getOption(COLLAR_COLOR, DyeColor.class);
        this.angry = getOption(ANGRY_WOLF, Boolean.class, false);
        this.wolfVariant = getOption(WOLF_VARIANT, Registry.WOLF_VARIANT);
        this.wolfArmor = getOption(WOLF_ARMOR, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Wolf))
            return;

        if(collarColor != null)
            ((org.bukkit.entity.Wolf) customMob).setCollarColor(collarColor);
        ((org.bukkit.entity.Wolf) customMob).setAngry(angry);
        if(wolfVariant != null)
            ((org.bukkit.entity.Wolf) customMob).setVariant(wolfVariant);
        // No current way to add the armor to the wolf using the API.
        // ((org.bukkit.entity.Wolf) customMob)
    }

    @Override
    public void resetOptions() {
        setOption(COLLAR_COLOR, null);
        setOption(ANGRY_WOLF, null);
        setOption(WOLF_VARIANT, null);
        setOption(WOLF_ARMOR, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getCollarColorItem(), true, true));
        items.add(getOptionItemStack(getAngryItem(), true, true));
        items.add(getOptionItemStack(getWolfVariantItem(), true, true));
        // No current way to add the armor to the wolf using the API.
        // items.add(getOptionItemStack(getWolfArmorItem(), false, false));
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
                setOption(COLLAR_COLOR, this.collarColor.name());
                return getOptionItemStack(getCollarColorItem(), true, true);
            }

            case "angry": {
                this.angry = !angry;
                setOption(ANGRY_WOLF, this.angry);
                return getOptionItemStack(getAngryItem(), false, false);
            }

            case "wolfvariant": {
                if (clickType.isRightClick()) {
                    this.wolfVariant = null;
                } else {
                    List<org.bukkit.entity.Wolf.Variant> wolfVariants = Registry.WOLF_VARIANT.stream().toList();

                    if (wolfVariants.indexOf(wolfVariant) == wolfVariants.size() - 1)
                        this.wolfVariant = wolfVariants.get(0);
                    else
                        this.wolfVariant = wolfVariants.get(wolfVariants.indexOf(wolfVariant) + 1);
                }
                setOption(WOLF_VARIANT, wolfVariant != null ? wolfVariant.getKeyOrNull().getKey() : null);
                return getOptionItemStack(getWolfVariantItem(), true, true);
            }

            case "wolfarmor": {
                this.wolfArmor = !this.wolfArmor;
                setOption(WOLF_ARMOR, this.wolfArmor);

                if(wolfArmor) {
                    customMob.addDrop(new Drop(new ItemStack(Material.WOLF_ARMOR), 1, "WolfArmor"));
                } else {
                    customMob.removeDropItem("WolfArmor");
                }

                return getOptionItemStack(getWolfArmorItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return entityType.getEntityClass() == org.bukkit.entity.Wolf.class;
    }

    public CustomMobsItem getCollarColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&b&lCollar color");
        String color = collarColor == null ? "&fDefault" : Utils.getChatColorOfColor(collarColor.name()) + Utils.getSentenceCase(collarColor.name());
        item.addLore("&eColor: " + color);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CollarColor");
        return item;
    }

    public CustomMobsItem getAngryItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_SWORD);
        String angry = this.angry ? "&a&lOn" : "&c&lOff";
        item.setName("&c&lAngry");
        item.addLore("&eAngry: " + angry);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Angry");
        return item;
    }

    public CustomMobsItem getWolfVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WOLF_SPAWN_EGG);
        item.setName("&b&lWolf variant");
        String variant = wolfVariant == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(wolfVariant.getKeyOrNull().getKey());
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "WolfVariant");
        return item;
    }

    public CustomMobsItem getWolfArmorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WOLF_ARMOR);
        String wolfArmor = this.wolfArmor ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lWolf armor");
        item.addLore("&eArmor: " + wolfArmor);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "WolfArmor");
        return item;
    }
}
