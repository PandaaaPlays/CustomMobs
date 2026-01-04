package ca.pandaaa.custommobs.guis.EditCustomMobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Particles.CustomMobParticle;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticleSelectionGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private final Player player;
    private final Map<String, Class<?>> availableParticles;

    public ParticleSelectionGUI(CustomMob customMob, Player player) {
        super(54, "&8Particles &8&l» Selection");
        this.customMob = customMob;
        this.player = player;
        this.availableParticles = customMob.getCustomMobConfiguration().getParticleClasses();
    }

    public void openInventory() {
        int index = 0;
        for (Map.Entry<String, Class<?>> entry : availableParticles.entrySet()) {
            if (index >= 45)
                break;
            try {
                // Instantiate to get the item/name
                CustomMobParticle tempInstance = (CustomMobParticle) entry.getValue().getDeclaredConstructor().newInstance();

                CustomMobsItem item = new CustomMobsItem(tempInstance.getMaterial());
                item.setName("&6&l" + tempInstance.getName());
                item.addLore("", "&7&o(( Click to select this particle ))");
                inventory.setItem(index, getMenuItem(item, true));
                index++;
            } catch (Exception e) {
                CustomMobs.getPlugin().getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Error displaying particle selection for " + entry.getKey());
            }
        }

        inventory.setItem(45, getPreviousItem());

        // Fill the rest
        for (int i = index; i < 45; i++)
            inventory.setItem(i, filler);
        for (int i = 46; i < 54; i++) // Fill bottom row except back button
            inventory.setItem(i, filler);

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

        if (event.getSlot() == 45) {
            new ParticlesGUI(customMob, clicker).openInventory();
            return;
        }

        // Identify which particle was clicked based on slot and map iteration order
        // This is a bit fragile if the map order changes, but since we just iterated to
        // fill,
        // we can iterate again.
        int index = 0;
        Class<?> selectedClass = null;
        for (Map.Entry<String, Class<?>> entry : availableParticles.entrySet()) {
            if (index == event.getSlot()) {
                selectedClass = entry.getValue();
                break;
            }
            index++;
        }

        if (selectedClass != null) {
            try {
                CustomMobParticle newParticle = (CustomMobParticle) selectedClass.getDeclaredConstructor()
                        .newInstance();
                customMob.addParticle(newParticle);
                new ParticlesGUI(customMob, clicker).openInventory();
            } catch (Exception e) {
                clicker.sendMessage(ChatColor.RED + "Error creating particle.");
                e.printStackTrace();
            }
        }
    }
}
