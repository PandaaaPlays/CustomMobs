package ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects;

import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomMobCustomEffect;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
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

public class CustomEffectOptionsGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private List<ItemStack> optionItems;

    public CustomEffectOptionsGUI(CustomMob customMob, CustomMobCustomEffect customMobCustomEffect, List<ItemStack> optionItems) {
        super(27, "&8CustomMobs &8&l» &8" + formatClassName(customMobCustomEffect.getClass().getSimpleName()));

        this.customMob = customMob;
        this.optionItems = optionItems.stream().map(item -> getMenuItem(item, true)).toList();
        if(this.optionItems == null)
            this.optionItems = new ArrayList<>();
    }

    public void openInventory(Player player) {
        for(int i = 0; i < 27; i++)
            inventory.setItem(i, filler);
        inventory.setItem(18, getPreviousItem());

        switch(optionItems.size()) {
            case 1:
                inventory.setItem(13, optionItems.get(0));
                break;
            case 2:
                inventory.setItem(12, optionItems.get(0));
                inventory.setItem(14, optionItems.get(1));
                break;
            case 3:
                for(int i = 0; i < optionItems.size(); i++) {
                    inventory.setItem(i + 12, optionItems.get(i));
                }
                break;
            case 4:
                inventory.setItem(11, optionItems.get(0));
                inventory.setItem(12, optionItems.get(1));
                inventory.setItem(14, optionItems.get(2));
                inventory.setItem(15, optionItems.get(3));
                break;
            case 5:
                for(int i = 0; i < optionItems.size(); i++) {
                    inventory.setItem(i + 11, optionItems.get(i));
                }
                break;
            case 6:
                for(int i = 0; i < optionItems.size() / 2; i++) {
                    inventory.setItem(i + 10, optionItems.get(i));
                }
                for(int i = optionItems.size() / 2; i < optionItems.size(); i++) {
                    inventory.setItem(i + 11, optionItems.get(i));
                }
                break;
            case 7:
                for(int i = 0; i < optionItems.size(); i++) {
                    inventory.setItem(i + 10, optionItems.get(i));
                }
                break;
            default:
                break;
        }

        player.openInventory(inventory);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory()))
            return;
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }

        event.setCancelled(true);

        final ItemStack item = event.getCurrentItem();
        if(item == null || item.getType() == Material.GRAY_STAINED_GLASS_PANE)
            return;

        final ItemMeta itemMeta = item.getItemMeta();
        final String name = itemMeta.getDisplayName();
        Player clicker = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 18:
                new CustomEffectsGUI(customMob).openInventory(((Player) event.getWhoClicked()).getPlayer(), 1);
                break;
            default:
                for(NamespacedKey key : itemMeta.getPersistentDataContainer().getKeys()) {
                    String keyString = key.getKey();
                    if(keyString.contains("custommobs.customeffect")) {
                        String[] value = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING).split("\\.");
                        // TODO Tech debt ? SetCurrentItem is useless when triggering a new gui cause we reopen it anyway...
                        event.setCurrentItem(getMenuItem(customMob.getCustomMobCustomEffect(value[0]).modifyOption(clicker, customMob, value[1], event.getClick()), true));
                    }
                }
                break;

        }
    }

    private static String formatClassName(String className) {
        String spaced = className.replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("([A-Z])([A-Z][a-z])", "$1 $2");
        return spaced.substring(0, 1).toUpperCase() + spaced.substring(1).toLowerCase();
    }
}
