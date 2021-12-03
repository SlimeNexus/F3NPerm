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

        ServerVersion version = ServerVersion.fromBukkitVersion();

        if (version == null) {
            logger.severe("Could not read server version! (Too new or too old?)");
            return;
        }

        logger.info("Trying to load plugin for version " + version + "!");

        try {
            if (version.isLowerThan(ServerVersion.v_1_17)) {
                this.reflector = new Reflector_1_8();
            } else if (version.isLowerThan(ServerVersion.v_1_18)) {
                this.reflector = new Reflector_1_17();
            } else {
                this.reflector = new Reflector_1_18();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not load plugin no this version! " +
                    "(Is this server version incompatible?)", e);
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
}
