package ca.pandaaa.custommobs.guis.EditCustomMobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TypesCustomMobsGUI extends CustomMobsGUI implements Listener {
    private final List<ItemStack> typeItems;
    private final CustomMob customMob;
    private final ItemStack previous;
    private final ItemStack next;

    public TypesCustomMobsGUI(CustomMob customMob) {
        super(54, "&8CustomMobs &8&l» &8Types");

        this.customMob = customMob;
        CustomMobs plugin = CustomMobs.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"));
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"));
        typeItems = getTypesItems();
    }

    public void openInventory(Player player, int page) {

        boolean nextPage = true;

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (typeItems.size() > position)
                inventory.setItem(i, typeItems.get(position));
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
        inventory.setItem(49, filler);
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
    private void onInventoryClick(InventoryClickEvent event) {
        if(!Objects.equals(event.getClickedInventory(), inventory))
            return;
        if (event.getCurrentItem() == null)
            return;

        event.setCancelled(true);
        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        final String name = itemMeta.getDisplayName();
        Player clicker = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 45:
                if(!name.contains("("))
                    new EditCustomMobsGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                else {
                    int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                    openInventory(clicker, page);
                }
                break;
            case 53:
                int page = Character.getNumericValue(name.charAt(name.indexOf('(') + 1));
                openInventory(clicker, page);
                break;
            // Every mob types
            default:
                if (event.getCurrentItem() != filler) {
                    customMob.setType(EntityType.valueOf(ChatColor.stripColor(itemMeta.getDisplayName().replace(" ", "_"))));
                    new EditCustomMobsGUI(customMob, CustomMobs.getPlugin().getCustomMobsManager(), clicker).openInventory();
                }
                break;
        }
    }

    private List<ItemStack> getTypesItems() {
        List<ItemStack> items = new ArrayList<>();

        List<EntityType> types = Arrays.asList(EntityType.values());
        types.sort(Comparator.comparing(EntityType::name));

        for(EntityType type : types) {
            if(type != EntityType.PLAYER
                    && type != EntityType.ARMOR_STAND
                    && type.isAlive()) {
                Material spawnEgg = getSpawnEggMaterial(type);
                if(spawnEgg != null) {
                    ItemStack item = new ItemStack(spawnEgg);
                    ItemMeta itemMeta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("");
                    lore.add(Utils.applyFormat("&7&o(( Click to select this CustomMob type ))"));
                    itemMeta.setLore(lore);
                    itemMeta.setDisplayName(Utils.applyFormat("&6&l" + type.name().replace("_", " ")));
                    item.setItemMeta(itemMeta);
                    items.add(getMenuItem(item));
                }
            }
        }
        return items;
    }

    private Material getSpawnEggMaterial(EntityType type) {
        try {
            if(type == EntityType.ILLUSIONER)
                return Material.SPECTRAL_ARROW;
            if(type == EntityType.GIANT)
                return Material.ZOMBIE_HEAD;

            String eggName = type.name() + "_SPAWN_EGG";
            return Material.valueOf(eggName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
