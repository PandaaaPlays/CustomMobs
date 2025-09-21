package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.Potions.PotionEffectsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.Potions.SpecificPotionDurationGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Infection extends CustomMobCustomEffect {

    /**
     * Determines the time (in second(s)) that the effect is applied on the player hurt by the CustomMob.
     * To set the time to be infinite, set the time to -1 (anything under 0).
     * @minimum 1
     * @maximum Infinite
     */
    private static final String INFECTION_TIME = "custom-effects.infection.time";
    private int infectionTime;

    /**
     * Determines the strength of the effect applied on the player hurt by the CustomMob.
     * @minimum 1
     * @maximum 255
     */
    private static final String INFECTION_AMPLIFIER = "custom-effects.infection.amplifier";
    private int infectionAmplifier;

    /**
     * Determines the effect applied on the player hurt by the CustomMob.
     */
    private static final String INFECTION_EFFECT = "custom-effects.infection.effect";
    private PotionEffectType infectionEffect;

    public Infection(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.ON_DAMAGE_ON_PLAYER);
        this.infectionTime = getCustomEffectOption(INFECTION_TIME, Integer.class, 5);
        this.infectionAmplifier = getCustomEffectOption(INFECTION_AMPLIFIER, Integer.class, 1);
        this.infectionEffect = getCustomEffectOption(INFECTION_EFFECT, PotionEffectType.class, PotionEffectType.BLINDNESS);
    }

    public void triggerCustomEffect(Entity entity) {
        if(!((LivingEntity) entity).hasPotionEffect(infectionEffect)) {
            trySendCustomEffectMessage((Player) entity);
            ((LivingEntity) entity).addPotionEffect(new PotionEffect(infectionEffect, infectionTime * 20, infectionAmplifier));
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "infectiontime": {
                if(clickType.isRightClick()) {
                    this.infectionTime = 5;
                    setCustomEffectOption(INFECTION_TIME, this.infectionTime);
                } else {
                    new SpecificPotionDurationGUI(this.infectionTime, (value) -> {
                        this.infectionTime = value;
                        setCustomEffectOption(INFECTION_TIME, this.infectionTime);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker);
                }
                return getCustomEffectOptionItemStack(getInfectionTimeItem(), true);
            }
            case "infectionamplifier": {
                if(clickType.isRightClick()) {
                    this.infectionAmplifier = 1;
                    setCustomEffectOption(INFECTION_AMPLIFIER, this.infectionAmplifier);
                } else {
                    new IntegerGUI("Amplifier", false, 1, 255, (value) -> {
                        this.infectionAmplifier = value;
                        setCustomEffectOption(INFECTION_AMPLIFIER, this.infectionAmplifier);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, infectionAmplifier);
                }
                return getCustomEffectOptionItemStack(getInfectionTimeItem(), true);
            }
            case "infectioneffect": {
                new PotionEffectsGUI(customMob, (value) -> {
                    this.infectionEffect = value.getType();
                    setCustomEffectOption(INFECTION_EFFECT, this.infectionEffect.getKeyOrNull().getKey());
                    new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                }).openInventory(clicker, 1);
                return getCustomEffectOptionItemStack(getInfectionEffectItem(), false);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPECTRAL_ARROW);
        item.setName("&4&lInfection");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eInfection: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Gives a potion effect to the hurt player ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getInfectionTimeItem(), true));
        items.add(getCustomEffectOptionItemStack(getInfectionAmplifierItem(), true));
        items.add(getCustomEffectOptionItemStack(getInfectionEffectItem(), false));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getInfectionTimeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&b&lEffect duration");
        String duration = this.infectionTime <= 0 ? "Infinite" : Utils.getFormattedTime(this.infectionTime, false, true);
        item.addLore("&eDuration: &f" + duration);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".InfectionTime");
        return item;
    }

    private CustomMobsItem getInfectionAmplifierItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GLOWSTONE_DUST);
        item.setName("&6&lEffect amplifier");
        item.addLore("&eAmplifier: &f" + this.infectionAmplifier);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".InfectionAmplifier");
        return item;
    }

    private CustomMobsItem getInfectionEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BLAZE_POWDER);
        item.setName("&2&lPotion Effect");
        item.addLore("&eEffect: &f" + Utils.getStartCase(this.infectionEffect.getKeyOrNull().getKey()));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".InfectionEffect");
        return item;
    }
}