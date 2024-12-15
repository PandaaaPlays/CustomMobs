package ca.pandaaa.custommobs;

import ca.pandaaa.custommobs.commands.Commands;
import ca.pandaaa.custommobs.commands.TabCompletion;
import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.*;
import ca.pandaaa.custommobs.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;

public class CustomMobs extends JavaPlugin {
    private File mobFolder;
    private List<CustomMobConfiguration> mobConfigurations = new ArrayList<>();
    private static CustomMobs plugin;
    private ConfigurationManager configManager;
    private Manager customMobsManager;

    @Override
    public void onEnable() {
        plugin = this;

        /* TODO
        FAT LOG ERROR : [CustomMobs] Task #102 for CustomMobs v1.0.0 generated an exception
java.lang.NoClassDefFoundError: ca/pandaaa/custommobs/utils/Metrics$CustomChart
        at ca.pandaaa.custommobs.utils.Metrics$MetricsBase.submitData(Metrics.java:258) ~[?:?]
        at org.bukkit.craftbukkit.v1_21_R2.scheduler.CraftTask.run(CraftTask.java:82) ~[spigot-1.21.3-R0.1-SNAPSHOT.jar:4378-Spigot-e65d67a-8ef9079]
        at org.bukkit.craftbukkit.v1_21_R2.scheduler.CraftScheduler.mainThreadHeartbeat(CraftScheduler.java:415) ~[spigot-1.21.3-R0.1-SNAPSHOT.jar:4378-Spigot-e65d67a-8ef9079]
        at net.minecraft.server.MinecraftServer.c(MinecraftServer.java:1502) ~[spigot-1.21.3-R0.1-SNAPSHOT.jar:4378-Spigot-e65d67a-8ef9079]
        at net.minecraft.server.MinecraftServer.a(MinecraftServer.java:1391) ~[spigot-1.21.3-R0.1-SNAPSHOT.jar:4378-Spigot-e65d67a-8ef9079]
        at net.minecraft.server.MinecraftServer.y(MinecraftServer.java:1093) ~[spigot-1.21.3-R0.1-SNAPSHOT.jar:4378-Spigot-e65d67a-8ef9079]
        at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:329) ~[spigot-1.21.3-R0.1-SNAPSHOT.jar:4378-Spigot-e65d67a-8ef9079]
        at java.base/java.lang.Thread.run(Thread.java:1570) [?:?]
Caused by: java.lang.ClassNotFoundException: ca.pandaaa.custommobs.utils.Metrics$CustomChart
        at org.bukkit.plugin.java.PluginClassLoader.loadClass0(PluginClassLoader.java:160) ~[spigot-api-1.21.3-R0.1-SNAPSHOT.jar:?]
        at org.bukkit.plugin.java.PluginClassLoader.loadClass(PluginClassLoader.java:112) ~[spigot-api-1.21.3-R0.1-SNAPSHOT.jar:?]
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:525) ~[?:?]
        ... 8 more
         */

        // TODO Sounds Enum might miss new sounds... add thing to show on console saying its missing one

        Metrics metrics = new Metrics(this, 21648);

        this.sendStartedMessage();

        // DropItem serialization
        ConfigurationSerialization.registerClass(Drop.class, "ca.pandaaa.custommobs.custommobs.Drop");
        ConfigurationSerialization.registerClass(Sound.class, "ca.pandaaa.custommobs.custommobs.Sound");
        ConfigurationSerialization.registerClass(Spawner.class, "ca.pandaaa.custommobs.custommobs.Spawner");
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

        this.addMetrics(metrics);
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
                if (!entityTypeAmount.containsKey(customMob.getType().getClass().getSimpleName()))
                    entityTypeAmount.put(customMob.getType().getClass().getSimpleName(), 1);
                else
                    entityTypeAmount.put(customMob.getType().getClass().getSimpleName(), entityTypeAmount.get(customMob.getType().getClass().getSimpleName()) + 1);
            }
            return entityTypeAmount;
        }));
    }
}