package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

// TODO Add Jeb sheep

// Children of CustomMobOption must implement the isApplicable method for the plugin to work properly.
public abstract class CustomMobOption {
    protected CustomMobConfiguration mobConfiguration;

    protected CustomMobOption(CustomMobConfiguration mobConfiguration) {
        this.mobConfiguration = mobConfiguration;
    }

    public abstract void applyOptions(Entity customMobs);
    public abstract void resetOptions();
    public abstract List<ItemStack> getOptionItems();
    public abstract ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType);

    protected ItemStack getOptionItemStack(CustomMobsItem item, boolean hasTwoClicks, boolean cycle) {
        if (hasTwoClicks && cycle)
            item.addLore("", "&7&o(( Left-Click to cycle this option ))", "&7&o(( Right-Click to reset this option ))");
        else if (hasTwoClicks)
            item.addLore("", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        else if (cycle)
            item.addLore("", "&7&o(( Click to cycle this option ))");
        else
            item.addLore("", "&7&o(( Click to edit this option ))");
        return item.getItem();
    }

    protected void saveConfigurationFile() {
        try {
            mobConfiguration.getFileConfiguration().save(mobConfiguration.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This method will return the default value if the value is not found.
    protected <T> T getOption(String configurationPath, Class<T> type, T defaultValue) {
        if (!mobConfiguration.getFileConfiguration().contains(configurationPath, true))
            return type.cast(defaultValue);

        return getOption(configurationPath, type);
    }

    // This method will return null if the value is not found.
    protected <T> T getOption(String configurationPath, Class<T> type) {
        if (!mobConfiguration.getFileConfiguration().contains(configurationPath, true))
            return null;

        try {
            if (type.isEnum())
                return (T) Enum.valueOf((Class<? extends Enum>) type, mobConfiguration.getFileConfiguration().getString(configurationPath));
            if (type == UUID.class)
                return type.cast(UUID.fromString(mobConfiguration.getFileConfiguration().getString(configurationPath)));
            if (type == Boolean.class)
                return type.cast(mobConfiguration.getFileConfiguration().getBoolean(configurationPath));
            if (type == String.class)
                return type.cast(mobConfiguration.getFileConfiguration().getString(configurationPath));
            if (type == Integer.class)
                return type.cast(mobConfiguration.getFileConfiguration().getInt(configurationPath));
            if (type == Double.class)
                return type.cast(mobConfiguration.getFileConfiguration().getDouble(configurationPath));
            return type.cast(mobConfiguration.getFileConfiguration().get(configurationPath));
        } catch (Exception exception) {
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[!] The type of the option " + configurationPath + " is not supported."));
            return null;
        }
    }

    protected <T extends Keyed> T getOption(String configurationPath, Registry<T> registry) {
        if (!mobConfiguration.getFileConfiguration().contains(configurationPath, true))
            return null;

        try {
            String keyValue = mobConfiguration.getFileConfiguration().getString(configurationPath);
            NamespacedKey key = NamespacedKey.minecraft(keyValue.toLowerCase());
            T value = registry.get(key);

            if (value == null)
                CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "&c[!] Invalid registry key: " + keyValue));

            return value;
        } catch (Exception exception) {
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&c[!] Failed to load registry option for: " + configurationPath));
            return null;
        }
    }

    protected <T> void setOption(String configurationPath, T value) {
        mobConfiguration.getFileConfiguration().set(configurationPath, value);
        saveConfigurationFile();
    }
}
