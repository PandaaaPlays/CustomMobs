package ca.pandaaa.custommobs.custommobs.Events;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomEffectType;
import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomMobCustomEffect;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.DropManager;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.custommobs.Messages.Message;
import ca.pandaaa.custommobs.custommobs.Messages.SpawnDeathMessage;
import ca.pandaaa.custommobs.custommobs.Options.Special;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Events implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();

        if (entity instanceof Projectile)
            entity = (LivingEntity) ((Projectile) entity).getShooter();

        if (entity == null)
            return;

        Set<NamespacedKey> keys = entity.getPersistentDataContainer().getKeys();
        NamespacedKey minDamageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MinDamage");
        NamespacedKey maxDamageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MaxDamage");
        if (keys.contains(minDamageKey) && keys.contains(maxDamageKey)) {
            double minDamage = entity.getPersistentDataContainer().get(minDamageKey, PersistentDataType.DOUBLE);
            double maxDamage = entity.getPersistentDataContainer().get(maxDamageKey, PersistentDataType.DOUBLE);
            Random rand = new Random();
            double randomValue = minDamage + (maxDamage - minDamage) * rand.nextDouble();
            event.setDamage(randomValue);
        }

        if (!(event.getEntity() instanceof Player))
            return;

        NamespacedKey nameKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Name");
        if (keys.contains(nameKey)) {
            CustomMob customMob = CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(entity.getPersistentDataContainer().get(nameKey, PersistentDataType.STRING));
            customMob.getCustomMobCustomEffects().stream()
                    .filter(effect -> CustomEffectType.ON_DAMAGE_ON_PLAYER.equals(effect.getCustomEffectType()))
                    .filter(CustomMobCustomEffect::isEnabled)
                    .forEach(effect ->  {
                        effect.triggerCustomEffect(event.getEntity());
                    });
        }
    }

    /**
     * Damage of players on the CustomMob, which is used for MOST_DAMAGE.
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();

        if (!(entity instanceof Player))
            return;

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Name");
        if (!event.getEntity().getPersistentDataContainer().getKeys().contains(key))
            return;

        // Enable the custom effects of the CustomMob.
        CustomMobs.getPlugin().getCustomMobsManager()
                .getCustomMob(event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.STRING))
                .enableCustomEffects(event.getEntity());

        double healthAfter = Math.max(0, ((LivingEntity)event.getEntity()).getHealth() - event.getFinalDamage());
        double maxHealth = Objects.requireNonNull(((LivingEntity)event.getEntity()).getAttribute(Registry.ATTRIBUTE.get(NamespacedKey.minecraft("max_health")))).getBaseValue();
        CustomMobs.getPlugin().getCustomMobsManager().getBossBar().update(event.getEntity().getUniqueId(), healthAfter, maxHealth);

        double damage = 0;
        NamespacedKey damageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Damage." + entity.getUniqueId());
        if (event.getEntity().getPersistentDataContainer().getKeys().contains(damageKey)) {
            damage = event.getEntity().getPersistentDataContainer().get(damageKey, PersistentDataType.DOUBLE);
        }
        damage += event.getFinalDamage();
        event.getEntity().getPersistentDataContainer().set(damageKey, PersistentDataType.DOUBLE, damage);
    }


    private final Map<UUID, Long> playerMoveCooldowns = new HashMap<>();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Only trigger on meaningful movements (not only mouse)
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        // Check every 3 seconds, not more often
        Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        if (playerMoveCooldowns.containsKey(player.getUniqueId()) && now - playerMoveCooldowns.get(player.getUniqueId()) < 3000)
            return;
        playerMoveCooldowns.put(player.getUniqueId(), now);

        List<Entity> entities = event.getPlayer().getNearbyEntities(10, 5, 10);
        for(Entity entity : entities) {
            NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Name");
            if (!entity.getPersistentDataContainer().getKeys().contains(key))
                continue;

            CustomMobs.getPlugin().getCustomMobsManager()
                    .getCustomMob(entity.getPersistentDataContainer().get(key, PersistentDataType.STRING))
                    .enableCustomEffects(entity);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Name");
        if (!event.getEntity().getPersistentDataContainer().getKeys().contains(key))
            return;

        String name = event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        CustomMob customMob = CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(name);
        if (customMob == null)
            return;

        CustomMobs.getPlugin().getCustomMobsManager().getBossBar().deleteBossBar(event.getEntity().getUniqueId());

        // Cancel the custom effects of the CustomMob.
        customMob.cancelCustomEffects(event.getEntity().getUniqueId());

        CustomMobDeathEvent customEvent = new CustomMobDeathEvent(event.getEntity(), customMob);
        Bukkit.getServer().getPluginManager().callEvent(customEvent);

        for (Message message : customMob.getCustomMobMessages()) {
            if (((SpawnDeathMessage) message).isOnDeath())
                message.sendMessage(event.getEntity());
        }

        if (customMob.getCustomMobOption("Special") != null && !((Special) customMob.getCustomMobOption("Special")).getNaturalDrops())
            event.getDrops().clear();

        if (!customMob.getDrops().isEmpty())
            DropManager.sendDrops(customMob.getDrops(), event.getEntity());
    }

    // Menu Item deletion //
    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (!Objects.equals(event.getHand(), EquipmentSlot.HAND))
            return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR))
            return;

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (container.getKeys().contains(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MenuItem"))) {
            player.getInventory().remove(item);
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&c[!] CustomMobs : " + player.getName() + " tried to use a CustomMob menu item and it was deleted. Please report this incident."));
        }

        NamespacedKey itemTypeKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.ItemType");
        if (!container.getKeys().contains(itemTypeKey))
            return;

        if (player.getGameMode() != GameMode.CREATIVE)
            item.setAmount(item.getAmount() - 1);
        CustomMob customMob = CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(container.get(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName"), PersistentDataType.STRING));

        if (customMob == null)
            return;

        String itemType = container.get(itemTypeKey, PersistentDataType.STRING);

        if (itemType.equalsIgnoreCase("Item")) {
            event.setCancelled(true);
            customMob.spawnCustomMob(event.getClickedBlock().getLocation().add(0.5, 2, 0.5));
        } else if (itemType.equalsIgnoreCase("Spawner-Item")) {
            event.setCancelled(true);
            customMob.placeCustomMobSpawner(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER)
            return;

        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (container.getKeys().contains(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.MenuItem"))) {
            event.setCancelled(true);
            event.getClickedInventory().removeItem(item);
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&c[!] CustomMobs : " + event.getWhoClicked().getName() +
                            " had a CustomMob menu item and it was deleted. Please report this incident."));
        }
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        PersistentDataContainer container = event.getSpawner().getPersistentDataContainer();
        Location location = event.getEntity().getLocation();

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Spawner");
        if (container.has(key, PersistentDataType.STRING)) {
            String mobName = container.get(key, PersistentDataType.STRING);
            CustomMob customMob = CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(mobName);
            if (customMob == null || customMob.getSpawner() == null)
                return;
            int range = customMob.getSpawner().getSpawnRange();

            // With this, we find the mobs of the same type. If that is ever a problem, we could filter on the persistent key of the CustomMob.
            int nearbyCustomMobs = (int) event.getSpawner().getLocation().getWorld().getNearbyEntities(event.getSpawner().getLocation(), range, range, range).stream()
                    .filter(entity -> entity.getType() == customMob.getType())
                    .count();

            event.getEntity().remove();
            if (nearbyCustomMobs < customMob.getSpawner().getMaxNearbyCount()) {
                int availableSpawnCount = customMob.getSpawner().getMaxNearbyCount() - nearbyCustomMobs;
                int random = new Random().nextInt(Math.min(customMob.getSpawner().getSpawnCount(), availableSpawnCount)) + 1;
                for (int i = 0; i < random; i++) {
                    double offsetX = (new Random().nextDouble() * 2 - 1) * range;
                    double offsetZ = (new Random().nextDouble() * 2 - 1) * range;

                    Location spawnLocation = location.clone().add(offsetX, 0, offsetZ);
                    spawnLocation.setY(getNearestY(location.getWorld(), location.getX() + offsetX, location.getY(), location.getZ() + offsetZ));

                    CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(mobName).spawnCustomMob(spawnLocation);
                }
            }
        }
    }

    private double getNearestY(World world, double x, double y, double z) {
        Location location = new Location(world, x, y, z);

        for (int i = -2; i < 5; i++) {
            boolean blockUnder = !location.clone().add(0, i - 1, 0).getBlock().isPassable();
            boolean passable = location.clone().add(0, i, 0).getBlock().isPassable();
            boolean passableOver = location.clone().add(0, i + 1, 0).getBlock().isPassable();

            if (blockUnder && passable && passableOver) {
                return y + i;
            }
        }
        return y;
    }

    @EventHandler
    public void onSpawnerBreak(BlockBreakEvent event) {
        Manager manager = CustomMobs.getPlugin().getCustomMobsManager();
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if (!manager.getConfigManager().getSilkSpawner() || block.getType() != Material.SPAWNER || !itemStack.getType().toString().contains("PICKAXE")) {
            return;
        }

        CreatureSpawner spawnerBlock = (CreatureSpawner) block.getState();
        PersistentDataContainer container = spawnerBlock.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Spawner");

        if (itemStack.containsEnchantment(Enchantment.SILK_TOUCH) && container.has(key, PersistentDataType.STRING)) {
            ItemStack spawnerItem = manager.getCustomMobItem(manager.getCustomMob(container.get(key, PersistentDataType.STRING)), "spawner", 1);
            block.getLocation().getWorld().dropItem(block.getLocation(), spawnerItem);
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        switch (reason) {
            case CHUNK_GEN:
            case TRIAL_SPAWNER:
            case BUCKET:
            case SHEARED:
            case COMMAND:
            case SPAWNER_EGG:
            case CUSTOM:
                return;
        }

        Map<CustomMob, Double> customMobReplacePercentages = new HashMap<>();
        double totalPercentage = 0;
        for(CustomMob customMob : CustomMobs.getPlugin().getCustomMobsManager().getCustomMobs()) {
            if(customMob.getType() != event.getEntityType())
                continue;
            if (customMob.getCustomMobOption("Special") != null && ((Special) customMob.getCustomMobOption("Special")).getReplaceNaturalPercentage() != 0.0) {
                double percentage = ((Special) customMob.getCustomMobOption("Special")).getReplaceNaturalPercentage();
                customMobReplacePercentages.put(customMob, percentage);
                totalPercentage += percentage;
            }
        }

        if(customMobReplacePercentages.isEmpty())
            return;

        double roll = Math.random() * 100;
        if (roll > totalPercentage) return;

        double random = Math.random() * totalPercentage;
        double cumulative = 0;
        CustomMob chosen = null;
        for (Map.Entry<CustomMob, Double> entry : customMobReplacePercentages.entrySet()) {
            cumulative += entry.getValue();
            if (random <= cumulative) {
                chosen = entry.getKey();
                break;
            }
        }

        if (chosen != null) {
            event.setCancelled(true);
            chosen.spawnCustomMob(event.getLocation());
        }
    }
}

