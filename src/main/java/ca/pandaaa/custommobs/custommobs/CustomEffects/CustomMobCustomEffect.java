package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public abstract class CustomMobCustomEffect {
    protected boolean enabled;
    protected CustomMobConfiguration mobConfiguration;
    protected CustomEffectType customEffectType;
    public abstract void triggerCustomEffect(Entity entity);
    public abstract ItemStack modifyStatus(CustomMob customMob);
    public abstract ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType);
    public abstract List<ItemStack> getOptionsItems();
    public abstract ItemStack getCustomEffectItem();

    protected CustomMobCustomEffect(CustomMobConfiguration mobConfiguration) {
        this.mobConfiguration = mobConfiguration;
    }

    protected ItemStack getCustomEffectItemStack(CustomMobsItem item, String customEffectName) {
        item.addLore("", "&7&o(( Left-Click to edit this CustomMob's " + customEffectName + " ))", "&7&o(( Right-Click to toggle this custom effect ))");
        if(this.enabled)
            item.addEnchantment(Enchantment.MENDING, 1, false);
        return item;
    }

    protected ItemStack getCustomEffectOptionItemStack(CustomMobsItem item, boolean hasTwoClicks) {
        if(hasTwoClicks)
            item.addLore("", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        else
            item.addLore("", "&7&o(( Click to edit this option ))");
        return item;
    }

    public CustomEffectType getCustomEffectType() {
        return customEffectType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    protected boolean getCustomEffectStatus(String className) {
        String path = "custom-effects." + className.toLowerCase();
        if(!mobConfiguration.getFileConfiguration().contains(path, true))
            return false;
        return mobConfiguration.getFileConfiguration().getBoolean(path);
    }

    protected void setCustomEffectStatus(String className) {
        String path = "custom-effects." + className.toLowerCase();
        mobConfiguration.getFileConfiguration().set(path, this.enabled);
        saveConfigurationFile();
    }

    // This method will return the default value if the value is not found.
    protected <T> T getCustomEffectOption(String configurationPath, Class<T> type, T defaultValue) {
        if (!mobConfiguration.getFileConfiguration().contains(configurationPath, true))
            return type.cast(defaultValue);

        return getCustomEffectOption(configurationPath, type);
    }

    // This method will return null if the value is not found.
    protected <T> T getCustomEffectOption(String configurationPath, Class<T> type) {
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

    protected <T> void setCustomEffectOption(String configurationPath, T value) {
        mobConfiguration.getFileConfiguration().set(configurationPath, value);
        saveConfigurationFile();
    }

    protected void saveConfigurationFile() {
        try {
            mobConfiguration.getFileConfiguration().save(mobConfiguration.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
