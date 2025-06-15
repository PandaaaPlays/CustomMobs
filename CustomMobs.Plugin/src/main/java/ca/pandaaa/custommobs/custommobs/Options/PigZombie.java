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

public class PigZombie extends CustomMobOption {
    /**
     * Indicates the anger duration (in ticks) of a zombified piglin, which states for how long the zombified piglin
     * will try to attack the player.
     * @minimum 0
     * @maximum 72000
     */
    private static final String ZOMBIFIED_PIGLIN_ANGER = "mob.zombified-piglin-anger";
    private int anger;

    public PigZombie(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.anger = getOption(ZOMBIFIED_PIGLIN_ANGER, Integer.class, 0);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.PigZombie))
            return;

        ((org.bukkit.entity.PigZombie) customMob).setAnger(anger);
    }

    @Override
    public void resetOptions() {
        setOption(ZOMBIFIED_PIGLIN_ANGER, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getAngerItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "anger": {
                if (clickType.isRightClick()) {
                    this.anger = 0;
                    setOption(ZOMBIFIED_PIGLIN_ANGER, this.anger);
                } else {
                    new IntegerGUI("Anger", false, 0, 72000, (value) -> {
                        this.anger = value;
                        setOption(ZOMBIFIED_PIGLIN_ANGER, this.anger);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, anger);
                }
                return getOptionItemStack(getAngerItem(), true, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.PigZombie.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getAngerItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLDEN_SWORD);
        item.setName("&6&lAnger");
        item.addLore("&eAnger cooldown timer: &f" + anger + " (" + String.format("%.1f", (double) anger / 20) + " sec)");
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Anger");
        return item;
    }
}
