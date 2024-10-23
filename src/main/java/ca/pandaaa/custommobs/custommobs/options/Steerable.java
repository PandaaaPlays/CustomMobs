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

public class Steerable extends CustomMobType {
    private boolean saddle;

    public Steerable(boolean saddle) {
        this.saddle = saddle;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Steerable))
            return;

        ((org.bukkit.entity.Steerable) customMob).setSaddle(saddle);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack saddle = new ItemStack(Material.SADDLE);
        ItemMeta saddleMeta = saddle.getItemMeta();
        saddleMeta.setDisplayName(Utils.applyFormat("&6&lSaddle"));
        saddle.setItemMeta(saddleMeta);
        items.add(getOptionItemStack(getSaddleItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "saddle": {
                this.saddle = !this.saddle;
                customMob.getCustomMobConfiguration().setHasSaddle(saddle);
                return getOptionItemStack(getSaddleItem(), false, false);
            }
        }
        return null;
    }

    public CustomMobsItem getSaddleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SADDLE);
        String saddle = this.saddle ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lSaddle");
        item.addLore("&eSaddle: " + saddle);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Saddle");
        return item;
    }
}
