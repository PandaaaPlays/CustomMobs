package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;
import java.util.Set;

public class CustomMobEvents implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if(entity instanceof Projectile)
            entity = (LivingEntity) ((Projectile) entity).getShooter();

        if(entity == null)
            return;

        Set<NamespacedKey> keys = entity.getPersistentDataContainer().getKeys();
        NamespacedKey minDamageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMob.MinDamage");
        NamespacedKey maxDamageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMob.MaxDamage");
        if(keys.contains(minDamageKey) && keys.contains(maxDamageKey)) {
            double minDamage = entity.getPersistentDataContainer().get(minDamageKey, PersistentDataType.DOUBLE);
            double maxDamage = entity.getPersistentDataContainer().get(maxDamageKey, PersistentDataType.DOUBLE);
            Random rand = new Random();
            double randomValue = minDamage + (maxDamage - minDamage) * rand.nextDouble();
            event.setDamage(randomValue);
        }
    }
}
