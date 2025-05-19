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

public class Rabbit extends CustomMobOption {
    private static final String RABBIT_TYPE = "mob.rabbit-type";
    private org.bukkit.entity.Rabbit.Type rabbitType;

    public Rabbit(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.rabbitType = rabbitType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Rabbit))
            return;

        if(rabbitType != null)
            ((org.bukkit.entity.Rabbit) customMob).setRabbitType(rabbitType);
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
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
                    List<org.bukkit.entity.Rabbit.Type> rabbitTypes = Arrays.asList(org.bukkit.entity.Rabbit.Type.values());

                    if (rabbitTypes.indexOf(rabbitType) == rabbitTypes.size() - 1)
                        this.rabbitType = rabbitTypes.get(0);
                    else
                        this.rabbitType = rabbitTypes.get(rabbitTypes.indexOf(rabbitType) + 1);
                }
                customMob.getCustomMobConfiguration().setRabbitType(rabbitType);
                return getOptionItemStack(getRabbitTypeItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    public CustomMobsItem getRabbitTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.RABBIT_SPAWN_EGG);
        item.setName("&b&lRabbit type");
        String type = rabbitType == null ? "&fRandom" : "&f" + Utils.getSentenceCase(rabbitType.toString());
        item.addLore("&eType: &f" + type);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "RabbitType");
        return item;
    }
}
