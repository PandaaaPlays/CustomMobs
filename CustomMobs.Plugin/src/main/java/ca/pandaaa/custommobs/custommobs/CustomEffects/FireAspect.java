package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FireAspect extends CustomMobCustomEffect {

    /**
     * Determines how long (in second(s)) players will stay on fire upon ignition from the CustomMob.
     * @minimum 1
     * @maximum 30
     */
    private static final String FIRE_DURATION = "custom-effects.fireaspect.fire-duration";
    private int fireDuration;

    public FireAspect(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.ON_IMPACT);
        fireDuration = getCustomEffectOption(FIRE_DURATION, Integer.class, 5);
    }

    public void triggerCustomEffect(Entity entity) {
        List<Entity> playersAround = entity.getNearbyEntities(1D, 1D, 1D).stream().filter(e -> e instanceof Player).toList();
        for(Entity player : playersAround) {
            player.setFireTicks(fireDuration * 20);
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "fireduration": {
                if(clickType.isRightClick()) {
                    fireDuration = 5;
                    setCustomEffectOption(FIRE_DURATION, this.fireDuration);
                } else {
                    new IntegerGUI("Fire duration", false, 1, 30, (value) -> {
                        this.fireDuration = value;
                        setCustomEffectOption(FIRE_DURATION, this.fireDuration);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, fireDuration);
                }
                return getCustomEffectOptionItemStack(getFireDurationItem(), true);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.FLINT_AND_STEEL);
        item.setName("&6&lFire aspect");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eFire aspect: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Ignites players upon contact ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getFireDurationItem(), true));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getFireDurationItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&6&lFire duration");
        item.addLore("&eDuration: &f" + this.fireDuration + " second(s)");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".FireDuration");
        return item;
    }
}