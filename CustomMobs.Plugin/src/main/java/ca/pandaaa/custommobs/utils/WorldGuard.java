package ca.pandaaa.custommobs.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class WorldGuard {
    public static boolean canBreakBlock(Block block) {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null)
            return true;

        Location location = block.getLocation();
        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldedit.util.Location worldGuardLocation = BukkitAdapter.adapt(location);

        RegionQuery query = container.createQuery();
        return query.testState(worldGuardLocation, null, new StateFlag[]{Flags.BUILD});
    }
}
