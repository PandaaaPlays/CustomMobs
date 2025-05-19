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

public class ChestedHorse extends CustomMobOption {
    private static final String CHEST = "mob.chested";
    private boolean chested;

    public ChestedHorse(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.chested = getOption(CHEST, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.ChestedHorse))
            return;

        ((org.bukkit.entity.ChestedHorse) customMob).setCarryingChest(chested);
    }

    @Override
    public void resetOptions() {
        setOption(CHEST, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getChestedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "chested": {
                this.chested = !chested;
                setOption(CHEST, chested);
                return getOptionItemStack(getChestedItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.ChestedHorse.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getChestedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CHEST);
        String chested = this.chested ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lChested");
        item.addLore("&eChested: " + chested);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "chested");
        return item;
    }
}
