package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
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

public class Phantom extends CustomMobOption {
    private int size;

    public Phantom(int size) {
        this.size = size;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Phantom))
            return;

        ((org.bukkit.entity.Phantom) customMob).setSize(size);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        // TODO other mobs scaling?

        ItemStack phantomSize = new ItemStack(Material.PHANTOM_MEMBRANE);
        ItemMeta phantomSizeMeta = phantomSize.getItemMeta();
        phantomSizeMeta.setDisplayName(Utils.applyFormat("&a&lPhantom size"));
        phantomSize.setItemMeta(phantomSizeMeta);
        items.add(getOptionItemStack(getPhantomSizeItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "phantomsize": {
                if (clickType.isRightClick()) {
                    this.size = 0;
                    customMob.getCustomMobConfiguration().setPhantomSize(size);
                } else {
                    new IntegerGUI("Phantom size", customMob, false, 0, 64, (value) -> {
                        this.size = value;
                        customMob.getCustomMobConfiguration().setPhantomSize(size);
                    }).openInventory(clicker, size);
                }
                return getOptionItemStack(getPhantomSizeItem(), true, false);
            }
        }
        return null;
    }

    public CustomMobsItem getPhantomSizeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PHANTOM_MEMBRANE);
        item.setName("&a&lPhantom size");
        item.addLore("&ePhantom size: &f" + size);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "PhantomSize");
        return item;
    }
}
