package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Equipment {

    private final CustomMobConfiguration customMobConfiguration;
    private ItemStack mainHand;
    private ItemStack offHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    public Equipment(CustomMobConfiguration customMobConfiguration, ItemStack mainHand, ItemStack offHand, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.customMobConfiguration = customMobConfiguration;
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
        customMobConfiguration.setItemStack(CustomMobConfiguration.EQUIPMENT_MAIN_HAND, mainHand);
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public void setOffHand(ItemStack offHand) {
        this.offHand = offHand;
        customMobConfiguration.setItemStack(CustomMobConfiguration.EQUIPMENT_OFF_HAND, offHand);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        customMobConfiguration.setItemStack(CustomMobConfiguration.EQUIPMENT_HELMET, helmet);
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        customMobConfiguration.setItemStack(CustomMobConfiguration.EQUIPMENT_CHESTPLATE, chestplate);
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        customMobConfiguration.setItemStack(CustomMobConfiguration.EQUIPMENT_LEGGINGS, leggings);
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
        customMobConfiguration.setItemStack(CustomMobConfiguration.EQUIPMENT_BOOTS, boots);
    }

    public void giveEquipments(Entity customMob) {
        if(customMob instanceof LivingEntity) {
            ((LivingEntity) customMob).getEquipment().setItemInMainHand(mainHand, true);
            ((LivingEntity) customMob).getEquipment().setItemInMainHandDropChance(0f);
            ((LivingEntity) customMob).getEquipment().setItemInOffHand(offHand, true);
            ((LivingEntity) customMob).getEquipment().setItemInOffHandDropChance(0f);
            ((LivingEntity) customMob).getEquipment().setHelmet(helmet, true);
            ((LivingEntity) customMob).getEquipment().setHelmetDropChance(0f);
            ((LivingEntity) customMob).getEquipment().setChestplate(chestplate, true);
            ((LivingEntity) customMob).getEquipment().setChestplateDropChance(0f);
            ((LivingEntity) customMob).getEquipment().setLeggings(leggings, true);
            ((LivingEntity) customMob).getEquipment().setLeggingsDropChance(0f);
            ((LivingEntity) customMob).getEquipment().setBoots(boots, true);
            ((LivingEntity) customMob).getEquipment().setBootsDropChance(0f);
        }
    }
}
