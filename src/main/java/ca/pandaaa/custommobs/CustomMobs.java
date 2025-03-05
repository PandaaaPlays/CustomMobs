package ca.pandaaa.custommobs;

import ca.pandaaa.custommobs.commands.Commands;
import ca.pandaaa.custommobs.commands.TabCompletion;
import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.*;
import ca.pandaaa.custommobs.custommobs.Events.Events;
import ca.pandaaa.custommobs.custommobs.Messages.DropMessage;
import ca.pandaaa.custommobs.custommobs.Messages.SpawnDeathMessage;
import ca.pandaaa.custommobs.utils.DamageRange;
import ca.pandaaa.custommobs.utils.Metrics;
import ca.pandaaa.custommobs.utils.SoundEnum;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class CustomMobs extends JavaPlugin {
    private Metrics metrics;
    private File mobFolder;
    private List<CustomMobConfiguration> mobConfigurations = new ArrayList<>();
    private static CustomMobs plugin;
    private ConfigurationManager configManager;
    private Manager customMobsManager;

    public API getMobAPI(String customMobName) {
        if (!customMobsManager.getCustomMobNames().contains(customMobName.toLowerCase())) {
            return null;
        }
        return new API(customMobsManager.getCustomMob(customMobName.toLowerCase()));
    }

    @Override
    public void onEnable() {
        // TODO Multi version support.
        plugin = this;

        this.sendStartedMessage();

        ConfigurationSerialization.registerClass(DropMessage.class, "ca.pandaaa.custommobs.custommobs.Messages.DropMessage");
        ConfigurationSerialization.registerClass(SpawnDeathMessage.class, "ca.pandaaa.custommobs.custommobs.Messages.SpawnDeathMessage");
        ConfigurationSerialization.registerClass(Drop.class, "ca.pandaaa.custommobs.custommobs.Drop");
        ConfigurationSerialization.registerClass(Sound.class, "ca.pandaaa.custommobs.custommobs.Sound");
        ConfigurationSerialization.registerClass(Spawner.class, "ca.pandaaa.custommobs.custommobs.Spawner");
        ConfigurationSerialization.registerClass(DamageRange.class, "ca.pandaaa.custommobs.utils.DamageRange");
        ConfigurationSerialization.registerClass(PotionEffect.class, "ca.pandaaa.custommobs.custommobs.PotionEffect");

        // Initialize SoundEnum for faster timings on click
        try {
            Class.forName("ca.pandaaa.custommobs.utils.SoundEnum");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        saveDefaultConfigurations();
        loadAllMobsConfigurations();

        configManager = new ConfigurationManager(getConfig());
        customMobsManager = new Manager(configManager, mobConfigurations);
        customMobsManager.loadCustomMobs();

        getCommandsAndListeners();

        checkSoundEnum();

        Bukkit.getScheduler().runTask(this, () -> {
            metrics = new Metrics(this, 21648);
            this.addMetrics(metrics);
        });
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

    private void addMetrics(Metrics metrics) {
        metrics.addCustomChart(new Metrics.AdvancedPie("custommobs_entity_types", () -> {
            Map<String, Integer> entityTypeAmount = new HashMap<>();
            for (CustomMob customMob : customMobsManager.getCustomMobs()) {
                String entityType = Utils.getSentenceCase(customMob.getType().getEntityClass().getSimpleName());
                if (!entityTypeAmount.containsKey(entityType))
                    entityTypeAmount.put(entityType, 1);
                else
                    entityTypeAmount.put(entityType, entityTypeAmount.get(entityType) + 1);
            }
            return entityTypeAmount;
        }));
    }

    private void checkSoundEnum() {
        List<String> soundsName = SoundEnum.getSoundsName();
        List<org.bukkit.Sound> sounds = new ArrayList<>();
        List<String> newSounds = new ArrayList<>();

        if (Bukkit.getBukkitVersion().startsWith("1.21.1") || Bukkit.getBukkitVersion().startsWith("1.21.2")) {
            return;
        } else {
            Registry.SOUNDS.iterator().forEachRemaining(sounds::add);
        }

        Collections.sort(sounds);
        Collections.sort(soundsName);

        int i = 0, j = 0;

        while (j < sounds.size()) {
            String registrySound = sounds.get(j).toString();

            if (i >= soundsName.size() || registrySound.compareToIgnoreCase(soundsName.get(i)) < 0) {
                newSounds.add(registrySound);
                j++;
            }
            // If registrySound matches the current soundName
            else if (registrySound.equalsIgnoreCase(soundsName.get(i))) {
                i++;
                j++;
            }
            else
                i++;
        }

        if(!newSounds.isEmpty()) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',  "&c[!] SoundEnum is missing some Sound values :"));
            for(String value : newSounds) {
                getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c - " + value));
            }
        }
    }
}