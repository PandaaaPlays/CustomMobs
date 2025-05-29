package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
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

public class Vanish extends CustomMobCustomEffect {
    /**
     * Determines the time (in second(s)) that the invisibility effect is applied on the CustomMob.
     * To set the time to be infinite, set the time to -1 (anything under 0).
     * @minimum 1
     * @maximum Infinite
     */
    private static final String VANISH_TIME = "custom-effects.vanish.vanish-time";
    private int vanishTime;

    public Vanish(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.COOLDOWN);
        this.vanishTime = getCustomEffectOption(VANISH_TIME, Integer.class, 5);
    }

    public void triggerCustomEffect(Entity entity) {
        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, vanishTime * 20, 1));
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "vanishtime": {
                if(clickType.isRightClick()) {
                    this.vanishTime = 5;
                    setCustomEffectOption(VANISH_TIME, this.vanishTime);
                } else {
                    new SpecificPotionDurationGUI(this.vanishTime, (value) -> {
                        this.vanishTime = value;
                        setCustomEffectOption(VANISH_TIME, this.vanishTime);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker);
                }
                return getCustomEffectOptionItemStack(getVanishTimeItem(), true);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BARRIER);
        item.setName("&6&lVanish");
        String vanish = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eInvisibility: &f" + vanish);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( CustomMob becomes invisible ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getVanishTimeItem(), true));
        items.add(getMessageItem());
        return items;
    }

    private CustomMobsItem getVanishTimeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BARRIER);
        item.setName("&b&lInvisibility duration");
        String duration = this.vanishTime <= 0 ? "Infinite" : Utils.getFormattedTime(this.vanishTime, false, true);
        item.addLore("&eDuration: &f" + duration);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".VanishTime");
        return item;
    }
}
