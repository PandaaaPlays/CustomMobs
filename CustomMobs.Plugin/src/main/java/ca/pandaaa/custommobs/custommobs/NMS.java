package ca.pandaaa.custommobs.custommobs;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import ca.pandaaa.custommobs.CustomMobs;

public class NMS {

    private static final Set<UUID> patchedEntities = new HashSet<>();

    public void setCustomMobAggressivity(org.bukkit.entity.Mob entity, double followRange) {
        if (patchedEntities.contains(entity.getUniqueId())) return;
        patchedEntities.add(entity.getUniqueId());

        Mob mob = getHandle(entity);

        AttributeInstance attackDamageAttribute = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttribute == null) {
            AttributeInstance instance = new AttributeInstance(Attributes.ATTACK_DAMAGE, attr -> attr.setBaseValue(1.0D));
            setAttribute(mob, instance);
        }

        if (mob instanceof PathfinderMob pathfinderMob) {
            mob.goalSelector.addGoal(2, new MeleeAttackGoal(pathfinderMob, 1.0D, false));
            mob.targetSelector.addGoal(1, new HurtByTargetGoal(pathfinderMob));
        }

        mob.goalSelector.addGoal(3, new RandomLookAroundGoal(mob));
        mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, Player.class, 0, true, false, null));

        if (mob instanceof net.minecraft.world.entity.animal.axolotl.Axolotl || mob instanceof net.minecraft.world.entity.animal.goat.Goat || mob instanceof net.minecraft.world.entity.animal.frog.Frog) {
            new BukkitRunnable() {
                private int attackCooldown = 0;
                @Override
                public void run() {
                    if (!entity.isValid() || entity.isDead()) {
                        patchedEntities.remove(entity.getUniqueId());
                        this.cancel();
                        return;
                    }

                    if (attackCooldown > 0) attackCooldown--;
                    if (mob instanceof net.minecraft.world.entity.animal.axolotl.Axolotl axo && axo.isPlayingDead()) return;

                    LivingEntity target = mob.getTarget();
                    if (target == null) {
                        Player nearest = mob.level().getNearestPlayer(mob, followRange);
                        if (nearest != null && nearest.isAlive()) target = nearest;
                    }

                    Brain<?> brain = mob.getBrain();
                    if (target != null && target.isAlive() && !(target instanceof Player p && (p.isCreative() || p.isSpectator()))) {
                        brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);
                        double dist = mob.distanceToSqr(target);

                        if (dist <= 13.0D && attackCooldown == 0) {
                            if (target.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity bent) {
                                bent.damage(1.0, entity);
                                mob.swing(InteractionHand.MAIN_HAND);
                            }
                            attackCooldown = 20;
                        }

                        if (dist > 3.0D && dist < 400.0D && !(mob instanceof net.minecraft.world.entity.animal.axolotl.Axolotl)) {
                            mob.getNavigation().moveTo(target, 1.3D);
                        }
                    } else {
                        brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
                    }
                }
            }.runTaskTimer(CustomMobs.getPlugin(), 1L, 1L);
        } else if (!(mob instanceof PathfinderMob)) {
            new BukkitRunnable() {
                private int attackCooldown = 0;
                @Override
                public void run() {
                    if (!entity.isValid() || entity.isDead()) {
                        patchedEntities.remove(entity.getUniqueId());
                        this.cancel();
                        return;
                    }

                    if (attackCooldown > 0) attackCooldown--;
                    LivingEntity target = mob.getTarget();
                    if (target != null && target.isAlive()) {
                        if (mob instanceof net.minecraft.world.entity.ambient.Bat bat) bat.setResting(false);
                        double dist = mob.distanceToSqr(target);
                        if (dist > 3.0D) {
                            mob.getLookControl().setLookAt(target, (float) followRange, (float) followRange);
                            Vector dir = target.getBukkitEntity().getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(0.3);
                            entity.setVelocity(dir);
                        } else if (attackCooldown == 0) {
                            if (target.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity bent) bent.damage(1.0, entity);
                            attackCooldown = 20;
                        }
                    }
                }
            }.runTaskTimer(CustomMobs.getPlugin(), 1L, 1L);
        }
    }

    private Mob getHandle(org.bukkit.entity.Mob bukkitEntity) {
        try {
            return (Mob) bukkitEntity.getClass().getMethod("getHandle").invoke(bukkitEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void setAttribute(Mob nmsEntity, AttributeInstance attributeInstance) {
        try {
            java.lang.reflect.Field attributesField = AttributeMap.class.getDeclaredField("attributes");
            attributesField.setAccessible(true);
            Map<Holder<Attribute>, AttributeInstance> map = (Map) attributesField.get(nmsEntity.getAttributes());
            map.put(attributeInstance.getAttribute(), attributeInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
