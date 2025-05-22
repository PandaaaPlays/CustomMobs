package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Charge extends CustomMobCustomEffect {

    public Charge(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.enabled = getCustomEffectStatus(this.getClass().getSimpleName());
        this.customEffectType = CustomEffectType.ON_IMPACT;
    }

    public void triggerCustomEffect(Entity entity) {
        List<Entity> playersAround = entity.getNearbyEntities(1D, 1D, 1D).stream().filter(e -> e instanceof Player).toList();
        for(Entity player : playersAround) {
            player.setVelocity(player.getLocation().getDirection().multiply(-1).setY(1));
        }
    }

    public ItemStack modifyStatus(CustomMob customMob) {
        setCustomEffectStatus(this.getClass().getSimpleName());
        return getCustomEffectItem();
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        return null;
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
        CustomMobsItem item = new CustomMobsItem(Material.ELYTRA);
        item.setName("&c&lInvisibility duration");
        items.add(getCustomEffectOptionItemStack(item, false));
        return items;
    }
}
