package ca.pandaaa.custommobs.guis.EditCustomMobs.Particles;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Particles.CustomMobParticle;
import ca.pandaaa.custommobs.guis.BasicTypes.DoubleGUI;
import ca.pandaaa.custommobs.guis.BasicTypes.IntegerGUI;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class EditParticleGUI extends CustomMobsGUI {

    private final CustomMob customMob;
    private final CustomMobParticle particle;
    private final int particleIndex;
    private final Player player;

    public EditParticleGUI(CustomMob customMob, int particleIndex, Player player) {
        super(36, "&8Particles &8&l» &8Edit");
        this.customMob = customMob;
        this.particleIndex = particleIndex;
        this.particle = customMob.getParticles().get(particleIndex);
        this.player = player;
    }

    public void openInventory() {
        for (int i = 0; i < 36; i++)
            inventory.setItem(i, filler);

        inventory.setItem(10, getAmountItem());
        inventory.setItem(11, getSpeedItem());
        inventory.setItem(13, getDeathSpawnItem());
        inventory.setItem(14, getOffsetItem("X", particle.getOffsetX()));
        inventory.setItem(15, getOffsetItem("Y", particle.getOffsetY()));
        inventory.setItem(16, getOffsetItem("Z", particle.getOffsetZ()));

        inventory.setItem(18, getPreviousItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory()))
            return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }

        event.setCancelled(true);
        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null)
            return;

        Player clicker = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 10:
                new IntegerGUI("Amount", false, 0, 1000, (amount) -> {
                    particle.setAmount(amount);
                    customMob.editParticle(particleIndex, particle);
                    new EditParticleGUI(customMob, particleIndex, clicker).openInventory();
                }).openInventory(clicker, particle.getAmount());
                break;
            case 11:
                new DoubleGUI("Speed", false, 0, 10, (speed) -> {
                    particle.setSpeed(speed);
                    customMob.editParticle(particleIndex, particle);
                    new EditParticleGUI(customMob, particleIndex, clicker).openInventory();
                }).openInventory(clicker, particle.getSpeed());
                break;
            case 13:
                particle.setOnDeath(!particle.isOnDeath());
                customMob.editParticle(particleIndex, particle);
                openInventory();
                break;
            case 14:
                new DoubleGUI("Offset X", false, 0, 100, (offset) -> {
                    particle.setOffsetX(offset);
                    customMob.editParticle(particleIndex, particle);
                    new EditParticleGUI(customMob, particleIndex, clicker).openInventory();
                }).openInventory(clicker, particle.getOffsetX());
                break;
            case 15:
                new DoubleGUI("Offset Y", false, 0, 100, (offset) -> {
                    particle.setOffsetY(offset);
                    customMob.editParticle(particleIndex, particle);
                    new EditParticleGUI(customMob, particleIndex, clicker).openInventory();
                }).openInventory(clicker, particle.getOffsetY());
                break;
            case 16:
                new DoubleGUI("Offset Z", false, 0, 100, (offset) -> {
                    particle.setOffsetZ(offset);
                    customMob.editParticle(particleIndex, particle);
                    new EditParticleGUI(customMob, particleIndex, clicker).openInventory();
                }).openInventory(clicker, particle.getOffsetZ());
                break;
            case 18:
                new ParticlesGUI(customMob, player).openInventory();
                break;
        }
    }

    private ItemStack getAmountItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GLOWSTONE_DUST);
        item.setName("&6&lAmount");
        item.addLore("&eAmount: &f" + particle.getAmount());
        item.addLore("", "&7&o(( Click to edit the amount ))");
        return getMenuItem(item.getItem(), true);
    }

    private ItemStack getSpeedItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SUGAR);
        item.setName("&6&lSpeed");
        item.addLore("&eSpeed: &f" + particle.getSpeed());
        item.addLore("", "&7&o(( Click to edit the speed ))");
        return getMenuItem(item.getItem(), true);
    }

    private ItemStack getDeathSpawnItem() {
        CustomMobsItem item = new CustomMobsItem(Material.DEAD_BUSH);
        item.setName("&6&lDeath/Spawn");
        if(particle.isOnDeath()) {
            item.setType(Material.DEAD_BUSH);
            item.addLore("&eDeath/Spawn:&f On Death", "", "&7&o(( Click to edit this option ))");
        } else {
            item.setType(Material.RESPAWN_ANCHOR);
            item.addLore("&eDeath/Spawn:&f On Spawn", "", "&7&o(( Click to edit this option ))");
        }
        return getMenuItem(item, true);
    }

    private ItemStack getOffsetItem(String axis, double value) {
        CustomMobsItem item = new CustomMobsItem(Material.COMPASS);
        item.setName("&6&l" + axis + " Offset");
        item.addLore("&eCurrent " + axis + " offset: &f" + value);
        item.addLore("", "&7&o(( Click to edit the offset ))");
        return getMenuItem(item.getItem(), true);
    }
}
