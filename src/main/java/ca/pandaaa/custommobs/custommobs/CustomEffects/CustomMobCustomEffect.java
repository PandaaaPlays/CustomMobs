package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class CustomMobCustomEffect {
    protected boolean enabled;
    protected CustomEffectType customEffectType;
    public abstract void triggerCustomEffect(Entity entity);
    public abstract ItemStack modifyStatus(CustomMob customMob);
    public abstract ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType);
    public abstract List<ItemStack> getOptionsItems();
    public abstract ItemStack getCustomEffectItem();

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
}
