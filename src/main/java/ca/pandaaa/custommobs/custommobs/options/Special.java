package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.NMS;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.DamageRange;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Special extends CustomMobOption {
    private boolean isNameVisible;
    private Double health;
    private boolean aggressive;
    private boolean glowing;
    private boolean canPickupLoot;
    private double knockbackResistance;
    private double speed;
    private final DamageRange damageRange;
    private boolean invincible;
    private boolean silent;
    private boolean gravity;
    private boolean persistent;
    private boolean intelligent;
    private double followRange;
    private double size;
    //TODO change DDDDdddouble dans les configs ca devrait etre de double si pas besoin de Double
    public Special(boolean isNameVisible, Double health, boolean aggressive, boolean glowing, boolean canPickupLoot, Double knockbackResistance, Double speed, DamageRange damageRange, boolean invincible, boolean silent, boolean gravity, boolean persistent, boolean intelligent, Double followRange, double size) {
        this.isNameVisible = isNameVisible;
        this.health = health;
        this.aggressive = aggressive;
        this.glowing = glowing;
        this.canPickupLoot = canPickupLoot;
        this.knockbackResistance = knockbackResistance;
        this.speed = speed;
        this.damageRange = damageRange;
        this.invincible = invincible;
        this.silent = silent;
        this.gravity = gravity;
        this.persistent = persistent;
        this.intelligent = intelligent;
        this.followRange = followRange;
        this.size = size;
    }

    public void applyOptions(Entity customMob) {
        customMob.setCustomNameVisible(isNameVisible);

        if(customMob instanceof Attributable) {
            if (health != null)
                Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(knockbackResistance);
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(speed);
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_FOLLOW_RANGE)).setBaseValue(followRange);
            Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_SCALE)).setBaseValue(size);
        }

        if(damageRange != null) {
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MinDamage"), PersistentDataType.DOUBLE, damageRange.getMinimumDamage());
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MaxDamage"), PersistentDataType.DOUBLE, damageRange.getMaximimDamage());
        }

        if(customMob instanceof Animals && aggressive) {
            addAggressivity(customMob);
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
    }

    private void addAggressivity(Entity customMob) {
        new NMS().setCustomMobAggressivity((Mob) customMob);
    }

    public ItemStack modifyOption(org.bukkit.entity.Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "visible": {
                this.isNameVisible = !isNameVisible;
                customMob.getCustomMobConfiguration().setNameVisible(isNameVisible);
                return getOptionItemStack(getVisibleNameItem(), false, false);
            }

            case "health": {
                if(clickType.isRightClick()) {
                    this.health = null;
                    customMob.getCustomMobConfiguration().setHealth(health);
                } else {
                    new DoubleGUI("Health", false, 0, 1024, (value) -> {
                        this.health = value;
                        customMob.getCustomMobConfiguration().setHealth(health);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, health == null ? 20D : health);
                }
                return getOptionItemStack(getHealthItem(), true, false);
            }

            case "aggressive" : {
                this.aggressive = !aggressive;
                customMob.getCustomMobConfiguration().setAggressive(aggressive);
                return getOptionItemStack(getAggressiveItem(), false, false);
            }

            case "glowing" : {
                this.glowing = !glowing;
                customMob.getCustomMobConfiguration().setGlowing(glowing);
                return getOptionItemStack(getGlowingItem(), false, false);
            }

            case "canpickuploot" : {
                this.canPickupLoot = !canPickupLoot;
                customMob.getCustomMobConfiguration().setCanPickupLoot(canPickupLoot);
                return getOptionItemStack(getCanPickupLootItem(), false, false);
            }

            case "knockbackresistance": {
                if(clickType.isRightClick()) {
                    this.knockbackResistance = 0D;
                    customMob.getCustomMobConfiguration().setKnockbackResistance(knockbackResistance);
                } else {
                    new DoubleGUI("Knockback resistance", true, 0, 1, (value) -> {
                        this.knockbackResistance = value;
                        customMob.getCustomMobConfiguration().setKnockbackResistance(knockbackResistance);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, knockbackResistance);
                }
                return getOptionItemStack(getKnockbackResistanceItem(), true, false);
            }

            case "speed": {
                if(clickType.isRightClick()) {
                    this.speed = 0.3D;
                    customMob.getCustomMobConfiguration().setSpeed(speed);
                } else {
                    new DoubleGUI("Speed", false, 0, 1024, (value) -> {
                        this.speed = value;
                        customMob.getCustomMobConfiguration().setSpeed(speed);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, speed);
                }
                return getOptionItemStack(getSpeedItem(), true, false);
            }

            case "invincible" : {
                this.invincible = !invincible;
                customMob.getCustomMobConfiguration().setInvincible(invincible);
                return getOptionItemStack(getInvincibleItem(), false, false);
            }

            case "silent" : {
                this.silent = !silent;
                customMob.getCustomMobConfiguration().setSilent(silent);
                return getOptionItemStack(getSilentItem(), false, false);
            }

            case "gravity" : {
                this.gravity = !gravity;
                customMob.getCustomMobConfiguration().setGravity(gravity);
                return getOptionItemStack(getGravityItem(), false, false);
            }

            case "persistent" : {
                this.persistent = !persistent;
                customMob.getCustomMobConfiguration().setPersistent(persistent);
                return getOptionItemStack(getPersistentItem(), false, false);
            }

            case "intelligent" : {
                this.intelligent = !intelligent;
                customMob.getCustomMobConfiguration().setIntelligent(intelligent);
                return getOptionItemStack(getIntelligentItem(), false, false);
            }

            case "followrange": {
                if(clickType.isRightClick()) {
                    this.followRange = 32D;
                    customMob.getCustomMobConfiguration().setFollowRange(followRange);
                } else {
                    new DoubleGUI("Follow range", false, 0, 2048, (value) -> {
                        this.followRange = value;
                        customMob.getCustomMobConfiguration().setFollowRange(followRange);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, followRange);
                }
                return getOptionItemStack(getFollowRangeItem(), true, false);
            }

            case "size": {
                if(clickType.isRightClick()) {
                    this.size = 1;
                    customMob.getCustomMobConfiguration().setSize(size);
                } else {
                    //TODO custom GUI for 0.06 value
                    new DoubleGUI("Size", true, 0.06, 16, (value) -> {
                        this.size = value;
                        customMob.getCustomMobConfiguration().setSize(size);
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, size);
                }
                return getOptionItemStack(getSizeItem(), true, false);
            }
        }
        return null;
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getVisibleNameItem(), false, false));
        items.add(getOptionItemStack(getHealthItem(), true, false));
        items.add(getOptionItemStack(getAggressiveItem(), false, false));
        items.add(getOptionItemStack(getGlowingItem(), false, false));
        items.add(getOptionItemStack(getCanPickupLootItem(), false, false));
        items.add(getOptionItemStack(getKnockbackResistanceItem(), true, false));
        items.add(getOptionItemStack(getSpeedItem(), true, false));

        // TODO
        CustomMobsItem damageRange = new CustomMobsItem(Material.WOODEN_SWORD);
        ItemMeta damageRangeMeta = damageRange.getItemMeta();
        damageRangeMeta.setDisplayName(Utils.applyFormat("&c&lDamage range"));
        damageRange.setItemMeta(damageRangeMeta);
        items.add(getOptionItemStack(damageRange, false, false));

        items.add(getOptionItemStack(getInvincibleItem(), false, false));
        items.add(getOptionItemStack(getSilentItem(), false, false));
        items.add(getOptionItemStack(getGravityItem(), false, false));
        items.add(getOptionItemStack(getPersistentItem(), false, false));
        items.add(getOptionItemStack(getIntelligentItem(), false, false));
        items.add(getOptionItemStack(getFollowRangeItem(), true, false));
        items.add(getOptionItemStack(getSizeItem(), true, false));

        return items;
    }

    public CustomMobsItem getVisibleNameItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GLOW_INK_SAC);
        String visible = this.isNameVisible ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lName always visible");
        item.addLore("&eVisible: " + visible);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Visible");
        return item;
    }

    public CustomMobsItem getHealthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLDEN_APPLE);
        item.setName("&6&lHealth");
        if (health != null)
            item.addLore("&eHealth: &f" + health + " (" + health / 2 + " ❤)");
        else
            item.addLore("&eHealth: &fDefault entity value");
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Health");
        return item;
    }

    public CustomMobsItem getAggressiveItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_SWORD);
        String aggressive = this.aggressive ? "&a&lOn" : "&c&lOff";
        item.setName("&c&lAggressive");
        item.addLore("&eAggressive: " + aggressive);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Aggressive");
        return item;
    }

    public CustomMobsItem getGlowingItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NETHER_STAR);
        String glowing = this.glowing ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lGlowing");
        item.addLore("&eGlowing: " + glowing);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Glowing");
        return item;
    }

    public CustomMobsItem getCanPickupLootItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CARROT);
        String canPickupLoot = this.canPickupLoot ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lCan pickup loot");
        item.addLore("&eCan pickup loot: " + canPickupLoot);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "CanPickupLoot");
        return item;
    }

    public CustomMobsItem getKnockbackResistanceItem() {
        CustomMobsItem item = new CustomMobsItem(Material.STICK);
        item.addEnchantment(Enchantment.KNOCKBACK, 1, false);
        item.setName("&a&lKnockback resistance");
        item.addLore("&eKnockback resistance: &f" + knockbackResistance);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "KnockbackResistance");
        return item;
    }

    public CustomMobsItem getSpeedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_BOOTS);
        item.addEnchantment(Enchantment.PROTECTION, 1, false);
        item.setName("&b&lSpeed");
        item.addLore("&eSpeed: &f" + speed);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Speed");
        return item;
    }

    public CustomMobsItem getInvincibleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENCHANTED_GOLDEN_APPLE);
        String invincible = this.invincible ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lInvincible");
        item.addLore("&eInvincible: " + invincible);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Invincible");
        return item;
    }

    public CustomMobsItem getSilentItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SCULK_SHRIEKER);
        String silent = this.silent ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lSilent");
        item.addLore("&eSilent: " + silent);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Silent");
        return item;
    }

    public CustomMobsItem getGravityItem() {
        CustomMobsItem item = new CustomMobsItem(Material.APPLE);
        String gravity = this.gravity ? "&a&lOn" : "&c&lOff";
        item.setName("&2&lGravity");
        item.addLore("&eGravity: " + gravity);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Gravity");
        return item;
    }

    public CustomMobsItem getPersistentItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NAME_TAG);
        String persistent = this.persistent ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lPersistent");
        item.addLore("&eMob cannot dispawn: " + persistent);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Persistent");
        return item;
    }

    public CustomMobsItem getIntelligentItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LIGHT);
        String intelligent = this.intelligent ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lIntelligent");
        item.addLore("&eIntelligent: " + intelligent);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Intelligent");
        return item;
    }

    public CustomMobsItem getFollowRangeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPYGLASS);
        item.setName("&c&lFollow range");
        item.addLore("&eFollow range: &f" + followRange);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "FollowRange");
        return item;
    }

    public CustomMobsItem getSizeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LARGE_AMETHYST_BUD);
        item.setName("&b&lSize");
        item.addLore("&eSize: &f" + size);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Size");
        return item;
    }
}
