package ca.pandaaa.custommobs.guis.EditCustomMobs.Particles;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Particles.CustomMobParticle;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OthersGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ParticlesGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private final Player player;

    public ParticlesGUI(CustomMob customMob, Player player) {
        super(18, "&8Particles &8&l» &8Spawn / Death");
        this.customMob = customMob;
        this.player = player;
    }

    public void openInventory() {
        List<ItemStack> particleItems = getParticleItems();
        for (int i = 0; i < particleItems.size(); i++)
            inventory.setItem(i, particleItems.get(i));
        for (int i = particleItems.size(); i < 9; i++)
            inventory.setItem(i, new ItemStack(Material.AIR));

        for (int i = 9; i < 18; i++)
            inventory.setItem(i, filler);

        ItemStack addParticle = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
        ItemMeta addItemMeta = addParticle.getItemMeta();
        if (addItemMeta != null) {
            addItemMeta.setDisplayName(Utils.applyFormat("&a&l[+] Add particle"));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Click to add a new particle ))"));
            addItemMeta.setLore(lore);
            addParticle.setItemMeta(addItemMeta);
        }

        inventory.setItem(9, getPreviousItem());
        inventory.setItem(13, addParticle);

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
            case 9:
                new OthersGUI(customMob).openInventory(clicker);
                break;
            case 13:
                new ParticleSelectionGUI(customMob, player).openInventory();
                break;
            default:
                if (event.getSlot() < 9) {
                    NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Particle.Remove.Confirm");
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key)) {
                        if (event.isRightClick()) {
                            customMob.removeParticle(event.getSlot());
                            openInventory();
                        } else {
                            openInventory();
                        }

                    } else {
                        if (event.isRightClick()) {
                            event.getInventory().setItem(event.getSlot(),
                                    getMenuItem(getDeleteItem(new ItemStack(Material.BARRIER)), true));
                        } else {
                            if (event.isShiftClick()) {
                                CustomMobParticle particle = customMob.getParticles().get(event.getSlot());
                                particle.setOnDeath(!particle.isOnDeath());
                                customMob.editParticle(event.getSlot(), particle);
                                openInventory();
                            } else {
                                new EditParticleGUI(customMob, event.getSlot(), clicker).openInventory();
                            }
                        }
                    }
                }
                break;
        }
    }

    private List<ItemStack> getParticleItems() {
        List<ItemStack> particleItems = new ArrayList<>();
        for (CustomMobParticle particle : customMob.getParticles()) {
            particleItems.add(getMenuItem(particle.getItemStack(), true));
        }
        return particleItems;
    }

    private ItemStack getDeleteItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Particle.Remove.Confirm");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Confirm particle deletion"));
        lore.add(Utils.applyFormat("&7&o(( Left-click to cancel the deletion ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-click again to confirm the deletion ))"));
        lore.add(Utils.applyFormat("&c&l[!] &cThis will permanently delete this particle."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return getMenuItem(item, true);
    }
}
