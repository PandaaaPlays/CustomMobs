package ca.pandaaa.custommobs.custommobs.Particles;

import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomMobParticle implements ConfigurationSerializable {

    private Particle particle;
    protected boolean onDeath;
    protected int amount;
    protected double speed;
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;

    public CustomMobParticle(Particle particle, boolean onDeath, int amount, double speed, double offsetX, double offsetY, double offsetZ) {
        this.onDeath = onDeath;
        this.amount = amount;
        this.speed = speed;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.particle = particle;
    }

    public CustomMobParticle(Particle particle) {
        this(particle, false, 10, 1.0, 0.5, 0.5, 0.5);
    }

    public void play(Entity entity) {
        entity.getWorld().spawnParticle(particle, entity.getLocation(), amount, offsetX, offsetY, offsetZ, speed);
    }

    public abstract String getName();

    public abstract Material getMaterial();

    public boolean isOnDeath() {
        return onDeath;
    }

    public void setOnDeath(boolean onDeath) {
        this.onDeath = onDeath;
    }

    public int getAmount() {
        return amount;
    }

    public double getSpeed() {
        return speed;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public void setOffsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
    }

    public ItemStack getItemStack() {
        CustomMobsItem item = new CustomMobsItem(getMaterial());
        item.setName("&6&l" + getName());
        String event = isOnDeath() ? "&c&lDeath" : "&b&lSpawn";
        item.addLore(Utils.applyFormat("&f&l* &eHappens on:&f " + event));
        item.addLore(Utils.applyFormat("&f&l* &aAmount:&f " + amount));
        item.addLore(Utils.applyFormat("&f&l* &aSpeed:&f " + speed));
        item.addLore(Utils.applyFormat("&f&l* &aOffset:&f " + offsetX + ", " + offsetY + ", " + offsetZ));
        item.addLore("");
        item.addLore(Utils.applyFormat("&7&o(( Left-Click to edit this particle ))"));
        item.addLore(Utils.applyFormat("&7&o(( Shift-Left-Click to toggle Spawn/Death ))"));
        item.addLore(Utils.applyFormat("&7&o(( Right-Click to remove this particle ))"));
        return item.getItem();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("class", this.getClass().getSimpleName());
        data.put("on-death", onDeath);
        data.put("amount", amount);
        data.put("speed", speed);
        data.put("offset-x", offsetX);
        data.put("offset-y", offsetY);
        data.put("offset-z", offsetZ);
        return data;
    }

    protected CustomMobParticle(Map<String, Object> data) {
        this.onDeath = (boolean) data.getOrDefault("on-death", false);
        this.amount = (int) data.getOrDefault("amount", 10);
        this.speed = (double) data.getOrDefault("speed", 0.1);
        this.offsetX = (double) data.getOrDefault("offset-x", 0.5);
        this.offsetY = (double) data.getOrDefault("offset-y", 0.5);
        this.offsetZ = (double) data.getOrDefault("offset-z", 0.5);
    }
}
