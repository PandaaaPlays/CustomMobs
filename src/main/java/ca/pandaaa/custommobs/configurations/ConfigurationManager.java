package ca.pandaaa.custommobs.configurations;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class ConfigurationManager {
    private final FileConfiguration configuration;

    public ConfigurationManager(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean getSilkSpawner() {
        return configuration.getBoolean("silk-spawner");
    }

    public void setSilkSpawner(boolean bool){
        configuration.set("silk-spawner", bool);
        CustomMobs.getPlugin().saveConfig();
    }

    public String getUnknownCommandMessage(String complement) {
        return Utils.applyFormat(configuration.getString("unknown-command").replaceAll("%complement%", complement));
    }

    public String getConsoleUnknownCommandMessage(String complement) {
        return Utils.applyFormat(configuration.getString("console-unknown-command").replaceAll("%complement%", complement));
    }

    public String getIncorrectCoordinatesMessage() {
        return Utils.applyFormat(configuration.getString("incorrect-coordinates"));
    }

    public String getSenderGiveMessage(String receiver, String customMob, String type, int amount) {
        return Utils.applyFormat(configuration.getString("give-sender")
                .replaceAll("%amount%", String.valueOf(amount))
                .replaceAll("%custommob%", customMob)
                .replaceAll("%type%", type)
                .replaceAll("%receiver%", receiver));
    }

    public String getReceiverGiveMessage(String sender, String customMob, String type, int amount) {
        return Utils.applyFormat(configuration.getString("give-receiver")
                .replaceAll("%amount%", String.valueOf(amount))
                .replaceAll("%custommob%", customMob)
                .replaceAll("%type%", type)
                .replaceAll("%sender%", sender));
    }

    public String getNoPermissionMessage() {
        return Utils.applyFormat(configuration.getString("no-permission"));
    }

    public String getMessagesChangeMessage(String complement) {
        return Utils.applyFormat(configuration.getString("messages-change").replaceAll("%complement%", complement));
    }

    public String getPluginReloadMessage() {
        return Utils.applyFormat(configuration.getString("plugin-reload"));
    }

    public String getSilkSpawnerMessage(String complement) {
        return Utils.applyFormat(configuration.getString("silk-spawner-command").replaceAll("%complement%", complement));
    }

}