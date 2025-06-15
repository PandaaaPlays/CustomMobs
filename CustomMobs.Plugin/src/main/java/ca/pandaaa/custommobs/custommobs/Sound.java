package ca.pandaaa.custommobs.custommobs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sound implements ConfigurationSerializable {
    private org.bukkit.Sound soundType;
    private double radius;
    private SoundCategory soundCategory;
    private float volume;
    private float pitch;
    private Material material;
    private Boolean onDeath;

    public Sound(org.bukkit.Sound type, double radius, SoundCategory soundCategory, float volume, float pitch, Material material, boolean onDeath) {
        this.soundType = type;
        this.radius = radius;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
        this.material = material;
        this.onDeath = onDeath;
    }

    // SoundType
    public void setSoundType(org.bukkit.Sound soundType){
        this.soundType = soundType;
    }

    public org.bukkit.Sound getSoundType(){
        return soundType;
    }

    // Radius
    public void setRadius(double radius){
        this.radius = radius;
    }

    public double getRadius(){
        return radius;
    }

    // SoundCategory
    public void setCategory(SoundCategory soundCategory){
        this.soundCategory = soundCategory;
    }

    public SoundCategory getCategory(){
        return soundCategory;
    }

    // Volume
    public void setVolume(float volume){
        this.volume = volume;
    }

    public float getVolume(){
        return volume;
    }

    // Pitch
    public void setPitch(float pitch){
        this.pitch = pitch;
    }

    public float getPitch(){
        return pitch;
    }

    // Material
    public void setMaterial(Material material){
        this.material = material;
    }

    public Material getMaterial(){
        return material;
    }

    // OnDeath
    public void setOnDeath(Boolean onDeath){
        this.onDeath = onDeath;
    }

    public boolean getOnDeath(){
        return onDeath;
    }

    public void playSound(Entity customMob) {
        if(soundType != null) {
            if(radius == -1) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), soundType, soundCategory, volume, pitch);
                }
            } else {
                List<Entity> entities = customMob.getNearbyEntities(radius, radius, radius);
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        ((Player) entity).playSound(entity.getLocation(), soundType, soundCategory, volume, pitch);
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("soundType", soundType.name());
        data.put("radius", radius);
        data.put("soundCategory", soundCategory.name());
        data.put("volume", volume);
        data.put("pitch", pitch);
        data.put("material", material.name());
        data.put("onDeath", onDeath);

        return data;
    }

    public static Sound deserialize(Map<String, Object> data) {
        org.bukkit.Sound soundType = org.bukkit.Sound.valueOf(data.get("soundType").toString());
        double radius =(double) data.get("radius");
        SoundCategory soundCategory =SoundCategory.valueOf(data.get("soundCategory").toString());
        float volume =(float)(double) data.get("volume");
        float pitch =(float)(double) data.get("pitch");
        Material material =Material.valueOf(data.get("material").toString());
        boolean onDeath =(boolean) data.get("onDeath");
        return new Sound(soundType,radius, soundCategory, volume,  pitch, material, onDeath);
    }
}
