package ca.pandaaa.custommobs.configurations;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.*;
import ca.pandaaa.custommobs.custommobs.CustomEffects.Charge;
import ca.pandaaa.custommobs.custommobs.CustomEffects.Explosion;
import ca.pandaaa.custommobs.custommobs.CustomEffects.Vanish;
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
import java.lang.reflect.Field;
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

        CustomMob customMob = new CustomMob(
                dateTime,
                type,
                fileName,
                getItem(ITEM),
                getItem(SPAWNER_ITEM),
                getEquipment(),
                getSpawner(),
                getPotionEffects(),
                getDrops(),
                getName(),
                getSounds(),
                getMessages(),
                this);

        setCustomMobConfigurations(customMob, type);

        return customMob;
    }

    public void setCustomMobConfigurations(CustomMob customMob, EntityType type) {
        try {
            ClassPath path = ClassPath.from(this.getClass().getClassLoader());
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive("ca.pandaaa.custommobs.custommobs.Options")) {
                Class clazz = Class.forName(info.getName(), true, this.getClass().getClassLoader());
                if (Modifier.isAbstract(clazz.getModifiers())) // Skip abstract class
                    continue;
                if((boolean) clazz.getMethod("isApplicable", EntityType.class).invoke(null, type)) {
                    customMob.addCustomMobType((CustomMobOption) clazz.getDeclaredConstructor(CustomMobConfiguration.class).newInstance(this));
                }
            }
        } catch (IOException | ClassNotFoundException | InvocationTargetException
                | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
            CustomMobs.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[!] Something went wrong while loading a CustomMob's options : " + exception));
        }


        /*if(ChestedHorse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    ChestedHorse(hasChest()));
        if(Chicken.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Chicken(getChickenVariant()));
        if(Cow.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Cow(getCowVariant()));
        if(Creeper.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Creeper(getExplosionCooldown(), getExplosionRadius(), isChargedCreeper()));
        if(Fox.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Fox(getFoxType()));
        if(Frog.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Frog(getFrogVariant()));
        if(Horse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Horse(getHorseColor(), getHorseStyle(), getHorseArmor()));
        if(Llama.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Llama(getLlamaColor()));
        if(Panda.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Panda(getPandaGene()));
        if(Parrot.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Parrot(getParrotVariant()));
        if(Pig.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Pig(getPigVariant()));
        if(PigZombie.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    PigZombie(getZombifiedPiglinAnger()));
        if(Rabbit.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Rabbit(getRabbitType()));
        if(Shulker.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Shulker(getShulkerColor()));
        if(Sittable.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Sittable(isSitting()));
        if(SkeletonHorse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    SkeletonHorse(isSkeletonTrap()));
        if(Slime.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Slime(getSlimeSize()));
        if(Tameable.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Tameable(getOwner(), isTamed()));
        if(Villager.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Villager(getVillagerType(), getVillagerProfession()));
        if(Vindicator.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Vindicator(isJohnnyVindicator()));
        if(Wolf.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Wolf(getCollarColor(), isAngry(), getWolfVariant(), hasWolfArmor()));
        if(Zombie.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.Options.
                    Zombie(canBreakDoors()));*/

        // Custom Effects :
        customMob.addCustomMobCustomEffect(new Charge(getChargeCustomEffect()));
        customMob.addCustomMobCustomEffect(new Vanish(getVanishCustomEffect()));
        customMob.addCustomMobCustomEffect(new Explosion(getExplosionCustomEffect(), getExplosionCustomEffectExplosionStrength()));
    }

    public void resetType(EntityType type) {
        try {
            ClassPath path = ClassPath.from(this.getClass().getClassLoader());
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive("ca.pandaaa.custommobs.custommobs.Options")) {
                Class clazz = Class.forName(info.getName(), true, this.getClass().getClassLoader());
                if (Modifier.isAbstract(clazz.getModifiers()) || clazz.getSimpleName().contains("Special")) // Skip abstract class and Special (not applicable)
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

        /*
        if (Chicken.class.isAssignableFrom(type.getEntityClass()))
            setChickenVariant(null);
        if (Cow.class.isAssignableFrom(type.getEntityClass()))
            setCowVariant(null);
        if (Creeper.class.isAssignableFrom(type.getEntityClass())) {
            setExplosionCooldown(null);
            setExplosionRadius(null);
            setChargedCreeper(null);
        }
        if(Fox.class.isAssignableFrom(type.getEntityClass()))
            setFoxType(null);
        if(Frog.class.isAssignableFrom(type.getEntityClass()))
            setFrogVariant(null);
        if(Horse.class.isAssignableFrom(type.getEntityClass())) {
            setHorseColor(null);
            setHorseStyle(null);
            setHorseArmor(null);
        }
        if(Llama.class.isAssignableFrom(type.getEntityClass()))
            setLlamaColor(null);
        if(Panda.class.isAssignableFrom(type.getEntityClass()))
            setPandaGene(null);
        if(Parrot.class.isAssignableFrom(type.getEntityClass()))
            setParrotVariant(null);
        if(Phantom.class.isAssignableFrom(type.getEntityClass()))
            setPhantomSize(null);
        if (Pig.class.isAssignableFrom(type.getEntityClass()))
            setPigVariant(null);
        if(PigZombie.class.isAssignableFrom(type.getEntityClass()))
            setZombifiedPiglinAnger(null);
        if(Rabbit.class.isAssignableFrom(type.getEntityClass()))
            setRabbitType(null);
        if(Shulker.class.isAssignableFrom(type.getEntityClass()))
            setShulkerColor(null);
        if(Sittable.class.isAssignableFrom(type.getEntityClass()))
            setSitting(null);
        if(SkeletonHorse.class.isAssignableFrom(type.getEntityClass()))
            setSkeletonTrap(null);
        if(Slime.class.isAssignableFrom(type.getEntityClass()))
            setSlimeSize(null);
        if(Steerable.class.isAssignableFrom(type.getEntityClass()))
            setHasSaddle(null);
        if(Tameable.class.isAssignableFrom(type.getEntityClass())) {
            setOwner(null);
            setTamed(null);
        }
        if(Villager.class.isAssignableFrom(type.getEntityClass())) {
            setVillagerType(null);
            setVillagerProfession(null);
        }
        if(Vindicator.class.isAssignableFrom(type.getEntityClass()))
            setJohnnyVindicator(null);
        if(Wolf.class.isAssignableFrom(type.getEntityClass())) {
            setCollarColor(null);
            setAngryWolf(null);
            setWolfVariant(null);
            setHasWolfArmor(null);
        }
        if(Zombie.class.isAssignableFrom(type.getEntityClass()))
            setCanBreakDoors(null);*/
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



    private static final String CAN_BREAK_DOORS = "mob.can-break-doors";
    private boolean canBreakDoors() {
        if(!mobConfiguration.contains(CAN_BREAK_DOORS, true))
            return false;

        return mobConfiguration.getBoolean(CAN_BREAK_DOORS);
    }

    public void setCanBreakDoors(Boolean canBreakDoors) {
        mobConfiguration.set(CAN_BREAK_DOORS, canBreakDoors);
        saveConfigurationFile();
    }


    private static final String RABBIT_TYPE = "mob.rabbit-type";
    private Rabbit.Type getRabbitType() {
        try {
            return Rabbit.Type.valueOf(mobConfiguration.getString(RABBIT_TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setRabbitType(Rabbit.Type rabbitType) {
        if (rabbitType != null)
            mobConfiguration.set(RABBIT_TYPE, rabbitType.toString());
        else
            mobConfiguration.set(RABBIT_TYPE, null);
        saveConfigurationFile();
    }

    private static final String FOX_TYPE = "mob.fox-type";
    private org.bukkit.entity.Fox.Type getFoxType() {
        try {
            return Fox.Type.valueOf(mobConfiguration.getString(FOX_TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setFoxType(Fox.Type foxType) {
        if (foxType != null)
            mobConfiguration.set(FOX_TYPE, foxType.toString());
        else
            mobConfiguration.set(FOX_TYPE, null);
        saveConfigurationFile();
    }

    private static final String VILLAGER_TYPE = "mob.villager-type";
    private Villager.Type getVillagerType() {
        try {
            return Registry.VILLAGER_TYPE.get(NamespacedKey.minecraft(mobConfiguration.getString(VILLAGER_TYPE).toLowerCase()));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setVillagerType(Villager.Type villagerType) {
        if (villagerType != null)
            mobConfiguration.set(VILLAGER_TYPE, villagerType.toString());
        else
            mobConfiguration.set(VILLAGER_TYPE, null);
        saveConfigurationFile();
    }

    private static final String HORSE_COLOR = "mob.horse-color";
    private Horse.Color getHorseColor() {
        try {
            return Horse.Color.valueOf(mobConfiguration.getString(HORSE_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setHorseColor(Horse.Color horseColor) {
        if (horseColor != null)
            mobConfiguration.set(HORSE_COLOR, horseColor.name());
        else
            mobConfiguration.set(HORSE_COLOR, null);
        saveConfigurationFile();
    }

    private static final String HORSE_STYLE = "mob.horse-style";
    private Horse.Style getHorseStyle() {
        try {
            return Horse.Style.valueOf(mobConfiguration.getString(HORSE_STYLE));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setHorseStyle(Horse.Style horseStyle) {
        if (horseStyle != null)
            mobConfiguration.set(HORSE_STYLE, horseStyle.name());
        else
            mobConfiguration.set(HORSE_STYLE, null);
        saveConfigurationFile();
    }

    private static final String HORSE_ARMOR = "mob.horse-armor";
    private Material getHorseArmor() {
        try {
            return Material.valueOf(mobConfiguration.getString(HORSE_ARMOR));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setHorseArmor(Material horseArmor) {
        if (horseArmor != null)
            mobConfiguration.set(HORSE_ARMOR, horseArmor.toString());
        else
            mobConfiguration.set(HORSE_ARMOR, null);
        saveConfigurationFile();
    }

    private static final String LLAMA_COLOR = "mob.llama-color";
    private Llama.Color getLlamaColor() {
        try {
            return Llama.Color.valueOf(mobConfiguration.getString(LLAMA_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setLlamaColor(Llama.Color llamaColor) {
        if (llamaColor != null)
            mobConfiguration.set(LLAMA_COLOR, llamaColor.name());
        else
            mobConfiguration.set(LLAMA_COLOR, null);
        saveConfigurationFile();
    }

    private static final String FROG_VARIANT = "mob.frog-variant";
    private Frog.Variant getFrogVariant() {
        try {
            return Registry.FROG_VARIANT.get(NamespacedKey.minecraft(mobConfiguration.getString(FROG_VARIANT).toLowerCase()));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setFrogVariant(Frog.Variant frogVariant) {
        if (frogVariant != null)
            mobConfiguration.set(FROG_VARIANT, frogVariant.toString());
        else
            mobConfiguration.set(FROG_VARIANT, null);
        saveConfigurationFile();
    }

    private static final String PIG_VARIANT = "mob.pig-variant";
    private Pig.Variant getPigVariant() {
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Field pigVariantField = registryClass.getDeclaredField("PIG_VARIANT");
            Object pigVariantRegistry = pigVariantField.get(null);

            if (pigVariantRegistry instanceof org.bukkit.Registry<?> registry) {
                String configKey = mobConfiguration.getString(PIG_VARIANT);
                if (configKey == null) return null;

                NamespacedKey namespacedKey = NamespacedKey.minecraft(configKey.toLowerCase());
                Object variant = registry.get(namespacedKey);

                if (variant instanceof Pig.Variant pigVariant) {
                    return pigVariant;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    public void setPigVariant(Pig.Variant pigVariant) {
        if (pigVariant != null)
            mobConfiguration.set(PIG_VARIANT, pigVariant.getKeyOrNull().getKey());
        else
            mobConfiguration.set(PIG_VARIANT, null);
        saveConfigurationFile();
    }

    /*private static final String CHICKEN_VARIANT = "mob.chicken-variant";
    private Chicken.Variant getChickenVariant() {
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Field chickenVariantField = registryClass.getDeclaredField("CHICKEN_VARIANT");
            Object chickenVariantRegistry = chickenVariantField.get(null);

            if (chickenVariantRegistry instanceof org.bukkit.Registry<?> registry) {
                String configKey = mobConfiguration.getString(CHICKEN_VARIANT);
                if (configKey == null) return null;

                NamespacedKey namespacedKey = NamespacedKey.minecraft(configKey.toLowerCase());
                Object variant = registry.get(namespacedKey);

                if (variant instanceof Chicken.Variant chickenVariant) {
                    return chickenVariant;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    public void setChickenVariant(Chicken.Variant chickenVariant) {
        if (chickenVariant != null)
            mobConfiguration.set(CHICKEN_VARIANT, chickenVariant.getKeyOrNull().getKey());
        else
            mobConfiguration.set(CHICKEN_VARIANT, null);
        saveConfigurationFile();
    }*/

    private static final String COW_VARIANT = "mob.cow-variant";
    private Cow.Variant getCowVariant() {
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Field cowVariantField = registryClass.getDeclaredField("COW_VARIANT");
            Object cowVariantRegistry = cowVariantField.get(null);

            if (cowVariantRegistry instanceof org.bukkit.Registry<?> registry) {
                String configKey = mobConfiguration.getString(COW_VARIANT);
                if (configKey == null) return null;

                NamespacedKey namespacedKey = NamespacedKey.minecraft(configKey.toLowerCase());
                Object variant = registry.get(namespacedKey);

                if (variant instanceof Cow.Variant cowVariant) {
                    return cowVariant;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    public void setCowVariant(Cow.Variant cowVariant) {
        if (cowVariant != null)
            mobConfiguration.set(COW_VARIANT, cowVariant.getKeyOrNull().getKey());
        else
            mobConfiguration.set(COW_VARIANT, null);
        saveConfigurationFile();
    }

    private static final String PARROT_VARIANT = "mob.parrot-variant";
    private Parrot.Variant getParrotVariant() {
        try {
            return Parrot.Variant.valueOf(mobConfiguration.getString(PARROT_VARIANT));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setParrotVariant(Parrot.Variant parrotVariant) {
        if (parrotVariant != null)
            mobConfiguration.set(PARROT_VARIANT, parrotVariant.toString());
        else
            mobConfiguration.set(PARROT_VARIANT, null);
        saveConfigurationFile();
    }

    private static final String WOLF_VARIANT = "mob.wolf-variant";
    private Wolf.Variant getWolfVariant() {
        try {
            return Registry.WOLF_VARIANT.get(NamespacedKey.minecraft(mobConfiguration.getString(WOLF_VARIANT).toLowerCase()));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setWolfVariant(Wolf.Variant wolfVariant) {
        if (wolfVariant != null)
            mobConfiguration.set(WOLF_VARIANT, wolfVariant.getKeyOrNull().getKey());
        else
            mobConfiguration.set(WOLF_VARIANT, null);
        saveConfigurationFile();
    }

    private static final String VILLAGER_PROFESSION = "mob.villager-profession";
    private Villager.Profession getVillagerProfession() {
        try {
            return Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(mobConfiguration.getString(VILLAGER_PROFESSION).toLowerCase()));
        } catch(Exception exception) {
            return Villager.Profession.NONE;
        }
    }

    public void setVillagerProfession(Villager.Profession villagerProfession) {
        if (villagerProfession != null)
            mobConfiguration.set(VILLAGER_PROFESSION, villagerProfession.toString());
        else
            mobConfiguration.set(VILLAGER_PROFESSION, null);
        saveConfigurationFile();
    }

    private static final String EXPLOSION_COOLDOWN = "mob.explosion-cooldown";
    private int getExplosionCooldown() {
        if(!mobConfiguration.contains(EXPLOSION_COOLDOWN, true))
            return 30;

        return mobConfiguration.getInt(EXPLOSION_COOLDOWN);
    }

    public void setExplosionCooldown(Integer explosionCooldown) {
        mobConfiguration.set(EXPLOSION_COOLDOWN, explosionCooldown);
        saveConfigurationFile();
    }

    private static final String EXPLOSION_RADIUS = "mob.explosion-radius";
    private int getExplosionRadius() {
        if(!mobConfiguration.contains(EXPLOSION_RADIUS, true))
            return 3;

        return mobConfiguration.getInt(EXPLOSION_RADIUS);
    }

    public void setExplosionRadius(Integer explosionRadius) {
        mobConfiguration.set(EXPLOSION_RADIUS, explosionRadius);
        saveConfigurationFile();
    }

    private static final String CHARGED_CREEPER = "mob.charged-creeper";
    private boolean isChargedCreeper() {
        if(!mobConfiguration.contains(CHARGED_CREEPER, true))
            return false;

        return mobConfiguration.getBoolean(CHARGED_CREEPER);
    }

    public void setChargedCreeper(Boolean chargedCreeper) {
        mobConfiguration.set(CHARGED_CREEPER, chargedCreeper);
        saveConfigurationFile();
    }

    private static final String SLIME_SIZE = "mob.slime-size";
    private Integer getSlimeSize() {
        if(!mobConfiguration.contains(SLIME_SIZE, true))
            return null;

        return mobConfiguration.getInt(SLIME_SIZE);
    }

    public void setSlimeSize(Integer slimeSize) {
        mobConfiguration.set(SLIME_SIZE, slimeSize);
        saveConfigurationFile();
    }

    private static final String ZOMBIFIED_PIGLIN_ANGER = "mob.zombified-piglin-anger";
    private int getZombifiedPiglinAnger() {
        if(!mobConfiguration.contains(ZOMBIFIED_PIGLIN_ANGER, true))
            return 0;

        return mobConfiguration.getInt(ZOMBIFIED_PIGLIN_ANGER);
    }

    public void setZombifiedPiglinAnger(Integer zombifiedPiglinAnger) {
        mobConfiguration.set(ZOMBIFIED_PIGLIN_ANGER, zombifiedPiglinAnger);
        saveConfigurationFile();
    }

    private static final String SHULKER_COLOR = "mob.shulker-color";
    private DyeColor getShulkerColor() {
        try {
            return DyeColor.valueOf(mobConfiguration.getString(SHULKER_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setShulkerColor(DyeColor shulkerColor) {
        if (shulkerColor != null)
            mobConfiguration.set(SHULKER_COLOR, shulkerColor.name());
        else
            mobConfiguration.set(SHULKER_COLOR, null);
        saveConfigurationFile();
    }

    private static final String JOHNNY_VANDICATOR = "mob.johnny-vandicator";
    private boolean isJohnnyVindicator() {
        if(!mobConfiguration.contains(JOHNNY_VANDICATOR, true))
            return false;

        return mobConfiguration.getBoolean(JOHNNY_VANDICATOR);
    }

    public void setJohnnyVindicator(Boolean johnnyVindicator) {
        mobConfiguration.set(JOHNNY_VANDICATOR, johnnyVindicator);
        saveConfigurationFile();
    }


    private static final String WOLF_ARMOR = "mob.wolf-armor";
    public boolean hasWolfArmor() {
        if(!mobConfiguration.contains(WOLF_ARMOR, true))
            return false;

        return mobConfiguration.getBoolean(WOLF_ARMOR);
    }

    public void setHasWolfArmor(Boolean hasWolfArmor) {
        mobConfiguration.set(WOLF_ARMOR, hasWolfArmor);
        saveConfigurationFile();
    }



    private static final String SITTING = "mob.sitting";
    private boolean isSitting() {
        if(!mobConfiguration.contains(SITTING, true))
            return false;

        return mobConfiguration.getBoolean(SITTING);
    }

    public void setSitting(Boolean sitting) {
        mobConfiguration.set(SITTING, sitting);
        saveConfigurationFile();
    }

    private static final String COLLAR_COLOR = "mob.collar-color";
    private DyeColor getCollarColor() {
        try {
            return DyeColor.valueOf(mobConfiguration.getString(COLLAR_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setCollarColor(DyeColor dyeColor) {
        if (dyeColor != null)
            mobConfiguration.set(COLLAR_COLOR, dyeColor.name());
        else
            mobConfiguration.set(COLLAR_COLOR, null);
        saveConfigurationFile();
    }

    private static final String OWNER = "mob.owner";
    private UUID getOwner() {
        try {
            return UUID.fromString(mobConfiguration.getString(OWNER));
        } catch (Exception exception) {
            return null;
        }
    }

    public void setOwner(UUID owner) {
        if(owner != null)
            mobConfiguration.set(OWNER, owner.toString());
        else
            mobConfiguration.set(OWNER, null);
        saveConfigurationFile();
    }



    private static final String TAMED = "mob.tamed";
    private boolean isTamed() {
        if(!mobConfiguration.contains(TAMED, true))
            return false;

        return mobConfiguration.getBoolean(TAMED);
    }

    public void setTamed(Boolean tamed) {
        mobConfiguration.set(TAMED, tamed);
        saveConfigurationFile();
    }

    private static final String SKELETON_TRAP = "mob.skeleton-trap";
    private boolean isSkeletonTrap() {
        if(!mobConfiguration.contains(SKELETON_TRAP, true))
            return false;

        return mobConfiguration.getBoolean(SKELETON_TRAP);
    }

    public void setSkeletonTrap(Boolean skeletonTrap) {
        mobConfiguration.set(SKELETON_TRAP, skeletonTrap);
        saveConfigurationFile();
    }

    private static final String PANDA_GENE = "mob.panda-gene";
    private Panda.Gene getPandaGene() {
        try {
            return Panda.Gene.valueOf(mobConfiguration.getString(PANDA_GENE));
        } catch (Exception exception) {
            return null;
        }
    }

    public void setPandaGene(Panda.Gene pandaGene) {
        if (pandaGene != null)
            mobConfiguration.set(PANDA_GENE, pandaGene.name());
        else
            mobConfiguration.set(PANDA_GENE, null);
        saveConfigurationFile();
    }

    private static final String ANGRY_WOLF = "mob.angry-wolf";
    private boolean isAngry() {
        if(!mobConfiguration.contains(ANGRY_WOLF, true))
            return false;

        return mobConfiguration.getBoolean(ANGRY_WOLF);
    }

    public void setAngryWolf(Boolean angry) {
        mobConfiguration.set(ANGRY_WOLF, angry);
        saveConfigurationFile();
    }


    // Custom Effects
    private static final String CHARGE_CUSTOM_EFFECT = "custom-effects.charge";
    private boolean getChargeCustomEffect() {
        if(!mobConfiguration.contains(CHARGE_CUSTOM_EFFECT, true))
            return false;
        return mobConfiguration.getBoolean(CHARGE_CUSTOM_EFFECT);
    }

    public void setChargeCustomEffect(boolean chargeCustomEffect) {
        if(chargeCustomEffect)
            mobConfiguration.set(CHARGE_CUSTOM_EFFECT, true);
        else
            mobConfiguration.set(CHARGE_CUSTOM_EFFECT, null);
        saveConfigurationFile();
    }

    private static final String VANISH_CUSTOM_EFFECT = "custom-effects.vanish";
    private boolean getVanishCustomEffect() {
        if(!mobConfiguration.contains(VANISH_CUSTOM_EFFECT, true))
            return false;
        return mobConfiguration.getBoolean(VANISH_CUSTOM_EFFECT);
    }

    public void setVanishCustomEffect(boolean vanishCustomEffect) {
        if(vanishCustomEffect)
            mobConfiguration.set(VANISH_CUSTOM_EFFECT, true);
        else
            mobConfiguration.set(VANISH_CUSTOM_EFFECT, null);
        saveConfigurationFile();
    }

    private static final String EXPLOSION_CUSTOM_EFFECT = "custom-effects.explosion";
    private boolean getExplosionCustomEffect() {
        if(!mobConfiguration.contains(EXPLOSION_CUSTOM_EFFECT, true))
            return false;
        return true;
    }

    public void setExplosionCustomEffect(boolean explosionCustomEffect) {
        if(explosionCustomEffect)
            setExplosionCustomEffectExplosionStrength(5);
        else
            mobConfiguration.set(EXPLOSION_CUSTOM_EFFECT, null);
        saveConfigurationFile();
    }

    private static final String EXPLOSION_CUSTOM_EFFECT_EXPLOSION_STRENGTH = EXPLOSION_CUSTOM_EFFECT + ".explosion-strength";
    private int getExplosionCustomEffectExplosionStrength() {
        if(!mobConfiguration.contains(EXPLOSION_CUSTOM_EFFECT_EXPLOSION_STRENGTH, true))
            return 5;
        return mobConfiguration.getInt(EXPLOSION_CUSTOM_EFFECT_EXPLOSION_STRENGTH);
    }

    public void setExplosionCustomEffectExplosionStrength(Integer explosionStrength) {
        if(explosionStrength != null)
            mobConfiguration.set(EXPLOSION_CUSTOM_EFFECT_EXPLOSION_STRENGTH, explosionStrength);
        else
            mobConfiguration.set(EXPLOSION_CUSTOM_EFFECT_EXPLOSION_STRENGTH, null);
        saveConfigurationFile();
    }

    // Others

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
