package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Manager {
    ConfigurationManager configManager;
    private final List<CustomMobConfiguration> mobsConfigurations;
    private final Map<String, CustomMob> customMobs = new HashMap<>();

    public Manager(ConfigurationManager configManager, List<CustomMobConfiguration> mobsConfigurations) {
        this.configManager = configManager;
        this.mobsConfigurations = mobsConfigurations;
    }

    public void loadCustomMobs() {
        for (CustomMobConfiguration mobConfiguration : mobsConfigurations) {
            CustomMob customMob = mobConfiguration.loadCustomMob();
            if (customMob != null)
                customMobs.put(mobConfiguration.getFileName().toLowerCase().replace(".yml", ""), customMob);
        }
    }

    public CustomMob addCustomMob(String customMobName, EntityType mobType) {
        File file = new File(CustomMobs.getPlugin().getDataFolder() + "/Mobs/" + customMobName + ".yml");
        try {
            file.createNewFile();
        } catch (IOException exception) {
            // TODO
        }
        CustomMobConfiguration mobConfiguration = new CustomMobConfiguration(YamlConfiguration.loadConfiguration(file), file);
        mobConfiguration.setType(mobType);
        mobsConfigurations.add(mobConfiguration);
        CustomMob customMob = mobConfiguration.loadCustomMob();
        customMobs.put(customMobName, customMob);
        return customMob;
    }

    public void removeCustomMob(String customMobName) {
        mobsConfigurations.remove(customMobs.get(customMobName).getCustomMobConfiguration());
        customMobs.remove(customMobName);
    }

    public void giveCustomMob(Player receiver, String customMob, String type, int amount) {
        receiver.getInventory().addItem(getCustomMobItem(getCustomMob(customMob), type, amount));
    }

    public ItemStack getCustomMobItem(CustomMob customMob, String type, int amount) {
        if(type.equalsIgnoreCase("item")) {
            ItemStack item = customMob.getItem().clone();
            item.setAmount(amount);
            return item;
        } else if(type.equalsIgnoreCase("spawner")) {
            ItemStack spawner = customMob.getSpawnerItem().clone();
            spawner.setAmount(amount);
            return spawner;
        }
        return null;
    }

    public Set<String> getCustomMobNames() {
        return customMobs.keySet();
    }

    public CustomMob getCustomMob(String customMobName) {
        if (!customMobs.containsKey(customMobName))
            return null;

        return customMobs.get(customMobName);
    }

    public List<CustomMob> getCustomMobs() {
        return customMobs.values().stream()
                .sorted(Comparator.comparing(CustomMob::getCreationDate))
                .collect(Collectors.toList());
    }
}
