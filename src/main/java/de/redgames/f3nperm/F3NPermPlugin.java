package de.redgames.f3nperm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.logging.Logger;

public final class F3NPermPlugin extends JavaPlugin implements Listener {
    private Reflector reflector;
    private Logger logger;
    private boolean bypassPermissionChecking;

    @Override
    public void onLoad() {
        logger = getLogger();

        bypassPermissionChecking = new File(".bypass_permission_checking").exists();

        try {
            reflector = new Reflector();
        } catch (Throwable e) {
            logger.severe("Could not load plugin for this version... (Server version might be incompatible)");
        }

        logger.info("Plugin loaded!");
    }

    @Override
    public void onEnable() {
        if (reflector != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            logger.info("Plugin enabled!");
        } else {
            logger.severe("Plugin not enabled! No compatible version found while the plugin loaded!");
        }
    }

    @Override
    public void onDisable() {
        logger.info("Plugin disabled!");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        checkF3NPermUpdate(event.getPlayer());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        checkF3NPermUpdate(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        checkF3NPermUpdate(event.getPlayer());
    }

    private void checkF3NPermUpdate(Player player) {
        if (bypassPermissionChecking || player.hasPermission("F3NPerm.use")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    reflector.sendEntityStatus(player);
                }
            }.runTaskLater(this, 10);
        }
    }

    private class Reflector {
        private static final byte STATUS_BYTE = 28;

        private final Class<?> entityStatusPacketClass;
        private final Class<?> playerConnectionClass;
        private final Class<?> entityClass;
        private final Class<?> packetClass;

        private Reflector() throws ClassNotFoundException {
            String namespace = getServer().getClass().getPackage().getName().split("\\.")[3];

            entityStatusPacketClass = Class.forName("net.minecraft.server." + namespace + ".PacketPlayOutEntityStatus");
            playerConnectionClass = Class.forName("net.minecraft.server." + namespace + ".PlayerConnection");
            entityClass = Class.forName("net.minecraft.server." + namespace + ".Entity");
            packetClass = Class.forName("net.minecraft.server." + namespace + ".Packet");
        }

        private void sendEntityStatus(Player p) {
            try {
                Object entityPlayer = p.getClass().getDeclaredMethod("getHandle").invoke(p);
                Object playerConnection = entityPlayer.getClass().getDeclaredField("playerConnection").get(entityPlayer);
                Object packet = entityStatusPacketClass.getConstructor(entityClass, byte.class).newInstance(entityPlayer, STATUS_BYTE);
                playerConnectionClass.getDeclaredMethod("sendPacket", packetClass).invoke(playerConnection, packet);
            } catch (Throwable e) {
                throw new RuntimeException("Error while sending entity status 28", e);
            }
        }
    }
}
