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
import java.util.logging.Level;
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
            reflector = new Reflector_1_18();
        } catch (Exception e) {
            try {
                reflector = new Reflector_1_17();
            } catch (Exception e2) {
                try {
                    reflector = new Reflector_1_8();
                } catch (Exception e3) {
                    logger.log(Level.SEVERE, "Could not load plugin for this version... (Server version might be incompatible)", e);
                }
            }
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

    private static abstract class Reflector {
        private final Class<?> entityClass;
        private final Class<?> packetClass;
        private final Class<?> packetPlayOutEntityStatusClass;

        public Reflector() {
            try {
                this.entityClass = Class.forName(getEntityClassName());
                this.packetClass = Class.forName(getPacketClassName());
                this.packetPlayOutEntityStatusClass = Class.forName(getPacketPlayOutEntityStatusClassName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendEntityStatus(Player p) {
            try {
                Object entityPlayer = p.getClass()
                        .getDeclaredMethod(getGetHandleMethodName())
                        .invoke(p);

                Object playerConnection = entityPlayer.getClass()
                        .getDeclaredField(getPlayerConnectionFieldName())
                        .get(entityPlayer);

                Object packet = packetPlayOutEntityStatusClass
                        .getConstructor(entityClass, byte.class)
                        .newInstance(entityPlayer, getStatusByte());

                playerConnection.getClass()
                        .getDeclaredMethod(getSendPacketMethodName(), packetClass)
                        .invoke(playerConnection, packet);
            } catch (Throwable e) {
                throw new RuntimeException("Error while sending entity status 28", e);
            }
        }

        protected abstract byte getStatusByte();
        protected abstract String getEntityClassName();
        protected abstract String getPacketClassName();
        protected abstract String getPacketPlayOutEntityStatusClassName();
        protected abstract String getGetHandleMethodName();
        protected abstract String getPlayerConnectionFieldName();
        protected abstract String getSendPacketMethodName();
    }

    private static class Reflector_1_8 extends Reflector {
        private final String namespace = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        @Override
        protected byte getStatusByte() {
            return 26;
        }

        @Override
        protected String getEntityClassName() {
            return "net.minecraft.server." + namespace + ".Entity";
        }

        @Override
        protected String getPacketClassName() {
            return "net.minecraft.server." + namespace + ".Packet";
        }

        @Override
        protected String getPacketPlayOutEntityStatusClassName() {
            return "net.minecraft.server." + namespace + ".PacketPlayOutEntityStatus";
        }

        @Override
        protected String getGetHandleMethodName() {
            return "getHandle";
        }

        @Override
        protected String getPlayerConnectionFieldName() {
            return "playerConnection";
        }

        @Override
        protected String getSendPacketMethodName() {
            return "sendPacket";
        }
    }

    private static class Reflector_1_17 extends Reflector_1_8 {
        @Override
        protected String getPacketPlayOutEntityStatusClassName() {
            return "net.minecraft.network.protocol.game.PacketPlayOutEntityStatus";
        }

        @Override
        protected String getEntityClassName() {
            return "net.minecraft.world.entity.Entity";
        }

        @Override
        protected String getPacketClassName() {
            return "net.minecraft.network.protocol.Packet";
        }

        @Override
        protected String getPlayerConnectionFieldName() {
            return "b";
        }
    }

    private static class Reflector_1_18 extends Reflector_1_17 {
        @Override
        protected String getSendPacketMethodName() {
            return "a";
        }
    }
}
