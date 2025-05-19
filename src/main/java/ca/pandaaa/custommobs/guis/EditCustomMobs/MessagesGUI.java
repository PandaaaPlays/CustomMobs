package ca.pandaaa.custommobs.guis.EditCustomMobs;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Messages.SpawnDeathMessage;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class MessagesGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private final Player player;

    private Integer waitingForChange;

    public MessagesGUI(CustomMob customMob, String option, Player player) {
        super(18, "&8Message &8&l» &8" + option);
        this.customMob = customMob;
        this.player = player;

        waitingForChange = null;
    }

    public void openInventory() {
        List<ItemStack> messageItems = getMessageItems();
        for(int i = 0; i < messageItems.size(); i++)
            inventory.setItem(i, messageItems.get(i));
        for(int i = messageItems.size(); i < 9; i++)
            inventory.setItem(i, new ItemStack(Material.AIR));

        for(int i = 9; i < 18; i++)
            inventory.setItem(i, filler);

        ItemStack addMessage = getMenuItem(new ItemStack(Material.END_CRYSTAL), true);
        ItemMeta addItemMeta = addMessage.getItemMeta();
        if(addItemMeta != null) {
            addItemMeta.setDisplayName(Utils.applyFormat("&a&l[+] Add message"));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Click to add a new message ))"));
            addItemMeta.setLore(lore);
            addMessage.setItemMeta(addItemMeta);
        }

        inventory.setItem(9, getPreviousItem());
        inventory.setItem(13, addMessage);

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

        switch(event.getSlot()) {
            case 9:
                new OthersGUI(customMob).openInventory(clicker);
                break;
            case 13:
                if(customMob.getCustomMobConfiguration().getMessages().size() != 9) {
                    clicker.closeInventory();
                    clicker.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eCreate message"));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fEnter the message you want &e&nin the chat&f."));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fThe messages supports &4c&co&6l&eo&ar&bs&f ( &* (&ebasic&f) and &#* (&#cbff0fhex&f) )"));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel creation."));
                    waitingForChange = customMob.getCustomMobMessages().size();
                }
                break;
            default:
                if(event.getSlot() < 9) {
                    NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Message.Remove.Confirm");
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key)) {
                        if (event.isRightClick()) {
                            customMob.removeMessage(event.getSlot());
                            openInventory();
                        } else {
                            openInventory();
                        }

                    } else {
                        if (event.isRightClick()) {
                            if (event.isShiftClick()) {
                                new DoubleGUI("Radius", false, 0, 200, (value) -> {
                                    SpawnDeathMessage message = customMob.getCustomMobMessages().get(event.getSlot());
                                    message.setRadius(value);
                                    customMob.editMessage(event.getSlot(), message);
                                    openInventory();
                                }).setMinusPrettyValue("Everyone").openInventory(player, customMob.getCustomMobMessages().get(event.getSlot()).getRadius());
                            } else {
                                event.getInventory().setItem(event.getSlot(), getMenuItem(getDeleteItem(new ItemStack(Material.BARRIER)), true));
                            }
                        } else {
                            if (event.isShiftClick()) {
                                SpawnDeathMessage message = (SpawnDeathMessage) customMob.getCustomMobMessages().get(event.getSlot());
                                message.setOnDeath(!message.isOnDeath());
                                customMob.editMessage(event.getSlot(), message);
                                openInventory();
                            } else {
                                clicker.closeInventory();
                                clicker.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eEdit message"));
                                clicker.sendMessage(Utils.applyFormat(" &6&l- &fEnter the message you want &e&nin the chat&f."));
                                clicker.sendMessage(Utils.applyFormat(" &6&l- &fThe messages supports &4c&co&6l&eo&ar&bs&f ( &* (&ebasic&f) and &#* (&#cbff0fhex&f) )"));
                                clicker.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel creation."));
                                waitingForChange = event.getSlot();
                            }
                        }
                    }
                }
                break;
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() != player)
            return;
        if (waitingForChange == null)
            return;

        event.setCancelled(true);

        String message = event.getMessage();
        if (!message.equalsIgnoreCase("cancel")) {
            SpawnDeathMessage mobMessage = new SpawnDeathMessage(null, false, 100);
            if(customMob.getCustomMobMessages().size() - 1 >= waitingForChange)
                mobMessage = (SpawnDeathMessage) customMob.getCustomMobMessages().get(waitingForChange);

            mobMessage.setMessage(message);
            customMob.editMessage(waitingForChange, mobMessage);
            player.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eMessage saved : &r" + message));
        }

        waitingForChange = null;
        Bukkit.getScheduler().runTask(CustomMobs.getPlugin(), new MessagesGUI(customMob, "Spawn / Death", player)::openInventory);
    }

    private List<ItemStack> getMessageItems() {
        List<ItemStack> messageItems = new ArrayList<>();
        for(SpawnDeathMessage message : customMob.getCustomMobMessages()) {
            ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
            if(message.isOnDeath())
                item.setType(Material.DEAD_BUSH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.applyFormat(message.getMessage()));
            List<String> lore = new ArrayList<>();
            String event = message.isOnDeath() ? "&c&lDeath" : "&b&lSpawn";
            lore.add(Utils.applyFormat("&f&l* &eHappens on:&f " + event));
            String radius = message.getRadius() > 0 ? message.getRadius() + " block(s)" : "Everyone";
            lore.add(Utils.applyFormat("&f&l* &aRadius:&f " + radius));
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Left-Click to edit this message ))"));
            lore.add(Utils.applyFormat("&7&o(( Shift-Left-Click to edit this message's type ))"));
            lore.add(Utils.applyFormat("&7&o(( Right-Click to remove this message ))"));
            lore.add(Utils.applyFormat("&7&o(( Shift-Right-Click to edit this message's radius ))"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            messageItems.add(item);
        }
        return messageItems;
    }

    private ItemStack getDeleteItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Message.Remove.Confirm");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        lore.add("");
        itemMeta.setDisplayName(Utils.applyFormat("&c&l[-] Confirm message deletion"));
        lore.add(Utils.applyFormat("&7&o(( Left-click to cancel the deletion ))"));
        lore.add(Utils.applyFormat("&7&o(( Right-click again to confirm the deletion ))"));
        lore.add(Utils.applyFormat("&c&l[!] &cThis will permanently delete this message."));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

}
