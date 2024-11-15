package ca.pandaaa.custommobs.guis.EditCustomMobs.Potions;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.DropConditions;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class SpecificPotionGUI extends CustomMobsGUI {
    /*private final CustomMob customMob;

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
    private final PotionEffect potionEffect;
    private final ItemStack itemstack;
    private final CustomMob customMob;
    private final int potionIndex;
    public SpecificPotionGUI(CustomMob customMob, ItemMeta itemMeta){
        super(36, "&8CustomMobs &8&l» &8Potion configuration");
        this.customMob = customMob;
        customMob.getPotionMeta().getCustomEffects().contains()
        customMob.getPotionMeta().getCustomEffects()
        potionEffect = itemStack.getItemMeta();
        this.potionEffect = customMob.getPotionMeta().getCustomEffects().get(potionIndex);
    }
    public void openInventory(Player player) {
        for(int i = 0; i < 36; i++)
            inventory.setItem(i, filler);

        inventory.setItem(10, getParticlesItem());
        inventory.setItem(11, getAmplifierItem());
        inventory.setItem(12, getDurationItem());
        inventory.setItem(14, getAmbientItem());
        inventory.setItem(27, getPreviousItem());
        inventory.setItem(31, getPotionItem());
        inventory.setItem(35, getDeleteItem(false));

        player.openInventory(inventory);
    }
    private ItemStack getParticlesItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TIPPED_ARROW);
        item.setName("&d&lParticles");
        item.addLore("&eParticles are set to:&f " + potionEffect.hasParticles() + "%", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }
    private ItemStack getDurationItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&d&lDuration");
        item.addLore("&eCurrent duration:&f " + potionEffect.getDuration() + "%", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    } private ItemStack getAmplifierItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GLOWSTONE_DUST);
        item.setName("&d&lAmplifier");
        item.addLore("&eAmplifier is set to:&f " + potionEffect.getAmplifier() + "%", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }
    private ItemStack getAmbientItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DIRT);
        item.setName("&d&lAmbient");
        item.addLore("&eAmbient is set to:&f " + potionEffect.isAmbient() + "%", "", "&7&o(( Click to edit this option ))");
        return getMenuItem(item, true);
    }
    private ItemStack getPotionItem() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

        ItemStack item = drop.getItemStack().clone();
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Drag & drop an item to change this item ))"));
        lore.add(Utils.applyFormat("&7&o(( Left-Click to get the current item ))"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return getMenuItem(item, false);
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
