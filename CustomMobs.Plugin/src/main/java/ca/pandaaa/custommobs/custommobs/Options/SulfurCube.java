package ca.pandaaa.custommobs.custommobs.Options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;

public class SulfurCube extends CustomMobOption {
    /**
     * Specifies the block material inside the sulfur cube CustomMob.
     */
    private static final String SULFUR_CUBE_BLOCK = "mob.sulfur-cube-block";
    private Material sulfurCubeBlock;

    public SulfurCube(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.sulfurCubeBlock = getOption(SULFUR_CUBE_BLOCK, Material.class);
    }

    @Override
    public void applyOptions(Entity customMob) {
        if (!(customMob instanceof org.bukkit.entity.SulfurCube sulfurCube))
            return;

        if (sulfurCubeBlock != null) {
            ((org.bukkit.entity.SulfurCube) customMob).getEquipment().setItem(EquipmentSlot.BODY,
                    new ItemStack(sulfurCubeBlock));
        }
    }

    @Override
    public void resetOptions() {
        setOption(SULFUR_CUBE_BLOCK, null);
    }

    @Override
    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getOptionItemStack(getSulfurCubeBlockItem(), true, false));
        return items;
    }

    @Override
    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch (option.toLowerCase()) {
            case "sulfurcubeblock":
                if (clickType.isRightClick()) {
                    this.sulfurCubeBlock = null;
                } else {
                    ItemStack cursorItem = clicker.getItemOnCursor();
                    if (cursorItem != null && cursorItem.getType() != Material.AIR && cursorItem.getType().isBlock()) {
                        this.sulfurCubeBlock = cursorItem.getType();
                    }
                }
                setOption(SULFUR_CUBE_BLOCK, this.sulfurCubeBlock != null ? this.sulfurCubeBlock.name() : null);
                return getOptionItemStack(getSulfurCubeBlockItem(), true, false);
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("26.2") && org.bukkit.entity.SulfurCube.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getSulfurCubeBlockItem() {
        Material displayMaterial = (sulfurCubeBlock != null && sulfurCubeBlock.isItem()) ? sulfurCubeBlock : Material.SULFUR;

        CustomMobsItem item = new CustomMobsItem(displayMaterial);
        item.setName("&e&lSulfur cube block");
        String blockName = sulfurCubeBlock == null ? "&fNone" : "&f" + Utils.getSentenceCase(sulfurCubeBlock.name());
        item.addLore("&eInside block: " + blockName);
        item.addLore("&7&o(( Drag & drop a block to change this option ))");
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "SulfurCubeBlock");
        return item;
    }
}
