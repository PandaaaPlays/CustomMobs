package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
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

public class AbstractHorse extends CustomMobOption {
    private static final String JUMP_STRENGTH = "mob.jump-strength";
    private Double jumpStrength;
    private static final String SADDLE = "mob.saddle";
    private boolean saddle;

    public AbstractHorse(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.jumpStrength = getOption(JUMP_STRENGTH, Double.class);
        this.saddle = getOption(SADDLE, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.AbstractHorse))
            return;

        if(jumpStrength != null)
            ((org.bukkit.entity.AbstractHorse) customMob).setJumpStrength(jumpStrength);
        if(saddle)
            ((org.bukkit.entity.Horse) customMob).getInventory().setSaddle(new ItemStack(Material.SADDLE));

    }

    @Override
    public void resetOptions() {
        setOption(JUMP_STRENGTH, null);
        setOption(SADDLE, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getJumpStrengthItem(), true, false));
        items.add(getOptionItemStack(getSaddleItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "jumpstrength": {
                if(clickType.isRightClick()) {
                    this.jumpStrength = null;
                    setOption(JUMP_STRENGTH, jumpStrength);
                } else {
                    new DoubleGUI("Jump strength", true, 0, 2, (value) -> {
                        this.jumpStrength = value;
                        setOption(JUMP_STRENGTH, jumpStrength);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, jumpStrength == null ? 0.7 : jumpStrength);
                }
                return getOptionItemStack(getJumpStrengthItem(), true, false);
            }
            case "saddle": {
                saddle = !saddle;
                setOption(SADDLE, saddle);

                if(saddle) {
                    customMob.addDrop(new Drop(new ItemStack(Material.SADDLE), 1, "Saddle"));
                } else {
                    customMob.removeDropItem("Saddle");
                }

                return getOptionItemStack(getSaddleItem(), true, false);
            }

        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.AbstractHorse.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getJumpStrengthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SLIME_BLOCK);
        item.setName("&a&lJump strength");
        item.addLore("&eJump strength: &f" + (jumpStrength == null ? "Random" : jumpStrength.toString()));
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "JumpStrength");
        return item;
    }

    public CustomMobsItem getSaddleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SADDLE);
        item.setName("&a&lSaddle");
        String saddle = this.saddle ? "&a&lOn" : "&c&lOff";
        item.addLore("&eSaddle: &f" + saddle);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Saddle");
        return item;
    }
}
