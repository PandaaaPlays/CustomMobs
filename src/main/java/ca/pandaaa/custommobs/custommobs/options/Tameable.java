package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.PlayerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tameable extends CustomMobOption {
    private UUID owner;
    private boolean tamed;

    public Tameable(UUID owner, boolean tamed) {
        this.owner = owner;
        this.tamed = tamed;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Tameable))
            return;

        ((org.bukkit.entity.Tameable) customMob).setTamed(tamed);
        if(owner != null)
            ((org.bukkit.entity.Tameable) customMob).setOwner(Bukkit.getPlayer(owner));
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getOwnerItem(), true, false));
        items.add(getOptionItemStack(getTamedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "owner": {
                if(clickType.isRightClick()) {
                    this.owner = null;
                    customMob.getCustomMobConfiguration().setOwner(null);
                    return getOptionItemStack(getOwnerItem(), true, false);
                } else {
                    new PlayerGUI("Select player", (value) -> {
                        if(value != null) {
                            this.owner = value.getUniqueId();
                            customMob.getCustomMobConfiguration().setOwner(owner);
                        }
                        new OptionsGUI(customMob).openInventory((Player) clicker, 1);
                    }).openInventory(clicker, 1);
                }
            }

            case "tamed": {
                this.tamed = !tamed;
                customMob.getCustomMobConfiguration().setTamed(tamed);
                return getOptionItemStack(getTamedItem(), false, false);
            }

        }
        return null;
    }

    public CustomMobsItem getOwnerItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PLAYER_HEAD);
        String owner = this.owner == null ? "&fNone" : "&f" + Bukkit.getOfflinePlayer(this.owner).getName();
        item.setName("&3&lOwner");
        item.addLore("&eOwner: " + owner);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Owner");
        return item;
    }

    public CustomMobsItem getTamedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.APPLE);
        String tamed = this.tamed ? "&a&lOn" : "&c&lOff";
        item.setName("&a&lTamed");
        item.addLore("&eTamed: " + tamed);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Tamed");
        return item;
    }
}
