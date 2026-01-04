package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.CustomMobs;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RideSystem {
    private final ProtocolManager protocolManager;
    private final Map<UUID, PlayerInput> inputs = new ConcurrentHashMap<>();

    public RideSystem() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        registerListener();
    }

    public void startRiding(Player player, LivingEntity entity) {
        entity.addPassenger(player);
        if (entity instanceof Mob)
            ((Mob) entity).setAware(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || entity.isDead() || !entity.isValid()
                        || !entity.getPassengers().contains(player)) {
                    inputs.remove(player.getUniqueId());
                    if (entity instanceof Mob)
                        ((Mob) entity).setAware(true);
                    cancel();
                    return;
                }

                PlayerInput input = inputs.get(player.getUniqueId());
                if (input == null)
                    return;

                Location location = player.getLocation();
                entity.setRotation(location.getYaw(), 0);

                Vector forwardDir = location.getDirection().setY(0).normalize();
                Vector sideDir = forwardDir.clone().crossProduct(new Vector(0, 1, 0)).normalize();

                Vector velocity = forwardDir.multiply(input.forward).add(sideDir.multiply(-input.sideways));

                if (velocity.lengthSquared() > 0) {
                    double speed = 0.2;
                    if (entity.getAttribute(Attribute.MOVEMENT_SPEED) != null)
                        speed = entity.getAttribute(Attribute.MOVEMENT_SPEED).getValue();
                    velocity.normalize().multiply(speed);
                }

                if (input.jump && entity.isOnGround()) {
                    velocity.setY(0.5);
                } else {
                    velocity.setY(entity.getVelocity().getY());
                }

                entity.setVelocity(velocity);
            }
        }.runTaskTimer(CustomMobs.getPlugin(), 0L, 1L);
    }

    private void registerListener() {
        protocolManager.addPacketListener(new PacketAdapter(
                CustomMobs.getPlugin(),
                PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                Object input = event.getPacket().getModifier().read(0);
                java.lang.reflect.Field[] fields = input.getClass().getDeclaredFields();

                boolean left = false;
                boolean right = false;
                boolean forward = false;
                boolean backward = false;
                boolean jump = false;

                if (fields.length < 5)
                    return;

                try {
                    for (java.lang.reflect.Field field : fields) {
                        field.setAccessible(true);
                        if (field.getType() == boolean.class) {
                            // Possible to see the values by broadcasting the fields.
                            boolean value = field.getBoolean(input);
                            switch (field.getName()) {
                                case "c", "forward" -> forward = value;
                                case "d", "backward" -> backward = value;
                                case "e", "left" -> left = value;
                                case "f", "right" -> right = value;
                                case "g", "jump" -> jump = value;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                inputs.put(player.getUniqueId(), new PlayerInput(
                        (forward ? 1 : 0) - (backward ? 1 : 0),
                        (left ? 1 : 0) - (right ? 1 : 0),
                        jump));
            }
        });
    }

    private static class PlayerInput {
        final float forward;
        final float sideways;
        final boolean jump;

        PlayerInput(float forward, float sideways, boolean jump) {
            this.forward = forward;
            this.sideways = sideways;
            this.jump = jump;
        }
    }
}
