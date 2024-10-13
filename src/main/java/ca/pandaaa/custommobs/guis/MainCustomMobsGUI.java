package ca.pandaaa.custommobs.guis;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class MainCustomMobsGUI extends CustomMobsGUI implements Listener {
    private final List<ItemStack> items;
    private final ItemStack previous;
    private final ItemStack addCustomMob;
    private final ItemStack next;

    public MainCustomMobsGUI() {
        super(54, "&8CustomMobs &8&l» &8Mobs");

        CustomMobs plugin = CustomMobs.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.items = new ArrayList<>();
        for (CustomMobConfiguration customMobConfiguration : plugin.getCustomMobsManager().getCustomMobConfigurations()) {
            ItemStack item = new ItemStack(customMobConfiguration.getItem().getType());

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Utils.applyFormat(customMobConfiguration.getName()));
            itemMeta.setLore(getItemLore());
            NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.FileName");
            itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, customMobConfiguration.getFileName().replace(".yml", ""));
            item.setItemMeta(itemMeta);

            items.add(getMenuItem(item));
        }

        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"));
        next = getMenuItem(Utils.createHead("6d865aae2746a9b8e9a4fe629fb08d18d0a9251e5ccbe5fa7051f53eab9b94"));

        addCustomMob = getMenuItem(new ItemStack(Material.END_CRYSTAL));
    }

    public void openInventory(Player player, int page) {
        ItemMeta previousItemMeta = previous.getItemMeta();
        if(previousItemMeta != null)
            previousItemMeta.setDisplayName(Utils.applyFormat("&e&lPrevious (" + (page - 1) + ")"));
        previous.setItemMeta(previousItemMeta);

        ItemMeta nextItemMeta = next.getItemMeta();
        if(nextItemMeta != null)
            nextItemMeta.setDisplayName(Utils.applyFormat("&e&lNext (" + (page + 1) + ")"));
        next.setItemMeta(nextItemMeta);

        ItemMeta addItemMeta = addCustomMob.getItemMeta();
        if(addItemMeta != null)
            addItemMeta.setDisplayName(Utils.applyFormat("&a&l[+] Add CustomMob"));
        addCustomMob.setItemMeta(addItemMeta);

        boolean nextPage = true;

        // Make sure we set the extra items to air, so that other page(s) item(s) are not persisted.
        for(int i = 0; i < 45; i++) {
            int position = ((page - 1) * 45) + i;
            if (items.size() > position)
                inventory.setItem(i, items.get(position));
            else {
                inventory.setItem(i, new ItemStack(Material.AIR));
                nextPage = false;
            }
        }

        if(page > 1)
            inventory.setItem(45, previous);
        else
            inventory.setItem(45, filler);
        inventory.setItem(46, filler);
        inventory.setItem(47, filler);
        inventory.setItem(48, filler);
        inventory.setItem(49, addCustomMob);
        inventory.setItem(50, filler);
        inventory.setItem(51, filler);
        inventory.setItem(52, filler);
        if(nextPage)
            inventory.setItem(53, next);
        else
            inventory.setItem(53, filler);

        player.openInventory(inventory);
    }

    private List<String> getItemLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to edit this CustomMob ))"));
        return lore;
    }
}
