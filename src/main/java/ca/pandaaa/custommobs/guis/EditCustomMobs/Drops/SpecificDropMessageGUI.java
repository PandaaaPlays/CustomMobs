package ca.pandaaa.custommobs.guis.EditCustomMobs.Drops;

import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import org.bukkit.entity.Player;

public class SpecificDropMessageGUI extends CustomMobsGUI {

    public SpecificDropMessageGUI() {
        super(18,"&8CustomMobs &8&l» &8Drop messages");
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
