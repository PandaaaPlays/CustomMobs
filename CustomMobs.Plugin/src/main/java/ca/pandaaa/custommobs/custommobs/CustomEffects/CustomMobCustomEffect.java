package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public abstract class CustomMobCustomEffect {
    protected boolean enabled;
    protected boolean message;
    protected CustomMobConfiguration mobConfiguration;
    protected CustomEffectType customEffectType;
    protected double messageRadius;
    public abstract void triggerCustomEffect(Entity entity);
    public abstract ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType);
    public abstract List<ItemStack> getOptionsItems();
    public abstract ItemStack getCustomEffectItem();

    protected CustomMobCustomEffect(CustomMobConfiguration mobConfiguration, CustomEffectType customEffectType) {
        this.mobConfiguration = mobConfiguration;
        this.customEffectType = customEffectType;
        this.enabled = getCustomEffectStatus(this.getClass().getSimpleName());
        this.message = getCustomEffectOption("custom-effects." + this.getClass().getSimpleName().toLowerCase() + ".message", Boolean.class, true);
        if(this.customEffectType != CustomEffectType.ON_IMPACT)
            this.messageRadius = getCustomEffectOption("custom-effects." + this.getClass().getSimpleName().toLowerCase() + ".message-radius", Double.class, 32D);
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

    public ItemStack modifyStatus() {
        setCustomEffectStatus(this.getClass().getSimpleName());
        return getCustomEffectItem();
    }

    public double getMessageRadius() {
        return messageRadius;
    }

    public void tryBroadcastCustomEffectMessage() {
        if(!this.message)
            return;

        String message = CustomMobs.getPlugin().getCustomMobsManager().getConfigManager().getCustomEffectMessage(this.getClass().getSimpleName());
        if(message != null) {
            Bukkit.broadcastMessage(Utils.applyFormat(message.replaceAll("%name%", mobConfiguration.getName())));
        }
    }

    public void trySendCustomEffectMessage(Player player) {
        if(!this.message)
            return;

        String message = CustomMobs.getPlugin().getCustomMobsManager().getConfigManager().getCustomEffectMessage(this.getClass().getSimpleName());
        if(message != null) {
            player.sendMessage(Utils.applyFormat(message.replaceAll("%name%", mobConfiguration.getName())));
        }
    }

    protected ItemStack handleMessageOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        if (option.equalsIgnoreCase("message")) {
            if(clickType.isRightClick() && customEffectType != CustomEffectType.ON_IMPACT) {
                new DoubleGUI("Message radius", false, 0, 150, (value) -> {
                    this.messageRadius = value;
                    setCustomEffectOption("custom-effects." + this.getClass().getSimpleName().toLowerCase() + ".message-radius", this.messageRadius);
                    new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                }).setMinusPrettyValue("Everyone").openInventory(clicker, messageRadius);
            } else {
                this.message = !this.message;
                setCustomEffectOption("custom-effects." + this.getClass().getSimpleName().toLowerCase() + ".message", this.message);
            }
            return getMessageItem().getItem();
        }
        return null;
    }

    protected ItemStack getNoLeftClickCustomEffectItemStack(CustomMobsItem item) {
        item.addLore("", "&7&o(( Right-Click to toggle this custom effect ))");
        if(this.enabled)
            item.addEnchantment(Enchantment.MENDING, 1, false);
        return item.getItem();
    }

    protected ItemStack getCustomEffectItemStack(CustomMobsItem item, String customEffectName) {
        item.addLore("", "&7&o(( Left-Click to edit this CustomMob's " + customEffectName + " ))", "&7&o(( Right-Click to toggle this custom effect ))");
        if(this.enabled)
            item.addEnchantment(Enchantment.MENDING, 1, false);
        return item.getItem();
    }

    protected ItemStack getCustomEffectOptionItemStack(CustomMobsItem item, boolean hasTwoClicks) {
        if(hasTwoClicks)
            item.addLore("", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        else
            item.addLore("", "&7&o(( Click to edit this option ))");
        return item.getItem();
    }

    protected CustomMobsItem getMessageItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPRUCE_HANGING_SIGN);
        item.setName("&6&lEffect message");
        String messageStatus = this.message ? "&a&lOn" : "&c&lOff";
        item.addLore("&eDisplay message: &f" + messageStatus);
        if(customEffectType != CustomEffectType.ON_IMPACT && customEffectType != CustomEffectType.ON_DAMAGE_ON_PLAYER) {
            item.addLore("&bMessage radius: &f" + (messageRadius > 0 ? messageRadius : "Everyone"));
            item.addLore("", "&7&o(( Left-Click to edit this option ))", "&7&o(( Right-Click to change the radius of the message ))");
        } else {
            item.addLore("", "&7&o(( Click to edit this option ))");
        }
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".Message");
        return item;
    }

    protected boolean getCustomEffectStatus(String className) {
        String path = "custom-effects." + className.toLowerCase() + ".enabled";
        if(!mobConfiguration.getFileConfiguration().contains(path, true))
            return false;
        return mobConfiguration.getFileConfiguration().getBoolean(path);
    }

    protected void setCustomEffectStatus(String className) {
        String path = "custom-effects." + className.toLowerCase() + ".enabled";
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
            if (type == CustomMob.class)
                return type.cast(CustomMobs.getPlugin().getCustomMobsManager().getCustomMob(mobConfiguration.getFileConfiguration().getString(configurationPath)));
            if (type == PotionEffectType.class)
                return type.cast(Registry.EFFECT.get(NamespacedKey.minecraft(mobConfiguration.getFileConfiguration().getString(configurationPath))));
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
