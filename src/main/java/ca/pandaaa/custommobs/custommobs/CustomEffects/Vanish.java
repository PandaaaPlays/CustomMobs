package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.custommobs.CustomMob;
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
    private int vanishTime;

    public Vanish(boolean enabled) {
        this.enabled = enabled;
        this.customEffectType = CustomEffectType.COOLDOWN;
    }

    public void triggerCustomEffect(Entity entity) {
        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));
    }

    public ItemStack modifyStatus(CustomMob customMob) {
        customMob.getCustomMobConfiguration().setVanishCustomEffect(this.enabled);
        return getCustomEffectItem();
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        return null;
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
        CustomMobsItem item = new CustomMobsItem(Material.BARRIER);
        item.setName("&c&lInvisibility duration");
        items.add(getCustomEffectOptionItemStack(item, false));
        return items;
    }
}
