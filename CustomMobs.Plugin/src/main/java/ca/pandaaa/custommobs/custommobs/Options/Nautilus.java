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
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;

public class Nautilus extends CustomMobOption {
    /**
     * Represents the armor item equipped on the nautilus CustomMob.
     */
    private static final String NAUTILUS_ARMOR = "mob.nautilus-armor";
    private Material nautilusArmor;

    public Nautilus(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.nautilusArmor = getOption(NAUTILUS_ARMOR, Material.class);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Nautilus))
            return;

        if(nautilusArmor != null)
            ((org.bukkit.entity.Nautilus) customMob).getEquipment().setItem(EquipmentSlot.BODY, new ItemStack(nautilusArmor));
    }

    @Override
    public void resetOptions() {
        setOption(NAUTILUS_ARMOR, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getNautilusArmorItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "nautilusarmor": {
                if(clickType.isRightClick()) {
                    this.nautilusArmor = null;
                } else {
                    List<Material> armors = new ArrayList<>();
                    armors.add(Material.COPPER_NAUTILUS_ARMOR);
                    armors.add(Material.IRON_NAUTILUS_ARMOR);
                    armors.add(Material.GOLDEN_NAUTILUS_ARMOR);
                    armors.add(Material.DIAMOND_NAUTILUS_ARMOR);
                    armors.add(Material.NETHERITE_NAUTILUS_ARMOR);
                    if (armors.indexOf(nautilusArmor) == armors.size() - 1)
                        this.nautilusArmor = armors.get(0);
                    else
                        this.nautilusArmor = armors.get(armors.indexOf(nautilusArmor) + 1);
                }
                setOption(NAUTILUS_ARMOR, this.nautilusArmor != null ? this.nautilusArmor.toString() : null);

                if(nautilusArmor != null) {
                    customMob.addDrop(new Drop(new ItemStack(nautilusArmor), 1, "NautilusArmor"));
                } else {
                    customMob.removeDropItem("NautilusArmor");
                }

                return getOptionItemStack(getNautilusArmorItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("1.21.11") && org.bukkit.entity.Nautilus.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getNautilusArmorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLDEN_NAUTILUS_ARMOR);
        item.setName("&b&lNautilus armor");
        String armor = nautilusArmor == null ? "&fNone" : "&f" + Utils.getSentenceCase(nautilusArmor.toString());
        item.addLore("&eArmor: &f" + armor);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "NautilusArmor");
        return item;
    }
}
