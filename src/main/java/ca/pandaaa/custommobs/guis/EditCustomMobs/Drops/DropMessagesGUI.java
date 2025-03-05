package ca.pandaaa.custommobs.guis.EditCustomMobs.Drops;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.custommobs.Messages.DropMessage;
import ca.pandaaa.custommobs.custommobs.Messages.Message;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.DropConditions;
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

public class DropMessagesGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private final Drop drop;
    private final Player player;
    private final ItemStack previous;
    private final int dropIndex;

    private Integer waitingForChange;

    public DropMessagesGUI(CustomMob customMob, Drop drop, String option, Player player) {
        super(18, "&8Message &8&l» &8" + option);
        this.customMob = customMob;
        this.dropIndex = customMob.getDrops().indexOf(drop);
        this.drop = drop;
        this.player = player;
        previous = getMenuItem(Utils.createHead("a2f0425d64fdc8992928d608109810c1251fe243d60d175bed427c651cbe"), true);

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

        inventory.setItem(9, previous);
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
                new SpecificDropGUI(customMob, drop, dropIndex).openInventory(clicker);
                break;
            case 13:
                if(drop.getMessages().size() != 9) {
                    clicker.closeInventory();
                    clicker.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eCreate message"));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fEnter the message you want &e&nin the chat&f."));
                    if(drop.getDropCondition() != DropConditions.NEARBY)
                        clicker.sendMessage(Utils.applyFormat(" &6&l- &fThe placeholder &e%player%&f will be replaced by the dropper's name."));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fThe messages supports &4c&co&6l&eo&ar&bs&f ( &* (&ebasic&f) and &#* (&#cbff0fhex&f) )"));
                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel creation."));
                    waitingForChange = drop.getMessages().size();
                }
                break;
            default:
                if(event.getSlot() < 9) {
                    NamespacedKey key = new NamespacedKey(CustomMobs.getPlugin(), "CustomMobs.Message.Remove.Confirm");
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().getKeys().contains(key)) {
                        if (event.isRightClick()) {
                            drop.getMessages().remove(event.getSlot());
                            customMob.editDrop(drop, dropIndex);
                            openInventory();
                        } else {
                            openInventory();
                        }

                    } else {
                        if (event.isRightClick()) {
                            if (event.isShiftClick()) {
                                if(!((DropMessage) drop.getMessages().get(event.getSlot())).isDropperOnly()) {
                                    new DoubleGUI("Radius", false, 0, 200, (value) -> {
                                        Message message = drop.getMessages().get(event.getSlot());
                                        message.setRadius(value);
                                        customMob.editDrop(drop, dropIndex);
                                        openInventory();
                                    }).setMinusPrettyValue("Everyone").openInventory(player, drop.getMessages().get(event.getSlot()).getRadius());
                                }
                            } else {
                                event.getInventory().setItem(event.getSlot(), getMenuItem(getDeleteItem(new ItemStack(Material.BARRIER)), true));
                            }
                        } else {
                            if (event.isShiftClick()) {
                                if(drop.getDropCondition() != DropConditions.DROP) {
                                    DropMessage message = (DropMessage) drop.getMessages().get(event.getSlot());
                                    message.setDropperOnly(!message.isDropperOnly());
                                    customMob.editDrop(drop, dropIndex);
                                    openInventory();
                                }
                            } else {
                                clicker.closeInventory();
                                clicker.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eEdit message"));
                                clicker.sendMessage(Utils.applyFormat(" &6&l- &fEnter the message you want &e&nin the chat&f."));
                                if(drop.getDropCondition() != DropConditions.NEARBY)
                                    clicker.sendMessage(Utils.applyFormat(" &6&l- &fThe placeholder &e%player%&f will be replaced by the dropper's name."));
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
            DropMessage mobMessage = new DropMessage(null, true, 100);
            if(drop.getMessages().size() - 1 >= waitingForChange)
                mobMessage = (DropMessage) drop.getMessages().get(waitingForChange);
            mobMessage.setMessage(message);
            drop.editMessages(waitingForChange, mobMessage);
            customMob.editDrop(drop, dropIndex);

            player.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eMessage saved : &r" + message));
        }

        waitingForChange = null;
        Bukkit.getScheduler().runTask(CustomMobs.getPlugin(), new DropMessagesGUI(customMob, drop, "Drop", player)::openInventory);
    }

    private List<ItemStack> getMessageItems() {
        List<ItemStack> messageItems = new ArrayList<>();
        for(Message message : drop.getMessages()) {
            ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.applyFormat(message.getMessage()));
            List<String> lore = new ArrayList<>();
            if(drop.getDropCondition() != DropConditions.DROP) {
                String type = ((DropMessage) message).isDropperOnly() ? "Dropper only" : "Nearby players / Everyone";
                lore.add(Utils.applyFormat("&f&l* &cMessage type:&f " + type));
            }
            if(!((DropMessage) message).isDropperOnly()) {
                String radius = message.getRadius() > 0 ? message.getRadius() + (drop.getDropCondition() == DropConditions.DROP ? " block(s) around the mob" : " block(s) around the 'dropper'") : "Everyone";
                lore.add(Utils.applyFormat("&f&l* &aRadius:&f " + radius));
            }
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Left-Click to edit this message ))"));
            if(drop.getDropCondition() != DropConditions.DROP)
                lore.add(Utils.applyFormat("&7&o(( Shift-Left-Click to change the type of this message ))"));
            lore.add(Utils.applyFormat("&7&o(( Right-Click to remove this message ))"));
            if(!((DropMessage) message).isDropperOnly())
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
