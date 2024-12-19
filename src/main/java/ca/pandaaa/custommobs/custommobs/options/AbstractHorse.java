package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
//TODO saddle drop on mob death (pig aussi)
public class AbstractHorse extends CustomMobOption {
    private Double jumpStrength;
    private boolean saddle;

    public AbstractHorse(Double jumpStrength, boolean saddle) {
        this.jumpStrength = jumpStrength;
        this.saddle = saddle;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.AbstractHorse))
            return;

        if(jumpStrength != null)
            ((org.bukkit.entity.AbstractHorse) customMob).setJumpStrength(jumpStrength);
        if(saddle)
            ((org.bukkit.entity.AbstractHorse) customMob).getInventory().setSaddle(new ItemStack(Material.SADDLE));

    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getJumpStrengthItem(), true, false));
        items.add(getOptionItemStack(getSaddleItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "jumpstrength": {
                if(clickType.isRightClick()) {
                    this.jumpStrength = 0.7D;
                    customMob.getCustomMobConfiguration().setJumpStrength(jumpStrength);
                } else {
                    new DoubleGUI("Jump strength", true, 0, 2, (value) -> {
                        this.jumpStrength = value;
                        customMob.getCustomMobConfiguration().setJumpStrength(jumpStrength);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, jumpStrength);
                }
                return getOptionItemStack(getJumpStrengthItem(), true, false);
            }
            case "saddle": {
                saddle = !saddle;
                customMob.getCustomMobConfiguration().setHasSaddle(saddle);
                new OptionsGUI(customMob).openInventory((Player) clicker, 1);

                return getOptionItemStack(getSaddleItem(), true, false);
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

    public CustomMobsItem getSaddleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SADDLE);
        item.setName("&a&lSaddle");
        String saddle = this.saddle ? "&a&lOn" : "&c&lOff";
        item.addLore("&eSaddle: &f" + saddle);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Saddle");
        return item;
    }
}
