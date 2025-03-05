package ca.pandaaa.custommobs.guis.BasicTypes;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerGUI extends CustomMobsGUI implements Listener {

    private final Consumer<Player> consumer;
    private final ItemStack previous;
    private final ItemStack next;
    private int currentPage;

    public PlayerGUI(String option, Consumer<Player> consumer) {
        super(54, "&8Parameter &8&l» &8" + option);
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"), true);
        this.consumer = consumer;
    }

    public void openInventory(Player player, int page) {
        this.currentPage = page;
        List<ItemStack> playerItems = getPlayerItems();

        for(int i = 45; i < 54; i++)
            inventory.setItem(i, filler);

        boolean nextPage = playerItems.size() > (page * 45);

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (playerItems.size() > position) {
                inventory.setItem(i, playerItems.get(position));
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

        if(event.getSlot() == 45) {
            if(currentPage > 1)
                openInventory((Player) event.getWhoClicked(), currentPage - 1);
            else
                consumer.accept(null);
        } else if(event.getSlot() == 53 && item.getType() != Material.GRAY_STAINED_GLASS_PANE) {
            openInventory((Player) event.getWhoClicked(), currentPage + 1);
        } else if(event.getSlot() < 45) {
            NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Player");
            String playerName = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if(Bukkit.getPlayer(playerName) != null) {
                consumer.accept(Bukkit.getPlayer(playerName));
            } else {
                event.getWhoClicked().sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
                openInventory((Player) event.getWhoClicked(), 1);
            }
        }
    }

    private List<ItemStack> getPlayerItems() {
        List<ItemStack> playerItems = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Player"), PersistentDataType.STRING, player.getName());
            ((SkullMeta) meta).setOwningPlayer(player);
            meta.setDisplayName(Utils.applyFormat("&6&l" + player.getName()));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Click on this head to select this player ))"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            playerItems.add(item);
        }
        return playerItems;
    }

}
