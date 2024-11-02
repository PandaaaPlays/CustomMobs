package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Fox extends CustomMobOption {
    private org.bukkit.entity.Fox.Type foxType;

    public Fox(org.bukkit.entity.Fox.Type foxType) {
        this.foxType = foxType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Fox))
            return;

        if(foxType != null)
            ((org.bukkit.entity.Fox) customMob).setFoxType(foxType);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getFoxTypeItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "foxtype": {
                if(clickType.isRightClick()) {
                    this.foxType = null;
                } else {
                    this.foxType = NextOptions.getNextFoxType(foxType);
                }
                customMob.getCustomMobConfiguration().setFoxType(foxType);
                return getOptionItemStack(getFoxTypeItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getFoxTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.FOX_SPAWN_EGG);
        item.setName("&b&lFox type");
        String type = foxType == null ? "&fBiome based" : "&f" + foxType.toString();
        item.addLore("&eType: &f" + type);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "FoxType");
        return item;
    }
}
