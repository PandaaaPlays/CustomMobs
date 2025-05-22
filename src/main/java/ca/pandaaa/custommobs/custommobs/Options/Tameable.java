package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.PlayerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tameable extends CustomMobOption {
    /**
     * Stores the unique identifier (UUID) of the player who "owns" the tameable CustomMob. The "tamed" option must
     * also be toggled on for the mob to actually be "owned" by a player. The player will remain the owner even
     * after a username change.
     */
    private static final String OWNER = "mob.owner";
    private UUID owner;
    /**
     * Indicates if the entity has been tamed by a player (true/false).
     */
    private static final String TAMED = "mob.tamed";
    private boolean tamed;

    public Tameable(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.owner = getOption(OWNER, UUID.class);
        this.tamed = getOption(TAMED, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Tameable))
            return;

        ((org.bukkit.entity.Tameable) customMob).setTamed(tamed);
        if(owner != null)
            ((org.bukkit.entity.Tameable) customMob).setOwner(Bukkit.getPlayer(owner));
    }

    @Override
    public void resetOptions() {
        setOption(OWNER, null);
        setOption(TAMED, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getOwnerItem(), true, false));
        items.add(getOptionItemStack(getTamedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "owner": {
                if(clickType.isRightClick()) {
                    this.owner = null;
                    setOption(OWNER, null);
                    return getOptionItemStack(getOwnerItem(), true, false);
                } else {
                    new PlayerGUI("Select player", (value) -> {
                        if(value != null) {
                            this.owner = value.getUniqueId();
                            setOption(OWNER, this.owner.toString());
                        }
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, 1);
                }
            }

            case "tamed": {
                this.tamed = !tamed;
                setOption(TAMED, this.tamed);
                return getOptionItemStack(getTamedItem(), false, false);
            }

        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Tameable.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getOwnerItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PLAYER_HEAD);
        String owner = this.owner == null ? "&fNone" : "&f" + Bukkit.getOfflinePlayer(this.owner).getName();
        item.setName("&3&lOwner");
        item.addLore("&eOwner: " + owner);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Owner");
        return item;
    }

    public CustomMobsItem getTamedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.APPLE);
        String tamed = this.tamed ? "&a&lOn" : "&c&lOff";
        item.setName("&a&lTamed");
        item.addLore("&eTamed: " + tamed);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Tamed");
        return item;
    }
}
