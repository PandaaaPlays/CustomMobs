package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.DamageRange;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class Special extends CustomMobType {

    private final String name;
    private final boolean isNameVisible;
    private final Double health;
    private final boolean aggressive;
    private final boolean glowing;
    private final boolean canPickupLoot;
    private final Double knockbackResistance;
    private final Double speed;
    private final DamageRange damageRange;
    private final boolean invincible;
    private final boolean silent;
    private final boolean gravity;
    private final boolean persistent;
    private final boolean intelligent;
    private final Double followRange;

    public Special(String name, boolean isNameVisible, Double health, boolean aggressive, boolean glowing, boolean canPickupLoot, Double knockbackResistance, Double speed, DamageRange damageRange, boolean invincible, boolean silent, boolean gravity, boolean persistent, boolean intelligent, Double followRange) {
        this.name = name;
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
        if(name != null)
            customMob.setCustomName(Utils.applyFormat(name));
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
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMob.MinDamage"), PersistentDataType.DOUBLE, damageRange.getMinimumDamage());
            customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMob.MaxDamage"), PersistentDataType.DOUBLE, damageRange.getMaximimDamage());
        }

        if(customMob instanceof Animals && aggressive) {
            addAgressivity(customMob);
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

    private void addAgressivity(Entity customMob) {
        /*
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
}
