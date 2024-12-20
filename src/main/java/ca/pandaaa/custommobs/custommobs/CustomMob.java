package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.options.CustomMobOption;
import ca.pandaaa.custommobs.guis.EditCustomMobs.TypesGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomMob implements Listener {
    private final LocalDateTime creationDate;
    private EntityType entityType;
    private final String customMobFileName;
    private HashMap<String, CustomMobOption> customMobOptions = new HashMap<>();
    private ItemStack item;
    private ItemStack spawnerItem;
    private final Equipment equipment;
    private final List<Drop> drops;
    private Spawner spawner;
    private String name;
    private final List<Sound> sounds;
    private final List<PotionEffect> potionEffects;
    private final CustomMobConfiguration mobConfiguration;;

    public CustomMob(LocalDateTime creationDate,
                     EntityType entityType,
                     String customMobFileName,
                     ItemStack item,
                     ItemStack spawnerItem,
                     Equipment equipment,
                     Spawner spawner,
                     List<PotionEffect> potionEffects,
                     List<Drop> drops,
                     String name,
                     List<Sound> sounds,
                     CustomMobConfiguration mobConfiguration) {
        this.creationDate = creationDate;
        this.entityType = entityType;
        this.customMobFileName = customMobFileName;
        this.item = item;
        this.spawnerItem = spawnerItem;
        this.equipment = equipment;
        this.potionEffects = potionEffects;
        this.spawner = spawner;
        this.drops = drops;
        this.name = name;
        this.sounds = sounds;
        this.mobConfiguration = mobConfiguration;
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
        mobConfiguration.getMessages().sendMessages(customMob);

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

    public List<Drop> getDrops() {
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
        drops.add(drop);
        mobConfiguration.setDrops(drops);
    }

    public void editDrop(Drop drop, int index) {
        drops.set(index, drop);
        mobConfiguration.setDrops(drops);
    }

    // TODO This clearly works... Might not really be the best thing to just trust that the order is synchronized between the GUI and the List index...
    /**
     * Remove a custom Drop from the drop list of the CustomMob.
     * @param dropIndex The index of the Drop to be removed to the list.
     */
    public void removeDropItem(int dropIndex) {
        drops.remove(dropIndex);
        mobConfiguration.setDrops(drops);
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

    /* === Others === */

    public Messages getCustomMobMessages() {
        return mobConfiguration.getMessages();
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
