package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
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
        NamespacedKey minDamageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MinDamage");
        NamespacedKey maxDamageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MaxDamage");
        if(keys.contains(minDamageKey) && keys.contains(maxDamageKey)) {
            double minDamage = entity.getPersistentDataContainer().get(minDamageKey, PersistentDataType.DOUBLE);
            double maxDamage = entity.getPersistentDataContainer().get(maxDamageKey, PersistentDataType.DOUBLE);
            Random rand = new Random();
            double randomValue = minDamage + (maxDamage - minDamage) * rand.nextDouble();
            event.setDamage(randomValue);
        }
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        Action action = event.getAction();
        if(!action.equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(!Objects.equals(event.getHand(), EquipmentSlot.HAND))
            return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().equals(Material.AIR))
            return;

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        NamespacedKey itemTypeKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.ItemType");
        if (!container.getKeys().contains(itemTypeKey))
            return;

        if (container.getKeys().contains(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MenuItem"))) {
            player.getInventory().remove(item);
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE)
            item.setAmount(item.getAmount() - 1);
        CustomMob customMob = CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(container.get(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName"), PersistentDataType.STRING));

        String itemType = container.get(itemTypeKey, PersistentDataType.STRING);
        if (itemType.equalsIgnoreCase("Item")) {
            event.setCancelled(true);
            customMob.spawnCustomMob(event.getClickedBlock().getLocation().add(0.5, 2, 0.5));
        } else if (itemType.equalsIgnoreCase("Spawner")) {
            event.setCancelled(true);
            customMob.placeCustomMobSpawner(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation());
        }
    }
}
