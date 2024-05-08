package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Villager extends CustomMobType {
    private final org.bukkit.entity.Villager.Type villagerType;
    private final org.bukkit.entity.Villager.Profession villagerProfession;

    public Villager(org.bukkit.entity.Villager.Type villagerType, org.bukkit.entity.Villager.Profession villagerProfession) {
        this.villagerType = villagerType;
        this.villagerProfession = villagerProfession;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Villager))
            return;

        if(villagerType != null)
            ((org.bukkit.entity.Villager) customMob).setVillagerType(villagerType);
        if(villagerProfession != null)
            ((org.bukkit.entity.Villager) customMob).setProfession(villagerProfession);
    }
}
