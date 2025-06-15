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

public class Sittable extends CustomMobOption {
    /**
     * Determines whether the CustomMob entity is currently in a sitting state.
     */
    private static final String SITTING = "mob.sitting";
    private boolean sitting;

    public Sittable(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.sitting = getOption(SITTING, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Sittable))
            return;

        ((org.bukkit.entity.Sittable) customMob).setSitting(sitting);
    }

    @Override
    public void resetOptions() {
        setOption(SITTING, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getSittingItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "sitting": {
                this.sitting = !sitting;
                setOption(SITTING, sitting);
                return getOptionItemStack(getSittingItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Sittable.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getSittingItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BONE);
        String sitting = this.sitting ? "&a&lOn" : "&c&lOff";
        item.setName("&b&lSitting");
        item.addLore("&eSitting: " + sitting);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Sitting");
        return item;
    }
}
