package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
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

public class Creeper extends CustomMobOption {
    /**
     * Represents the key for configuring the explosion cooldown of a Creeper entity.
     * This variable is used to retrieve or define the cooldown time before a Creeper
     * entity can perform another explosion.
     * @minimum 0
     * @maximum 1200
     */
    private static final String EXPLOSION_COOLDOWN = "mob.explosion-cooldown";
    private int explosionCooldown;
    /**
     * Represents the configuration key used to set or retrieve the explosion radius for a mob,
     * commonly associated with entities like Creepers.
     * This value determines the blast radius of an explosion caused by the mob.
     * The key is used within the configuration to dynamically adjust the explosion radius in the mob system.
     * @minimum 5
     * @maximum 1600
     */
    private static final String EXPLOSION_RADIUS = "mob.explosion-radius";
    private int explosionRadius;
    /**
     * Represents the identifier for the configuration of a charged creeper mob.
     *
     * This constant is used in the customization and configuration of the charged
     * creeper entity within the system, allowing specific options to be applied
     * or manipulated for it.
     *
     * The "mob.charged-creeper" identifier is likely used in configuration files
     * or system logic to reference the charged creeper entity type.
     */
    private static final String CHARGED_CREEPER = "mob.charged-creeper";
    private boolean charged;

    public Creeper(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.explosionCooldown = explosionCooldown;
        this.explosionRadius = explosionRadius;
        this.charged = charged;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Creeper))
            return;

        ((org.bukkit.entity.Creeper) customMob).setPowered(charged);
        ((org.bukkit.entity.Creeper) customMob).setMaxFuseTicks(explosionCooldown);
        ((org.bukkit.entity.Creeper) customMob).setExplosionRadius(explosionRadius);
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getExplosionCooldownItem(), true, false));
        items.add(getOptionItemStack(getExplosionRadiusItem(), true, false));
        items.add(getOptionItemStack(getChargedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "explosioncooldown": {
                if(clickType.isRightClick()) {
                    this.explosionCooldown = 30;
                    customMob.getCustomMobConfiguration().setExplosionCooldown(explosionCooldown);
                } else {
                    new IntegerGUI("Explosion cooldown", false, 0, 1200, (value) -> {
                        this.explosionCooldown = value;
                        customMob.getCustomMobConfiguration().setExplosionCooldown(explosionCooldown);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, explosionCooldown);
                }
                return getOptionItemStack(getExplosionCooldownItem(), true, false);
            }

            case "explosionradius": {
                if(clickType.isRightClick()) {
                    this.explosionRadius = charged ? 6 : 3;
                    customMob.getCustomMobConfiguration().setExplosionRadius(explosionRadius);
                } else {
                    new IntegerGUI("Explosion radius", false, 0, 128, (value) -> {
                        this.explosionRadius = value;
                        customMob.getCustomMobConfiguration().setExplosionRadius(explosionRadius);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, explosionRadius);
                }
                return getOptionItemStack(getExplosionRadiusItem(), true, false);
            }

            case "charged": {
                this.charged = !charged;
                customMob.getCustomMobConfiguration().setChargedCreeper(charged);
                return getOptionItemStack(getChargedItem(), false, false);
            }

        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    public CustomMobsItem getExplosionCooldownItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&c&lExplosion cooldown");
        item.addLore("&eExplosion cooldown: &f" + explosionCooldown + " (" + String.format("%.1f", (double) explosionCooldown / 20) + " sec)");
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ExplosionCooldown");
        return item;
    }

    public CustomMobsItem getExplosionRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NETHER_STAR);
        item.setName("&c&lExplosion radius");
        item.addLore("&eExplosion radius: &f" + explosionRadius);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ExplosionRadius");
        return item;
    }

    public CustomMobsItem getChargedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LIGHTNING_ROD);
        String charged = this.charged ? "&a&lOn" : "&c&lOff";
        item.setName("&b&lCharged");
        item.addLore("&eCharged: " + charged);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Charged");
        return item;
    }

}
