package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkeletonHorse extends CustomMobOption {
    /**
     * Indicates whether the skeleton horse is a trap variant (spawns skeleton riders when triggered by player(s) nearby).
     */
    private static final String SKELETON_TRAP = "mob.skeleton-trap";
    private boolean trapped;

    public SkeletonHorse(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.trapped = getOption(SKELETON_TRAP, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.SkeletonHorse))
            return;

        ((org.bukkit.entity.SkeletonHorse) customMob).setTrapped(trapped);
    }

    public void resetOptions() {
        setOption(SKELETON_TRAP, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getTrappedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "trapped": {
                this.trapped = !trapped;
                setOption(SKELETON_TRAP, trapped);
                return getOptionItemStack(getTrappedItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.SkeletonHorse.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getTrappedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TRIPWIRE_HOOK);
        String trapped = this.trapped ? "&a&lOn" : "&c&lOff";
        item.setName("&4&lTrapped");
        item.addLore("&eTrapped: " + trapped);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Trapped");
        return item;
    }
}
