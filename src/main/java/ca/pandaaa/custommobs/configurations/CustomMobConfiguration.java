package ca.pandaaa.custommobs.configurations;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.custommobs.Equipment;
import ca.pandaaa.custommobs.custommobs.Messages;
import ca.pandaaa.custommobs.utils.DamageRange;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CustomMobConfiguration {
    private final String fileName;
    private final FileConfiguration mobConfiguration;
    private final File mobFile;

    public CustomMobConfiguration(FileConfiguration mobConfiguration, File mobFile) {
        this.fileName = mobFile.getName();
        this.mobConfiguration = mobConfiguration;
        this.mobFile = mobFile;
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
                getItem(SPAWNER),
                getEquipment(),
                getPotionMeta(),
                getDrops(),
                getName(),
                new ArrayList<>(),
                this);

        // TODO sounds
        // TODO Horses, lama etc saddles should be able to have saddle

        setCustomMobConfigurations(customMob, type);

        return customMob;
    }

    public void setCustomMobConfigurations(CustomMob customMob, EntityType type) {
        customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.Special(
                isNameVisible(),
                getHealth(),
                isAggressive(),
                isGlowing(),
                canPickupLoot(),
                getKnockbackResistance(),
                getSpeed(),
                getDamageRange(),
                isInvincible(),
                isSilent(),
                hasGravity(),
                isPersistent(),
                isIntelligent(),
                getFollowRange()));

        if(AbstractHorse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    AbstractHorse(getJumpStrength()));

        // "Ageable" mobs that cannot actually be babies : Parrot, Bat, Piglin Brute
        // TODO Verify that there are no other mobs (probably not)
        if(Ageable.class.isAssignableFrom(type.getEntityClass())
            && !Arrays.asList(Parrot.class, Bat.class, PiglinBrute.class).contains(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Ageable(isBaby()));
        if(Axolotl.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Axolotl(getAxolotlVariant()));
        if(Cat.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Cat(getCatType(), getCollarColor()));
        if(ChestedHorse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    ChestedHorse(hasChest()));
        if(Creeper.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Creeper(getExplosionCooldown(), getExplosionRadius(), isChargedCreeper()));
        if(Fox.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Fox(getFoxType()));
        if(Frog.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Frog(getFrogVariant()));
        if(Horse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Horse(getHorseColor(), getHorseStyle()));
        if(Llama.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Llama(getLlamaColor()));
        if(Panda.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Panda(getPandaGene()));
        if(Parrot.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Parrot(getParrotVariant()));
        if(Phantom.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Phantom(getPhantomSize()));
        if(PigZombie.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    PigZombie(getZombifiedPiglinAnger()));
        if(Rabbit.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Rabbit(getRabbitType()));
        if(Shulker.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Shulker(getShulkerColor()));
        if(Sittable.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Sittable(isSitting()));
        if(SkeletonHorse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    SkeletonHorse(isSkeletonTrap()));
        if(Slime.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Slime(getSlimeSize()));
        if(Steerable.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Steerable(hasSaddle()));
        if(Tameable.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Tameable(getOwner(), isTamed()));
        if(Villager.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Villager(getVillagerType(), getVillagerProfession()));
        if(Vindicator.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Vindicator(isJohnnyVindicator()));
        if(Wolf.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Wolf(getCollarColor(), isAngry()));
        if(Zombie.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    Zombie(canBreakDoors()));
    }

    public void resetType(EntityType type) {
        if(AbstractHorse.class.isAssignableFrom(type.getEntityClass()))
            setJumpStrength(null);
        if (Ageable.class.isAssignableFrom(type.getEntityClass()))
            setBaby(null);
        if (Axolotl.class.isAssignableFrom(type.getEntityClass()))
            setAxolotlVariant(null);
        if (Cat.class.isAssignableFrom(type.getEntityClass())) {
            setCatType(null);
            setCollarColor(null);
        }
        if (ChestedHorse.class.isAssignableFrom(type.getEntityClass()))
            setHasChest(null);
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
        }
        if(Llama.class.isAssignableFrom(type.getEntityClass()))
            setLlamaColor(null);
        if(Panda.class.isAssignableFrom(type.getEntityClass()))
            setPandaGene(null);
        if(Parrot.class.isAssignableFrom(type.getEntityClass()))
            setParrotVariant(null);
        if(Phantom.class.isAssignableFrom(type.getEntityClass()))
            setPhantomSize(null);
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
        }
        if(Zombie.class.isAssignableFrom(type.getEntityClass()))
            setCanBreakDoors(null);
    }

    public static final String ITEM = "item";
    public static final String SPAWNER = "spawner";

    public ItemStack getItem(String configurationPath) {
        ItemStack item = getItemStack(configurationPath);
        if(item == null) {
            item = new ItemStack(configurationPath.equalsIgnoreCase(ITEM) ? Material.ALLAY_SPAWN_EGG : Material.SPAWNER);
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

    public static final String POTIONS = "potions";
    public List<PotionMeta> getPotionMeta() {
        if(!mobConfiguration.contains(POTIONS, true))
            return new ArrayList<>();
        return (List<PotionMeta>) mobConfiguration.getList(POTIONS);
    }

    public void setPotionMeta(List<PotionMeta> potionMeta) {
        mobConfiguration.set(POTIONS, potionMeta);
        saveConfigurationFile();
    }


    public Messages getMessages() {
        return new Messages(getMessageText(false), getMessageRadius(false),
                getMessageText(true), getMessageRadius(true));
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

    private static final String VISIBLE_NAME = "special.visible-name";
    public boolean isNameVisible() {
        if(!mobConfiguration.contains(VISIBLE_NAME, true))
            return false;

        return mobConfiguration.getBoolean(VISIBLE_NAME);
    }

    public void setNameVisible(boolean visible) {
        mobConfiguration.set(VISIBLE_NAME, visible);
        saveConfigurationFile();
    }

    private static final String BABY = "mob.baby";
    private Boolean isBaby() {
        if(!mobConfiguration.contains(BABY, true))
            return null;

        return mobConfiguration.getBoolean(BABY);
    }

    public void setBaby(Boolean baby) {
        mobConfiguration.set(BABY, baby);
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

    private static final String CAT_TYPE = "mob.cat-type";
    private Cat.Type getCatType() {
        try {
            return Registry.CAT_VARIANT.get(NamespacedKey.minecraft(mobConfiguration.getString(CAT_TYPE).toLowerCase()));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setCatType(Cat.Type catType) {
        if (catType != null)
            mobConfiguration.set(CAT_TYPE, catType.toString());
        else
            mobConfiguration.set(CAT_TYPE, null);
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

    private static final String AXOLOTL_VARIANT = "mob.axolotl-variant";
    private Axolotl.Variant getAxolotlVariant() {
        try {
            return Axolotl.Variant.valueOf(mobConfiguration.getString(AXOLOTL_VARIANT));
        } catch(Exception exception) {
            return null;
        }
    }

    public void setAxolotlVariant(Axolotl.Variant axolotlVariant) {
        if (axolotlVariant != null)
            mobConfiguration.set(AXOLOTL_VARIANT, axolotlVariant.name());
        else
            mobConfiguration.set(AXOLOTL_VARIANT, null);
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

    private static final String PHANTOM_SIZE = "mob.phantom-size";
    private int getPhantomSize() {
        if(!mobConfiguration.contains(PHANTOM_SIZE, true))
            return 0;

        return mobConfiguration.getInt(PHANTOM_SIZE);
    }

    public void setPhantomSize(Integer phantomSize) {
        mobConfiguration.set(PHANTOM_SIZE, phantomSize);
        saveConfigurationFile();
    }

    private static final String SADDLE = "mob.saddle";
    private boolean hasSaddle() {
        if(!mobConfiguration.contains(SADDLE, true))
            return false;

        return mobConfiguration.getBoolean(SADDLE);
    }

    public void setHasSaddle(Boolean hasSaddle) {
        mobConfiguration.set(SADDLE, hasSaddle);
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


    private static final String CHEST = "mob.chested";
    private boolean hasChest() {
        if(!mobConfiguration.contains(CHEST, true))
            return false;

        return mobConfiguration.getBoolean(CHEST);
    }

    public void setHasChest(Boolean hasChest) {
        mobConfiguration.set(CHEST, hasChest);
        saveConfigurationFile();
    }

    private static final String JUMP_STRENGTH = "mob.jump-strength";
    private double getJumpStrength() {
        if(!mobConfiguration.contains(JUMP_STRENGTH, true))
            return 0.7D;

        return mobConfiguration.getDouble(JUMP_STRENGTH);
    }

    public void setJumpStrength(Double jumpStrength) {
        mobConfiguration.set(JUMP_STRENGTH, jumpStrength);
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

    private static final String HEALTH = "special.health";
    private Double getHealth() {
        if(!mobConfiguration.contains(HEALTH, true))
            return null;

        return mobConfiguration.getDouble(HEALTH);
    }

    public void setHealth(Double health) {
        mobConfiguration.set(HEALTH, health);
        saveConfigurationFile();
    }

    private static final String AGGRESSIVE = "special.aggressive";
    public boolean isAggressive() {
        if(!mobConfiguration.contains(AGGRESSIVE, true))
            return false;

        return mobConfiguration.getBoolean(AGGRESSIVE);
    }

    public void setAggressive(boolean aggressive) {
        mobConfiguration.set(AGGRESSIVE, aggressive);
        saveConfigurationFile();
    }

    private static final String GLOWING = "special.glowing";
    public boolean isGlowing() {
        if(!mobConfiguration.contains(GLOWING, true))
            return false;

        return mobConfiguration.getBoolean(GLOWING);
    }

    public void setGlowing(boolean glowing) {
        mobConfiguration.set(GLOWING, glowing);
        saveConfigurationFile();
    }

    private static final String CAN_PICKUP_LOOT = "special.can-pickup-loot";
    public boolean canPickupLoot() {
        if(!mobConfiguration.contains(CAN_PICKUP_LOOT, true))
            return true;

        return mobConfiguration.getBoolean(CAN_PICKUP_LOOT);
    }

    public void setCanPickupLoot(boolean canPickupLoot) {
        mobConfiguration.set(CAN_PICKUP_LOOT, canPickupLoot);
        saveConfigurationFile();
    }

    private static final String KNOCKBACK_RESISTANCE = "special.knockback-resistance";
    private double getKnockbackResistance() {
        if(!mobConfiguration.contains(KNOCKBACK_RESISTANCE, true))
            return 0D;

        return mobConfiguration.getDouble(KNOCKBACK_RESISTANCE);
    }

    public void setKnockbackResistance(double knockbackResistance) {
        mobConfiguration.set(KNOCKBACK_RESISTANCE, knockbackResistance);
        saveConfigurationFile();
    }

    private static final String SPEED = "special.speed";
    private double getSpeed() {
        if(!mobConfiguration.contains(SPEED, true))
            return 0.7D;

        return mobConfiguration.getDouble(SPEED);
    }

    public void setSpeed(double speed) {
        mobConfiguration.set(SPEED, speed);
        saveConfigurationFile();
    }

    private static final String MINIMUM_DAMAGE = "special.damage-range.minimum";
    private static final String MAXIMUM_DAMAGE = "special.damage-range.maximum";
    private static final String DAMAGE = "special.damage";
    private DamageRange getDamageRange() {
        if(mobConfiguration.contains(MINIMUM_DAMAGE, true)
                && mobConfiguration.contains(MAXIMUM_DAMAGE, true))
            return new DamageRange(
                    mobConfiguration.getDouble(MINIMUM_DAMAGE),
                    mobConfiguration.getDouble(MAXIMUM_DAMAGE));
        else if (mobConfiguration.contains(DAMAGE, true))
            return new DamageRange(
                    mobConfiguration.getDouble(DAMAGE),
                    mobConfiguration.getDouble(DAMAGE));
        else
            return null;
    }


    private static final String INVINCIBLE = "special.invincible";
    public boolean isInvincible() {
        if(!mobConfiguration.contains(INVINCIBLE, true))
            return false;

        return mobConfiguration.getBoolean(INVINCIBLE);
    }

    public void setInvincible(boolean invincible) {
        mobConfiguration.set(INVINCIBLE, invincible);
        saveConfigurationFile();
    }

    private static final String SILENT = "special.silent";
    public boolean isSilent() {
        if(!mobConfiguration.contains(SILENT, true))
            return false;

        return mobConfiguration.getBoolean(SILENT);
    }

    public void setSilent(boolean silent) {
        mobConfiguration.set(SILENT, silent);
        saveConfigurationFile();
    }

    private static final String GRAVITY = "special.gravity";
    public boolean hasGravity() {
        if(!mobConfiguration.contains(GRAVITY, true))
            return true;

        return mobConfiguration.getBoolean(GRAVITY);
    }

    public void setGravity(boolean gravity) {
        mobConfiguration.set(GRAVITY, gravity);
        saveConfigurationFile();
    }

    private static final String PERSISTENT = "special.persistent";
    public boolean isPersistent() {
        if(!mobConfiguration.contains(PERSISTENT, true))
            return false;

        return mobConfiguration.getBoolean(PERSISTENT);
    }

    public void setPersistent(boolean persistent) {
        mobConfiguration.set(PERSISTENT, persistent);
        saveConfigurationFile();
    }

    private static final String INTELLIGENT = "special.intelligent";
    public boolean isIntelligent() {
        if(!mobConfiguration.contains(INTELLIGENT, true))
            return true;

        return mobConfiguration.getBoolean(INTELLIGENT);
    }

    public void setIntelligent(boolean intelligent) {
        mobConfiguration.set(INTELLIGENT, intelligent);
        saveConfigurationFile();
    }

    private static final String FOLLOW_RANGE = "special.follow-range";
    private double getFollowRange() {
        if(!mobConfiguration.contains(FOLLOW_RANGE, true))
            return 32D;

        return mobConfiguration.getDouble(FOLLOW_RANGE);
    }

    public void setFollowRange(double followRange) {
        mobConfiguration.set(FOLLOW_RANGE, followRange);
        saveConfigurationFile();
    }

    private static final String SOUND_TYPE = "sound.type";
    private Sound getSoundType() {
        try {
            return Sound.valueOf(mobConfiguration.getString(SOUND_TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String SOUND_RADIUS = "sound.radius";
    private double getSoundRadius() {
        if(!mobConfiguration.contains(SOUND_RADIUS, true))
            return -1;

        return mobConfiguration.getDouble(SOUND_RADIUS);
    }

    private static final String SOUND_VOLUME = "sound.volume";
    private float getSoundVolume() {
        if(!mobConfiguration.contains(SOUND_VOLUME, true))
            return 1;

        return (float) mobConfiguration.getDouble(SOUND_VOLUME);
    }

    private static final String SOUND_PITCH = "sound.pitch";
    private float getSoundPitch() {
        if(!mobConfiguration.contains(SOUND_PITCH, true))
            return 1;

        return (float) mobConfiguration.getDouble(SOUND_PITCH);
    }

    // Messages //

    private static final String MESSAGE_TEXT = "messages.text";
    private static final String DEATH_MESSAGE_TEXT = "death-messages.text";
    private List<String> getMessageText(boolean death) {
        if(!mobConfiguration.contains(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, true))
            return new ArrayList<>();

        return mobConfiguration.getStringList(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT);
    }

    public void addMessageText(String text, boolean death) {
        List<String> messages = new ArrayList<>();
        if(mobConfiguration.contains(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, true))
            messages = mobConfiguration.getStringList(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT);
        messages.add(text);

        mobConfiguration.set(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, messages);
        saveConfigurationFile();
    }

    public boolean removeMessageText(int number, boolean death) {
        List<String> messages = new ArrayList<>();
        if(mobConfiguration.contains(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, true))
            messages = mobConfiguration.getStringList(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT);
        if(messages.size() < number)
            return false;

        messages.remove(number - 1);
        mobConfiguration.set(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, messages);
        saveConfigurationFile();
        return true;
    }

    public void clearMessageText(boolean death) {
        List<String> messages = new ArrayList<>();

        mobConfiguration.set(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, messages);
        saveConfigurationFile();
    }

    public boolean editMessageText(int number, String text, boolean death) {
        List<String> messages = new ArrayList<>();
        if(mobConfiguration.contains(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, true))
            messages = mobConfiguration.getStringList(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT);
        if(messages.size() < number)
            return false;

        messages.set(number - 1, text);
        mobConfiguration.set(death ? DEATH_MESSAGE_TEXT : MESSAGE_TEXT, messages);
        saveConfigurationFile();
        return true;
    }

    private static final String MESSAGE_RADIUS = "messages.radius";
    private static final String DEATH_MESSAGE_RADIUS = "death-messages.radius";
    private double getMessageRadius(boolean death) {
        if(!mobConfiguration.contains(death ? DEATH_MESSAGE_RADIUS : MESSAGE_RADIUS, true))
            return -1;

        return mobConfiguration.getDouble(death ? DEATH_MESSAGE_RADIUS : MESSAGE_RADIUS);
    }

    public void setMessagesRadius(double radius, boolean death) {
        mobConfiguration.set(death ? DEATH_MESSAGE_RADIUS : MESSAGE_RADIUS, radius < 0 ? -1 : radius);
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
