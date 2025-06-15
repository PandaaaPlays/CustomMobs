package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.*;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GravityPull extends CustomMobCustomEffect {

    /**
     * Determines the strength of the "pulling" effect, which indicates how fast (and "strongly") the players will be
     * pulled towards the CustomMob.
     * @minimum 0.5
     * @maximum 5
     */
    private static final String GRAVITY_STRENGTH = "custom-effects.gravitypull.gravity-strength";
    private double gravityStrength;

    /**
     * Determines the radius (in block(s)) around the CustomMob where the effect should affect player(s).
     * @minimum 1
     * @maximum 32
     */
    private static final String PULL_RADIUS = "custom-effects.gravitypull.pull-radius";
    private int pullRadius;

    public GravityPull(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.COOLDOWN);
        gravityStrength = getCustomEffectOption(GRAVITY_STRENGTH, Double.class, 2D);
        pullRadius = getCustomEffectOption(PULL_RADIUS, Integer.class, 20);
    }

    public void triggerCustomEffect(Entity entity) {
        List<Entity> playersAround = entity.getNearbyEntities(pullRadius, pullRadius, pullRadius).stream().filter(e -> e instanceof Player).toList();
        for(Entity player : playersAround) {
            Vector pullDirection = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            Vector pullVelocity = pullDirection.multiply(gravityStrength);
            player.setVelocity(pullVelocity);
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "gravitystrength": {
                if(clickType.isRightClick()) {
                    gravityStrength = 2D;
                    setCustomEffectOption(GRAVITY_STRENGTH, this.gravityStrength);
                } else {
                    new DoubleGUI("Gravity strength", false, 0.5, 5, (value) -> {
                        this.gravityStrength = value;
                        setCustomEffectOption(GRAVITY_STRENGTH, this.gravityStrength);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, gravityStrength);
                }
                return getCustomEffectOptionItemStack(getGravityStrengthItem(), true);
            }
            case "pullradius": {
                if(clickType.isRightClick()) {
                    pullRadius = 20;
                    setCustomEffectOption(PULL_RADIUS, this.pullRadius);
                } else {
                    new IntegerGUI("Pull radius", false, 1, 32, (value) -> {
                        this.pullRadius = value;
                        setCustomEffectOption(PULL_RADIUS, this.pullRadius);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, pullRadius);
                }
                return getCustomEffectOptionItemStack(getPullRadiusItem(), true);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NETHER_STAR);
        item.setName("&6&lGravity pull");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eGravity pull: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Pulls the players toward the CustomMob ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getGravityStrengthItem(), true));
        items.add(getCustomEffectOptionItemStack(getPullRadiusItem(), true));
        items.add(getMessageItem());
        return items;
    }

    private CustomMobsItem getGravityStrengthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ANVIL);
        item.setName("&3&lGravity strength");
        item.addLore("&eGravity strength: &f" + this.gravityStrength);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".GravityStrength");
        return item;
    }

    private CustomMobsItem getPullRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENDER_PEARL);
        item.setName("&a&lPull radius");
        item.addLore("&ePulling radius: &f" + pullRadius + " blocks");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".PullRadius");
        return item;
    }
}