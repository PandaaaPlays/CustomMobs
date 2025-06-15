package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Villager extends CustomMobOption {
    /**
     * Indicates the biome-based appearance of the villager CustomMob (e.g., plains, desert, jungle).
     */
    private static final String VILLAGER_TYPE = "mob.villager-type";
    private org.bukkit.entity.Villager.Type villagerType;
    /**
     * Specifies the CustomMob villager’s job or role (e.g., farmer, librarian, blacksmith).
     */
    private static final String VILLAGER_PROFESSION = "mob.villager-profession";
    private org.bukkit.entity.Villager.Profession villagerProfession;

    public Villager(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.villagerType = getOption(VILLAGER_TYPE, Registry.VILLAGER_TYPE);
        this.villagerProfession = getOption(VILLAGER_PROFESSION, Registry.VILLAGER_PROFESSION);
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

    @Override
    public void resetOptions() {
        setOption(VILLAGER_TYPE, null);
        setOption(VILLAGER_PROFESSION, null);
    }

    public List<ItemStack> getOptionItems() {
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
                setOption(VILLAGER_TYPE, villagerType != null ? villagerType.toString() : null);
                return getOptionItemStack(getVillagerTypeItem(), true, true);
            }

            case "villagerprofession": {
                if(clickType.isRightClick()) {
                    this.villagerProfession = null;
                } else {
                    List<org.bukkit.entity.Villager.Profession> villagerProfessions = Registry.VILLAGER_PROFESSION.stream().toList();

                    if (villagerProfessions.indexOf(villagerProfession) == villagerProfessions.size() - 1)
                        this.villagerProfession = villagerProfessions.get(0);
                    else
                        this.villagerProfession = villagerProfessions.get(villagerProfessions.indexOf(villagerProfession) + 1);
                }
                setOption(VILLAGER_PROFESSION, villagerProfession != null ? villagerProfession.toString() : null);
                return getOptionItemStack(getVillagerProfessionItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Villager.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getVillagerTypeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.VILLAGER_SPAWN_EGG);
        item.setName("&b&lVillager type");
        String type = villagerType == null ? "Biome based" : Utils.getSentenceCase(villagerType.toString());
        item.addLore("&eType: &f" + type);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "VillagerType");
        return item;
    }

    public CustomMobsItem getVillagerProfessionItem() {
        CustomMobsItem item = new CustomMobsItem(Material.BOOK);
        item.setName("&b&lVillager profession");
        String profession = villagerProfession == null ? "&fRandom" : "&f" + Utils.getSentenceCase(villagerProfession.toString());
        item.addLore("&eProfession: &f" + profession);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "VillagerProfession");
        return item;
    }
}
