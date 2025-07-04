package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LightningStrike extends CustomMobCustomEffect {

    /**
     * Determines if the lightning should spawn directly on nearby player(s) or not.
     */
    private static final String ON_PLAYERS = "custom-effects.lightningstrike.on-players";
    private boolean onPlayers;

    /**
     * Determines the radius (in block(s)) around the CustomMob where the effect should create lightning(s).
     *
     * @minimum 1
     * @maximum 32
     */
    private static final String RADIUS = "custom-effects.lightningstrike.radius";
    private int radius;

    /**
     * Determines the amount of lightning that should appear around the CustomMob (will only have an
     * effect if "On players" is disabled.
     * @minimum 1
     * @maximum 50
     */
    private static final String AMOUNT = "custom-effects.lightningstrike.amount";
    private int amount;

    public LightningStrike(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.COOLDOWN);
        onPlayers = getCustomEffectOption(ON_PLAYERS, Boolean.class, false);
        radius = getCustomEffectOption(RADIUS, Integer.class, 25);
        amount = getCustomEffectOption(AMOUNT, Integer.class, 15);
    }

    public void triggerCustomEffect(Entity entity) {
        if(onPlayers) {
            List<Entity> playersAround = entity.getNearbyEntities(radius, radius, radius).stream().filter(e -> e instanceof Player).toList();
            for(Entity player : playersAround) {
                Objects.requireNonNull(player.getLocation().getWorld()).spawnEntity(player.getLocation(), EntityType.LIGHTNING_BOLT);
            }
        } else {
            Random random = new Random();
            for (int i = 0; i < amount; i++) {
                double angle = 2 * Math.PI * random.nextDouble();
                double distance = random.nextDouble() * radius;
                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;
                Location location = entity.getLocation().clone().add(offsetX, 0, offsetZ);
                Objects.requireNonNull(entity.getLocation().getWorld())
                        .spawnEntity(location, EntityType.LIGHTNING_BOLT);
            }
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "onplayers": {
                this.onPlayers = !this.onPlayers;
                setCustomEffectOption(ON_PLAYERS, this.onPlayers);
                // Reopen the gui because we also need to update the "amount" item.
                new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                return getCustomEffectOptionItemStack(getOnPlayersItem(), false);
            }
            case "radius": {
                if(clickType.isRightClick()) {
                    radius = 25;
                    setCustomEffectOption(RADIUS, this.radius);
                } else {
                    new IntegerGUI("Radius", false, 1, 32, (value) -> {
                        this.radius = value;
                        setCustomEffectOption(RADIUS, this.radius);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, radius);
                }
                return getCustomEffectOptionItemStack(getRadiusItem(), true);
            }
            case "amount": {
                if(clickType.isRightClick()) {
                    amount = 15;
                    setCustomEffectOption(AMOUNT, this.amount);
                } else {
                    new IntegerGUI("Amount", false, 1, 50, (value) -> {
                        this.amount = value;
                        setCustomEffectOption(AMOUNT, this.amount);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, amount);
                }
                return getCustomEffectOptionItemStack(getAmountItem(), true);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.LIGHTNING_ROD);
        item.setName("&6&lLightning strike");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eLightning strike: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Strikes players (or blocks) around the CustomMob ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getOnPlayersItem(), false));
        items.add(getCustomEffectOptionItemStack(getRadiusItem(), true));
        items.add(getCustomEffectOptionItemStack(getAmountItem(), true));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getOnPlayersItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PLAYER_HEAD);
        item.setName("&e&lOn players");
        item.addLore("&eStrike players directly: &f" + (this.onPlayers ? "&a&lOn" : "&c&lOff"));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".OnPlayers");
        return item;
    }

    private CustomMobsItem getRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENDER_PEARL);
        item.setName("&a&lRadius");
        item.addLore("&eRadius: &f" + this.radius + " block(s)");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".Radius");
        return item;
    }

    private CustomMobsItem getAmountItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ECHO_SHARD);
        item.setName("&2&lAmount");
        item.addLore("&eAmount: &f" + (this.onPlayers ? "Directly on player(s)" : this.amount + " lightning(s)"));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".Amount");
        return item;
    }
}