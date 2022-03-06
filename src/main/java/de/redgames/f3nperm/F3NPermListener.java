package de.redgames.f3nperm;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class F3NPermListener implements Listener {
    private final F3NPermPlugin plugin;

    public F3NPermListener(F3NPermPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Double-check the permission state after 10 ticks of delay
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                plugin.getProvider().update(player);
            }
        }, 10L);
    }
}
