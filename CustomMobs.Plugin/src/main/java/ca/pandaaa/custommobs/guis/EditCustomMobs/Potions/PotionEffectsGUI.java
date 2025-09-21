package ca.pandaaa.custommobs.guis.EditCustomMobs.Potions;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.PotionEffect;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class PotionEffectsGUI extends CustomMobsGUI {

    private final Consumer<PotionEffect> consumer;
    private final List<ItemStack> potionItems;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;
    private int currentPage = 1;

    public PotionEffectsGUI(CustomMob customMob, Consumer<PotionEffect> consumer) {
        super(54, "&8CustomMobs &8&l» &8Potion effects");
        this.consumer = consumer;
        this.customMob = customMob;
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);
        potionItems = getPotionTypesItems();
    }

    public void openInventory(Player player, int page) {
        this.currentPage = page;
        boolean nextPage = potionItems.size() > (page * 45);
        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (potionItems.size() > position){
                inventory.setItem(i, potionItems.get(position));
            } else {
                inventory.setItem(i, new ItemStack(Material.AIR));
                nextPage = false;
            }
        }
        if(currentPage > 1) {
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

        for(int i = 46; i <= 52; i++)
            inventory.setItem(i, filler);

        if (nextPage) {
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
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER || event.getCurrentItem() == null) {
            event.setCancelled(event.isShiftClick());
            return;
        }
        event.setCancelled(true);

        Player clicker = (Player) event.getWhoClicked();
        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        String name = itemMeta.getDisplayName();

        switch (event.getSlot()) {
            case 45:
                if(!name.contains("("))
                    consumer.accept(null);
                else {
                    int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                    openInventory(clicker, page);
                }
                break;
            case 53:
                if(event.getView().getItem(event.getSlot()).getType() == Material.GRAY_STAINED_GLASS_PANE)
                    break;
                int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                openInventory(clicker, page);
                break;
            default:
                if (event.getSlot() < 45) {
                    if(itemMeta.getDisplayName().contains("Absorption")) {
                        ((PotionMeta) itemMeta).clearCustomEffects();
                        ((PotionMeta) itemMeta).addCustomEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.ABSORPTION, -1, 1), true);
                    }

                    consumer.accept(new PotionEffect((PotionMeta) itemMeta));
                }
                break;
        }
    }
    private List<ItemStack> getPotionTypesItems() {
        List<ItemStack> items = new ArrayList<>();
        List<PotionEffectType> effects = new ArrayList<>();
        Registry.EFFECT.iterator().forEachRemaining(effects::add);

        for(PotionEffect potionEffect : customMob.getPotionEffects())
            effects.remove(potionEffect.getType());

        effects.sort(Comparator.comparing(effect -> effect.getKey().getKey()));
        for(PotionEffectType effect : effects) {
            ItemStack potion = new ItemStack(Material.POTION);
            PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
            if(potionMeta != null) {
                if (effect == PotionEffectType.ABSORPTION) {
                    potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.STRENGTH, -1, 1), true);
                } else {
                    potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(effect, -1, 1,true,true), true);
                }
                ArrayList<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(Utils.applyFormat("&7&o(( Click to select this potion effect ))"));
                potionMeta.setLore(lore);
                potionMeta.setDisplayName(Utils.applyFormat("&6&l" + Utils.getStartCase(effect.getKey().getKey())));
                potion.setItemMeta(potionMeta);
                items.add(getMenuItem(potion, true));
            }
        }
        return items;
    }


}
