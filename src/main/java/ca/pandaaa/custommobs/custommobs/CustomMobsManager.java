package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomMobsManager {
    ConfigurationManager configManager;
    private final List<CustomMobConfiguration> mobsConfigurations;
    private final Map<String, CustomMob> customMobs = new HashMap<>();

    public CustomMobsManager(ConfigurationManager configManager, List<CustomMobConfiguration> mobsConfigurations) {
        this.configManager = configManager;
        this.mobsConfigurations = mobsConfigurations;
    }

    public void loadCustomMobs() {
        for (CustomMobConfiguration mobConfiguration : mobsConfigurations) {
            CustomMob customMob = mobConfiguration.loadCustomMob();
            if(customMob != null)
                customMobs.put(mobConfiguration.getFileName().toLowerCase().replace(".yml", ""), customMob);
        }
    }

    public void giveCustomMob(Player receiver, String customMob, String type, int amount) {
        giveCustomMob(receiver, getCustomMob(customMob), type, amount);
    }

    public void giveCustomMob(Player receiver, CustomMob customMob, String type, int amount) {
        if(type.equalsIgnoreCase("item")) {
            ItemStack item = customMob.getItem().clone();
            item.setAmount(amount);
            receiver.getInventory().addItem(item);
        } else if(type.equalsIgnoreCase("spawner")) {
            ItemStack spawner = customMob.getSpawner().clone();
            spawner.setAmount(amount);
            receiver.getInventory().addItem(spawner);
        }
    }

    public Set<String> getCustomMobNames() {
        return customMobs.keySet();
    }

    public CustomMob getCustomMob(String customMobName) {
        if (!customMobs.containsKey(customMobName))
            return null;

        return customMobs.get(customMobName);
    }

    public List<CustomMobConfiguration> getCustomMobConfigurations() {
        return mobsConfigurations;
    }
}
