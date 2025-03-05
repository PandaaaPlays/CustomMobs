package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.custommobs.Messages.Message;
import ca.pandaaa.custommobs.utils.DropConditions;
import org.bukkit.DyeColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drop implements ConfigurationSerializable {

    private ItemStack itemStack;
    // Probability in %
    private double probability;
    private boolean looting;
    private DropConditions dropCondition;
    // This is only used for DropConditions.NEARBY
    private double nearbyRange;
    private DyeColor groupColor;
    private List<Message> messages;
    private final int priority;
    private final boolean deletable;
    private final String customType;

    public Drop(ItemStack itemStack) {
        this(itemStack, 50, false, DropConditions.NEARBY, 50, null, new ArrayList<>(),99, true, "");
    }

    public Drop(ItemStack itemStack, int priority, String customType) {
        this(itemStack, 0, false, DropConditions.DROP, 50, null, new ArrayList<>(), priority, false, customType);
    }

    public Drop(ItemStack itemStack,
                double probability,
                boolean looting,
                DropConditions dropCondition,
                double nearbyRange,
                DyeColor groupColor,
                List<Message> messages,
                int priority,
                boolean deletable,
                String customType) {
        this.itemStack = itemStack;
        this.probability = probability;
        this.looting = looting;
        this.dropCondition = dropCondition;
        this.nearbyRange = nearbyRange;
        this.groupColor = groupColor;
        this.messages = messages;
        this.priority = priority;
        this.deletable = deletable;
        this.customType = customType;
    }

    /**
     * Draws with the probability in %.
     * @return True if the draw was successful.
     */
    public boolean draw() {
        return Math.random() < (probability / 100);
    }

    /**
     * Draws with a looting level affecting the % if it is enabled.
     * If the looting switch is disabled, we do a normal draw.
     * If the looting switch is enabled, the probability is % + (% * looting level) in %.
     * For example, 25% with looting level 2 : (25% + (25% * 2)) = 75%
     * @param lootingLevel The level of looting used for the draw.
     * @return True if the draw was successful.
     */
    public boolean draw(int lootingLevel) {
        if(looting)
            return Math.random() < ((probability + (probability * lootingLevel)) / 100);
        else
            return draw();
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

    public void setNearbyRange(double nearbyRange) {
        this.nearbyRange = nearbyRange;
    }

    public DyeColor getGroupColor() {
        return groupColor;
    }

    public void setGroupColor(DyeColor groupColor) {
        this.groupColor = groupColor;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void editMessages(int index, Message message) {
        if(messages.size() - 1 >= index)
            messages.set(index, message);
        else
            messages.add(message);
    }

    public int getPriority() {
        return priority;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public String getCustomType() {
        return customType;
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
        data.put("priority", priority);
        data.put("deletable", deletable);
        data.put("custom-type", customType);
        data.put("messages", messages);
        return data;
    }

    public static Drop deserialize(Map<String, Object> data) {
        ItemStack itemStack = (ItemStack) data.get("item");
        double probability = (double) data.get("probability");
        boolean looting = (boolean) data.get("affected-by-looting");
        DropConditions dropCondition = DropConditions.valueOf(data.get("drop-condition").toString());
        double nearbyRange = 0D;
        if(dropCondition == DropConditions.NEARBY)
            nearbyRange = (double) data.get("nearby-drop-range");
        int priority = (int) data.get("priority");
        boolean deletable = (boolean) data.get("deletable");
        String customType = (String) data.get("custom-type");
        // Handle potential null value for "group-color"
        DyeColor groupColor = null;
        if (data.get("group-color") != null) {
            groupColor = DyeColor.valueOf(data.get("group-color").toString());
        }
        List<Message> messages = (List<Message>) data.get("messages");
        return new Drop(itemStack, probability, looting, dropCondition, nearbyRange, groupColor, messages, priority, deletable, customType);
    }
}
