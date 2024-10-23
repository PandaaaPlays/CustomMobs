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

public class ChestedHorse extends CustomMobType {
    private boolean chested;

    public ChestedHorse(boolean chested) {
        this.chested = chested;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.ChestedHorse))
            return;

        ((org.bukkit.entity.ChestedHorse) customMob).setCarryingChest(chested);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getChestedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "chested": {
                this.chested = !chested;
                customMob.getCustomMobConfiguration().setHasChest(chested);
                return getOptionItemStack(getChestedItem(), false, false);
            }
        }
        return null;
    }

    public CustomMobsItem getChestedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CHEST);
        String chested = this.chested ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lChested");
        item.addLore("&eChested: " + chested);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "chested");
        return item;
    }
}
