package ca.pandaaa.custommobs.custommobs;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DropItem implements ConfigurationSerializable {

    private ItemStack itemStack;
    private double probability;

    public DropItem(ItemStack itemStack, double probability) {
        this.itemStack = itemStack;
        this.probability = probability;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("item", itemStack);
        data.put("probability", probability);
        return data;
    }

    public static DropItem deserialize(Map<String, Object> data) {
        ItemStack itemStack = (ItemStack) data.get("item");
        double probability = (double) data.get("probability");
        return new DropItem(itemStack, probability);
    }
}
