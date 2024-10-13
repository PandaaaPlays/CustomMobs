package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.DamageRange;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Special extends CustomMobType {
    private boolean isNameVisible;
    private final Double health;
    private boolean aggressive;
    private boolean glowing;
    private boolean canPickupLoot;
    private final Double knockbackResistance;
    private final Double speed;
    private final DamageRange damageRange;
    private boolean invincible;
    private boolean silent;
    private boolean gravity;
    private boolean persistent;
    private boolean intelligent;
    private final Double followRange;

    public Special(boolean isNameVisible, Double health, boolean aggressive, boolean glowing, boolean canPickupLoot, Double knockbackResistance, Double speed, DamageRange damageRange, boolean invincible, boolean silent, boolean gravity, boolean persistent, boolean intelligent, Double followRange) {
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
    }

    public void applyOptions(Entity customMob) {
        customMob.setCustomNameVisible(isNameVisible);

        if(customMob instanceof Attributable) {
            if (health != null)
                Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
            if (knockbackResistance != null)
                Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(knockbackResistance);
            if (speed != null)
                Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(speed);
            if (followRange != null)
                Objects.requireNonNull(((Attributable) customMob).getAttribute(Attribute.GENERIC_FOLLOW_RANGE)).setBaseValue(followRange);
        }

        if(damageRange != null) {
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MinDamage"), PersistentDataType.DOUBLE, damageRange.getMinimumDamage());
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MaxDamage"), PersistentDataType.DOUBLE, damageRange.getMaximimDamage());
        }

        if(customMob instanceof Animals && aggressive) {
            addAggressivity(customMob);
        }

        customMob.setGlowing(glowing);

        if(customMob instanceof LivingEntity) {
            ((LivingEntity) customMob).setCanPickupItems(canPickupLoot);
            ((LivingEntity) customMob).setAI(intelligent);
        }

        customMob.setInvulnerable(invincible);
        customMob.setSilent(silent);
        customMob.setGravity(gravity);
        customMob.setPersistent(persistent);
    }

    private void addAggressivity(Entity customMob) {
        /* TODO addAggressivity
        Pathfinder pathfinder = ((Mob) customMob).getPathfinder();
            ((net.minecraft.world.entity.Entity)entity).getHandle();

            List goalB = (List)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
            List goalC = (List)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
            List targetB = (List)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
            List targetC = (List)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
            ((CraftWorld)customMob.getWorld()).getHandle();
            Class<net.minecraft.world.entity.Entity> clazz = net.minecraft.world.entity.Entity.class;
            Method m = clazz.getDeclaredMethod("getBukkitEntity");
            m.setAccessible(true);
            Class<org.bukkit.entity.Entity> clazz2 = m.getReturnType().asSubclass(org.bukkit.entity.Entity.class);

            super(((CraftWorld)world).getHandle());
            List goalB = (List)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
            List goalC = (List)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
            List targetB = (List)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
            List targetC = (List)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

            this.goalSelector.a(0, new PathfinderGoalFloat(this));
            this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
            this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
            this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
            this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
            this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
            this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
            this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
            this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
            this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
            this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 0, false));
         */
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        switch(option.toLowerCase()) {
            case "visible": {
                this.isNameVisible = !isNameVisible;
                customMob.getCustomMobConfiguration().setNameVisible(isNameVisible);
                return getOptionItemStack(getVisibleNameItem());
            }

            case "aggressive" : {
                this.aggressive = !aggressive;
                customMob.getCustomMobConfiguration().setAggressive(aggressive);
                return getOptionItemStack(getAggressiveItem());
            }

            case "glowing" : {
                this.glowing = !glowing;
                customMob.getCustomMobConfiguration().setGlowing(glowing);
                return getOptionItemStack(getGlowingItem());
            }

            case "canpickuploot" : {
                this.canPickupLoot = !canPickupLoot;
                customMob.getCustomMobConfiguration().setCanPickupLoot(canPickupLoot);
                return getOptionItemStack(getCanPickupLootItem());
            }

            case "invincible" : {
                this.invincible = !invincible;
                customMob.getCustomMobConfiguration().setInvincible(invincible);
                return getOptionItemStack(getInvincibleItem());
            }

            case "silent" : {
                this.silent = !silent;
                customMob.getCustomMobConfiguration().setSilent(silent);
                return getOptionItemStack(getSilentItem());
            }

            case "gravity" : {
                this.gravity = !gravity;
                customMob.getCustomMobConfiguration().setGravity(gravity);
                return getOptionItemStack(getGravityItem());
            }

            case "persistent" : {
                this.persistent = !persistent;
                customMob.getCustomMobConfiguration().setPersistent(persistent);
                return getOptionItemStack(getPersistentItem());
            }

            case "intelligent" : {
                this.intelligent = !intelligent;
                customMob.getCustomMobConfiguration().setIntelligent(intelligent);
                return getOptionItemStack(getIntelligentItem());
            }
        }
        return null;
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getVisibleNameItem()));

        // TODO
        CustomMobsItem health = new CustomMobsItem(Material.GOLDEN_APPLE);
        health.setName("&6&lHealth");
        items.add(getOptionItemStack(health));

        items.add(getOptionItemStack(getAggressiveItem()));
        items.add(getOptionItemStack(getGlowingItem()));
        items.add(getOptionItemStack(getCanPickupLootItem()));

        // TODO
        CustomMobsItem knockbackResistance = new CustomMobsItem(Material.STICK);
        ItemMeta knockbackResistanceMeta = knockbackResistance.getItemMeta();
        knockbackResistanceMeta.addEnchant(Enchantment.KNOCKBACK, 1, false);
        knockbackResistanceMeta.setDisplayName(Utils.applyFormat("&a&lKnockback resistance"));
        knockbackResistance.setItemMeta(knockbackResistanceMeta);
        items.add(getOptionItemStack(knockbackResistance));

        // TODO
        CustomMobsItem speed = new CustomMobsItem(Material.DIAMOND_BOOTS);
        ItemMeta speedMeta = speed.getItemMeta();
        speedMeta.setDisplayName(Utils.applyFormat("&b&lSpeed"));
        speed.setItemMeta(speedMeta);
        items.add(getOptionItemStack(speed));

        // TODO
        CustomMobsItem damageRange = new CustomMobsItem(Material.WOODEN_SWORD);
        ItemMeta damageRangeMeta = damageRange.getItemMeta();
        damageRangeMeta.setDisplayName(Utils.applyFormat("&c&lDamage range"));
        damageRange.setItemMeta(damageRangeMeta);
        items.add(getOptionItemStack(damageRange));

        items.add(getOptionItemStack(getInvincibleItem()));
        items.add(getOptionItemStack(getSilentItem()));
        items.add(getOptionItemStack(getGravityItem()));
        items.add(getOptionItemStack(getPersistentItem()));
        items.add(getOptionItemStack(getIntelligentItem()));

        // TODO
        CustomMobsItem followRange = new CustomMobsItem(Material.SPYGLASS);
        ItemMeta followRangeMeta = followRange.getItemMeta();
        followRangeMeta.setDisplayName(Utils.applyFormat("&c&lFollow range"));
        followRange.setItemMeta(followRangeMeta);
        items.add(getOptionItemStack(followRange));

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
        item.addLore("&eMob can dispawn: " + persistent);
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
}
