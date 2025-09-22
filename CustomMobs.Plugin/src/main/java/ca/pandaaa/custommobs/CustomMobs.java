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
import com.google.common.reflect.ClassPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
        if (!mobFolder.exists()) {
            mobFolder.mkdirs();
            saveResource("Mobs/angrycow.yml", false);
            saveResource("Mobs/fastskeleton.yml", false);
            saveResource("Mobs/megazombie.yml", false);
            saveResource("Mobs/zombie.yml", false);
        }

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
        HandlerList.unregisterAll(this);
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
            for (CustomMob customMob : customMobsManager.getMetricsCustomMobs()) {
                String entityType = Utils.getSentenceCase(customMob.getType().getEntityClass().getSimpleName());
                if (!entityTypeAmount.containsKey(entityType))
                    entityTypeAmount.put(entityType, 1);
                else
                    entityTypeAmount.put(entityType, entityTypeAmount.get(entityType) + 1);
            }
            return entityTypeAmount;
        }));

        // Get the CustomMobs (not metrics one) because we also want the 4 defaults in the count.
        metrics.addCustomChart(new Metrics.SingleLineChart("custommobs_amount", () -> customMobsManager.getCustomMobs().size()));
    }

    private void checkSoundEnum() {
        List<String> soundKeys = SoundEnum.getSoundKeys();
        List<org.bukkit.Sound> sounds = new ArrayList<>();
        List<String> newSounds = new ArrayList<>();

        if (Utils.isVersionBeforeOrEqual("1.21.8")) {
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

    public void generateEntityTypesJson() {
        Map<String, List<String>> entityTypeOptions = new HashMap<>();

        for (EntityType type : EntityType.values()) {
            if (type != null && type.isAlive() && type != EntityType.PLAYER && type != EntityType.ARMOR_STAND) {
                List<Class<?>> optionClasses = new ArrayList<>();
                try {
                    ClassPath path = ClassPath.from(this.getClassLoader());
                    for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive("ca.pandaaa.custommobs.custommobs.Options")) {
                        Class clazz = Class.forName(info.getName(), true, this.getClass().getClassLoader());
                        if (!Modifier.isAbstract(clazz.getModifiers())) {
                            try {
                                if((boolean) clazz.getMethod("isApplicable", EntityType.class).invoke(null, type)) {
                                    optionClasses.add(clazz);
                                }
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
                        }
                    }
                    entityTypeOptions.put(type.name(), optionClasses.stream().map(Class::getSimpleName).toList());
                } catch (IOException | ClassNotFoundException e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CustomMobs] Failed to scan option classes.");
                    e.printStackTrace();
                    return;
                }
            }
        }

        // Write to JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonFile = new File(getDataFolder(), "entity-types.json");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile))) {
            writer.write(gson.toJson(entityTypeOptions));
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CustomMobs] entity-types.json successfully generated.");
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CustomMobs] Failed to write entity-types.json.");
            e.printStackTrace();
        }
    }
}