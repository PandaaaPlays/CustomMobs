package ca.pandaaa.custommobs.custommobs;

import org.bukkit.Registry;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Spawner implements ConfigurationSerializable {
    private int spawnCount; // 4 : 1 a 25
    private int maxNearbyCount; // 6 : 1 a 150
    private int spawnRange; // How many blocks around the spawner can the mob spawn // 4
    private int spawnDelay; // Delay before the first spawn // 0 a 72000
    private int minSpawnDelay; // 200 : 1 a max
    private int maxSpawnDelay; // 799 : min a 72000 foufounne
    private int requiredPlayerRange; // Default 16 : 0 a 128
    private boolean disableRequirements; //on off

    public Spawner(int spawnCount, int maxNearbyCount, int spawnRange, int spawnDelay, int minSpawnDelay, int maxSpawnDelay, int requiredPlayerRange, boolean disableRequirements) {
        this.spawnCount = spawnCount;
        this.maxNearbyCount = maxNearbyCount;
        this.spawnRange = spawnRange;
        this.spawnDelay = spawnDelay;
        this.minSpawnDelay = minSpawnDelay;
        this.maxSpawnDelay = maxSpawnDelay;
        this.requiredPlayerRange = requiredPlayerRange;
        this.disableRequirements = disableRequirements;
    }

    // SpawnCount
    public void setSpawnCount(int spawnCount){
        this.spawnCount = spawnCount;
    }

    public int getSpawnCount(){
        return spawnCount;
    }

    // MaxNearbyCount
    public void setMaxNearbyCount(int maxNearbyCount){
        this.maxNearbyCount = maxNearbyCount;
    }

    public int getMaxNearbyCount(){
        return maxNearbyCount;
    }

    // SpawnRange
    public void setSpawnRange(int spawnRange){
        this.spawnRange = spawnRange;
    }

    public int getSpawnRange(){
        return spawnRange;
    }

    // SpawnDelay
    public void setSpawnDelay(int spawnDelay){
        this.spawnDelay = spawnDelay;
    }

    public int getSpawnDelay(){
        return spawnDelay;
    }

    // minSpawnDelay
    public void setMinSpawnDelay(int minSpawnDelay){
        this.minSpawnDelay = minSpawnDelay;
    }

    public int getMinSpawnDelay(){
        return minSpawnDelay;
    }

    // maxSpawnDelay
    public void setMaxSpawnDelay(int maxSpawnDelay){
        this.maxSpawnDelay = maxSpawnDelay;
    }

    public int getMaxSpawnDelay(){
        return maxSpawnDelay;
    }

    // requiredPlayerRange
    public void setRequiredPlayerRange(int requiredPlayerRange){
        this.requiredPlayerRange = requiredPlayerRange;
    }

    public int getRequiredPlayerRange(){
        return requiredPlayerRange;
    }

    // DisableChecks
    public void setDisableRequirements(boolean disableRequirements){
        this.disableRequirements = disableRequirements;
    }

    public boolean getDisableRequirements(){
        return disableRequirements;
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

    public boolean areChecksDisabled() {
        return disableRequirements;
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
        data.put("disable-requirements", disableRequirements);
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
        boolean requiresDarkness = (boolean) data.get("disable-requirements");
        return new Spawner(spawnCount, maxNearbyCount, spawnRange, spawnDelay, minimumSpawnDelay, maximumSpawnDelay, requiredPlayerRange, requiresDarkness);
    }
}
