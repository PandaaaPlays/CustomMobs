package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Axolotl extends CustomMobOption {
    private org.bukkit.entity.Axolotl.Variant axolotlVariant;

    public Axolotl(org.bukkit.entity.Axolotl.Variant axolotlVariant) {
        this.axolotlVariant = axolotlVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Axolotl))
            return;

        if(axolotlVariant != null)
            ((org.bukkit.entity.Axolotl) customMob).setVariant(axolotlVariant);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getAxolotlVariantItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "axolotlvariant": {
                if (clickType.isRightClick()) {
                    this.axolotlVariant = null;
                } else {
                    this.axolotlVariant = NextOptions.getNextAxolotlVariant(axolotlVariant);
                }
                customMob.getCustomMobConfiguration().setAxolotlVariant(axolotlVariant);
                return getOptionItemStack(getAxolotlVariantItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getAxolotlVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.AXOLOTL_BUCKET);
        item.setName("&b&lAxolotl variant");
        String variant = axolotlVariant == null ? "&fRandom" : "&f" + Utils.getSentenceCase(axolotlVariant.toString());
        item.addLore("&eVariant: &f" + variant);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "AxolotlVariant");
        return item;
    }
}
