package ca.pandaaa.custommobs.custommobs;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.spawner.BaseSpawner;

import java.util.HashMap;
import java.util.Map;

public class Spawner implements ConfigurationSerializable {
    private int spawnCount;
    private int maxNearbyCount;
    private int spawnRange;
    private int spawnDelay; // Delay before the first spawn
    private int minSpawnDelay;
    private int maxSpawnDelay;
    private int requiredPlayerRange;

    public Spawner(int spawnCount, int maxNearbyCount, int spawnRange, int spawnDelay, int minSpawnDelay, int maxSpawnDelay, int requiredPlayerRange) {
        this.spawnCount = spawnCount;
        this.maxNearbyCount = maxNearbyCount;
        this.spawnRange = spawnRange;
        this.spawnDelay = spawnDelay;
        this.minSpawnDelay = minSpawnDelay;
        this.maxSpawnDelay = maxSpawnDelay;
        this.requiredPlayerRange = requiredPlayerRange;
    }

    public void setCharacteristics(org.bukkit.spawner.Spawner spawner) {
        spawner.setSpawnCount(spawnCount);
        spawner.setMaxNearbyEntities(maxNearbyCount);
        spawner.setSpawnRange(spawnRange);
        spawner.setDelay(spawnDelay);
        spawner.setMinSpawnDelay(minSpawnDelay);
        spawner.setMaxSpawnDelay(maxSpawnDelay);
        spawner.setRequiredPlayerRange(requiredPlayerRange);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("spawn-count", spawnCount);
        data.put("maximum-nearby-count", maxNearbyCount);
        data.put("spawn-range", spawnRange);
        data.put("spawn-delay", spawnDelay);
        data.put("minimum-spawn-delay", minSpawnDelay);
        data.put("maximum-spawn-delay", maxSpawnDelay);
        data.put("required-player-range", requiredPlayerRange);
        return data;
    }

    public static Spawner deserialize(Map<String, Object> data) {
        int spawnCount = (int) data.get("spawn-count");
        int maxNearbyCount = (int) data.get("maximum-nearby-count");
        int spawnRange = (int) data.get("spawn-range");
        int spawnDelay = (int) data.get("spawn-delay");
        int minimumSpawnDelay = (int) data.get("minimum-spawn-delay");
        int maximumSpawnDelay = (int) data.get("maximum-spawn-delay");
        int requiredPlayerRange = (int) data.get("required-player-range");
        return new Spawner(spawnCount, maxNearbyCount, spawnRange, spawnDelay, minimumSpawnDelay, maximumSpawnDelay, requiredPlayerRange);
    }
}
