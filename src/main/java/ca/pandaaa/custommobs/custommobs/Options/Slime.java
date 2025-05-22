package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Slime extends CustomMobOption {
    /**
     * Integer value indicating the size of the slime CustomMob (affects health and split count).
     * @minimum 0
     * @maximum 126
     */
    private static final String SLIME_SIZE = "mob.slime-size";
    private Integer size;

    public Slime(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.size = getOption(SLIME_SIZE, Integer.class);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Slime))
            return;

        if(size != null)
            ((org.bukkit.entity.Slime) customMob).setSize(size);
    }

    @Override
    public void resetOptions() {
        setOption(SLIME_SIZE, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getOptionItemStack(getSlimeSizeItem(), true, false));
        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "slimesize": {
                if (clickType.isRightClick()) {
                    this.size = null;
                    setOption(SLIME_SIZE, size);
                } else {
                    new IntegerGUI("Slime size", false,0, 126, (value) -> {
                        this.size = value;
                        setOption(SLIME_SIZE, size);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, size == null ? 0 : size);
                }
                return getOptionItemStack(getSlimeSizeItem(), true, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Slime.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getSlimeSizeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SLIME_BALL);
        item.setName("&a&lSlime size");
        String size = this.size == null ? "&fNatural random (0-1-3)" : "&f" + this.size;
        item.addLore("&eSlime size: " + size);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "SlimeSize");
        return item;
    }
}
