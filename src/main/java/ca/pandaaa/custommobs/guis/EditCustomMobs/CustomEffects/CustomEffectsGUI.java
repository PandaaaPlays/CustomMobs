package ca.pandaaa.custommobs.guis.EditCustomMobs.CustomEffects;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomMobCustomEffect;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
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

public class CustomEffectsGUI extends CustomMobsGUI {
    private final List<ItemStack> customEffectsItems;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;

    public CustomEffectsGUI(CustomMob customMob) {
        super(54, "&8CustomMobs &8&l» &8Special effects");

        this.customMob = customMob;

        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);

        this.customEffectsItems = new ArrayList<>();
        customMob.getCustomMobCustomEffects().stream()
                .map(effect -> getMenuItem(effect.getCustomEffectItem(), true))
                .forEach(this.customEffectsItems::add);
    }

    public void openInventory(Player player, int page) {
        ItemMeta nextItemMeta = next.getItemMeta();
        if(nextItemMeta != null)
            nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
        next.setItemMeta(nextItemMeta);

        boolean nextPage = true;

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (customEffectsItems.size() > position)
                inventory.setItem(i, customEffectsItems.get(position));
            else {
                inventory.setItem(i, new ItemStack(Material.AIR));
                nextPage = false;
            }
        }

        ItemMeta previousItemMeta = previous.getItemMeta();
        if(page > 1) {
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
        } else {
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
        }
        previous.setItemMeta(previousItemMeta);
        inventory.setItem(45, previous);
        inventory.setItem(46, filler);
        inventory.setItem(47, filler);
        inventory.setItem(48, filler);
        inventory.setItem(49, getCooldownItem());
        inventory.setItem(50, filler);
        inventory.setItem(51, filler);
        inventory.setItem(52, filler);
        if(nextPage)
            inventory.setItem(53, next);
        else
            inventory.setItem(53, filler);

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
            case 45:
                if(!name.contains("("))
                    new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                else {
                    int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                    openInventory(clicker, page);
                }
                break;
            case 49:
                if(event.getClick().isRightClick()) {
                    // TODO Tech debt, make sure nothing is hardocded like this, make sure everything are variables....
                    customMob.setCustomEffectsCooldownDuration(5);
                    inventory.setItem(49, getCooldownItem());
                } else {
                    new IntegerGUI("Cooldown duration", false, 3, 15000, (value) -> {
                        customMob.setCustomEffectsCooldownDuration(value);
                        new CustomEffectsGUI(customMob).openInventory(clicker, 1);
                    }).setTimeGUI().openInventory(clicker, customMob.getCustomEffectsCooldownDuration());
                }
                break;
            case 53:
                int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                openInventory(clicker, page);
                break;
            default:
                for(NamespacedKey key : itemMeta.getPersistentDataContainer().getKeys()) {
                    String keyString = key.getKey();
                    if(keyString.contains("custommobs.customeffect")) {
                        CustomMobCustomEffect customMobCustomEffect = customMob.getCustomMobCustomEffect(itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING));
                        if(event.getClick().isRightClick()) {
                            customMobCustomEffect.toggle();
                            event.setCurrentItem(getMenuItem(customMobCustomEffect.modifyStatus(), true));
                        } else {
                            List<ItemStack> optionItems = customMobCustomEffect.getOptionsItems();
                            if (optionItems != null && !optionItems.isEmpty())
                                new CustomEffectOptionsGUI(customMob, customMobCustomEffect, customMobCustomEffect.getOptionsItems()).openInventory(clicker);
                        }
                    }
                }
                break;
        }
    }

    private ItemStack getCooldownItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CLOCK);
        item.setName("&6&lCooldown duration");
        item.addLore(
                "&eDuration: &f" + Utils.getFormattedTime(customMob.getCustomEffectsCooldownDuration(), false, false),
                "",
                "&7&o(( Left-Click to change the cooldown in between 'Cooldown' effect(s) ))",
                "&7&o(( Right-Click to reset the cooldown in between 'Cooldown' effect(s) ))"
        );
        return getMenuItem(item, true);
    }
}
