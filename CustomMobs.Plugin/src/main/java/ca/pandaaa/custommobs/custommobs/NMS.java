package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.Utils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NMS {

    private static final NMSResolver NMS_RESOLVER = new NMSResolver();
    private static final Set<UUID> patchedEntities = new HashSet<>();

    public void setCustomMobAggressivity(org.bukkit.entity.Mob entity, double followRange) {
        if (patchedEntities.contains(entity.getUniqueId()))
            return;
        patchedEntities.add(entity.getUniqueId());
        Mob mob = NMS_RESOLVER.getNMSEntity(entity);

        AttributeInstance attackDamageAttribute = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttribute == null)
            NMS_RESOLVER.setAttribute(mob,
                    new AttributeInstance(Attributes.ATTACK_DAMAGE, attribute -> attribute.setBaseValue(1D)));

        if (mob instanceof PathfinderMob pathfinderMob) {
            NMS_RESOLVER.addGoal(mob, 2, new MeleeAttackGoal(pathfinderMob, 1D, false), false);
            NMS_RESOLVER.addGoal(mob, 1, new HurtByTargetGoal(pathfinderMob), true);
        }

        NMS_RESOLVER.addGoal(mob, 3, new RandomLookAroundGoal(mob), false);
        NMS_RESOLVER.addGoal(mob, 0, new NearestAttackableTargetGoal<Player>(mob,
                net.minecraft.world.entity.player.Player.class, 0, true, false, null), true);

        if (mob instanceof net.minecraft.world.entity.animal.axolotl.Axolotl ||
                mob instanceof net.minecraft.world.entity.animal.goat.Goat ||
                mob instanceof net.minecraft.world.entity.animal.frog.Frog) {
            new org.bukkit.scheduler.BukkitRunnable() {
                private int attackCooldown = 0;

                @Override
                public void run() {
                    if (!entity.isValid() || entity.isDead()) {
                        patchedEntities.remove(entity.getUniqueId());
                        this.cancel();
                        return;
                    }

                    if (attackCooldown > 0)
                        attackCooldown--;

                    if (mob instanceof net.minecraft.world.entity.animal.axolotl.Axolotl axolotl && axolotl.isPlayingDead())
                        return;

                    net.minecraft.world.entity.LivingEntity target = mob.getTarget();
                    if (target == null) {
                        net.minecraft.world.entity.player.Player nearest = mob.level().getNearestPlayer(mob,
                                followRange);
                        if (nearest != null && nearest.isAlive())
                            target = nearest;
                    }

                    Brain<?> brain = mob.getBrain();
                    if (target != null && target.isAlive()
                        && !((target instanceof net.minecraft.world.entity.player.Player p)
                        && (p.isCreative() || p.isSpectator()))) {
                        brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);
                        double distance = mob.distanceToSqr(target);

                        if (distance <= 13.0D && attackCooldown == 0) {
                            if (target.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity bent) {
                                bent.damage(1.0, entity);
                                mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                            }
                            attackCooldown = 20;
                        }

                        if (distance > 3.0D && distance < 400.0D
                                && !(mob instanceof net.minecraft.world.entity.animal.axolotl.Axolotl)) {
                            mob.getNavigation().moveTo(target, 1.3D);
                        }
                    } else {
                        brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
                    }
                }
            }.runTaskTimer(CustomMobs.getPlugin(), 1L, 1L);
        } else if (!(mob instanceof PathfinderMob)) {
            new org.bukkit.scheduler.BukkitRunnable() {
                private int attackCooldown = 0;

                @Override
                public void run() {
                    if (!entity.isValid() || entity.isDead()) {
                        patchedEntities.remove(entity.getUniqueId());
                        this.cancel();
                        return;
                    }

                    if (attackCooldown > 0)
                        attackCooldown--;

                    net.minecraft.world.entity.LivingEntity target = mob.getTarget();
                    if (target != null && target.isAlive()) {
                        if (mob instanceof net.minecraft.world.entity.ambient.Bat bat) {
                            bat.setResting(false);
                        }

                        double distance = mob.distanceToSqr(target);
                        if (distance > 3.0D) {
                            mob.getLookControl().setLookAt(target, (float) followRange, (float) followRange);
                            org.bukkit.util.Vector direction = target.getBukkitEntity().getLocation().toVector()
                                    .subtract(entity.getLocation().toVector()).normalize().multiply(0.3);
                            entity.setVelocity(direction);
                        } else if (attackCooldown == 0) {
                            if (target.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity bent) {
                                bent.damage(1.0, entity);
                            }
                            attackCooldown = 20;
                        }
                    }
                }
            }.runTaskTimer(CustomMobs.getPlugin(), 1L, 1L);
        }
    }

    private static final class NMSResolver {
        private static final String PACKAGE_BASE = "org.bukkit.craftbukkit";
        private static final String VERSION;
        static {
            String name = org.bukkit.Bukkit.getServer().getClass().getPackage().getName();
            if (name.contains("craftbukkit")) {
                String[] parts = name.split("\\.");
                VERSION = (parts.length == 4) ? parts[3] : "";
            } else {
                VERSION = "";
            }
        }
        private static final String MIDDLE_PACKAGE = "entity";
        private static final String CRAFT_LIVING_ENTITY_CLASS_NAME = "CraftLivingEntity";
        private Method getHandleMethod = null;
        private Field attributeMap = null; // LivingEntity AttributeMap
        private Field attributes = null; // AttributeMap attributes
        private Field goalSelectorField = null;
        private Field targetSelectorField = null;

        private NMSResolver() {
            try {
                Class<?> craftLivingEntityClass;
                if (VERSION.isEmpty()) {
                    craftLivingEntityClass = Class.forName(
                            String.format("%s.%s.%s", PACKAGE_BASE, MIDDLE_PACKAGE, CRAFT_LIVING_ENTITY_CLASS_NAME));
                } else {
                    craftLivingEntityClass = Class.forName(String.format("%s.%s.%s.%s", PACKAGE_BASE, VERSION,
                            MIDDLE_PACKAGE, CRAFT_LIVING_ENTITY_CLASS_NAME));
                }

                getHandleMethod = craftLivingEntityClass.getMethod("getHandle");
                // To find the fields corresponding to the version, see :
                // https://minidigger.github.io/MiniMappingViewer/#/mojang/client/1.XX.XX/LivingEntity
                if (Utils.isVersionExactly("1.21.11")) {
                    attributeMap = LivingEntity.class.getDeclaredField("cm"); // Field 'attributes' in NMS LivingEntity
                    attributes = AttributeMap.class.getDeclaredField("a"); // Field 'attributes' in NMS AttributeMap
                    goalSelectorField = Mob.class.getDeclaredField("cs"); // Field 'goalSelector' in NMS entity.Mob
                    targetSelectorField = Mob.class.getDeclaredField("ct"); // Field 'targetSelector' in NMS entity.Mob
                } else if (Utils.isVersionAtLeast("1.21.9") && Utils.isVersionBeforeOrEqual("1.21.10")) {
                    attributeMap = LivingEntity.class.getDeclaredField("cj");
                    attributes = AttributeMap.class.getDeclaredField("a");
                    goalSelectorField = Mob.class.getDeclaredField("cq");
                    targetSelectorField = Mob.class.getDeclaredField("cr");
                } else if (Utils.isVersionAtLeast("1.21.6") && Utils.isVersionBeforeOrEqual("1.21.8")) {
                    attributeMap = LivingEntity.class.getDeclaredField("cc");
                    attributes = AttributeMap.class.getDeclaredField("a");
                    goalSelectorField = Mob.class.getDeclaredField("ch");
                    targetSelectorField = Mob.class.getDeclaredField("ci");
                } else if (Bukkit.getBukkitVersion().contains("1.21.5")) {
                    attributeMap = LivingEntity.class.getDeclaredField("bF");
                    attributes = AttributeMap.class.getDeclaredField("a");
                    goalSelectorField = Mob.class.getDeclaredField("bE");
                    targetSelectorField = Mob.class.getDeclaredField("bG");
                } else {
                    throw new Exception(
                            "This server version does not support aggressive animals. Please contact the developper if you believe this is an issue.");
                }

                attributeMap.setAccessible(true);
                attributes.setAccessible(true);
                goalSelectorField.setAccessible(true);
                targetSelectorField.setAccessible(true);
            } catch (Exception e) {
                CustomMobs.getPlugin().getServer().getConsoleSender()
                        .sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e));
            }
        }

        private Mob getNMSEntity(org.bukkit.entity.Mob bukkit) {
            try {
                return (Mob) getHandleMethod.invoke(bukkit);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private void addGoal(Mob mob, int integer, Goal goal, boolean target) {
            try {
                Object selector = target ? targetSelectorField.get(mob) : goalSelectorField.get(mob);
                Method addGoalMethod = null;
                if (Utils.isVersionAtLeast("1.21.5") && Utils.isVersionBeforeOrEqual("1.21.11")) {
                    addGoalMethod = selector.getClass().getDeclaredMethod("a", int.class, Goal.class); // Method 'addGoal' in NMS GoalSelector
                } else {
                    throw new Exception(
                            "This server version does not support aggressive animals. Please contact the developper if you believe this is an issue.");
                }
                addGoalMethod.invoke(selector, integer, goal);
            } catch (Exception e) {
                CustomMobs.getPlugin().getServer().getConsoleSender()
                        .sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e));
            }
        }

        @SuppressWarnings("unchecked")
        private void setAttribute(LivingEntity nmsEntity, AttributeInstance attributeInstance) {
            Map<Holder<Attribute>, AttributeInstance> nmsEntityAttributes;
            try {
                nmsEntityAttributes = (Map<Holder<Attribute>, AttributeInstance>) attributes
                        .get(attributeMap.get(nmsEntity));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            nmsEntityAttributes.put(attributeInstance.getAttribute(), attributeInstance);
        }
    }

}
