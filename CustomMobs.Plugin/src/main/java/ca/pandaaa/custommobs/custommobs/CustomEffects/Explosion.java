package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Explosion extends CustomMobCustomEffect {

    /**
     * Determines the strength (size) of the explosion created by the CustomMob upon trigger of this
     * custom effect.
     * @minimum 1
     * @maximum 100
     */
    private static final String EXPLOSION_STRENGTH = "custom-effects.explosion.explosion-strength";
    private int explosionStrength;

    /**
     * Determines whether the custom effect's explosion should damage the CustomMob or not.
     */
    private static final String DAMAGE_SELF = "custom-effects.explosion.damage-self";
    private boolean damageSelf;

    /**
     * Determines whether the custom effect's explosion should break blocks or not.
     */
    private static final String BREAK_BLOCKS = "custom-effects.explosion.break-blocks";
    private boolean breakBlocks;

    public Explosion(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.COOLDOWN);
        this.explosionStrength = getCustomEffectOption(EXPLOSION_STRENGTH, Integer.class, 5);
        this.damageSelf = getCustomEffectOption(DAMAGE_SELF, Boolean.class, false);
        this.breakBlocks = getCustomEffectOption(BREAK_BLOCKS, Boolean.class, false);
    }

    public void triggerCustomEffect(Entity entity) {
        boolean invulnerable = entity.isInvulnerable();

        if(!damageSelf)
            entity.setInvulnerable(true);

        entity.getWorld().createExplosion(entity.getLocation().add(0, 0.5, 0), explosionStrength, false, breakBlocks);
        entity.setInvulnerable(invulnerable);
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "explosionstrength": {
                if(clickType.isRightClick()) {
                    this.explosionStrength = 5;
                    setCustomEffectOption(EXPLOSION_STRENGTH, this.explosionStrength);
                } else {
                    new IntegerGUI("Explosion strength", false, 1, 100, (value) -> {
                        this.explosionStrength = value;
                        setCustomEffectOption(EXPLOSION_STRENGTH, this.explosionStrength);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, explosionStrength);
                }
                return getCustomEffectOptionItemStack(getExplosionStrengthItem(), true);
            }
            case "damageself": {
                this.damageSelf = !this.damageSelf;
                setCustomEffectOption(DAMAGE_SELF, this.damageSelf);
                return getCustomEffectOptionItemStack(getDamageSelfItem(), false);
            }
            case "breakblocks": {
                this.breakBlocks = !this.breakBlocks;
                setCustomEffectOption(BREAK_BLOCKS, this.breakBlocks);
                return getCustomEffectOptionItemStack(getBreakBlocksItem(), false);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TNT);
        item.setName("&6&lExplosion");
        String explosion = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eExplosion: &f" + explosion);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( CustomMob creates an explosion ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getExplosionStrengthItem(), true));
        items.add(getCustomEffectOptionItemStack(getDamageSelfItem(), false));
        items.add(getCustomEffectOptionItemStack(getBreakBlocksItem(), false));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getExplosionStrengthItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TNT);
        item.setName("&c&lExplosion strength");
        item.addLore("&eRadius: &f" + explosionStrength);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".ExplosionStrength");
        return item;
    }

    private CustomMobsItem getDamageSelfItem() {
        CustomMobsItem item = new CustomMobsItem(Material.NETHER_STAR);
        item.setName("&b&lDamage self");
        item.addLore("&eDamage itself from explosion: &f" + (this.damageSelf ? "&a&lOn" : "&c&lOff"));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".DamageSelf");
        return item;
    }

    private CustomMobsItem getBreakBlocksItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_PICKAXE);
        item.setName("&2&lBreak blocks");
        item.addLore("&eBreak blocks: &f" + (this.breakBlocks ? "&a&lOn" : "&c&lOff"));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".BreakBlocks");
        return item;
    }
}
