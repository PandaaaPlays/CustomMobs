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

public class Zombie extends CustomMobOption {
    /**
     * Determines whether the zombie CustomMob can break doors. This option is used to configure hostile behavior
     * against villages or structures.
     */
    private static final String CAN_BREAK_DOORS = "mob.can-break-doors";
    private boolean canBreakDoors;

    public Zombie(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.canBreakDoors = getOption(CAN_BREAK_DOORS, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Zombie))
            return;

        ((org.bukkit.entity.Zombie) customMob).setCanBreakDoors(canBreakDoors);
    }

    @Override
    public void resetOptions() {
        setOption(CAN_BREAK_DOORS, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getCanBreakDoorsItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "canbreakdoors": {
                this.canBreakDoors = !canBreakDoors;
                setOption(CAN_BREAK_DOORS, this.canBreakDoors);
                return getOptionItemStack(getCanBreakDoorsItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Zombie.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getCanBreakDoorsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.OAK_DOOR);
        String canBreakDoors = this.canBreakDoors ? "&a&lOn" : "&c&lOff";
        item.setName("&c&lCan break doors");
        item.addLore("&eCan break doors: " + canBreakDoors);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CanBreakDoors");
        return item;
    }
}
