package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Rabbit extends CustomMobType {
    private org.bukkit.entity.Rabbit.Type rabbitType;

    public Rabbit(org.bukkit.entity.Rabbit.Type rabbitType) {
        this.rabbitType = rabbitType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Rabbit))
            return;

        if(rabbitType != null)
            ((org.bukkit.entity.Rabbit) customMob).setRabbitType(rabbitType);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getRabbitTypeItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "rabbittype": {
                if(clickType.isRightClick()) {
                    this.rabbitType = null;
                } else {
                    this.rabbitType = NextOptions.getNextRabbitType(rabbitType);
                }
                customMob.getCustomMobConfiguration().setRabbitType(rabbitType);
                return getOptionItemStack(getRabbitTypeItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getRabbitTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.RABBIT_SPAWN_EGG);
        item.setName("&b&lRabbit type");
        String type = rabbitType == null ? "&fRandom" : "&f" + rabbitType.toString();
        item.addLore("&eType: &f" + type);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "RabbitType");
        return item;
    }
}
