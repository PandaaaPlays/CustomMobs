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
     * Represents the cooldown time (in ticks) before the creeper CustomMob explodes when it is near a player.
     * @minimum 0
     * @maximum 1200
     */
    private static final String EXPLOSION_COOLDOWN = "mob.explosion-cooldown";
    private int explosionCooldown;
    /**
     * Represents the distance (in block(s)) that the nearest player needs to be from the creeper CustomMob for it
     * to explode.
     * @minimum 0
     * @maximum 128
     */
    private static final String EXPLOSION_RADIUS = "mob.explosion-radius";
    private int explosionRadius;
    /**
     * Indicates whether the creeper is charged (electrified), making its explosion more powerful.
     */
    private static final String CHARGED_CREEPER = "mob.charged-creeper";
    private boolean charged;

    public Creeper(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.explosionCooldown = getOption(EXPLOSION_COOLDOWN, Integer.class, 30);
        this.explosionRadius = getOption(EXPLOSION_RADIUS, Integer.class, 3);
        this.charged = getOption(CHARGED_CREEPER, Boolean.class, false);
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
        setOption(EXPLOSION_COOLDOWN, null);
        setOption(EXPLOSION_RADIUS, null);
        setOption(CHARGED_CREEPER, null);
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
                    setOption(EXPLOSION_COOLDOWN, explosionCooldown);
                } else {
                    new IntegerGUI("Explosion cooldown", false, 0, 1200, (value) -> {
                        this.explosionCooldown = value;
                        setOption(EXPLOSION_COOLDOWN, explosionCooldown);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, explosionCooldown);
                }
                return getOptionItemStack(getExplosionCooldownItem(), true, false);
            }

            case "explosionradius": {
                if(clickType.isRightClick()) {
                    this.explosionRadius = charged ? 6 : 3;
                    setOption(EXPLOSION_RADIUS, explosionRadius);
                } else {
                    new IntegerGUI("Explosion radius", false, 0, 128, (value) -> {
                        this.explosionRadius = value;
                        setOption(EXPLOSION_RADIUS, explosionRadius);
                        new OptionsGUI(customMob).openInventory(clicker, 1);
                    }).openInventory(clicker, explosionRadius);
                }
                return getOptionItemStack(getExplosionRadiusItem(), true, false);
            }

            case "charged": {
                this.charged = !charged;
                setOption(CHARGED_CREEPER, charged);
                return getOptionItemStack(getChargedItem(), false, false);
            }

        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Creeper.class.isAssignableFrom(entityType.getEntityClass());
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
