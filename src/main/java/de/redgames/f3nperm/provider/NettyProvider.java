package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.F3NPermPlugin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class NettyProvider implements Provider, Listener {
    private final Map<Player, F3NPermChannelHandler> playerHandlers;

    private F3NPermPlugin plugin;

    public NettyProvider() {
        playerHandlers = new HashMap<>();
    }

    public abstract void sendPacket(Player player);

    public abstract void adjustPacket(Player player, Object packet);

    public abstract Channel getChannel(Player player);

    @Override
    public void update(Player player) {
        sendPacket(player);
    }

    @Override
    public void register(F3NPermPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void unregister(F3NPermPlugin plugin) {
        HandlerList.unregisterAll(this);

        for (F3NPermChannelHandler handler : playerHandlers.values()) {
            handler.unregister();
        }

        playerHandlers.clear();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        F3NPermChannelHandler handler = playerHandlers.get(player);

        if (handler == null) {
            Channel channel = getChannel(player);
            F3NPermChannelHandler newHandler = new F3NPermChannelHandler(player, channel);

            newHandler.register();
            playerHandlers.put(player, newHandler);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        F3NPermChannelHandler handler = playerHandlers.remove(event.getPlayer());

        if (handler != null) {
            handler.unregister();
        }
    }

    public F3NPermPlugin getPlugin() {
        return plugin;
    }

    public final class F3NPermChannelHandler extends ChannelDuplexHandler {
        private static final String NAME = "f3nperm_handler";

        private final Player player;
        private final Channel channel;

        public F3NPermChannelHandler(Player player, Channel channel) {
            this.player = player;
            this.channel = channel;
        }

        public void register() {
            channel.pipeline().addBefore("packet_handler", NAME, this);
        }

        public void unregister() {
            if (channel.pipeline().get(NAME) != null) {
                channel.pipeline().remove(NAME);
            }
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            adjustPacket(player, msg);
            super.write(ctx, msg, promise);
        }
    }
}
