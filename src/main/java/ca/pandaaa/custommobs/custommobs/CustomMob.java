package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.options.CustomMobType;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomMob implements Listener {
    private final EntityType entityType;
    private final String customMobFileName;
    private final HashMap<String, CustomMobType> customMobTypes = new HashMap<>();
    private ItemStack item;
    private ItemStack spawner;
    private String name;
    private final List<CustomMobsSound> sounds;
    private final CustomMobConfiguration mobConfiguration;

    public CustomMob(EntityType entityType,
                     String customMobFileName,
                     ItemStack item,
                     ItemStack spawner,
                     String name,
                     List<CustomMobsSound> sounds,
                     CustomMobConfiguration mobConfiguration) {
        this.entityType = entityType;
        this.customMobFileName = customMobFileName;
        this.item = item;
        this.spawner = spawner;
        this.name = name;
        this.sounds = sounds;
        this.mobConfiguration = mobConfiguration;
    }

    public void spawnCustomMob(Location location) {
        Entity customMob = Objects.requireNonNull(location.getWorld()).spawnEntity(location, entityType);

        // Naming
        if(name != null)
            customMob.setCustomName(Utils.applyFormat(name));

        // Sounds
        for(CustomMobsSound sound : sounds) {
            sound.playSound(customMob);
        }

        // Message
        mobConfiguration.getMessage().sendMessage(customMob);

        // Options
        for(CustomMobType customMobType : customMobTypes.values()) {
            customMobType.applyOptions(customMob);
        }
    }

    public void placeCustomMobSpawner(Location location) {
        // TODO All of the spawners characteristics
        location.getBlock().setType(Material.DIAMOND_BLOCK);
    }

    public void addCustomMobType(CustomMobType customMobType) {
        customMobTypes.put(customMobType.getClass().getSimpleName().toLowerCase(), customMobType);
    }

    public String getCustomMobFileName() {
        return customMobFileName;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getSpawner() {
        return spawner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        getCustomMobConfiguration().setName(name);
        this.item = getCustomMobConfiguration().getItem();
        this.spawner = getCustomMobConfiguration().getSpawner();
        this.name = name;
    }

    public EntityType getType() {
        return entityType;
    }

    public CustomMobsMessage getCustomMobMessage() {
        return mobConfiguration.getMessage();
    }

    public Collection<CustomMobType> getCustomMobTypes() {
        return customMobTypes.values();
    }

    public CustomMobConfiguration getCustomMobConfiguration() {
        return mobConfiguration;
    }

    public CustomMobType getCustomMobType(String option) {
        return customMobTypes.get(option.toLowerCase());
    }
}
