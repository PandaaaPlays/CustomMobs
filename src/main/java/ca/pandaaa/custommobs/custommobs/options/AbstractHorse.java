package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AbstractHorse extends CustomMobOption {
    private Double jumpStrength;

    public AbstractHorse(Double jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.AbstractHorse))
            return;

        if(jumpStrength != null)
            ((org.bukkit.entity.AbstractHorse) customMob).setJumpStrength(jumpStrength);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getJumpStrengthItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "jumpstrength": {
                if(clickType.isRightClick()) {
                    this.jumpStrength = 0.7D;
                    customMob.getCustomMobConfiguration().setJumpStrength(jumpStrength);
                } else {
                    new DoubleGUI("Jump strength", customMob, true, 0, 2, (value) -> {
                        this.jumpStrength = value;
                        customMob.getCustomMobConfiguration().setJumpStrength(jumpStrength);
                    }).openInventory(clicker, jumpStrength);
                }
                return getOptionItemStack(getJumpStrengthItem(), true, false);
            }

        }
        return null;
    }

    public CustomMobsItem getJumpStrengthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SLIME_BLOCK);
        item.setName("&a&lJump strength");
        item.addLore("&eJump strength: &f" + jumpStrength);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "JumpStrength");
        return item;
    }
}
