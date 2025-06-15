package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

// "Ageable" mobs that cannot actually be babies : Parrot, Bat, Piglin Brute
public class Ageable extends CustomMobOption {
    /**
     * Specifies whether the entity is a baby (true/false), affecting size and behavior. If left unset, the CustomMob's age
     * will be randomly chosen (like the default behavior of minecraft mob spawning).
     */
    private static final String BABY = "mob.baby";
    private Boolean isBaby;

    public Ageable(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.isBaby = getOption(BABY, Boolean.class);
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Ageable.class.isAssignableFrom(entityType.getEntityClass())
                && entityType.getEntityClass() != org.bukkit.entity.Parrot.class
                && entityType.getEntityClass() != org.bukkit.entity.Frog.class
                && entityType.getEntityClass() != org.bukkit.entity.Bat.class
                && entityType.getEntityClass() != org.bukkit.entity.PiglinBrute.class;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Ageable))
            return;

        if(isBaby != null) {
            if (isBaby) {
                ((org.bukkit.entity.Ageable) customMob).setBaby();
                if (customMob instanceof Breedable)
                    ((org.bukkit.entity.Breedable) customMob).setAgeLock(true);
            } else
                ((org.bukkit.entity.Ageable) customMob).setAdult();
        }
    }

    public void resetOptions() {
        setOption(BABY, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getOptionItemStack(getAgeItem(), true, false));
        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "baby": {
                if(clickType.isRightClick())
                    this.isBaby = null;
                else
                    this.isBaby = this.isBaby == null ? true : !isBaby;

                setOption(BABY, isBaby);
                return getOptionItemStack(getAgeItem(), true, false);
            }
        }
        return null;
    }

    public CustomMobsItem getAgeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.EGG);
        String age;
        if (this.isBaby == null)
            age = "&fRandom";
        else
            age = this.isBaby ? "&bBaby" : "&2Adult";
        item.setName("&b&lAge");
        item.addLore("&eAge: " + age);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "baby");
        return item;
    }
}
