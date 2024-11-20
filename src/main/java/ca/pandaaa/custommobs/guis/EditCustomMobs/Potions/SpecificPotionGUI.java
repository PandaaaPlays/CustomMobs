package ca.pandaaa.custommobs.guis.EditCustomMobs.Potions;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.options.NextOptions;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.Drops.DropsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.Drops.SpecificDropChanceGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.Drops.SpecificDropMessageGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.DropConditions;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpecificPotionGUI extends CustomMobsGUI{

/*
    private final ItemStack yes;
    private final ItemStack no;
    private final ItemStack minusBig;
    private final ItemStack plusBig;
    private final ItemStack plusSmall;
    private final ItemStack minusSmall;
    private final ItemStack confirm;
    private final ItemStack confirm;
    private final ItemStack particle;

    private final boolean ambient;
    private final int amplifier;
    private final int duration;

    private final ItemStack particle;*/
    private final CustomMob customMob;
    private PotionEffect potionEffect;
    private PotionMeta emptyPotionMeta;
    private final int potionIndex;

    private PotionEffectType type;
    private boolean particles;
    private boolean ambient;
    private int duration;
    private int amplifier;


    public SpecificPotionGUI(CustomMob customMob, int potionIndex){
        super(36, "&8CustomMobs &8&l» &8Potion configuration");
        this.potionIndex = potionIndex;
        this.customMob = customMob;
        this.emptyPotionMeta = (PotionMeta) new ItemStack(Material.POTION).getItemMeta();
        this.potionEffect = customMob.getPotionMeta().get(potionIndex).getCustomEffects().get(0);

        this.type = potionEffect.getType();
        this.particles = potionEffect.hasParticles();
        this.ambient = potionEffect.isAmbient();
        this.duration = potionEffect.getDuration()/20;
        this.amplifier = potionEffect.getAmplifier();

    }
    public void openInventory(Player player) {

        for(int i = 0; i < 36; i++)
            inventory.setItem(i, filler);

        inventory.setItem(11, getParticlesItem());
        inventory.setItem(12, getAmplifierItem());
        inventory.setItem(14, getDurationItem());
        inventory.setItem(15, getAmbientItem());
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
        ItemStack cursorItem = clicker.getItemOnCursor();
        PotionMeta potionMeta = emptyPotionMeta.clone();
        switch (event.getSlot()) {
            case 11:
                particles = !particles;
                event.getView().setItem(event.getSlot(), getParticlesItem());
                update(potionMeta);
                break;
            case 14:
                new SpecificPotionDurationGUI(customMob, duration, potionIndex, (value) -> {
                    Bukkit.broadcastMessage(value + " seconde(s) // " + value * 20 + " tick(s)");
                    duration = value;
                    update(potionMeta);
                }).openInventory(clicker);
                event.getView().setItem(event.getSlot(), getDurationItem());
                break;
            case 15:
                ambient = !ambient;
                event.getView().setItem(event.getSlot(), getAmbientItem());
                update(potionMeta);
                break;
            case 31:
                new PotionsGUI(customMob).openInventory(clicker, 1);

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
    private void update(PotionMeta potionMeta){
        potionMeta.addCustomEffect(new PotionEffect(type, duration*20, amplifier, ambient, particles),true);
        customMob.editPotion(potionMeta,potionIndex);
    }
    private ItemStack getParticlesItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TIPPED_ARROW);
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1, 1), true);
        item.setItemMeta(potionMeta);
        item.setName("&d&lParticles");
        item.addLore("&eParticles are set to:&f " + particles + " ","&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }
    private ItemStack getDurationItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&d&lDuration");
        if(duration <0)
            item.addLore("&eCurrent duration:&f infinity"+ " "," &7&o(( Click to edit this option ))");
        else
            item.addLore("&eCurrent duration:&f " + SpecificPotionDurationGUI.getFormattedSize(duration) + " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    } private ItemStack getAmplifierItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GLOWSTONE_DUST);
        item.setName("&d&lAmplifier");
        item.addLore("&eAmplifier is set to:&f " + amplifier + " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }
    private ItemStack getAmbientItem() {
        CustomMobsItem item = new CustomMobsItem(Material.COPPER_GRATE);
        item.setName("&d&lAmbient");
        item.addLore("&eAmbient is set to:&f " + ambient +  " ", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }
    private ItemStack getPotionItem() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta1 = (PotionMeta) potion.getItemMeta();
        potionMeta1.addCustomEffect(new PotionEffect(type,1 ,1),true);

        List<String> lore = potionMeta1.getLore() == null ? new ArrayList<>() : potionMeta1.getLore();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Left-Click to change the current item ))"));
        potionMeta1.setLore(lore);
        potion.setItemMeta(potionMeta1);
        return potion;
    }
    private ItemStack getDeleteItem(boolean confirm) {
        CustomMobsItem item = new CustomMobsItem(Material.BARRIER);
        item.addLore("");
        if (confirm) {
            item.setName("&c&l[-] Confirm drop deletion");
            item.addLore("&7&o(( Click again to confirm the deletion ))");
        } else
            item.setName("&c&l[-] Delete drop");
        item.addLore("&c&l[!] &cThis will permanently delete this drop.");
        return getMenuItem(item, true);
    }


}
