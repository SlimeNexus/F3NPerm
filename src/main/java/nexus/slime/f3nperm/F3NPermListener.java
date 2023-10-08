package nexus.slime.f3nperm;

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

        // Since the op package is sent so early in the login process
        // (before the permission plugin is initialized). After everything
        // is initialized, we check again and update the player.
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                plugin.getProvider().update(player);
            }
        }, 10L);
    }
}
