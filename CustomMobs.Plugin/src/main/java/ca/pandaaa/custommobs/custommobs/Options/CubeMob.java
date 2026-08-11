package ca.pandaaa.custommobs.custommobs.Options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.Utils;
import ca.pandaaa.custommobs.utils.CustomMobsItem;

public class CubeMob extends CustomMobOption {
    /**
     * Integer value indicating the size of the cubic CustomMob.
     * @minimum 0
     * @maximum 126
     */
    private static final String CUBE_SIZE = "mob.cube-size";
    private Integer size;

    public CubeMob(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.size = getOption(CUBE_SIZE, Integer.class);
    }

    public void applyOptions(Entity customMob) {
        if (!(customMob instanceof org.bukkit.entity.AbstractCubeMob))
            return;

        if (size != null)
            ((org.bukkit.entity.AbstractCubeMob) customMob).setSize(size);
    }

    @Override
    public void resetOptions() {
        setOption(CUBE_SIZE, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getOptionItemStack(getCubeSizeItem(), true, false));
        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "cubesize": {
                if (clickType.isRightClick()) {
                    this.size = null;
                    setOption(CUBE_SIZE, size);
                } else {
                    new IntegerGUI("Cube size", false, 0, 126, (value) -> {
                        this.size = value;
                        setOption(CUBE_SIZE, size);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, size == null ? 0 : size);
                }
                return getOptionItemStack(getCubeSizeItem(), true, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("26.2") && org.bukkit.entity.AbstractCubeMob.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getCubeSizeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SLIME_BLOCK);
        item.setName("&a&lCube size");
        String size = this.size == null ? "&fNatural" : "&f" + this.size;
        item.addLore("&eCube size: " + size);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CubeSize");
        return item;
    }
}
