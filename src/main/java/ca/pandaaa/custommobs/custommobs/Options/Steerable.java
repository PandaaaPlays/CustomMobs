package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Steerable extends CustomMobOption {
    /**
     * Indicates if the entity (like a pig or strider) has a saddle and can be ridden.
     */
    private static final String SADDLE = "mob.saddle";
    private boolean saddle;

    public Steerable(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.saddle = getOption(SADDLE, Boolean.class, false);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Steerable))
            return;

        ((org.bukkit.entity.Steerable) customMob).setSaddle(saddle);
    }

    public void resetOptions() {
        setOption(SADDLE, null);
    }

    public List<ItemStack> getOptionItems() {
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
                setOption(SADDLE, saddle);

                if(saddle) {
                    customMob.addDrop(new Drop(new ItemStack(Material.SADDLE), 1, "Saddle"));
                } else {
                    customMob.removeDropItem("Saddle");
                }

                return getOptionItemStack(getSaddleItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Steerable.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getSaddleItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SADDLE);
        String saddle = this.saddle ? "&a&lOn" : "&c&lOff";
        item.setName("&6&lSaddle");
        item.addLore("&eSaddle: " + saddle);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Saddle");
        return item;
    }
}
