package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import ca.pandaaa.custommobs.utils.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class Miner extends CustomMobCustomEffect {
    public final static Map<UUID, Miner> activeMiners = new HashMap<>();

    /**
     * Determines the radius (in block(s)) around the CustomMob where it should detect player(s).
     * @minimum 1
     * @maximum 32
     */
    private static final String RADIUS = "custom-effects.miner.radius";
    private int radius;

    public Miner(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.ALWAYS);
        radius = getCustomEffectOption(RADIUS, Integer.class, 25);
    }

    public void triggerCustomEffect(Entity entity) {
        if (!(entity instanceof org.bukkit.entity.Mob mob)) return;

        Player target = getNearestPlayer(mob, radius);
        if (target == null) return;

        mob.setTarget(target);

        if (!hasClearPath(mob, target)) {
            digTowardsTarget(mob, target);
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch (option.toLowerCase()) {
            case "radius": {
                if (clickType.isRightClick()) {
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
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIAMOND_PICKAXE);
        item.setName("&7&lMiner");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eMiner: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Mines blocks to reach players through walls ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getRadiusItem(), true));
        items.add(getMessageItem().getItem());
        return items;
    }

    private CustomMobsItem getRadiusItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ENDER_PEARL);
        item.setName("&a&lFollow radius");
        item.addLore("&eFollow radius: &f" + radius + " block(s)");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".Radius");
        return item;
    }

    private Player getNearestPlayer(Mob mob, int radius) {
        Player nearest = null;
        double closest = Double.MAX_VALUE;

        for (Entity nearbyEntity : mob.getNearbyEntities(radius, radius, radius)) {
            if (nearbyEntity instanceof Player player) {
                if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE)
                    continue;
                double dist = player.getLocation().distanceSquared(mob.getLocation());
                if (dist < closest) {
                    closest = dist;
                    nearest = player;
                }
            }
        }
        return nearest;
    }

    private boolean hasClearPath(Mob mob, Player target) {
        Location eyeStart = mob.getEyeLocation();
        Location feetStart = mob.getLocation().add(0, 0.1, 0);

        Vector direction = target.getEyeLocation().toVector().subtract(eyeStart.toVector());
        double distance = direction.length();
        if (distance == 0) return true;

        direction.normalize();

        for (double i = 0; i < distance; i += 0.5) {
            Block eyeBlock = eyeStart.clone().add(direction.clone().multiply(i)).getBlock();
            Block feetBlock = feetStart.clone().add(direction.clone().multiply(i)).getBlock();

            if (!eyeBlock.isPassable() || !feetBlock.isPassable()) {
                return false;
            }
        }
        return true;
    }

    private Long cooldown = 0L;
    private void digTowardsTarget(org.bukkit.entity.Mob mob, Player target) {
        long now = System.currentTimeMillis();
        if (now < cooldown) return;
        cooldown = now + 750L;

        Vector direction = target.getEyeLocation().toVector()
                .subtract(mob.getEyeLocation().toVector()).normalize();

        Block headBlock = mob.getEyeLocation().clone().add(direction).getBlock();
        Block feetBlock = mob.getLocation().clone().add(direction).getBlock();

        if (isBlockBreakable(headBlock)) {
            headBlock.breakNaturally();
            return;
        }
        if (isBlockBreakable(feetBlock)) {
            feetBlock.breakNaturally();
        }
    }

    private boolean isBlockBreakable(Block block) {
        if (block == null || block.getType().isAir()) return false;
        if (!WorldGuard.canBreakBlock(block)) return false;

        return switch (block.getType()) {
            case BEDROCK, BARRIER, END_PORTAL_FRAME -> false;
            default -> true;
        };
    }
}