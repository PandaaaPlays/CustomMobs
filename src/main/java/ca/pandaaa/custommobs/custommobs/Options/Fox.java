package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fox extends CustomMobOption {
    /**
     * Specifies the variant of the fox CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal fox).
     */
    private static final String FOX_TYPE = "mob.fox-type";
    private org.bukkit.entity.Fox.Type foxType;

    public Fox(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.foxType = foxType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Fox))
            return;

        if(foxType != null)
            ((org.bukkit.entity.Fox) customMob).setFoxType(foxType);
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
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
                    List<org.bukkit.entity.Fox.Type> foxTypes = Arrays.asList(org.bukkit.entity.Fox.Type.values());

                    if (foxTypes.indexOf(foxType) == foxTypes.size() - 1)
                        this.foxType = foxTypes.get(0);
                    else
                        this.foxType = foxTypes.get(foxTypes.indexOf(foxType) + 1);
                }
                customMob.getCustomMobConfiguration().setFoxType(foxType);
                return getOptionItemStack(getFoxTypeItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    public CustomMobsItem getFoxTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.FOX_SPAWN_EGG);
        item.setName("&b&lFox type");
        String type = foxType == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(foxType.toString());
        item.addLore("&eType: &f" + type);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "FoxType");
        return item;
    }
}
