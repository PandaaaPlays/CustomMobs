package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Charge extends CustomMobCustomEffect {

    /**
     * Determines the strength of the "charge" effect, which indicates how far the players will be pushed back
     * upon impact with the CustomMob.
     * @minimum 0.5
     * @maximum 3
     */
    private static final String KNOCKBACK_STRENGTH = "custom-effects.charge.knockback-strength";
    private double knockbackStrength;

    public Charge(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.ON_IMPACT);
        this.knockbackStrength = getCustomEffectOption(KNOCKBACK_STRENGTH, Double.class, 1D);
    }

    public void triggerCustomEffect(Entity entity) {
        List<Entity> playersAround = entity.getNearbyEntities(1D, 1D, 1D).stream().filter(e -> e instanceof Player).toList();
        for(Entity player : playersAround) {
            player.setVelocity(player.getLocation().getDirection().multiply(knockbackStrength * -1).setY(knockbackStrength));
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "knockbackstrength": {
                if(clickType.isRightClick()) {
                    this.knockbackStrength = 1;
                    setCustomEffectOption(KNOCKBACK_STRENGTH, this.knockbackStrength);
                } else {
                    new DoubleGUI("Knockback strength", false, 0.5, 3, (value) -> {
                        this.knockbackStrength = value;
                        setCustomEffectOption(KNOCKBACK_STRENGTH, this.knockbackStrength);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, knockbackStrength);
                }
                return getCustomEffectOptionItemStack(getKnockbackStrengthItem(), true);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOAT_HORN);
        item.setName("&6&lCharge");
        String charge = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eCharge: &f" + charge);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( CustomMob makes players jump back on impact ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getKnockbackStrengthItem(), true));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getKnockbackStrengthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.STICK);
        item.addEnchantment(Enchantment.KNOCKBACK, 1, true);
        item.setName("&a&lKnockback strength");
        item.addLore("&eKnockback strength: &f" + this.knockbackStrength);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".KnockbackStrength");
        return item;
    }
}
