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

public class Slime extends CustomMobOption {
    private Integer size;

    public Slime(Integer size) {
        this.size = size;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Slime))
            return;

        if(size != null)
            ((org.bukkit.entity.Slime) customMob).setSize(size);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getSlimeSizeItem(), true, false));
// TODO other mobs scaling?
        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "slimesize": {
                if (clickType.isRightClick()) {
                    this.size = null;
                    customMob.getCustomMobConfiguration().setSlimeSize(size);
                } else {
                    new IntegerGUI("Slime size", customMob, false, 0, 126, (value) -> {
                        this.size = value;
                        customMob.getCustomMobConfiguration().setSlimeSize(size);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, size == null ? 0 : size);
                }
                return getOptionItemStack(getSlimeSizeItem(), true, false);
            }
        }
        return null;
    }

    public CustomMobsItem getSlimeSizeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SLIME_BALL);
        item.setName("&a&lSlime size");
        String size = this.size == null ? "&fNatural random (0-1-3)" : "&f" + this.size;
        item.addLore("&eSlime size: " + size);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "SlimeSize");
        return item;
    }
}
