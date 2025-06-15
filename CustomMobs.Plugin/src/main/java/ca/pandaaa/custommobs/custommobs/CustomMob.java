package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomEffectType;
import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomMobCustomEffect;
import ca.pandaaa.custommobs.custommobs.Events.CustomMobSpawnEvent;
import ca.pandaaa.custommobs.custommobs.Messages.SpawnDeathMessage;
import ca.pandaaa.custommobs.custommobs.Options.CustomMobOption;
import ca.pandaaa.custommobs.guis.EditCustomMobs.TypesGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CustomMob implements Listener {
    private final Map<UUID, BukkitTask> activeCustomEffectRunnables = new HashMap<>();
    private final Map<UUID, LocalDateTime> nextCooldownOccurences = new HashMap<>();
    private final LocalDateTime creationDate;
    private EntityType entityType;
    private final String customMobFileName;
    private HashMap<String, CustomMobOption> customMobOptions = new HashMap<>();
    private final HashMap<String, CustomMobCustomEffect> customMobCustomEffects = new HashMap<>();
    private int customEffectsCooldownDuration;
    private ItemStack item;
    private ItemStack spawnerItem;
    private final Equipment equipment;
    private final List<Drop> drops;
    private Spawner spawner;
    private String name;
    private final List<Sound> sounds;
    private final List<PotionEffect> potionEffects;
    private final List<SpawnDeathMessage> messages;
    private final CustomMobConfiguration mobConfiguration;

    public CustomMob(LocalDateTime creationDate,
                     EntityType entityType,
                     CustomMobConfiguration mobConfiguration) {
        this.creationDate = creationDate;
        this.entityType = entityType;
        this.mobConfiguration = mobConfiguration;
        this.customMobFileName = mobConfiguration.getFileName();
        this.item = mobConfiguration.getItem(CustomMobConfiguration.ITEM);
        this.spawnerItem = mobConfiguration.getItem(CustomMobConfiguration.SPAWNER_ITEM);
        this.equipment = mobConfiguration.getEquipment();
        this.potionEffects = mobConfiguration.getPotionEffects();
        this.spawner = mobConfiguration.getSpawner();
        this.drops = mobConfiguration.getDrops();
        this.name = mobConfiguration.getName();
        this.sounds = mobConfiguration.getSounds();
        this.messages = mobConfiguration.getMessages();
        this.customEffectsCooldownDuration = mobConfiguration.getCustomEffectsCooldownDuration();
    }

    /**
     * Constructor of the CustomMobs.
     */
    public void spawnCustomMob(Location location) {
        Entity customMob = Objects.requireNonNull(location.getWorld()).spawnEntity(location, entityType);

        // Naming
        if(name != null)
            customMob.setCustomName(Utils.applyFormat(name));

        // Sounds
        for(Sound sound : sounds) {
            sound.playSound(customMob);
        }

        // Message
        for(SpawnDeathMessage message : messages) {
            if(!message.isOnDeath())
                message.sendMessage(customMob);
        }

        // Equipment
        equipment.giveEquipments(customMob);

        // Drops
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Name");
        customMob.getPersistentDataContainer().set(key, PersistentDataType.STRING, customMobFileName.replaceAll(".yml", ""));

        // Options
        for(CustomMobOption customMobType : customMobOptions.values()) {
            customMobType.applyOptions(customMob);
        }

        // Potions
        for(PotionEffect potionEffect : potionEffects) {
            potionEffect.apply((LivingEntity) customMob);
        }

        CustomMobSpawnEvent customEvent = new CustomMobSpawnEvent(customMob);
        Bukkit.getServer().getPluginManager().callEvent(customEvent);
    }

    public void placeCustomMobSpawner(Location location) {
        location.getBlock().setType(Material.SPAWNER);
        CreatureSpawner spawnerBlock = (CreatureSpawner) location.getBlock().getState();

        if(this.spawner.areRequirementsDisabled())
            // Set this to be an invisible "entity" that spawns regardless of conditions.
            spawnerBlock.setSpawnedType(EntityType.AREA_EFFECT_CLOUD);
        else
            spawnerBlock.setSpawnedType(entityType);

        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Spawner");
        spawnerBlock.getPersistentDataContainer().set(key, PersistentDataType.STRING, customMobFileName.replaceAll(".yml", ""));

        this.spawner.setCharacteristics(spawnerBlock);

        spawnerBlock.update();
    }

    public void addCustomMobType(CustomMobOption customMobType) {
        customMobOptions.put(customMobType.getClass().getSimpleName().toLowerCase(), customMobType);
    }

    public void addCustomMobCustomEffect(CustomMobCustomEffect customMobCustomEffect) {
        customMobCustomEffects.put(customMobCustomEffect.getClass().getSimpleName().toLowerCase(), customMobCustomEffect);
    }

    public String getCustomMobFileName() {
        return customMobFileName;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public void setItem(ItemStack item) {
        mobConfiguration.setItemStack(CustomMobConfiguration.ITEM, item);
        this.item = mobConfiguration.getItem(CustomMobConfiguration.ITEM);
    }

    public ItemStack getSpawnerItem() {
        return spawnerItem.clone();
    }

    public void setSpawnerItem(ItemStack spawner) {
        mobConfiguration.setItemStack(CustomMobConfiguration.SPAWNER, spawner);
        this.spawnerItem = mobConfiguration.getItem(CustomMobConfiguration.SPAWNER);
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getCustomEffectsCooldownDuration() {
        return customEffectsCooldownDuration;
    }

    public void setCustomEffectsCooldownDuration(int customEffectsCooldownDuration) {
        mobConfiguration.setCustomEffectsCooldownDuration(customEffectsCooldownDuration);
        this.customEffectsCooldownDuration = customEffectsCooldownDuration;
    }

    public List<Drop> getDrops() {
        drops.sort(Comparator.comparingInt(Drop::getPriority));
        return drops;
    }

    public List<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
         this.potionEffects.add(potionEffect);
         mobConfiguration.setPotionEffects(this.potionEffects);
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void addSound(Sound sound) {
        this.sounds.add(sound);
        mobConfiguration.setSounds(this.sounds);
    }

    public void removeSound(int soundIndex) {
        sounds.remove(soundIndex);
        mobConfiguration.setSounds(sounds);
    }

    public void editSound(Sound sound, int index) {
        this.sounds.set(index, sound);
        mobConfiguration.setSounds(this.sounds);
    }

    /* === NAME (OVER HEAD) === */

    /**
     * Get the CustomMob name.
     * @return The pretty name of the CustomMob.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the CustomMob name.
     * @param name The pretty name of the CustomMob.
     */
    public void setName(String name) {
        mobConfiguration.setName(name);
        this.item = getCustomMobConfiguration().getItem(CustomMobConfiguration.ITEM);
        this.spawnerItem = getCustomMobConfiguration().getItem(CustomMobConfiguration.SPAWNER);
        this.name = name;
    }

    /* === SPAWNER === */

    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
        mobConfiguration.setSpawner(spawner);
    }

    public Spawner getSpawner() {
        return spawner;
    }

    /* === POTION === */

    public void editPotion(PotionEffect potionEffect, int index) {
        this.potionEffects.set(index, potionEffect);
        mobConfiguration.setPotionEffects(potionEffects);
    }

    public void removePotionItem(int potionIndex) {
        potionEffects.remove(potionIndex);
        mobConfiguration.setPotionEffects(potionEffects);
    }

    /* === DROPS === */

    /**
     * Add a custom Drop to the drop list of the CustomMob.
     * @param drop The DropItem to be added to the list.
     */
    public void addDrop(Drop drop) {
        if(!drop.getCustomType().isEmpty())
            removeDropItem(drop.getCustomType());
        drops.add(drop);
        mobConfiguration.setDrops(drops);
    }

    public void editDrop(Drop drop, int index) {
        drops.set(index, drop);
        mobConfiguration.setDrops(drops);
    }

    /**
     * Remove a custom Drop from the drop list of the CustomMob.
     * @param dropIndex The index of the Drop to be removed to the list.
     */
    public void removeDropItem(int dropIndex) {
        drops.remove(dropIndex);
        mobConfiguration.setDrops(drops);
    }

    public void removeDropItem(String customType) {
        for(int i = 0; i < getDrops().size(); i++) {
            if(getDrops().get(i).getCustomType().equalsIgnoreCase(customType)) {
                removeDropItem(i);
                break;
            }
        }
    }

    /* === TYPE === */

    public EntityType getType() {
        return entityType;
    }

    public void setType(EntityType entityType) {
        if(item.getType() == TypesGUI.getSpawnEggMaterial(getType())) {
            ItemStack newItem = item.clone();
            newItem.setType(TypesGUI.getSpawnEggMaterial(entityType));
            setItem(newItem);
        }
        mobConfiguration.resetType(this.entityType);
        mobConfiguration.setType(entityType);
        this.entityType = entityType;

        for(int i = 0; i < getDrops().size(); i++) {
            if(getDrops().get(i).getCustomType().equalsIgnoreCase("Saddle")) {
                removeDropItem(i);
                break;
            }
        }

        customMobOptions = new HashMap<>();
        mobConfiguration.setCustomMobConfigurations(this, entityType);
    }

    /* === OPTIONS === */

    /**
     * Get all the options of the CustomMob.
     * @return A collection of the CustomMobOption(s) assignable to the CustomMob.
     */
    public Collection<CustomMobOption> getCustomMobOptions() {
        return customMobOptions.values();
    }

    /**
     * Get the option associated with the provided name.
     * @param optionName The option name. e.g.: Special
     * @return The actual CustomMobOption associated with the provided option name.
     */
    public CustomMobOption getCustomMobOption(String optionName) {
        return customMobOptions.get(optionName.toLowerCase());
    }

    /* === CUSTOM EFFECTS === */
    public void enableCustomEffects(Entity entity) {
        UUID entityId = entity.getUniqueId();
        if (activeCustomEffectRunnables.containsKey(entityId) || entity.isDead())
            return;

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Entity> nearbyEntities = entity.getNearbyEntities(32D, 32D, 32D);
                if(nearbyEntities.stream().noneMatch(e -> e instanceof Player)) {
                    cancelCustomEffects(entityId);
                    nextCooldownOccurences.put(entityId, null);
                    return;
                }

                List<Entity> impactEntities = entity.getNearbyEntities(1D, 1D, 1D);
                if(impactEntities.stream().anyMatch(e -> e instanceof Player)) {
                    customMobCustomEffects.values().stream()
                            .filter(effect -> CustomEffectType.ON_IMPACT.equals(effect.getCustomEffectType()))
                            .filter(CustomMobCustomEffect::isEnabled)
                            .forEach(effect ->  {
                                effect.triggerCustomEffect(entity);
                                for(Entity player : impactEntities.stream().filter(e -> e instanceof Player).toList()) {
                                    effect.trySendCustomEffectMessage((Player) player);
                                }
                            });
                }

                if(nextCooldownOccurences.get(entityId) == null)
                    nextCooldownOccurences.put(entityId, LocalDateTime.now().plusSeconds(3));
                if(nextCooldownOccurences.get(entityId).isAfter(LocalDateTime.now()))
                    return;

                List<CustomMobCustomEffect> cooldownCustomEffects = customMobCustomEffects.values().stream()
                        .filter(effect -> CustomEffectType.COOLDOWN.equals(effect.getCustomEffectType()))
                        .filter(CustomMobCustomEffect::isEnabled)
                        .toList();
                if(!cooldownCustomEffects.isEmpty()) {
                    int randomIndex = new Random().nextInt(cooldownCustomEffects.size());
                    cooldownCustomEffects.get(randomIndex).triggerCustomEffect(entity);
                    double radius = cooldownCustomEffects.get(randomIndex).getMessageRadius();
                    if(radius <= 0)
                        cooldownCustomEffects.get(randomIndex).tryBroadcastCustomEffectMessage();
                    else {
                        for (Entity entity : entity.getNearbyEntities(radius, radius, radius).stream().filter(x -> x instanceof Player).toList())
                            cooldownCustomEffects.get(randomIndex).trySendCustomEffectMessage((Player) entity);
                    }
                    nextCooldownOccurences.put(entityId, LocalDateTime.now().plusSeconds(customEffectsCooldownDuration));
                }
            }
        }.runTaskTimer(CustomMobs.getPlugin(), 0L, 10L); // 0.5 sec

        activeCustomEffectRunnables.put(entityId, task);
    }

    public void cancelCustomEffects(UUID entityId) {
        BukkitTask task = activeCustomEffectRunnables.remove(entityId);
        if (task != null) {
            task.cancel();
        }
    }

    /* === Others === */

    public Collection<CustomMobCustomEffect> getCustomMobCustomEffects() {
        return customMobCustomEffects.values();
    }

    public CustomMobCustomEffect getCustomMobCustomEffect(String customEffectName) {
        return customMobCustomEffects.get(customEffectName.toLowerCase());
    }

    public void removeMessage(int index) {
        messages.remove(index);
        mobConfiguration.setMessages(messages);
    }

    public void editMessage(int index, SpawnDeathMessage message) {
        if(messages.size() - 1 >= index)
            messages.set(index, message);
        else
            messages.add(message);
        mobConfiguration.setMessages(messages);
    }

    public List<SpawnDeathMessage> getCustomMobMessages() {
        return messages;
    }

    public void delete() {
        CustomMobs.getPlugin().getCustomMobsManager().removeCustomMob(customMobFileName.replaceAll(".yml", ""));
        mobConfiguration.setDeleted(LocalDate.now().plusDays(14));
    }

    /**
     * The date provided by the creation date of the mob's yaml file.
     * @return The creation date of the mob.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public CustomMobConfiguration getCustomMobConfiguration() {
        return mobConfiguration;
    }
}
