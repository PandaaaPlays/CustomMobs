package ca.pandaaa.custommobs.guis.EditCustomMobs.Drops;

import ca.pandaaa.custommobs.custommobs.DropItem;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import org.bukkit.entity.Player;

public class SpecificDropGUI extends CustomMobsGUI {

    private final DropItem dropItem;

    public SpecificDropGUI(DropItem dropItem) {
        super(9, "&8CustomMobs &8&l» &8Drop configuration");
        this.dropItem = dropItem;
    }

    public void openInventory(Player player) {
        inventory.setItem(4, getMenuItem(dropItem.getItemStack(), false));

        player.openInventory(inventory);
    }
}
