package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.options.CustomMobOption;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CustomMob implements Listener {
    private LocalDateTime creationDate;
    private EntityType entityType;
    private final String customMobFileName;
    private HashMap<String, CustomMobOption> customMobOptions = new HashMap<>();
    private ItemStack item;
    private ItemStack spawner;
    private final Equipment equipment;
    private final List<DropItem> dropItems;
    private String name;
    private final List<Sound> sounds;
    private final CustomMobConfiguration mobConfiguration;

    public CustomMob(LocalDateTime creationDate,
                     EntityType entityType,
                     String customMobFileName,
                     ItemStack item,
                     ItemStack spawner,
                     Equipment equipment,
                     List<DropItem> dropItems,
                     String name,
                     List<Sound> sounds,
                     CustomMobConfiguration mobConfiguration) {
        this.creationDate = creationDate;
        this.entityType = entityType;
        this.customMobFileName = customMobFileName;
        this.item = item;
        this.spawner = spawner;
        this.equipment = equipment;
        this.dropItems = dropItems;
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


        // Options
        for(CustomMobOption customMobType : customMobOptions.values()) {
            customMobType.applyOptions(customMob);
        }
    }

    public void placeCustomMobSpawner(Location location) {
        // TODO All of the spawners characteristics
        location.getBlock().setType(Material.DIAMOND_BLOCK);
    }

    public void addCustomMobType(CustomMobOption customMobType) {
        customMobOptions.put(customMobType.getClass().getSimpleName().toLowerCase(), customMobType);
    }

    public String getCustomMobFileName() {
        return customMobFileName;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        mobConfiguration.setItemStack(CustomMobConfiguration.ITEM, item);
        this.item = mobConfiguration.getItem(CustomMobConfiguration.ITEM);
    }

    public ItemStack getSpawner() {
        return spawner;
    }

    public void setSpawner(ItemStack spawner) {
        mobConfiguration.setItemStack(CustomMobConfiguration.SPAWNER, spawner);
        this.spawner = mobConfiguration.getItem(CustomMobConfiguration.SPAWNER);
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public List<DropItem> getDropItems() {
        return dropItems;
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
        this.spawner = getCustomMobConfiguration().getItem(CustomMobConfiguration.SPAWNER);
        this.name = name;
    }

    /* === DROP ITEMS === */

    /**
     * Add a custom DropItem to the drop list of the CustomMob.
     * @param dropItem The DropItem to be added to the list.
     */
    public void addDropItem(DropItem dropItem) {
        dropItems.add(dropItem);
        mobConfiguration.setDropItems(dropItems);
    }

    // TODO This clearly works... Might not really be the best thing to just trust that the order is synchronized between the GUI and the List index...
    /**
     * Remove a custom DropItem from the drop list of the CustomMob.
     * @param dropItemIndex The index of the DropItem to be removed to the list.
     */
    public void removeDropItem(int dropItemIndex) {
        dropItems.remove(dropItemIndex);
        mobConfiguration.setDropItems(dropItems);
    }

    /* === TYPE === */

    public EntityType getType() {
        return entityType;
    }

    public void setType(EntityType entityType) {
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
        mobConfiguration.setDeleted(LocalDate.now().plusDays(14));
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public CustomMobConfiguration getCustomMobConfiguration() {
        return mobConfiguration;
    }
}
