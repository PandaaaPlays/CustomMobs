package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.Utils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class NMS {

    private static final NMSResolver NMS_RESOLVER = new NMSResolver();

    public void setCustomMobAggressivity(org.bukkit.entity.Mob entity) {
        Mob mob = NMS_RESOLVER.getNMSEntity(entity);

        AttributeInstance attackDamageAttribute = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttribute == null)
            NMS_RESOLVER.setAttribute(mob, new AttributeInstance(Attributes.ATTACK_DAMAGE, attribute -> attribute.setBaseValue(1D)));

        NMS_RESOLVER.addGoal(mob, 2, new MeleeAttackGoal((PathfinderMob) mob, 1D, false));
        NMS_RESOLVER.addGoal(mob, 3, new RandomLookAroundGoal(mob));
        NMS_RESOLVER.addGoal(mob, 1, new HurtByTargetGoal((PathfinderMob) mob));
        NMS_RESOLVER.addGoal(mob, 0, new NearestAttackableTargetGoal<Player>(mob, Player.class, true));
    }

    private static final class NMSResolver {
        private static final int VERSION_PACKAGE_COUNTER = 3;
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
        private Field targetSelectorField = null;

        private NMSResolver() {
            try {
                Class<?> craftLivingEntityClass;
                if(VERSION.isEmpty()) {
                    craftLivingEntityClass = Class.forName(String.format("%s.%s.%s", PACKAGE_BASE, MIDDLE_PACKAGE, CRAFT_LIVING_ENTITY_CLASS_NAME));
                } else {
                    craftLivingEntityClass = Class.forName(String.format("%s.%s.%s.%s", PACKAGE_BASE, VERSION, MIDDLE_PACKAGE, CRAFT_LIVING_ENTITY_CLASS_NAME));
                }

                getHandleMethod = craftLivingEntityClass.getMethod("getHandle");
                // To find the fields corresponding to the version, see : https://minidigger.github.io/MiniMappingViewer/#/mojang/client/1.XX.XX/LivingEntity
                if(Utils.isVersionAtLeast("1.21.6") && Utils.isVersionBeforeOrEqual("1.21.8")) {
                    attributeMap = LivingEntity.class.getDeclaredField("cc");  // Field 'attributes' in NMS LivingEntity class
                    attributes = AttributeMap.class.getDeclaredField("a");     // Field 'attributes' in NMS AttributeMap class
                    targetSelectorField = Mob.class.getDeclaredField("ci");    // Field 'targetSelector' in NMS entity.Mob class
                } else if(Bukkit.getBukkitVersion().contains("1.21.5")) {
                    attributeMap = LivingEntity.class.getDeclaredField("bF");
                    attributes = AttributeMap.class.getDeclaredField("a");
                    targetSelectorField = Mob.class.getDeclaredField("bG");
                } else {
                    throw new Exception("This server version does not support aggressive animals. Please contact the developper if you believe this is an issue.");
                }

                attributeMap.setAccessible(true);
                attributes.setAccessible(true);
                targetSelectorField.setAccessible(true);
            } catch (Exception e) {
                CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e));
            }
        }

        private Mob getNMSEntity(org.bukkit.entity.Mob bukkit) {
            try {
                return (Mob) getHandleMethod.invoke(bukkit);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private void addGoal(Mob mob, int integer, Goal goal) {
            try {
                Object goalSelector = targetSelectorField.get(mob);
                Method addGoalMethod = null;
                if(Utils.isVersionAtLeast("1.21.5") && Utils.isVersionBeforeOrEqual("1.21.8")) {
                    addGoalMethod = goalSelector.getClass().getDeclaredMethod("a", int.class, Goal.class); // Method 'addGoal' in NMS GoalSelector class
                } else {
                    throw new Exception("This server version does not support aggressive animals. Please contact the developper if you believe this is an issue.");
                }
                addGoalMethod.invoke(goalSelector, integer, goal);
            } catch (Exception e) {
                CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e));
            }
        }

        @SuppressWarnings("unchecked")
        private void setAttribute(LivingEntity nmsEntity, AttributeInstance attributeInstance) {
            Map<Holder<Attribute>, AttributeInstance> nmsEntityAttributes;
            try {
                nmsEntityAttributes = (Map<Holder<Attribute>, AttributeInstance>) attributes.get(attributeMap.get(nmsEntity));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            nmsEntityAttributes.put(attributeInstance.getAttribute(), attributeInstance);
        }
    }


}
