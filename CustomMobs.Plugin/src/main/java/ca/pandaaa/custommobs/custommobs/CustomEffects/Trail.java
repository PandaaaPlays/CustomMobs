package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Trail extends CustomMobCustomEffect {
    public final static Map<UUID, Trail> activeTrails = new HashMap<>();

    public Trail(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.ALWAYS);
    }

    public void triggerCustomEffect(Entity entity) {
        Location location = entity.getLocation();
        Block block = location.getBlock();

        Material type = block.getType();
        if (type.isAir() || type.isBurnable()) {
            block.setType(Material.FIRE);
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        return handleMessageOption(clicker, customMob, option, clickType);
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIRT_PATH);
        item.setName("&6&lFire trail");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eFire trail: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Leaves a trail of fire behind the CustomMob ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getNoLeftClickCustomEffectItemStack(item);
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getMessageItem().getItem());
        return items;
    }
}