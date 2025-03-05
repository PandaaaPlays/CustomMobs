package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PigZombie extends CustomMobOption {
    private int anger;

    public PigZombie(Integer anger) {
        this.anger = anger;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.PigZombie))
            return;

        ((org.bukkit.entity.PigZombie) customMob).setAnger(anger);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getAngerItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "anger": {
                if (clickType.isRightClick()) {
                    this.anger = 0;
                    customMob.getCustomMobConfiguration().setZombifiedPiglinAnger(anger);
                } else {
                    new IntegerGUI("Anger", false, 0, 72000, (value) -> {
                        this.anger = value;
                        customMob.getCustomMobConfiguration().setZombifiedPiglinAnger(anger);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, anger);
                }
                return getOptionItemStack(getAngerItem(), true, false);
            }
        }
        return null;
    }

    public CustomMobsItem getAngerItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLDEN_SWORD);
        item.setName("&6&lAnger");
        item.addLore("&eAnger cooldown timer: &f" + anger + " (" + String.format("%.1f", (double) anger / 20) + " sec)");
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Anger");
        return item;
    }
}
