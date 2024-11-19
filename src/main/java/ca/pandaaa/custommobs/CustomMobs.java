package ca.pandaaa.custommobs;

import ca.pandaaa.custommobs.commands.Commands;
import ca.pandaaa.custommobs.commands.TabCompletion;
import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.custommobs.Events;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.custommobs.Spawner;
import ca.pandaaa.custommobs.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class CustomMobs extends JavaPlugin {
    private File mobFolder;
    private List<CustomMobConfiguration> mobConfigurations = new ArrayList<>();
    private static CustomMobs plugin;
    private ConfigurationManager configManager;
    private Manager customMobsManager;

    @Override
    public void onEnable() {
        plugin = this;
        int pluginId = 21648;
        Metrics metrics = new Metrics(this, pluginId);

        this.sendStartedMessage();

        // DropItem serialization
        ConfigurationSerialization.registerClass(Drop.class, "ca.pandaaa.custommobs.custommobs.Drop");
        ConfigurationSerialization.registerClass(Spawner.class, "ca.pandaaa.custommobs.custommobs.Spawner");

        saveDefaultConfigurations();
        loadAllMobsConfigurations();

        configManager = new ConfigurationManager(getConfig());
        customMobsManager = new Manager(configManager, mobConfigurations);
        customMobsManager.loadCustomMobs();

        getCommandsAndListeners();
    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getOpenInventory().getTitle().contains("CustomMobs")) {
                player.closeInventory();
            }
        }
    }

    public static CustomMobs getPlugin() {
        return plugin;
    }

    public Manager getCustomMobsManager() {
        return customMobsManager;
    }

    public void reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        loadAllMobsConfigurations();

        configManager = new ConfigurationManager(getConfig());
        customMobsManager = new Manager(configManager, mobConfigurations);
        customMobsManager.loadCustomMobs();

        getCommandsAndListeners();

        sender.sendMessage(configManager.getPluginReloadMessage());
    }

    private void sendStartedMessage() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "   &6_____  &e_____"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &6|     |&e|     |     &6Custom&eMobs"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &6|   --|&e| | | |    &7Version " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &6|_____|&e|_|_|_|     &7by &8Pa&7nd&5aaa"));
        getServer().getConsoleSender().sendMessage("");
    }

    private void saveDefaultConfigurations() {
        this.saveDefaultConfig();

        mobFolder = new File(getDataFolder(), "Mobs");
        if (!mobFolder.exists())
            mobFolder.mkdirs();
    }

    private void loadAllMobsConfigurations() {
        mobConfigurations = new ArrayList<>();
        for(File mobFile : Objects.requireNonNull(mobFolder.listFiles())) {
            mobConfigurations.add(new CustomMobConfiguration(YamlConfiguration.loadConfiguration(mobFile), mobFile));
        }
    }

    private void getCommandsAndListeners() {
        PluginCommand command = getCommand("CustomMobs");
        if(command == null)
            return;

        getServer().getPluginManager().registerEvents(new Events(), this);
        command.setExecutor(new Commands(configManager, customMobsManager));
        command.setTabCompleter(new TabCompletion());
    }
}