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

public class Llama extends CustomMobOption {
    private static final String LLAMA_COLOR = "mob.llama-color";
    private org.bukkit.entity.Llama.Color llamaColor;

    public Llama(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.llamaColor = llamaColor;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Llama))
            return;

        if(llamaColor != null)
            ((org.bukkit.entity.Llama) customMob).setColor(llamaColor);
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getLlamaColorItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "llamacolor": {
                if (clickType.isRightClick()) {
                    this.llamaColor = null;
                } else {
                    List<org.bukkit.entity.Llama.Color> llamaColors = Arrays.asList(org.bukkit.entity.Llama.Color.values());

                    if (llamaColors.indexOf(llamaColor) == llamaColors.size() - 1)
                        this.llamaColor = llamaColors.get(0);
                    else
                        this.llamaColor = llamaColors.get(llamaColors.indexOf(llamaColor) + 1);
                }
                customMob.getCustomMobConfiguration().setLlamaColor(llamaColor);
                return getOptionItemStack(getLlamaColorItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    public CustomMobsItem getLlamaColorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&b&lLlama color");
        String color = llamaColor == null ? "&fRandom" : Utils.getChatColorOfColor(llamaColor.name()) + Utils.getSentenceCase(llamaColor.name());
        item.addLore("&eColor: " + color);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "LlamaColor");
        return item;
    }

}
