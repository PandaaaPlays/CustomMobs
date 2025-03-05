package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Villager extends CustomMobOption {
    private org.bukkit.entity.Villager.Type villagerType;
    private org.bukkit.entity.Villager.Profession villagerProfession;

    public Villager(org.bukkit.entity.Villager.Type villagerType, org.bukkit.entity.Villager.Profession villagerProfession) {
        this.villagerType = villagerType;
        this.villagerProfession = villagerProfession;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Villager))
            return;

        if(villagerType != null)
            ((org.bukkit.entity.Villager) customMob).setVillagerType(villagerType);
        if(villagerProfession != null) {
            ((org.bukkit.entity.Villager) customMob).setProfession(villagerProfession);
            ((org.bukkit.entity.Villager) customMob).setVillagerExperience(1);
        }
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getVillagerTypeItem(), true, true));
        items.add(getOptionItemStack(getVillagerProfessionItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "villagertype": {
                if(clickType.isRightClick()) {
                    this.villagerType = null;
                } else {
                    List<org.bukkit.entity.Villager.Type> villagerTypes = Registry.VILLAGER_TYPE.stream().toList();

                    if (villagerTypes.indexOf(villagerType) == villagerTypes.size() - 1)
                        this.villagerType = villagerTypes.get(0);
                    else
                        this.villagerType = villagerTypes.get(villagerTypes.indexOf(villagerType) + 1);
                }
                customMob.getCustomMobConfiguration().setVillagerType(villagerType);
                return getOptionItemStack(getVillagerTypeItem(), true, true);
            }

            case "villagerprofession": {
                if(clickType.isRightClick()) {
                    this.villagerProfession = org.bukkit.entity.Villager.Profession.NONE;
                } else {
                    List<org.bukkit.entity.Villager.Profession> villagerProfessions = Registry.VILLAGER_PROFESSION.stream().toList();

                    if (villagerProfessions.indexOf(villagerProfession) == villagerProfessions.size() - 1)
                        this.villagerProfession = villagerProfessions.get(0);
                    else
                        this.villagerProfession = villagerProfessions.get(villagerProfessions.indexOf(villagerProfession) + 1);
                }
                customMob.getCustomMobConfiguration().setVillagerProfession(villagerProfession);
                return getOptionItemStack(getVillagerProfessionItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getVillagerTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.VILLAGER_SPAWN_EGG);
        item.setName("&b&lVillager type");
        String type = villagerType == null ? "Biome based" : Utils.getSentenceCase(villagerType.toString());
        item.addLore("&eType: &f" + type);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "VillagerType");
        return item;
    }

    public CustomMobsItem getVillagerProfessionItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BOOK);
        item.setName("&b&lVillager profession");
        String profession = villagerProfession == null ? "&fRandom" : "&f" + Utils.getSentenceCase(villagerProfession.toString());
        item.addLore("&eProfession: &f" + profession);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "VillagerProfession");
        return item;
    }
}
