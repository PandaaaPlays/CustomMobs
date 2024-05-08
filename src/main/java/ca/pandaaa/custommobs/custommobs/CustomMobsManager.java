package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomMobsManager {
    ConfigurationManager configManager;
    List<CustomMobConfiguration> mobsConfigurations;
    private Map<String, CustomMob> customMobs = new HashMap<>();

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

    public void giveCustomMob(CommandSender sender, Player receiver, String customMob, String type, int amount) {
        sender.sendMessage(configManager.getSenderGiveMessage(receiver.getName(), customMob, type, amount));
        String senderName;
        if(sender instanceof ConsoleCommandSender) {
            senderName = "Console";
        } else {
            senderName = sender.getName();
        }
        receiver.sendMessage(configManager.getReceiverGiveMessage(senderName, customMob, type, amount));

        receiver.getInventory().addItem(new ItemStack(Material.ACACIA_BOAT, 1));
    }

    public Set<String> getCustomMobNames() {
        return customMobs.keySet();
    }

    public CustomMob getCustomMob(String customMobName) {
        if (!customMobs.containsKey(customMobName))
            return null;

        return customMobs.get(customMobName);
    }

}
