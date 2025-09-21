package ca.pandaaa.custommobs.guis.EditCustomMobs.Potions;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.PotionEffect;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpecificPotionGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private final int potionIndex;
    private PotionEffectType type;
    private boolean particles;
    private boolean ambient;
    private int duration;
    private int amplifier;


    public SpecificPotionGUI(CustomMob customMob, int potionIndex) {
        super(36, "&8CustomMobs &8&l» &8Potion configuration");
        this.potionIndex = potionIndex;
        this.customMob = customMob;

        PotionEffect potionEffect = customMob.getPotionEffects().get(potionIndex);
        this.type = potionEffect.getType();
        this.particles = potionEffect.hasParticles();
        this.ambient = potionEffect.isAmbient();
        this.duration = potionEffect.getDuration();
        this.amplifier = potionEffect.getAmplifier();

    }

    public void openInventory(Player player) {

        for(int i = 0; i < 36; i++)
            inventory.setItem(i, filler);

        inventory.setItem(11, getAmplifierItem());
        inventory.setItem(12, getDurationItem());
        inventory.setItem(14, getAmbientItem());
        inventory.setItem(15, getParticlesItem());
        inventory.setItem(27, getPreviousItem());
        inventory.setItem(31, getPotionItem());
        inventory.setItem(35, getDeleteItem(false));

        player.openInventory(inventory);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory()))
            return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null)
            return;
        Player clicker = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            // Amplifier
            case 11:
                new IntegerGUI("Amplifier", false, 1, 255, (value) -> {
                    this.amplifier = value;
                    customMob.editPotion(new PotionEffect(type, duration, amplifier, ambient, particles), potionIndex);
                    new SpecificPotionGUI(customMob, potionIndex).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, amplifier);
                break;
            // Duration
            case 12:
                if (event.getClick().isRightClick()) {
                    duration = 0;
                    event.getView().setItem(event.getSlot(), getDurationItem());
                    customMob.editPotion(new PotionEffect(type, duration, amplifier, ambient, particles), potionIndex);
                } else {
                    new SpecificPotionDurationGUI(duration, (value) -> {
                        duration = value;
                        customMob.editPotion(new PotionEffect(type, duration, amplifier, ambient, particles), potionIndex);
                        new SpecificPotionGUI(customMob, potionIndex).openInventory((Player) event.getWhoClicked());
                    }).openInventory(clicker);
                }
                break;
            // Ambient
            case 14:
                ambient = !ambient;
                event.getView().setItem(event.getSlot(), getAmbientItem());
                customMob.editPotion(new PotionEffect(type, duration, amplifier, ambient, particles), potionIndex);
                break;
            // Particles
            case 15:
                particles = !particles;
                event.getView().setItem(event.getSlot(), getParticlesItem());
                customMob.editPotion(new PotionEffect(type, duration, amplifier, ambient, particles), potionIndex);
                break;
            // Previous page
            case 27:
                new PotionsGUI(customMob).openInventory(clicker, 1);
                break;
            // Edit potion
            case 31:
                new PotionEffectsGUI(customMob, (value) -> {
                    if(value != null) {
                        type = value.getType();
                        customMob.editPotion(new PotionEffect(type, duration, amplifier, ambient, particles), potionIndex);
                    }
                    new SpecificPotionGUI(customMob, potionIndex).openInventory((Player) event.getWhoClicked());
                }).openInventory(clicker, 1);
                break;
            case 35:
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Confirm")) {
                    customMob.removePotionItem(potionIndex);
                    new PotionsGUI(customMob).openInventory(clicker, 1);
                } else {
                    inventory.setItem(35, getDeleteItem(true));

                    for(int i = 0; i < 36; i++) {
                        if (inventory.getItem(i).getType() == Material.GRAY_STAINED_GLASS_PANE)
                            inventory.getItem(i).setType(Material.RED_STAINED_GLASS_PANE);
                    }
                }
                break;
        }

    }

    private ItemStack getAmplifierItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GLOWSTONE_DUST);
        item.setName("&6&lAmplifier");
        item.addLore("&eAmplifier:&f " + amplifier, "", "&7&o(( The potion effect level ))", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getDurationItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&b&lDuration");
        String duration = this.duration <= 0 ? "Infinite" : Utils.getFormattedTime(this.duration, false, true);
        item.addLore("&eCurrent duration:&f " + duration, " ", "&7&o(( Click to edit this option ))", "&7&o(( Right-Click to reset this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getAmbientItem() {
        CustomMobsItem item = new CustomMobsItem(Material.COPPER_GRATE);
        item.setName("&a&lAmbient");
        String ambient = this.ambient ? "&a&lOn" : "&c&lOff";
        item.addLore("&eAmbient:&f " + ambient, "", "&7&o(( Ambient potion particles are translucent ))", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getParticlesItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TIPPED_ARROW);
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1, 1), true);
        item.setItemMeta(potionMeta);
        item.setName("&d&lParticles");
        String particles = this.particles ? "&a&lOn" : "&c&lOff";
        item.addLore("&eParticles:&f " + particles, " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }

    private ItemStack getPotionItem() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        if(type == PotionEffectType.ABSORPTION)
            potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.STRENGTH,1 ,1),true);
        else
            potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(type,1 ,1), true);
        potionMeta.setDisplayName(Utils.applyFormat("&6&l" + Utils.getStartCase(type.getKey().getKey())));
        List<String> lore = potionMeta.getLore() == null ? new ArrayList<>() : potionMeta.getLore();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to change the current potion effect ))"));
        potionMeta.setLore(lore);
        potion.setItemMeta(potionMeta);
        return getMenuItem(potion, true);
    }

    private ItemStack getDeleteItem(boolean confirm) {
        CustomMobsItem item = new CustomMobsItem(Material.BARRIER);
        item.addLore("");
        if (confirm) {
            item.setName("&c&l[-] Confirm potion deletion");
            item.addLore("&7&o(( Click again to confirm the deletion ))");
        } else
            item.setName("&c&l[-] Delete potion");
        item.addLore("&c&l[!] &cThis will permanently delete this potion.");
        return getMenuItem(item, true);
    }


}
