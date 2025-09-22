package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
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

public class Trampoline extends CustomMobCustomEffect {

    /**
     * Determines the height (in block(s)) to which the players should be sent in the air.
     * @minimum 1
     * @maximum 50
     */
    private static final String HEIGHT = "custom-effects.trampoline.height";
    private int height;

    /**
     * Determines the radius (in block(s)) around the CustomMob where the effect should affect player(s).
     * @minimum 1
     * @maximum 32
     */
    private static final String RADIUS = "custom-effects.trampoline.radius";
    private int radius;

    public Trampoline(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.COOLDOWN);
        height = getCustomEffectOption(HEIGHT, Integer.class, 25);
        radius = getCustomEffectOption(RADIUS, Integer.class, 25);
    }

    public void triggerCustomEffect(Entity entity) {
        List<Entity> playersAround = entity.getNearbyEntities(radius, radius, radius).stream().filter(e -> e instanceof Player).toList();
        for(Entity player : playersAround) {
            Vector velocity = player.getVelocity();
            final double gravity = 0.08;
            velocity.setY(Math.sqrt(2 * gravity * height));
            player.setVelocity(velocity);
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch (option.toLowerCase()) {
            case "height": {
                if (clickType.isRightClick()) {
                    height = 25;
                    setCustomEffectOption(HEIGHT, this.height);
                } else {
                    new IntegerGUI("Height", false, 1, 50, (value) -> {
                        this.height = value;
                        setCustomEffectOption(HEIGHT, this.height);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, height);
                }
                return getCustomEffectOptionItemStack(getHeightItem(), true);
            }
            case "radius": {
                if (clickType.isRightClick()) {
                    radius = 25;
                    setCustomEffectOption(RADIUS, this.radius);
                } else {
                    new IntegerGUI("Radius", false, 1, 32, (value) -> {
                        this.radius = value;
                        setCustomEffectOption(RADIUS, this.radius);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, radius);
                }
                return getCustomEffectOptionItemStack(getRadiusItem(), true);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENDER_EYE);
        item.setName("&2&lTrampoline");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eTrampoline: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Shoots the nearby player(s) in the air ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getHeightItem(), true));
        items.add(getCustomEffectOptionItemStack(getRadiusItem(), true));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getHeightItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BEDROCK);
        item.setName("&9&lHeight");
        item.addLore("&ePropulsion height: &f" + height + " block(s)");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".Height");
        return item;
    }

    private CustomMobsItem getRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENDER_PEARL);
        item.setName("&a&lRadius");
        item.addLore("&eTrigger radius: &f" + radius + " block(s)");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".Radius");
        return item;
    }
}