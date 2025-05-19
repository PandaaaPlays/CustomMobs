package ca.pandaaa.custommobs.custommobs.CustomEffects;

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

public class Explosion extends CustomMobCustomEffect {
    private int explosionStrength;

    public Explosion(boolean enabled, int explosionStrength) {
        this.enabled = enabled;
        this.explosionStrength = explosionStrength;
        this.customEffectType = CustomEffectType.COOLDOWN;
    }

    public void triggerCustomEffect(Entity entity) {
        entity.getWorld().createExplosion(entity.getLocation(), explosionStrength);
    }

    public ItemStack modifyStatus(CustomMob customMob) {
        customMob.getCustomMobConfiguration().setExplosionCustomEffect(this.enabled);
        if(this.enabled)
            explosionStrength = 5;
        return getCustomEffectItem();
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "explosionstrength": {
                if(clickType.isRightClick()) {
                    this.explosionStrength = 5;
                    customMob.getCustomMobConfiguration().setExplosionCustomEffectExplosionStrength(explosionStrength);
                } else {
                    new IntegerGUI("Explosion strength", false, 1, 100, (value) -> {
                        this.explosionStrength = value;
                        customMob.getCustomMobConfiguration().setExplosionCustomEffectExplosionStrength(explosionStrength);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, explosionStrength);
                }
                return getCustomEffectOptionItemStack(getExplosionStrengthItem(), true);
            }
        }
        return null;
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TNT);
        item.setName("&6&lExplosion");
        String explosion = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eExplosion: &f" + explosion);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( CustomMob creates an explosion ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getExplosionStrengthItem(), true));
        return items;
    }

    private CustomMobsItem getExplosionStrengthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TNT);
        item.setName("&c&lExplosion strength");
        item.addLore("&eRadius: &f" + explosionStrength);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".ExplosionStrength");
        return item;
    }


}
