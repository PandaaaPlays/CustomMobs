package ca.pandaaa.custommobs.custommobs.CustomEffects;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects.CustomEffectOptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobSpawner extends CustomMobCustomEffect {

    /**
     * Specifies the CustomMob that should be used to create the entities spawned around the CustomMob.
     */
    private static final String CUSTOMMOB = "custom-effects.mobspawner.custommob";
    private CustomMob spawnedCustomMob;

    /**
     * Specifies how many CustomMob(s) will be spawned around the CustomMob when this custom effect
     * triggers.
     * @minimum 1
     * @maximum 25
     */
    private static final String SPAWNED_AMOUNT = "custom-effects.mobspawner.spawned-amount";
    private int spawnedAmount;

    public MobSpawner(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration, CustomEffectType.COOLDOWN);
        spawnedCustomMob = getCustomEffectOption(CUSTOMMOB, CustomMob.class);
        spawnedAmount = getCustomEffectOption(SPAWNED_AMOUNT, Integer.class, 3);
    }

    public void triggerCustomEffect(Entity entity) {
        for(int i = 0; i < spawnedAmount; i++) {
            Random random = new Random();
            double angle = 2 * Math.PI * random.nextDouble();
            double distance = 4 + (6 - 4) * random.nextDouble();
            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;
            spawnedCustomMob.spawnCustomMob(new Location(
                    entity.getWorld(),
                    entity.getLocation().getX() + offsetX,
                    entity.getLocation().getY(),
                    entity.getLocation().getZ() + offsetZ));
        }
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "custommob": {
                if(clickType.isRightClick()) {
                    spawnedCustomMob = null;
                    setCustomEffectOption(CUSTOMMOB, null);
                } else {
                    new CustomMobsGUI("Select CustomMob", (value) -> {
                        if(value != null) {
                            this.spawnedCustomMob = value;
                            setCustomEffectOption(CUSTOMMOB, this.spawnedCustomMob.getCustomMobFileName().replaceAll(".yml", ""));
                        }
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, 1);
                }
                return getCustomEffectOptionItemStack(getCustomMobItem(), true);
            }
            case "spawnedamount": {
                if(clickType.isRightClick()) {
                    spawnedAmount = 3;
                    setCustomEffectOption(SPAWNED_AMOUNT, this.spawnedAmount);
                } else {
                    new IntegerGUI("Spawned amount", false, 1, 25, (value) -> {
                        this.spawnedAmount = value;
                        setCustomEffectOption(SPAWNED_AMOUNT, this.spawnedAmount);
                        new CustomEffectOptionsGUI(customMob, this, getOptionsItems()).openInventory(clicker);
                    }).openInventory(clicker, spawnedAmount);
                }
                return getCustomEffectOptionItemStack(getSpawnedAmountItem(), true);
            }
            default:
                return handleMessageOption(clicker, customMob, option, clickType);
        }
    }

    public ItemStack getCustomEffectItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPAWNER);
        item.setName("&6&lMob spawner");
        String status = this.enabled ? "&a&lOn" : "&c&lOff";
        item.addLore("&eMob spawner: &f" + status);
        item.addLore("&bType: &f" + Utils.getSentenceCase(this.customEffectType.name()));
        item.addLore("", "&7&o(( Spawns the specified custom mob(s) around the CustomMob ))");
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName());
        return getCustomEffectItemStack(item, this.getClass().getSimpleName());
    }

    public List<ItemStack> getOptionsItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getCustomEffectOptionItemStack(getCustomMobItem(), true));
        items.add(getCustomEffectOptionItemStack(getSpawnedAmountItem(), true));
        items.add(getMessageItem());
        return items;
    }

    private CustomMobsItem getCustomMobItem() {
        CustomMobsItem item = new CustomMobsItem(Material.END_CRYSTAL);
        item.setName("&c&lCustom mob");
        item.addLore("&eSelected custom mob: &f" + (spawnedCustomMob == null ? "None" : spawnedCustomMob.getName()));
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".CustomMob");
        return item;
    }

    private CustomMobsItem getSpawnedAmountItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ECHO_SHARD);
        item.setName("&2&lSpawned amount");
        item.addLore("&eSpawned custom mob(s): &f" + spawnedAmount);
        item.setCustomEffectPersistentDataContainer(this.getClass().getSimpleName() + ".SpawnedAmount");
        return item;
    }
}
