package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Vindicator extends CustomMobOption {
    private static final String JOHNNY_VANDICATOR = "mob.johnny-vandicator";
    private boolean johnny;

    public Vindicator(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.johnny = johnny;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Vindicator))
            return;

        ((org.bukkit.entity.Vindicator) customMob).setJohnny(johnny);
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getJohnnyItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch (option.toLowerCase()) {
            case "johnny": {
                this.johnny = !johnny;
                customMob.getCustomMobConfiguration().setJohnnyVindicator(johnny);
                return getOptionItemStack(getJohnnyItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    public CustomMobsItem getJohnnyItem() {
        CustomMobsItem item = new CustomMobsItem(Material.IRON_AXE);
        String johnny = this.johnny ? "&a&lOn" : "&c&lOff";
        item.setName("&4&lJohnny vandicator");
        item.addLore("&eAggressive towards all mobs: " + johnny);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Johnny");
        return item;
    }

}
