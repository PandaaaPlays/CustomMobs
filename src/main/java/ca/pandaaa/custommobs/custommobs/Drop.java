package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.utils.DropConditions;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.util.*;

public class Drop implements ConfigurationSerializable {

    private ItemStack itemStack;
    // Probability in %
    private double probability;
    private boolean looting;
    private DropConditions dropCondition;
    // This is only used for DropConditions.NEARBY // TODO This is hardcoded to 50
    private double nearbyRange = 50D;
    private DyeColor groupColor;
    private List<String> receiverMessages;
    private List<String> broadcastedMessages;
    private double nearbyBroadcastRange;

    public Drop(ItemStack itemStack) {
        this(itemStack, 50, false, DropConditions.NEARBY, null, new ArrayList<>(), new ArrayList<>(), -1);
    }

    public Drop(ItemStack itemStack,
                double probability,
                boolean looting,
                DropConditions dropCondition,
                DyeColor groupColor,
                List<String> receiverMessages,
                List<String> broadcastedMessages,
                double nearbyBroadcastRange) {
        this.itemStack = itemStack;
        this.probability = probability;
        this.looting = looting;
        this.dropCondition = dropCondition;
        this.groupColor = groupColor;
        this.receiverMessages = receiverMessages;
        this.broadcastedMessages = broadcastedMessages;
        this.nearbyBroadcastRange = nearbyBroadcastRange;
    }

    public boolean draw() {
        return Math.random() < (probability / 100);
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
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

    public boolean isLooting() {
        return looting;
    }

    public void setLooting(boolean looting) {
        this.looting = looting;
    }

    public DropConditions getDropCondition() {
        return dropCondition;
    }

    public void setDropCondition(DropConditions dropCondition) {
        this.dropCondition = dropCondition;
    }

    public double getNearbyRange() {
        return nearbyRange;
    }

    public DyeColor getGroupColor() {
        return groupColor;
    }

    public void setGroupColor(DyeColor groupColor) {
        this.groupColor = groupColor;
    }

    public List<String> getReceiverMessages() {
        return receiverMessages;
    }

    public void setReceiverMessages(List<String> receiverMessages) {
        this.receiverMessages = receiverMessages;
    }

    public List<String> getBroadcastedMessages() {
        return broadcastedMessages;
    }

    public void setBroadcastedMessages(List<String> broadcastedMessages) {
        this.broadcastedMessages = broadcastedMessages;
    }

    public double getNearbyBroadcastRange() {
        return nearbyBroadcastRange;
    }

    public void setNearbyBroadcastRange(double nearbyBroadcastRange) {
        this.nearbyBroadcastRange = nearbyBroadcastRange;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("item", itemStack);
        data.put("probability", probability);
        data.put("affected-by-looting", looting);
        data.put("drop-condition", dropCondition.name());
        if(dropCondition == DropConditions.NEARBY)
            data.put("nearby-drop-range", nearbyRange);
        if(groupColor != null)
            data.put("group-color", groupColor.name());
        return data;
    }

    public static Drop deserialize(Map<String, Object> data) {
        ItemStack itemStack = (ItemStack) data.get("item");
        double probability = (double) data.get("probability");
        boolean looting = (boolean) data.get("affected-by-looting");
        DropConditions dropCondition = DropConditions.valueOf(data.get("drop-condition").toString());
        // Handle potential null value for "group-color"
        DyeColor groupColor = null;
        if (data.get("group-color") != null) {
            groupColor = DyeColor.valueOf(data.get("group-color").toString());
        }
        // TODO Messages
        return new Drop(itemStack, probability, looting, dropCondition, groupColor, new ArrayList<>(), new ArrayList<>(), -1);
    }
}
