package ca.pandaaa.custommobs.custommobs;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

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

        mob.targetSelector.addGoal(2, new MeleeAttackGoal((PathfinderMob) mob, 1D, false));
        mob.targetSelector.addGoal(8, new RandomLookAroundGoal(mob));
        mob.targetSelector.addGoal(1, new HurtByTargetGoal((PathfinderMob) mob));
        mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(mob, Player.class, true));
    }

    private static final class NMSResolver {
        private static final int VERSION_PACKAGE_COUNTER = 3;
        private static final String PACKAGE_BASE = "org.bukkit.craftbukkit";
        private static final String VERSION = org.bukkit.Bukkit.getServer().getClass().getPackage().getName()
                .split("\\.")[VERSION_PACKAGE_COUNTER];
        private static final String MIDDLE_PACKAGE = "entity";
        private static final String CRAFT_LIVING_ENTITY_CLASS_NAME = "CraftLivingEntity";
        private final Method getHandleMethod;
        private final Field attributeMap; // LivingEntity AttributeMap
        private final Field attributes; // AttributeMap attributes

        private NMSResolver() {
            try {
                Class<?> craftLivingEntityClass = Class
                        .forName(String.format("%s.%s.%s.%s", PACKAGE_BASE, VERSION, MIDDLE_PACKAGE, CRAFT_LIVING_ENTITY_CLASS_NAME));
                getHandleMethod = craftLivingEntityClass.getMethod("getHandle");
                // To find the fields corresponding to the version, see : https://minidigger.github.io/MiniMappingViewer/#/mojang/client/1.XX.XX/LivingEntity
                // Field in NMS LivingEntity class of type : AttributeMap
                attributeMap = LivingEntity.class.getDeclaredField("bR");
                // To find the fields corresponding to the version, see : https://minidigger.github.io/MiniMappingViewer/#/mojang/client/1.XX.XX/AttributeMap
                // Field in NMS AttributeMap class of type : Map
                attributes = AttributeMap.class.getDeclaredField("b");
                attributeMap.setAccessible(true);
                attributes.setAccessible(true);
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        private Mob getNMSEntity(org.bukkit.entity.Mob bukkit) {
            try {
                return (Mob) getHandleMethod.invoke(bukkit);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
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
