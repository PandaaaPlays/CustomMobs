package ca.pandaaa.custommobs.guis.EditCustomMobs.Potions;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.EditCustomMobs.EditGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionsGUI extends CustomMobsGUI {
    private List<ItemStack> potionItems;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;
    private final ItemStack addPotionEffect;
    private int currentPage = 1;

    public PotionsGUI(CustomMob customMob) {
        super(54, "&8CustomMobs &8&l» &8Applied effects");

        this.customMob = customMob;
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);
        addPotionEffect = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
    }
    public void openInventory(Player player, int page) {
        potionItems = getPotionsItems();
        this.currentPage = page;
        boolean nextPage = potionItems.size() > (page * 45);

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (potionItems.size() > position) {
                inventory.setItem(i, potionItems.get(position));
            }
            else {
                inventory.setItem(i, new ItemStack(Material.AIR));
                nextPage = false;
            }
        }
        if(page > 1) {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
            previous.setItemMeta(previousItemMeta);
        } else {
            ItemMeta previousItemMeta = previous.getItemMeta();
            if(previousItemMeta != null)
                previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious"));
            previous.setItemMeta(previousItemMeta);
        }
        inventory.setItem(45, previous);
        inventory.setItem(46, filler);
        inventory.setItem(47, filler);
        inventory.setItem(48, filler);
        ItemMeta addItemMeta = addPotionEffect.getItemMeta();
        if(addItemMeta != null) {
            addItemMeta.setDisplayName(Utils.applyFormat("&a&l[+] Add potion"));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Click to add potion effects ))"));
            addItemMeta.setLore(lore);
            addPotionEffect.setItemMeta(addItemMeta);
        }
        inventory.setItem(49, addPotionEffect);
        inventory.setItem(50, filler);
        inventory.setItem(51, filler);
        inventory.setItem(52, filler);
        if(nextPage) {
            ItemMeta nextItemMeta = next.getItemMeta();
            if(nextItemMeta != null)
                nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
            next.setItemMeta(nextItemMeta);
            inventory.setItem(53, next);
        } else
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
        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null)
            return;
        Player clicker = (Player) event.getWhoClicked();
        ItemMeta itemMeta = item.getItemMeta();
        String name = itemMeta.getDisplayName();

        switch (event.getSlot()) {
            case 45:
                if(!name.contains("("))
                    new EditGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                else {
                    openInventory(clicker, currentPage-1);
                }
                break;
            case 49:
                 new PotionEffectsGUI(customMob, (value) -> {
                     customMob.addPotionMeta(value);
                     new PotionsGUI(customMob).openInventory(clicker, currentPage);
                 }).openInventory(clicker, 1);
                break;
            case 53:
                if(name.contains("(")){
                    openInventory(clicker, currentPage+1);
                }
                break;
            default:

                if (event.getSlot() < 45) {
                    int potionItemIndex = (currentPage - 1) * 45 + event.getSlot();
                    NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.PotionEffect.Remove.Confirm");
                    if (event.isRightClick()) {
                        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key)) {
                            customMob.removePotionItem(potionItemIndex);
                            // DropItems is desynchronized until the reopening of the inventory.
                            boolean currentPageEmpty = potionItems.size() - 1 <= ((currentPage - 1) * 45);
                            if (currentPageEmpty)
                                currentPage = currentPage == 1 ? currentPage : currentPage - 1;
                            openInventory(clicker, currentPage);
                        } else {
                            openInventory(clicker, currentPage);
                            event.getInventory().setItem(event.getSlot(), getMenuItem(getDeleteItem(new ItemStack(Material.BARRIER)), true));
                        }
                    } else {
                        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key))
                            openInventory(clicker, currentPage);
                        else
                            new SpecificPotionGUI(customMob, (currentPage - 1) * 45 + event.getSlot()).openInventory(clicker);
                    }
                }
                break;
        }
    }
    private List<ItemStack> getPotionsItems() {
        List<ItemStack> items = new ArrayList<>();
        if(customMob.getPotionMeta() != null) {
            List<PotionMeta> potionMetas = customMob.getPotionMeta();

            for(PotionMeta potionMeta1 : potionMetas) {
                ItemStack potion = new ItemStack(Material.POTION);
                PotionMeta potionMeta = (PotionMeta) new ItemStack(Material.POTION).getItemMeta();
                if(potionMeta1.getCustomEffects().get(0).getType() == PotionEffectType.ABSORPTION)
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH,1 ,1),true);
                else
                    potionMeta.addCustomEffect(new PotionEffect(potionMeta1.getCustomEffects().get(0).getType(),1 ,1),true);
                ArrayList<String> lore = new ArrayList<>();
                PotionEffect effect = potionMeta1.getCustomEffects().get(0);
                lore.add(Utils.applyFormat("&f&l* &eAmplifier:&f " + effect.getAmplifier()));
                lore.add(Utils.applyFormat("&f&l* &bDuration:&f " + (effect.getDuration() <= 0 ? "Infinite" : Utils.getFormattedTime(effect.getDuration() / 20))));
                String ambient = effect.isAmbient() ? "&a&lOn" : "&c&lOff";
                lore.add(Utils.applyFormat("&f&l* &aAmbient:&f " + ambient));
                String particles = effect.hasParticles() ? "&a&lOn" : "&c&lOff";
                lore.add(Utils.applyFormat("&f&l* &dParticules:&f " + particles));
                // TODO Look into has-icon of potionmeta
                lore.add("");
                lore.add(Utils.applyFormat("&7&o(( Left-Click to edit this potion effect ))"));
                lore.add(Utils.applyFormat("&7&o(( Right-Click to remove this potion effect ))"));
                potionMeta.setLore(lore);
                potionMeta.setDisplayName(Utils.applyFormat("&6&l" + Utils.getStartCase(potionMeta1.getCustomEffects().get(0).getType().getKey().getKey())));
                potion.setItemMeta(potionMeta);
                items.add(getMenuItem(potion, true));
            }
        }
        return items;
    }

    private ItemStack getDeleteItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.PotionEffect.Remove.Confirm");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Confirm potion effect deletion"));
        lore.add(Utils.applyFormat("&7&o(( Left-click to cancel the deletion ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-click again to confirm the deletion ))"));
        lore.add(Utils.applyFormat("&c&l[!] &cThis will permanently delete this potion effect."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
