package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fireball extends CustomMobCustomEffect {
    /**
     * Determines the strength (size) of the explosion created by the CustomMob's fireball upon trigger of this
     * custom effect.
     * @minimum 1
     * @maximum 100
     */
    private static final String EXPLOSION_STRENGTH = "custom-effects.fireball.explosion-strength";
    private int explosionStrength;

    /**
     * Sends a purple fireball (like the vanilla Dragon) instead of the Ghast one. This fireball will
     * leave a dragon's breath potion effect on the ground.
     */
    private static final String DRAGON_FIREBALL = "custom-effects.fireball.dragon-fireball";
    private boolean dragonFireball;

    /**
     * When enabled, a fireball is sent to one of the random nearby player(s) (in radius range). Otherwise,
     * a fireball is sent to every nearby player(s).
     */
    private static final String SHOOT_ONLY_ONE_PLAYER = "custom-effects.fireball.shoot-only-one-player";
    private boolean shootOnlyOnePlayer;

    /**
     * Determines whether the custom effect's fireball explosion should break blocks or not. The
     * dragon fireball cannot break blocks.
     */
    private static final String BREAK_BLOCKS = "custom-effects.fireball.break-blocks";
    private boolean breakBlocks;

    /**
     * Determines the radius (in block(s)) around the CustomMob where the player(s) could receive fireball(s).
     * @minimum 1
     * @maximum 32
     */
    private static final String RADIUS = "custom-effects.fireball.radius";
    private int radius;

    public Fireball(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.COOLDOWN);

        this.explosionStrength = getCustomEffectOption(EXPLOSION_STRENGTH, Integer.class, 5);
        this.dragonFireball = getCustomEffectOption(DRAGON_FIREBALL, Boolean.class, false);
        this.shootOnlyOnePlayer = getCustomEffectOption(SHOOT_ONLY_ONE_PLAYER, Boolean.class, true);
        this.breakBlocks = getCustomEffectOption(BREAK_BLOCKS, Boolean.class, false);
        this.radius = getCustomEffectOption(RADIUS, Integer.class, 25);
    }

    public void triggerCustomEffect(Entity entity) {
        List<Entity> playersAround = entity.getNearbyEntities(radius, radius, radius).stream().filter(e -> e instanceof Player).toList();
        if (!playersAround.isEmpty()) {
            if (shootOnlyOnePlayer) {
                Player target = (Player) playersAround.get(new Random().nextInt(playersAround.size()));
                shootFireball(entity, target);
            } else {
                for (Entity target : playersAround) {
                    shootFireball(entity, (Player) target);
                }
            }
        }
    }

    private void shootFireball(Entity shooter, Player target) {
        Vector direction = target.getLocation().toVector()
                .subtract(shooter.getLocation().toVector())
                .normalize();

        if (dragonFireball) {
            DragonFireball fireball = shooter.getWorld().spawn(shooter.getLocation().add(0, 1, 0), DragonFireball.class);
            fireball.setDirection(direction);
            fireball.setShooter((ProjectileSource) shooter);
        } else {
            org.bukkit.entity.Fireball fireball = shooter.getWorld().spawn(shooter.getLocation().add(0, 1, 0), org.bukkit.entity.Fireball.class);
            fireball.setDirection(direction);
            fireball.setYield(breakBlocks ? explosionStrength : 0F);
            fireball.setIsIncendiary(false);
            fireball.setShooter((ProjectileSource) shooter);
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "explosionstrength": {
                if(clickType.isRightClick()) {
                    this.explosionStrength = 5;
                    setCustomEffectOption(EXPLOSION_STRENGTH, this.explosionStrength);
                } else {
                    new IntegerGUI("Explosion strength", false, 1, 100, (value) -> {
                        this.explosionStrength = value;
                        setCustomEffectOption(EXPLOSION_STRENGTH, this.explosionStrength);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, explosionStrength);
                }
                return getCustomEffectOptionItemStack(getExplosionStrengthItem(), true);
            }
            case "dragonfireball": {
                this.dragonFireball = !this.dragonFireball;
                setCustomEffectOption(DRAGON_FIREBALL, this.dragonFireball);
                return getCustomEffectOptionItemStack(getDragonFireballItem(), false);
            }
            case "shootonlyoneplayer": {
                this.shootOnlyOnePlayer = !this.shootOnlyOnePlayer;
                setCustomEffectOption(SHOOT_ONLY_ONE_PLAYER, this.shootOnlyOnePlayer);
                return getCustomEffectOptionItemStack(getShootTypeItem(), false);
            }
            case "breakblocks": {
                this.breakBlocks = !this.breakBlocks;
                setCustomEffectOption(BREAK_BLOCKS, this.breakBlocks);
                return getCustomEffectOptionItemStack(getBreakBlocksItem(), false);
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
        CustomMobsItem item = new CustomMobsItem(Material.FIRE_CHARGE);
        item.setName("&6&lFireball");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eFireball: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Shoots a fireball to nearby player(s) ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getExplosionStrengthItem(), true));
        items.add(getCustomEffectOptionItemStack(getDragonFireballItem(), false));
        items.add(getCustomEffectOptionItemStack(getShootTypeItem(), false));
        items.add(getCustomEffectOptionItemStack(getBreakBlocksItem(), false));
        items.add(getCustomEffectOptionItemStack(getRadiusItem(), true));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getExplosionStrengthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TNT);
        item.setName("&c&lExplosion strength");
        item.addLore("&eRadius: &f" + explosionStrength);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".ExplosionStrength");
        return item;
    }

    private CustomMobsItem getDragonFireballItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DRAGON_BREATH);
        item.setName("&d&lDragon fireball");
        item.addLore("&eSend dragon fireball: &f" + (this.dragonFireball ? "&a&lOn" : "&c&lOff"));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".DragonFireball");
        return item;
    }

    private CustomMobsItem getShootTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PLAYER_HEAD);
        item.setName("&2&lFireball target");
        item.addLore("&eTarget: &f" + (this.shootOnlyOnePlayer ? "One random player" : "Every nearby players"));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".ShootOnlyOnePlayer");
        return item;
    }

    private CustomMobsItem getBreakBlocksItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_PICKAXE);
        item.setName("&2&lBreak blocks");
        item.addLore("&eBreak blocks: &f" + (this.breakBlocks ? "&a&lOn" : "&c&lOff"));
        item.addLore("&c&l[!] &cDragon fireball cannot break blocks.");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".BreakBlocks");
        return item;
    }

    private CustomMobsItem getRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENDER_PEARL);
        item.setName("&a&lRadius");
        item.addLore("&eNearby radius: &f" + radius + " block(s)");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".Radius");
        return item;
    }
}