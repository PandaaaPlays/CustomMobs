package ca.pandaaa.custommobs.configurations;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.DamageRange;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.UUID;

public class CustomMobConfiguration {
    private final String fileName;
    private final FileConfiguration mobConfiguration;

    public CustomMobConfiguration(String fileName, FileConfiguration mobConfiguration) {
        this.fileName = fileName;
        this.mobConfiguration = mobConfiguration;
    }

    public String getFileName() {
        return fileName;
    }

    public CustomMob loadCustomMob() {
        EntityType type = this.getType();
        if(type == null
                || type.getEntityClass() == null
                || type == EntityType.PLAYER
                || fileName.contains(" ")
                || !(LivingEntity.class.isAssignableFrom(type.getEntityClass())))
            return null;

        CustomMob customMob = new CustomMob(type, fileName);

        customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.Special(
                getName(),
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

        customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.Sound(
                getSoundType(),
                getSoundRadius(),
                getSoundVolume(),
                getSoundPitch()));

        customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.Message(
                getMessageText(),
                getMessageRadius()));

        if(AbstractHorse.class.isAssignableFrom(type.getEntityClass()))
            customMob.addCustomMobType(new ca.pandaaa.custommobs.custommobs.options.
                    AbstractHorse(getJumpStrength()));
        if(Ageable.class.isAssignableFrom(type.getEntityClass()))
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
                    Creeper(getExplosionCooldown(), getExplosionRadius()));
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
        return customMob;
    }

    public ItemStack getItem() {
        if(fileName.contains(" "))
            return null;

        ItemStack item = new ItemStack(Material.GLOW_ITEM_FRAME);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(fileName.replace(".yml", ""));
        item.setItemMeta(itemMeta);
        return item;
    }

    private static final String TYPE = "mob.type";
    private EntityType getType() {
        try {
            return EntityType.valueOf(mobConfiguration.getString(TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String NAME = "mob.name";
    private String getName() {
        if(!mobConfiguration.contains(NAME, true))
            return null;

        return mobConfiguration.getString(NAME);
    }

    private static final String VISIBLE_NAME = "mob.visible-name";
    private boolean isNameVisible() {
        if(!mobConfiguration.contains(VISIBLE_NAME, true))
            return false;

        return mobConfiguration.getBoolean(VISIBLE_NAME);
    }

    private static final String BABY = "mob.baby";
    private Boolean isBaby() {
        if(!mobConfiguration.contains(BABY, true))
            return null;

        return mobConfiguration.getBoolean(BABY);
    }

    private static final String CAN_BREAK_DOORS = "mob.can-break-doors";
    private boolean canBreakDoors() {
        if(!mobConfiguration.contains(CAN_BREAK_DOORS, true))
            return false;

        return mobConfiguration.getBoolean(CAN_BREAK_DOORS);
    }

    private static final String CAT_TYPE = "mob.cat-type";
    private Cat.Type getCatType() {
        try {
            return Cat.Type.valueOf(mobConfiguration.getString(CAT_TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String RABBIT_TYPE = "mob.rabbit-type";
    private Rabbit.Type getRabbitType() {
        try {
            return Rabbit.Type.valueOf(mobConfiguration.getString(RABBIT_TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String FOX_TYPE = "mob.fox-type";
    private org.bukkit.entity.Fox.Type getFoxType() {
        try {
            return Fox.Type.valueOf(mobConfiguration.getString(FOX_TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String VILLAGER_TYPE = "mob.villager-type";
    private Villager.Type getVillagerType() {
        try {
            return Villager.Type.valueOf(mobConfiguration.getString(VILLAGER_TYPE));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String HORSE_COLOR = "mob.horse-color";
    private Horse.Color getHorseColor() {
        try {
            return Horse.Color.valueOf(mobConfiguration.getString(HORSE_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String HORSE_STYLE = "mob.horse-style";
    private Horse.Style getHorseStyle() {
        try {
            return Horse.Style.valueOf(mobConfiguration.getString(HORSE_STYLE));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String LLAMA_COLOR = "mob.llama-color";
    private Llama.Color getLlamaColor() {
        try {
            return Llama.Color.valueOf(mobConfiguration.getString(LLAMA_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String AXOLOTL_VARIANT = "mob.axolotl-variant";
    private Axolotl.Variant getAxolotlVariant() {
        try {
            return Axolotl.Variant.valueOf(mobConfiguration.getString(AXOLOTL_VARIANT));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String FROG_VARIANT = "mob.frog-variant";
    private Frog.Variant getFrogVariant() {
        try {
            return Frog.Variant.valueOf(mobConfiguration.getString(FROG_VARIANT));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String PARROT_VARIANT = "mob.parrot-variant";
    private Parrot.Variant getParrotVariant() {
        try {
            return Parrot.Variant.valueOf(mobConfiguration.getString(PARROT_VARIANT));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String VILLAGER_PROFESSION = "mob.villager-profession";
    private Villager.Profession getVillagerProfession() {
        try {
            return Villager.Profession.valueOf(mobConfiguration.getString(VILLAGER_PROFESSION));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String EXPLOSION_COOLDOWN = "mob.explosion-cooldown";
    private Integer getExplosionCooldown() {
        if(!mobConfiguration.contains(EXPLOSION_COOLDOWN, true))
            return null;

        return mobConfiguration.getInt(EXPLOSION_COOLDOWN);
    }

    private static final String EXPLOSION_RADIUS = "mob.explosion-radius";
    private Integer getExplosionRadius() {
        if(!mobConfiguration.contains(EXPLOSION_RADIUS, true))
            return null;

        return mobConfiguration.getInt(EXPLOSION_RADIUS);
    }

    private static final String SLIME_SIZE = "mob.slime-size";
    private Integer getSlimeSize() {
        if(!mobConfiguration.contains(SLIME_SIZE, true))
            return null;

        return mobConfiguration.getInt(SLIME_SIZE);
    }

    private static final String ZOMBIFIED_PIGLIN_ANGER = "mob.zombified-piglin-anger";
    private int getZombifiedPiglinAnger() {
        if(!mobConfiguration.contains(ZOMBIFIED_PIGLIN_ANGER, true))
            return 0;

        return mobConfiguration.getInt(ZOMBIFIED_PIGLIN_ANGER);
    }

    private static final String SHULKER_COLOR = "mob.shulker-color";
    private DyeColor getShulkerColor() {
        try {
            return DyeColor.valueOf(mobConfiguration.getString(SHULKER_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String JOHNNY_VANDICATOR = "mob.johnny-vandicator";
    private boolean isJohnnyVindicator() {
        if(!mobConfiguration.contains(JOHNNY_VANDICATOR, true))
            return false;

        return mobConfiguration.getBoolean(JOHNNY_VANDICATOR);
    }

    private static final String PHANTOM_SIZE = "mob.phantom-size";
    private Integer getPhantomSize() {
        if(!mobConfiguration.contains(PHANTOM_SIZE, true))
            return null;

        return mobConfiguration.getInt(PHANTOM_SIZE);
    }

    private static final String SADDLE = "mob.saddle";
    private boolean hasSaddle() {
        if(!mobConfiguration.contains(SADDLE, true))
            return false;

        return mobConfiguration.getBoolean(SADDLE);
    }

    private static final String SITTING = "mob.sitting";
    private boolean isSitting() {
        if(!mobConfiguration.contains(SITTING, true))
            return false;

        return mobConfiguration.getBoolean(SITTING);
    }

    private static final String COLLAR_COLOR = "mob.collar-color";
    private DyeColor getCollarColor() {
        try {
            return DyeColor.valueOf(mobConfiguration.getString(COLLAR_COLOR));
        } catch(Exception exception) {
            return null;
        }
    }

    private static final String OWNER = "mob.owner";
    private UUID getOwner() {
        try {
            return Objects.requireNonNull(Bukkit.getPlayer(
                    Objects.requireNonNull(mobConfiguration.getString(OWNER))))
                    .getUniqueId();
        } catch (Exception exception) {
            return null;
        }
    }

    private static final String CHEST = "mob.chested";
    private boolean hasChest() {
        if(!mobConfiguration.contains(CHEST, true))
            return false;

        return mobConfiguration.getBoolean(CHEST);
    }

    private static final String JUMP_STRENGTH = "mob.jump-strength";
    private Double getJumpStrength() {
        if(!mobConfiguration.contains(JUMP_STRENGTH, true))
            return null;

        return mobConfiguration.getDouble(JUMP_STRENGTH);
    }

    private static final String TAMED = "mob.tamed";
    private boolean isTamed() {
        if(!mobConfiguration.contains(TAMED, true))
            return false;

        return mobConfiguration.getBoolean(TAMED);
    }

    private static final String SKELETON_TRAP = "mob.skeleton-trap";
    private boolean isSkeletonTrap() {
        if(!mobConfiguration.contains(SKELETON_TRAP, true))
            return false;

        return mobConfiguration.getBoolean(SKELETON_TRAP);
    }

    private static final String PANDA_GENE = "mob.panda-gene";
    private Panda.Gene getPandaGene() {
        try {
            return Panda.Gene.valueOf(mobConfiguration.getString(PANDA_GENE));
        } catch (Exception exception) {
            return null;
        }
    }

    private static final String ANGRY_WOLF = "mob.angry-wolf";
    private boolean isAngry() {
        if(!mobConfiguration.contains(ANGRY_WOLF, true))
            return false;

        return mobConfiguration.getBoolean(ANGRY_WOLF);
    }

    private static final String HEALTH = "special.health";
    private Double getHealth() {
        if(!mobConfiguration.contains(HEALTH, true))
            return null;

        return mobConfiguration.getDouble(HEALTH);
    }

    private static final String AGGRESSIVE = "special.aggressive";
    private boolean isAggressive() {
        if(!mobConfiguration.contains(AGGRESSIVE, true))
            return false;

        return mobConfiguration.getBoolean(AGGRESSIVE);
    }

    private static final String GLOWING = "special.glowing";
    private boolean isGlowing() {
        if(!mobConfiguration.contains(GLOWING, true))
            return false;

        return mobConfiguration.getBoolean(GLOWING);
    }

    private static final String CAN_PICKUP_LOOT = "special.can-pickup-loot";
    private boolean canPickupLoot() {
        if(!mobConfiguration.contains(CAN_PICKUP_LOOT, true))
            return true;

        return mobConfiguration.getBoolean(CAN_PICKUP_LOOT);
    }

    private static final String KNOCKBACK_RESISTANCE = "special.knockback-resistance";
    private Double getKnockbackResistance() {
        if(!mobConfiguration.contains(KNOCKBACK_RESISTANCE, true))
            return null;

        return mobConfiguration.getDouble(KNOCKBACK_RESISTANCE);
    }

    private static final String SPEED = "special.speed";
    private Double getSpeed() {
        if(!mobConfiguration.contains(SPEED, true))
            return null;

        return mobConfiguration.getDouble(SPEED);
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
    private boolean isInvincible() {
        if(!mobConfiguration.contains(INVINCIBLE, true))
            return false;

        return mobConfiguration.getBoolean(INVINCIBLE);
    }

    private static final String SILENT = "special.silent";
    private boolean isSilent() {
        if(!mobConfiguration.contains(SILENT, true))
            return false;

        return mobConfiguration.getBoolean(SILENT);
    }

    private static final String GRAVITY = "special.gravity";
    private boolean hasGravity() {
        if(!mobConfiguration.contains(GRAVITY, true))
            return true;

        return mobConfiguration.getBoolean(GRAVITY);
    }

    private static final String PERSISTENT = "special.persistent";
    private boolean isPersistent() {
        if(!mobConfiguration.contains(PERSISTENT, true))
            return false;

        return mobConfiguration.getBoolean(PERSISTENT);
    }

    private static final String INTELLIGENT = "special.intelligent";
    private boolean isIntelligent() {
        if(!mobConfiguration.contains(INTELLIGENT, true))
            return true;

        return mobConfiguration.getBoolean(INTELLIGENT);
    }

    private static final String FOLLOW_RANGE = "special.follow-range";
    private Double getFollowRange() {
        if(!mobConfiguration.contains(FOLLOW_RANGE, true))
            return null;

        return mobConfiguration.getDouble(FOLLOW_RANGE);
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

    private static final String MESSAGE_TEXT = "message.text";
    private String getMessageText() {
        if(!mobConfiguration.contains(MESSAGE_TEXT, true))
            return null;

        return mobConfiguration.getString(MESSAGE_TEXT);
    }

    private static final String MESSAGE_RADIUS = "message.radius";
    private double getMessageRadius() {
        if(!mobConfiguration.contains(MESSAGE_RADIUS, true))
            return -1;

        return mobConfiguration.getDouble(MESSAGE_RADIUS);
    }
}
