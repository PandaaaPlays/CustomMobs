package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.guis.EditCustomMobs.OptionsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tameable extends CustomMobOption implements Listener {
    private UUID owner;
    private boolean tamed;
    private boolean waitingForOwner;
    private Player player = null;
    private CustomMob customMob = null;

    public Tameable(UUID owner, boolean tamed) {
        this.owner = owner;
        this.tamed = tamed;
        waitingForOwner = false;

        CustomMobs plugin = CustomMobs.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Tameable))
            return;

        ((org.bukkit.entity.Tameable) customMob).setTamed(tamed);
        if(owner != null)
            ((org.bukkit.entity.Tameable) customMob).setOwner(Bukkit.getPlayer(owner));
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getOwnerItem(), true, false));
        items.add(getOptionItemStack(getTamedItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {

            // TODO Gui for players instead
            case "owner": {
                if(clickType.isRightClick()) {
                    this.owner = null;
                    customMob.getCustomMobConfiguration().setOwner(null);
                    return getOptionItemStack(getOwnerItem(), true, false);
                } else {
                    clicker.closeInventory();
                    clicker.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eCustomMob owner"));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fEnter the name of the owner in the chat."));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel owner change."));
                    this.player = clicker;
                    this.customMob = customMob;
                    waitingForOwner = true;
                }
            }

            case "tamed": {
                this.tamed = !tamed;
                customMob.getCustomMobConfiguration().setTamed(tamed);
                return getOptionItemStack(getTamedItem(), false, false);
            }

        }
        return null;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() != player)
            return;
        if (!waitingForOwner)
            return;

        event.setCancelled(true);

        String message = event.getMessage();
        if (!message.equalsIgnoreCase("cancel")) {
            Player newOwner = Bukkit.getPlayer(message);
            if (newOwner == null) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
                return;
            }

            player.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eSuccessfully changed owner to: &r" + newOwner.getName()));
            customMob.getCustomMobConfiguration().setOwner(newOwner.getUniqueId());
            this.owner = newOwner.getUniqueId();
        }

        Bukkit.getScheduler().runTask(CustomMobs.getPlugin(), () -> new OptionsGUI(customMob).openInventory(player, 1));
        waitingForOwner = false;
    }

    public CustomMobsItem getOwnerItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PLAYER_HEAD);
        String owner = this.owner == null ? "&fNone" : "&f" + Bukkit.getOfflinePlayer(this.owner).getName();
        item.setName("&3&lOwner");
        item.addLore("&eOwner: " + owner);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Owner");
        return item;
    }

    public CustomMobsItem getTamedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.APPLE);
        String tamed = this.tamed ? "&a&lOn" : "&c&lOff";
        item.setName("&a&lTamed");
        item.addLore("&eTamed: " + tamed);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "Tamed");
        return item;
    }
}
