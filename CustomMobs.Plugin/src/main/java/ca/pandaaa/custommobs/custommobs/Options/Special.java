package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.NMS;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleRangeGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.DamageRange;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Special extends CustomMobOption {
    /**
     *  Toggles the visibility of the entity's name tag, even when not directly looking at it.
     */
    private static final String VISIBLE_NAME = "special.visible-name";
    private boolean isNameVisible;
    /**
     * Sets the entity's health (in half heart(s)). A health of 20 means that the CustomMob has 10 hearts of health.
     * @minimum 0
     * @maximum 1024
     */
    private static final String HEALTH = "special.health";
    private Double health;
    /**
     * Indicates whether the entity is in an aggressive state, which means that they will attack any nearby player (even
     * if the mob is normally passive).
     */
    private static final String AGGRESSIVE = "special.aggressive";
    private boolean aggressive;
    /**
     * Toggles the glowing effect around the entity.
     */
    private static final String GLOWING = "special.glowing";
    private boolean glowing;
    /**
     * When enabled, allows the entity to pick up dropped items.
     */
    private static final String CAN_PICKUP_LOOT = "special.can-pickup-loot";
    private boolean canPickupLoot;
    /**
     * Indicates how far the entity is knocked back when hit (a high value indicates that the CustomMob should not go far).
     * @minimum 0
     * @maximum 1
     */
    private static final String KNOCKBACK_RESISTANCE = "special.knockback-resistance";
    private double knockbackResistance;
    /**
     * Controls the movement speed of the entity. The default value is 0,3.
     * @minimum 0
     * @maximum 1024
     */
    private static final String SPEED = "special.speed";
    private double speed;
    /**
     * Defines the range of damage the entity can inflict. For a mob to inflict exactly "x" damage every time, set the
     * lower and higher bounds of the range to the same "x" value.
     * @minimum 0
     * @maximum 1024
     */
    private static final String DAMAGE = "special.damage";
    private DamageRange damageRange;
    /**
     * When enabled, prevents the entity from taking any damage.
     */
    private static final String INVINCIBLE = "special.invincible";
    private boolean invincible;
    /**
     * MWhen enabled, mutes all sounds made by the entity.
     */
    private static final String SILENT = "special.silent";
    private boolean silent;
    /**
     * Determines whether the entity is affected by gravity or not.
     */
    private static final String GRAVITY = "special.gravity";
    private boolean gravity;
    /**
     * When enabled, keeps the entity from despawning naturally.
     */
    private static final String PERSISTENT = "special.persistent";
    private boolean persistent;
    /**
     * Specifies whether the entity uses advanced AI behaviors (such as moving around, attacking, etc.).
     */
    private static final String INTELLIGENT = "special.intelligent";
    private boolean intelligent;
    /**
     * Sets the range at which the entity can detect and follow targets. The default value is 32.
     * @minimum 0
     * @maximum 2048
     */
    private static final String FOLLOW_RANGE = "special.follow-range";
    private double followRange;
    /**
     * Adjusts the entity’s physical size. The default value is 1.
     * @minimum 0.06
     * @maximum 16
     */
    private static final String SIZE = "special.size";
    private double size;
    /**
     * Toggles whether the entity drops default loot on death.
     */
    private static final String NATURAL_DROPS = "special.natural-drops";
    private boolean naturalDrops;

    public Special(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.isNameVisible = getOption(VISIBLE_NAME, Boolean.class, false);
        this.health = getOption(HEALTH, Double.class);
        this.aggressive = getOption(AGGRESSIVE, Boolean.class, false);
        this.glowing = getOption(GLOWING, Boolean.class, false);
        this.canPickupLoot = getOption(CAN_PICKUP_LOOT, Boolean.class, false);
        this.knockbackResistance = getOption(KNOCKBACK_RESISTANCE, Double.class, 0.0);
        this.speed = getOption(SPEED, Double.class, 0.3);
        this.damageRange = getOption(DAMAGE, DamageRange.class);
        this.invincible = getOption(INVINCIBLE, Boolean.class, false);
        this.silent = getOption(SILENT, Boolean.class, false);
        this.gravity = getOption(GRAVITY, Boolean.class, true);
        this.persistent = getOption(PERSISTENT, Boolean.class, false);
        this.intelligent = getOption(INTELLIGENT, Boolean.class, true);
        this.followRange = getOption(FOLLOW_RANGE, Double.class, 32.0);
        this.size = getOption(SIZE, Double.class, 1.0);
        this.naturalDrops = getOption(NATURAL_DROPS, Boolean.class, true);
    }

    public void applyOptions(Entity customMob) {
        customMob.setCustomNameVisible(isNameVisible);

        if(customMob instanceof Attributable) {
            if (health != null) {
                Objects.requireNonNull(((Attributable) customMob).getAttribute(Registry.ATTRIBUTE.get(NamespacedKey.minecraft("max_health")))).setBaseValue(health);
                ((LivingEntity) customMob).setHealth(health);
            }
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Registry.ATTRIBUTE.get(NamespacedKey.minecraft("knockback_resistance")))).setBaseValue(knockbackResistance);
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Registry.ATTRIBUTE.get(NamespacedKey.minecraft("movement_speed")))).setBaseValue(speed);
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Registry.ATTRIBUTE.get(NamespacedKey.minecraft("follow_range")))).setBaseValue(followRange);
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Registry.ATTRIBUTE.get(NamespacedKey.minecraft("scale")))).setBaseValue(size);
        }

        if(damageRange != null) {
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MinDamage"), PersistentDataType.DOUBLE, damageRange.getMinimumDamage());
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MaxDamage"), PersistentDataType.DOUBLE, damageRange.getMaximimDamage());
        }

        customMob.setGlowing(glowing);

        if(customMob instanceof org.bukkit.entity.LivingEntity) {
            ((org.bukkit.entity.LivingEntity) customMob).setCanPickupItems(canPickupLoot);
            ((org.bukkit.entity.LivingEntity) customMob).setAI(intelligent);
        }

        customMob.setInvulnerable(invincible);
        customMob.setSilent(silent);
        customMob.setGravity(gravity);
        customMob.setPersistent(persistent);

        if(customMob instanceof Animals && aggressive) {
            addAggressivity(customMob);
        }
    }

    public void resetOptions() {
        // default options do not need to be reset as they are applicable for all entity types.
    }

    private void addAggressivity(Entity customMob) {
        new NMS().setCustomMobAggressivity((Mob) customMob);
    }

    public ItemStack modifyOption(org.bukkit.entity.Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "visible": {
                this.isNameVisible = !isNameVisible;
                    setOption(VISIBLE_NAME, isNameVisible);
                return getOptionItemStack(getVisibleNameItem(), false, false);
            }

            case "health": {
                if(clickType.isRightClick()) {
                    this.health = null;
                    setOption(HEALTH, health);
                } else {
                    new DoubleGUI("Health", false, 0, 1024, (value) -> {
                        this.health = value;
                        setOption(HEALTH, health);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, health == null ? 20D : health);
                }
                return getOptionItemStack(getHealthItem(), true, false);
            }

            case "aggressive" : {
                this.aggressive = !aggressive;
                setOption(AGGRESSIVE, aggressive);
                return getOptionItemStack(getAggressiveItem(), false, false);
            }

            case "glowing" : {
                this.glowing = !glowing;
                setOption(GLOWING, glowing);
                return getOptionItemStack(getGlowingItem(), false, false);
            }

            case "canpickuploot" : {
                this.canPickupLoot = !canPickupLoot;
                setOption(CAN_PICKUP_LOOT, canPickupLoot);
                return getOptionItemStack(getCanPickupLootItem(), false, false);
            }

            case "knockbackresistance": {
                if(clickType.isRightClick()) {
                    this.knockbackResistance = 0D;
                    setOption(KNOCKBACK_RESISTANCE, knockbackResistance);
                } else {
                    new DoubleGUI("Knockback resistance", true, 0, 1, (value) -> {
                        this.knockbackResistance = value;
                        setOption(KNOCKBACK_RESISTANCE, knockbackResistance);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).setMinusPrettyValue("0.0").openInventory(clicker, knockbackResistance);
                }
                return getOptionItemStack(getKnockbackResistanceItem(), true, false);
            }

            case "speed": {
                if(clickType.isRightClick()) {
                    this.speed = 0.3D;
                    setOption(SPEED, speed);
                } else {
                    new DoubleGUI("Speed", false, 0, 1024, (value) -> {
                        this.speed = value;
                        setOption(SPEED, speed);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).setMinusPrettyValue("0.0").openInventory(clicker, speed);
                }
                return getOptionItemStack(getSpeedItem(), true, false);
            }

            case "damagerange": {
                if(clickType.isRightClick()) {
                    this.damageRange = null;
                    setOption(DAMAGE, damageRange);
                } else {
                    new DoubleRangeGUI("Damage range", false, 0, 1024, (value) -> {
                        this.damageRange = new DamageRange(value[0], value[1]);
                        setOption(DAMAGE, damageRange);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker,
                            damageRange == null ? 5 : damageRange.getMinimumDamage(),
                            damageRange == null ? 5 : damageRange.getMaximimDamage());
                }
                return getOptionItemStack(getDamageRangeItem(), true, false);
            }

            case "invincible" : {
                this.invincible = !invincible;
                setOption(INVINCIBLE, invincible);
                return getOptionItemStack(getInvincibleItem(), false, false);
            }

            case "silent" : {
                this.silent = !silent;
                setOption(SILENT, silent);
                return getOptionItemStack(getSilentItem(), false, false);
            }

            case "gravity" : {
                this.gravity = !gravity;
                setOption(GRAVITY, gravity);
                return getOptionItemStack(getGravityItem(), false, false);
            }

            case "persistent" : {
                this.persistent = !persistent;
                setOption(PERSISTENT, persistent);
                return getOptionItemStack(getPersistentItem(), false, false);
            }

            case "intelligent" : {
                this.intelligent = !intelligent;
                setOption(INTELLIGENT, intelligent);
                return getOptionItemStack(getIntelligentItem(), false, false);
            }

            case "followrange": {
                if(clickType.isRightClick()) {
                    this.followRange = 32D;
                    setOption(FOLLOW_RANGE, followRange);
                } else {
                    new DoubleGUI("Follow range", false, 0, 2048, (value) -> {
                        this.followRange = value;
                        setOption(FOLLOW_RANGE, followRange);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).setMinusPrettyValue("0.0").openInventory(clicker, followRange);
                }
                return getOptionItemStack(getFollowRangeItem(), true, false);
            }

            case "size": {
                if(clickType.isRightClick()) {
                    this.size = 1;
                    setOption(SIZE, size);
                } else {
                    new DoubleGUI("Size", false, 0.06, 16, (value) -> {
                        this.size = value;
                        setOption(SIZE, size);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, size);
                }
                return getOptionItemStack(getSizeItem(), true, false);
            }

            case "naturaldrops": {
                this.naturalDrops = !naturalDrops;
                setOption(NATURAL_DROPS, naturalDrops);
                return getOptionItemStack(getNaturalDropsItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return true;
    }

    public boolean getNaturalDrops() {
        return naturalDrops;
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getVisibleNameItem(), false, false));
        items.add(getOptionItemStack(getHealthItem(), true, false));
        items.add(getOptionItemStack(getAggressiveItem(), false, false));
        items.add(getOptionItemStack(getGlowingItem(), false, false));
        items.add(getOptionItemStack(getCanPickupLootItem(), false, false));
        items.add(getOptionItemStack(getKnockbackResistanceItem(), true, false));
        items.add(getOptionItemStack(getSpeedItem(), true, false));
        items.add(getOptionItemStack(getDamageRangeItem(), true, false));
        items.add(getOptionItemStack(getInvincibleItem(), false, false));
        items.add(getOptionItemStack(getSilentItem(), false, false));
        items.add(getOptionItemStack(getGravityItem(), false, false));
        items.add(getOptionItemStack(getPersistentItem(), false, false));
        items.add(getOptionItemStack(getIntelligentItem(), false, false));
        items.add(getOptionItemStack(getFollowRangeItem(), true, false));
        items.add(getOptionItemStack(getSizeItem(), true, false));
        items.add(getOptionItemStack(getNaturalDropsItem(), false, false));

        return items;
    }

    public CustomMobsItem getVisibleNameItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GLOW_INK_SAC);
        String visible = this.isNameVisible ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lName always visible");
        item.addLore("&eVisible: " + visible);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Visible");
        return item;
    }

    public CustomMobsItem getHealthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLDEN_APPLE);
        item.setName("&6&lHealth");
        if (health != null)
            item.addLore("&eHealth: &f" + health + " (" + health / 2 + " ❤)");
        else
            item.addLore("&eHealth: &fDefault entity value");
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Health");
        return item;
    }

    public CustomMobsItem getAggressiveItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_SWORD);
        String aggressive = this.aggressive ? "&a&lOn" : "&c&lOff";
        item.setName("&c&lAggressive");
        item.addLore("&eAggressive: " + aggressive);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Aggressive");
        return item;
    }

    public CustomMobsItem getGlowingItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NETHER_STAR);
        String glowing = this.glowing ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lGlowing");
        item.addLore("&eGlowing: " + glowing);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Glowing");
        return item;
    }

    public CustomMobsItem getCanPickupLootItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CARROT);
        String canPickupLoot = this.canPickupLoot ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lCan pickup loot");
        item.addLore("&eCan pickup loot: " + canPickupLoot);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CanPickupLoot");
        return item;
    }

    public CustomMobsItem getKnockbackResistanceItem() {
        CustomMobsItem item = new CustomMobsItem(Material.STICK);
        item.addEnchantment(Enchantment.KNOCKBACK, 1, false);
        item.setName("&a&lKnockback resistance");
        item.addLore("&eKnockback resistance: &f" + knockbackResistance);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "KnockbackResistance");
        return item;
    }

    public CustomMobsItem getSpeedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_BOOTS);
        item.addEnchantment(Enchantment.PROTECTION, 1, false);
        item.setName("&b&lSpeed");
        item.addLore("&eSpeed: &f" + speed);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Speed");
        return item;
    }

    public CustomMobsItem getDamageRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WOODEN_SWORD);
        item.setName("&c&lDamage range");
        if(damageRange != null) {
            String min = damageRange.getMinimumDamage() + " (" + damageRange.getMinimumDamage() / 2 + " ❤)";
            String max = damageRange.getMaximimDamage() + " (" + damageRange.getMaximimDamage() / 2 + " ❤)";
            item.addLore("&eRange: &f" + min + " - " + max);
        } else {
            item.addLore("&eRange: &fDefault mob value");
        }
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "DamageRange");
        return item;
    }

    public CustomMobsItem getInvincibleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENCHANTED_GOLDEN_APPLE);
        String invincible = this.invincible ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lInvincible");
        item.addLore("&eInvincible: " + invincible);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Invincible");
        return item;
    }

    public CustomMobsItem getSilentItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SCULK_SHRIEKER);
        String silent = this.silent ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lSilent");
        item.addLore("&eSilent: " + silent);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Silent");
        return item;
    }

    public CustomMobsItem getGravityItem() {
        CustomMobsItem item = new CustomMobsItem(Material.APPLE);
        String gravity = this.gravity ? "&a&lOn" : "&c&lOff";
        item.setName("&2&lGravity");
        item.addLore("&eGravity: " + gravity);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Gravity");
        return item;
    }

    public CustomMobsItem getPersistentItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NAME_TAG);
        String persistent = this.persistent ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lPersistent");
        item.addLore("&eMob cannot dispawn: " + persistent);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Persistent");
        return item;
    }

    public CustomMobsItem getIntelligentItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LIGHT);
        String intelligent = this.intelligent ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lIntelligent");
        item.addLore("&eIntelligent: " + intelligent);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Intelligent");
        return item;
    }

    public CustomMobsItem getFollowRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPYGLASS);
        item.setName("&c&lFollow range");
        item.addLore("&eFollow range: &f" + followRange);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "FollowRange");
        return item;
    }

    public CustomMobsItem getSizeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LARGE_AMETHYST_BUD);
        item.setName("&b&lSize");
        item.addLore("&eSize: &f" + size);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Size");
        return item;
    }

    public CustomMobsItem getNaturalDropsItem() {
        CustomMobsItem item = new CustomMobsItem(Material.IRON_SHOVEL);
        item.setName("&9&lNatural drops");
        String naturalDrops = this.naturalDrops ? "&a&lOn" : "&c&lOff";
        item.addLore("&eNatural mob drops: &f" + naturalDrops);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "NaturalDrops");
        return item;
    }
}
