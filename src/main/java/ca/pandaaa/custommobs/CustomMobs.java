package ca.pandaaa.custommobs;

import ca.pandaaa.custommobs.commands.Commands;
import ca.pandaaa.custommobs.commands.TabCompletion;
import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMobEvents;
import ca.pandaaa.custommobs.custommobs.CustomMobsManager;
import ca.pandaaa.custommobs.guis.GUIManager;
import ca.pandaaa.custommobs.guis.MainCustomMobsGUI;
import ca.pandaaa.custommobs.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class CustomMobs extends JavaPlugin {
    private File mobFolder;
    private final List<CustomMobConfiguration> mobConfigurations = new ArrayList<>();
    private static CustomMobs plugin;
    private ConfigurationManager configManager;
    private CustomMobsManager customMobsManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        plugin = this;
        int pluginId = 21648;
        Metrics metrics = new Metrics(this, pluginId);

        saveDefaultConfigurations();
        loadAllMobsConfigurations();

        configManager = new ConfigurationManager(getConfig());
        customMobsManager = new CustomMobsManager(configManager, mobConfigurations);
        customMobsManager.loadCustomMobs();
        guiManager = new GUIManager(mobConfigurations);

        getCommandsAndListeners();

        this.sendStartedMessage();
        this.sendTestingMessage();
    }

    public static CustomMobs getPlugin() {
        return plugin;
    }

    public CustomMobsManager getCustomMobsManager() {
        return customMobsManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public void reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        loadAllMobsConfigurations();

        configManager = new ConfigurationManager(getConfig());
        customMobsManager = new CustomMobsManager(configManager, mobConfigurations);
        customMobsManager.loadCustomMobs();
        guiManager = new GUIManager(mobConfigurations);

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

    private void sendTestingMessage() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + getDescription().getName() + " : This version of the plugin is meant for testing / may have bugs."));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease try to update to the next fully working version as soon as possible."));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease contact Pandaaa on discord if you believe this is an error."));
    }

    private void saveDefaultConfigurations() {
        this.saveDefaultConfig();

        mobFolder = new File(getDataFolder(), "Mobs");
        if (!mobFolder.exists())
            mobFolder.mkdirs();
    }

    private void loadAllMobsConfigurations() {
        for(File mobFile : Objects.requireNonNull(mobFolder.listFiles())) {
            mobConfigurations.add(new CustomMobConfiguration(mobFile.getName(), YamlConfiguration.loadConfiguration(mobFile)));
        }
    }

    private void getCommandsAndListeners() {
        PluginCommand command = getCommand("CustomMobs");
        if(command == null)
            return;

        getServer().getPluginManager().registerEvents(new CustomMobEvents(), this);
        getServer().getPluginManager().registerEvents(guiManager.getMainCustomMobsGUI(), this);
        command.setExecutor(new Commands(configManager, customMobsManager));
        command.setTabCompleter(new TabCompletion());
    }
}