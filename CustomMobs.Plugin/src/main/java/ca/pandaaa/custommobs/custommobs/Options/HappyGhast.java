package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HappyGhast extends CustomMobOption {
    /**
     * Represents the harness equipped on the happy ghast CustomMob.
     */
    private static final String HARNESS = "mob.harness";
    private Material harness;

    public HappyGhast(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.harness = getOption(HARNESS, Material.class);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.HappyGhast))
            return;
        if(harness != null)
            ((org.bukkit.entity.HappyGhast) customMob).getEquipment().setItem(EquipmentSlot.BODY, new ItemStack(harness));
    }

    @Override
    public void resetOptions() {
        setOption(HARNESS, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getHarnessItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "harness": {
                if(clickType.isRightClick()) {
                    this.harness = null;
                } else {
                    List<Material> armors = new ArrayList<>();
                    for(DyeColor color : DyeColor.values()) {
                        armors.add(Material.getMaterial(color.name().toUpperCase() + "_HARNESS"));
                    }
                    if (armors.indexOf(harness) == armors.size() - 1)
                        this.harness = armors.get(0);
                    else
                        this.harness = armors.get(armors.indexOf(harness) + 1);
                }
                setOption(HARNESS, this.harness != null ? this.harness.toString() : null);

                if(harness != null) {
                    customMob.addDrop(new Drop(new ItemStack(harness), 1, "Harness"));
                } else {
                    customMob.removeDropItem("Harness");
                }

                return getOptionItemStack(getHarnessItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.HappyGhast.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getHarnessItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GREEN_HARNESS);
        item.setName("&b&lHappy ghast harness");
        String harnessString = harness == null ? "&fNone" : Utils.getChatColorOfColor(harness.toString().replaceAll("_HARNESS", "")) + Utils.getSentenceCase(harness.toString());
        item.addLore("&eHarness: &f" + harnessString);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Harness");
        return item;
    }
}
