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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        configManager = new ConfigurationManager(getConfig(), getCustomEffectConfiguration());
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

        configManager = new ConfigurationManager(getConfig(), getCustomEffectConfiguration());
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

        File customEffectsMessagesFile = new File(getDataFolder(), "custom-effects-messages.yml");
        if (!customEffectsMessagesFile.exists())
            saveResource("custom-effects-messages.yml", false);
    }

    private void loadAllMobsConfigurations() {
        mobConfigurations = new ArrayList<>();
        for(File mobFile : Objects.requireNonNull(mobFolder.listFiles())) {
            mobConfigurations.add(new CustomMobConfiguration(YamlConfiguration.loadConfiguration(mobFile), mobFile));
        }
    }

    private FileConfiguration getCustomEffectConfiguration() {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        File file = new File(getDataFolder(), "custom-effects-messages.yml");
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[!] An error occurred while fetching the values in custom-effects-messages.yml."));
        }
        return fileConfiguration;
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

        metrics.addCustomChart(new Metrics.SingleLineChart("custommobs_amount", () -> customMobsManager.getCustomMobs().size()));
    }

    private void checkSoundEnum() {
        List<String> soundKeys = SoundEnum.getSoundKeys();
        List<org.bukkit.Sound> sounds = new ArrayList<>();
        List<String> newSounds = new ArrayList<>();

        if (Bukkit.getBukkitVersion().startsWith("1.21.1") ||
            Bukkit.getBukkitVersion().startsWith("1.21.2") ||
            Bukkit.getBukkitVersion().startsWith("1.21.3") ||
            Bukkit.getBukkitVersion().startsWith("1.21.4") ||
            Bukkit.getBukkitVersion().startsWith("1.21.5")) {
            return;
        } else {
            Registry.SOUNDS.iterator().forEachRemaining(sounds::add);
        }

        Collections.sort(sounds);
        Collections.sort(soundKeys);

        for (org.bukkit.Sound sound : sounds) {
            boolean found = false;
            for (int j = 0; j < soundKeys.size(); j++) {
                if (sound.toString().equalsIgnoreCase(soundKeys.get(j)) || sound.toString().equalsIgnoreCase(soundKeys.get(j).replaceAll("\\.", "_"))) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                newSounds.add(sound.toString());
            }
        }

        if(!newSounds.isEmpty()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(CustomMobs.getPlugin().getDataFolder(), "MissingSounds.txt")));
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',  "&c[!] SoundEnum is missing some Sound values, please refer to the MissingSounds.txt file."));
                writer.write("MISSING SOUND(S): ");
                writer.newLine();
                for(String value : newSounds) {
                    writer.write(value);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}