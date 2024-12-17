package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.DropConditions;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

// TODO this class needs a big refactor.
public class Events implements Listener {
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
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();

        if(!(entity instanceof Player))
            return;

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Name");
        if(!event.getEntity().getPersistentDataContainer().getKeys().contains(key))
            return;

        double damage = 0;
        NamespacedKey damageKey = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Damage." + entity.getUniqueId());
        if(event.getEntity().getPersistentDataContainer().getKeys().contains(damageKey)) {
            damage = event.getEntity().getPersistentDataContainer().get(damageKey, PersistentDataType.DOUBLE);
        }
        damage += event.getFinalDamage();
        event.getEntity().getPersistentDataContainer().set(damageKey, PersistentDataType.DOUBLE, damage);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Name");
        if(!event.getEntity().getPersistentDataContainer().getKeys().contains(key))
            return;

        String name = event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        CustomMob customMob = CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(name);

        if(customMob.getDrops().isEmpty())
            return;
        event.getDrops().clear();

        List<Drop> globalDrops = new ArrayList<>();
        List<Drop> playerDrops = new ArrayList<>();
        for(Drop drop : customMob.getDrops()) {
            if(drop.getDropCondition() == DropConditions.NEARBY)
                playerDrops.add(drop);
            else
                globalDrops.add(drop);
        }

        sendPlayerDrops(playerDrops, event.getEntity());
        sendGlobalDrops(globalDrops, event.getEntity());
    }

    // Menu Item deletion //
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

    private void sendPlayerDrops(List<Drop> playerDrops, Entity entity) {
        Map<Player, List<Drop>> successfulPlayerDrops = new HashMap<>();
        for (Drop drop : playerDrops) {
            double nearbyRange = drop.getNearbyRange();
            List<Player> nearbyPlayers = entity.getNearbyEntities(nearbyRange, nearbyRange, nearbyRange).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            for (Player player : nearbyPlayers) {
                if (drop.draw()) {
                    List<Drop> drops = new ArrayList<>();
                    if (successfulPlayerDrops.containsKey(player))
                        drops = successfulPlayerDrops.get(player);
                    drops.add(drop);
                    successfulPlayerDrops.put(player, drops);
                }
            }
        }

        for (Player player : successfulPlayerDrops.keySet()) {
            for (Drop drop : filterJustOnePerGroup(successfulPlayerDrops.get(player))) {
                player.getInventory().addItem(drop.getItemStack());
            }
            // TODO messages
        }
    }

    // TODO refactor
    private List<Drop> filterJustOnePerGroup(List<Drop> dropList) {
        if(dropList.isEmpty())
            return null;
        if(dropList.size() == 1) {
            List<Drop> drops = new ArrayList<>();
            drops.add(dropList.get(0));
            return drops;
        }

        List<Drop> returnedDrops = new ArrayList<>();
        Map<DyeColor, List<Drop>> dropsByGroup = new HashMap<>();
        for(Drop drop : dropList) {
            if(drop.getGroupColor() == null)
                returnedDrops.add(drop);
            else {
                List<Drop> currentGroupDrops = new ArrayList<>();
                if(dropsByGroup.containsKey(drop.getGroupColor()))
                    currentGroupDrops = dropsByGroup.get(drop.getGroupColor());
                currentGroupDrops.add(drop);
                dropsByGroup.put(drop.getGroupColor(), currentGroupDrops);
            }
        }

        for(DyeColor group : dropsByGroup.keySet()) {
            List<Drop> groupDropList = dropsByGroup.get(group);
            if(groupDropList.size() == 1)
                returnedDrops.add(groupDropList.get(0));
            else {
                double totalChance = 0;
                for (Drop drop : groupDropList) {
                    totalChance += drop.getProbability();
                }

                double randomValue = Math.random() * totalChance;
                // Iterate over the drops and subtract their chances until the random value is reached
                for (Drop drop : groupDropList) {
                    randomValue -= drop.getProbability();
                    if (randomValue <= 0) {
                        returnedDrops.add(drop);  // Return the drop when we find the one that corresponds to the random value
                        break;
                    }
                }
            }
        }

        return returnedDrops;
    }

    private void sendGlobalDrops(List<Drop> globalDrops, LivingEntity entity) {
        List<Drop> successfulGlobalDrops = new ArrayList<>();
        for (Drop drop : globalDrops){
            if (drop.draw())
                successfulGlobalDrops.add(drop);
        }

        List<Drop> globalFilteredDrops = filterJustOnePerGroup(successfulGlobalDrops);
        if(globalFilteredDrops == null)
            return;
// TODO MAYBE A BUG WHEN DROPPING WITH LOW PERCENTS
        // Global drops (one for everyone)
        for (Drop successfulDrop : globalFilteredDrops) {
            switch (successfulDrop.getDropCondition()) {
                case KILLER:
                    LivingEntity killer = entity.getKiller();
                    if (killer instanceof Player)
                        entity.getKiller().getInventory().addItem(successfulDrop.getItemStack());
                    break;
                case DROP:
                    entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), successfulDrop.getItemStack());
                    break;
                case MOST_DAMAGE:
                    double mostDamage = 0;
                    Player player = null;
                    for(NamespacedKey entityNamespacedKey : entity.getPersistentDataContainer().getKeys()) {
                        if (entityNamespacedKey.getKey().contains("CustomMobs.Damage.".toLowerCase())) {
                            UUID uuid = UUID.fromString(entityNamespacedKey.getKey().replaceAll("CustomMobs.Damage.".toLowerCase(), ""));
                            double damage = entity.getPersistentDataContainer().get(entityNamespacedKey, PersistentDataType.DOUBLE);
                            // TODO make sure this work if the player is offline (should not receive it)
                            if(Bukkit.getPlayer(uuid) != null && damage > mostDamage) {
                                mostDamage = damage;
                                player = Bukkit.getPlayer(uuid);
                            }
                        }
                    }
                    if(player != null)
                        player.getInventory().addItem(successfulDrop.getItemStack());
                    break;
            }
        }
        // TODO Message
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        if(event.getEntity().getType() != EntityType.AREA_EFFECT_CLOUD)
            return;

        PersistentDataContainer container = event.getSpawner().getPersistentDataContainer();
        Location location = event.getEntity().getLocation();

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Spawner");
        if (container.has(key, PersistentDataType.STRING)) {
            String mobName = container.get(key, PersistentDataType.STRING);
            CustomMob customMob = CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(mobName);
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
                    double offsetY = (new Random().nextDouble() * 2 - 1) * range;
                    double offsetZ = (new Random().nextDouble() * 2 - 1) * range;
                    Location spawnLocation = location.clone().add(offsetX, offsetY, offsetZ);

                    CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(mobName).spawnCustomMob(spawnLocation);
                }
            }
        }
    }

    @EventHandler
    public void onSpawnerBreak(BlockBreakEvent event) {
        Manager manager = CustomMobs.getPlugin().getCustomMobsManager();
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        if(!manager.configManager.getSilkSpawner() || block.getType() != Material.SPAWNER || !itemStack.getType().toString().contains("PICKAXE")){
            return;
        }

        CreatureSpawner spawnerBlock = (CreatureSpawner) block.getState();
        PersistentDataContainer container = spawnerBlock.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Spawner");

        if(itemStack.containsEnchantment(Enchantment.SILK_TOUCH) && container.has(key, PersistentDataType.STRING) ) {

            ItemStack spawnerItem = manager.getCustomMobItem(manager.getCustomMob(container.get(key, PersistentDataType.STRING)),"spawner",1);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), spawnerItem);
        }
    }
}

