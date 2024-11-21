package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Creeper extends CustomMobOption {
    private int explosionCooldown;
    private int explosionRadius;
    private boolean charged;

    public Creeper(int explosionCooldown, int explosionRadius, boolean charged) {
        this.explosionCooldown = explosionCooldown;
        this.explosionRadius = explosionRadius;
        this.charged = charged;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Creeper))
            return;

        ((org.bukkit.entity.Creeper) customMob).setPowered(charged);
        ((org.bukkit.entity.Creeper) customMob).setMaxFuseTicks(explosionCooldown);
        ((org.bukkit.entity.Creeper) customMob).setExplosionRadius(explosionRadius);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getExplosionCooldownItem(), true, false));
        items.add(getOptionItemStack(getExplosionRadiusItem(), true, false));
        items.add(getOptionItemStack(getChargedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            case "explosioncooldown": {
                if(clickType.isRightClick()) {
                    this.explosionCooldown = 30;
                    customMob.getCustomMobConfiguration().setExplosionCooldown(explosionCooldown);
                } else {
                    new IntegerGUI("Explosion cooldown", customMob, false, 0, 1200, (value) -> {
                        this.explosionCooldown = value;
                        customMob.getCustomMobConfiguration().setExplosionCooldown(explosionCooldown);
                    }).openInventory(clicker, explosionCooldown);
                    new OptionsGUI(customMob).openInventory(clicker, 1);
                }
                return getOptionItemStack(getExplosionCooldownItem(), true, false);
            }

            case "explosionradius": {
                if(clickType.isRightClick()) {
                    this.explosionRadius = charged ? 6 : 3;
                    customMob.getCustomMobConfiguration().setExplosionRadius(explosionRadius);
                } else {
                    new IntegerGUI("Explosion radius", customMob, false, 0, 128, (value) -> {
                        this.explosionRadius = value;
                        customMob.getCustomMobConfiguration().setExplosionRadius(explosionRadius);
                    }).openInventory(clicker, explosionRadius);
                    new OptionsGUI(customMob).openInventory(clicker, 1);
                }
                return getOptionItemStack(getExplosionRadiusItem(), true, false);
            }

            case "charged": {
                this.charged = !charged;
                customMob.getCustomMobConfiguration().setChargedCreeper(charged);
                return getOptionItemStack(getChargedItem(), false, false);
            }

        }
        return null;
    }

    public CustomMobsItem getExplosionCooldownItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&c&lExplosion cooldown");
        item.addLore("&eExplosion cooldown: &f" + explosionCooldown + " (" + String.format("%.1f", (double) explosionCooldown / 20) + " sec)");
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "ExplosionCooldown");
        return item;
    }

    public CustomMobsItem getExplosionRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NETHER_STAR);
        item.setName("&c&lExplosion radius");
        item.addLore("&eExplosion radius: &f" + explosionRadius);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "ExplosionRadius");
        return item;
    }

    public CustomMobsItem getChargedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LIGHTNING_ROD);
        String charged = this.charged ? "&a&lOn" : "&c&lOff";
        item.setName("&b&lCharged");
        item.addLore("&eCharged: " + charged);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Charged");
        return item;
    }

}
