package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.custommobs.Messages.DropMessage;
import ca.pandaaa.custommobs.custommobs.Messages.Message;
import ca.pandaaa.custommobs.utils.DropConditions;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class DropManager {

    public static void sendDrops(List<Drop> drops, LivingEntity killedEntity) {
        List<Drop> globalDrops = new ArrayList<>();
        List<Drop> playerDrops = new ArrayList<>();
        for(Drop drop : drops) {
            if(drop.getDropCondition() == DropConditions.NEARBY)
                playerDrops.add(drop);
            else
                globalDrops.add(drop);
        }

        sendPlayerDrops(playerDrops, killedEntity);
        sendGlobalDrops(globalDrops, killedEntity);
    }

    /**
     * Manages the sending of the drops that are per player.
     * Even if 2 drops have 100% chance to drop, they will not drop if they are in the same group.
     * @param playerDrops The drops that are of the type : "Nearby"
     * @param entity The killed entity.
     */
    private static void sendPlayerDrops(List<Drop> playerDrops, Entity entity) {
        Map<Player, List<Drop>> filteredDropsPerPlayer = new HashMap<>();
        for (Drop drop : playerDrops) {
            // Get nearby players (in the range)
            double nearbyRange = drop.getNearbyRange();
            List<Player> nearbyPlayers = entity.getNearbyEntities(nearbyRange, nearbyRange, nearbyRange).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            // Tries and draw ever single "nearby" drops (done for every nearby player)
            // No looting for the nearby (because it does not make sense...)
            for (Player player : nearbyPlayers) {
                if (drop.draw()) {
                    List<Drop> drops = new ArrayList<>();
                    if (filteredDropsPerPlayer.containsKey(player))
                        drops = filteredDropsPerPlayer.get(player);
                    drops.add(drop);
                    filteredDropsPerPlayer.put(player, drops);
                }
            }
        }

        // Send the drops (only one per group max.)
        for (Player player : filteredDropsPerPlayer.keySet()) {
            for (Drop drop : filterDropPerGroup(filteredDropsPerPlayer.get(player))) {
                player.getInventory().addItem(drop.getItemStack());
                for(Message message : drop.getMessages()) {
                    ((DropMessage) message).sendMessage(player);
                }
            }
        }
    }

    /**
     * Manages the sending of the drops that are global (not calculated per-player).
     * Even if 2 drops have 100% chance to drop, they will not drop if they are in the same group.
     * @param globalDrops The drops that are of the types : "Killer", "Drop" or "Most damage"
     * @param entity The killed entity (it's living / before death state).
     */
    private static void sendGlobalDrops(List<Drop> globalDrops, LivingEntity entity) {
        List<Drop> drawnDrops = new ArrayList<>();

        // Draw the drops with the looting (if enabled)
        if(entity.getKiller() != null) {
            ItemStack item = entity.getKiller().getInventory().getItemInMainHand();
            for (Drop drop : globalDrops){
                if (drop.draw(item.getEnchantmentLevel(Enchantment.LOOTING)))
                    drawnDrops.add(drop);
            }
        }
        if(drawnDrops.isEmpty())
            return;

        // Filter the drops for 1 per group
        List<Drop> filteredDrops = filterDropPerGroup(drawnDrops);
        if(filteredDrops == null)
            return;

        // Global drops (one for everyone)
        for (Drop successfulDrop : filteredDrops) {
            switch (successfulDrop.getDropCondition()) {
                case KILLER:
                    Player killer = entity.getKiller();
                    if (killer instanceof Player) {
                        entity.getKiller().getInventory().addItem(successfulDrop.getItemStack());
                        for(Message message : successfulDrop.getMessages()) {
                            ((DropMessage) message).sendMessage(killer);
                        }
                    }
                    break;
                case DROP:
                    entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), successfulDrop.getItemStack());
                    for(Message message : successfulDrop.getMessages()) {
                        message.sendMessage(entity);
                    }
                    break;
                case MOST_DAMAGE:
                    double mostDamage = 0;
                    Player player = null;
                    for(NamespacedKey entityNamespacedKey : entity.getPersistentDataContainer().getKeys()) {
                        if (entityNamespacedKey.getKey().contains("CustomMobs.Damage.".toLowerCase())) {
                            UUID uuid = UUID.fromString(entityNamespacedKey.getKey().replaceAll("CustomMobs.Damage.".toLowerCase(), ""));
                            double damage = entity.getPersistentDataContainer().get(entityNamespacedKey, PersistentDataType.DOUBLE);
                            if(Bukkit.getPlayer(uuid) != null && damage > mostDamage) {
                                mostDamage = damage;
                                player = Bukkit.getPlayer(uuid);
                            }
                        }
                    }
                    if(player != null) {
                        player.getInventory().addItem(successfulDrop.getItemStack());
                        for(Message message : successfulDrop.getMessages()) {
                            ((DropMessage) message).sendMessage(player);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Filters the given drops to choose only 1 per group.
     * @param unfilteredDrops The unfiltered list of drop (with possible multiple drops in the same group)
     * @return A list of Drops with a maximum of 1 drop per group.
     */
    private static List<Drop> filterDropPerGroup(List<Drop> unfilteredDrops) {
        if(unfilteredDrops.isEmpty())
            return null;

        List<Drop> filteredDrops = new ArrayList<>();

        // Size one list (prevents the calculation of the rest for this simple (but most used) case)
        if(unfilteredDrops.size() == 1) {
            filteredDrops.add(unfilteredDrops.get(0));
            return filteredDrops;
        }

        // Mapping Group to a list of Drops.
        Map<DyeColor, List<Drop>> dropsByGroup = new HashMap<>();
        for(Drop drop : unfilteredDrops) {
            if(drop.getGroupColor() == null)
                filteredDrops.add(drop);
            else {
                List<Drop> currentGroupDrops = new ArrayList<>();
                if(dropsByGroup.containsKey(drop.getGroupColor()))
                    currentGroupDrops = dropsByGroup.get(drop.getGroupColor());
                currentGroupDrops.add(drop);
                dropsByGroup.put(drop.getGroupColor(), currentGroupDrops);
            }
        }

        // Applies the filter of every groups.
        for(DyeColor group : dropsByGroup.keySet()) {
            List<Drop> groupDrops = dropsByGroup.get(group);
            // Only 1 drop of the specified group.
            if(groupDrops.size() == 1)
                filteredDrops.add(groupDrops.get(0));
            // Multiple drops of the specified group.
            else {
                double totalChance = 0;
                for (Drop drop : groupDrops) {
                    totalChance += drop.getProbability(); // Total = Sum of the probability
                }

                double randomValue = Math.random() * totalChance;
                // Iterate over the drops and subtract their chances until the random value is reached
                for (Drop drop : groupDrops) {
                    randomValue -= drop.getProbability();
                    if (randomValue <= 0) {
                        filteredDrops.add(drop);  // Return the drop when we find the one that corresponds to the random value
                        break;
                    }
                }
            }
        }

        return filteredDrops;
    }

}
