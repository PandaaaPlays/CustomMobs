package ca.pandaaa.custommobs.configurations;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.*;
import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomMobCustomEffect;
import ca.pandaaa.custommobs.custommobs.Messages.SpawnDeathMessage;
import ca.pandaaa.custommobs.custommobs.Options.CustomMobOption;
import ca.pandaaa.custommobs.custommobs.Sound;
import ca.pandaaa.custommobs.guis.EditCustomMobs.TypesGUI;
import ca.pandaaa.custommobs.utils.Utils;
import com.google.common.reflect.ClassPath;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class CustomMobConfiguration {
    private final String fileName;
    private final FileConfiguration mobConfiguration;
    private final File mobFile;

    public CustomMobConfiguration(FileConfiguration mobConfiguration, File mobFile) {
        this.fileName = mobFile.getName();
        this.mobConfiguration = mobConfiguration;
        this.mobFile = mobFile;
    }

    public FileConfiguration getFileConfiguration() {
        return mobConfiguration;
    }

    public File getFile() {
        return mobFile;
    }

    public String getFileName() {
        return fileName;
    }

    public CustomMob loadCustomMob() {
        if(isDeleted()) {
            LocalDate deleteDate = getDeleted();
            if(!deleteDate.isAfter(LocalDate.now()))
                mobFile.delete();
            else {
                CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cThe CustomMob '" + fileName.replaceAll(".yml", "") + "' will be deleted permanently on " + deleteDate + "."));
                CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cRemove the 'delete-on' in the config of the CustomMob if this is a mistake."));
            }
            return null;
        }

        EntityType type = this.getType();
        if(type == null
                || type.getEntityClass() == null
                || type == EntityType.PLAYER
                || fileName.contains(" ")
                || !(LivingEntity.class.isAssignableFrom(type.getEntityClass())))
            return null;

        LocalDateTime dateTime;
        try {
            dateTime = Files.readAttributes(mobFile.toPath(), BasicFileAttributes.class).creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            dateTime = LocalDateTime.now();
        }

        CustomMob customMob = new CustomMob(dateTime, type, this);

        setCustomMobOptions(customMob, type);

        return customMob;
    }

    public void setCustomMobOptions(CustomMob customMob, EntityType type) {
        try {
            ClassPath path = ClassPath.from(this.getClass().getClassLoader());
            List<ClassPath.ClassInfo> classInfos = new ArrayList<>(path.getTopLevelClassesRecursive("ca.pandaaa.custommobs.custommobs.Options"));

            // Sort so that Special comes first
            classInfos.sort((info1, info2) -> {
                if (info1.getName().endsWith("Special")) return -1;
                if (info2.getName().endsWith("Special")) return 1;
                return 0;
            });

            for (ClassPath.ClassInfo info : classInfos) {
                Class clazz = Class.forName(info.getName(), true, this.getClass().getClassLoader());
                if (Modifier.isAbstract(clazz.getModifiers())) // Skip abstract class
                    continue;
                if((boolean) clazz.getMethod("isApplicable", EntityType.class).invoke(null, type)) {
                    customMob.addCustomMobType((CustomMobOption) clazz.getDeclaredConstructor(CustomMobConfiguration.class).newInstance(this));
                }
            }
        } catch (IOException | ClassNotFoundException | InvocationTargetException
                | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[!] Something went wrong while loading a CustomMob's option : " + exception));
            if (exception instanceof InvocationTargetException) {
                Throwable cause = ((InvocationTargetException) exception).getCause();
                CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.RED + "Cause: " + cause);
            }
        }
    }

    public void setCustomMobCustomEffects(CustomMob customMob) {
        try {
            ClassPath path = ClassPath.from(this.getClass().getClassLoader());
            List<ClassPath.ClassInfo> classInfos = new ArrayList<>(path.getTopLevelClassesRecursive("ca.pandaaa.custommobs.custommobs.CustomEffects"));

            for (ClassPath.ClassInfo info : classInfos) {
                Class clazz = Class.forName(info.getName(), true, this.getClass().getClassLoader());
                if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isEnum()) // Skip abstract class and enum
                    continue;
                customMob.addCustomMobCustomEffect((CustomMobCustomEffect) clazz.getDeclaredConstructor(CustomMobConfiguration.class).newInstance(this));
            }
        } catch (IOException | ClassNotFoundException | InvocationTargetException
                 | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[!] Something went wrong while loading a CustomMob's custom effect : " + exception));
            if (exception instanceof InvocationTargetException) {
                Throwable cause = ((InvocationTargetException) exception).getCause();
                CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.RED + "Cause: " + cause);
            }
        }
    }

    public void resetType(EntityType type) {
        try {
            ClassPath path = ClassPath.from(this.getClass().getClassLoader());
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive("ca.pandaaa.custommobs.custommobs.Options")) {
                Class clazz = Class.forName(info.getName(), true, this.getClass().getClassLoader());
                if (Modifier.isAbstract(clazz.getModifiers()) || clazz.getSimpleName().contains("Special")) // Skip abstract class and Default (not applicable)
                    continue;
                if((boolean) clazz.getMethod("isApplicable", EntityType.class).invoke(null, type)) {
                    CustomMobOption instance = (CustomMobOption) clazz.getDeclaredConstructor(CustomMobConfiguration.class).newInstance(this);
                    instance.resetOptions();
                }
            }
        } catch (IOException | ClassNotFoundException | InvocationTargetException
                 | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[!] Something went wrong while reseting a CustomMob's option."));
        }
    }

    public static final String ITEM = "item";
    public static final String SPAWNER_ITEM = "spawner-item";

    public ItemStack getItem(String configurationPath) {
        ItemStack item = getItemStack(configurationPath);
        if(item == null) {
            item = new ItemStack(configurationPath.equalsIgnoreCase(ITEM) ? TypesGUI.getSpawnEggMaterial(getType()) : Material.SPAWNER);
        }

        if(item.getItemMeta().getDisplayName().isEmpty()) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(Utils.applyFormat(getName()) + (configurationPath.equalsIgnoreCase(ITEM) ? " Spawn Egg" : " Spawner"));
                item.setItemMeta(itemMeta);
            }
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName");
            itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, getFileName().replace(".yml", ""));
            NamespacedKey keyType = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.ItemType");
            itemMeta.getPersistentDataContainer().set(keyType, PersistentDataType.STRING, configurationPath);

            item.setItemMeta(itemMeta);
        }
        return item;
    }

    public static final String EQUIPMENT_MAIN_HAND = "equipment.main-hand";
    public static final String EQUIPMENT_OFF_HAND = "equipment.off-hand";
    public static final String EQUIPMENT_HELMET = "equipment.helmet";
    public static final String EQUIPMENT_CHESTPLATE = "equipment.chestplate";
    public static final String EQUIPMENT_LEGGINGS = "equipment.leggings";
    public static final String EQUIPMENT_BOOTS = "equipment.boots";
    public Equipment getEquipment() {
        return new Equipment(
                this,
                getItemStack(EQUIPMENT_MAIN_HAND),
                getItemStack(EQUIPMENT_OFF_HAND),
                getItemStack(EQUIPMENT_HELMET),
                getItemStack(EQUIPMENT_CHESTPLATE),
                getItemStack(EQUIPMENT_LEGGINGS),
                getItemStack(EQUIPMENT_BOOTS));
    }

    public static final String DROPS = "drops";
    public List<Drop> getDrops() {
        if(!mobConfiguration.contains(DROPS, true))
            return new ArrayList<>();
        return (List<Drop>) mobConfiguration.getList(DROPS);
    }

    public void setDrops(List<Drop> drops) {
        mobConfiguration.set(DROPS, drops);
        saveConfigurationFile();
    }

    public static final String SPAWNER = "spawner";
    public Spawner getSpawner() {
        if(!mobConfiguration.contains(SPAWNER, true))
            return new Spawner(4, 6, 4, 0, 200, 799, 16, true);
        return (Spawner) mobConfiguration.get(SPAWNER);
    }

    public void setSpawner(Spawner spawner) {
        mobConfiguration.set(SPAWNER, spawner);
        saveConfigurationFile();
    }

    public static final String POTIONS = "potions";
    public List<PotionEffect> getPotionEffects() {
        if(!mobConfiguration.contains(POTIONS, true))
            return new ArrayList<>();
        return (List<PotionEffect>) mobConfiguration.getList(POTIONS);
    }

    public void setPotionEffects(List<PotionEffect> potionEffects) {
        mobConfiguration.set(POTIONS, potionEffects);
        saveConfigurationFile();
    }

    public static final String SOUNDS = "sounds";
    public List<Sound> getSounds() {
        if(!mobConfiguration.contains(SOUNDS, true))
            return new ArrayList<>();
        return (List<Sound>) mobConfiguration.getList(SOUNDS);
    }

    public void setSounds(List<Sound> sounds) {
        mobConfiguration.set(SOUNDS, sounds);
        saveConfigurationFile();
    }

    public static final String MESSAGES = "messages";
    public List<SpawnDeathMessage> getMessages() {
        if(!mobConfiguration.contains(MESSAGES, true))
            return new ArrayList<>();
        return (List<SpawnDeathMessage>) mobConfiguration.getList(MESSAGES);
    }

    public void setMessages(List<SpawnDeathMessage> messages) {
        mobConfiguration.set(MESSAGES, messages);
        saveConfigurationFile();
    }

    private static final String TYPE = "mob.type";
    private EntityType getType() {
        try {
            return EntityType.valueOf(mobConfiguration.getString(TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setType(EntityType entityType) {
        mobConfiguration.set(TYPE, entityType.name());
        saveConfigurationFile();
    }

    private static final String NAME = "mob.name";
    public String getName() {
        if(!mobConfiguration.contains(NAME, true))
            return getFileName().replace(".yml","");

        return mobConfiguration.getString(NAME);
    }

    public void setName(String name) {
        mobConfiguration.set(NAME, name);
        saveConfigurationFile();
    }

    private static final String CUSTOM_EFFECTS_COOLDOWN_DURATION = "custom-effects.cooldown";
    public int getCustomEffectsCooldownDuration() {
        if(!mobConfiguration.contains(CUSTOM_EFFECTS_COOLDOWN_DURATION, true))
            return 5;

        return mobConfiguration.getInt(CUSTOM_EFFECTS_COOLDOWN_DURATION);
    }

    public void setCustomEffectsCooldownDuration(int duration) {
        mobConfiguration.set(CUSTOM_EFFECTS_COOLDOWN_DURATION, duration);
        saveConfigurationFile();
    }

    public void setItemStack(String configurationPath, ItemStack itemStack) {
        mobConfiguration.set(configurationPath, itemStack);
        saveConfigurationFile();
    }

    public ItemStack getItemStack(String configurationPath) {
        try {
            ItemStack item = mobConfiguration.getItemStack(configurationPath);
            if(item != null && item.getItemMeta() != null) {
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(Utils.applyFormat(itemMeta.getDisplayName()));
                itemMeta.setLore(itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore().stream().map(Utils::applyFormat).toList());
                item.setItemMeta(itemMeta);
            }
            return item;
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String DELETED = "delete-on";

    public boolean isDeleted() {
        if(!mobConfiguration.contains(DELETED, true))
            return false;

        return true;
    }

    public LocalDate getDeleted() {
        if(!mobConfiguration.contains(DELETED, true))
            return null;

        return LocalDate.parse(mobConfiguration.getString(DELETED));
    }

    public void setDeleted(LocalDate date) {
        mobConfiguration.set(DELETED, date.toString());
        saveConfigurationFile();
    }

    private void saveConfigurationFile() {
        try {
            mobConfiguration.save(mobFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
