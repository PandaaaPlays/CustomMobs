package ca.pandaaa.custommobs.custommobs;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBar {
    private final ConcurrentHashMap<UUID, org.bukkit.boss.BossBar> bossBars = new ConcurrentHashMap<>();

    public org.bukkit.boss.BossBar getBossBar(UUID uuid) {
        if (!bossBars.containsKey(uuid))
            return null;
        return bossBars.get(uuid);
    }

    public void createBossBar(UUID entityId, org.bukkit.boss.BossBar bossBar) {
        bossBars.put(entityId, bossBar);
    }

    public void deleteBossBar(UUID entityId) {
        clearBossBarPlayers(entityId);
        bossBars.remove(entityId);
    }

    public void addPlayerToBossBar(Player player, UUID entityId) {
        if(bossBars.containsKey(entityId))
            bossBars.get(entityId).addPlayer(player);
    }

    public void clearBossBarPlayers(UUID entityId) {
        if(bossBars.containsKey(entityId))
            bossBars.get(entityId).removeAll();
    }

    public void update(UUID entityId, double health, double maxHealth) {
        if(bossBars.containsKey(entityId))
            bossBars.get(entityId).setProgress(Math.max(0, health / maxHealth));
    }
}
