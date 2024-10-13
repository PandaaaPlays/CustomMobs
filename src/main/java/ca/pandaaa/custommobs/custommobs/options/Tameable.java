package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tameable extends CustomMobType {
    private final UUID owner;
    private final boolean tamed;

    public Tameable(UUID owner, boolean tamed) {
        this.owner = owner;
        this.tamed = tamed;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Tameable))
            return;

        if(owner != null)
            ((org.bukkit.entity.Tameable) customMob).setOwner(Bukkit.getOfflinePlayer(owner));
         ((org.bukkit.entity.Tameable) customMob).setTamed(tamed);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack owner = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta ownerMeta = owner.getItemMeta();
        ownerMeta.setDisplayName(Utils.applyFormat("&3&lOwner"));
        owner.setItemMeta(ownerMeta);
        items.add(getOptionItemStack(owner));

        ItemStack tamed = new ItemStack(Material.APPLE);
        ItemMeta tamedMeta = tamed.getItemMeta();
        tamedMeta.setDisplayName(Utils.applyFormat("&a&lTamed"));
        tamed.setItemMeta(tamedMeta);
        items.add(getOptionItemStack(tamed));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
