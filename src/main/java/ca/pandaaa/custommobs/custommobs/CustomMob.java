package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.options.CustomMobType;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CustomMob {
    private final EntityType entityType;
    private final String customMobFileName;
    private final List<CustomMobType> customMobTypes = new ArrayList<>();

    public CustomMob(EntityType entityType, String customMobFileName) {
        this.entityType = entityType;
        this.customMobFileName = customMobFileName;
    }

    public void spawnCustomMob(Location location) {
        Entity customMob = Objects.requireNonNull(location.getWorld()).spawnEntity(location, entityType);



        customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMob.Name"), PersistentDataType.STRING, customMobFileName);
        customMob.getPersistentDataContainer().set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMob.Id"), PersistentDataType.STRING, UUID.randomUUID().toString());

        for(CustomMobType customMobType : customMobTypes) {
            customMobType.applyOptions(customMob);
        }
    }

    public void addCustomMobType(CustomMobType customMobType) {
        customMobTypes.add(customMobType);
    }
}
